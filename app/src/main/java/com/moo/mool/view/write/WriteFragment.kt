package com.moo.mool.view.write

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.moo.mool.R
import com.moo.mool.database.TempPost
import com.moo.mool.database.TempPostDatabase
import com.moo.mool.databinding.FragmentWriteBinding
import com.moo.mool.util.*
import com.moo.mool.view.ToastDefaultBlack
import com.moo.mool.view.main.MainActivity
import com.moo.mool.viewmodel.WriteViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class WriteFragment : Fragment() {
    private var _binding: FragmentWriteBinding? = null
    private val binding get() = requireNotNull(_binding)
    private lateinit var writeViewModel: WriteViewModel
    private lateinit var db: TempPostDatabase

    private var productImageUpload = ""
    private var imagePath: String? = null
    private val imageFileTimeFormat = SimpleDateFormat("yyyy-MM-d-HH-mm-ss", Locale.KOREA)
    private val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
    private val permitFileExtensionList = arrayListOf("jpg", "jpeg", "png", "webp")
    private lateinit var fileExtension: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireView().findNavController().popBackStack()
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWriteBinding.inflate(layoutInflater)
        db = TempPostDatabase.getDatabase(requireContext())!!

        writeViewModel = ViewModelProvider(this, WriteViewModel.Factory(requireActivity().application)).get(WriteViewModel::class.java)
        binding.writeViewModel = writeViewModel
        ToolbarDecorationUtil.setToolbarDetail(binding.toolbarWriteBoard, R.string.write, requireActivity(), this)

        textWatcherEditText()
        recallTempPost()

        setAddPhotoClickListener()
        setWriteCompleteClickListener()

        writeViewModel.writeSuccess.observe(viewLifecycleOwner, Observer {
            if (!it) {
                /** ????????? ????????? ????????? ?????? **/
                val mAlertDialog = DefaultDialogUtil.createDialog(requireContext(),
                    R.string.write_save_fail_server_error_dialog_title, R.string.write_save_fail_server_error_dialog_description,
                    true, false, null,null,
                    {activeButtonSave()}, null
                )
                // Dialog ?????? ?????? ??????
                if (mAlertDialog != null && !mAlertDialog.isShowing) {
                    mAlertDialog.show()
                }
            }
        })
        writeViewModel.writeFailedMessage.observe(viewLifecycleOwner, Observer {
            Log.e("Write Error", it.toString())
        })

        binding.btnWriteTempPostsArchive.setOnClickListener {
            navigate(R.id.action_writeFragment_to_tempListWriteFragment)
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_write_toolbar, menu)
        menu?.apply {
            for (index in 0 until this.size()) {
                val item = this.getItem(index)
                val s = SpannableString(item.title)
                s.setSpan(
                    ForegroundColorSpan(
                        resources.getColor(
                            R.color.moomool_pink_ff227c,
                            requireContext()?.theme
                        )
                    ), 0, s.length, 0
                )
                item.title = s
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).onBackPressed()
        } else if (item.itemId == R.id.action_save_temp_post) {
            if (binding.etWriteTitle.text.toString().trim()
                    .isNullOrBlank() || binding.etWriteContent.text.toString().trim()
                    .isNullOrBlank()
            ) {
                ToastDefaultBlack.createToast(
                    requireContext(),
                    getString(R.string.write_temp_save_fail_empty_message)
                )?.show()
            } else {
                val tempPost = TempPost(
                    binding.etWriteTitle.text.toString(),
                    binding.etWriteContent.text.toString(),
                    productImageUpload,
                    fileExtension,
                    timeFormat.format(Date(System.currentTimeMillis())).toString()
                )
                writeViewModel.addTempPost(tempPost)
                ToastDefaultBlack.createToast(
                    requireContext(),
                    getString(R.string.write_temp_save_success_meesage)
                )?.show()
            }
        }
        return true
    }

    private fun textWatcherEditText() {
        binding.etWriteTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().trim().isNullOrBlank() && !binding.etWriteContent.text.toString()
                        .trim().isNullOrBlank()
                ) {
                    activeButtonSave()
                } else {
                    deactiveButtonSave()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        binding.etWriteContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().trim().isNullOrBlank() && !binding.etWriteTitle.text.toString()
                        .trim().isNullOrBlank()
                ) {
                    activeButtonSave()
                } else {
                    deactiveButtonSave()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun recallTempPost() {
        // ?????? ?????? ????????? ?????? ??????
        writeViewModel.getAllTempList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                binding.imgWrtieTempPostsArchiveNew.visibility = View.INVISIBLE
            } else {
                binding.imgWrtieTempPostsArchiveNew.visibility = View.VISIBLE
            }
        })

        // ?????? ????????? ???????????? ????????? ?????? ????????????
        val tempPost = arguments?.getParcelable<TempPost>("currentTempPost")
        binding.etWriteTitle.setText(tempPost?.title)
        binding.etWriteContent.setText(tempPost?.content)
        productImageUpload = tempPost?.productImage.toString()
        fileExtension = tempPost?.productImageFileExtension.toString()
        if (!fileExtension.isNullOrEmpty()) {
            setUploadImagePath(fileExtension)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.imgDetailPost.setImageBitmap(tempPost?.productImage?.toBitmap())
        }
        if (binding.etWriteTitle.text.toString().trim()
                .isNotEmpty() && binding.etWriteContent.text.toString().trim().isNotEmpty()
        ) {
            activeButtonSave()
        } else {
            deactiveButtonSave()
        }
    }

    private fun setAddPhotoClickListener() {
        // ????????? ?????? ?????????, ?????? ?????? ??? ???????????? ?????? ??? ?????? ????????? ?????? ??? ??????
        binding.btnWriteAddPhoto.setOnClickListener {
            requestOpenGallery.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setWriteCompleteClickListener() {
        // ??? ?????? ?????? ????????? ????????? ??? ???????????? ?????? ?????? ???????????? ??????
        binding.fabWriteToComplete.setOnClickListener { view ->
            if (EdittextCount.getGraphemeCount(binding.etWriteTitle.text.toString()) > 20) {
                ToastDefaultBlack.createToast(requireContext(), getString(R.string.write_save_fail_toast_length_exceed_title))?.show()
            } else if (EdittextCount.getGraphemeCount(binding.etWriteContent.text.toString()) > 1000) {
                ToastDefaultBlack.createToast(requireContext(), getString(R.string.write_save_fail_toast_length_exceed_content))?.show()
            } else {
                writeViewModel.uploadPost(
                    binding.etWriteTitle.text.toString(),
                    binding.etWriteContent.text.toString(),
                    bitmapToFile(productImageUpload.toBitmap(), imagePath)
                )
                deactiveButtonSave()
            }
            writeViewModel.writePostId.observe(viewLifecycleOwner, Observer {
                navigateWithData(WriteFragmentDirections.actionWriteFragmentToVoteFragment(it.toInt()))
            })
        }
    }

    private val requestOpenGallery =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                if (it.value == false) {
                    return@registerForActivityResult
                }
            }
            openGallery()
        }

    private fun openGallery() {
        // ACTION PICK ?????????, intent type?????? ????????? ????????? ???????????? MediaStore?????? ???????????? ???????????? ?????? ??? ????????? ??? ?????? ??? ??????
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        requestActivity.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val requestActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val data: Intent? = activityResult.data
                // ????????? ??????????????? ????????? ?????????, data??? data???????????? ?????? ???????????? Uri ??????
                val uri = data?.data!!
                // Glide??? ???????????? uri??? ???????????? ????????? ???, Glide??? ????????? Uri -> Bitmap ??????
                // BitmapToString ??????????????? ???????????? Bitmap -> String?????? ???????????? product_image_upload??? ??????

                // ????????? ????????? ??????, ?????? ???????????? ?????? ??????
                fileExtension =
                    requireContext().contentResolver.getType(uri).toString().split("/")[1]
                if (permitFileExtensionList.contains(fileExtension)) {
                    setUploadImagePath(fileExtension)
                    Glide.with(requireContext())
                        .load(uri)
                        .listener(setBitmapListener()) // ????????? ?????? String ????????? ??????
                        .centerCrop()
                        .into(binding.imgDetailPost)
                } else {
                    val mAlertDialog = DefaultDialogUtil.createDialog(requireContext(),
                        R.string.write_save_fail_format_dialog_title, R.string.write_save_fail_format_dialog_description,
                        true, false, null,null,
                        null, null
                    )
                    // Dialog ?????? ?????? ??????
                    if (mAlertDialog != null && !mAlertDialog.isShowing) {
                        mAlertDialog.show()
                    }
                }
            }
        }

    private fun setBitmapListener(): RequestListener<Drawable>? {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (resource is BitmapDrawable) {
                    val bitmap = resource.bitmap
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        productImageUpload = bitmap.toBase64String()
                    }
                }
                return false
            }
        }
    }

    private fun setUploadImagePath(fileExtension: String) {
        // uri??? ????????? ????????? ???????????? ????????? ????????? ????????? ????????? ??? ?????? ?????? ??????????????? ??????,
        // ?????? ????????? ????????? ?????? ??????
        val fileName = imageFileTimeFormat.format(Date(System.currentTimeMillis()))
            .toString() + "." + fileExtension
        val cacheDir = requireContext().cacheDir.toString()
        imagePath = "$cacheDir/$fileName"
    }

    fun bitmapToFile(bitmap: Bitmap?, path: String?): File? {
        if (bitmap == null || path == null) {
            return null
        }
        var file = File(path)
        var out: OutputStream? = null
        try {
            file.createNewFile()
            out = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
        } finally {
            out?.close()
        }
        return file
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Bitmap.toBase64String(): String {
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG, 70, this)
            return Base64.getEncoder().encodeToString(toByteArray())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun String.toBitmap(): Bitmap? {
        Base64.getDecoder().decode(this).apply {
            return BitmapFactory.decodeByteArray(this, 0, size)
        }
    }

    private fun activeButtonSave() {
        binding.fabWriteToComplete.isEnabled = true
        binding.fabWriteToComplete.backgroundTintList = requireContext().resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme)
    }

    private fun deactiveButtonSave() {
        binding.fabWriteToComplete.isEnabled = false
        binding.fabWriteToComplete.backgroundTintList = requireContext().resources.getColorStateList(R.color.bluegray500_878C96, context?.theme)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
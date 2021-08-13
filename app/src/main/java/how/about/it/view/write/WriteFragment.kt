package how.about.it.view.write

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import how.about.it.R
import how.about.it.database.TempPost
import how.about.it.database.TempPostDatabase
import how.about.it.databinding.FragmentWriteBinding
import how.about.it.view.ToastDefaultBlack
import how.about.it.view.main.MainActivity
import how.about.it.viewmodel.WriteViewModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class WriteFragment : Fragment() {
    private var _binding: FragmentWriteBinding? = null
    private val binding get() = requireNotNull(_binding)
    private lateinit var writeViewModel: WriteViewModel
    private lateinit var db : TempPostDatabase
    private var product_image_upload : String = ""
    private val t_dateFormat = SimpleDateFormat("yyyy-MM-dd kk:mm:ss E", Locale("ko", "KR"))

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWriteBinding.inflate(layoutInflater)
        db = TempPostDatabase.getDatabase(requireContext())!!

        writeViewModel = ViewModelProvider(this, WriteViewModel.Factory(Application())).get(WriteViewModel::class.java)
        binding.writeViewModel = writeViewModel

        binding.toolbarWriteBoard.tvToolbarTitle.text = "글쓰기"
        (activity as MainActivity).setSupportActionBar(binding.toolbarWriteBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        showBackButton()

        // 임시 저장한 게시글을 불러온 경우 채워넣음
        val tempPost = arguments?.getParcelable<TempPost>("currentTempPost")
        binding.etWriteTitle.setText(tempPost?.title)
        binding.etWriteContent.setText(tempPost?.content)
        product_image_upload = tempPost?.product_image.toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.imgDetailPost.setImageBitmap(tempPost?.product_image?.toBitmap())
        }

        binding.etWriteTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { afterTextChanged(s as Editable?) }
            override fun afterTextChanged(s: Editable?) {
                if(!s.toString().trim().isNullOrBlank() && !binding.etWriteContent.text.toString().trim().isNullOrBlank() ) {
                    activeButtonSave()
                } else {
                    deactiveButtonSave()
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })

        binding.etWriteContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { afterTextChanged(s as Editable?) }
            override fun afterTextChanged(s: Editable?) {
                if(!s.toString().trim().isNullOrBlank() && !binding.etWriteTitle.text.toString().trim().isNullOrBlank() ) {
                    activeButtonSave()
                } else {
                    deactiveButtonSave()
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })

        // 이미지 추가 클릭시, 권한 확인 후 갤러리를 띄운 뒤 해당 이미지 출력 및 저장
        binding.btnWriteAddPhoto.setOnClickListener {
            requestOpenGallery.launch(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            )
        }

        // 글 작성 완료 버튼을 눌렀을 때 서버와의 연동 확인 방식으로 변경
        binding.fabWriteToComplete.setOnClickListener {
            /*
            if (false/** 서버와 연동에 실패한 경우 **/) {
                // TODO : Dialog 띄우기 코드 개선 필요
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                val mAlertDialog = mBuilder.show()

                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.write_save_fail_server_error_dialog_title)
                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.write_save_fail_server_error_dialog_description)

                // Dialog 확인, 취소버튼 설정
                val confirmButton = mDialogView.findViewById<Button>(R.id.btn_dialog_confirm)
                mDialogView.findViewById<ConstraintLayout>(R.id.layout_dialog_cancel).visibility = View.GONE
                // Dialog 확인 버튼을 클릭 한 경우
                confirmButton.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            } else{ // TODO: 서버에 게시글 등록하는 코드

            } */
            ToastDefaultBlack.createToast(requireContext(), "저장 버튼 클릭")?.show()
        }

        binding.btnWriteTempPostsArchive.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_writeFragment_to_tempListWriteFragment)
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_write_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).onBackPressed()
        } else if(item.itemId == R.id.action_save_temp_post) {
            if(binding.etWriteTitle.text.toString().trim().isNullOrBlank() || binding.etWriteContent.text.toString().trim().isNullOrBlank()) {
                ToastDefaultBlack.createToast(requireContext(), getString(R.string.write_temp_save_fail_empty_message))?.show()
            } else {
                val tempPost = TempPost(binding.etWriteTitle.text.toString(), binding.etWriteContent.text.toString(), product_image_upload, t_dateFormat.format(Date(System.currentTimeMillis())).toString())
                writeViewModel.addTempPost(tempPost)
                ToastDefaultBlack.createToast(requireContext(), getString(R.string.write_temp_save_success_meesage))?.show()
            }
        }
        return true
    }

    val requestOpenGallery = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach{
            if(it.value == false){
                return@registerForActivityResult
            }
        }
        openGallery()
    }

    private fun openGallery() {
        // ACTION PICK 사용시, intent type에서 설정한 종류의 데이터를 MediaStore에서 불러와서 목록으로 나열 후 선택할 수 있는 앱 실행
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        requestActivity.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if(activityResult.resultCode == Activity.RESULT_OK) {
            val data: Intent? = activityResult.data
            // 호출된 갤러리에서 이미지 선택시, data의 data속성으로 해당 이미지의 Uri 전달
            val uri = data?.data!!
            // Glide를 사용하여 uri을 전달하여 보여준 뒤, Glide를 사용해 Uri -> Bitmap 변환
            // BitmapToString 확장함수를 사용하여 Bitmap -> String으로 변환하여 product_image_upload에 저장
            Glide.with(requireContext())
                .load(uri)
                .listener(setBitmapListener())
                .centerCrop()
                .into(binding.imgDetailPost)
        }
    }

    private fun setBitmapListener() : RequestListener<Drawable>? {
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
                if(resource is BitmapDrawable) {
                    val bitmap = resource.bitmap
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        product_image_upload = bitmap.toBase64String()
                    }
                }
                return false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Bitmap.toBase64String():String{
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG,70,this)
            return Base64.getEncoder().encodeToString(toByteArray())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun String.toBitmap(): Bitmap?{
        Base64.getDecoder().decode(this).apply {
            return BitmapFactory.decodeByteArray(this,0,size)
        }
    }

    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.setHasOptionsMenu(true)
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
package how.about.it.view.networkerror

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import how.about.it.databinding.FragmentNetworkErrorBinding

class NetworkErrorFragment : Fragment() {
    private var _binding: FragmentNetworkErrorBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNetworkErrorBinding.inflate(inflater, container, false)
        registerDefaultNetworkCallback()
        return binding.root
    }

    private fun registerDefaultNetworkCallback() {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        val networkCallBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectivityManager.unregisterNetworkCallback(this)
                popBackStack()
            }

            override fun onLost(network: Network) {
            }
        }
        connectivityManager.registerNetworkCallback(networkRequest, networkCallBack)
    }

    private fun popBackStack() {
        requireActivity().runOnUiThread {
            requireView().findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

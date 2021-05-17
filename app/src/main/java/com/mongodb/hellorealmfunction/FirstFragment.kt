package com.mongodb.hellorealmfunction

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mongodb.hellorealmfunction.databinding.FragmentFirstBinding
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.functions.Functions

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        Realm.init(requireContext())
        val appID = "realmfunction-zugql" // replace this with your App ID
        val app: App = App(AppConfiguration.Builder(appID).build())
        val anonymousCredentials: Credentials = Credentials.anonymous()
        app.loginAsync(anonymousCredentials) {
            if (it.isSuccess) {
                val user: User? = app.currentUser()
                val functionsManager: Functions = app.getFunctions(user)
                val args: List<Int> = listOf(1, 2)
                functionsManager.callFunctionAsync("sum", args, Integer::class.java) { result ->
                    if (result.isSuccess) {
                        Log.v("EXAMPLE", "Sum value: ${result.get()}")
                    } else {
                        Log.e("EXAMPLE", "failed to call sum function with: " + result.error)
                    }
                }
            } else {
                Log.e("EXAMPLE", "Error logging into the Realm app. Make sure that anonymous authentication is enabled. Error: " + it.error)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.eathemeat.easytimer.ui.user.ui.user.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.databinding.FragmentRegisterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    protected lateinit var binding:FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        binding.registerMb.setOnClickListener {
            var passWord = binding.passwordTiet.text.toString()
            var passPort = binding.passportTiet.text.toString()
            var phone = binding.phoneTiet.text.toString()

1
            if (TextUtils.isEmpty(passPort) || TextUtils.isEmpty(passWord) || TextUtils.isEmpty(phone)) {

            }

            // TODO: register

        }
        return binding.root
    }








}
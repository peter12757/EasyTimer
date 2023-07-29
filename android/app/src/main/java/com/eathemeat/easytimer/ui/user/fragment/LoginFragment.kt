package com.eathemeat.easytimer.ui.user.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eathemeat.easytimer.databinding.FragmentLoginBinding

const val TAG = "LoginFragment"
class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    private lateinit var binding:FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.loginMb.setOnClickListener {
            var passport = binding.passportTiet.text.toString()
            var password = binding.passwordTiet.text.toString()
            var agree = binding.agreeMcb.isChecked
            // TODO: login 
        }
        binding.agreeMtv.setOnClickListener {
            // TODO: 用户协议
        }
        binding.loginProblemMtv.setOnClickListener{
            // TODO: 登录问题 
        }
        return binding.root
    }

}
package com.eathemeat.easytimer.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.eathemeat.easytimer.data.DataManager
import com.eathemeat.easytimer.data.Task
import com.eathemeat.easytimer.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar
import java.util.UUID

class AddFragment : Fragment() {

    companion object {
        fun newInstance() = AddFragment()
    }

    private lateinit var viewModel: AddViewModel

    private lateinit var binding:FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(AddViewModel::class.java)
        binding.btnAdd.setOnClickListener {
            if (TextUtils.isEmpty(binding.edtxtName.text)) {
                Toast.makeText(context, "名字不能为空", Toast.LENGTH_LONG).show()

                Snackbar.make(binding.btnAdd,"名字不能为空！",Snackbar.LENGTH_LONG).setAction("确认") {
                }.show()
                return@setOnClickListener
            }
            DataManager.sIntance.add(Task(Task.generateUid(),"${binding.edtxtName.text}","${binding.edtxtDesc.text}",
                mutableListOf()

            ))
            Navigation.findNavController(binding.root).popBackStack()
        }
        return binding.root
    }



}
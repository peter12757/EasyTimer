package com.eathemeat.easytimer.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.eathemeat.easytimer.data.DataManager
import com.eathemeat.easytimer.data.Task
import com.eathemeat.easytimer.databinding.FragmentAddBinding
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
        binding.mbtnAdd.setOnClickListener {
            DataManager.sIntance.add(Task(Task.generateUid(),"${binding.edtxtName.text}","${binding.edtxtDesc.text}",
                mutableListOf()

            ))
            Navigation.findNavController(binding.root).popBackStack()
        }
        return binding.root
    }



}
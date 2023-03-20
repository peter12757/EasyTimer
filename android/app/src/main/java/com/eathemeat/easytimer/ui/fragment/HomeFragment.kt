package com.eathemeat.easytimer.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.databinding.ActivityHomeBinding
import com.eathemeat.easytimer.databinding.FragmentHomeBinding
import com.eathemeat.easytimer.databinding.ItemHomeListBinding

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    private lateinit var mLayoutManager:LinearLayoutManager
    private lateinit var mAdapter:HomeListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        //recycleview
        mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        mAdapter = HomeListAdapter()
        binding.rvHomeList.layoutManager = mLayoutManager
        binding.rvHomeList.adapter = mAdapter
        return binding.root
    }

    class ViewHolder(itemBinding: ItemHomeListBinding,val type:Int) : RecyclerView.ViewHolder(itemBinding.root) {

    }

    class HomeListAdapter: RecyclerView.Adapter<ViewHolder>() {



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var itemBinding = ItemHomeListBinding.inflate(LayoutInflater.from(parent.context))
            return ViewHolder(itemBinding,viewType)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

    }


}
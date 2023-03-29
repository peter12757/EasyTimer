package com.eathemeat.easytimer.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.data.DataManager
import com.eathemeat.easytimer.data.Task
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

    class ViewHolder(val itemBinding: ItemHomeListBinding,val type:Int) : RecyclerView.ViewHolder(itemBinding.root) {

    }


    class HomeListAdapter: RecyclerView.Adapter<ViewHolder>() {
        var mVirualData = Task(-1,"","", mutableListOf())


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var itemBinding = ItemHomeListBinding.inflate(LayoutInflater.from(parent.context))
            return ViewHolder(itemBinding,viewType)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                var task = DataManager.sIntance.mTaskList[position]
                // TODO: 跳转详情页
            }
            if (position == DataManager.sIntance.mTaskList.size) {
                //virual
                holder.itemBinding.clNormal.visibility = View.GONE
                holder.itemBinding.clVirual.visibility = View.VISIBLE
                holder.itemBinding.btnAdd.setOnClickListener {
//                    Navigation.findNavController(holder.itemView).navigate()
                }

            }else {
                var task = DataManager.sIntance.mTaskList[position]
                holder.itemBinding.clNormal.visibility = View.VISIBLE
                holder.itemBinding.clVirual.visibility = View.GONE
                holder.itemBinding.txtName.text = task.name
                holder.itemBinding.txtDesc.text = task.desc
                holder.itemBinding.btnDel.setOnClickListener {
                    DataManager.sIntance.del(DataManager.sIntance.mTaskList[position])
                    notifyDataSetChanged()
                }
            }

        }


        override fun getItemCount(): Int {
            return DataManager.sIntance.mTaskList.size+1 //任务列表加上一个虚拟任务
        }

    }


}
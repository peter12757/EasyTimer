package com.eathemeat.easytimer.ui.fragment

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.eathemeat.easytimer.R
import com.eathemeat.easytimer.data.DataManager
import com.eathemeat.easytimer.data.Task
import com.eathemeat.easytimer.databinding.ActivityHomeBinding
import com.eathemeat.easytimer.databinding.FragmentHomeBinding
import com.eathemeat.easytimer.databinding.ItemHomeListBinding
import com.eathemeat.easytimer.ui.fragment.DetailFragment.Companion.KEY_POS

val TAG = HomeFragment::class.java.simpleName

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

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
        mAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView() called")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach() called with: context = $context")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach() called")
    }

    class ViewHolder(val itemBinding: ItemHomeListBinding,val type:Int) : RecyclerView.ViewHolder(itemBinding.root) {

    }


    class HomeListAdapter: RecyclerView.Adapter<ViewHolder>() {
        var mVirualData = Task(-1,"","", mutableListOf())


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var itemBinding = ItemHomeListBinding.inflate(LayoutInflater.from(parent.context))
            itemBinding.root.layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
            return ViewHolder(itemBinding,viewType)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var task = mVirualData
            if (position !=  DataManager.sIntance.mTaskList.size)
                task = DataManager.sIntance.mTaskList[position]
            Log.d(TAG, "onBindViewHolder() called with: task = $task, position = $position")
            holder.itemView.tag = task
            holder.itemView.setOnClickListener {
                // TODO: 跳转详情页
                Log.d(TAG, "onBindViewHolder() called")
                if (holder.itemView.tag == mVirualData) return@setOnClickListener
                Navigation.findNavController(holder.itemView).navigate(R.id.detailFragment,
                    bundleOf(Pair(KEY_POS,position))
                )
            }
            if (task == mVirualData) {
                //virual
                holder.itemBinding.clNormal.visibility = View.INVISIBLE
                holder.itemBinding.clVirual.visibility = View.VISIBLE
                holder.itemBinding.btnAdd.setOnClickListener {
                    Navigation.findNavController(holder.itemView).navigate(R.id.addFragment)
                }
            } else {
                holder.itemBinding.clNormal.visibility = View.VISIBLE
                holder.itemBinding.clVirual.visibility = View.INVISIBLE
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
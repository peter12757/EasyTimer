package com.eathemeat.easytimer.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eathemeat.easytimer.data.DataManager
import com.eathemeat.easytimer.data.Task
import com.eathemeat.easytimer.data.TimeSegment
import com.eathemeat.easytimer.databinding.FragmentDetailBinding
import com.eathemeat.easytimer.databinding.ItemSegmentListBinding
import com.eathemeat.easytimer.util.TimeUtil

class DetailFragment() : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()

        const val KEY_POS = "pos"
    }

    private lateinit var viewModel: DetailViewModel
    private lateinit var binding:FragmentDetailBinding

    lateinit var mTask: Task
    lateinit var mLayoutManager:LinearLayoutManager
    lateinit var mAdapter:SegmentAdapter
    var mSegment:TimeSegment? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)
        binding.txtName.text = mTask.name
        binding.txtDesc.text = mTask.desc

        //list
        mAdapter = SegmentAdapter(mTask)
        mLayoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        binding.rvSegment.layoutManager = mLayoutManager
        binding.rvSegment.adapter = mAdapter
        binding.btnStart.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mSegment = TimeSegment(SystemClock.elapsedRealtime(),0,"")
                mSegment!!.start()
            } else {
                mSegment?.let {
                    it.end()
                    mTask.timeList.add(it)
                    DataManager.sIntance.notifyDataChanged(mTask)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        return binding.root
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        Log.d(TAG, "setArguments() called with: args = $args")
        var pos = args!!.getInt(KEY_POS)
        mTask = DataManager.sIntance.mTaskList[pos]

    }

    class SegmentViewHolder(val binding: ItemSegmentListBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    class SegmentAdapter(var task:Task): RecyclerView.Adapter<SegmentViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SegmentViewHolder {
            var itemBinding = ItemSegmentListBinding.inflate(LayoutInflater.from(parent.context))
            itemBinding.root.layoutParams = ViewGroup.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
            return SegmentViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: SegmentViewHolder, position: Int) {
            var segment = task.timeList[position]
            holder.binding.txtStartTime.text = TimeUtil.Time2String(segment.startTime)
            holder.binding.txtEndTime.text = TimeUtil.Time2String(segment.endTime)
        }

        override fun getItemCount(): Int {
            return task.timeList.size
        }

    }


}
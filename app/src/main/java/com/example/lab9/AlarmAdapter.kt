package com.example.lab9

import Alarm
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlarmAdapter(private val alarms: MutableList<Alarm>) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    private var selectedPosition: Int = -1

    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.alarm_time)
        val dateTextView: TextView = itemView.findViewById(R.id.alarm_date)
        val alarmSwitch: ImageView = itemView.findViewById(R.id.alarm_switch)

        init {
            itemView.setOnLongClickListener {
                selectedPosition = adapterPosition // Store the selected position on long click
                notifyItemChanged(adapterPosition)  // Refresh the item to trigger context menu
                false // Return false to allow context menu to appear
            }

            alarmSwitch.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val alarm = alarms[position]
                    alarm.isChecked = !alarm.isChecked
                    alarmSwitch.setImageResource(
                        if (alarm.isChecked) R.drawable.onbutton else R.drawable.offbutton
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.timeTextView.text = alarm.time
        holder.dateTextView.text = alarm.date
        holder.alarmSwitch.setImageResource(
            if (alarm.isChecked) R.drawable.onbutton else R.drawable.offbutton
        )
    }

    override fun getItemCount(): Int = alarms.size

    fun getSelectedAlarm(): Alarm? {
        return if (selectedPosition >= 0 && selectedPosition < alarms.size) {
            alarms[selectedPosition]
        } else {
            null
        }
    }

    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    fun addAlarm(alarm: Alarm) {
        alarms.add(alarm)
        notifyItemInserted(alarms.size - 1)
    }

    fun removeAlarm(position: Int) {
        if (position >= 0 && position < alarms.size) {
            alarms.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateAlarm(position: Int, alarm: Alarm) {
        if (position >= 0 && position < alarms.size) {
            alarms[position] = alarm
            notifyItemChanged(position)
        }
    }

    fun clearAlarms() {
        alarms.clear()
        notifyDataSetChanged() // Cập nhật lại giao diện RecyclerView
    }
    fun isEmpty(): Boolean {
        return alarms.isEmpty()
    }

}


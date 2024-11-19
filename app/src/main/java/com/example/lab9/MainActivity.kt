package com.example.lab9

import Alarm
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var recyclerView: RecyclerView
    private var selectedAlarm: Alarm? = null
    private var selectedPosition: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Đăng ký BroadcastReceiver để theo dõi thay đổi kết nối mạng
        val networkReceiver = NetworkChangeReceiver()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, intentFilter)

        recyclerView = findViewById(R.id.recyclerView)
        alarmAdapter = AlarmAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = alarmAdapter

        registerForContextMenu(recyclerView)

        // Xử lý xóa tất cả với xác nhận
        val deleteAllTextView = findViewById<TextView>(R.id.deleteAll)
        deleteAllTextView.setOnClickListener {
            if (alarmAdapter.isEmpty()) {
                Toast.makeText(this, "Chưa có dữ liệu để xóa!", Toast.LENGTH_SHORT).show()
            } else {
                showDeleteAllConfirmationDialog()
            }
        }

        val custom = findViewById<LinearLayout>(R.id.custom)
        custom.setOnClickListener{
            val intent = Intent(this, CustomActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hủy đăng ký BroadcastReceiver khi Activity bị hủy
        val networkReceiver = NetworkChangeReceiver()
        unregisterReceiver(networkReceiver)
    }

    // Các phương thức khác của bạn như trước...



    // Hàm hiển thị dialog xác nhận
    private fun showDeleteAllConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa tất cả")
            .setMessage("Bạn có chắc chắn muốn xóa tất cả thời gian đã thêm không?")
            .setPositiveButton("Đồng ý") { _, _ ->
                alarmAdapter.clearAlarms()
                Toast.makeText(this, "Đã xóa tất cả!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null) // Đóng dialog nếu nhấn Hủy
            .show()
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                Toast.makeText(this, "Tìm kiếm", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.clock -> {
                showDateTimePicker()
                true
            }
            R.id.setting -> {
                Toast.makeText(this, "Cài đặt", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.shared -> {
                Toast.makeText(this, "Chia sẻ", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            showTimePicker(selectedYear, selectedMonth, selectedDay)
        }, year, month, day).show()
    }

    private fun showTimePicker(year: Int, month: Int, day: Int) {
        TimePickerDialog(this, { _, hourOfDay, minute ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val timeString = timeFormat.format(calendar.time)

            val date = String.format("%02d/%02d/%04d", day, month + 1, year)
            alarmAdapter.addAlarm(Alarm(timeString, date))
        }, 12, 0, false).show()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v?.id == R.id.recyclerView) {
            menuInflater.inflate(R.menu.context_menu, menu)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        selectedPosition = alarmAdapter.getSelectedPosition()
        selectedAlarm = alarmAdapter.getSelectedAlarm()

        if (selectedPosition == -1 || selectedAlarm == null) return super.onContextItemSelected(item)

        return when (item.itemId) {
            R.id.edit -> {
                showEditDateTimePicker(selectedPosition, selectedAlarm!!)
                true
            }
            R.id.delete -> {
                alarmAdapter.removeAlarm(selectedPosition)
                Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }


    private fun showEditDateTimePicker(position: Int, alarm: Alarm) {
        val calendar = Calendar.getInstance()

        // Hiển thị DatePickerDialog
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // Hiển thị TimePickerDialog
            TimePickerDialog(this, { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                // Cập nhật thời gian mới
                val newTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)
                val newDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)

                alarm.time = newTime
                alarm.date = newDate
                alarmAdapter.updateAlarm(position, alarm)

                Toast.makeText(this, "Chỉnh sửa thành công!", Toast.LENGTH_SHORT).show()
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }


}


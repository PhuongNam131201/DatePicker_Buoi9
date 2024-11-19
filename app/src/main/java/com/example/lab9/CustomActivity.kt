package com.example.lab9

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class CustomActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var btnShowDialog: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)

        tvName = findViewById(R.id.tvName)
        btnShowDialog = findViewById(R.id.btnShowDialog)

        btnShowDialog.setOnClickListener {
            showCustomDialog()
        }
        val time = findViewById<LinearLayout>(R.id.daytime)
        time.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showCustomDialog() {
        // Inflate layout tùy chỉnh cho AlertDialog
        val dialogView = layoutInflater.inflate(R.layout.dialog_name_input, null)

        // Tạo AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Khởi tạo các thành phần trong dialog
        val etName = dialogView.findViewById<EditText>(R.id.etName)

        // Nút Lưu
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Lưu") { _, _ ->
            val name = etName.text.toString()
            if (name.isNotEmpty()) {
                tvName.text = name
                Toast.makeText(this, "Tên đã được lưu!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show()
            }
        }

        // Nút Hủy
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hủy") { _, _ ->
            dialog.dismiss()
        }

        dialog.show()
    }
}

package com.example.lab9

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.widget.Toast

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork
        val capabilities = cm.getNetworkCapabilities(activeNetwork)

        if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            // Mạng có kết nối
            Toast.makeText(context, "Đã kết nối mạng", Toast.LENGTH_SHORT).show()
        } else {
            // Không có kết nối
            Toast.makeText(context, "Không có kết nối mạng", Toast.LENGTH_SHORT).show()
        }
    }
}

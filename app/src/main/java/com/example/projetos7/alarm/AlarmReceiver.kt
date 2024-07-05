package com.example.projetos7.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.projetos7.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val opc = intent.getStringExtra("opc")
        val isOn = intent.getBooleanExtra("isOn", false)
        val api = Api()
        CoroutineScope(Dispatchers.IO).launch {
            api.ligarLed("led_$opc",isOn)
        }
        println("Funcionou")
    }
}
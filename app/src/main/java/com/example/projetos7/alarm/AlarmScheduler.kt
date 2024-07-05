package com.example.projetos7.alarm

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.projetos7.R
import java.util.Calendar

class AlarmScheduler(private val context: Context) {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleAlarm(
        hour: Int,
        minute: Int,
        opc: String,
        isOn: Boolean,
        code: Int,
        functionToExecute: () -> Unit
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("opc", opc)
        intent.putExtra("isOn", isOn)
        val pendingIntent =
            PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_IMMUTABLE)

        // Configurando o alarme para disparar no horário especificado
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        val sharedPreferences = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            val id = opc[0]
            if (isOn) {
                putInt("item_${id}_start_hour", hour)
                putInt("item_${id}_start_minute", minute)
            } else {
                putInt("item_${id}_end_hour", hour)
                putInt("item_${id}_end_minute", minute)
            }
            apply()
        }

        sendNotification("Alarme Agendado", "Alarme agendado para $hour:$minute.")
    }

    @SuppressLint("RestrictedApi")
    private fun sendNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, "channelId")
            .setSmallIcon(R.drawable.lampada)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(0, notification)
    }

    fun getScheduledAlarmTimes(context: Context, itemId: Int): Pair<Pair<Int, Int>?, Pair<Int, Int>?> {
        val sharedPreferences = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)

        val startHour = sharedPreferences.getInt("item_${itemId}_start_hour", -1)
        val startMinute = sharedPreferences.getInt("item_${itemId}_start_minute", -1)
        val endHour = sharedPreferences.getInt("item_${itemId}_end_hour", -1)
        val endMinute = sharedPreferences.getInt("item_${itemId}_end_minute", -1)

        val startTime = if (startHour != -1 && startMinute != -1) Pair(startHour, startMinute) else null
        val endTime = if (endHour != -1 && endMinute != -1) Pair(endHour, endMinute) else null

        return Pair(startTime, endTime)
    }
    fun getScheduledAlarmTimesFormatted(context: Context, itemId: Int): Pair<String?, String?> {
        val (startTime, endTime) = getScheduledAlarmTimes(context, itemId)

        val startFormatted = startTime?.let { (hour, minute) -> formatTime(hour, minute) }
        val endFormatted = endTime?.let { (hour, minute) -> formatTime(hour, minute) }

        return Pair(startFormatted, endFormatted)
    }

    @SuppressLint("DefaultLocale")
    fun formatTime(hour: Int, minute: Int): String {
        return String.format("%02d:%02d:00", hour, minute)
    }

}

package com.example.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.receiver.DailyReminderReceiver
import java.util.Calendar

object NotificationHelper {

  const val CHANNEL_ID = "matemexico_daily_reminders"
  const val NOTIFICATION_ID = 1001
  private const val PREFS_NAME = "matemexico_reminders_prefs"
  private const val KEY_ENABLED = "reminder_enabled"
  private const val KEY_HOUR = "reminder_hour"
  private const val KEY_MINUTE = "reminder_minute"
  private const val ALARM_REQ_CODE = 9001

  fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val name = "Recordatorios Diarios"
      val descriptionText = "Recordatorio para mantener tu racha de matemáticas"
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
        description = descriptionText
      }
      val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

  fun sendDailyReminder(context: Context, mascotName: String = "Axo el Ajolote") {
    createNotificationChannel(context)

    val intent = Intent(context, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(
      context, 0, intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.ic_dialog_info)
      .setContentTitle("¡Hora de tu reto matemático en MateMéxico! 🇲🇽")
      .setContentText("$mascotName te espera para practicar y ganar medallas tradicionales.")
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setContentIntent(pendingIntent)
      .setAutoCancel(true)
      .build()

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(NOTIFICATION_ID, notification)
  }

  fun scheduleDailyReminder(context: Context, hour: Int, minute: Int, mascotName: String = "Axo el Ajolote") {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

    val intent = Intent(context, DailyReminderReceiver::class.java).apply {
      putExtra("mascot_name", mascotName)
    }
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      ALARM_REQ_CODE,
      intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val calendar = Calendar.getInstance().apply {
      timeInMillis = System.currentTimeMillis()
      set(Calendar.HOUR_OF_DAY, hour)
      set(Calendar.MINUTE, minute)
      set(Calendar.SECOND, 0)
      set(Calendar.MILLISECOND, 0)
      // If time has already passed today, set for tomorrow
      if (timeInMillis <= System.currentTimeMillis()) {
        add(Calendar.DAY_OF_YEAR, 1)
      }
    }

    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
      } else {
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
      }
    } catch (_: SecurityException) {
      alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    saveReminderPreferences(context, enabled = true, hour = hour, minute = minute)
  }

  fun cancelDailyReminder(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
    val intent = Intent(context, DailyReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      ALARM_REQ_CODE,
      intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
    )
    if (pendingIntent != null) {
      alarmManager.cancel(pendingIntent)
      pendingIntent.cancel()
    }
    val (_, hour, minute) = getReminderPreferences(context)
    saveReminderPreferences(context, enabled = false, hour = hour, minute = minute)
  }

  fun saveReminderPreferences(context: Context, enabled: Boolean, hour: Int, minute: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit()
      .putBoolean(KEY_ENABLED, enabled)
      .putInt(KEY_HOUR, hour)
      .putInt(KEY_MINUTE, minute)
      .apply()
  }

  fun getReminderPreferences(context: Context): Triple<Boolean, Int, Int> {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val enabled = prefs.getBoolean(KEY_ENABLED, false)
    val hour = prefs.getInt(KEY_HOUR, 16)
    val minute = prefs.getInt(KEY_MINUTE, 30)
    return Triple(enabled, hour, minute)
  }
}

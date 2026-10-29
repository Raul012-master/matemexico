package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.util.NotificationHelper

class DailyReminderReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    val mascotName = intent.getStringExtra("mascot_name") ?: "Axo el Ajolote"
    NotificationHelper.sendDailyReminder(context, mascotName)

    // Re-schedule for the next day at the same configured time to keep daily recurrence
    val prefs = NotificationHelper.getReminderPreferences(context)
    if (prefs.first) {
      NotificationHelper.scheduleDailyReminder(context, prefs.second, prefs.third, mascotName)
    }
  }
}

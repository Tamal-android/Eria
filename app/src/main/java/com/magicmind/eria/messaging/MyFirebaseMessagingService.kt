package com.magicmind.eria.messaging

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.magicmind.eria.R
import com.magicmind.eria.app.EriaApplication
import com.magicmind.eria.ui.activity.SplashActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        EriaApplication.getPrefs().setFcmToken(
            this,
            token
        )
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()) {
            sendUserNotification(
                remoteMessage.data["title"].toString(),
                remoteMessage.data["body"].toString()
            );
        }
    }

    private fun sendUserNotification(title: String, mess: String) {
        val notifyID = System.currentTimeMillis().toInt()
        val intent: Intent
        val mChannel: NotificationChannel
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val CHANNEL_ID = this.packageName // The id of the channel.
        val name: CharSequence = "VT_Cab_Push_Notification" // The user-visible name of the channel.
        var importance = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH
        }
        val notificationBuilder =
            NotificationCompat.Builder(this, CHANNEL_ID)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.priority = Notification.PRIORITY_HIGH
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        notificationBuilder.setContentIntent(pendingIntent)

        /*val mp: MediaPlayer = MediaPlayer.create(this, R.raw.notification)
        mp.start()*/
        notificationBuilder.setStyle(
            NotificationCompat.BigTextStyle().bigText(mess)
        )
        notificationBuilder.setContentText(mess)
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE)
        notificationBuilder.setSmallIcon(getNotificationIcon(notificationBuilder))
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        notificationManager?.notify(
            notifyID /* ID of notification */,
            notificationBuilder.build()
        )
    }

    private fun getNotificationIcon(notificationBuilder: NotificationCompat.Builder): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val color = 0x036085
            notificationBuilder.color = color
            R.drawable.logo
        } else {
            R.mipmap.ic_launcher
        }
    }
}
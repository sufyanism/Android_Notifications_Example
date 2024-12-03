package com.learning.notificationsdemos

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.learning.notificationsdemos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: Notification.Builder
    private val channelId = "ChannelId"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.apply {

            btnInboxStyleNotification.setOnClickListener {
                makeInboxStyleNotification()
            }
            btnBigPictureNotification.setOnClickListener {
                makeBigPictureNotification()
            }
        }
    }

    private fun makeInboxStyleNotification() {
        getNotificationChannel()
        val style = Notification.InboxStyle()
        style.apply {
            addLine("Hi")
            addLine("How are you?")
//            setSummaryText("+10 more items..")
        }
        notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, channelId)
                .setContentTitle("Inbox")
                .setContentText("Message from")
                .setSmallIcon(R.drawable.profile)
                .setStyle(style)
                .setContentIntent(setPendingIntent(this))
        } else {
            Notification.Builder(this)
                .setContentTitle(getString(R.string.your_title))
                .setContentText(getString(R.string.your_body))
                .setStyle(style)
                .setContentIntent(setPendingIntent(this))
        }
        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun makeBigPictureNotification() {
        getNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val style = NotificationCompat.BigPictureStyle()
            style.bigPicture(BitmapFactory.decodeResource(resources, R.drawable.profile)).build()

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Text and Image")
                .setContentText("Message from")
                .setSmallIcon(R.drawable.profile)

                .setStyle(style)
                .setContentIntent(setPendingIntent(this))
            notificationManager.notify(1, notificationBuilder.build())
        }
    }

    private fun getNotificationChannel() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                channelId,
                getString(R.string.description_of_your_channel),
                NotificationManager.IMPORTANCE_HIGH
            ).also {
                it.enableLights(true)
                it.enableVibration(true)
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun setPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, HomeActivity::class.java)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}
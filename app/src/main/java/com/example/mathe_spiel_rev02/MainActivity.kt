package com.example.mathe_spiel_rev02



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import kotlin.random.Random
import kotlin.random.nextUInt

class MainActivity : RobotActivity(), RobotLifecycleCallbacks  {

    // Store the QiChatbot.
    lateinit var qiChatbot1: QiChatbot
    lateinit var chat1: Chat
    lateinit var topic1 : Topic
    lateinit var locale1 : Locale


    lateinit var button1: Button
    lateinit var button2: Button
    lateinit var button3: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         button1 = findViewById(R.id.Zahlenraum20)
         button2 = findViewById(R.id.Zahlenraum50)
         button3 = findViewById(R.id.Zahlenraum100)

        QiSDK.register(this,this)

        button1.setOnClickListener {
            val intent = Intent(this,Zahlenraum20::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener {
            val intent = Intent(this,Zahlenraum50::class.java)
            startActivity(intent)
        }
        button3.setOnClickListener {
            val intent = Intent(this,Zahlenraum100::class.java)
            startActivity(intent)
        }

    }

    override fun onRobotFocusGained(qiContext: QiContext?) {

        // Festlegen der Sprache des Roboters
        locale1 = com.aldebaran.qi.sdk.`object`.locale.Locale(Language.GERMAN, Region.GERMANY)
        // Einbinden des zuvor erstellen Topics
        topic1 = TopicBuilder.with(qiContext).withResource(R.raw.small_talk).build()
        // Erstellen qiChatbot
        qiChatbot1 = QiChatbotBuilder.with(qiContext).withTopic(topic1).withLocale(locale1).build()
        // Chat erstellen
        chat1 = ChatBuilder.with(qiContext).withChatbot(qiChatbot1).withLocale(locale1).build()

        chat1.run()

    }

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this,this)
    }
}
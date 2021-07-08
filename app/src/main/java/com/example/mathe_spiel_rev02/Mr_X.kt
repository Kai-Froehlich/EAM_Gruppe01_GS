package com.example.mathe_spiel_rev02



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.actuation.Animate
import com.aldebaran.qi.sdk.`object`.actuation.Animation
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.*
import com.aldebaran.qi.sdk.design.activity.RobotActivity

class Mr_X : RobotActivity(), RobotLifecycleCallbacks  {

    // Speichern des QiChatbot.
    lateinit var qiChatbot: QiChatbot
    lateinit var chat: Chat
    lateinit var topic : Topic
    lateinit var locale : Locale

    // Speichern der Buttons
    lateinit var button1: Button
    lateinit var button2: Button
    lateinit var button3: Button

    // Speichern der Animate action.
    private var animate: Animate? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Tablet Buttons des gespeicherten Buttons zuordnen
         button1 = findViewById(R.id.Zahlenraum20)
         button2 = findViewById(R.id.Zahlenraum50)
         button3 = findViewById(R.id.Zahlenraum100)

        QiSDK.register(this,this)

        // Wechseln in die Activity Zahlenraum 20 durch Betätigung des Buttons 1
        button1.setOnClickListener {
            val intent = Intent(this,Zahlenraum20::class.java)
            startActivity(intent)
        }
        // Wechseln in die Activity Zahlenraum 50 durch Betätigung des Buttons 2
        button2.setOnClickListener {
            val intent = Intent(this,Zahlenraum50::class.java)
            startActivity(intent)
        }
        // Wechseln in die Activity Zahlenraum 100 durch Betätigung des Buttons 3
        button3.setOnClickListener {
            val intent = Intent(this,Zahlenraum100::class.java)
            startActivity(intent)
        }

    }


    override fun onRobotFocusGained(qiContext: QiContext?) {

        // Festlegen der Sprache des Roboters
        locale = com.aldebaran.qi.sdk.`object`.locale.Locale(Language.GERMAN, Region.GERMANY)
        // Einbinden des zuvor erstellen Topics
        topic = TopicBuilder.with(qiContext).withResource(R.raw.zahlenraum).build()
        // Erstellen qiChatbot
        qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).withLocale(locale).build()
        // Chat erstellen
        chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).withLocale(locale).build()

        // Beginn des Chats mit dem proposal Spiel
        chat.addOnStartedListener { goToBookmark("Spiel") }

        // Start des Chats
        chat.async().run()

    }

    // Definition der goToBookmark. BookMark hat hohe wichtigkeit und Reaktion wird unmittlebar ausgeführt
    private fun  goToBookmark(bookmarkName : String) {
        qiChatbot.goToBookmark(
                topic.bookmarks[bookmarkName],
                AutonomousReactionImportance.HIGH,
                AutonomousReactionValidity.IMMEDIATE)
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
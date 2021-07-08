package com.example.mathe_spiel_rev02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.actuation.Animate
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity

class Abfrage : RobotActivity(), RobotLifecycleCallbacks {

    //Store the qiChatbot
    lateinit var qiChatbot: QiChatbot

    lateinit var chat: Chat
    lateinit var topic: Topic
    lateinit var locale: Locale

    //anlegen (Initialisieren) der 4 Auswahlbuttons und definieren dieser als button
    lateinit var button1: Button
    lateinit var button2: Button
    lateinit var button3: Button
    lateinit var button4: Button
    lateinit var button5: Button

    //Store the animate action
    private var animate: Animate? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abfrage)
    //Definieren der 4 Buttons (Button1 ruft bspw. die activity Bundeslaender auf)
        button1 = findViewById(R.id.Bundeslaender)
        button2 = findViewById(R.id.Landeshauptstaedte)
        button3 = findViewById(R.id.Nachbarlaender)
        button4 = findViewById(R.id.Hauptstaedte_der_Nachbarlaender)
        button5 = findViewById(R.id.home1)

        QiSDK.register(this,this)
    // Durch ein klick auf den Button wird die oben definierte Activity aufgerufen
        button1.setOnClickListener {
            val intent = Intent(this,Bundeslaender::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener {
            val intent = Intent(this,Landeshauptstaedte::class.java)
            startActivity(intent)
        }
        button3.setOnClickListener {
            val intent = Intent(this,Nachbarlaender::class.java)
            startActivity(intent)
        }
        button4.setOnClickListener {
            val intent = Intent(this,Hauptstaedte_der_Nachbarlaender::class.java)
            startActivity(intent)
        }
        button5.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        //Festlegen der Sprache des Roboters//
        val locale = com.aldebaran.qi.sdk.`object`.locale.Locale(Language.GERMAN, Region.GERMANY)
        //Einbinden des erstellten Topics
        val topic = TopicBuilder.with(qiContext).withResource(R.raw.abfrage_laender_und_hauptstaedte).build()
        //Erstellen eines qiChatbots mittels Chatbotsbuilder und unter Einbeziehung der Spracheinstellungen
        val qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).withLocale(locale).build()
        //Chat erstellen
        val chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).withLocale(locale).build()
        //Ausführen des Chats
                chat.run()
    }

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
package com.example.mathe_spiel_rev02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity

class Abfrage : RobotActivity(), RobotLifecycleCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abfrage)

        QiSDK.register(this,this)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        //Festlegen der Sprache des Roboters//
        val locale = com.aldebaran.qi.sdk.`object`.locale.Locale(Language.GERMAN, Region.GERMANY)
        //Einbinden des zuvor erstellten Topics
        val topic = TopicBuilder.with(qiContext).withResource(R.raw.abfrage_laender_und_hauptstaedte).build()
        //Erstellen eines qiChatbots mittels Chatbotsbuilder und unter Einbeziehung der Spracheinstellungen
        val qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).withLocale(locale).build()
        //Chat erstellen
        val chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).withLocale(locale).build()
        //Ausführen des Chats
        chat.run()
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
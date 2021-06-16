package com.example.mathe_spiel_rev02

import android.os.Bundle
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
import java.util.concurrent.Future
import kotlin.random.Random

class Zahlenraum20 : RobotActivity(), RobotLifecycleCallbacks {

    // Store the QiChatbot.
    lateinit var qiChatbot: QiChatbot
    lateinit var chat: Chat
    lateinit var topic : Topic
    lateinit var locale : Locale
    // Store the Animate action.
    private var animate: Animate? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zahlenraum)
        QiSDK.register(this,this)
    }

    class MyQiChatExecutor(qiContext: QiContext?): BaseQiChatExecutor(qiContext){
        override fun runWith(params: MutableList<String>?) {
            animate(qiContext)
        }

        override fun stop() {
            TODO("Not yet implemented")
        }
        private fun animate(qiContext: QiContext?){
            val animation: Animation = AnimationBuilder.with(qiContext).withResources(R.raw.clapping_b001).build()
            val animate: Animate = AnimateBuilder.with(qiContext).withAnimation(animation).build()
            animate.run()
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

        randomize()
        chat.addOnStartedListener { goToBookmark("Zahlenraum20") }


        // Animation
        val executors = HashMap<String,QiChatExecutor>()
        executors["Applaus"] = MyQiChatExecutor(qiContext)
        qiChatbot.executors = executors
        val chatbots = mutableListOf<Chatbot>()
        chatbots.add(qiChatbot)


        //chat.addOnStartedListener { goToBookmark("Zahlenraum20") }
        // Ausführen des Chats
        // Get the variable.

        chat.async().run()
    }

    private fun  goToBookmark(bookmarkName : String) {
        qiChatbot.goToBookmark(
            topic.bookmarks[bookmarkName],
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE)
    }

    private fun randomize() {
        //val Zahl_A = Random.nextInt(0,20)
        val Zahl_A = 4
        qiChatbot.variable("peppersNumber").value = "$Zahl_A"

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
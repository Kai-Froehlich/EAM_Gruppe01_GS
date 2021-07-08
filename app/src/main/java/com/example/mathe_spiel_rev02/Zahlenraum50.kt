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
import kotlin.random.Random

class Zahlenraum50 : RobotActivity(), RobotLifecycleCallbacks {

    // Speichern des QiChatbots.
    lateinit var qiChatbot: QiChatbot
    lateinit var chat: Chat
    lateinit var topic : Topic
    lateinit var locale : Locale

    // Speichern der Animate action.
    private var animate: Animate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Zeigen des Layouts activity_zahlenraum auf dem Tablet
        setContentView(R.layout.activity_zahlenraum)
        QiSDK.register(this,this)
    }

    // Klasse für Applaus definieren und Animationen aufbauen und ausführen
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

        // Aufruf der randomize Funktion
        randomize()
        // Beginn des Chats mit Bookmark Zahlenraum50
        chat.addOnStartedListener { goToBookmark("Zahlenraum50") }

        // Animation wird mit excecute(Applaus) im chat topic verknüpft
        val executors = HashMap<String,QiChatExecutor>()
        executors["Applaus"] = Zahlenraum50.MyQiChatExecutor(qiContext)
        qiChatbot.executors = executors
        val chatbots = mutableListOf<Chatbot>()
        chatbots.add(qiChatbot)

        // Ausführen des Chats
        chat.async().run()
    }

    // Definition der goToBookmark. BookMark hat hohe wichtigkeit und Reaktion wird unmittlebar ausgeführt
    private fun  goToBookmark(bookmarkName : String) {
        qiChatbot.goToBookmark(
            topic.bookmarks[bookmarkName],
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE)
    }

    // Speichert eine zufällige Zahl in einem Zahlenraum von 0-50 und übergibt diese an den qichatbot
    private fun randomize() {
        val Zahl_A = Random.nextInt(0,50)
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
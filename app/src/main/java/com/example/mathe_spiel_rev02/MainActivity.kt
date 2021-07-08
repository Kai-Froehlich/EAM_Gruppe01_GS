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

class MainActivity : RobotActivity(), RobotLifecycleCallbacks {

    // Store the QiChatbot.
    lateinit var qiChatbot: QiChatbot
    lateinit var chat: Chat
    lateinit var topic : Topic
    lateinit var locale : Locale

    lateinit var button1: Button
    lateinit var button2: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_small_talk)

        button1 = findViewById(R.id.Mr_x)
        button2 = findViewById(R.id.Abfrage)

        QiSDK.register(this,this)

        button1.setOnClickListener {
            val intent = Intent(this,Mr_X::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener {
            val intent = Intent(this,Abfrage::class.java)
            startActivity(intent)
        }
    }

    class MyQiChatExecutor(qiContext: QiContext?): BaseQiChatExecutor(qiContext){
        override fun runWith(params: MutableList<String>?) {
            animate(qiContext)
        }

        override fun stop() {
            TODO("Not yet implemented")
        }
        private fun animate(qiContext: QiContext?){
            val animation: Animation = AnimationBuilder.with(qiContext).withResources(R.raw.hello_a009).build()
            val animate: Animate = AnimateBuilder.with(qiContext).withAnimation(animation).build()
            animate.run()
        }
    }

    class MyQiChatFunExecutor(qiContext: QiContext?): BaseQiChatExecutor(qiContext){
        override fun runWith(params: MutableList<String>?) {
            animate(qiContext)
        }

        override fun stop() {
            TODO("Not yet implemented")
        }
        private fun animate(qiContext: QiContext?){
            val animation: Animation = AnimationBuilder.with(qiContext).withResources(R.raw.funny_a001).build()
            val animate: Animate = AnimateBuilder.with(qiContext).withAnimation(animation).build()
            animate.run()
        }
    }

    class MyQiChatTabletExecutor(qiContext: QiContext?): BaseQiChatExecutor(qiContext){
        override fun runWith(params: MutableList<String>?) {
            animate(qiContext)
        }

        override fun stop() {
            TODO("Not yet implemented")
        }
        private fun animate(qiContext: QiContext?){
            val animation: Animation = AnimationBuilder.with(qiContext).withResources(R.raw.show_tablet_a002).build()
            val animate: Animate = AnimateBuilder.with(qiContext).withAnimation(animation).build()
            animate.run()
        }
    }





    override fun onRobotFocusGained(qiContext: QiContext?) {

        // Festlegen der Sprache des Roboters
        locale = com.aldebaran.qi.sdk.`object`.locale.Locale(Language.GERMAN, Region.GERMANY)
        // Einbinden des zuvor erstellen Topics
        topic = TopicBuilder.with(qiContext).withResource(R.raw.small_talk).build()
        // Erstellen qiChatbot
        qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).withLocale(locale).build()
        // Chat erstellen
        chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).withLocale(locale).build()

        val executors = HashMap<String, QiChatExecutor>()
        executors["Begruessung"] = MainActivity.MyQiChatExecutor(qiContext)
        qiChatbot.executors = executors
        val chatbots = mutableListOf<Chatbot>()
        chatbots.add(qiChatbot)


        executors["funny"] = MainActivity.MyQiChatFunExecutor(qiContext)
        qiChatbot.executors = executors
        chatbots.add(qiChatbot)

        executors["Zeigen"] = MainActivity.MyQiChatTabletExecutor(qiContext)
        qiChatbot.executors = executors
        chatbots.add(qiChatbot)

        chat.async().run()
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
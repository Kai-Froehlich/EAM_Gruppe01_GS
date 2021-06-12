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

    // Store the QiChatbot.
    lateinit var qiChatbot: QiChatbot
    lateinit var chat: Chat
    lateinit var topic : Topic
    lateinit var locale : Locale


    lateinit var button1: Button
    lateinit var button2: Button
    lateinit var button3: Button

    // Store the Animate action.
    private var animate: Animate? = null


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

    override fun onRobotFocusGained(qiContext: QiContext?) {

        // Festlegen der Sprache des Roboters
        locale = com.aldebaran.qi.sdk.`object`.locale.Locale(Language.GERMAN, Region.GERMANY)
        // Einbinden des zuvor erstellen Topics
        topic = TopicBuilder.with(qiContext).withResource(R.raw.small_talk).build()
        // Erstellen qiChatbot
        qiChatbot = QiChatbotBuilder.with(qiContext).withTopic(topic).withLocale(locale).build()
        // Chat erstellen
        chat = ChatBuilder.with(qiContext).withChatbot(qiChatbot).withLocale(locale).build()

        val executors = HashMap<String,QiChatExecutor>()
        executors["Begruessung"] = Mr_X.MyQiChatExecutor(qiContext)
        qiChatbot.executors = executors
        val chatbots = mutableListOf<Chatbot>()
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
package com.example.checksolution

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random
import android.os.CountDownTimer

class MainActivity : AppCompatActivity() {
    private lateinit var totalQuestions : TextView
    private lateinit var correctAnswers : TextView
    private lateinit var incorrectAnswers : TextView
    private lateinit var score : TextView
    private lateinit var operand1 : TextView
    private lateinit var operand2 : TextView
    private lateinit var operator : TextView
    private lateinit var answer : TextView
    private lateinit var correctBtn : Button
    private lateinit var incorrectBtn : Button
    private lateinit var start : Button
    private lateinit var minText : TextView
    private lateinit var maxText : TextView
    private lateinit var avgText : TextView

    var min = Int.MAX_VALUE
    var max = Int.MIN_VALUE
    var avgSummary = 0
    var correctAns = 0
    var incorrectAns = 0
    val operations = listOf("+", "-", "*", "/")
    var seconds = 0
    val totalSeconds = 1000
    lateinit var timer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        totalQuestions = findViewById(R.id.totalQuestions)
        correctAnswers = findViewById(R.id.correctAnswers)
        incorrectAnswers = findViewById(R.id.incorrectAnswers)
        score = findViewById(R.id.score)
        operand1 = findViewById(R.id.operand1)
        operand2 = findViewById(R.id.operand2)
        operator = findViewById(R.id.operator)
        answer = findViewById(R.id.answer)
        minText = findViewById(R.id.minText)
        maxText = findViewById(R.id.maxText)
        avgText = findViewById(R.id.avgText)

        correctBtn = findViewById(R.id.correctBtn)
        correctBtn.setOnClickListener {
            validateAnswer()
        }

        incorrectBtn = findViewById<Button>(R.id.incorrectBtn)
        incorrectBtn.setOnClickListener {
            invalidateAnswer()
        }

        start = findViewById(R.id.button)
        start.setOnClickListener {
            generateNewQuestion()
        }

        correctBtn.isEnabled = false
        incorrectBtn.isEnabled = false

        timer = object : CountDownTimer(totalSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                seconds++
            }
            override fun onFinish() { }
        }.start()
    }

    fun validateAnswer() {
        updateElapsedTime()
        correctBtn.isEnabled = false
        incorrectBtn.isEnabled = false
        start.isEnabled = true

        val correctAnswer = calculateAnswer().toInt().toString()

        if (answer.text.toString() == correctAnswer) {
            correctAns++
        }
        else {
            incorrectAns++
        }

        updateCounters()
    }

    fun invalidateAnswer() {
        updateElapsedTime()
        correctBtn.isEnabled = false
        incorrectBtn.isEnabled = false
        start.isEnabled = true

        val correctAnswer = calculateAnswer().toInt().toString()

        if (answer.text.toString() != correctAnswer) {
            correctAns++
        }
        else {
            incorrectAns++
        }

        updateCounters()
    }

    fun generateNewQuestion() {
        start.isEnabled = false
        correctBtn.isEnabled = true
        incorrectBtn.isEnabled = true
        answer.isEnabled = true
        answer.setBackgroundColor(Color.TRANSPARENT)

        createExpression()

        seconds = 0
    }

    fun updateElapsedTime() {
        if (seconds < min) {
            min = seconds
        }
        if (seconds > max) {
            max = seconds
        }
        avgSummary += seconds

        minText.text = min.toString()
        maxText.text = max.toString()
        avgText.text = String.format("%.2f", avgSummary.toDouble() / (correctAns + incorrectAns))
    }

    fun createExpression() {
        operand1.text = Random.nextInt(10, 100).toString()
        operand2.text = Random.nextInt(10, 100).toString()
        operator.text = operations[Random.nextInt(0, 4)]

        while (operator.text == "/" && operand1.text.toString().toDouble() % operand2.text.toString().toDouble() != 0.0) {
            operand1.text = Random.nextInt(10, 100).toString()
            operand2.text = Random.nextInt(10, 100).toString()
        }

        if (Random.nextInt(0, 2) == 1) {
            answer.text = calculateAnswer().toInt().toString()
        }
        else {
            answer.text = Random.nextInt(10, 100).toString()
        }
    }

    fun calculateAnswer() : Double {
        when (operator.text){
            "+" -> {
                return operand1.text.toString().toDouble() + operand2.text.toString().toDouble()
            }
            "-" -> {
                return operand1.text.toString().toDouble() - operand2.text.toString().toDouble()
            }
            "*" -> {
                return operand1.text.toString().toDouble() * operand2.text.toString().toDouble()
            }
            "/" -> {
                return operand1.text.toString().toDouble() / operand2.text.toString().toDouble()
            }
        }
        return 0.0
    }

    fun updateCounters() {
        correctAnswers.text = correctAns.toString()
        incorrectAnswers.text = incorrectAns.toString()
        totalQuestions.text = (correctAns + incorrectAns).toString()
        score.text = String.format("%.2f", (correctAns.toDouble() / (correctAns + incorrectAns) * 100)) + "%"
    }
}

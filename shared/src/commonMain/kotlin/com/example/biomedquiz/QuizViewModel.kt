package com.example.biomedquiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class QuizViewModel {

    private var questions: List<Question> = emptyList()
    private var currentIndex = 0
    private var scoreValue = 0
    private var isInitialized = false

    var currentCategory by mutableStateOf<String?>(null)
        private set

    var currentQuestion by mutableStateOf<Question?>(null)
        private set

    var isQuizFinished by mutableStateOf(false)
        private set

    var score by mutableStateOf(0)
        private set

    // Stany do podświetlania odpowiedzi
    var selectedAnswerIndex by mutableStateOf<Int?>(null)
        private set

    var correctAnswerIndex by mutableStateOf<Int?>(null)
        private set

    // Inicjalizacja pytań dla wybranej kategorii
    fun startQuiz(category: String) {
        currentCategory = category
        questions = if (category == "Wszystko") {
            QuestionRepository.questions.shuffled().take(10)
        } else {
            QuestionRepository.questions.filter { it.category == category }.shuffled().take(10)
        }
        currentIndex = 0
        scoreValue = 0
        score = 0
        isQuizFinished = false
        selectedAnswerIndex = null
        correctAnswerIndex = null
        isInitialized = true
        loadCurrentQuestion()
    }

    private fun loadCurrentQuestion() {
        if (currentIndex < questions.size) {
            currentQuestion = questions[currentIndex]
            selectedAnswerIndex = null
            correctAnswerIndex = null
        }
    }

    // Sprawdzanie odpowiedzi i podświetlanie
    fun selectAnswer(index: Int) {
        if (selectedAnswerIndex != null) return // Zapobiega wielokrotnemu klikaniu

        selectedAnswerIndex = index
        val currentQ = questions[currentIndex]
        correctAnswerIndex = currentQ.correctAnswer

        if (index == currentQ.correctAnswer) {
            scoreValue++
            score = scoreValue
        }
    }

    // Przejście do kolejnego pytania
    fun moveToNextQuestion() {
        currentIndex++
        if (currentIndex < questions.size) {
            loadCurrentQuestion()
        } else {
            isQuizFinished = true
        }
    }

    // Powrót do ekranu wyboru kategorii
    fun returnToCategorySelection() {
        currentCategory = null
        isInitialized = false
        isQuizFinished = false
    }

    fun getCurrentQuestionNumber(): Int = currentIndex + 1
    fun getTotalQuestionsCount(): Int = questions.size
    fun getFinalScore(): Int = scoreValue
}
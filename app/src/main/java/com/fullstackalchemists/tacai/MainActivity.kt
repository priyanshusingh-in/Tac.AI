package com.fullstackalchemists.tacai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import android.media.MediaPlayer
import android.media.AudioManager
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {
    private lateinit var buttons: Array<Array<Button>>
    private lateinit var statusText: TextView
    private lateinit var resetButton: Button
    private lateinit var thinkingProgress: ProgressBar
    private lateinit var playerScoreText: TextView
    private lateinit var aiScoreText: TextView
    private lateinit var timerText: TextView  // New TextView for timer display

    private var board = Array(3) { Array(3) { "" } }
    private var isPlayerTurn = true
    private var playerScore = 0
    private var aiScore = 0
    private val handler = Handler(Looper.getMainLooper())
    private var playerTimer: CountDownTimer? = null // Timer for the player's turn

    // Sound players
    private var buttonClickSound: MediaPlayer? = null
    private var winSound: MediaPlayer? = null
    private var drawSound: MediaPlayer? = null
    private var aiMoveSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize sound function
        initializeSounds()

        // Set volume control to media volume
        volumeControlStream = AudioManager.STREAM_MUSIC

        // Initialize views
        statusText = findViewById(R.id.statusText)
        resetButton = findViewById(R.id.resetButton)
        thinkingProgress = findViewById(R.id.thinkingProgress)
        playerScoreText = findViewById(R.id.playerScoreText)
        aiScoreText = findViewById(R.id.aiScoreText)
        timerText = findViewById(R.id.timerText)  // New TextView for timer display

        // Initialize buttons array
        buttons = Array(3) { row ->
            Array(3) { col ->
                findViewById<Button>(
                    resources.getIdentifier(
                        "btn$row$col",
                        "id",
                        packageName
                    )
                )
            }
        }

        // Set click listeners for all buttons
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].setOnClickListener {
                    if (isPlayerTurn && board[i][j].isEmpty()) {
                        playSound(buttonClickSound)
                        makeMove(i, j, "X")
                        playerTimer?.cancel()  // Stop timer when player makes a move
                        if (!checkGameEnd()) {
                            setButtonsEnabled(false)
                            thinkingProgress.visibility = View.VISIBLE
                            handler.postDelayed({
                                makeAIMove()
                                setButtonsEnabled(true)
                                thinkingProgress.visibility = View.GONE
                            }, 1000) // 1 second delay
                        }
                    }
                }
            }
        }

        resetButton.setOnClickListener {
            playSound(buttonClickSound)
            resetGame()
        }

        // Start timer on player's turn
        startPlayerTimer()
    }

    private fun startPlayerTimer() {
        playerTimer?.cancel()  // Cancel any existing timer
        playerTimer = object : CountDownTimer(10000, 1000) {  // 10 seconds countdown
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerText.text = "Time left: $secondsLeft s"
            }

            override fun onFinish() {
                timerText.text = "Time's up!"
                if (isPlayerTurn) {
                    isPlayerTurn = false
                    makeAIMove()
                    setButtonsEnabled(true)
                }
            }
        }.start()
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].isEnabled = enabled
            }
        }
        resetButton.isEnabled = enabled
    }

    private fun initializeSounds() {
        buttonClickSound = MediaPlayer.create(this, R.raw.button_click)
        winSound = MediaPlayer.create(this, R.raw.win_sound)
        drawSound = MediaPlayer.create(this, R.raw.draw_sound)
        aiMoveSound = MediaPlayer.create(this, R.raw.ai_move)
    }

    private fun playSound(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.seekTo(0)
            } else {
                it.start()
            }
        }
    }

    private fun makeMove(row: Int, col: Int, symbol: String) {
        board[row][col] = symbol
        buttons[row][col].text = symbol
        isPlayerTurn = !isPlayerTurn
        statusText.text = if (isPlayerTurn) "Your Turn" else "AI's Turn"
        if (isPlayerTurn) startPlayerTimer()  // Restart timer on player's turn
    }

    private fun makeAIMove() {
        val bestMove = findBestMove()
        playSound(aiMoveSound)
        makeMove(bestMove.first, bestMove.second, "O")
        checkGameEnd()
    }

    private fun findBestMove(): Pair<Int, Int> {
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].isEmpty()) {
                    board[i][j] = "O"
                    if (checkWinner("O")) {
                        board[i][j] = ""
                        return Pair(i, j)
                    }
                    board[i][j] = ""
                }
            }
        }

        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].isEmpty()) {
                    board[i][j] = "X"
                    if (checkWinner("X")) {
                        board[i][j] = ""
                        return Pair(i, j)
                    }
                    board[i][j] = ""
                }
            }
        }

        if (board[1][1].isEmpty()) {
            return Pair(1, 1)
        }

        val corners = listOf(Pair(0, 0), Pair(0, 2), Pair(2, 0), Pair(2, 2))
        for (corner in corners) {
            if (board[corner.first][corner.second].isEmpty()) {
                return corner
            }
        }

        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].isEmpty()) {
                    return Pair(i, j)
                }
            }
        }

        return Pair(0, 0)
    }

    private fun checkGameEnd(): Boolean {
        val winner = when {
            checkWinner("X") -> {
                playerScore++
                playerScoreText.text = "Player: $playerScore"
                playSound(winSound)
                "You win!"
            }

            checkWinner("O") -> {
                aiScore++
                aiScoreText.text = "AI: $aiScore"
                playSound(winSound)
                "AI wins!"
            }

            isBoardFull() -> {
                playSound(drawSound)
                "It's a draw!"
            }

            else -> null
        }

        winner?.let {
            playerTimer?.cancel()  // Stop timer on game end
            showGameEndDialog(it)
            return true
        }
        return false
    }

    private fun checkWinner(symbol: String): Boolean {
        for (i in 0..2) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) return true
        }

        for (i in 0..2) {
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) return true
        }

        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) return true
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) return true

        return false
    }

    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { it.isNotEmpty() } }
    }

    private fun showGameEndDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage(message)
            .setPositiveButton("Play Again") { _, _ ->
                resetGame()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetGame() {
        board = Array(3) { Array(3) { "" } }
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].text = ""
            }
        }
        isPlayerTurn = true
        playerTimer?.cancel()  // Cancel any running timer
        startPlayerTimer()  // Start timer for new game
        statusText.text = "Your Turn"
        timerText.text = "Time left: 10 s"
    }
}
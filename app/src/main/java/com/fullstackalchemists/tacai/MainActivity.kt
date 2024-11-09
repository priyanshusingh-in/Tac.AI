package com.fullstackalchemists.tacai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import android.media.MediaPlayer
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {
    private lateinit var buttons: Array<Array<Button>>
    private lateinit var statusText: TextView
    private lateinit var resetButton: Button
    private lateinit var thinkingProgress: ProgressBar
    private var board = Array(3) { Array(3) { "" } }
    private var isPlayerTurn = true
    private val handler = Handler(Looper.getMainLooper())

    // Sound players
    private var buttonClickSound: MediaPlayer? = null
    private var winSound: MediaPlayer? = null
    private var drawSound: MediaPlayer? = null
    private var aiMoveSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize sound
        initializeSounds()

        // Set volume control to media volume
        volumeControlStream = AudioManager.STREAM_MUSIC

        statusText = findViewById(R.id.statusText)
        resetButton = findViewById(R.id.resetButton)
        thinkingProgress = findViewById(R.id.thinkingProgress)

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
                        if (!checkGameEnd()) {
                            // Disable all buttons during AI turn
                            setButtonsEnabled(false)
                            // Show thinking progress
                            thinkingProgress.visibility = View.VISIBLE
                            // Add delay before AI move
                            handler.postDelayed({
                                makeAIMove()
                                // Re-enable buttons after AI move
                                setButtonsEnabled(true)
                                // Hide thinking progress
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
    }

    private fun makeAIMove() {
        var bestMove = findBestMove()
        playSound(aiMoveSound)
        makeMove(bestMove.first, bestMove.second, "O")
        checkGameEnd()
    }

    private fun findBestMove(): Pair<Int, Int> {
        // Check for winning move
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

        // Check for blocking player's winning move
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

        // Try to take center
        if (board[1][1].isEmpty()) {
            return Pair(1, 1)
        }

        // Take any corner
        val corners = listOf(Pair(0,0), Pair(0,2), Pair(2,0), Pair(2,2))
        for (corner in corners) {
            if (board[corner.first][corner.second].isEmpty()) {
                return corner
            }
        }

        // Take any available spot
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
                playSound(winSound)
                "You win!"
            }
            checkWinner("O") -> {
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
            showGameEndDialog(it)
            return true
        }
        return false
    }

    private fun checkWinner(symbol: String): Boolean {
        // Check rows
        for (i in 0..2) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) return true
        }

        // Check columns
        for (i in 0..2) {
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) return true
        }

        // Check diagonals
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
        statusText.text = "Your Turn"
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaPlayer resources
        buttonClickSound?.release()
        winSound?.release()
        drawSound?.release()
        aiMoveSound?.release()
    }
}


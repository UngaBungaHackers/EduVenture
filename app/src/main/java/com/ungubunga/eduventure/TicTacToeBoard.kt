package com.ungubunga.eduventure.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ungubunga.eduventure.R
import kotlin.math.ceil
import kotlin.math.min

class TicTacToeBoard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val boardColor: Int
    private val Xcolor: Int
    private val Ocolor: Int
    private val winningLineColor: Int

    private val paint: Paint = Paint()

    private var cellSize: Int = 0

    private val boardMarkers = Array(3) { IntArray(3) }

    private var currentPlayer = 1

    private var gameOver = false

    private var winningPlayer = 0

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TicTacToeBoard,
            0,
            0
        )

        try {
            boardColor = a.getColor(R.styleable.TicTacToeBoard_boardColor, 0xFF000000.toInt()) // Black
            Xcolor = a.getColor(R.styleable.TicTacToeBoard_XColor, 0xFFFF0000.toInt()) // Red
            Ocolor = a.getColor(R.styleable.TicTacToeBoard_OColor, 0xFF0000FF.toInt()) // Blue
            winningLineColor = a.getColor(R.styleable.TicTacToeBoard_winningLineColor, 0xFFFFFF00.toInt()) // Yellow
        } finally {
            a.recycle()
        }

        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 16f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val dimension = min(measuredWidth, measuredHeight)
        cellSize = dimension / 3
        setMeasuredDimension(dimension, dimension)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGameBoard(canvas)
        drawMarkers(canvas)
        if (gameOver) {
            drawWinningLine(canvas)
        }
    }

    private fun drawGameBoard(canvas: Canvas) {
        paint.color = boardColor

        for (c in 1..2) {
            val x = (cellSize * c).toFloat()
            canvas.drawLine(x, 0f, x, height.toFloat(), paint)
        }

        for (r in 1..2) {
            val y = (cellSize * r).toFloat()
            canvas.drawLine(0f, y, width.toFloat(), y, paint)
        }
    }

    private fun drawMarkers(canvas: Canvas) {
        for (r in 0..2) {
            for (c in 0..2) {
                when (boardMarkers[r][c]) {
                    1 -> drawX(canvas, r, c)
                    2 -> drawO(canvas, r, c)
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (gameOver) {
            return super.onTouchEvent(event)
        }

        val x = event.x
        val y = event.y

        val action = event.action

        if (action == MotionEvent.ACTION_DOWN) {
            val row = ceil(y / cellSize.toDouble()).toInt() - 1
            val col = ceil(x / cellSize.toDouble()).toInt() - 1

            if (row in 0..2 && col in 0..2 && boardMarkers[row][col] == 0) {
                boardMarkers[row][col] = currentPlayer
                invalidate()
                if (checkForWin(row, col)) {
                    gameOver = true
                    winningPlayer = currentPlayer
                    invalidate()
                } else if (isBoardFull()) {
                    gameOver = true
                    invalidate()
                } else {
                    currentPlayer = if (currentPlayer == 1) 2 else 1
                }
            }
        }

        return true
    }

    private fun drawX(canvas: Canvas, row: Int, col: Int) {
        paint.color = Xcolor

        val padding = cellSize * 0.2f

        val startX1 = (col * cellSize + padding)
        val startY1 = (row * cellSize + padding)
        val endX1 = ((col + 1) * cellSize - padding)
        val endY1 = ((row + 1) * cellSize - padding)

        val startX2 = ((col + 1) * cellSize - padding)
        val startY2 = (row * cellSize + padding)
        val endX2 = (col * cellSize + padding)
        val endY2 = ((row + 1) * cellSize - padding)

        canvas.drawLine(startX1, startY1, endX1, endY1, paint)
        canvas.drawLine(startX2, startY2, endX2, endY2, paint)
    }

    private fun drawO(canvas: Canvas, row: Int, col: Int) {
        paint.color = Ocolor

        val padding = cellSize * 0.2f

        val left = (col * cellSize + padding)
        val top = (row * cellSize + padding)
        val right = ((col + 1) * cellSize - padding)
        val bottom = ((row + 1) * cellSize - padding)

        canvas.drawOval(left, top, right, bottom, paint)
    }

    private fun checkForWin(row: Int, col: Int): Boolean {
        if (boardMarkers[row].all { it == currentPlayer }) {
            return true
        }

        if (boardMarkers.all { it[col] == currentPlayer }) {
            return true
        }

        if (row == col && boardMarkers.indices.all { boardMarkers[it][it] == currentPlayer }) {
            return true
        }

        if (row + col == 2 && boardMarkers.indices.all { boardMarkers[it][2 - it] == currentPlayer }) {
            return true
        }

        return false
    }

    private fun isBoardFull(): Boolean {
        return boardMarkers.all { row -> row.all { it != 0 } }
    }

    private fun drawWinningLine(canvas: Canvas) {
        paint.color = winningLineColor
        paint.strokeWidth = 20f

        for (r in 0..2) {
            if (boardMarkers[r].all { it == winningPlayer }) {
                val y = (r * cellSize + cellSize / 2).toFloat()
                canvas.drawLine(0f, y, width.toFloat(), y, paint)
                return
            }
        }

        for (c in 0..2) {
            if (boardMarkers.all { it[c] == winningPlayer }) {
                val x = (c * cellSize + cellSize / 2).toFloat()
                canvas.drawLine(x, 0f, x, height.toFloat(), paint)
                return
            }
        }

        if (boardMarkers.indices.all { boardMarkers[it][it] == winningPlayer }) {
            canvas.drawLine(0f, 0f, width.toFloat(), height.toFloat(), paint)
            return
        }

        if (boardMarkers.indices.all { boardMarkers[it][2 - it] == winningPlayer }) {
            canvas.drawLine(width.toFloat(), 0f, 0f, height.toFloat(), paint)
            return
        }
    }

    fun resetGame() {
        for (r in 0..2) {
            for (c in 0..2) {
                boardMarkers[r][c] = 0
            }
        }
        currentPlayer = 1
        gameOver = false
        winningPlayer = 0
        invalidate()
    }
}

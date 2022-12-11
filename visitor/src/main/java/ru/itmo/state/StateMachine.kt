package ru.itmo.state

import ru.itmo.tokenizer.Brace
import ru.itmo.tokenizer.NumberToken
import ru.itmo.tokenizer.Operation
import ru.itmo.tokenizer.StubToken
import ru.itmo.tokenizer.Token

class StateMachine(private val input: String) {
    private var state: State = nextState()
    private var iterator = 0

    fun nextState(): State {
        skipWhiteSpace()

        if (state is End || state is Error) {
            return state
        }

        if (iterator == input.length) {
            state = End
            return End
        }

        return input[iterator]
            .let {
                when {
                    it in setOf('+', '-', '*', '/', '(', ')') -> Start()
                    it.isDigit() -> Number()
                    it.isWhitespace() -> Start()
                    else -> Error(iterator)
                }
            }
    }

    inner class Start : State {
        override fun createToken(): Token = when (input[iterator++]) {
            '+' -> Operation.PLUS
            '-' -> Operation.MINUS
            '*' -> Operation.MULTIPLY
            '/' -> Operation.DIVIDE
            '(' -> Brace.OPEN
            ')' -> Brace.CLOSE
            else -> throw IllegalStateException("Unexpected symbol on position $iterator")
        }
    }

    inner class Number : State {
        override fun createToken(): Token {
            val startNum = iterator

            while (iterator != input.length && input[iterator].isDigit()) {
                iterator++
            }

            return NumberToken(input.substring(startNum, iterator).toInt())
        }
    }

    class Error(val pos: Int) : State

    object End : State

    private fun skipWhiteSpace() {
        while (iterator != input.length && input[iterator].isWhitespace()) {
            iterator++
        }
    }
}

interface State {
    fun createToken(): Token = StubToken
}

package ru.itmo.tokenizer

import ru.itmo.state.StateMachine

object Tokenizer {

    @JvmStatic
    fun tokenize(expression: String): List<Token> {
        val tokens: MutableList<Token> = mutableListOf()
        val stateMachine = StateMachine(expression)

        while (true) {
            when (val curState = stateMachine.nextState()) {
                is StateMachine.End   -> break
                is StateMachine.Error -> throw IllegalStateException("Unexpected symbol on position ${curState.pos + 1}")
                else                  -> tokens.add(curState.createToken())
            }
        }

        return tokens
    }
}

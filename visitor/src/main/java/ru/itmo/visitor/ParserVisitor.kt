package ru.itmo.visitor

import ru.itmo.tokenizer.Brace
import ru.itmo.tokenizer.NumberToken
import ru.itmo.tokenizer.Operation
import ru.itmo.tokenizer.Token
import java.lang.Exception
import java.lang.IllegalArgumentException

object ParserVisitor : TokenVisitor {
    private lateinit var stack: MutableList<Token>
    private lateinit var rpn: MutableList<Token>

    override fun visit(token: NumberToken) {
        rpn.add(token)
    }

    override fun visit(token: Brace) {
        when (token) {
            Brace.OPEN -> stack.add(token)
            Brace.CLOSE -> {
                clearStack {
                    val last = stack.lastOrNull()

                    last !is Brace || last != Brace.OPEN
                }
                stack.removeLast()
            }
        }
    }

    override fun visit(token: Operation) {
        var last = stack.lastOrNull()

        while (last is Operation && last.priority >= token.priority) {
            rpn.add(stack.removeLast())
            last = stack.lastOrNull()
        }

        stack.add(token)
    }

    @JvmStatic
    fun infixToRPN(tokens: List<Token>): List<Token> = try {
        stack = mutableListOf()
        rpn = mutableListOf()

        for (token in tokens) {
            token.accept(this)
        }

        clearStack()

        rpn
    } catch (e: Exception) {
        throw IllegalArgumentException("Incorrect expression")
    }

    private fun clearStack(condition: () -> Boolean = { true }) {
        while (stack.isNotEmpty() && condition()) {
            rpn.add(stack.removeLast())
        }
    }

}

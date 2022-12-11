package ru.itmo.tokenizer

import ru.itmo.visitor.TokenVisitor

interface Token {
    fun accept(tokenVisitor: TokenVisitor)
}

class NumberToken(val number: Int) : Token {
    override fun accept(tokenVisitor: TokenVisitor) {
        tokenVisitor.visit(this)
    }

    override fun toString(): String = number.toString()
}

enum class Brace(private val humanName: String) : Token {
    OPEN("("), CLOSE(")");


    override fun accept(tokenVisitor: TokenVisitor) {
        tokenVisitor.visit(this)
    }

    override fun toString(): String = humanName
}

enum class Operation(private val humanName: String, val exec: (Int, Int) -> Int, val priority: Int) : Token {
    PLUS("+", { a, b -> a + b }, 1),
    MINUS("-", { a, b -> a - b }, 1),
    MULTIPLY("*", { a, b -> a * b }, 2),
    DIVIDE("/", { a, b -> a / b }, 2);

    override fun accept(tokenVisitor: TokenVisitor) {
        tokenVisitor.visit(this)
    }

    override fun toString(): String = humanName
}

object StubToken : Token {
    override fun accept(tokenVisitor: TokenVisitor) {}
}

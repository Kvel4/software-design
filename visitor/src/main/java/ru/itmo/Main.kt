package ru.itmo

import ru.itmo.tokenizer.Tokenizer
import ru.itmo.visitor.CalcVisitor
import ru.itmo.visitor.ParserVisitor
import ru.itmo.visitor.PrintVisitor

fun main() {
    val expr = readln()

    val tokens = Tokenizer.tokenize(expr)
    val rpn = ParserVisitor.infixToRPN(tokens)

    println(PrintVisitor.printRpnExpr(rpn))
    println(CalcVisitor.calc(rpn))
}

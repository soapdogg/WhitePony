package compiler.parser.impl.internal

import compiler.core.ExpressionParserStackItem
import compiler.core.Stack
import compiler.core.Token

internal interface IExpressionStackPusher {

    fun pushUnary(
        tokens: List<Token>,
        startingPosition: Int,
        stack: Stack<ExpressionParserStackItem>
    ): Int

    fun pushBinaryOr(
        tokens: List<Token>,
        startingPosition: Int,
        stack: Stack<ExpressionParserStackItem>
    ): Int

    fun pushFactor(
        tokens: List<Token>,
        startingPosition: Int,
        stack: Stack<ExpressionParserStackItem>
    ): Int

    fun pushTerm(
        tokens: List<Token>,
        startingPosition: Int,
        stack: Stack<ExpressionParserStackItem>
    ): Int

    fun pushBinaryAssign(
        tokens: List<Token>,
        startingPosition: Int,
        stack: Stack<ExpressionParserStackItem>
    ): Int
}
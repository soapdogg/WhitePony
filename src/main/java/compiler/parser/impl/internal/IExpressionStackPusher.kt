package compiler.parser.impl.internal

import compiler.core.ExpressionParserStackItem
import compiler.core.Stack
import compiler.core.Token
import compiler.core.TokenType

internal interface IExpressionStackPusher {

    fun push(
        tokens: List<Token>,
        startingPosition: Int,
        acceptedTokenTypes: Set<TokenType>,
        acceptedTokenValues: Set<String>,
        location: Int,
        stack: Stack<ExpressionParserStackItem>
    ): Int

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
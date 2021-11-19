package compiler.parser.impl

import compiler.core.ExpressionParserStackItem
import compiler.core.Stack
import compiler.core.Token
import compiler.core.TokenType
import compiler.core.constants.ParserConstants
import compiler.core.constants.TokenizerConstants
import compiler.parser.impl.internal.IExpressionStackPusher
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class ExpressionStackPusher(
    private val tokenTypeAsserter: ITokenTypeAsserter
): IExpressionStackPusher {
    override fun push(
        tokens: List<Token>,
        startingPosition: Int,
        acceptedTokenTypes: Set<TokenType>,
        acceptedTokenValues: Set<String>,
        location: Int,
        stack: Stack<ExpressionParserStackItem>
    ): Int {
        val (token, positionAfterToken) = tokenTypeAsserter.assertTokenType(
            tokens,
            startingPosition,
            acceptedTokenTypes
        )
        tokenTypeAsserter.assertTokenValue(tokens, startingPosition, acceptedTokenValues)
        stack.push(ExpressionParserStackItem(location, token))
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1))
        return positionAfterToken
    }

    override fun pushUnary(
        tokens: List<Token>,
        startingPosition: Int,
        stack: Stack<ExpressionParserStackItem>
    ): Int {
        val (unaryToken, positionAfterUnary) = tokenTypeAsserter.assertTokenType(
            tokens,
            startingPosition,
            setOf(
                TokenType.PLUS_MINUS,
                TokenType.PRE_POST,
                TokenType.BIT_NEGATION,
                TokenType.UNARY_NOT
            )
        )
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_2, unaryToken))
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1))
        return positionAfterUnary
    }

    override fun pushBinaryOr(
        tokens: List<Token>,
        startingPosition: Int,
        stack: Stack<ExpressionParserStackItem>
    ): Int {
        val (_, positionAfterOr) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.BINARY_OR)
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_5))
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1))
        return positionAfterOr
    }
}
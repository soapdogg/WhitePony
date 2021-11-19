package compiler.parser.impl

import compiler.core.ExpressionParserStackItem
import compiler.core.Stack
import compiler.core.Token
import compiler.core.TokenType
import compiler.core.constants.ParserConstants
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
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, token))
        return positionAfterToken
    }
}
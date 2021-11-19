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

    override fun pushUnary(
        tokens: List<Token>,
        startingPosition: Int,
        stack: Stack<ExpressionParserStackItem>
    ): Int {
        val unaryToken = tokens[startingPosition]
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_2, unaryToken))
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1))
        return startingPosition + 1
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

    override fun pushTerm(tokens: List<Token>, startingPosition: Int, stack: Stack<ExpressionParserStackItem>): Int {
        val (termToken, positionAfterTerm) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.PLUS_MINUS)
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_14, termToken))
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1))
        return positionAfterTerm
    }

    override fun pushBinaryAssign(tokens: List<Token>, startingPosition: Int, stack: Stack<ExpressionParserStackItem>): Int {
        val (binaryAssignToken, positionAfterBinaryAssign) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, setOf(TokenType.BINARY_ASSIGN, TokenType.BINARY_ASSIGN_OP))
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_15, binaryAssignToken))
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1))
        return positionAfterBinaryAssign
    }
}
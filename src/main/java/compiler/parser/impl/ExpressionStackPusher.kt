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

    override fun pushFactor(
        tokens: List<Token>,
        startingPosition: Int,
        stack: Stack<ExpressionParserStackItem>
    ): Int {
        val(factorToken, positionAfterFactor) = tokenTypeAsserter.assertTokenValue(
            tokens,
            startingPosition,
            setOf(
                ParserConstants.MULTIPLY_OPERATOR,
                ParserConstants.DIVIDE_OPERATOR,
                ParserConstants.MODULUS_OPERATOR
            )
        )
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_13, factorToken))
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1))
        return positionAfterFactor
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
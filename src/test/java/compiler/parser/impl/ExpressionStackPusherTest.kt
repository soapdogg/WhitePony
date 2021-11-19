package compiler.parser.impl

import compiler.core.ExpressionParserStackItem
import compiler.core.Stack
import compiler.core.Token
import compiler.core.TokenType
import compiler.core.constants.ParserConstants
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ExpressionStackPusherTest {

    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)

    private val expressionStackPusher = ExpressionStackPusher(
        tokenTypeAsserter
    )

    @Test
    fun pushUnaryTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0
        val stack = Stack<ExpressionParserStackItem>()

        val unaryToken = Mockito.mock(Token::class.java)
        val positionAfterOr = 1
        Mockito.`when`(
            tokenTypeAsserter.assertTokenType(
                tokens,
                startingPosition,
                setOf(
                    TokenType.PLUS_MINUS,
                    TokenType.PRE_POST,
                    TokenType.BIT_NEGATION,
                    TokenType.UNARY_NOT
                )
            )
        ).thenReturn(Pair(unaryToken, positionAfterOr))

        val actual = expressionStackPusher.pushUnary(tokens, startingPosition, stack)
        Assertions.assertEquals(positionAfterOr, actual)

        val location1Item = stack.pop()
        Assertions.assertEquals(ParserConstants.LOCATION_1, location1Item.location)

        val location5Item = stack.pop()
        Assertions.assertEquals(ParserConstants.LOCATION_2, location5Item.location)
    }

    @Test
    fun pushBinaryOrTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0
        val stack = Stack<ExpressionParserStackItem>()

        val positionAfterOr = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.BINARY_OR)).thenReturn(Pair(Mockito.mock(Token::class.java), positionAfterOr))

        val actual = expressionStackPusher.pushBinaryOr(tokens, startingPosition, stack)
        Assertions.assertEquals(positionAfterOr, actual)

        val location1Item = stack.pop()
        Assertions.assertEquals(ParserConstants.LOCATION_1, location1Item.location)

        val location5Item = stack.pop()
        Assertions.assertEquals(ParserConstants.LOCATION_5, location5Item.location)
    }

    @Test
    fun pushFactorTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0
        val stack = Stack<ExpressionParserStackItem>()

        val factorToken = Mockito.mock(Token::class.java)
        val positionAfterFactor = 1
        Mockito.`when`(
            tokenTypeAsserter.assertTokenValue(
                tokens,
                startingPosition,
                setOf(
                    ParserConstants.MULTIPLY_OPERATOR,
                    ParserConstants.DIVIDE_OPERATOR,
                    ParserConstants.MODULUS_OPERATOR
                )
            )
        ).thenReturn(Pair(factorToken, positionAfterFactor))

        val actual = expressionStackPusher.pushFactor(tokens, startingPosition, stack)
        Assertions.assertEquals(positionAfterFactor, actual)

        val location1Item = stack.pop()
        Assertions.assertEquals(ParserConstants.LOCATION_1, location1Item.location)

        val location13Item = stack.pop()
        Assertions.assertEquals(ParserConstants.LOCATION_13, location13Item.location)
        Assertions.assertEquals(factorToken, location13Item.token)
    }

    @Test
    fun pushTermTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0
        val stack = Stack<ExpressionParserStackItem>()

        val termToken = Mockito.mock(Token::class.java)
        val positionAfterTerm = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.PLUS_MINUS)).thenReturn(Pair(termToken, positionAfterTerm))

        val actual = expressionStackPusher.pushTerm(tokens, startingPosition, stack)
        Assertions.assertEquals(positionAfterTerm, actual)

        val location1Item = stack.pop()
        Assertions.assertEquals(ParserConstants.LOCATION_1, location1Item.location)

        val location14Item = stack.pop()
        Assertions.assertEquals(ParserConstants.LOCATION_14, location14Item.location)
        Assertions.assertEquals(termToken, location14Item.token)
    }

    @Test
    fun pushBinaryAssignTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0
        val stack = Stack<ExpressionParserStackItem>()

        val binaryOpToken = Mockito.mock(Token::class.java)
        val positionAfterTerm = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, startingPosition, setOf(TokenType.BINARY_ASSIGN, TokenType.BINARY_ASSIGN_OP))).thenReturn(Pair(binaryOpToken, positionAfterTerm))

        val actual = expressionStackPusher.pushBinaryAssign(tokens, startingPosition, stack)
        Assertions.assertEquals(positionAfterTerm, actual)

        val location1Item = stack.pop()
        Assertions.assertEquals(ParserConstants.LOCATION_1, location1Item.location)

        val location15Item = stack.pop()
        Assertions.assertEquals(ParserConstants.LOCATION_15, location15Item.location)
        Assertions.assertEquals(binaryOpToken, location15Item.token)
    }
}
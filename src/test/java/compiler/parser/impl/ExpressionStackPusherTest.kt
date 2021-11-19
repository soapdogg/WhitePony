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
    fun pushTest() {
        val tokens = listOf<Token>()
        val startingPosition = 0
        val stack = Stack<ExpressionParserStackItem>()
        val tokenTypeSet = setOf<TokenType>()
        val tokenValueSet = setOf<String>()
        val location = 2
        val token = Mockito.mock(Token::class.java)
        val positionAfterToken = 1
        Mockito.`when`(
            tokenTypeAsserter.assertTokenType(
                tokens,
                startingPosition,
                tokenTypeSet
            )
        ).thenReturn(Pair(token, positionAfterToken))

        val actual = expressionStackPusher.push(
            tokens,
            startingPosition,
            tokenTypeSet,
            tokenValueSet,
            location,
            stack
        )
        Assertions.assertEquals(positionAfterToken, actual)

        val location1Item = stack.pop()
        Assertions.assertEquals(ParserConstants.LOCATION_1, location1Item.location)

        val returnLocationItem = stack.pop()
        Assertions.assertEquals(location, returnLocationItem.location)
        Assertions.assertEquals(token, returnLocationItem.token)

        Mockito.verify(tokenTypeAsserter).assertTokenValue(tokens, startingPosition, tokenValueSet)
    }

}
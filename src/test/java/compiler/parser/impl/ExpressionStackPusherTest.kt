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
}
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
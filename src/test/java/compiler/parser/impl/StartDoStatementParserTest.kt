package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocations
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StartDoStatementParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val startDoStatementParser = StartDoStatementParser(tokenTypeAsserter)

    @Test
    fun parseTest(){
        val tokens = listOf<Token>()
        val tokenPosition = 0
        val stack = Stack<Int>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val positionAfterDo = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.DO)).thenReturn(Pair(Mockito.mock(Token::class.java), positionAfterDo))
        val actual = startDoStatementParser.parse(tokens, tokenPosition, stack, resultStack, expressionStack, numberOfStatementsBlockStack)
        Assertions.assertEquals(positionAfterDo, actual)
        val startLocation = stack.pop()
        Assertions.assertEquals(StatementParserLocations.LOCATION_START, startLocation)
        val doLocation = stack.pop()
        Assertions.assertEquals(StatementParserLocations.LOCATION_DO, doLocation)
    }
}
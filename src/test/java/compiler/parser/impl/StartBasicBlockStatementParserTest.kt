package compiler.parser.impl

import compiler.core.constants.PrinterConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementParserLocation
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.ITokenTypeAsserter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StartBasicBlockStatementParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)
    private val startBasicBlockStatementParser = StartBasicBlockStatementParser(tokenTypeAsserter)

    @Test
    fun nonEmptyBasicBlockTest() {
        val leftBraceToken = Mockito.mock(Token::class.java)
        val secondToken = Mockito.mock(Token::class.java)
        val tokens = listOf(leftBraceToken, secondToken)
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val positionAfterLeftBrace = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.LEFT_BRACE)).thenReturn(Pair(Mockito.mock(Token::class.java), positionAfterLeftBrace))

        val tokenType = TokenType.WHILE
        Mockito.`when`(secondToken.type).thenReturn(tokenType)

        val actual = startBasicBlockStatementParser.parse(tokens, tokenPosition, stack, resultStack, expressionStack, numberOfStatementsBlockStack)
        Assertions.assertEquals(positionAfterLeftBrace, actual)
        val start = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_START, start)
        val basicBlock = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_BASIC_BLOCK, basicBlock)
        val number = numberOfStatementsBlockStack.pop()
        Assertions.assertEquals(PrinterConstants.ONE.toInt(), number)
    }

    @Test
    fun emptyBasicBlockTest() {
        val leftBraceToken = Mockito.mock(Token::class.java)
        val secondToken = Mockito.mock(Token::class.java)
        val tokens = listOf(leftBraceToken, secondToken)
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val positionAfterLeftBrace = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.LEFT_BRACE)).thenReturn(Pair(Mockito.mock(Token::class.java), positionAfterLeftBrace))

        val tokenType = TokenType.RIGHT_BRACE
        Mockito.`when`(secondToken.type).thenReturn(tokenType)

        val positionAfterRightBrace = 2
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, positionAfterLeftBrace, TokenType.RIGHT_BRACE)).thenReturn(Pair(Mockito.mock(Token::class.java), positionAfterRightBrace))

        val actual = startBasicBlockStatementParser.parse(tokens, tokenPosition, stack, resultStack, expressionStack, numberOfStatementsBlockStack)
        Assertions.assertEquals(positionAfterRightBrace, actual)
        val top = resultStack.pop() as ParsedBasicBlockNode
        Assertions.assertEquals(listOf<IParsedStatementNode>(), top.statements)
    }
}
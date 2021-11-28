package compiler.parser.impl

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

class EndBasicBlockStatementParserTest {
    private val tokenTypeAsserter = Mockito.mock(ITokenTypeAsserter::class.java)

    private val endBasicBlockStatementParser = EndBasicBlockStatementParser(tokenTypeAsserter)

    @Test
    fun nextTokenNotRightBraceTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val tokenType = TokenType.DO
        Mockito.`when`(token.type).thenReturn(tokenType)

        val number = 1
        numberOfStatementsBlockStack.push(number)

        val actual = endBasicBlockStatementParser.parse(
            tokens, tokenPosition, stack, resultStack, expressionStack, numberOfStatementsBlockStack
        )

        Assertions.assertEquals(tokenPosition, actual)
        val nextNumber = numberOfStatementsBlockStack.pop()
        Assertions.assertEquals(number + 1, nextNumber)

        val start = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_START, start)

        val basicBlock = stack.pop()
        Assertions.assertEquals(StatementParserLocation.LOCATION_BASIC_BLOCK, basicBlock)
    }

    @Test
    fun nextTokenIsRightBraceTest() {
        val token = Mockito.mock(Token::class.java)
        val tokens = listOf(token)
        val tokenPosition = 0
        val stack = Stack<StatementParserLocation>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        val numberOfStatementsBlockStack = Stack<Int>()

        val tokenType = TokenType.RIGHT_BRACE
        Mockito.`when`(token.type).thenReturn(tokenType)

        val positionAfterRightBrace = 1
        Mockito.`when`(tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.RIGHT_BRACE)).thenReturn(Pair(Mockito.mock(Token::class.java), positionAfterRightBrace))

        val statement1 = Mockito.mock(IParsedStatementNode::class.java)
        resultStack.push(statement1)
        val statement2 = Mockito.mock(IParsedStatementNode::class.java)
        resultStack.push(statement2)

        val number = 2
        numberOfStatementsBlockStack.push(number)

        val actual = endBasicBlockStatementParser.parse(
            tokens, tokenPosition, stack, resultStack, expressionStack, numberOfStatementsBlockStack
        )

        Assertions.assertEquals(positionAfterRightBrace, actual)

        val basicBlock = resultStack.pop() as ParsedBasicBlockNode
        Assertions.assertEquals(listOf(statement1, statement2), basicBlock.statements)
    }
}
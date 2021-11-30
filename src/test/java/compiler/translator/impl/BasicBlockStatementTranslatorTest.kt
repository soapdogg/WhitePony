package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BasicBlockStatementTranslatorTest {
    private val basicBlockStatementTranslator = BasicBlockStatementTranslator()

    @Test
    fun startLocationTest() {
        val node = Mockito.mock(ParsedBasicBlockNode::class.java)
        val location = StatementTranslatorLocation.START
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mutableMapOf<String,String>()
        val stack = Stack<StatementTranslatorStackItem>()
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        val labelStack = Stack<String>()

        val statement1 = Mockito.mock(IParsedStatementNode::class.java)
        val statement2 = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.statements).thenReturn(listOf(statement1, statement2))

        val (actualTemp, actualLabel) = basicBlockStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempCounter, actualTemp)
        Assertions.assertEquals(labelCounter, actualLabel)

        val s1 = stack.pop()
        Assertions.assertEquals(statement1, s1.node)
        val s2 = stack.pop()
        Assertions.assertEquals(statement2, s2.node)
        val top = stack.pop()
        Assertions.assertEquals(node, top.node)
    }

    @Test
    fun endLocationTest() {
        val node = Mockito.mock(ParsedBasicBlockNode::class.java)
        val location = StatementTranslatorLocation.END
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mutableMapOf<String,String>()
        val stack = Stack<StatementTranslatorStackItem>()
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        val labelStack = Stack<String>()

        val statement1 = Mockito.mock(IParsedStatementNode::class.java)
        val statement2 = Mockito.mock(IParsedStatementNode::class.java)
        Mockito.`when`(node.statements).thenReturn(listOf(statement1, statement2))

        val result1 = Mockito.mock(ITranslatedStatementNode::class.java)
        val result2 = Mockito.mock(ITranslatedStatementNode::class.java)
        resultStack.push(result1)
        resultStack.push(result2)

        val (actualTemp, actualLabel) = basicBlockStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempCounter, actualTemp)
        Assertions.assertEquals(labelCounter, actualLabel)

        val top = resultStack.pop() as TranslatedBasicBlockNode
        Assertions.assertEquals(listOf(result1, result2), top.statements)
    }
}
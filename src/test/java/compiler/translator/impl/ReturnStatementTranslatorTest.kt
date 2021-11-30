package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.nodes.parsed.ParsedReturnNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedExpressionStatementNode
import compiler.core.nodes.translated.TranslatedReturnNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.IExpressionStatementTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ReturnStatementTranslatorTest {

    private val expressionStatementTranslator = Mockito.mock(IExpressionStatementTranslator::class.java)

    private val returnStatementTranslator = ReturnStatementTranslator(expressionStatementTranslator)

    @Test
    fun translateTest() {
        val node = Mockito.mock(ParsedReturnNode::class.java)
        val location = StatementTranslatorLocation.END
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mapOf<String,String>()
        val stack = Stack<StatementTranslatorStackItem>()
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        val labelStack = Stack<String>()

        val expressionStatement = Mockito.mock(ParsedExpressionStatementNode::class.java)
        Mockito.`when`(node.expressionStatement).thenReturn(expressionStatement)

        val translatedExpressionStatement = Mockito.mock(TranslatedExpressionStatementNode::class.java)
        val tempAfterExpression = 2
        Mockito.`when`(expressionStatementTranslator.translate(expressionStatement, variableToTypeMap, tempCounter)).thenReturn(Pair(translatedExpressionStatement, tempAfterExpression))

        val (actualTemp, actualLabel) = returnStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempAfterExpression, actualTemp)
        Assertions.assertEquals(labelCounter, actualLabel)
        val top = resultStack.pop()
        Assertions.assertEquals(TranslatedReturnNode(translatedExpressionStatement), top)
    }
}
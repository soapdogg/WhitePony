package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.frontend.translator.impl.internal.IExpressionTranslatorOrchestrator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ExpressionStatementTranslatorTest {
    private val expressionTranslator = Mockito.mock(IExpressionTranslatorOrchestrator::class.java)

    private val expressionStatementTranslator = ExpressionStatementTranslator(expressionTranslator)

    @Test
    fun translateTest() {
        val node = Mockito.mock(ParsedExpressionStatementNode::class.java)
        val location = StatementTranslatorLocation.END
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mutableMapOf<String,String>()
        val stack = Stack<StatementTranslatorStackItem>()
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        val labelStack = Stack<String>()

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.expressionNode).thenReturn(expression)

        val translatedExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        val tempAfterExpression = 2
        Mockito.`when`(expressionTranslator.translate(expression, variableToTypeMap, tempCounter)).thenReturn(Pair(translatedExpression, tempAfterExpression))

        val (actualTemp, actualLabel) = expressionStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempAfterExpression, actualTemp)
        Assertions.assertEquals(labelCounter, actualLabel)
        val top = resultStack.pop()
        Assertions.assertEquals(TranslatedExpressionStatementNode(translatedExpression), top)
    }
}
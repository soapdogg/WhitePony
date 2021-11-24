package compiler.translator.impl

import compiler.core.IParsedExpressionNode
import compiler.core.ITranslatedExpressionNode
import compiler.core.ParsedExpressionStatementNode
import compiler.core.TranslatedExpressionNode
import compiler.translator.impl.internal.IExpressionTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ExpressionStatementTranslatorTest {
    private val expressionTranslator = Mockito.mock(IExpressionTranslator::class.java)

    private val expressionStatementTranslator = ExpressionStatementTranslator(expressionTranslator)

    @Test
    fun translateTest() {
        val expressionStatement = Mockito.mock(ParsedExpressionStatementNode::class.java)
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 0

        val expression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(expressionStatement.expressionNode).thenReturn(expression)

        val translatedExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        val t = 2
        Mockito.`when`(expressionTranslator.translate(expression, variableToTypeMap, tempCounter)).thenReturn(Pair(translatedExpression, t))

        val actual = expressionStatementTranslator.translate(expressionStatement, variableToTypeMap, tempCounter)
        Assertions.assertEquals(translatedExpression, actual.first.expression)
        Assertions.assertEquals(t, actual.second)
    }
}
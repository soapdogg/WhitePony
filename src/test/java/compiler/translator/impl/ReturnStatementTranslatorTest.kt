package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedExpressionStatementNode
import compiler.core.nodes.parsed.ParsedReturnNode
import compiler.core.nodes.translated.TranslatedExpressionStatementNode
import compiler.translator.impl.internal.IExpressionStatementTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ReturnStatementTranslatorTest {

    private val expressionStatementTranslator = Mockito.mock(IExpressionStatementTranslator::class.java)

    private val returnStatementTranslator = ReturnStatementTranslator(expressionStatementTranslator)

    @Test
    fun translateTest() {
        val returnNode = Mockito.mock(ParsedReturnNode::class.java)
        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 0

        val expressionStatement = Mockito.mock(ParsedExpressionStatementNode::class.java)
        Mockito.`when`(returnNode.expressionStatement).thenReturn(expressionStatement)

        val translatedExpressionStatement = Mockito.mock(TranslatedExpressionStatementNode::class.java)
        val t = 2
        Mockito.`when`(expressionStatementTranslator.translate(expressionStatement, variableToTypeMap, tempCounter)).thenReturn(Pair(translatedExpressionStatement, t))

        val actual = returnStatementTranslator.translate(returnNode, variableToTypeMap, tempCounter)
        Assertions.assertEquals(translatedExpressionStatement, actual.first.expressionStatement)
        Assertions.assertEquals(t, actual.second)
    }
}
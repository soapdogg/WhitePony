package compiler.translator.impl

import compiler.core.ParsedExpressionStatementNode
import compiler.core.ParsedReturnNode
import compiler.core.TranslatedExpressionStatementNode
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
        val labelCounter = 0
        val tempCounter = 0

        val expressionStatement = Mockito.mock(ParsedExpressionStatementNode::class.java)
        Mockito.`when`(returnNode.expressionStatement).thenReturn(expressionStatement)

        val translatedExpressionStatement = Mockito.mock(TranslatedExpressionStatementNode::class.java)
        Mockito.`when`(expressionStatementTranslator.translate(expressionStatement, labelCounter, tempCounter)).thenReturn(translatedExpressionStatement)

        val actual = returnStatementTranslator.translate(returnNode, labelCounter, tempCounter)
        Assertions.assertEquals(translatedExpressionStatement, actual.expressionStatement)
    }
}
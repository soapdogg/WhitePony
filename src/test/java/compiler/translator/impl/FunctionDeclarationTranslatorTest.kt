package compiler.translator.impl

import compiler.core.ParsedBasicBlockNode
import compiler.core.ParsedFunctionDeclarationNode
import compiler.core.TranslatedBasicBlockNode
import compiler.translator.impl.internal.IStatementTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class FunctionDeclarationTranslatorTest {
    private val statementTranslator = Mockito.mock(IStatementTranslator::class.java)

    private val functionDeclarationTranslator = FunctionDeclarationTranslator(statementTranslator)

    @Test
    fun translateTest() {
        val functionDeclarationNode = Mockito.mock(ParsedFunctionDeclarationNode::class.java)

        val parsedStatementNode = Mockito.mock(ParsedBasicBlockNode::class.java)
        Mockito.`when`(functionDeclarationNode.basicBlockNode).thenReturn(parsedStatementNode)

        val translatedStatementNode = Mockito.mock(TranslatedBasicBlockNode::class.java)
        Mockito.`when`(statementTranslator.translate(parsedStatementNode)).thenReturn(translatedStatementNode)

        val type = "type"
        Mockito.`when`(functionDeclarationNode.type).thenReturn(type)

        val functionName = "functionName"
        Mockito.`when`(functionDeclarationNode.functionName).thenReturn(functionName)

        val actual = functionDeclarationTranslator.translate(functionDeclarationNode)

        Assertions.assertEquals(type, actual.type)
        Assertions.assertEquals(functionName, actual.functionName)
        Assertions.assertEquals(translatedStatementNode, actual.basicBlockNode)
    }
}
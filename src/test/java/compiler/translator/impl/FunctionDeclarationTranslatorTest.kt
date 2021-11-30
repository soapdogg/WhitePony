package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.nodes.parsed.ParsedFunctionDeclarationNode
import compiler.core.nodes.translated.TranslatedBasicBlockNode
import compiler.translator.impl.internal.IStatementTranslatorOrchestrator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class FunctionDeclarationTranslatorTest {
    private val statementTranslator = Mockito.mock(IStatementTranslatorOrchestrator::class.java)

    private val functionDeclarationTranslator = FunctionDeclarationTranslator(statementTranslator)

    @Test
    fun translateTest() {
        val functionDeclarationNode = Mockito.mock(ParsedFunctionDeclarationNode::class.java)
        val variableToTypeMap = mutableMapOf<String, String>()

        val parsedStatementNode = Mockito.mock(ParsedBasicBlockNode::class.java)
        Mockito.`when`(functionDeclarationNode.basicBlockNode).thenReturn(parsedStatementNode)

        val translatedStatementNode = Mockito.mock(TranslatedBasicBlockNode::class.java)
        Mockito.`when`(statementTranslator.translate(parsedStatementNode, variableToTypeMap)).thenReturn(translatedStatementNode)

        val type = "type"
        Mockito.`when`(functionDeclarationNode.type).thenReturn(type)

        val functionName = "functionName"
        Mockito.`when`(functionDeclarationNode.functionName).thenReturn(functionName)

        val actual = functionDeclarationTranslator.translate(functionDeclarationNode, variableToTypeMap)

        Assertions.assertEquals(type, actual.type)
        Assertions.assertEquals(functionName, actual.functionName)
        Assertions.assertEquals(translatedStatementNode, actual.basicBlockNode)
    }
}
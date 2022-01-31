package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedDeclarationStatementNode
import compiler.core.nodes.translated.ITranslatedDeclarationStatementNode
import compiler.core.nodes.parsed.ParsedProgramRootNode
import compiler.frontend.translator.impl.internal.IDeclarationStatementTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TranslatorTest {

    private val declarationStatementTranslator = Mockito.mock(IDeclarationStatementTranslator::class.java)

    private val translator = Translator(declarationStatementTranslator)

    @Test
    fun translateTest() {
        val rootNode = Mockito.mock(ParsedProgramRootNode::class.java)
        val variableToTypeMap = mutableMapOf<String, String>()

        val parsedDeclarationStatementNode = Mockito.mock(IParsedDeclarationStatementNode::class.java)
        Mockito.`when`(rootNode.declarationStatements).thenReturn(listOf(parsedDeclarationStatementNode))

        val translatedDeclarationStatementNode = Mockito.mock(ITranslatedDeclarationStatementNode::class.java)
        Mockito.`when`(declarationStatementTranslator.translate(parsedDeclarationStatementNode, variableToTypeMap)).thenReturn(translatedDeclarationStatementNode)

        val actual = translator.translate(rootNode)

        Assertions.assertEquals(translatedDeclarationStatementNode, actual.declarationStatements[0])
    }
}
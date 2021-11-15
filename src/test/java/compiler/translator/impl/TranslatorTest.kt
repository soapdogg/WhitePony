package compiler.translator.impl

import compiler.core.IParsedDeclarationStatementNode
import compiler.core.ITranslatedDeclarationStatementNode
import compiler.core.ParsedProgramRootNode
import compiler.translator.impl.internal.IDeclarationStatementTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TranslatorTest {

    private val declarationStatementTranslator = Mockito.mock(IDeclarationStatementTranslator::class.java)

    private val translator = Translator(declarationStatementTranslator)

    @Test
    fun translateTest() {
        val rootNode = Mockito.mock(ParsedProgramRootNode::class.java)

        val parsedDeclarationStatementNode = Mockito.mock(IParsedDeclarationStatementNode::class.java)
        Mockito.`when`(rootNode.declarationStatements).thenReturn(listOf(parsedDeclarationStatementNode))

        val translatedDeclarationStatementNode = Mockito.mock(ITranslatedDeclarationStatementNode::class.java)
        Mockito.`when`(declarationStatementTranslator.translate(parsedDeclarationStatementNode)).thenReturn(translatedDeclarationStatementNode)

        val actual = translator.translate(rootNode)

        Assertions.assertEquals(translatedDeclarationStatementNode, actual.declarationStatementNodes[0])
    }
}
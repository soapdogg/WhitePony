package compiler

import compiler.core.nodes.parsed.ParsedProgramRootNode
import compiler.core.tokenizer.Token
import compiler.core.nodes.translated.TranslatedProgramRootNode
import compiler.parser.IParser
import compiler.printer.IPrinter
import compiler.tokenizer.ITokenizer
import compiler.translator.ITranslator
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class CompilerTest {

    private val tokenizer = Mockito.mock(ITokenizer::class.java)
    private val parser = Mockito.mock(IParser::class.java)
    private val printer = Mockito.mock(IPrinter::class.java)
    private val translator = Mockito.mock(ITranslator::class.java)

    private val compiler = Compiler(
        tokenizer,
        parser,
        translator,
        printer
    )

    @Test
    fun compileTest() {
        val program = "program"

        val tokens = listOf<Token>()
        Mockito.`when`(tokenizer.tokenize(program)).thenReturn(tokens)

        val parseTree = Mockito.mock(ParsedProgramRootNode::class.java)
        Mockito.`when`(parser.parse(
            tokens,
        )).thenReturn(parseTree)

        val translatedTree = Mockito.mock(TranslatedProgramRootNode::class.java)
        Mockito.`when`(translator.translate(parseTree)).thenReturn(translatedTree)

        val actual = compiler.compile(
            program,
        )
    }
}
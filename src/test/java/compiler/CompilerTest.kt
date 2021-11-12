package compiler

import compiler.core.ProgramRootNode
import compiler.core.Token
import compiler.parser.IParser
import compiler.tokenizer.ITokenizer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class CompilerTest {

    private val tokenizer = Mockito.mock(ITokenizer::class.java)
    private val parser = Mockito.mock(IParser::class.java)

    private val compiler = Compiler(
        tokenizer,
        parser
    )

    @Test
    fun compileTest() {
        val program = "program"

        val tokens = listOf<Token>()
        Mockito.`when`(tokenizer.tokenize(program)).thenReturn(tokens)

        val parseTree = Mockito.mock(ProgramRootNode::class.java)
        Mockito.`when`(parser.parse(tokens)).thenReturn(parseTree)

        val actual = compiler.compile(program)
        Assertions.assertEquals(parseTree.toString(), actual)
    }
}
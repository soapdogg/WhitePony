package compiler.tokenizer.impl

import compiler.core.tokenizer.Token
import compiler.tokenizer.impl.internal.IWordTokenizer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TokenizerTest {


    private val wordTokenizer = Mockito.mock(IWordTokenizer::class.java)
    private val tokenizer = Tokenizer(
        wordTokenizer
    )

    @Test
    fun tokenizeTest() {
        val word1 = "word1"
        val word2 = "word2"
        val line = "\t$word1    $word2"

        val token1 = Mockito.mock(Token::class.java)
        val token2 = Mockito.mock(Token::class.java)


        Mockito.`when`(wordTokenizer.tokenizeWord(word1)).thenReturn(listOf(token1))
        Mockito.`when`(wordTokenizer.tokenizeWord(word2)).thenReturn(listOf(token2))

        val actual = tokenizer.tokenize(line)
        Assertions.assertEquals(2, actual.size)
        Assertions.assertEquals(token1, actual[0])
        Assertions.assertEquals(token2, actual[1])
    }
}
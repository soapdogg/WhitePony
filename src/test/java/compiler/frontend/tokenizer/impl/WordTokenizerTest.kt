package compiler.frontend.tokenizer.impl

import compiler.core.tokenizer.Token
import compiler.frontend.tokenizer.impl.internal.IMatchFinder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.lang.Exception

class WordTokenizerTest {

    private val matchFinder = Mockito.mock(IMatchFinder::class.java)
    private val wordTokenizer = WordTokenizer(matchFinder)

    @Test
    fun wordTokenizerSuccessfulMatchTest() {
        val word = "word"
        val token = Mockito.mock(Token::class.java)
        val remainingText = ""
        Mockito.`when`(matchFinder.findMatch(word)).thenReturn(Pair(token, remainingText))

        val actual = wordTokenizer.tokenizeWord(word)
        Assertions.assertEquals(token, actual[0])
    }

    @Test
    fun wordTokenizerUnsuccessfulMatchTest() {
        val word = "word"
        Mockito.`when`(matchFinder.findMatch(word)).thenReturn(Pair(null, word))

        Assertions.assertThrows(
            Exception::class.java,
        ) {
            wordTokenizer.tokenizeWord(word)
        }
    }
}
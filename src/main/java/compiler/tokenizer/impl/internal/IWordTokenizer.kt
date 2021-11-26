package compiler.tokenizer.impl.internal

import compiler.core.tokenizer.Token

internal interface IWordTokenizer {
    fun tokenizeWord(word: String): List<Token>
}
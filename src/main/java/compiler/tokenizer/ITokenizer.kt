package compiler.tokenizer

import compiler.core.tokenizer.Token

interface ITokenizer {
    fun tokenize(input: String): List<Token>
}
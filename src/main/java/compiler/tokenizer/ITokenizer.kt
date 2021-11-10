package compiler.tokenizer

import compiler.core.Token

interface ITokenizer {
    fun tokenize(input: String): List<Token>
}
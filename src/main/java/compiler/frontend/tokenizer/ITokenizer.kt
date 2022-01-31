package compiler.frontend.tokenizer

import compiler.core.tokenizer.Token

interface ITokenizer {
    fun tokenize(input: String): List<Token>
}
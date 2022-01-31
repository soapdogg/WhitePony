package compiler.frontend.tokenizer.impl

import compiler.core.tokenizer.Token
import compiler.core.constants.TokenizerConstants
import compiler.frontend.tokenizer.ITokenizer
import compiler.frontend.tokenizer.impl.internal.IWordTokenizer

internal class Tokenizer (
    private val wordTokenizer: IWordTokenizer
): ITokenizer {

    override fun tokenize(input: String): List<Token> {
        val trimmedLine = input.trim{ it <= ' '}
        val words = trimmedLine.split(TokenizerConstants.WHITE_SPACE_PATTERN.toRegex())
        return words.map {
            word -> wordTokenizer.tokenizeWord(word)
        }.flatMap { it.toList() }
    }
}
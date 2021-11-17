package compiler.tokenizer.impl

import compiler.core.Token
import compiler.core.constants.TokenizerConstants
import compiler.tokenizer.ITokenizer
import compiler.tokenizer.impl.internal.IWordTokenizer

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
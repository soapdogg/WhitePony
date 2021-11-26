package compiler.tokenizer.impl.internal

import compiler.core.tokenizer.Token

internal interface IMatchFinder {
    fun findMatch(word: String): Pair<Token?, String>
}
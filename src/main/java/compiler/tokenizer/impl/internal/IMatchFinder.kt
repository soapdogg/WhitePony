package compiler.tokenizer.impl.internal

import compiler.core.Token

internal interface IMatchFinder {
    fun findMatch(word: String): Pair<Token?, String>
}
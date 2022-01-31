package compiler.frontend.parser.impl.internal

internal interface IOperatorPrecedenceDeterminer {
    fun determinerIfLookaheadIsLowerPrecedenceThanCurrent(
        current: String,
        lookahead: String
    ): Boolean
}
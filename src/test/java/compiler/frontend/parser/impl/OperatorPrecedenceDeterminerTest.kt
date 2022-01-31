package compiler.frontend.parser.impl

import compiler.core.constants.TokenizerConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class OperatorPrecedenceDeterminerTest {
    private val operatorPrecedenceMap = mapOf(
        TokenizerConstants.PLUS_OPERATOR to 0,
        TokenizerConstants.AND_OPERATOR to 1,
        TokenizerConstants.ASSIGN_OPERATOR to 2
    )
    private val assignOperatorSet = setOf(
        TokenizerConstants.ASSIGN_OPERATOR
    )

    private val operatorPrecedenceDeterminer = OperatorPrecedenceDeterminer(
        operatorPrecedenceMap,
        assignOperatorSet
    )

    @Test
    fun operatorPrecedenceMapDoesNotContainKeyTest() {
        val current = TokenizerConstants.PLUS_OPERATOR
        val lookahead = "lookahead"
        val result = operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(current, lookahead)
        Assertions.assertFalse(result)
    }

    @Test
    fun assignOperatorSetContainsLookaheadTest() {
        val current = TokenizerConstants.PLUS_OPERATOR
        val lookahead = TokenizerConstants.ASSIGN_OPERATOR
        val result = operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(current, lookahead)
        Assertions.assertTrue(result)
    }

    @Test
    fun currentAndLookaheadAreComparedTest() {
        val current = TokenizerConstants.PLUS_OPERATOR
        val lookahead = TokenizerConstants.AND_OPERATOR
        val result = operatorPrecedenceDeterminer.determinerIfLookaheadIsLowerPrecedenceThanCurrent(current, lookahead)
        Assertions.assertFalse(result)
    }
}
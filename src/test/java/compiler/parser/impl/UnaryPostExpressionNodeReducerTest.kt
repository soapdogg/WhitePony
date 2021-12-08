package compiler.parser.impl

import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPostOperatorExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryPostExpressionNodeReducerTest {
    private val unaryPostExpressionNodeReducer = UnaryPostExpressionNodeReducer()

    @Test
    fun reduceTest() {
        val rightNode = Mockito.mock(IParsedExpressionNode::class.java)
        val operator = "operator"
        val parseStack = Stack<IShiftReduceStackItem>()

        unaryPostExpressionNodeReducer.reduceToExpressionNode(rightNode, operator, parseStack)
        val top = parseStack.pop() as NodeShiftReduceStackItem
        val node = top.node as ParsedUnaryPostOperatorExpressionNode
        Assertions.assertEquals(rightNode, node.expression)
        Assertions.assertEquals(TokenizerConstants.MINUS_OPERATOR, node.operator)
        Assertions.assertEquals(TokenizerConstants.PLUS_OPERATOR, node.oppositeOperator)
    }
}
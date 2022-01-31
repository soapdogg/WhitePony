package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedInnerExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class InnerExpressionNodeReducerTest {
    private val innerExpressionNodeReducer = InnerExpressionNodeReducer()

    @Test
    fun reduceTest() {
        val rightNode = Mockito.mock(IParsedExpressionNode::class.java)
        val operator = "operator"
        val parseStack = Stack<IShiftReduceStackItem>()

        val leftParentheses = Mockito.mock(IShiftReduceStackItem::class.java)
        parseStack.push(leftParentheses)

        innerExpressionNodeReducer.reduceToExpressionNode(rightNode, operator, parseStack)
        val top = parseStack.pop() as NodeShiftReduceStackItem
        val node = top.node as ParsedInnerExpressionNode
        Assertions.assertEquals(rightNode, node.expression)
    }
}
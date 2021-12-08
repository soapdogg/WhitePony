package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryNotOperatorExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPreOperatorExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryNotExpressionNodeReducerTest {
    private val unaryNotExpressionNodeReducer = UnaryNotExpressionNodeReducer()

    @Test
    fun reduceTest() {
        val insideNode = Mockito.mock(IParsedExpressionNode::class.java)
        val operator = "operator"
        val parseStack = Stack<IShiftReduceStackItem>()

        unaryNotExpressionNodeReducer.reduceToUnaryNode(insideNode, operator, parseStack)
        val top = parseStack.pop() as NodeShiftReduceStackItem
        val node = top.node as ParsedUnaryNotOperatorExpressionNode
        Assertions.assertEquals(insideNode, node.expression)
    }
}
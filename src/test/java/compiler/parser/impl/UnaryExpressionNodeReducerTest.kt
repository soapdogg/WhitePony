package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryExpressionNodeReducerTest {
    private val unaryExpressionNodeReducer = UnaryExpressionNodeReducer()

    @Test
    fun reduceTest() {
        val insideNode = Mockito.mock(IParsedExpressionNode::class.java)
        val operator = "operator"
        val parseStack = Stack<IShiftReduceStackItem>()

        unaryExpressionNodeReducer.reduceToExpressionNode(insideNode, operator, parseStack)
        val top = parseStack.pop() as NodeShiftReduceStackItem
        val node = top.node as ParsedUnaryExpressionNode
        Assertions.assertEquals(insideNode, node.expression)
        Assertions.assertEquals(operator, node.operator)
    }
}
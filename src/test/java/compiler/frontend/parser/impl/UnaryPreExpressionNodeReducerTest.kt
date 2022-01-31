package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedUnaryPreOperatorExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class UnaryPreExpressionNodeReducerTest {
    private val unaryPreExpressionOperatorNodeReducer = UnaryPreExpressionOperatorNodeReducer()

    @Test
    fun reduceTest() {
        val insideNode = Mockito.mock(IParsedExpressionNode::class.java)
        val operator = "operator"
        val parseStack = Stack<IShiftReduceStackItem>()

        unaryPreExpressionOperatorNodeReducer.reduceToExpressionNode(insideNode, operator, parseStack)
        val top = parseStack.pop() as NodeShiftReduceStackItem
        val node = top.node as ParsedUnaryPreOperatorExpressionNode
        Assertions.assertEquals(insideNode, node.expression)
        Assertions.assertEquals(operator[0].toString(), node.operator)
    }
}
package compiler.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryAssignExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryAssignExpressionNodeReducerTest {
    private val binaryAssignExpressionNodeReducer = BinaryAssignExpressionNodeReducer()

    @Test
    fun reduceTest() {
        val rightNode = Mockito.mock(IParsedExpressionNode::class.java)
        val operator = "operator"
        val parseStack = Stack<IShiftReduceStackItem>()

        val leftItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        parseStack.push(leftItem)

        val leftNode = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(leftItem.node).thenReturn(leftNode)

        binaryAssignExpressionNodeReducer.reduceToExpressionNode(rightNode, operator, parseStack)
        val top = parseStack.pop() as NodeShiftReduceStackItem
        val resultNode = top.node as ParsedBinaryAssignExpressionNode
        Assertions.assertEquals(leftNode, resultNode.leftExpression)
        Assertions.assertEquals(rightNode, resultNode.rightExpression)
    }
}
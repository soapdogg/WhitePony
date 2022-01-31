package compiler.frontend.parser.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryArrayExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.stack.IShiftReduceStackItem
import compiler.core.stack.NodeShiftReduceStackItem
import compiler.core.stack.Stack
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryArrayExpressionNodeReducerTest {
    private val binaryArrayExpressionNodeReducer = BinaryArrayExpressionNodeReducer()

    @Test
    fun reduceTest() {
        val rightNode = Mockito.mock(IParsedExpressionNode::class.java)
        val operator = "operator"
        val parseStack = Stack<IShiftReduceStackItem>()

        val variableItem = Mockito.mock(NodeShiftReduceStackItem::class.java)
        parseStack.push(variableItem)

        val leftBracket = Mockito.mock(IShiftReduceStackItem::class.java)
        parseStack.push(leftBracket)

        val node = Mockito.mock(ParsedVariableExpressionNode::class.java)
        Mockito.`when`(variableItem.node).thenReturn(node)

        binaryArrayExpressionNodeReducer.reduceToExpressionNode(rightNode, operator, parseStack)
        val top = parseStack.pop() as NodeShiftReduceStackItem
        val actualNode = top.node as ParsedBinaryArrayExpressionNode
        Assertions.assertEquals(node, actualNode.leftExpression)
        Assertions.assertEquals(rightNode, actualNode.rightExpression)
    }
}
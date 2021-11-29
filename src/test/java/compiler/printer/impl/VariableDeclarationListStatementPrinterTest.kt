package compiler.printer.impl

import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class VariableDeclarationListStatementPrinterTest {
    private val variableDeclarationListPrinter = Mockito.mock(VariableDeclarationListPrinter::class.java)
    private val variableDeclarationListStatementPrinter = VariableDeclarationListStatementPrinter(
        variableDeclarationListPrinter
    )

    @Test
    fun printNodeTest() {
        val node = Mockito.mock(VariableDeclarationListNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END_FOR
        val stack = Stack<StatementPrinterStackItem>()
        val resultStack = Stack<String>()

        val result = "result"
        Mockito.`when`(variableDeclarationListPrinter.printNode(node)).thenReturn(result)
        variableDeclarationListStatementPrinter.printNode(node, numberOfTabs, location, stack, resultStack)
        val actual = resultStack.pop()
        Assertions.assertEquals(result, actual)
    }
}
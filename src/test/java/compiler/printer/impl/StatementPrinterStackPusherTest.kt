package compiler.printer.impl

import compiler.core.nodes.IStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementPrinterLocation
import compiler.core.stack.StatementPrinterStackItem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StatementPrinterStackPusherTest {
    private val statementPrinterStackPusher = StatementPrinterStackPusher()

    @Test
    fun pushTest() {
        val node = Mockito.mock(IStatementNode::class.java)
        val numberOfTabs = 1
        val location = StatementPrinterLocation.END_DO_WHILE
        val stack = Stack<StatementPrinterStackItem>()

        statementPrinterStackPusher.push(node, numberOfTabs, location, stack)
        val top = stack.pop()
        Assertions.assertEquals(node, top.node)
        Assertions.assertEquals(numberOfTabs, top.numberOfTabs)
        Assertions.assertEquals(location, top.location)
    }
}
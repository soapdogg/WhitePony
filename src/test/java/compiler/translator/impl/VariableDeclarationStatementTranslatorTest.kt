package compiler.translator.impl

import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.IVariableTypeRecorder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class VariableDeclarationStatementTranslatorTest {
    private val variableTypeRecorder = Mockito.mock(IVariableTypeRecorder::class.java)
    private val variableDeclarationStatementTranslator = VariableDeclarationStatementTranslator(variableTypeRecorder)

    @Test
    fun translateTest() {
        val node = Mockito.mock(VariableDeclarationListNode::class.java)
        val location = StatementTranslatorLocation.END
        val tempCounter = 1
        val labelCounter = 2
        val variableToTypeMap = mutableMapOf<String,String>()
        val stack = Stack<StatementTranslatorStackItem>()
        val resultStack = Stack<ITranslatedStatementNode>()
        val expressionStack = Stack<ITranslatedExpressionNode>()
        val labelStack = Stack<String>()

        val (actualTemp, actualLabel) = variableDeclarationStatementTranslator.translate(node, location, tempCounter, labelCounter, variableToTypeMap, stack, resultStack, expressionStack, labelStack)
        Assertions.assertEquals(tempCounter, actualTemp)
        Assertions.assertEquals(labelCounter, actualLabel)
        val top = resultStack.pop()
        Assertions.assertEquals(node, top)
        Mockito.verify(variableTypeRecorder).recordVariableTypes(
            node,
            variableToTypeMap
        )
    }
}
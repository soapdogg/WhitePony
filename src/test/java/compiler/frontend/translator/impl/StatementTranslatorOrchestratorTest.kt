package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.frontend.translator.impl.internal.IStackGenerator
import compiler.frontend.translator.impl.internal.IStatementTranslator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class StatementTranslatorOrchestratorTest {
    private val stackGenerator = Mockito.mock(IStackGenerator::class.java)
    private val statementNode = Mockito.mock(IParsedStatementNode::class.java)
    private val translator = Mockito.mock(IStatementTranslator::class.java)
    private val translatorMap = mapOf<Class<out IParsedStatementNode>, IStatementTranslator>(
        statementNode.javaClass to translator
    )

    private val statementTranslatorOrchestrator = StatementTranslatorOrchestrator(
        stackGenerator,
        translatorMap
    )

    @Test
    fun translateTest() {
        val variableToTypeMap = mutableMapOf<String, String>()

        val stack = Stack<StatementTranslatorStackItem>()
        Mockito.`when`(stackGenerator.generateNewStack(StatementTranslatorStackItem::class.java)).thenReturn(stack)

        val resultStack = Stack<ITranslatedStatementNode>()
        Mockito.`when`(stackGenerator.generateNewStack(ITranslatedStatementNode::class.java)).thenReturn(resultStack)

        val expressionStack = Stack<ITranslatedExpressionNode>()
        Mockito.`when`(stackGenerator.generateNewStack(ITranslatedExpressionNode::class.java)).thenReturn(expressionStack)

        val labelStack = Stack<String>()
        Mockito.`when`(stackGenerator.generateNewStack(String::class.java)).thenReturn(labelStack)

        val tempCounter = 0
        val labelCounter = 0

        val tempAfter = 4
        val labelAfter = 10
        val result = Mockito.mock(TranslatedBasicBlockNode::class.java)
        Mockito.`when`(
            translator.translate(
                statementNode,
                StatementTranslatorLocation.START,
                tempCounter,
                labelCounter,
                variableToTypeMap,
                stack,
                resultStack,
                expressionStack,
                labelStack
            )
        ).then {
            resultStack.push(result)
            return@then Pair(tempAfter, labelAfter)
        }

        val actual = statementTranslatorOrchestrator.translate(
            statementNode,
            variableToTypeMap
        )
        Assertions.assertEquals(result, actual)
    }
}
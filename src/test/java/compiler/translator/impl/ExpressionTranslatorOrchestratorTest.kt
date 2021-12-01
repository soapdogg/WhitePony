package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IStackGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ExpressionTranslatorOrchestratorTest {
    private val stackGenerator = Mockito.mock(IStackGenerator::class.java)
    private val expressionNode = Mockito.mock(IParsedExpressionNode::class.java)
    private val translator = Mockito.mock(IExpressionTranslator::class.java)
    private val translatorMap = mapOf<Class<out IParsedExpressionNode>, IExpressionTranslator>(
        expressionNode.javaClass to translator
    )

    private val expressionTranslatorOrchestrator = ExpressionTranslatorOrchestrator(
        stackGenerator,
        translatorMap
    )

    @Test
    fun translateTest() {
        val stack = Stack<ExpressionTranslatorStackItem>()
        Mockito.`when`(stackGenerator.generateNewStack(ExpressionTranslatorStackItem::class.java)).thenReturn(stack)

        val resultStack = Stack<TranslatedExpressionNode>()
        Mockito.`when`(stackGenerator.generateNewStack(TranslatedExpressionNode::class.java)).thenReturn(resultStack)

        val variableToTypeMap = mapOf<String, String>()
        val tempCounter = 3
        val tempAfter = 34
        val result = Mockito.mock(TranslatedExpressionNode::class.java)
        Mockito.`when`(
            translator.translate(
                expressionNode,
                ExpressionTranslatorLocation.START,
                variableToTypeMap,
                tempCounter,
                stack,
                resultStack
            )
        ).then {
            resultStack.push(result)
            return@then tempAfter
        }

        val (actual, actualTemp) = expressionTranslatorOrchestrator.translate(
            expressionNode,
            variableToTypeMap,
            tempCounter
        )
        Assertions.assertEquals(result, actual)
        Assertions.assertEquals(tempAfter, actualTemp)
    }
}
package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IBooleanExpressionTranslator
import compiler.translator.impl.internal.IStackGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BooleanExpressionTranslatorTest {
    private val stackGenerator = Mockito.mock(IStackGenerator::class.java)
    private val expressionNode = Mockito.mock(IParsedExpressionNode::class.java)
    private val translator = Mockito.mock(IBooleanExpressionTranslator::class.java)
    private val translatorMap = mapOf<Class<out IParsedExpressionNode>, IBooleanExpressionTranslator>(
        expressionNode.javaClass to translator
    )

    private val booleanExpressionTranslator = BooleanExpressionTranslatorOrchestrator(
        stackGenerator,
        translatorMap
    )

    @Test
    fun translateTest() {
        val stack = Stack<BooleanExpressionTranslatorStackItem>()
        Mockito.`when`(stackGenerator.generateNewStack(BooleanExpressionTranslatorStackItem::class.java)).thenReturn(stack)

        val resultStack = Stack<TranslatedBooleanExpressionNode>()
        Mockito.`when`(stackGenerator.generateNewStack(TranslatedBooleanExpressionNode::class.java)).thenReturn(resultStack)

        val labelStack = Stack<String>()
        Mockito.`when`(stackGenerator.generateNewStack(String::class.java)).thenReturn(labelStack)


        val trueLabel = "trueLabel"
        val falseLabel = "falseLabel"
        val labelCounter = 1
        val tempCounter = 2
        val variableToTypeMap = mapOf<String, String>()

        val labelCountAfterTranslate = 23
        val tempCountAfterTranslate = 34
        val result = Mockito.mock(TranslatedBooleanExpressionNode::class.java)
        Mockito.`when`(
            translator.translate(
                expressionNode,
                LocationConstants.LOCATION_1,
                trueLabel,
                falseLabel,
                tempCounter,
                labelCounter,
                variableToTypeMap,
                stack,
                resultStack,
                labelStack
            )
        ).then {
            resultStack.push(result)
            return@then Pair(labelCountAfterTranslate, tempCountAfterTranslate)
        }

        val (actual, l, t) = booleanExpressionTranslator.translate(
            expressionNode,
            trueLabel,
            falseLabel,
            labelCounter,
            tempCounter,
            variableToTypeMap
        )

        Assertions.assertEquals(result, actual)
        Assertions.assertEquals(labelCountAfterTranslate, l)
        Assertions.assertEquals(tempCountAfterTranslate, t)
    }

}
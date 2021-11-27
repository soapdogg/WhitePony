package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedBinaryRelationalOperatorExpressionNode
import compiler.core.nodes.translated.TranslatedBooleanExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IConditionalGotoCodeGenerator
import compiler.translator.impl.internal.IExpressionTranslator
import compiler.translator.impl.internal.IGotoCodeGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class BinaryRelationalOperatorExpressionTranslatorTest {
    private val expressionTranslator = Mockito.mock(IExpressionTranslator::class.java)
    private val conditionalGotoCodeGenerator = Mockito.mock(IConditionalGotoCodeGenerator::class.java)
    private val gotoCodeGenerator = Mockito.mock(IGotoCodeGenerator::class.java)

    private val binaryRelationalOperatorExpressionTranslator = BinaryRelationalOperatorExpressionTranslator(
        expressionTranslator,
        conditionalGotoCodeGenerator,
        gotoCodeGenerator
    )

    @Test
    fun translateTest() {
        val node = Mockito.mock(ParsedBinaryRelationalOperatorExpressionNode::class.java)
        val trueLabel = "trueLabel"
        val falseLabel = "falseLabel"
        val tempCounter = 1
        val variableToTypeMap = mapOf<String, String>()
        val stack = Stack<BooleanExpressionTranslatorStackItem>()
        val resultStack = Stack<TranslatedBooleanExpressionNode>()

        val leftExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.leftExpression).thenReturn(leftExpression)

        val translatedLeftExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        val tempAfterLeft = 2
        Mockito.`when`(expressionTranslator.translate(leftExpression, variableToTypeMap, tempCounter)).thenReturn(Pair(translatedLeftExpression, tempAfterLeft))

        val rightExpression = Mockito.mock(IParsedExpressionNode::class.java)
        Mockito.`when`(node.rightExpression).thenReturn(rightExpression)

        val translatedRightExpression = Mockito.mock(TranslatedExpressionNode::class.java)
        val tempAfterRight = 3
        Mockito.`when`(expressionTranslator.translate(rightExpression, variableToTypeMap, tempAfterLeft)).thenReturn(Pair(translatedRightExpression, tempAfterRight))

        val leftAddress = "leftAddress"
        Mockito.`when`(translatedLeftExpression.address).thenReturn(leftAddress)

        val operator = "operator"
        Mockito.`when`(node.operator).thenReturn(operator)

        val rightAddress = "rightAddress"
        Mockito.`when`(translatedRightExpression.address).thenReturn(rightAddress)

        val conditionalGotoCode = "conditionalGotoCode"
        Mockito.`when`(conditionalGotoCodeGenerator.generateConditionalGotoCode(leftAddress, operator, rightAddress, trueLabel)).thenReturn(conditionalGotoCode)

        val gotoFalseLabelCode = "gotoFalseLabelCode"
        Mockito.`when`(gotoCodeGenerator.generateGotoCode(falseLabel)).thenReturn(gotoFalseLabelCode)

        val leftCode = "leftCode"
        Mockito.`when`(translatedLeftExpression.code).thenReturn(listOf(leftCode))

        val rightCode = "rightCode"
        Mockito.`when`(translatedRightExpression.code).thenReturn(listOf(rightCode))

        val actual = binaryRelationalOperatorExpressionTranslator.translate(
            node,
            trueLabel,
            falseLabel,
            tempCounter,
            variableToTypeMap,
            stack,
            resultStack
        )
        Assertions.assertEquals(tempAfterRight, actual)
        val top = resultStack.pop()
        Assertions.assertEquals(listOf(leftCode, rightCode, conditionalGotoCode, gotoFalseLabelCode), top.code)
    }
}
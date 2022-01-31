package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IExpressionTranslator
import compiler.frontend.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.frontend.translator.impl.internal.ITempGenerator

internal class VariableExpressionTranslator(
    private val tempGenerator: ITempGenerator,
    private val tempDeclarationCodeGenerator: ITempDeclarationCodeGenerator
): IExpressionTranslator {

    override fun translate(
        node: IParsedExpressionNode,
        location: ExpressionTranslatorLocation,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int,
        stack: Stack<ExpressionTranslatorStackItem>,
        resultStack: Stack<TranslatedExpressionNode>
    ): Int {
        node as ParsedVariableExpressionNode
        val (address, tempAfterAddress) = tempGenerator.generateTempVariable(tempCounter)
        val type = variableToTypeMap.getValue(node.value)
        val tempDeclarationCode = tempDeclarationCodeGenerator.generateTempDeclarationCode(
            type,
            address,
            node.value
        )
        val code = listOf(tempDeclarationCode)
        val translatedVariableExpressionNode = TranslatedExpressionNode(
            address,
            code,
            type
        )
        resultStack.push(translatedVariableExpressionNode)
        return tempAfterAddress
    }
}
package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedVariableExpressionNode
import compiler.core.nodes.translated.TranslatedExpressionNode
import compiler.core.stack.Stack
import compiler.translator.impl.internal.ITempDeclarationCodeGenerator
import compiler.translator.impl.internal.ITempGenerator
import compiler.translator.impl.internal.IVariableExpressionTranslator

internal class VariableExpressionTranslator(
    private val tempGenerator: ITempGenerator,
    private val tempDeclarationCodeGenerator: ITempDeclarationCodeGenerator
): IVariableExpressionTranslator {
    override fun translate(
        node: ParsedVariableExpressionNode,
        variableToTypeMap: Map<String, String>,
        tempCounter: Int,
        resultStack: Stack<TranslatedExpressionNode>
    ): Int {
        val (address, t) = tempGenerator.generateTempVariable(tempCounter)
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
        return t
    }
}
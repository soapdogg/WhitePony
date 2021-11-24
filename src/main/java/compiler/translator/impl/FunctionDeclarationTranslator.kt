package compiler.translator.impl

import compiler.core.ParsedFunctionDeclarationNode
import compiler.core.TranslatedBasicBlockNode
import compiler.core.TranslatedFunctionDeclarationNode
import compiler.translator.impl.internal.IFunctionDeclarationTranslator
import compiler.translator.impl.internal.IStatementTranslator

internal class FunctionDeclarationTranslator(
    private val statementTranslator: IStatementTranslator
): IFunctionDeclarationTranslator {
    override fun translate(
        functionDeclarationNode: ParsedFunctionDeclarationNode,
        variableToTypeMap: MutableMap<String, String>,
    ): TranslatedFunctionDeclarationNode {

        val translatedStatement = statementTranslator.translate(functionDeclarationNode.basicBlockNode, variableToTypeMap)

        return TranslatedFunctionDeclarationNode(
            functionDeclarationNode.type,
            functionDeclarationNode.functionName,
            translatedStatement
        )
    }
}
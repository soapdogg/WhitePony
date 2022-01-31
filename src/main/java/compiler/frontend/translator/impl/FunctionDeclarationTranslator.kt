package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.ParsedFunctionDeclarationNode
import compiler.core.nodes.translated.TranslatedFunctionDeclarationNode
import compiler.frontend.translator.impl.internal.IFunctionDeclarationTranslator
import compiler.frontend.translator.impl.internal.IStatementTranslatorOrchestrator

internal class FunctionDeclarationTranslator(
    private val statementTranslator: IStatementTranslatorOrchestrator
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
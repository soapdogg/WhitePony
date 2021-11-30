package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedStatementNode
import compiler.core.nodes.parsed.ParsedBasicBlockNode
import compiler.core.nodes.translated.ITranslatedExpressionNode
import compiler.core.nodes.translated.ITranslatedStatementNode
import compiler.core.nodes.translated.TranslatedBasicBlockNode
import compiler.core.stack.Stack
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.IStatementTranslator

internal class BasicBlockStatementTranslator: IStatementTranslator {
    override fun translate(
        node: IParsedStatementNode,
        location: StatementTranslatorLocation,
        tempCounter: Int,
        labelCounter: Int,
        variableToTypeMap: Map<String, String>,
        stack: Stack<StatementTranslatorStackItem>,
        resultStack: Stack<ITranslatedStatementNode>,
        expressionStack: Stack<ITranslatedExpressionNode>,
        labelStack: Stack<String>
    ): Pair<Int, Int> {
        node as ParsedBasicBlockNode
        when(location) {
            StatementTranslatorLocation.START -> {
                stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.END, node))
                node.statements.reversed().forEach {
                    stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.START, it))
                }
                return Pair(tempCounter, labelCounter)
            }
            else -> {
                val numberOfStatements = node.statements.size
                val body = mutableListOf<ITranslatedStatementNode>()
                for (i in 0 until numberOfStatements) {
                    body.add(resultStack.pop())
                }
                val translatedBasicBlock = TranslatedBasicBlockNode(
                    body.reversed()
                )
                resultStack.push(translatedBasicBlock)
                return Pair(tempCounter, labelCounter)
            }
        }
    }
}
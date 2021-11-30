package compiler.translator.impl

import compiler.core.nodes.*
import compiler.core.nodes.parsed.*
import compiler.core.nodes.translated.*
import compiler.core.stack.StatementTranslatorLocation
import compiler.core.stack.StatementTranslatorStackItem
import compiler.translator.impl.internal.*
import compiler.translator.impl.internal.IStatementTranslatorOrchestrator

internal class StatementTranslatorOrchestrator(
    private val stackGenerator: IStackGenerator,
    private val translatorMap: Map<Class<out IParsedStatementNode>, IStatementTranslator>,
    private val variableTypeRecorder: IVariableTypeRecorder,
): IStatementTranslatorOrchestrator {
    override fun translate(
        statementNode: IParsedStatementNode,
        variableToTypeMap: MutableMap<String, String>
    ): TranslatedBasicBlockNode {

        val stack = stackGenerator.generateNewStack(StatementTranslatorStackItem::class.java)
        stack.push(StatementTranslatorStackItem(StatementTranslatorLocation.START, statementNode))
        val resultStack = stackGenerator.generateNewStack(ITranslatedStatementNode::class.java)
        val expressionStack = stackGenerator.generateNewStack(ITranslatedExpressionNode::class.java)
        val labelStack = stackGenerator.generateNewStack(String::class.java)

        var tempCounter = 0
        var labelCounter = 0

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            if (translatorMap.containsKey(top.node.javaClass)) {
                val translator = translatorMap.getValue(top.node.javaClass)
                val (tempAfter, labelAfter) = translator.translate(
                    top.node,
                    top.location,
                    tempCounter,
                    labelCounter,
                    variableToTypeMap,
                    stack,
                    resultStack,
                    expressionStack,
                    labelStack
                )
                tempCounter = tempAfter
                labelCounter = labelAfter
            } else {

                when (top.node) {
                    is VariableDeclarationListNode -> {
                        variableTypeRecorder.recordVariableTypes(
                            top.node,
                            variableToTypeMap
                        )
                        resultStack.push(top.node)
                    }
                }
            }
        }

        return resultStack.pop() as TranslatedBasicBlockNode
    }
}
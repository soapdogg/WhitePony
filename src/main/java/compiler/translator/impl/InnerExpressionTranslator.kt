package compiler.translator.impl

import compiler.core.nodes.parsed.ParsedInnerExpressionNode
import compiler.core.stack.ExpressionTranslatorLocation
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IInnerExpressionTranslator

internal class InnerExpressionTranslator: IInnerExpressionTranslator {
    override fun translate(
        node: ParsedInnerExpressionNode,
        stack: Stack<ExpressionTranslatorStackItem>
    ) {
        stack.push(ExpressionTranslatorStackItem(ExpressionTranslatorLocation.START, node.expression))
    }
}
package compiler.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.ExpressionTranslatorStackItem
import compiler.core.stack.LocationConstants
import compiler.core.stack.Stack
import compiler.translator.impl.internal.IExpressionTranslatorStackPusher

internal class ExpressionTranslatorStackPusher: IExpressionTranslatorStackPusher {
    override fun push(
        location: Int,
        expression1: IParsedExpressionNode,
        expression2: IParsedExpressionNode,
        stack: Stack<ExpressionTranslatorStackItem>,
    ) {
        stack.push(ExpressionTranslatorStackItem(location, expression1))
        stack.push(ExpressionTranslatorStackItem(LocationConstants.LOCATION_1, expression2))
    }
}
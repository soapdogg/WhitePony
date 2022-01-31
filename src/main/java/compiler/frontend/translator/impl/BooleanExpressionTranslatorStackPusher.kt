package compiler.frontend.translator.impl

import compiler.core.nodes.parsed.IParsedExpressionNode
import compiler.core.stack.BooleanExpressionTranslatorStackItem
import compiler.core.stack.Stack
import compiler.frontend.translator.impl.internal.IBooleanExpressionTranslatorStackPusher

class BooleanExpressionTranslatorStackPusher: IBooleanExpressionTranslatorStackPusher {
    override fun push(
        location: Int,
        node: IParsedExpressionNode,
        trueLabel: String,
        falseLabel: String,
        stack: Stack<BooleanExpressionTranslatorStackItem>
    ) {
        stack.push(BooleanExpressionTranslatorStackItem(location, node, trueLabel, falseLabel))
    }
}
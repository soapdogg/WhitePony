package compiler.core.nodes.translated

import compiler.core.nodes.IExpressionNode

interface ITranslatedExpressionNode : IExpressionNode {
    val code: List<String>
}
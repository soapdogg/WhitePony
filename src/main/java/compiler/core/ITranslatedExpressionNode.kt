package compiler.core

interface ITranslatedExpressionNode : IExpressionNode {
    val code: List<String>
}
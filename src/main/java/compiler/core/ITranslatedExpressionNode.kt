package compiler.core

interface ITranslatedExpressionNode : IExpressionNode{
    val address: String
    val code: List<String>
}
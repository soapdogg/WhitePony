package compiler.core

data class TranslatedBasicBlockNode(
    override val statements: List<ITranslatedStatementNode>
): ITranslatedStatementNode, IBasicBlockNode
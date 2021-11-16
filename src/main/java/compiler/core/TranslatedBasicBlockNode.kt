package compiler.core

data class TranslatedBasicBlockNode(
    val statements: List<ITranslatedStatementNode>
): ITranslatedStatementNode
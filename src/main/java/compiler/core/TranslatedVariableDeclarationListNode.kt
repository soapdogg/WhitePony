package compiler.core

import compiler.core.constants.IVariableDeclarationListNode

data class TranslatedVariableDeclarationListNode(
    override val type: String,
    override val variableDeclarations: List<TranslatedVariableDeclarationNode>
): ITranslatedDeclarationStatementNode, ITranslatedStatementNode, IVariableDeclarationListNode
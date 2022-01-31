package compiler.frontend.translator.impl.internal

import compiler.core.nodes.VariableDeclarationListNode

internal interface IVariableTypeRecorder {
    fun recordVariableTypes(
        variableDeclarationListNode: VariableDeclarationListNode,
        variableToTypeMap: MutableMap<String, String>
    )
}
package compiler.translator.impl.internal

import compiler.core.nodes.VariableDeclarationListNode

interface IVariableTypeRecorder {
    fun recordVariableTypes(
        variableDeclarationListNode: VariableDeclarationListNode,
        variableToTypeMap: MutableMap<String, String>
    )
}
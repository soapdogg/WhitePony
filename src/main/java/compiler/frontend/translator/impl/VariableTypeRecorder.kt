package compiler.frontend.translator.impl

import compiler.core.nodes.VariableDeclarationListNode
import compiler.frontend.translator.impl.internal.IVariableTypeRecorder

internal class VariableTypeRecorder: IVariableTypeRecorder {
    override fun recordVariableTypes(
        variableDeclarationListNode: VariableDeclarationListNode,
        variableToTypeMap: MutableMap<String, String>
    ) {
        val type = variableDeclarationListNode.type
        variableDeclarationListNode.variableDeclarations.forEach {
            variableToTypeMap[it.id] = type
        }
    }
}
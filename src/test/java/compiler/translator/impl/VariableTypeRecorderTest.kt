package compiler.translator.impl

import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.nodes.VariableDeclarationNode
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class VariableTypeRecorderTest {
    private val variableTypeRecorder = VariableTypeRecorder()

    @Test
    fun recordVariableTypesTest() {
        val variableDeclarationListNode = Mockito.mock(VariableDeclarationListNode::class.java)
        val variableToTypeMap = mutableMapOf<String, String>()

        val type = "type"
        Mockito.`when`(variableDeclarationListNode.type).thenReturn(type)

        val variableDeclarationNode = Mockito.mock(VariableDeclarationNode::class.java)
        Mockito.`when`(variableDeclarationListNode.variableDeclarations).thenReturn(listOf(variableDeclarationNode))

        val id = "id"
        Mockito.`when`(variableDeclarationNode.id).thenReturn(id)

        variableTypeRecorder.recordVariableTypes(variableDeclarationListNode, variableToTypeMap)
        Assertions.assertEquals(variableToTypeMap.getValue(id), type)
    }
}
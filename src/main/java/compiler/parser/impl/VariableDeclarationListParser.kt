package compiler.parser.impl

import compiler.core.Token
import compiler.core.TokenType
import compiler.core.VariableDeclarationListNode
import compiler.core.VariableDeclarationNode
import compiler.parser.impl.internal.ITokenTypeAsserter
import compiler.parser.impl.internal.IVariableDeclarationListParser
import compiler.parser.impl.internal.IVariableDeclarationParser

internal class VariableDeclarationListParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val variableDeclarationParser: IVariableDeclarationParser
): IVariableDeclarationListParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<VariableDeclarationListNode, Int> {
        val (typeToken, positionAfterType) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)
        val variableDeclarations = mutableListOf<VariableDeclarationNode>()
        var currentPosition1 = positionAfterType
        do {
            //parse variables
            val (variableDeclaration, currentPosition2) = variableDeclarationParser.parse(tokens, currentPosition1)
            variableDeclarations.add(variableDeclaration)
            currentPosition1 = currentPosition2
            //comma
            val isComma = tokens[currentPosition2].type == TokenType.COMMA
            if (isComma) {
                currentPosition1++
            }
        } while(isComma)
        //semi
        val variableDeclarationNode = VariableDeclarationListNode(typeToken.value, variableDeclarations)
        return Pair(variableDeclarationNode, currentPosition1 + 1)
    }
}
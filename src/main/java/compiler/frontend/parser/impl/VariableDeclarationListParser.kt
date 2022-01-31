package compiler.frontend.parser.impl

import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.nodes.VariableDeclarationNode
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.frontend.parser.impl.internal.ITokenTypeAsserter
import compiler.frontend.parser.impl.internal.IVariableDeclarationListParser
import compiler.frontend.parser.impl.internal.IVariableDeclarationParser

internal class VariableDeclarationListParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val variableDeclarationParser: IVariableDeclarationParser
): IVariableDeclarationListParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<VariableDeclarationListNode, Int> {
        val (typeToken, _) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)

        val variableDeclarations = mutableListOf<VariableDeclarationNode>()
        var variableDeclarationPosition = startingPosition
        do {
            variableDeclarationPosition++

            val (variableDeclaration, positionAfterVariableDeclaration) = variableDeclarationParser.parse(tokens, variableDeclarationPosition)
            variableDeclarations.add(variableDeclaration)
            variableDeclarationPosition = positionAfterVariableDeclaration

            val hasAnotherVariableDeclaration = tokens[positionAfterVariableDeclaration].type == TokenType.COMMA
        } while(hasAnotherVariableDeclaration)

        val (_, positionAfterSemicolon) = tokenTypeAsserter.assertTokenType(tokens, variableDeclarationPosition, TokenType.SEMICOLON)
        val variableDeclarationListNode = VariableDeclarationListNode(typeToken.value, variableDeclarations)
        return Pair(variableDeclarationListNode, positionAfterSemicolon)
    }
}
package compiler.parser.impl

import compiler.core.DeclarationStatementNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IDeclarationChildParser
import compiler.parser.impl.internal.IDeclarationStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class DeclarationStatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val declarationChildParser: IDeclarationChildParser
) : IDeclarationStatementParser{

    override fun parse(tokens: List<Token>, startingPosition: Int): Pair<DeclarationStatementNode, Int> {
        val typeToken = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)
        val identifierToken = tokenTypeAsserter.assertTokenType(tokens, startingPosition + 1, TokenType.IDENTIFIER)
        val (declarationStatementChild, currentPosition) = declarationChildParser.parse(
            tokens,
            startingPosition + 2,
            typeToken.value,
            identifierToken.value
        )

        return Pair(DeclarationStatementNode(declarationStatementChild), currentPosition)
    }
}
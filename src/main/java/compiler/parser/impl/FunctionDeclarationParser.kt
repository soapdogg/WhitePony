package compiler.parser.impl

import compiler.core.BasicBlockNode
import compiler.core.FunctionDeclarationNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IFunctionDeclarationParser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class FunctionDeclarationParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val statementParser: IStatementParser
): IFunctionDeclarationParser {

    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<FunctionDeclarationNode, Int> {
        val typeToken = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)
        val identifierToken = tokenTypeAsserter.assertTokenType(tokens, startingPosition + 1, TokenType.IDENTIFIER)
        tokenTypeAsserter.assertTokenType(tokens, startingPosition + 2, TokenType.LEFT_PARENTHESES)
        tokenTypeAsserter.assertTokenType(tokens, startingPosition + 3, TokenType.RIGHT_PARENTHESES)
        tokenTypeAsserter.assertTokenType(tokens, startingPosition + 4, TokenType.LEFT_BRACE)
        val (basicBlockNode, currentPosition) = statementParser.parse(
            tokens,
            startingPosition + 4,
        )
        val functionDeclarationNode = FunctionDeclarationNode(
            identifierToken.value,
            typeToken.value,
            basicBlockNode as BasicBlockNode
        )
        return Pair(functionDeclarationNode, currentPosition)
    }
}
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
        type: String,
        identifierValue: String
    ): Pair<FunctionDeclarationNode, Int> {
        tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.LEFT_PARENTHESES)
        tokenTypeAsserter.assertTokenType(tokens, startingPosition + 1, TokenType.RIGHT_PARENTHESES)
        tokenTypeAsserter.assertTokenType(tokens, startingPosition + 2, TokenType.LEFT_BRACE)
        val (basicBlockNode, currentPosition) = statementParser.parse(
            tokens,
            startingPosition + 2,
        )
        val functionDeclarationNode = FunctionDeclarationNode(
            identifierValue,
            type,
            basicBlockNode as BasicBlockNode
        )
        return Pair(functionDeclarationNode, currentPosition)
    }
}
package compiler.parser.impl

import compiler.core.ParsedBasicBlockNode
import compiler.core.ParsedFunctionDeclarationNode
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
    ): Pair<ParsedFunctionDeclarationNode, Int> {
        val (typeToken, positionAfterType) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.TYPE)
        val (identifierToken, positionAfterIdentifier) = tokenTypeAsserter.assertTokenType(tokens, positionAfterType, TokenType.IDENTIFIER)
        val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterIdentifier, TokenType.LEFT_PARENTHESES)
        val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterLeftParentheses, TokenType.RIGHT_PARENTHESES)
        tokenTypeAsserter.assertTokenType(tokens, positionAfterRightParentheses, TokenType.LEFT_BRACE)
        val (basicBlockNode, finalPosition) = statementParser.parse(
            tokens,
            positionAfterRightParentheses,
        )
        val functionDeclarationNode = ParsedFunctionDeclarationNode(
            identifierToken.value,
            typeToken.value,
            basicBlockNode
        )
        return Pair(functionDeclarationNode, finalPosition)
    }
}
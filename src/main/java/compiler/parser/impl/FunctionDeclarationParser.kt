package compiler.parser.impl

import compiler.core.FunctionDeclarationNode
import compiler.core.Token
import compiler.core.TokenType
import compiler.parser.impl.internal.IBasicBlockParser
import compiler.parser.impl.internal.IFunctionDeclarationParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class FunctionDeclarationParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val basicBlockParser: IBasicBlockParser
): IFunctionDeclarationParser {

    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
        type: String,
        identifierValue: String
    ): Pair<FunctionDeclarationNode, Int> {
        tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.LEFT_PARENTHESES)
        tokenTypeAsserter.assertTokenType(tokens, startingPosition + 1, TokenType.RIGHT_PARENTHESES)
        val (basicBlockNode, currentPosition) = basicBlockParser.parse(
            tokens,
            startingPosition + 2,
        )
        val functionDeclarationNode = FunctionDeclarationNode(
            identifierValue,
            type,
            basicBlockNode
        )
        return Pair(functionDeclarationNode, currentPosition)
    }
}
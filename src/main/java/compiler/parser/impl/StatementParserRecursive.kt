package compiler.parser.impl

import compiler.core.*
import compiler.parser.impl.internal.*
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IExpressionStatementParser
import compiler.parser.impl.internal.IReturnStatementParser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.IVariableDeclarationListParser

internal class StatementParserRecursive(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val expressionParser: IExpressionParser,
    private val variableDeclarationListParser: IVariableDeclarationListParser,
    private val returnStatementParser: IReturnStatementParser,
    private val expressionStatementParser: IExpressionStatementParser
) : IStatementParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedStatementNode, Int> {
        val tokenType = tokens[startingPosition].type

        return when (tokenType) {
            TokenType.DO -> {
                val (_, positionAfterDo) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.DO)
                val (body, positionAfterBody) = parse(tokens, positionAfterDo)
                val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(tokens, positionAfterBody, TokenType.WHILE)
                val (expression, positionAfterExpression) = expressionParser.parse(tokens, positionAfterWhile)
                val (_, positionAfterSemicolon) = tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.SEMICOLON)
                val doStatement = DoWhileNode(
                    expression,
                    body
                )
                Pair(doStatement, positionAfterSemicolon)
            }
            TokenType.FOR -> {
                val (_, positionAfterFor) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.FOR)
                val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterFor, TokenType.LEFT_PARENTHESES)
                val (initExpression, positionAfterInitExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses)
                val (_, positionAfterFirstSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterInitExpression, TokenType.SEMICOLON)
                val (testExpression, positionAfterTestExpression) = expressionParser.parse(tokens, positionAfterFirstSemi)
                val (_, positionAfterSecondSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterTestExpression, TokenType.SEMICOLON)
                val (incrementExpression, positionAfterIncrementExpression) = expressionParser.parse(tokens, positionAfterSecondSemi)
                val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens,  positionAfterIncrementExpression, TokenType.RIGHT_PARENTHESES)
                val (body, positionAfterBody) = parse(tokens,  positionAfterRightParentheses)
                val forNode = ForNode(
                    initExpression,
                    testExpression,
                    incrementExpression,
                    body
                )
                return Pair(forNode, positionAfterBody)
            }
            TokenType.IF -> {
                val (_, positionAfterIf) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.IF)
                val (booleanExpression, positionAfterBooleanExpression) = expressionParser.parse(tokens, positionAfterIf)
                val (ifBody, positionAfterIfBody) = parse(tokens, positionAfterBooleanExpression)

                val ifNode = IfNode(
                    booleanExpression,
                    ifBody,
                )
                Pair(ifNode, positionAfterIfBody)
            }
            TokenType.ELSE -> {
                val (_, positionAfterElse) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.ELSE)
                val (elseBody, positionAfterElseBody) = parse(tokens, positionAfterElse)
                val elseNode = ElseNode(
                    elseBody
                )
                Pair(elseNode, positionAfterElseBody)
            }
            TokenType.LEFT_BRACE -> {
                val (_, positionAfterLeftBrace) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.LEFT_BRACE)
                val statements = mutableListOf<IParsedStatementNode>()
                var statementPosition = positionAfterLeftBrace
                while (tokens[statementPosition].type != TokenType.RIGHT_BRACE) {
                    val (statement, positionAfterStatement) = parse(tokens, statementPosition)
                    statements.add(statement)
                    statementPosition = positionAfterStatement
                }
                val (_, positionAfterRightBrace) = tokenTypeAsserter.assertTokenType(tokens, statementPosition, TokenType.RIGHT_BRACE)
                val basicBlockNode = ParsedBasicBlockNode(statements)
                Pair(basicBlockNode, positionAfterRightBrace)
            }
            TokenType.RETURN -> {
                returnStatementParser.parse(tokens, startingPosition)
            }
            TokenType.TYPE -> {
                variableDeclarationListParser.parse(tokens, startingPosition)
            }
            TokenType.WHILE -> {
                val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.WHILE)
                val (expression, positionAfterExpression) = expressionParser.parse(tokens, positionAfterWhile)
                val (body, positionAfterBody) = parse(tokens, positionAfterExpression)
                val whileNode = WhileNode(expression, body)
                Pair(whileNode, positionAfterBody)
            }
            else -> {
                expressionStatementParser.parse(tokens, startingPosition)
            }
        }

    }
}
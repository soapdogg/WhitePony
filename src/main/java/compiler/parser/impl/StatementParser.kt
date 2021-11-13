package compiler.parser.impl

import compiler.core.*
import compiler.parser.impl.internal.*
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IExpressionStatementParser
import compiler.parser.impl.internal.IReturnStatementParser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.IVariableDeclarationListParser

internal class StatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val expressionParser: IExpressionParser,
    private val variableDeclarationListParser: IVariableDeclarationListParser,
    private val returnStatementParser: IReturnStatementParser,
    private val expressionStatementParser: IExpressionStatementParser
) : IStatementParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IStatementNode, Int> {
        val tokenType = tokens[startingPosition].type

        return when (tokenType) {
            TokenType.DO -> {
                val (_, positionAfterDo) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.DO)
                val (body, positionAfterBody) = parse(tokens, positionAfterDo)
                val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(tokens, positionAfterBody, TokenType.WHILE)
                val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterWhile, TokenType.LEFT_PARENTHESES)
                val (expression, positionAfterExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses, setOf(TokenType.RIGHT_PARENTHESES))
                val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.RIGHT_PARENTHESES)
                val (_, positionAfterSemicolon) = tokenTypeAsserter.assertTokenType(tokens, positionAfterRightParentheses, TokenType.SEMICOLON)
                val doStatement = DoWhileNode(
                    expression,
                    body
                )
                Pair(doStatement, positionAfterSemicolon)
            }
            TokenType.FOR -> {
                val (_, positionAfterFor) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.FOR)
                val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterFor, TokenType.LEFT_PARENTHESES)
                val (initExpression, positionAfterInitExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses, setOf(TokenType.SEMICOLON))
                val (_, positionAfterFirstSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterInitExpression, TokenType.SEMICOLON)
                val (testExpression, positionAfterTestExpression) = expressionParser.parse(tokens, positionAfterFirstSemi, setOf(TokenType.SEMICOLON))
                val (_, positionAfterSecondSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterTestExpression, TokenType.SEMICOLON)
                val (incrementExpression, positionAfterIncrementExpression) = expressionParser.parse(tokens, positionAfterSecondSemi, setOf(TokenType.RIGHT_PARENTHESES))
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
                //if
                //LParent
                val (booleanExpression, currentPosition1) = expressionParser.parse(tokens, startingPosition + 2, setOf(TokenType.RIGHT_PARENTHESES))
                //RParent
                val (ifBody, currentPosition2) = parse(tokens, currentPosition1 + 1)
                if (tokens[currentPosition2].type == TokenType.ELSE) {
                    //else
                    val (elseBody, currentPosition3) = parse(tokens, currentPosition2 + 1)
                    val ifNode = IfNode(
                        booleanExpression,
                        ifBody,
                        elseBody
                    )
                    Pair(ifNode, currentPosition3)
                } else {
                    val ifNode = IfNode(
                        booleanExpression,
                        ifBody,
                        null
                    )
                    Pair(ifNode, currentPosition2)
                }
            }
            TokenType.LEFT_BRACE -> {
                val (_, positionAfterLeftBrace) = tokenTypeAsserter.assertTokenType(tokens, startingPosition, TokenType.LEFT_BRACE)
                val statements = mutableListOf<IStatementNode>()
                var statementPosition = positionAfterLeftBrace
                while (tokens[statementPosition].type != TokenType.RIGHT_BRACE) {
                    val (statement, positionAfterStatement) = parse(tokens, statementPosition)
                    statements.add(statement)
                    statementPosition = positionAfterStatement
                }
                val (_, positionAfterRightBrace) = tokenTypeAsserter.assertTokenType(tokens, statementPosition, TokenType.RIGHT_BRACE)
                val basicBlockNode = BasicBlockNode(statements)
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
                val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterWhile, TokenType.LEFT_PARENTHESES)
                val (expression, positionAfterExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses, setOf(TokenType.RIGHT_PARENTHESES))
                val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.RIGHT_PARENTHESES)
                val (body, positionAfterBody) = parse(tokens, positionAfterRightParentheses)
                val whileNode = WhileNode(expression, body)
                Pair(whileNode, positionAfterBody)
            }
            else -> {
                expressionStatementParser.parse(tokens, startingPosition)
            }
        }

    }
}
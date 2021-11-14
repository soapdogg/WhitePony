package compiler.parser.impl

import compiler.core.*
import compiler.parser.impl.internal.*
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IReturnStatementParser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import compiler.parser.impl.internal.IVariableDeclarationListParser

internal class StatementParserIterative(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val expressionParser: IExpressionParser,
    private val returnStatementParser: IReturnStatementParser,
    private val variableDeclarationListParser: IVariableDeclarationListParser,
    private val expressionStatementParser: IExpressionStatementParser
): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IStatementNode, Int> {

        val statementStack = Stack<IParseStackItem>()
        val stackItem = BlockParseStackItem(listOf())
        statementStack.push(stackItem)


        var currentPosition = startingPosition
        var latestStatement: IStatementNode? = null
        while(
            statementStack.isNotEmpty()
        ) {
            val tokenType = tokens[currentPosition].type
            when {
                tokenType == TokenType.DO -> {
                    val (_, positionAfterDo) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.DO)
                    currentPosition = positionAfterDo
                    statementStack.push(DoParseStackItem())
                    continue
                }
                tokenType == TokenType.WHILE -> {
                    val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.WHILE)
                    val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterWhile, TokenType.LEFT_PARENTHESES)
                    val (expression, positionAfterExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses, setOf(TokenType.RIGHT_PARENTHESES))
                    val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.RIGHT_PARENTHESES)
                    currentPosition = positionAfterRightParentheses
                    val stackItem = WhileParseStackItem(expression)
                    statementStack.push(stackItem)
                    continue
                }
                tokenType == TokenType.FOR -> {
                    val (_, positionAfterFor) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.FOR)
                    val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterFor, TokenType.LEFT_PARENTHESES)
                    val (initExpression, positionAfterInitExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses, setOf(TokenType.SEMICOLON))
                    val (_, positionAfterFirstSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterInitExpression, TokenType.SEMICOLON)
                    val (testExpression, positionAfterTestExpression) = expressionParser.parse(tokens, positionAfterFirstSemi, setOf(TokenType.SEMICOLON))
                    val (_, positionAfterSecondSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterTestExpression, TokenType.SEMICOLON)
                    val (incrementExpression, positionAfterIncrementExpression) = expressionParser.parse(tokens, positionAfterSecondSemi, setOf(TokenType.RIGHT_PARENTHESES))
                    val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens,  positionAfterIncrementExpression, TokenType.RIGHT_PARENTHESES)
                    currentPosition = positionAfterRightParentheses
                    val stackItem = ForParseStackItem(initExpression, testExpression, incrementExpression)
                    statementStack.push(stackItem)
                    continue
                }
                tokenType == TokenType.IF -> {
                    val (_, positionAfterIf) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.IF)
                    val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterIf, TokenType.LEFT_PARENTHESES)
                    val (booleanExpression, positionAfterBooleanExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses, setOf(TokenType.RIGHT_PARENTHESES))
                    val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterBooleanExpression, TokenType.RIGHT_PARENTHESES)
                    currentPosition = positionAfterRightParentheses
                    val stackItem = IfParseStackItem(booleanExpression)
                    statementStack.push(stackItem)
                    continue
                }
                tokenType == TokenType.ELSE -> {
                    val (_, positionAfterElse) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.ELSE)
                    currentPosition = positionAfterElse
                    val stackItem = ElseParseStackItem()
                    statementStack.push(stackItem)
                    continue
                }
                tokenType == TokenType.LEFT_BRACE -> {
                    val (_, positionAfterLeftBrace) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.LEFT_BRACE)
                    val statements = mutableListOf<IStatementNode>()
                    var statementPosition = positionAfterLeftBrace
                    currentPosition = statementPosition
                    val stackItem = BlockParseStackItem(statements)
                    statementStack.push(stackItem)
                    continue
                }
                tokenType == TokenType.RETURN -> {
                    val(_, positionAfterReturn) = returnStatementParser.parse(tokens, currentPosition)
                    currentPosition = positionAfterReturn
                }
                tokenType == TokenType.TYPE -> {
                    val(_, positionAfterVariableDeclarationList) = variableDeclarationListParser.parse(tokens, currentPosition)
                    currentPosition = positionAfterVariableDeclarationList
                }
                else -> {
                    val(_, positionAfterExpressionStatement) = expressionStatementParser.parse(tokens, currentPosition)
                    currentPosition = positionAfterExpressionStatement
                }
            }

            val top = statementStack.pop()

            when {
                top.getType() == StatementType.BLOCK_STATEMENT -> {
                    latestStatement = BasicBlockNode(listOf())
                }
            }
        }

        return Pair(latestStatement!!, tokens.size)
    }

}
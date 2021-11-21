package compiler.parser.impl

import compiler.core.*
import compiler.core.constants.StatementParserConstants
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IExpressionStatementParser
import compiler.parser.impl.internal.IReturnStatementParser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import compiler.parser.impl.internal.IVariableDeclarationListParser

internal class StatementParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val expressionParser: IExpressionParser,
    private val variableDeclarationListParser: IVariableDeclarationListParser,
    private val returnStatementParser: IReturnStatementParser,
    private val expressionStatementParser: IExpressionStatementParser
): IStatementParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedStatementNode, Int> {

        val stack = Stack<Int>()
        val resultStack = Stack<IParsedStatementNode>()
        val expressionStack = Stack<IParsedExpressionNode>()
        stack.push(StatementParserConstants.LOCATION_START)
        var tokenPosition = startingPosition
        val numberOfStatementsBlockStack = Stack<Int>()

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            when(top) {
                StatementParserConstants.LOCATION_START -> {
                    when (tokens[tokenPosition].type) {
                        TokenType.DO -> {
                            val (_, positionAfterDo) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.DO)
                            tokenPosition = positionAfterDo
                            stack.push(StatementParserConstants.LOCATION_DO)
                            stack.push(StatementParserConstants.LOCATION_START)
                        }
                        TokenType.FOR -> {
                            val (_, positionAfterFor) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.FOR)
                            val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterFor, TokenType.LEFT_PARENTHESES)
                            val (initExpression, positionAfterInitExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses)
                            val (_, positionAfterFirstSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterInitExpression, TokenType.SEMICOLON)
                            val (testExpression, positionAfterTestExpression) = expressionParser.parse(tokens, positionAfterFirstSemi)
                            val (_, positionAfterSecondSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterTestExpression, TokenType.SEMICOLON)
                            val (incrementExpression, positionAfterIncrementExpression) = expressionParser.parse(tokens, positionAfterSecondSemi)
                            val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens,  positionAfterIncrementExpression, TokenType.RIGHT_PARENTHESES)
                            expressionStack.push(incrementExpression)
                            expressionStack.push(testExpression)
                            expressionStack.push(initExpression)
                            tokenPosition = positionAfterRightParentheses
                            stack.push(StatementParserConstants.LOCATION_FOR)
                            stack.push(StatementParserConstants.LOCATION_START)
                        }
                        TokenType.IF -> {
                            val (_, positionAfterIf) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.IF)
                            val (booleanExpression, positionAfterBooleanExpression) = expressionParser.parse(tokens, positionAfterIf)
                            tokenPosition = positionAfterBooleanExpression
                            expressionStack.push(booleanExpression)
                            stack.push(StatementParserConstants.LOCATION_IF)
                            stack.push(StatementParserConstants.LOCATION_START)
                        }
                        TokenType.ELSE -> {
                            val (_, positionAfterElse) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.ELSE)
                            tokenPosition = positionAfterElse
                            stack.push(StatementParserConstants.LOCATION_ELSE)
                            stack.push(StatementParserConstants.LOCATION_START)
                        }
                        TokenType.LEFT_BRACE -> {
                            val (_, positionAfterLeftBrace) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.LEFT_BRACE)
                            if (tokens[positionAfterLeftBrace].type != TokenType.RIGHT_BRACE) {
                                tokenPosition = positionAfterLeftBrace
                                numberOfStatementsBlockStack.push(1)
                                stack.push(StatementParserConstants.LOCATION_BASIC_BLOCK)
                                stack.push(StatementParserConstants.LOCATION_START)
                                continue
                            }
                            val (_, positionAfterRightBrace) = tokenTypeAsserter.assertTokenType(tokens, positionAfterLeftBrace, TokenType.RIGHT_BRACE)
                            val basicBlockNode = ParsedBasicBlockNode(listOf())
                            tokenPosition = positionAfterRightBrace
                            resultStack.push(basicBlockNode)
                        }
                        TokenType.RETURN -> {
                            val (resultStatement, positionAfterReturn) = returnStatementParser.parse(tokens, tokenPosition)
                            tokenPosition = positionAfterReturn
                            resultStack.push(resultStatement)
                        }
                        TokenType.TYPE -> {
                            val (variableStatement, positionAfterVariable) = variableDeclarationListParser.parse(tokens, tokenPosition)
                            tokenPosition = positionAfterVariable
                            resultStack.push(variableStatement)
                        }
                        TokenType.WHILE -> {
                            val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.WHILE)
                            val (expression, positionAfterExpression) = expressionParser.parse(tokens, positionAfterWhile)
                            expressionStack.push(expression)
                            tokenPosition = positionAfterExpression
                            stack.push(StatementParserConstants.LOCATION_WHILE)
                            stack.push(StatementParserConstants.LOCATION_START)
                        }
                        else -> {
                            val (expressionStatement, positionAfterExpression) = expressionStatementParser.parse(tokens, tokenPosition)
                            tokenPosition = positionAfterExpression
                            resultStack.push(expressionStatement)
                        }
                    }
                }
                StatementParserConstants.LOCATION_DO -> {
                    val body = resultStack.pop()
                    val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.WHILE)
                    val (expression, positionAfterExpression) = expressionParser.parse(tokens, positionAfterWhile)
                    val (_, positionAfterSemicolon) = tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.SEMICOLON)
                    val doStatement = ParsedDoWhileNode(
                        expression,
                        body
                    )
                    resultStack.push(doStatement)
                    tokenPosition = positionAfterSemicolon
                }
                StatementParserConstants.LOCATION_FOR -> {
                    val initExpression = expressionStack.pop()
                    val testExpression = expressionStack.pop()
                    val incrementExpression = expressionStack.pop()
                    val body = resultStack.pop()
                    val forNode = ParsedForNode(
                        initExpression,
                        testExpression,
                        incrementExpression,
                        body
                    )
                    resultStack.push(forNode)
                }
                StatementParserConstants.LOCATION_IF -> {
                    val booleanExpression = expressionStack.pop()
                    val ifBody = resultStack.pop()
                    val ifNode = ParsedIfNode(
                        booleanExpression,
                        ifBody,
                    )
                    resultStack.push(ifNode)
                }
                StatementParserConstants.LOCATION_ELSE -> {
                    val elseBody = resultStack.pop()
                    val elseNode = ParsedElseNode(
                        elseBody
                    )
                    resultStack.push(elseNode)
                }
                StatementParserConstants.LOCATION_WHILE -> {
                    val expression = expressionStack.pop()
                    val body = resultStack.pop()
                    val whileNode = ParsedWhileNode(expression, body)
                    resultStack.push(whileNode)
                }
                StatementParserConstants.LOCATION_BASIC_BLOCK -> {
                    if (tokens[tokenPosition].type != TokenType.RIGHT_BRACE) {
                        val numberOfStatementsInBlock = numberOfStatementsBlockStack.pop()
                        numberOfStatementsBlockStack.push(numberOfStatementsInBlock + 1)
                        stack.push(StatementParserConstants.LOCATION_BASIC_BLOCK)
                        stack.push(StatementParserConstants.LOCATION_START)
                        continue
                    }
                    val (_, positionAfterRightBrace) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.RIGHT_BRACE)
                    val statements = mutableListOf<IParsedStatementNode>()
                    val numberOfStatementsInBlock = numberOfStatementsBlockStack.pop()
                    for (i in 0 until numberOfStatementsInBlock) {
                        statements.add(resultStack.pop())
                    }
                    statements.reverse()
                    val basicBlockNode = ParsedBasicBlockNode(statements)
                    tokenPosition = positionAfterRightBrace
                    resultStack.push(basicBlockNode)
                }
            }
        }
        return Pair(resultStack.pop(), tokenPosition)
    }
}
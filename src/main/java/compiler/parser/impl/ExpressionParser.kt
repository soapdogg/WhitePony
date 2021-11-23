package compiler.parser.impl

import compiler.core.*
import compiler.core.constants.ExpressionParserConstants
import compiler.parser.impl.internal.IExpressionGenerator
import compiler.parser.impl.internal.IExpressionStackPusher
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.ITokenTypeAsserter

internal class ExpressionParser(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val expressionStackPusher: IExpressionStackPusher,
    private val unaryTokenTypes: Set<TokenType>,
    private val arrayExpressionTokenTypes: Set<TokenType>,
    private val innerExpressionTokenTypes: Set<TokenType>,
    private val locationToAcceptedTokenValues: Map<Int, Pair<Set<String>, Set<TokenType>>>,
    private val unaryExpressionGenerator: IExpressionGenerator,
    private val binaryExpressionGenerators: Map<Int, IExpressionGenerator>
): IExpressionParser {

    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<IParsedExpressionNode, Int> {

        val stack = Stack<ExpressionParserStackItem>()
        val resultStack = Stack<IParsedExpressionNode>()
        stack.push(ExpressionParserStackItem(ExpressionParserConstants.LOCATION_START, tokens[startingPosition]))
        var tokenPosition = startingPosition

        top@ while(stack.isNotEmpty()) {
            val top = stack.pop()

            when (top.location) {
                ExpressionParserConstants.LOCATION_START -> {
                    when (tokens[tokenPosition].type) {
                        TokenType.PLUS_MINUS, TokenType.PRE_POST, TokenType.BIT_NEGATION, TokenType.UNARY_NOT -> {
                            tokenPosition = expressionStackPusher.push(tokens, tokenPosition, unaryTokenTypes, ExpressionParserConstants.LOCATION_UNARY_EXPRESSION, stack)
                        }
                        TokenType.FLOATING_POINT, TokenType.INTEGER -> {
                            val (constantToken, positionAfterConstant) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, setOf(TokenType.FLOATING_POINT, TokenType.INTEGER))
                            val type = if (constantToken.type == TokenType.INTEGER) "int" else "double"
                            val constantNode = ParsedConstantNode(constantToken.value, type)
                            tokenPosition = positionAfterConstant
                            resultStack.push(constantNode)
                        }
                        TokenType.IDENTIFIER -> {
                            val (identifierToken, positionAfterIdentifier) = tokenTypeAsserter.assertTokenType(tokens, tokenPosition, TokenType.IDENTIFIER)
                            tokenPosition = positionAfterIdentifier
                            val variableExpression = ParsedVariableExpressionNode(identifierToken.value)
                            if (tokens[tokenPosition].type == TokenType.LEFT_BRACKET) {
                                resultStack.push(variableExpression)
                                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, arrayExpressionTokenTypes, ExpressionParserConstants.LOCATION_BINARY_ARRAY, stack)
                                continue
                            }
                            val identifierExpression = if (tokens[tokenPosition].type == TokenType.PRE_POST) {
                                val prePostToken = tokens[tokenPosition]
                                tokenPosition++
                                ParsedUnaryPostOperatorNode(variableExpression, prePostToken.value[0].toString())
                            } else {
                                variableExpression
                            }
                            resultStack.push(identifierExpression)
                        }
                        else -> {
                            tokenPosition = expressionStackPusher.push(tokens, tokenPosition, innerExpressionTokenTypes, ExpressionParserConstants.LOCATION_INNER_EXPRESSION, stack)
                        }
                    }
                }
                ExpressionParserConstants.LOCATION_UNARY_EXPRESSION -> {
                    unaryExpressionGenerator.generateExpression(resultStack, top.token)
                }
                ExpressionParserConstants.LOCATION_BINARY_ARRAY -> {
                    val insideExpression = resultStack.pop()
                    val variableExpression = resultStack.pop() as ParsedVariableExpressionNode
                    tokenPosition++
                    val arrayExpression: IParsedExpressionNode = ParsedBinaryArrayOperatorNode(variableExpression, insideExpression)

                    val identifierExpression = if (tokens[tokenPosition].type == TokenType.PRE_POST) {
                        val prePostToken = tokens[tokenPosition]
                        tokenPosition++
                        ParsedUnaryPostOperatorNode(arrayExpression, prePostToken.value[0].toString())
                    } else {
                        arrayExpression
                    }
                    resultStack.push(identifierExpression)
                }
                ExpressionParserConstants.LOCATION_INNER_EXPRESSION -> {
                    val innerExpression = resultStack.pop()
                    tokenPosition++
                    resultStack.push(ParsedInnerExpression(innerExpression))
                }
            }

            for (i in ExpressionParserConstants.LOCATION_BINARY_OR..ExpressionParserConstants.LOCATION_BINARY_ASSIGN) {
                val acceptedTokens = locationToAcceptedTokenValues.getValue(i)
                if (acceptedTokens.first.contains(tokens[tokenPosition].value)) {
                    tokenPosition = expressionStackPusher.push(tokens, tokenPosition, acceptedTokens.second, i, stack)
                    continue@top
                }
            }

            if (binaryExpressionGenerators.containsKey(top.location)) {
                binaryExpressionGenerators.getValue(top.location).generateExpression(resultStack, top.token)
            }

            if (locationToAcceptedTokenValues.containsKey(top.location) && top.location != ExpressionParserConstants.LOCATION_BINARY_ASSIGN) {
                for (i in top.location..ExpressionParserConstants.LOCATION_BINARY_ASSIGN) {
                    val acceptedTokens = locationToAcceptedTokenValues.getValue(i)
                    if (acceptedTokens.first.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, acceptedTokens.second, i, stack)
                        continue@top
                    }
                }
            }
        }
        return Pair(resultStack.pop(), tokenPosition)
    }
}
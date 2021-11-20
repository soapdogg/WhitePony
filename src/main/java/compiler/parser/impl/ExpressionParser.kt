package compiler.parser.impl

import compiler.core.*
import compiler.core.constants.ParserConstants
import compiler.parser.impl.internal.IExpressionGenerator
import compiler.parser.impl.internal.IExpressionStackPusher
import compiler.parser.impl.internal.IExpressionParser

internal class ExpressionParser(
    private val expressionStackPusher: IExpressionStackPusher,
    private val unaryTokenTypes: Set<TokenType>,
    private val unaryValues: Set<String>,
    private val arrayExpressionTokenTypes: Set<TokenType>,
    private val arrayExpressionValues: Set<String>,
    private val innerExpressionTokenTypes: Set<TokenType>,
    private val innerExpressionValues: Set<String>,
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
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, tokens[startingPosition]))
        var tokenPosition = startingPosition

        top@ while(stack.isNotEmpty()) {
            val top = stack.pop()

            when (top.location) {
                ParserConstants.LOCATION_1 -> {
                    when (tokens[tokenPosition].type) {
                        TokenType.PLUS_MINUS, TokenType.PRE_POST, TokenType.BIT_NEGATION, TokenType.UNARY_NOT -> {
                            tokenPosition = expressionStackPusher.push(tokens, tokenPosition, unaryTokenTypes, unaryValues, ParserConstants.LOCATION_2, stack)
                        }
                        TokenType.FLOATING_POINT, TokenType.INTEGER -> {
                            val constantToken = tokens[tokenPosition]
                            val constantNode = ParsedConstantNode(constantToken.value, constantToken.type == TokenType.INTEGER)
                            tokenPosition++
                            resultStack.push(constantNode)
                        }
                        TokenType.IDENTIFIER -> {
                            val identifierToken = tokens[tokenPosition]
                            tokenPosition++
                            val variableExpression = ParsedVariableExpressionNode(identifierToken.value)
                            var identifierExpression: IParsedExpressionNode = variableExpression
                            if (tokens[tokenPosition].type == TokenType.LEFT_BRACKET) {
                                resultStack.push(variableExpression)
                                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, arrayExpressionTokenTypes, arrayExpressionValues, ParserConstants.LOCATION_3, stack)
                                continue
                            }
                            if (tokens[tokenPosition].type == TokenType.PRE_POST) {
                                val prePostToken = tokens[tokenPosition]
                                tokenPosition++
                                identifierExpression = ParsedUnaryPostOperatorNode(identifierExpression, prePostToken.value[0].toString())
                            }
                            resultStack.push(identifierExpression)
                        }
                        else -> {
                            tokenPosition = expressionStackPusher.push(tokens, tokenPosition, innerExpressionTokenTypes, innerExpressionValues, ParserConstants.LOCATION_4, stack)
                        }
                    }
                }
                ParserConstants.LOCATION_2 -> {
                    unaryExpressionGenerator.generateExpression(resultStack, top.token)
                }
                ParserConstants.LOCATION_3 -> {
                    val insideExpression = resultStack.pop()
                    val variableExpression = resultStack.pop() as ParsedVariableExpressionNode
                    tokenPosition++
                    var identifierExpression: IParsedExpressionNode = ParsedBinaryArrayOperatorNode(variableExpression, insideExpression)

                    if (tokens[tokenPosition].type == TokenType.PRE_POST) {
                        val prePostToken = tokens[tokenPosition]
                        tokenPosition++
                        identifierExpression = ParsedUnaryPostOperatorNode(identifierExpression, prePostToken.value[0].toString())
                    }
                    resultStack.push(identifierExpression)
                }
                ParserConstants.LOCATION_4 -> {
                    val innerExpression = resultStack.pop()
                    tokenPosition++
                    resultStack.push(ParsedInnerExpression(innerExpression))
                }
            }

            for (i in ParserConstants.LOCATION_5..ParserConstants.LOCATION_15) {
                val acceptedTokens = locationToAcceptedTokenValues.getValue(i)
                if (acceptedTokens.first.contains(tokens[tokenPosition].value)) {
                    tokenPosition = expressionStackPusher.push(tokens, tokenPosition, acceptedTokens.second, acceptedTokens.first, i, stack)
                    continue@top
                }
            }


            if (binaryExpressionGenerators.containsKey(top.location)) {
                binaryExpressionGenerators.getValue(top.location).generateExpression(resultStack, top.token)
            }

            if (locationToAcceptedTokenValues.containsKey(top.location) && top.location != ParserConstants.LOCATION_15) {
                for (i in top.location..ParserConstants.LOCATION_15) {
                    val acceptedTokens = locationToAcceptedTokenValues.getValue(i)
                    if (acceptedTokens.first.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, acceptedTokens.second, acceptedTokens.first, i, stack)
                        continue@top
                    }
                }
            }

        }
        return Pair(resultStack.pop(), tokenPosition)
    }
}
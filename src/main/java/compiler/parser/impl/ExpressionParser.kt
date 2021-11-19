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
    private val binaryOrTokenTypes: Set<TokenType>,
    private val binaryOrValues: Set<String>,
    private val binaryAndTokenTypes: Set<TokenType>,
    private val binaryAndValues: Set<String>,
    private val binaryOperatorTokenTypes: Set<TokenType>,
    private val bitwiseOrValues: Set<String>,
    private val bitwiseXorValues: Set<String>,
    private val bitwiseAndValues: Set<String>,
    private val relationalEqualsOperatorValues: Set<String>,
    private val relationalOperatorValues: Set<String>,
    private val relationalOperatorTokenTypes: Set<TokenType>,
    private val shiftValues: Set<String>,
    private val factorValues: Set<String>,
    private val termTokenTypes: Set<TokenType>,
    private val termValues: Set<String>,
    private val binaryAssignTokenTypes: Set<TokenType>,
    private val binaryAssignValues: Set<String>,
    private val unaryExpressionGenerator: IExpressionGenerator,
    private val binaryOrOperatorExpressionGenerator: IExpressionGenerator,
    private val binaryAndOperatorExpressionGenerator: IExpressionGenerator,
    private val binaryOperatorExpressionGenerator: IExpressionGenerator,
    private val binaryRelationalOperatorExpressionGenerator: IExpressionGenerator,
    private val binaryAssignExpressionGenerator: IExpressionGenerator
): IExpressionParser {

    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<IParsedExpressionNode, Int> {

        val stack = Stack<ExpressionParserStackItem>()
        val resultStack = Stack<IParsedExpressionNode>()
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, tokens[startingPosition]))
        var tokenPosition = startingPosition

        while(stack.isNotEmpty()) {
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

            if (binaryOrValues.contains(tokens[tokenPosition].value)) {
                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOrTokenTypes, binaryOrValues, ParserConstants.LOCATION_5, stack)
                continue
            }
            if (binaryAndValues.contains(tokens[tokenPosition].value)) {
                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAndTokenTypes, binaryAndValues, ParserConstants.LOCATION_6, stack)
                continue
            }
            if (bitwiseOrValues.contains(tokens[tokenPosition].value)) {
                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseOrValues, ParserConstants.LOCATION_7, stack)
                continue
            }
            if (bitwiseXorValues.contains(tokens[tokenPosition].value)) {
                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseXorValues, ParserConstants.LOCATION_8, stack)
                continue
            }
            if(bitwiseAndValues.contains(tokens[tokenPosition].value)) {
                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseAndValues, ParserConstants.LOCATION_9, stack)
                continue
            }
            if(relationalEqualsOperatorValues.contains(tokens[tokenPosition].value)) {
                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalEqualsOperatorValues, ParserConstants.LOCATION_10, stack)
                continue
            }
            if(relationalOperatorValues.contains(tokens[tokenPosition].value)) {
                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalOperatorValues, ParserConstants.LOCATION_11, stack)
                continue
            }
            if(shiftValues.contains(tokens[tokenPosition].value)) {
                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, shiftValues, ParserConstants.LOCATION_12, stack)
                continue
            }
            if(factorValues.contains(tokens[tokenPosition].value)) {
                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, factorValues, ParserConstants.LOCATION_13, stack)
                continue
            }
            if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                continue
            }
            if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                continue
            }

            when (top.location) {
                ParserConstants.LOCATION_5 -> {
                    binaryOrOperatorExpressionGenerator.generateExpression(resultStack, top.token)

                    if (binaryOrValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOrTokenTypes, binaryOrValues, ParserConstants.LOCATION_5, stack)
                        continue
                    }
                    if (binaryAndTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAndTokenTypes, binaryAndValues, ParserConstants.LOCATION_6, stack)
                        continue
                    }
                    if (bitwiseOrValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseOrValues, ParserConstants.LOCATION_7, stack)
                        continue
                    }
                    if (bitwiseXorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseXorValues, ParserConstants.LOCATION_8, stack)
                        continue
                    }
                    if(bitwiseAndValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseAndValues, ParserConstants.LOCATION_9, stack)
                        continue
                    }
                    if(relationalEqualsOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalEqualsOperatorValues, ParserConstants.LOCATION_10, stack)
                        continue
                    }
                    if(relationalOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalOperatorValues, ParserConstants.LOCATION_11, stack)
                        continue
                    }
                    if(shiftValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, shiftValues, ParserConstants.LOCATION_12, stack)
                        continue
                    }
                    if(factorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, factorValues, ParserConstants.LOCATION_13, stack)
                        continue
                    }
                    if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                        continue
                    }
                    if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                        continue
                    }
                }
                ParserConstants.LOCATION_6 -> {
                    binaryAndOperatorExpressionGenerator.generateExpression(resultStack, top.token)

                    if (binaryAndTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAndTokenTypes, binaryAndValues, ParserConstants.LOCATION_6, stack)
                        continue
                    }
                    if (bitwiseOrValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseOrValues, ParserConstants.LOCATION_7, stack)
                        continue
                    }
                    if (bitwiseXorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseXorValues, ParserConstants.LOCATION_8, stack)
                        continue
                    }
                    if(bitwiseAndValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseAndValues, ParserConstants.LOCATION_9, stack)
                        continue
                    }
                    if(relationalEqualsOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalEqualsOperatorValues, ParserConstants.LOCATION_10, stack)
                        continue
                    }
                    if(relationalOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalOperatorValues, ParserConstants.LOCATION_11, stack)
                        continue
                    }
                    if(shiftValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, shiftValues, ParserConstants.LOCATION_12, stack)
                        continue
                    }
                    if(factorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, factorValues, ParserConstants.LOCATION_13, stack)
                        continue
                    }
                    if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                        continue
                    }
                    if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                        continue
                    }
                }
                ParserConstants.LOCATION_7 -> {
                    binaryOperatorExpressionGenerator.generateExpression(resultStack, top.token)

                    if (bitwiseOrValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseOrValues, ParserConstants.LOCATION_7, stack)
                        continue
                    }
                    if (bitwiseXorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseXorValues, ParserConstants.LOCATION_8, stack)
                        continue
                    }
                    if(bitwiseAndValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseAndValues, ParserConstants.LOCATION_9, stack)
                        continue
                    }
                    if(relationalEqualsOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalEqualsOperatorValues, ParserConstants.LOCATION_10, stack)
                        continue
                    }
                    if(relationalOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalOperatorValues, ParserConstants.LOCATION_11, stack)
                        continue
                    }
                    if(shiftValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, shiftValues, ParserConstants.LOCATION_12, stack)
                        continue
                    }
                    if(factorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, factorValues, ParserConstants.LOCATION_13, stack)
                        continue
                    }
                    if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                        continue
                    }
                    if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                        continue
                    }
                }
                ParserConstants.LOCATION_8 -> {
                    binaryOperatorExpressionGenerator.generateExpression(resultStack, top.token)

                    if (bitwiseXorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseXorValues, ParserConstants.LOCATION_8, stack)
                        continue
                    }
                    if(bitwiseAndValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseAndValues, ParserConstants.LOCATION_9, stack)
                        continue
                    }
                    if(relationalEqualsOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalEqualsOperatorValues, ParserConstants.LOCATION_10, stack)
                        continue
                    }
                    if(relationalOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalOperatorValues, ParserConstants.LOCATION_11, stack)
                        continue
                    }
                    if(shiftValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, shiftValues, ParserConstants.LOCATION_12, stack)
                        continue
                    }
                    if(factorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, factorValues, ParserConstants.LOCATION_13, stack)
                        continue
                    }
                    if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                        continue
                    }
                    if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                        continue
                    }
                }
                ParserConstants.LOCATION_9 -> {
                    binaryOperatorExpressionGenerator.generateExpression(resultStack, top.token)

                    if(bitwiseAndValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, bitwiseAndValues, ParserConstants.LOCATION_9, stack)
                        continue
                    }
                    if(relationalEqualsOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalEqualsOperatorValues, ParserConstants.LOCATION_10, stack)
                        continue
                    }
                    if(relationalOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalOperatorValues, ParserConstants.LOCATION_11, stack)
                        continue
                    }
                    if(shiftValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, shiftValues, ParserConstants.LOCATION_12, stack)
                        continue
                    }
                    if(factorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, factorValues, ParserConstants.LOCATION_13, stack)
                        continue
                    }
                    if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                        continue
                    }
                    if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                        continue
                    }
                }
                ParserConstants.LOCATION_10 -> {
                    binaryRelationalOperatorExpressionGenerator.generateExpression(resultStack, top.token)

                    if(relationalEqualsOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalEqualsOperatorValues, ParserConstants.LOCATION_10, stack)
                        continue
                    }
                    if(relationalOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalOperatorValues, ParserConstants.LOCATION_11, stack)
                        continue
                    }
                    if(shiftValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, shiftValues, ParserConstants.LOCATION_12, stack)
                        continue
                    }
                    if(factorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, factorValues, ParserConstants.LOCATION_13, stack)
                        continue
                    }
                    if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                        continue
                    }
                    if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                        continue
                    }
                }
                ParserConstants.LOCATION_11 -> {
                    binaryRelationalOperatorExpressionGenerator.generateExpression(resultStack, top.token)

                    if(relationalOperatorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, relationalOperatorTokenTypes, relationalOperatorValues, ParserConstants.LOCATION_11, stack)
                        continue
                    }
                    if(shiftValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, shiftValues, ParserConstants.LOCATION_12, stack)
                        continue
                    }
                    if(factorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, factorValues, ParserConstants.LOCATION_13, stack)
                        continue
                    }
                    if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                        continue
                    }
                    if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                        continue
                    }
                }
                ParserConstants.LOCATION_12 -> {
                    binaryOperatorExpressionGenerator.generateExpression(resultStack, top.token)

                    if(shiftValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, shiftValues, ParserConstants.LOCATION_12, stack)
                        continue
                    }
                    if(factorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, factorValues, ParserConstants.LOCATION_13, stack)
                        continue
                    }
                    if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                        continue
                    }
                    if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                        continue
                    }
                }
                ParserConstants.LOCATION_13 -> {
                    binaryOperatorExpressionGenerator.generateExpression(resultStack, top.token)

                    if(factorValues.contains(tokens[tokenPosition].value)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryOperatorTokenTypes, factorValues, ParserConstants.LOCATION_13, stack)
                        continue
                    }
                    if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                        continue
                    }
                    if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                        continue
                    }
                }
                ParserConstants.LOCATION_14 -> {
                    binaryOperatorExpressionGenerator.generateExpression(resultStack, top.token)

                    if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                        continue
                    }
                    if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                        continue
                    }
                }
                ParserConstants.LOCATION_15 -> {
                    binaryAssignExpressionGenerator.generateExpression(
                        resultStack,
                        top.token
                    )
                }
            }

        }
        return Pair(resultStack.pop(), tokenPosition)
    }
}
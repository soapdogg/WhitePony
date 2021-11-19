package compiler.parser.impl

import compiler.core.*
import compiler.core.constants.ParserConstants
import compiler.parser.impl.internal.IExpressionStackPusher
import compiler.parser.impl.internal.IExpressionParser

internal class ExpressionParser(
    private val expressionStackPusher: IExpressionStackPusher,
    private val unaryTokenTypes: Set<TokenType>,
    private val unaryValues: Set<String>,
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
    private val binaryAssignValues: Set<String>
): IExpressionParser {

    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<IParsedExpressionNode, Int> {

        val stack = Stack<ExpressionParserStackItem>()
        val resultStack = Stack<IParsedExpressionNode>()
        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1))
        var tokenPosition = startingPosition

        while(stack.isNotEmpty()) {
            val top = stack.pop()

            when {
                top.location == ParserConstants.LOCATION_1 -> {
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
                                tokenPosition++
                                stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_3, null, variableExpression))
                                stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
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
                            tokenPosition++
                            stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_4, null))
                            stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        }
                    }
                }
                top.location == ParserConstants.LOCATION_2 -> {
                    val unaryToken = top.token
                    val insideExpression = resultStack.pop()
                    val unaryExpression = when (unaryToken!!.type) {
                        TokenType.PLUS_MINUS, TokenType.BIT_NEGATION -> {
                            ParsedUnaryOperatorNode(insideExpression, unaryToken.value)
                        }
                        TokenType.UNARY_NOT -> {
                            ParsedUnaryNotOperatorNode(insideExpression)
                        }
                        else -> {
                            ParsedUnaryPreOperatorNode(insideExpression, unaryToken.value[0].toString())
                        }
                    }
                    resultStack.push(unaryExpression)
                }
                top.location == ParserConstants.LOCATION_3 -> {
                    val insideExpression = resultStack.pop()
                    val variableExpression = top.expression as ParsedVariableExpressionNode
                    tokenPosition++
                    var identifierExpression: IParsedExpressionNode = ParsedBinaryArrayOperatorNode(variableExpression, insideExpression)

                    if (tokens[tokenPosition].type == TokenType.PRE_POST) {
                        val prePostToken = tokens[tokenPosition]
                        tokenPosition++
                        identifierExpression = ParsedUnaryPostOperatorNode(identifierExpression, prePostToken.value[0].toString())
                    }
                    resultStack.push(identifierExpression)
                }
                top.location == ParserConstants.LOCATION_4 ->{
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

            when {
                top.location == ParserConstants.LOCATION_5 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val binaryOrExpression = ParsedBinaryOrOperatorNode(leftExpression, rightExpression)
                    resultStack.push(binaryOrExpression)

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
                top.location == ParserConstants.LOCATION_6 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val binaryAndExpression = ParsedBinaryAndOperatorNode(leftExpression, rightExpression)
                    resultStack.push(binaryAndExpression)

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
                top.location == ParserConstants.LOCATION_7 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val bitwiseOrExpression = ParsedBinaryOperatorNode(leftExpression, rightExpression, top.token!!.value)
                    resultStack.push(bitwiseOrExpression)

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
                top.location == ParserConstants.LOCATION_8 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val bitwiseXorExpression = ParsedBinaryOperatorNode(leftExpression, rightExpression, top.token!!.value)
                    resultStack.push(bitwiseXorExpression)

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
                top.location == ParserConstants.LOCATION_9 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val bitwiseAndExpression = ParsedBinaryOperatorNode(leftExpression, rightExpression, top.token!!.value)
                    resultStack.push(bitwiseAndExpression)

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
                top.location == ParserConstants.LOCATION_10 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val relationalEqualsExpression = ParsedBinaryRelationalOperatorNode(leftExpression, rightExpression, top.token!!.value)
                    resultStack.push(relationalEqualsExpression)

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
                top.location == ParserConstants.LOCATION_11 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val relationalOperatorExpression = ParsedBinaryRelationalOperatorNode(leftExpression, rightExpression, top.token!!.value)
                    resultStack.push(relationalOperatorExpression)

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
                top.location == ParserConstants.LOCATION_12 -> {

                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val shiftExpression = ParsedBinaryOperatorNode(leftExpression, rightExpression, top.token!!.value)
                    resultStack.push(shiftExpression)

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
                top.location == ParserConstants.LOCATION_13 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val factorExpression = ParsedBinaryOperatorNode(leftExpression, rightExpression, top.token!!.value)
                    resultStack.push(factorExpression)


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
                top.location == ParserConstants.LOCATION_14 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val termExpression = ParsedBinaryOperatorNode(leftExpression, rightExpression, top.token!!.value)
                    resultStack.push(termExpression)


                    if(termTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, termTokenTypes, termValues, ParserConstants.LOCATION_14, stack)
                        continue
                    }
                    if(binaryAssignTokenTypes.contains(tokens[tokenPosition].type)) {
                        tokenPosition = expressionStackPusher.push(tokens, tokenPosition, binaryAssignTokenTypes, binaryAssignValues, ParserConstants.LOCATION_15, stack)
                        continue
                    }
                }
                top.location == ParserConstants.LOCATION_15 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val resultNode = if(top.token!!.type == TokenType.BINARY_ASSIGN) {
                        ParsedBinaryAssignNode(leftExpression, rightExpression)
                    } else {
                        ParsedBinaryAssignOperatorNode(leftExpression, rightExpression, top.token.value.replace(ParserConstants.ASSIGN_OPERATOR, ParserConstants.EMPTY))
                    }
                    resultStack.push(resultNode)
                }
            }

        }
        return Pair(resultStack.pop(), tokenPosition)
    }
}
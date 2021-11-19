package compiler.parser.impl

import compiler.core.*
import compiler.core.constants.ParserConstants
import compiler.parser.impl.internal.IExpressionStackPusher
import compiler.parser.impl.internal.IExpressionParser

internal class ExpressionParser(
    private val expressionStackPusher: IExpressionStackPusher,
    private val binaryOperatorTokenTypes: Set<TokenType>,
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
                            tokenPosition = expressionStackPusher.pushUnary(tokens, tokenPosition, stack)
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

            if (tokens[tokenPosition].type == TokenType.BINARY_OR) {
                tokenPosition = expressionStackPusher.pushBinaryOr(tokens, tokenPosition, stack)
                continue
            }
            if (tokens[tokenPosition].type == TokenType.BINARY_AND) {
                tokenPosition++
                stack.push(ExpressionParserStackItem(6, null))
                stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                continue
            }
            if (tokens[tokenPosition].value == ParserConstants.BITWISE_OR_OPERATOR) {
                tokenPosition++
                stack.push(ExpressionParserStackItem(7, null))
                stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                continue
            }
            if (tokens[tokenPosition].value == ParserConstants.BITWISE_XOR_OPERATOR) {
                tokenPosition++
                stack.push(ExpressionParserStackItem(8, null))
                stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                continue
            }
            if(tokens[tokenPosition].value == ParserConstants.BITWISE_AND_OPERATOR) {
                tokenPosition++
                stack.push(ExpressionParserStackItem(9, null))
                stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                continue
            }
            if(tokens[tokenPosition].value == ParserConstants.RELATIONAL_EQUALS || tokens[tokenPosition].value == ParserConstants.RELATIONAL_NOT_EQUALS) {
                val relationalEqualsToken = tokens[tokenPosition]
                tokenPosition++
                stack.push(ExpressionParserStackItem(10, relationalEqualsToken))
                stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                continue
            }
            if(tokens[tokenPosition].value == ParserConstants.GREATER_THAN_OPERATOR || tokens[tokenPosition].value == ParserConstants.GREATER_THAN_EQUALS_OPERATOR ||  tokens[tokenPosition].value == ParserConstants.LESS_THAN_EQUALS_OPERATOR || tokens[tokenPosition].value == ParserConstants.LESS_THAN_OPERATOR ) {
                val relationalOperatorToken = tokens[tokenPosition]
                tokenPosition++
                stack.push(ExpressionParserStackItem(11, relationalOperatorToken))
                stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
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

                    if (tokens[tokenPosition].type == TokenType.BINARY_OR) {
                        tokenPosition = expressionStackPusher.pushBinaryOr(tokens, tokenPosition, stack)
                        continue
                    }
                    if (tokens[tokenPosition].type == TokenType.BINARY_AND) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(6, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if (tokens[tokenPosition].value == ParserConstants.BITWISE_OR_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(7, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if (tokens[tokenPosition].value == ParserConstants.BITWISE_XOR_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(8, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.BITWISE_AND_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(9, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.RELATIONAL_EQUALS || tokens[tokenPosition].value == ParserConstants.RELATIONAL_NOT_EQUALS) {
                        val relationalEqualsToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(10, relationalEqualsToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.GREATER_THAN_OPERATOR || tokens[tokenPosition].value == ParserConstants.GREATER_THAN_EQUALS_OPERATOR ||  tokens[tokenPosition].value == ParserConstants.LESS_THAN_EQUALS_OPERATOR || tokens[tokenPosition].value == ParserConstants.LESS_THAN_OPERATOR ) {
                        val relationalOperatorToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(11, relationalOperatorToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
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
                top.location == 6 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val binaryAndExpression = ParsedBinaryAndOperatorNode(leftExpression, rightExpression)
                    resultStack.push(binaryAndExpression)

                    if (tokens[tokenPosition].type == TokenType.BINARY_AND) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(6, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if (tokens[tokenPosition].value == ParserConstants.BITWISE_OR_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(7, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if (tokens[tokenPosition].value == ParserConstants.BITWISE_XOR_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(8, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.BITWISE_AND_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(9, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.RELATIONAL_EQUALS || tokens[tokenPosition].value == ParserConstants.RELATIONAL_NOT_EQUALS) {
                        val relationalEqualsToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(10, relationalEqualsToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.GREATER_THAN_OPERATOR || tokens[tokenPosition].value == ParserConstants.GREATER_THAN_EQUALS_OPERATOR ||  tokens[tokenPosition].value == ParserConstants.LESS_THAN_EQUALS_OPERATOR || tokens[tokenPosition].value == ParserConstants.LESS_THAN_OPERATOR ) {
                        val relationalOperatorToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(11, relationalOperatorToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
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
                top.location == 7 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val bitwiseOrExpression = ParsedBinaryOperatorNode(leftExpression, rightExpression, ParserConstants.BITWISE_OR_OPERATOR)
                    resultStack.push(bitwiseOrExpression)

                    if (tokens[tokenPosition].value == ParserConstants.BITWISE_OR_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(7, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if (tokens[tokenPosition].value == ParserConstants.BITWISE_XOR_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(8, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.BITWISE_AND_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(9, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.RELATIONAL_EQUALS || tokens[tokenPosition].value == ParserConstants.RELATIONAL_NOT_EQUALS) {
                        val relationalEqualsToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(10, relationalEqualsToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.GREATER_THAN_OPERATOR || tokens[tokenPosition].value == ParserConstants.GREATER_THAN_EQUALS_OPERATOR ||  tokens[tokenPosition].value == ParserConstants.LESS_THAN_EQUALS_OPERATOR || tokens[tokenPosition].value == ParserConstants.LESS_THAN_OPERATOR ) {
                        val relationalOperatorToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(11, relationalOperatorToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
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
                top.location == 8 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val bitwiseXorExpression = ParsedBinaryOperatorNode(leftExpression, rightExpression, ParserConstants.BITWISE_XOR_OPERATOR)
                    resultStack.push(bitwiseXorExpression)

                    if (tokens[tokenPosition].value == ParserConstants.BITWISE_XOR_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(8, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.BITWISE_AND_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(9, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.RELATIONAL_EQUALS || tokens[tokenPosition].value == ParserConstants.RELATIONAL_NOT_EQUALS) {
                        val relationalEqualsToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(10, relationalEqualsToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.GREATER_THAN_OPERATOR || tokens[tokenPosition].value == ParserConstants.GREATER_THAN_EQUALS_OPERATOR ||  tokens[tokenPosition].value == ParserConstants.LESS_THAN_EQUALS_OPERATOR || tokens[tokenPosition].value == ParserConstants.LESS_THAN_OPERATOR ) {
                        val relationalOperatorToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(11, relationalOperatorToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
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
                top.location == 9 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val bitwiseAndExpression = ParsedBinaryOperatorNode(leftExpression, rightExpression, ParserConstants.BITWISE_AND_OPERATOR)
                    resultStack.push(bitwiseAndExpression)

                    if(tokens[tokenPosition].value == ParserConstants.BITWISE_AND_OPERATOR) {
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(9, null))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.RELATIONAL_EQUALS || tokens[tokenPosition].value == ParserConstants.RELATIONAL_NOT_EQUALS) {
                        val relationalEqualsToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(10, relationalEqualsToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.GREATER_THAN_OPERATOR || tokens[tokenPosition].value == ParserConstants.GREATER_THAN_EQUALS_OPERATOR ||  tokens[tokenPosition].value == ParserConstants.LESS_THAN_EQUALS_OPERATOR || tokens[tokenPosition].value == ParserConstants.LESS_THAN_OPERATOR ) {
                        val relationalOperatorToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(11, relationalOperatorToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
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
                top.location == 10 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val relationalEqualsExpression = ParsedBinaryRelationalOperatorNode(leftExpression, rightExpression, top.token!!.value)
                    resultStack.push(relationalEqualsExpression)

                    if(tokens[tokenPosition].value == ParserConstants.RELATIONAL_EQUALS || tokens[tokenPosition].value == ParserConstants.RELATIONAL_NOT_EQUALS) {
                        val relationalEqualsToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(10, relationalEqualsToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
                        continue
                    }
                    if(tokens[tokenPosition].value == ParserConstants.GREATER_THAN_OPERATOR || tokens[tokenPosition].value == ParserConstants.GREATER_THAN_EQUALS_OPERATOR ||  tokens[tokenPosition].value == ParserConstants.LESS_THAN_EQUALS_OPERATOR || tokens[tokenPosition].value == ParserConstants.LESS_THAN_OPERATOR ) {
                        val relationalOperatorToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(11, relationalOperatorToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
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
                top.location == 11 -> {
                    val rightExpression = resultStack.pop()
                    val leftExpression = resultStack.pop()
                    val relationalOperatorExpression = ParsedBinaryRelationalOperatorNode(leftExpression, rightExpression, top.token!!.value)
                    resultStack.push(relationalOperatorExpression)

                    if(tokens[tokenPosition].value == ParserConstants.GREATER_THAN_OPERATOR || tokens[tokenPosition].value == ParserConstants.GREATER_THAN_EQUALS_OPERATOR ||  tokens[tokenPosition].value == ParserConstants.LESS_THAN_EQUALS_OPERATOR || tokens[tokenPosition].value == ParserConstants.LESS_THAN_OPERATOR ) {
                        val relationalOperatorToken = tokens[tokenPosition]
                        tokenPosition++
                        stack.push(ExpressionParserStackItem(11, relationalOperatorToken))
                        stack.push(ExpressionParserStackItem(ParserConstants.LOCATION_1, null))
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
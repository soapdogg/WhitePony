package compiler.parser.impl

import compiler.core.ParsedConstantNode
import compiler.core.ParsedUnaryPreOperatorNode
import compiler.core.*
import compiler.core.constants.ParserConstants
import compiler.parser.impl.internal.IExpressionParser

internal class ExpressionParserRecursive: IExpressionParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterLeftExpression) = when (tokens[startingPosition].type) {
            TokenType.PLUS_MINUS, TokenType.PRE_POST, TokenType.BIT_NEGATION, TokenType.UNARY_NOT -> {
                val unaryToken = tokens[startingPosition]
                val positionAfterUnaryOperator = startingPosition + 1
                val (insideExpression, positionAfterInsideExpression) = parse(tokens, positionAfterUnaryOperator)
                val unaryExpression = when (unaryToken.type) {
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
                Pair(unaryExpression, positionAfterInsideExpression)
            }
            TokenType.FLOATING_POINT, TokenType.INTEGER -> {
                val constantToken = tokens[startingPosition]
                val constantNode = ParsedConstantNode(constantToken.value, constantToken.type == TokenType.INTEGER)
                Pair(constantNode, startingPosition + 1)
            }
            TokenType.IDENTIFIER -> {
                val identifierToken = tokens[startingPosition]
                var positionAfterIdentifier = startingPosition + 1
                val variableExpression = ParsedVariableExpressionNode(identifierToken.value)

                var identifierExpression: IParsedExpressionNode = variableExpression
                if (tokens[positionAfterIdentifier].type == TokenType.LEFT_BRACKET) {
                    positionAfterIdentifier++
                    val (insideExpression, positionAfterInnerExpression) = parse(tokens, positionAfterIdentifier)
                    positionAfterIdentifier = positionAfterInnerExpression + 1
                    identifierExpression = ParsedBinaryArrayOperatorNode(variableExpression, insideExpression)
                }
                if (tokens[positionAfterIdentifier].type == TokenType.PRE_POST) {
                    val prePostToken = tokens[positionAfterIdentifier]
                    positionAfterIdentifier++
                    identifierExpression = ParsedUnaryPostOperatorNode(identifierExpression, prePostToken.value[0].toString())
                }
                Pair(identifierExpression, positionAfterIdentifier)
            }
            else -> {
                val (innerExpression, positionAfterInnerExpression) = parse(tokens, startingPosition + 1)
                Pair(ParsedInnerExpression(innerExpression), positionAfterInnerExpression + 1)
            }
        }

        var result = leftExpression
        var currentPosition = positionAfterLeftExpression

        while(tokens[currentPosition].type == TokenType.BINARY_OR) {
            currentPosition++
            val (rightExpression, positionAfterBinaryOr) = parse(tokens, currentPosition)
            result = ParsedBinaryOrOperatorNode(result, rightExpression)
            currentPosition = positionAfterBinaryOr
        }

        while(tokens[currentPosition].type == TokenType.BINARY_AND) {
            currentPosition++
            val(rightExpression, positionAfterBinaryAnd) = parse(tokens, currentPosition)
            result = ParsedBinaryAndOperatorNode(result, rightExpression)
            currentPosition = positionAfterBinaryAnd
        }

        while(tokens[currentPosition].value == ParserConstants.BITWISE_OR_OPERATOR) {
            currentPosition++
            val(rightExpression, positionAfterBitwiseOr) = parse(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, ParserConstants.BITWISE_OR_OPERATOR)
            currentPosition = positionAfterBitwiseOr
        }

        while(tokens[currentPosition].value == ParserConstants.BITWISE_XOR_OPERATOR) {
            currentPosition++
            val(rightExpression, positionAfterBitwiseXor) = parse(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, ParserConstants.BITWISE_XOR_OPERATOR)
            currentPosition = positionAfterBitwiseXor
        }

        while(tokens[currentPosition].value == ParserConstants.BITWISE_AND_OPERATOR) {
            currentPosition++
            val(rightExpression, positionAfterBitwiseAnd) = parse(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, ParserConstants.BITWISE_AND_OPERATOR)
            currentPosition = positionAfterBitwiseAnd
        }

        while(tokens[currentPosition].value == ParserConstants.RELATIONAL_EQUALS || tokens[currentPosition].value == ParserConstants.RELATIONAL_NOT_EQUALS) {
            val relationalEqualsToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterRelationalEquals) = parse(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorNode(result, rightExpression, relationalEqualsToken.value)
            currentPosition = positionAfterRelationalEquals
        }

        while(tokens[currentPosition].value == ParserConstants.GREATER_THAN_OPERATOR || tokens[currentPosition].value == ParserConstants.GREATER_THAN_EQUALS_OPERATOR ||  tokens[currentPosition].value == ParserConstants.LESS_THAN_EQUALS_OPERATOR || tokens[currentPosition].value == ParserConstants.LESS_THAN_OPERATOR ) {
            val relationalOperatorToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterRelationalOperator) = parse(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorNode(result, rightExpression, relationalOperatorToken.value)
            currentPosition = positionAfterRelationalOperator
        }

        while(tokens[currentPosition].value == ParserConstants.LEFT_SHIFT_OPERATOR || tokens[currentPosition].value == ParserConstants.RIGHT_SHIFT_OPERATOR) {
            val shiftToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterShift) = parse(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, shiftToken.value)
            currentPosition = positionAfterShift
        }

        while(tokens[currentPosition].value == ParserConstants.MULTIPLY_OPERATOR || tokens[currentPosition].value == ParserConstants.DIVIDE_OPERATOR || tokens[currentPosition].value == ParserConstants.MODULUS_OPERATOR) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterTerm) = parse(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterTerm
        }

        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactor) = parse(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterFactor
        }
        if(
            tokens[currentPosition].type == TokenType.BINARY_ASSIGN
            || tokens[currentPosition].type == TokenType.BINARY_ASSIGN_OP
        ) {
            val binaryAssignToken = tokens[currentPosition]
            val positionAfterAssign = currentPosition + 1
            val (rightExpression, positionAfterRightExpression) = parse(tokens, positionAfterAssign)
            val resultNode =  if (binaryAssignToken.type == TokenType.BINARY_ASSIGN) {
                ParsedBinaryAssignNode(result, rightExpression)
            } else {
                ParsedBinaryAssignOperatorNode(result, rightExpression, binaryAssignToken.value.replace(ParserConstants.ASSIGN_OPERATOR, ParserConstants.EMPTY))
            }
            return Pair(resultNode, positionAfterRightExpression)
        }
        return Pair(result, currentPosition)
    }

}
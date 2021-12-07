package compiler.parser.impl

import compiler.core.constants.PrinterConstants
import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.*
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser

internal class RecursiveExpressionParser: IExpressionParser {

    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<IParsedExpressionNode, Int> {
        return parseAssignmentOperator(tokens, startingPosition)
    }

    private fun parseAssignmentOperator(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterLogicalOr) = parseLogicalOr(tokens, startingPosition)
        if(
            tokens[positionAfterLogicalOr].type == TokenType.BINARY_ASSIGN
            || tokens[positionAfterLogicalOr].type == TokenType.BINARY_ASSIGN_OP
        ) {
            val binaryAssignToken = tokens[positionAfterLogicalOr]
            val positionAfterAssign = positionAfterLogicalOr + 1
            val (rightExpression, positionAfterRightExpression) = parseAssignmentOperator(tokens, positionAfterAssign)
            val resultNode =  if (binaryAssignToken.type == TokenType.BINARY_ASSIGN) {
                ParsedBinaryAssignExpressionNode(leftExpression, rightExpression)
            } else {
                ParsedBinaryAssignOperatorExpressionNode(leftExpression, rightExpression, binaryAssignToken.value.replace(TokenizerConstants.ASSIGN_OPERATOR, PrinterConstants.EMPTY))
            }
            return Pair(resultNode, positionAfterRightExpression)
        }
        return Pair(leftExpression, positionAfterLogicalOr)
    }

    private fun parseLogicalOr(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterLogicalAnd) = parseLogicalAnd(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterLogicalAnd
        while(tokens[currentPosition].type == TokenType.BINARY_OR) {
            currentPosition++
            val (rightExpression, positionAfterLogicalAndInside) = parseLogicalAnd(tokens, currentPosition)
            result = ParsedBinaryOrOperatorExpressionNode(result, rightExpression)
            currentPosition = positionAfterLogicalAndInside
        }
        return Pair(result, currentPosition)
    }

    private fun parseLogicalAnd(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterBitwiseOr) = parseBitwiseOr(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterBitwiseOr
        while(tokens[currentPosition].type == TokenType.BINARY_AND) {
            currentPosition++
            val(rightExpression, positionAfterBitwiseOrInside) = parseBitwiseOr(tokens, currentPosition)
            result = ParsedBinaryAndOperatorExpressionNode(result, rightExpression)
            currentPosition = positionAfterBitwiseOrInside
        }
        return Pair(result, currentPosition)
    }

    private fun parseBitwiseOr(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterBitwiseXor) = parseBitwiseXor(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterBitwiseXor
        while(tokens[currentPosition].value == TokenizerConstants.BITWISE_OR_OPERATOR) {
            currentPosition++
            val(rightExpression, positionAfterBitwiseXorInside) = parseBitwiseXor(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, TokenizerConstants.BITWISE_OR_OPERATOR)
            currentPosition = positionAfterBitwiseXorInside
        }
        return Pair(result, currentPosition)
    }

    private fun parseBitwiseXor(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterBitwiseAnd) = parseBitwiseAnd(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterBitwiseAnd
        while(tokens[currentPosition].value == TokenizerConstants.BITWISE_XOR_OPERATOR) {
            currentPosition++
            val(rightExpression, positionAfterBitwiseAndInside) = parseBitwiseAnd(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, TokenizerConstants.BITWISE_XOR_OPERATOR)
            currentPosition = positionAfterBitwiseAndInside
        }
        return Pair(result, currentPosition)
    }

    private fun parseBitwiseAnd(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterRelationalEquals) = parseRelationalEquals(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterRelationalEquals
        while(tokens[currentPosition].value == TokenizerConstants.BITWISE_AND_OPERATOR) {
            currentPosition++
            val(rightExpression, positionAfterRelationalEqualsInside) = parseRelationalEquals(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, TokenizerConstants.BITWISE_AND_OPERATOR)
            currentPosition = positionAfterRelationalEqualsInside
        }
        return Pair(result, currentPosition)
    }

    private fun parseRelationalEquals(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterRelationalOperator) = parseRelationalOperator(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterRelationalOperator
        while(tokens[currentPosition].value == TokenizerConstants.RELATIONAL_NOT_EQUALS || tokens[currentPosition].value == TokenizerConstants.RELATIONAL_EQUALS) {
            val relationalEqualsToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterRelationalOperatorInside) = parseRelationalOperator(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalEqualsToken.value)
            currentPosition = positionAfterRelationalOperatorInside
        }
        return Pair(result, currentPosition)
    }

    private fun parseRelationalOperator(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterShift) = parseShift(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterShift
        while(tokens[currentPosition].value == TokenizerConstants.LESS_THAN_OPERATOR || tokens[currentPosition].value == TokenizerConstants.LESS_THAN_EQUALS_OPERATOR ||  tokens[currentPosition].value == TokenizerConstants.GREATER_THAN_OPERATOR || tokens[currentPosition].value == TokenizerConstants.GREATER_THAN_EQUALS_OPERATOR) {
            val relationalOperatorToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterShiftInside) = parseShift(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalOperatorToken.value)
            currentPosition = positionAfterShiftInside
        }
        return Pair(result, currentPosition)
    }

    private fun parseShift(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterTerm) = parseTerm(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterTerm
        while(tokens[currentPosition].value == TokenizerConstants.LEFT_SHIFT_OPERATOR || tokens[currentPosition].value == TokenizerConstants.RIGHT_SHIFT_OPERATOR) {
            val shiftToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterTermInside) = parseTerm(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, shiftToken.value)
            currentPosition = positionAfterTermInside
        }
        return Pair(result, currentPosition)
    }

    private fun parseTerm(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterFactor) = parseFactor(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterFactor
        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactorInside) = parseFactor(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterFactorInside
        }
        return Pair(result, currentPosition)
    }

    private fun parseFactor(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterUnary) = parseUnary(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterUnary
        while(tokens[currentPosition].value == TokenizerConstants.MULTIPLY_OPERATOR || tokens[currentPosition].value == TokenizerConstants.DIVIDE_OPERATOR || tokens[currentPosition].value == TokenizerConstants.MODULUS_OPERATOR) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterUnaryInside) = parseUnary(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterUnaryInside
        }
        return Pair(result, currentPosition)
    }

    private fun parseUnary(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        if(tokens[startingPosition].type == TokenType.PLUS_MINUS || tokens[startingPosition].type == TokenType.PRE_POST || tokens[startingPosition].type == TokenType.BIT_NEGATION || tokens[startingPosition].type == TokenType.UNARY_NOT) {
            val unaryToken = tokens[startingPosition]
            val positionAfterUnary = startingPosition + 1
            val (rightExpression, positionAfterRightExpression) = parseUnary(tokens, positionAfterUnary)
            val resultExpression = when (unaryToken.type) {
                TokenType.PLUS_MINUS, TokenType.BIT_NEGATION -> {
                    ParsedUnaryExpressionNode(rightExpression, unaryToken.value)
                }
                TokenType.UNARY_NOT -> {
                    ParsedUnaryNotOperatorExpressionNode(rightExpression)
                }
                else -> {
                    ParsedUnaryPreOperatorExpressionNode(rightExpression, unaryToken.value[0].toString())
                }
            }
            return Pair(resultExpression, positionAfterRightExpression)
        }
        return parsePrimary(tokens, startingPosition)
    }

    private fun parsePrimary(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        return when (tokens[startingPosition].type) {
            TokenType.FLOATING_POINT, TokenType.INTEGER -> {
                parseConstant(tokens, startingPosition)
            }
            TokenType.IDENTIFIER -> {
                parseIdentifier(tokens, startingPosition)
            }
            else -> {
                parseInnerExpression(tokens, startingPosition)
            }
        }
    }

    private fun parseConstant(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val constantToken = tokens[startingPosition]
        val type = if (constantToken.type == TokenType.INTEGER) PrinterConstants.INT else PrinterConstants.DOUBLE
        val constantNode = ParsedConstantExpressionNode(constantToken.value, type)
        return Pair(constantNode, startingPosition + 1)
    }

    private fun parseIdentifier(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val identifierToken = tokens[startingPosition]
        var currentPosition = startingPosition + 1
        val variableExpression = ParsedVariableExpressionNode(identifierToken.value)

        var result: IParsedExpressionNode = variableExpression
        if (tokens[currentPosition].type == TokenType.LEFT_BRACKET) {
            currentPosition++
            val (insideExpression, positionAfterInnerExpression) = parse(tokens, currentPosition)
            currentPosition = positionAfterInnerExpression + 1
            result = ParsedBinaryArrayExpressionNode(variableExpression, insideExpression)
        }
        if (tokens[currentPosition].type == TokenType.PRE_POST) {
            val prePostToken = tokens[currentPosition]
            currentPosition++
            val operator = prePostToken.value[0].toString()
            val oppositeOperator = if (operator == TokenizerConstants.PLUS_OPERATOR) TokenizerConstants.MINUS_OPERATOR else TokenizerConstants.PLUS_OPERATOR
            result = ParsedUnaryPostOperatorExpressionNode(result, prePostToken.value[0].toString(), oppositeOperator)
        }
        return Pair(result, currentPosition)
    }

    private fun parseInnerExpression(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (innerExpression, positionAfterInnerExpression) = parse(tokens, startingPosition + 1)
        return Pair(ParsedInnerExpressionNode(innerExpression), positionAfterInnerExpression + 1)
    }
}
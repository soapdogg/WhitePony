package compiler.parser.impl

import compiler.core.ParsedConstantNode
import compiler.core.ParsedUnaryPreOperatorNode
import compiler.core.*
import compiler.parser.impl.internal.IExpressionParser

internal class ExpressionParserRecursive: IExpressionParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterLogicalOr) = parseLogicalOr(tokens, startingPosition)
        if(
            tokens[positionAfterLogicalOr].type == TokenType.BINARY_ASSIGN
            || tokens[positionAfterLogicalOr].type == TokenType.BINARY_ASSIGN_OP
        ) {
            val binaryAssignToken = tokens[positionAfterLogicalOr]
            val positionAfterAssign = positionAfterLogicalOr + 1
            val (rightExpression, positionAfterRightExpression) = parse(tokens, positionAfterAssign)
            val resultNode =  if (binaryAssignToken.type == TokenType.BINARY_ASSIGN) {
                ParsedBinaryAssignNode(leftExpression, rightExpression)
            } else {
                ParsedBinaryAssignOperatorNode(leftExpression, rightExpression, binaryAssignToken.value.replace("=", ""))
            }
            return Pair(resultNode, positionAfterRightExpression)
        }
        return Pair(leftExpression, positionAfterLogicalOr)
    }

    private fun parseLogicalOr(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterLogicalAnd) =
            if(tokens[startingPosition].type == TokenType.PLUS_MINUS || tokens[startingPosition].type == TokenType.PRE_POST || tokens[startingPosition].type == TokenType.BIT_NEGATION || tokens[startingPosition].type == TokenType.UNARY_NOT) {
                val unaryToken = tokens[startingPosition]
                val positionAfterUnary = startingPosition + 1
                val (rightExpression, positionAfterRightExpression) = parseLogicalOr(tokens, positionAfterUnary)
                val resultExpression = if (unaryToken.type == TokenType.PLUS_MINUS || unaryToken.type == TokenType.BIT_NEGATION) {
                    ParsedUnaryOperatorNode(rightExpression, unaryToken.value)
                } else if (unaryToken.type == TokenType.UNARY_NOT) {
                    ParsedUnaryNotOperatorNode(rightExpression)
                } else {
                    ParsedUnaryPreOperatorNode(rightExpression, unaryToken.value[0].toString())
                }
                 Pair(resultExpression, positionAfterRightExpression)
            }
        else if (tokens[startingPosition].type == TokenType.FLOATING_POINT || tokens[startingPosition].type == TokenType.INTEGER) {
            val constantToken = tokens[startingPosition]
            val constantNode = ParsedConstantNode(constantToken.value, constantToken.type == TokenType.INTEGER)
            Pair(constantNode, startingPosition + 1)
        } else if (tokens[startingPosition].type == TokenType.IDENTIFIER) {
            val identifierToken = tokens[startingPosition]
            var currentPosition = startingPosition + 1
            val variableExpression = ParsedVariableExpressionNode(identifierToken.value)

            var result: IParsedExpressionNode = variableExpression
            if (tokens[currentPosition].type == TokenType.LEFT_BRACKET) {
                currentPosition++
                val (insideExpression, positionAfterInnerExpression) = parse(tokens, currentPosition)
                currentPosition = positionAfterInnerExpression + 1
                result = ParsedBinaryArrayOperatorNode(variableExpression, insideExpression)
            }
            if (tokens[currentPosition].type == TokenType.PRE_POST) {
                val prePostToken = tokens[currentPosition]
                currentPosition++
                result = ParsedUnaryPostOperatorNode(result, prePostToken.value[0].toString())
            }
            Pair(result, currentPosition)
        } else {
            val (innerExpression, positionAfterInnerExpression) = parse(tokens, startingPosition + 1)
            Pair(ParsedInnerExpression(innerExpression), positionAfterInnerExpression + 1)
        }

        var result = leftExpression
        var currentPosition = positionAfterLogicalAnd

        while(tokens[currentPosition].type == TokenType.BINARY_OR) {
            currentPosition++
            val (rightExpression, positionAfterLogicalAnd) = parseLogicalOr(tokens, currentPosition)
            result = ParsedBinaryOrOperatorNode(result, rightExpression)
            currentPosition = positionAfterLogicalAnd
        }

        while(tokens[currentPosition].type == TokenType.BINARY_AND) {
            currentPosition++
            val(rightExpression, positionAfterBitwiseOr) = parseLogicalOr(tokens, currentPosition)
            result = ParsedBinaryAndOperatorNode(result, rightExpression)
            currentPosition = positionAfterBitwiseOr
        }

        while(tokens[currentPosition].value == "|") {
            currentPosition++
            val(rightExpression, positionAfterBitwiseXor) = parseLogicalOr(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, "|")
            currentPosition = positionAfterBitwiseXor
        }

        while(tokens[currentPosition].value == "^") {
            currentPosition++
            val(rightExpression, positionAfterBitwiseAnd) = parseLogicalOr(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, "^")
            currentPosition = positionAfterBitwiseAnd
        }

        while(tokens[currentPosition].value == "&") {
            currentPosition++
            val(rightExpression, positionAfterRelationalEquals) = parseLogicalOr(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, "&")
            currentPosition = positionAfterRelationalEquals
        }

        while(tokens[currentPosition].value == "!=" || tokens[currentPosition].value == "==") {
            val relationalEqualsToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterRelationalOperator) = parseLogicalOr(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorNode(result, rightExpression, relationalEqualsToken.value)
            currentPosition = positionAfterRelationalOperator
        }

        while(tokens[currentPosition].value == "<" || tokens[currentPosition].value == "<=" ||  tokens[currentPosition].value == ">" || tokens[currentPosition].value == ">=" ) {
            val relationalOperatorToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterShift) = parseLogicalOr(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorNode(result, rightExpression, relationalOperatorToken.value)
            currentPosition = positionAfterShift
        }

        while(tokens[currentPosition].value == ">>" || tokens[currentPosition].value == "<<") {
            val shiftToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterTerm) = parseLogicalOr(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, shiftToken.value)
            currentPosition = positionAfterTerm
        }

        while(tokens[currentPosition].value == "*" || tokens[currentPosition].value == "/" || tokens[currentPosition].value == "%") {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterUnary) = parseLogicalOr(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterUnary
        }

        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactor) = parseLogicalOr(tokens, currentPosition)
            result = ParsedBinaryOperatorNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterFactor
        }

        return Pair(result, currentPosition)
    }

}
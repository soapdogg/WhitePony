package compiler.parser.impl

import compiler.core.ConstantNode
import compiler.core.UnaryPreOperatorNode
import compiler.core.*
import compiler.parser.impl.internal.IExpressionParser

internal class ExpressionParserRecursive: IExpressionParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<IExpressionNode, Int> {
        return parseAssignmentOperator(tokens, startingPosition)
    }

    private fun parseAssignmentOperator(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (leftExpression, positionAfterLogicalOr) = parseLogicalOr(tokens, startingPosition)
        if(
            tokens[positionAfterLogicalOr].type == TokenType.BINARY_ASSIGN
            || tokens[positionAfterLogicalOr].type == TokenType.BINARY_ASSIGN_OP
        ) {
            val binaryAssignToken = tokens[positionAfterLogicalOr]
            val positionAfterAssign = positionAfterLogicalOr + 1
            val (rightExpression, positionAfterRightExpression) = parseAssignmentOperator(tokens, positionAfterAssign)
            val resultNode =  if (binaryAssignToken.type == TokenType.BINARY_ASSIGN) {
                BinaryAssignNode(leftExpression, rightExpression)
            } else {
                BinaryAssignOperatorNode(leftExpression, rightExpression, binaryAssignToken.value.replace("=", ""))
            }
            return Pair(resultNode, positionAfterRightExpression)
        }
        return Pair(leftExpression, positionAfterLogicalOr)
    }

    private fun parseLogicalOr(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (leftExpression, positionAfterLogicalAnd) = parseLogicalAnd(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterLogicalAnd
        while(tokens[currentPosition].type == TokenType.BINARY_OR) {
            currentPosition++
            val (rightExpression, positionAfterLogicalAnd) = parseLogicalAnd(tokens, currentPosition)
            result = BinaryOrOperatorNode(result, rightExpression)
            currentPosition = positionAfterLogicalAnd
        }
        return Pair(result, currentPosition)
    }

    private fun parseLogicalAnd(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (leftExpression, positionAfterBitwiseOr) = parseBitwiseOr(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterBitwiseOr
        while(tokens[currentPosition].type == TokenType.BINARY_AND) {
            currentPosition++
            val(rightExpression, positionAfterBitwiseOr) = parseBitwiseOr(tokens, currentPosition)
            result = BinaryAndOperatorNode(result, rightExpression)
            currentPosition = positionAfterBitwiseOr
        }
        return Pair(result, currentPosition)
    }

    private fun parseBitwiseOr(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (leftExpression, positionAfterBitwiseXor) = parseBitwiseXor(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterBitwiseXor
        while(tokens[currentPosition].value == "|") {
            currentPosition++
            val(rightExpression, positionAfterBitwiseXor) = parseBitwiseXor(tokens, currentPosition)
            result = BinaryOperatorNode(result, rightExpression, "|")
            currentPosition = positionAfterBitwiseXor
        }
        return Pair(result, currentPosition)
    }

    private fun parseBitwiseXor(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (leftExpression, positionAfterBitwiseAnd) = parseBitwiseAnd(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterBitwiseAnd
        while(tokens[currentPosition].value == "^") {
            currentPosition++
            val(rightExpression, positionAfterBitwiseAnd) = parseBitwiseAnd(tokens, currentPosition)
            result = BinaryOperatorNode(result, rightExpression, "^")
            currentPosition = positionAfterBitwiseAnd
        }
        return Pair(result, currentPosition)
    }

    private fun parseBitwiseAnd(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (leftExpression, positionAfterRelationalEquals) = parseRelationalEquals(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterRelationalEquals
        while(tokens[currentPosition].value == "&") {
            currentPosition++
            val(rightExpression, positionAfterRelationalEquals) = parseRelationalEquals(tokens, currentPosition)
            result = BinaryOperatorNode(result, rightExpression, "&")
            currentPosition = positionAfterRelationalEquals
        }
        return Pair(result, currentPosition)
    }

    private fun parseRelationalEquals(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (leftExpression, positionAfterRelationalOperator) = parseRelationalOperator(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterRelationalOperator
        while(tokens[currentPosition].value == "!=" || tokens[currentPosition].value == "==") {
            val relationalEqualsToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterRelationalOperator) = parseRelationalOperator(tokens, currentPosition)
            result = BinaryRelationalOperatorNode(result, rightExpression, relationalEqualsToken.value)
            currentPosition = positionAfterRelationalOperator
        }
        return Pair(result, currentPosition)
    }

    private fun parseRelationalOperator(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (leftExpression, positionAfterShift) = parseShift(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterShift
        while(tokens[currentPosition].value == "<" || tokens[currentPosition].value == "<=" ||  tokens[currentPosition].value == ">" || tokens[currentPosition].value == ">=" ) {
            val relationalOperatorToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterShift) = parseShift(tokens, currentPosition)
            result = BinaryRelationalOperatorNode(result, rightExpression, relationalOperatorToken.value)
            currentPosition = positionAfterShift
        }
        return Pair(result, currentPosition)
    }

    private fun parseShift(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (leftExpression, positionAfterTerm) = parseTerm(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterTerm
        while(tokens[currentPosition].value == ">>" || tokens[currentPosition].value == "<<") {
            val shiftToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterTerm) = parseTerm(tokens, currentPosition)
            result = BinaryOperatorNode(result, rightExpression, shiftToken.value)
            currentPosition = positionAfterTerm
        }
        return Pair(result, currentPosition)
    }

    private fun parseTerm(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (leftExpression, positionAfterFactor) = parseFactor(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterFactor
        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactor) = parseFactor(tokens, currentPosition)
            result = BinaryOperatorNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterFactor
        }
        return Pair(result, currentPosition)
    }

    private fun parseFactor(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (leftExpression, positionAfterUnary) = parseUnary(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterUnary
        while(tokens[currentPosition].value == "*" || tokens[currentPosition].value == "/" || tokens[currentPosition].value == "%") {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterUnary) = parseUnary(tokens, currentPosition)
            result = BinaryOperatorNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterUnary
        }
        return Pair(result, currentPosition)
    }

    private fun parseUnary(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        if(tokens[startingPosition].type == TokenType.PLUS_MINUS || tokens[startingPosition].type == TokenType.PRE_POST || tokens[startingPosition].type == TokenType.BIT_NEGATION || tokens[startingPosition].type == TokenType.UNARY_NOT) {
            val unaryToken = tokens[startingPosition]
            val positionAfterUnary = startingPosition + 1
            val (rightExpression, positionAfterRightExpression) = parseUnary(tokens, positionAfterUnary)
            val resultExpression = if (unaryToken.type == TokenType.PLUS_MINUS || unaryToken.type == TokenType.BIT_NEGATION) {
                UnaryOperatorNode(rightExpression, unaryToken.value)
            } else if (unaryToken.type == TokenType.UNARY_NOT) {
                UnaryNotOperatorNode(rightExpression)
            } else {
                UnaryPreOperatorNode(rightExpression, unaryToken.value[0].toString())
            }
            return Pair(resultExpression, positionAfterRightExpression)
        }
        return parsePrimary(tokens, startingPosition)
    }

    private fun parsePrimary(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        return if (tokens[startingPosition].type == TokenType.FLOATING_POINT || tokens[startingPosition].type == TokenType.INTEGER) {
            parseConstant(tokens, startingPosition)
        } else if (tokens[startingPosition].type == TokenType.IDENTIFIER) {
            parseIdentifier(tokens, startingPosition)
        } else {
            parseInnerExpression(tokens, startingPosition)
        }
    }

    private fun parseConstant(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val constantToken = tokens[startingPosition]
        val constantNode = ConstantNode(constantToken.value, constantToken.type == TokenType.INTEGER)
        return Pair(constantNode, startingPosition + 1)
    }

    private fun parseIdentifier(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val identifierToken = tokens[startingPosition]
        var currentPosition = startingPosition + 1
        val variableExpression = VariableExpressionNode(identifierToken.value)

        var result: IExpressionNode = variableExpression
        if (tokens[currentPosition].type == TokenType.LEFT_BRACKET) {
            currentPosition++
            val (insideExpression, positionAfterInnerExpression) = parse(tokens, currentPosition)
            currentPosition = positionAfterInnerExpression + 1
            result = BinaryArrayOperatorNode(variableExpression, insideExpression)
        }
        if (tokens[currentPosition].type == TokenType.PRE_POST) {
            val prePostToken = tokens[currentPosition]
            currentPosition++
            result = UnaryPostOperatorNode(result, prePostToken.value[0].toString())
        }
        return Pair(result, currentPosition)
    }

    private fun parseInnerExpression(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IExpressionNode, Int> {
        val (innerExpression, positionAfterInnerExpression) = parse(tokens, startingPosition + 1)
        return Pair(innerExpression, positionAfterInnerExpression + 1)
    }
}
package compiler.parser.impl

import compiler.core.constants.PrinterConstants
import compiler.core.constants.TokenizerConstants
import compiler.core.nodes.parsed.*
import compiler.core.tokenizer.Token
import compiler.core.tokenizer.TokenType
import compiler.parser.impl.internal.IExpressionParser

internal class ExpressionParser: IExpressionParser {

    override fun parse(
        tokens: List<Token>,
        startingPosition: Int,
    ): Pair<IParsedExpressionNode, Int> {
        var (leftExpression, positionAfterBitwiseOr) = parseBitwiseOr(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterBitwiseOr
        while(tokens[currentPosition].type == TokenType.BINARY_AND) {
            currentPosition++
            val(rightExpression, positionAfterBitwiseOr) = parseBitwiseOr(tokens, currentPosition)
            result = ParsedBinaryAndOperatorExpressionNode(result, rightExpression)
            currentPosition = positionAfterBitwiseOr
        }

        while(tokens[currentPosition].type == TokenType.BINARY_OR) {
            currentPosition++
            val (rightExpression, positionAfterLogicalAnd) = parseLogicalAnd(tokens, currentPosition)
            result = ParsedBinaryOrOperatorExpressionNode(result, rightExpression)
            currentPosition = positionAfterLogicalAnd
        }
        if(
            tokens[currentPosition].type == TokenType.BINARY_ASSIGN
            || tokens[currentPosition].type == TokenType.BINARY_ASSIGN_OP
        ) {
            val binaryAssignToken = tokens[currentPosition]
            val positionAfterAssign = currentPosition + 1
            val (rightExpression, positionAfterRightExpression) = parse(tokens, positionAfterAssign)
            val resultNode =  if (binaryAssignToken.type == TokenType.BINARY_ASSIGN) {
                ParsedBinaryAssignExpressionNode(leftExpression, rightExpression)
            } else {
                ParsedBinaryAssignOperatorNode(leftExpression, rightExpression, binaryAssignToken.value.replace("=", ""))
            }
            return Pair(resultNode, positionAfterRightExpression)
        }
        return Pair(result, currentPosition)
    }

    private fun parseLogicalAnd(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterFactor) = parseFactor(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterFactor
        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactor) = parseFactor(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterFactor
        }

        while(tokens[currentPosition].value == ">>" || tokens[currentPosition].value == "<<") {
            val shiftToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterTerm) = parseTerm(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, shiftToken.value)
            currentPosition = positionAfterTerm
        }

        while(tokens[currentPosition].value == "<" || tokens[currentPosition].value == "<=" ||  tokens[currentPosition].value == ">" || tokens[currentPosition].value == ">=" ) {
            val relationalOperatorToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterShift) = parseShift(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalOperatorToken.value)
            currentPosition = positionAfterShift
        }
        while(tokens[currentPosition].value == "!=" || tokens[currentPosition].value == "==") {
            val relationalEqualsToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterRelationalOperator) = parseRelationalOperator(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalEqualsToken.value)
            currentPosition = positionAfterRelationalOperator
        }
        while(tokens[currentPosition].value == "&") {
            currentPosition++
            val(rightExpression, positionAfterRelationalEquals) = parseRelationalEquals(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, "&")
            currentPosition = positionAfterRelationalEquals
        }
        while(tokens[currentPosition].value == "^") {
            currentPosition++
            val(rightExpression, positionAfterBitwiseAnd) = parseBitwiseAnd(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, "^")
            currentPosition = positionAfterBitwiseAnd
        }
        while(tokens[currentPosition].value == "|") {
            currentPosition++
            val(rightExpression, positionAfterBitwiseXor) = parseBitwiseXor(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, "|")
            currentPosition = positionAfterBitwiseXor
        }
        while(tokens[currentPosition].type == TokenType.BINARY_AND) {
            currentPosition++
            val(rightExpression, positionAfterBitwiseOr) = parseBitwiseOr(tokens, currentPosition)
            result = ParsedBinaryAndOperatorExpressionNode(result, rightExpression)
            currentPosition = positionAfterBitwiseOr
        }
        return Pair(result, currentPosition)
    }

    private fun parseBitwiseOr(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterFactor) = parseFactor(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterFactor
        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactor) = parseFactor(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterFactor
        }

        while(tokens[currentPosition].value == ">>" || tokens[currentPosition].value == "<<") {
            val shiftToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterTerm) = parseTerm(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, shiftToken.value)
            currentPosition = positionAfterTerm
        }

        while(tokens[currentPosition].value == "<" || tokens[currentPosition].value == "<=" ||  tokens[currentPosition].value == ">" || tokens[currentPosition].value == ">=" ) {
            val relationalOperatorToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterShift) = parseShift(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalOperatorToken.value)
            currentPosition = positionAfterShift
        }
        while(tokens[currentPosition].value == "!=" || tokens[currentPosition].value == "==") {
            val relationalEqualsToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterRelationalOperator) = parseRelationalOperator(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalEqualsToken.value)
            currentPosition = positionAfterRelationalOperator
        }
        while(tokens[currentPosition].value == "&") {
            currentPosition++
            val(rightExpression, positionAfterRelationalEquals) = parseRelationalEquals(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, "&")
            currentPosition = positionAfterRelationalEquals
        }
        while(tokens[currentPosition].value == "^") {
            currentPosition++
            val(rightExpression, positionAfterBitwiseAnd) = parseBitwiseAnd(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, "^")
            currentPosition = positionAfterBitwiseAnd
        }
        while(tokens[currentPosition].value == "|") {
            currentPosition++
            val(rightExpression, positionAfterBitwiseXor) = parseBitwiseXor(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, "|")
            currentPosition = positionAfterBitwiseXor
        }
        return Pair(result, currentPosition)
    }

    private fun parseBitwiseXor(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterFactor) = parseFactor(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterFactor
        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactor) = parseFactor(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterFactor
        }

        while(tokens[currentPosition].value == ">>" || tokens[currentPosition].value == "<<") {
            val shiftToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterTerm) = parseTerm(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, shiftToken.value)
            currentPosition = positionAfterTerm
        }

        while(tokens[currentPosition].value == "<" || tokens[currentPosition].value == "<=" ||  tokens[currentPosition].value == ">" || tokens[currentPosition].value == ">=" ) {
            val relationalOperatorToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterShift) = parseShift(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalOperatorToken.value)
            currentPosition = positionAfterShift
        }
        while(tokens[currentPosition].value == "!=" || tokens[currentPosition].value == "==") {
            val relationalEqualsToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterRelationalOperator) = parseRelationalOperator(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalEqualsToken.value)
            currentPosition = positionAfterRelationalOperator
        }
        while(tokens[currentPosition].value == "&") {
            currentPosition++
            val(rightExpression, positionAfterRelationalEquals) = parseRelationalEquals(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, "&")
            currentPosition = positionAfterRelationalEquals
        }
        while(tokens[currentPosition].value == "^") {
            currentPosition++
            val(rightExpression, positionAfterBitwiseAnd) = parseBitwiseAnd(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, "^")
            currentPosition = positionAfterBitwiseAnd
        }
        return Pair(result, currentPosition)
    }

    private fun parseBitwiseAnd(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterFactor) = parseFactor(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterFactor
        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactor) = parseFactor(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterFactor
        }

        while(tokens[currentPosition].value == ">>" || tokens[currentPosition].value == "<<") {
            val shiftToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterTerm) = parseTerm(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, shiftToken.value)
            currentPosition = positionAfterTerm
        }

        while(tokens[currentPosition].value == "<" || tokens[currentPosition].value == "<=" ||  tokens[currentPosition].value == ">" || tokens[currentPosition].value == ">=" ) {
            val relationalOperatorToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterShift) = parseShift(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalOperatorToken.value)
            currentPosition = positionAfterShift
        }
        while(tokens[currentPosition].value == "!=" || tokens[currentPosition].value == "==") {
            val relationalEqualsToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterRelationalOperator) = parseRelationalOperator(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalEqualsToken.value)
            currentPosition = positionAfterRelationalOperator
        }
        while(tokens[currentPosition].value == "&") {
            currentPosition++
            val(rightExpression, positionAfterRelationalEquals) = parseRelationalEquals(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, "&")
            currentPosition = positionAfterRelationalEquals
        }
        return Pair(result, currentPosition)
    }

    private fun parseRelationalEquals(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterFactor) = parseFactor(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterFactor
        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactor) = parseFactor(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterFactor
        }

        while(tokens[currentPosition].value == ">>" || tokens[currentPosition].value == "<<") {
            val shiftToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterTerm) = parseTerm(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, shiftToken.value)
            currentPosition = positionAfterTerm
        }

        while(tokens[currentPosition].value == "<" || tokens[currentPosition].value == "<=" ||  tokens[currentPosition].value == ">" || tokens[currentPosition].value == ">=" ) {
            val relationalOperatorToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterShift) = parseShift(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalOperatorToken.value)
            currentPosition = positionAfterShift
        }
        while(tokens[currentPosition].value == "!=" || tokens[currentPosition].value == "==") {
            val relationalEqualsToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterRelationalOperator) = parseRelationalOperator(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalEqualsToken.value)
            currentPosition = positionAfterRelationalOperator
        }
        return Pair(result, currentPosition)
    }

    private fun parseRelationalOperator(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterFactor) = parseFactor(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterFactor
        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactor) = parseFactor(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterFactor
        }

        while(tokens[currentPosition].value == ">>" || tokens[currentPosition].value == "<<") {
            val shiftToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterTerm) = parseTerm(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, shiftToken.value)
            currentPosition = positionAfterTerm
        }

        while(tokens[currentPosition].value == "<" || tokens[currentPosition].value == "<=" ||  tokens[currentPosition].value == ">" || tokens[currentPosition].value == ">=" ) {
            val relationalOperatorToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterShift) = parseShift(tokens, currentPosition)
            result = ParsedBinaryRelationalOperatorExpressionNode(result, rightExpression, relationalOperatorToken.value)
            currentPosition = positionAfterShift
        }
        return Pair(result, currentPosition)
    }

    private fun parseShift(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterFactor) = parseFactor(tokens, startingPosition)
        var result = leftExpression
        var currentPosition = positionAfterFactor
        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactor) = parseFactor(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterFactor
        }

        while(tokens[currentPosition].value == ">>" || tokens[currentPosition].value == "<<") {
            val shiftToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterTerm) = parseTerm(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, shiftToken.value)
            currentPosition = positionAfterTerm
        }
        return Pair(result, currentPosition)
    }

    private fun parseTerm(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterFactor) = parseFactor(tokens, startingPosition)
        var result2 = leftExpression
        var currentPosition = positionAfterFactor
        while(tokens[currentPosition].type == TokenType.PLUS_MINUS) {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterFactor) = parseFactor(tokens, currentPosition)
            result2 = ParsedBinaryOperatorExpressionNode(result2, rightExpression, termToken.value)
            currentPosition = positionAfterFactor
        }
        return Pair(result2, currentPosition)
    }

    private fun parseFactor(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IParsedExpressionNode, Int> {
        val (leftExpression, positionAfterUnary) = if(tokens[startingPosition].type == TokenType.PLUS_MINUS ||
            tokens[startingPosition].type == TokenType.PRE_POST ||
            tokens[startingPosition].type == TokenType.BIT_NEGATION ||
            tokens[startingPosition].type == TokenType.UNARY_NOT) {
            val unaryToken = tokens[startingPosition]
            val positionAfterUnary = startingPosition + 1
            val (rightExpression, positionAfterRightExpression) = parseUnary(tokens, positionAfterUnary)
            if (unaryToken.type == TokenType.PLUS_MINUS || unaryToken.type == TokenType.BIT_NEGATION) {
                Pair(ParsedUnaryExpressionNode(rightExpression, unaryToken.value), positionAfterRightExpression)
            } else if (unaryToken.type == TokenType.UNARY_NOT) {
                Pair(ParsedUnaryNotOperatorExpressionNode(rightExpression), positionAfterRightExpression)
            } else {
                Pair(ParsedUnaryPreOperatorNode(rightExpression, unaryToken.value[0].toString()), positionAfterRightExpression)
            }
        } else if (tokens[startingPosition].type == TokenType.FLOATING_POINT || tokens[startingPosition].type == TokenType.INTEGER) {
            val constantToken = tokens[startingPosition]
            val type = if (constantToken.type == TokenType.INTEGER) PrinterConstants.INT else PrinterConstants.DOUBLE
            val constantNode = ParsedConstantExpressionNode(constantToken.value, type)
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
                result = ParsedBinaryArrayExpressionNode(variableExpression, insideExpression)
            }
            if (tokens[currentPosition].type == TokenType.PRE_POST) {
                val prePostToken = tokens[currentPosition]
                currentPosition++
                val operator = prePostToken.value[0].toString()
                val oppositeOperator = if (operator == TokenizerConstants.PLUS_OPERATOR) TokenizerConstants.MINUS_OPERATOR else TokenizerConstants.PLUS_OPERATOR
                result = ParsedUnaryPostOperatorNode(result, prePostToken.value[0].toString(), oppositeOperator)
            }
            Pair(result, currentPosition)
        } else {
            val (innerExpression, positionAfterInnerExpression) = parse(tokens, startingPosition + 1,)
            Pair(ParsedInnerExpressionNode(innerExpression), positionAfterInnerExpression + 1)
        }

        var result = leftExpression
        var currentPosition = positionAfterUnary
        while(tokens[currentPosition].value == "*" || tokens[currentPosition].value == "/" || tokens[currentPosition].value == "%") {
            val termToken = tokens[currentPosition]
            currentPosition++
            val(rightExpression, positionAfterUnary) = parseUnary(tokens, currentPosition)
            result = ParsedBinaryOperatorExpressionNode(result, rightExpression, termToken.value)
            currentPosition = positionAfterUnary
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
            val resultExpression = if (unaryToken.type == TokenType.PLUS_MINUS || unaryToken.type == TokenType.BIT_NEGATION) {
                ParsedUnaryExpressionNode(rightExpression, unaryToken.value)
            } else if (unaryToken.type == TokenType.UNARY_NOT) {
                ParsedUnaryNotOperatorExpressionNode(rightExpression)
            } else {
                ParsedUnaryPreOperatorNode(rightExpression, unaryToken.value[0].toString())
            }
            return Pair(resultExpression, positionAfterRightExpression)
        }
        return if (tokens[startingPosition].type == TokenType.FLOATING_POINT || tokens[startingPosition].type == TokenType.INTEGER) {
            val constantToken = tokens[startingPosition]
            val type = if (constantToken.type == TokenType.INTEGER) PrinterConstants.INT else PrinterConstants.DOUBLE
            val constantNode = ParsedConstantExpressionNode(constantToken.value, type)
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
                result = ParsedBinaryArrayExpressionNode(variableExpression, insideExpression)
            }
            if (tokens[currentPosition].type == TokenType.PRE_POST) {
                val prePostToken = tokens[currentPosition]
                currentPosition++
                val operator = prePostToken.value[0].toString()
                val oppositeOperator = if (operator == TokenizerConstants.PLUS_OPERATOR) TokenizerConstants.MINUS_OPERATOR else TokenizerConstants.PLUS_OPERATOR
                result = ParsedUnaryPostOperatorNode(result, prePostToken.value[0].toString(), oppositeOperator)
            }
            Pair(result, currentPosition)
        } else {
            val (innerExpression, positionAfterInnerExpression) = parse(tokens, startingPosition + 1,)
            Pair(ParsedInnerExpressionNode(innerExpression), positionAfterInnerExpression + 1)
        }
    }

}
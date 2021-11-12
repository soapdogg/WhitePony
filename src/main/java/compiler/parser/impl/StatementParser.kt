package compiler.parser.impl

import compiler.core.*
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.IVariableDeclarationParser

internal class StatementParser(
    private val expressionParser: IExpressionParser,
    private val variableDeclarationParser: IVariableDeclarationParser
) : IStatementParser {
    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IStatementNode, Int> {
        val tokenType = tokens[startingPosition].type

        return when (tokenType) {
            TokenType.DO -> {
                //do
                val (body, currentPosition1) = parse(tokens, startingPosition + 1)
                //while
                //LParent
                val (expression, currentPosition2) = expressionParser.parse(tokens, currentPosition1 + 2, setOf(TokenType.RIGHT_PARENTHESES))
                //RParent
                //Semicolon
                val doStatement = DoWhileNode(
                    expression,
                    body
                )
                Pair(doStatement, currentPosition2 + 2)
            }
            TokenType.FOR -> {
                //for
                //LParent
                val (initExpression, currentPosition1) = expressionParser.parse(tokens, startingPosition + 2, setOf(TokenType.SEMICOLON))
                //Semi
                val (testExpression, currentPosition2) = expressionParser.parse(tokens, currentPosition1 + 1, setOf(TokenType.SEMICOLON))
                //Semi
                val (incrementExpression, currentPosition3) = expressionParser.parse(tokens, currentPosition2 + 1, setOf(TokenType.RIGHT_PARENTHESES))
                //RParent
                val (body, currentPosition4) = parse(tokens, currentPosition3 + 1)
                val forNode = ForNode(
                    initExpression,
                    testExpression,
                    incrementExpression,
                    body
                )
                return Pair(forNode, currentPosition4)
            }
            TokenType.IF -> {
                //if
                //LParent
                val (booleanExpression, currentPosition1) = expressionParser.parse(tokens, startingPosition + 2, setOf(TokenType.RIGHT_PARENTHESES))
                //RParent
                val (ifBody, currentPosition2) = parse(tokens, currentPosition1 + 1)
                if (tokens[currentPosition2].type == TokenType.ELSE) {
                    //else
                    val (elseBody, currentPosition3) = parse(tokens, currentPosition2 + 1)
                    val ifNode = IfNode(
                        booleanExpression,
                        ifBody,
                        elseBody
                    )
                    Pair(ifNode, currentPosition3)
                } else {
                    val ifNode = IfNode(
                        booleanExpression,
                        ifBody,
                        null
                    )
                    Pair(ifNode, currentPosition2)
                }
            }
            TokenType.LEFT_BRACE -> {
                //LBrace
                val statements = mutableListOf<IStatementNode>()
                var currentPosition1 = startingPosition + 1
                while (tokens[currentPosition1].type != TokenType.RIGHT_BRACE) {
                    val (statement, cur) = parse(tokens, currentPosition1)
                    statements.add(statement)
                    currentPosition1 = cur
                }
                //RBrace
                val basicBlockNode = BasicBlockNode(statements)
                Pair(basicBlockNode, currentPosition1 + 1)
            }
            TokenType.RETURN -> {
                //return
                val (expression, currentPosition1) = expressionParser.parse(tokens, startingPosition + 1, setOf(TokenType.SEMICOLON)) //expressionStatement?
                //semi
                val returnNode = ReturnNode(expression)
                Pair(returnNode, currentPosition1 + 1)
            }
            TokenType.TYPE -> {
                //type
                val variableDeclarations = mutableListOf<VariableDeclarationNode>()
                var currentPosition1 = startingPosition + 1
                do {
                    //parse variables
                    val (variableDeclaration, currentPosition2) = variableDeclarationParser.parse(tokens, currentPosition1)
                    variableDeclarations.add(variableDeclaration)
                    currentPosition1 = currentPosition2
                    //comma
                    val isComma = tokens[currentPosition2].type == TokenType.COMMA
                    if (isComma) {
                        currentPosition1++
                    }

                } while(isComma)
                //semi
                val variableDeclarationNode = VariableDeclarationListNode(tokens[startingPosition].value, variableDeclarations)
                return Pair(variableDeclarationNode, currentPosition1 + 1)
            }
            TokenType.WHILE -> {
                //while
                //LParent
                val (expression, currentPosition1) = expressionParser.parse(tokens, startingPosition + 2, setOf(TokenType.RIGHT_PARENTHESES))
                //RParent
                val (body, currentPosition2) = parse(tokens, currentPosition1 + 1)
                val whileNode = WhileNode(expression, body)
                Pair(whileNode, currentPosition2)
            }
            else -> {
                val (expression, currentPosition1) = expressionParser.parse(tokens, startingPosition, setOf(TokenType.SEMICOLON))
                //Semi
                val expressionStatementNode = ExpressionStatementNode(expression)
                Pair(expressionStatementNode, currentPosition1 + 1)
            }
        }

    }
}
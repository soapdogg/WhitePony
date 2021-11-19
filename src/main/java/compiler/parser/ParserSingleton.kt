package compiler.parser

import compiler.core.TokenType
import compiler.core.constants.ParserConstants
import compiler.core.constants.TokenizerConstants
import compiler.parser.impl.*
import compiler.parser.impl.DeclarationStatementParser
import compiler.parser.impl.FunctionDeclarationParser
import compiler.parser.impl.Parser

enum class ParserSingleton {
    INSTANCE;

    private val tokenTypeAsserter = TokenTypeAsserter()

    private val expressionStackPusher = ExpressionStackPusher(
        tokenTypeAsserter
    )

    private val binaryOperatorTokenTypes = setOf(TokenType.BINARY_OPERATOR)
    private val shiftValues = setOf(TokenizerConstants.LEFT_SHIFT_OPERATOR, TokenizerConstants.RIGHT_SHIFT_OPERATOR)
    private val factorValues = setOf(TokenizerConstants.MULTIPLY_OPERATOR, TokenizerConstants.DIVIDE_OPERATOR, TokenizerConstants.MODULUS_OPERATOR)
    private val termTokenTypes = setOf(TokenType.PLUS_MINUS)
    private val termValues = setOf(TokenizerConstants.PLUS_OPERATOR, TokenizerConstants.MINUS_OPERATOR)
    private val binaryAssignTokenTypes = setOf(TokenType.BINARY_ASSIGN_OP, TokenType.BINARY_ASSIGN)
    private val binaryAssignValues = setOf(TokenizerConstants.AND_ASSIGN_OPERATOR, TokenizerConstants.DIVIDE_ASSIGN_OPERATOR, TokenizerConstants.LEFT_SHIFT_ASSIGN_OPERATOR, TokenizerConstants.MINUS_ASSIGN_OPERATOR, TokenizerConstants.MODULUS_ASSIGN_OPERATOR, TokenizerConstants.MULTIPLY_ASSIGN_OPERATOR, TokenizerConstants.OR_ASSIGN_OPERATOR, TokenizerConstants.PLUS_ASSIGN_OPERATOR, TokenizerConstants.RIGHT_SHIFT_ASSIGN_OPERATOR, TokenizerConstants.XOR_ASSIGN_OPERATOR, TokenizerConstants.ASSIGN_OPERATOR)

    private val expressionParser = ExpressionParser(
        expressionStackPusher,
        binaryOperatorTokenTypes,
        shiftValues,
        factorValues,
        termTokenTypes,
        termValues,
        binaryAssignTokenTypes,
        binaryAssignValues,
    )
    private val arrayParser = ArrayParser(
        tokenTypeAsserter,
        expressionParser
    )
    private val assignParser = AssignParser(
        tokenTypeAsserter,
        expressionParser,
    )
    private val variableDeclarationParser = VariableDeclarationParser(
        tokenTypeAsserter,
        arrayParser,
        assignParser
    )

    private val variableDeclarationListParser = VariableDeclarationListParser(
        tokenTypeAsserter,
        variableDeclarationParser
    )

    private val expressionStatementParser = ExpressionStatementParser(
        tokenTypeAsserter,
        expressionParser
    )

    private val returnStatementParser = ReturnStatementParser(
        tokenTypeAsserter,
        expressionStatementParser
    )

    private val statementParserRecursive = StatementParserRecursive(
        tokenTypeAsserter,
        expressionParser,
        variableDeclarationListParser,
        returnStatementParser,
        expressionStatementParser
    )

    private val recursiveFunctionDeclarationParser = FunctionDeclarationParser(
        tokenTypeAsserter,
        statementParserRecursive
    )


    private val recursiveDeclarationStatementParser = DeclarationStatementParser(
        recursiveFunctionDeclarationParser,
        variableDeclarationListParser
    )


    val recursiveParser: IParser = Parser(recursiveDeclarationStatementParser)
}
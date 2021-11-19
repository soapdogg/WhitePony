package compiler.tokenizer

import compiler.core.TokenType
import compiler.core.TokenTypeRegexPattern
import compiler.core.constants.TokenizerConstants
import compiler.tokenizer.impl.MatchFinder
import compiler.tokenizer.impl.Tokenizer
import compiler.tokenizer.impl.WordTokenizer

enum class TokenizerSingleton {
    INSTANCE;

    private val binaryAssignOperatorRegexPattern = Regex("^((&|/|${TokenizerConstants.LEFT_SHIFT_OPERATOR}|-|%|\\*|\\||\\+|${compiler.core.constants.TokenizerConstants.RIGHT_SHIFT_OPERATOR}|\\^)=)")
    private val binaryAssignOperatorTokenTypeRegexPattern = TokenTypeRegexPattern(
        binaryAssignOperatorRegexPattern,
        TokenType.BINARY_ASSIGN_OP
    )

    private val binaryAndOperatorRegexPattern = Regex("^&&")
    private val binaryAndOperatorTokenTypeRegexPattern = TokenTypeRegexPattern(
        binaryAndOperatorRegexPattern,
        TokenType.BINARY_AND
    )

    private val binaryOrOperatorRegexPattern = Regex("^(\\|\\|)")
    private val binaryOrOperatorTokenTypeRegexPattern = TokenTypeRegexPattern(
        binaryOrOperatorRegexPattern,
        TokenType.BINARY_OR,
    )

    private val binaryOperatorRegexPattern = Regex("^(/|<<|%|>>|&|\\||\\^|\\*)")
    private val binaryOperatorTokenTypeRegexPattern = TokenTypeRegexPattern(
        binaryOperatorRegexPattern,
        TokenType.BINARY_OPERATOR,
    )

    private val binaryRelationalOperatorRegexPattern = Regex("^(={2}|>=|>|<=|<|!=)")
    private val binaryRelationalOperatorTokenTypeRegexPattern = TokenTypeRegexPattern(
        binaryRelationalOperatorRegexPattern,
        TokenType.BINARY_RELATIONAL_OPERATOR,
    )

    private val unaryNotOperatorRegexPattern = Regex("^!")
    private val unaryNotOperatorTokenTypeRegexPattern = TokenTypeRegexPattern(
        unaryNotOperatorRegexPattern,
        TokenType.UNARY_NOT
    )

    private val binaryAssignRegexPattern = Regex("^=")
    private val binaryAssignTokenTypeRegexPattern = TokenTypeRegexPattern(
        binaryAssignRegexPattern,
        TokenType.BINARY_ASSIGN
    )

    private val typeRegexPattern = Regex("\\b(double|int)\\b")
    private val typeTokenTypeRegexPattern = TokenTypeRegexPattern(
        typeRegexPattern,
        TokenType.TYPE,
    )

    private val doRegexPattern = Regex("\\bdo\\b")
    private val doTokenTypeRegexPattern = TokenTypeRegexPattern(
        doRegexPattern,
        TokenType.DO
    )

    private val elseRegexPattern = Regex("\\belse\\b")
    private val elseTokenTypeRegexPattern = TokenTypeRegexPattern(
        elseRegexPattern,
        TokenType.ELSE
    )

    private val forRegexPattern = Regex("\\bfor\\b")
    private val forTokenTypeRegexPattern = TokenTypeRegexPattern(
        forRegexPattern,
        TokenType.FOR
    )

    private val ifRegexPattern = Regex("\\bif\\b")
    private val ifTokenTypeRegexPattern = TokenTypeRegexPattern(
        ifRegexPattern,
        TokenType.IF
    )

    private val returnRegexPattern = Regex("\\breturn\\b")
    private val returnTokenTypeRegexPattern = TokenTypeRegexPattern(
        returnRegexPattern,
        TokenType.RETURN
    )

    private val whileRegexPattern = Regex("\\bwhile\\b")
    private val whileTokenTypeRegexPattern = TokenTypeRegexPattern(
        whileRegexPattern,
        TokenType.WHILE
    )

    private val preOrPostOperatorRegexPattern = Regex("^(\\+{2}|-{2})")
    private val preOrPostOperatorTokenTypeRegexPattern = TokenTypeRegexPattern(
        preOrPostOperatorRegexPattern,
        TokenType.PRE_POST,
    )

    private val plusOrMinusRegexPattern = Regex("^(\\+|-)")
    private val plusOrMinusTokenTypeRegexPattern = TokenTypeRegexPattern(
        plusOrMinusRegexPattern,
        TokenType.PLUS_MINUS
    )

    private val bitNegationRegexPattern = Regex("^~")
    private val bitNegationTokenTypeRegexPattern = TokenTypeRegexPattern(
        bitNegationRegexPattern,
        TokenType.BIT_NEGATION,
    )

    private val leftBraceRegexPattern = Regex("^\\{")
    private val leftBraceTokenTypeRegexPattern = TokenTypeRegexPattern(
        leftBraceRegexPattern,
        TokenType.LEFT_BRACE
    )

    private val leftBracketRegexPattern = Regex("^\\[")
    private val leftBracketTokenTypeRegexPattern = TokenTypeRegexPattern(
        leftBracketRegexPattern,
        TokenType.LEFT_BRACKET
    )

    private val leftParenthesesRegexPattern = Regex("^\\(")
    private val leftParenthesesTokenTypeRegexPattern = TokenTypeRegexPattern(
        leftParenthesesRegexPattern,
        TokenType.LEFT_PARENTHESES,
    )

    private val rightBraceRegexPattern = Regex("^}")
    private val rightBraceTokenTypeRegexPattern = TokenTypeRegexPattern(
        rightBraceRegexPattern,
        TokenType.RIGHT_BRACE
    )

    private val rightBracketRegexPattern = Regex("^]")
    private val rightBracketTokenTypeRegexPattern = TokenTypeRegexPattern(
        rightBracketRegexPattern,
        TokenType.RIGHT_BRACKET
    )

    private val rightParenthesesRegexPattern = Regex("^\\)")
    private val rightParenthesesTokenTypeRegexPattern = TokenTypeRegexPattern(
        rightParenthesesRegexPattern,
        TokenType.RIGHT_PARENTHESES
    )

    private val semicolonRegexPattern = Regex("^;")
    private val semicolonTokenTypeRegexPattern = TokenTypeRegexPattern(
        semicolonRegexPattern,
        TokenType.SEMICOLON
    )

    private val commaRegexPattern = Regex("^,")
    private val commaTokenTypeRegexPattern = TokenTypeRegexPattern(
        commaRegexPattern,
        TokenType.COMMA
    )

    private val floatingPointRegexPattern = Regex("^(\\d*\\.\\d+)")
    private val floatingPointTokenTypeRegexPattern = TokenTypeRegexPattern(
        floatingPointRegexPattern,
        TokenType.FLOATING_POINT
    )

    private val identifierRegexPattern = Regex("^([_a-zA-Z]\\w*)")
    private val identifierTokenTypeRegexPattern = TokenTypeRegexPattern(
        identifierRegexPattern,
        TokenType.IDENTIFIER
    )

    private val integerConstantRegexPattern = Regex("^([1-9]\\d*|0)")
    private val integerConstantTokenTypeRegexPattern = TokenTypeRegexPattern(
        integerConstantRegexPattern,
        TokenType.INTEGER
    )


    private val tokenTypeRegexPatterns = listOf(
        binaryAssignOperatorTokenTypeRegexPattern,
        binaryAndOperatorTokenTypeRegexPattern,
        binaryOrOperatorTokenTypeRegexPattern,
        binaryOperatorTokenTypeRegexPattern,
        binaryRelationalOperatorTokenTypeRegexPattern,
        unaryNotOperatorTokenTypeRegexPattern,
        binaryAssignTokenTypeRegexPattern,
        typeTokenTypeRegexPattern,
        doTokenTypeRegexPattern,
        elseTokenTypeRegexPattern,
        forTokenTypeRegexPattern,
        ifTokenTypeRegexPattern,
        returnTokenTypeRegexPattern,
        whileTokenTypeRegexPattern,
        preOrPostOperatorTokenTypeRegexPattern,
        plusOrMinusTokenTypeRegexPattern,
        bitNegationTokenTypeRegexPattern,
        leftBraceTokenTypeRegexPattern,
        leftBracketTokenTypeRegexPattern,
        leftParenthesesTokenTypeRegexPattern,
        rightBraceTokenTypeRegexPattern,
        rightBracketTokenTypeRegexPattern,
        rightParenthesesTokenTypeRegexPattern,
        semicolonTokenTypeRegexPattern,
        commaTokenTypeRegexPattern,
        floatingPointTokenTypeRegexPattern,
        identifierTokenTypeRegexPattern,
        integerConstantTokenTypeRegexPattern
    )

    private val matchFinder = MatchFinder(
        tokenTypeRegexPatterns
    )

    private val wordTokenizer = WordTokenizer(
        matchFinder
    )

    val tokenizer: ITokenizer = Tokenizer(wordTokenizer)
}
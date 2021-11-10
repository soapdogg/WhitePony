package compiler.core

data class TokenTypeRegexPattern(
    val regex: Regex,
    val type: TokenType,
)

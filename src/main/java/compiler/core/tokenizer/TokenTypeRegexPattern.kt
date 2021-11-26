package compiler.core.tokenizer

data class TokenTypeRegexPattern(
    val regex: Regex,
    val type: TokenType,
)

/// Token types recognized by the lexer.
///
/// In Rust, a single enum replaces the Kotlin `TokenType` enum class.
/// The `#[derive]` attributes give us equality, cloning, and debug printing for free.
#[derive(Debug, Clone, PartialEq, Eq)]
pub enum TokenType {
    BinaryAnd,
    BinaryAssign,
    BinaryAssignOp,
    BinaryOperator,
    BinaryOr,
    BinaryRelationalOperator,
    BitNegation,
    Comma,
    Do,
    Else,
    FloatingPoint,
    For,
    If,
    Identifier,
    Integer,
    LeftBrace,
    LeftBracket,
    LeftParentheses,
    PlusMinus,
    PrePost,
    Return,
    RightBrace,
    RightBracket,
    RightParentheses,
    Semicolon,
    Type,
    UnaryNot,
    While,
}

/// A single token: its text value and its type.
///
/// Replaces the Kotlin `Token` data class. Rust's `#[derive(Clone)]`
/// lets us copy tokens cheaply without needing to worry about reference
/// semantics the way we would in Kotlin/Java.
#[derive(Debug, Clone, PartialEq, Eq)]
pub struct Token {
    pub value: String,
    pub token_type: TokenType,
}

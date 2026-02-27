use regex::Regex;
use crate::token::{Token, TokenType};

/// A pattern to try when matching the next token.
struct TokenPattern {
    regex: Regex,
    token_type: TokenType,
}

/// The lexer (tokenizer).
///
/// In the Kotlin version, this was spread across `Tokenizer`, `WordTokenizer`,
/// `MatchFinder`, `TokenizerSingleton`, and ~30 regex pattern fields.
///
/// In Rust, we can express the same logic in a single struct with a Vec of
/// patterns. The `new()` constructor replaces the entire `TokenizerSingleton`
/// enum wiring.
pub struct Lexer {
    patterns: Vec<TokenPattern>,
}

impl Lexer {
    pub fn new() -> Self {
        // Order matters: longer/more-specific patterns must come first,
        // just like in the Kotlin TokenizerSingleton's ordered list.
        let patterns = vec![
            // Compound assignment operators (e.g. +=, <<=)
            (r"^((&|/|<<|-|%|\*|\||\+|>>|\^)=)", TokenType::BinaryAssignOp),
            // Logical AND / OR
            (r"^&&", TokenType::BinaryAnd),
            (r"^(\|\|)", TokenType::BinaryOr),
            // Binary operators (arithmetic, bitwise, shift)
            (r"^(/|<<|%|>>|&|\||\^|\*)", TokenType::BinaryOperator),
            // Relational operators
            (r"^(==|>=|>|<=|<|!=)", TokenType::BinaryRelationalOperator),
            // Unary NOT (logical)
            (r"^!", TokenType::UnaryNot),
            // Plain assignment
            (r"^=", TokenType::BinaryAssign),
            // Keywords (must come before identifier)
            (r"\b(double|int)\b", TokenType::Type),
            (r"\bdo\b", TokenType::Do),
            (r"\belse\b", TokenType::Else),
            (r"\bfor\b", TokenType::For),
            (r"\bif\b", TokenType::If),
            (r"\breturn\b", TokenType::Return),
            (r"\bwhile\b", TokenType::While),
            // Pre/post increment/decrement
            (r"^(\+{2}|-{2})", TokenType::PrePost),
            // Plus/minus (single)
            (r"^(\+|-)", TokenType::PlusMinus),
            // Bitwise negation
            (r"^~", TokenType::BitNegation),
            // Delimiters
            (r"^\{", TokenType::LeftBrace),
            (r"^\[", TokenType::LeftBracket),
            (r"^\(", TokenType::LeftParentheses),
            (r"^\}", TokenType::RightBrace),
            (r"^\]", TokenType::RightBracket),
            (r"^\)", TokenType::RightParentheses),
            (r"^;", TokenType::Semicolon),
            (r"^,", TokenType::Comma),
            // Literals
            (r"^(\d*\.\d+)", TokenType::FloatingPoint),
            (r"^([_a-zA-Z]\w*)", TokenType::Identifier),
            (r"^([1-9]\d*|0)", TokenType::Integer),
        ];

        Lexer {
            patterns: patterns
                .into_iter()
                .map(|(pat, tt)| TokenPattern {
                    regex: Regex::new(pat).unwrap(),
                    token_type: tt,
                })
                .collect(),
        }
    }

    /// Tokenize an entire program string into a flat list of tokens.
    ///
    /// This replaces `Tokenizer.tokenize` + `WordTokenizer.tokenizeWord` +
    /// `MatchFinder.findMatch` from the Kotlin version -- three classes
    /// collapsed into one method.
    pub fn tokenize(&self, input: &str) -> Vec<Token> {
        let mut tokens = Vec::new();
        let words: Vec<&str> = input.split_whitespace().collect();

        for word in words {
            let mut remaining = word;
            while !remaining.is_empty() {
                let mut matched = false;
                for pattern in &self.patterns {
                    if let Some(m) = pattern.regex.find(remaining) {
                        tokens.push(Token {
                            value: m.as_str().to_string(),
                            token_type: pattern.token_type.clone(),
                        });
                        remaining = &remaining[m.end()..];
                        matched = true;
                        break;
                    }
                }
                if !matched {
                    panic!("No matching token type found for: {}", remaining);
                }
            }
        }

        tokens
    }
}

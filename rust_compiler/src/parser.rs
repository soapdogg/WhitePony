use crate::ast::*;
use crate::token::{Token, TokenType};

/// The parser.
///
/// In the Kotlin version, this was spread across ~50 classes:
///   - Parser, DeclarationStatementParser, FunctionDeclarationParser
///   - ShiftReduceExpressionParser, Shifter, Reducer, NodeReducer, OperatorReducer
///   - StatementParserOrchestrator and 15 statement parser classes
///   - Plus ~20 interface files
///
/// In Rust, all of this fits in a single module with ~400 lines,
/// because pattern matching on enums replaces the class hierarchy and
/// dispatch maps. There are no interfaces, no singletons, no DI wiring.

/// Items on the shift-reduce stack: either a parsed expression node or an operator string.
enum StackItem {
    Node(Expression),
    Operator(String),
}

/// Operator precedence table.
/// Lower number = higher precedence (binds tighter).
fn precedence(op: &str) -> u32 {
    match op {
        "[" => 0,
        "++" | "--" | "!" | "~" => 1,
        "*" | "/" | "%" => 2,
        "+" | "-" => 3,
        "<<" | ">>" => 4,
        "<" | "<=" | ">" | ">=" => 5,
        "==" | "!=" => 6,
        "&" => 7,
        "^" => 8,
        "|" => 9,
        "&&" => 10,
        "||" => 11,
        "=" | "<<=" | ">>=" | "&=" | "/=" | "-=" | "%=" | "*=" | "|=" | "+=" | "^=" => 12,
        _ => 99,
    }
}

fn is_assign_operator(op: &str) -> bool {
    matches!(
        op,
        "=" | "<<=" | ">>=" | "&=" | "/=" | "-=" | "%=" | "*=" | "|=" | "+=" | "^="
    )
}

fn is_expression_token(tt: &TokenType) -> bool {
    matches!(
        tt,
        TokenType::FloatingPoint
            | TokenType::Integer
            | TokenType::Identifier
            | TokenType::PlusMinus
            | TokenType::PrePost
            | TokenType::BinaryOperator
            | TokenType::BinaryRelationalOperator
            | TokenType::BinaryAnd
            | TokenType::BinaryOr
            | TokenType::BinaryAssign
            | TokenType::BinaryAssignOp
            | TokenType::UnaryNot
            | TokenType::BitNegation
            | TokenType::LeftParentheses
            | TokenType::RightParentheses
            | TokenType::LeftBracket
            | TokenType::RightBracket
    )
}

/// Reduce the top of the stack by combining a binary operator with two operands.
fn reduce_binary(stack: &mut Vec<StackItem>, operator: String) {
    // Pop right operand, then left operand
    let right = match stack.pop().unwrap() {
        StackItem::Node(n) => n,
        _ => panic!("Expected node on stack for right operand"),
    };
    let left = match stack.pop().unwrap() {
        StackItem::Node(n) => n,
        _ => panic!("Expected node on stack for left operand"),
    };

    let node = match operator.as_str() {
        "&&" => Expression::BinaryAndOp {
            left: Box::new(left),
            right: Box::new(right),
        },
        "||" => Expression::BinaryOrOp {
            left: Box::new(left),
            right: Box::new(right),
        },
        "=" => Expression::BinaryAssign {
            left: Box::new(left),
            right: Box::new(right),
        },
        "<" | "<=" | ">" | ">=" | "==" | "!=" => Expression::BinaryRelationalOp {
            left: Box::new(left),
            operator,
            right: Box::new(right),
        },
        op if is_assign_operator(op) => Expression::BinaryAssignOp {
            left: Box::new(left),
            operator: strip_assign_op(&operator),
            right: Box::new(right),
        },
        _ => Expression::BinaryOp {
            left: Box::new(left),
            operator,
            right: Box::new(right),
        },
    };
    stack.push(StackItem::Node(node));
}

/// For compound assignments like "+=", extract the base operator "+".
fn strip_assign_op(op: &str) -> String {
    op[..op.len() - 1].to_string()
}

/// Reduce operators on the stack while they have higher or equal precedence
/// to the given look-ahead operator.
fn reduce_while_higher_precedence(
    stack: &mut Vec<StackItem>,
    look_ahead_prec: u32,
    look_ahead_is_assign: bool,
) {
    loop {
        // Find the topmost operator on the stack
        let top_op_idx = find_top_operator(stack);
        if top_op_idx.is_none() {
            break;
        }
        let top_op_idx = top_op_idx.unwrap();
        let top_op = match &stack[top_op_idx] {
            StackItem::Operator(op) => op.clone(),
            _ => break,
        };

        let top_prec = precedence(&top_op);
        let top_is_assign = is_assign_operator(&top_op);

        // For right-associative operators (assignments), don't reduce same precedence
        let should_reduce = if look_ahead_is_assign || top_is_assign {
            top_prec < look_ahead_prec
        } else {
            top_prec <= look_ahead_prec
        };

        if !should_reduce {
            break;
        }

        // Remove the operator from the stack and reduce
        stack.remove(top_op_idx);
        reduce_binary(stack, top_op);
    }
}

fn find_top_operator(stack: &[StackItem]) -> Option<usize> {
    for i in (0..stack.len()).rev() {
        if let StackItem::Operator(_) = &stack[i] {
            return Some(i);
        }
    }
    None
}

pub struct Parser;

impl Parser {
    pub fn new() -> Self {
        Parser
    }

    /// Parse a token stream into a Program AST.
    pub fn parse(&self, tokens: &[Token]) -> Program {
        let mut pos = 0;
        let mut declarations = Vec::new();

        while pos < tokens.len() {
            let (decl, new_pos) = self.parse_declaration(tokens, pos);
            declarations.push(decl);
            pos = new_pos;
        }

        Program { declarations }
    }

    /// Parse a top-level declaration: function or global variable list.
    fn parse_declaration(&self, tokens: &[Token], pos: usize) -> (Declaration, usize) {
        // Look ahead: type identifier ( => function, otherwise variable list
        if pos + 2 < tokens.len() && tokens[pos + 2].token_type == TokenType::LeftParentheses {
            self.parse_function(tokens, pos)
        } else {
            let (type_name, new_pos) = self.expect(tokens, pos, TokenType::Type);
            let (decls, final_pos) = self.parse_variable_declaration_list(tokens, new_pos, &type_name);
            (
                Declaration::VariableDeclarationList {
                    type_name,
                    declarations: decls,
                },
                final_pos,
            )
        }
    }

    /// Parse a function declaration: type name() { body }
    fn parse_function(&self, tokens: &[Token], pos: usize) -> (Declaration, usize) {
        let (type_name, pos) = self.expect(tokens, pos, TokenType::Type);
        let (name, pos) = self.expect(tokens, pos, TokenType::Identifier);
        let (_, pos) = self.expect(tokens, pos, TokenType::LeftParentheses);
        let (_, pos) = self.expect(tokens, pos, TokenType::RightParentheses);
        let (body, pos) = self.parse_block(tokens, pos);
        (
            Declaration::Function {
                return_type: type_name,
                name,
                body,
            },
            pos,
        )
    }

    /// Parse a { ... } block containing statements.
    fn parse_block(&self, tokens: &[Token], pos: usize) -> (Statement, usize) {
        let (_, mut pos) = self.expect(tokens, pos, TokenType::LeftBrace);
        let mut statements = Vec::new();

        while pos < tokens.len() && tokens[pos].token_type != TokenType::RightBrace {
            let (stmt, new_pos) = self.parse_statement(tokens, pos);
            statements.push(stmt);
            pos = new_pos;
        }

        let (_, pos) = self.expect(tokens, pos, TokenType::RightBrace);
        (Statement::Block(statements), pos)
    }

    /// Parse a single statement (dispatched by the leading token type).
    fn parse_statement(&self, tokens: &[Token], pos: usize) -> (Statement, usize) {
        match tokens[pos].token_type {
            TokenType::LeftBrace => self.parse_block(tokens, pos),
            TokenType::Type => {
                let (type_name, new_pos) = self.expect(tokens, pos, TokenType::Type);
                let (decls, final_pos) =
                    self.parse_variable_declaration_list(tokens, new_pos, &type_name);
                (
                    Statement::VariableDeclarationList {
                        type_name,
                        declarations: decls,
                    },
                    final_pos,
                )
            }
            TokenType::Return => self.parse_return(tokens, pos),
            TokenType::If => self.parse_if(tokens, pos),
            TokenType::While => self.parse_while(tokens, pos),
            TokenType::Do => self.parse_do_while(tokens, pos),
            TokenType::For => self.parse_for(tokens, pos),
            _ => self.parse_expression_statement(tokens, pos),
        }
    }

    fn parse_return(&self, tokens: &[Token], pos: usize) -> (Statement, usize) {
        let (_, pos) = self.expect(tokens, pos, TokenType::Return);
        let (expr, pos) = self.parse_expression(tokens, pos);
        let (_, pos) = self.expect(tokens, pos, TokenType::Semicolon);
        (Statement::Return(expr), pos)
    }

    fn parse_if(&self, tokens: &[Token], pos: usize) -> (Statement, usize) {
        let (_, pos) = self.expect(tokens, pos, TokenType::If);
        let (_, pos) = self.expect(tokens, pos, TokenType::LeftParentheses);
        let (condition, pos) = self.parse_expression(tokens, pos);
        let (_, pos) = self.expect(tokens, pos, TokenType::RightParentheses);
        let (then_body, pos) = self.parse_statement(tokens, pos);

        let (else_body, pos) = if pos < tokens.len() && tokens[pos].token_type == TokenType::Else {
            let (_, pos) = self.expect(tokens, pos, TokenType::Else);
            let (else_stmt, pos) = self.parse_statement(tokens, pos);
            (Some(Box::new(else_stmt)), pos)
        } else {
            (None, pos)
        };

        (
            Statement::If {
                condition,
                then_body: Box::new(then_body),
                else_body,
            },
            pos,
        )
    }

    fn parse_while(&self, tokens: &[Token], pos: usize) -> (Statement, usize) {
        let (_, pos) = self.expect(tokens, pos, TokenType::While);
        let (_, pos) = self.expect(tokens, pos, TokenType::LeftParentheses);
        let (condition, pos) = self.parse_expression(tokens, pos);
        let (_, pos) = self.expect(tokens, pos, TokenType::RightParentheses);
        let (body, pos) = self.parse_statement(tokens, pos);
        (
            Statement::While {
                condition,
                body: Box::new(body),
            },
            pos,
        )
    }

    fn parse_do_while(&self, tokens: &[Token], pos: usize) -> (Statement, usize) {
        let (_, pos) = self.expect(tokens, pos, TokenType::Do);
        let (body, pos) = self.parse_statement(tokens, pos);
        let (_, pos) = self.expect(tokens, pos, TokenType::While);
        let (_, pos) = self.expect(tokens, pos, TokenType::LeftParentheses);
        let (condition, pos) = self.parse_expression(tokens, pos);
        let (_, pos) = self.expect(tokens, pos, TokenType::RightParentheses);
        let (_, pos) = self.expect(tokens, pos, TokenType::Semicolon);
        (
            Statement::DoWhile {
                body: Box::new(body),
                condition,
            },
            pos,
        )
    }

    fn parse_for(&self, tokens: &[Token], pos: usize) -> (Statement, usize) {
        let (_, pos) = self.expect(tokens, pos, TokenType::For);
        let (_, pos) = self.expect(tokens, pos, TokenType::LeftParentheses);
        let (init, pos) = self.parse_expression(tokens, pos);
        let (_, pos) = self.expect(tokens, pos, TokenType::Semicolon);
        let (condition, pos) = self.parse_expression(tokens, pos);
        let (_, pos) = self.expect(tokens, pos, TokenType::Semicolon);
        let (update, pos) = self.parse_expression(tokens, pos);
        let (_, pos) = self.expect(tokens, pos, TokenType::RightParentheses);
        let (body, pos) = self.parse_statement(tokens, pos);
        (
            Statement::For {
                init,
                condition,
                update,
                body: Box::new(body),
            },
            pos,
        )
    }

    fn parse_expression_statement(&self, tokens: &[Token], pos: usize) -> (Statement, usize) {
        let (expr, pos) = self.parse_expression(tokens, pos);
        let (_, pos) = self.expect(tokens, pos, TokenType::Semicolon);
        (Statement::ExpressionStatement(expr), pos)
    }

    /// Parse a variable declaration list: ident[, ident]* ;
    /// (The type has already been consumed.)
    fn parse_variable_declaration_list(
        &self,
        tokens: &[Token],
        pos: usize,
        _type_name: &str,
    ) -> (Vec<VariableDeclaration>, usize) {
        let mut decls = Vec::new();
        let (first_decl, mut pos) = self.parse_single_var_decl(tokens, pos);
        decls.push(first_decl);

        while pos < tokens.len() && tokens[pos].token_type == TokenType::Comma {
            pos += 1; // consume comma
            let (decl, new_pos) = self.parse_single_var_decl(tokens, pos);
            decls.push(decl);
            pos = new_pos;
        }

        let (_, pos) = self.expect(tokens, pos, TokenType::Semicolon);
        (decls, pos)
    }

    /// Parse a single variable declaration: name [ [size] ] [ = expr ]
    fn parse_single_var_decl(
        &self,
        tokens: &[Token],
        pos: usize,
    ) -> (VariableDeclaration, usize) {
        let (name, mut pos) = self.expect(tokens, pos, TokenType::Identifier);

        let array_size = if pos < tokens.len() && tokens[pos].token_type == TokenType::LeftBracket {
            pos += 1; // consume [
            let (size_expr, new_pos) = self.parse_expression(tokens, pos);
            pos = new_pos;
            let (_, new_pos) = self.expect(tokens, pos, TokenType::RightBracket);
            pos = new_pos;
            Some(Box::new(size_expr))
        } else {
            None
        };

        let initializer =
            if pos < tokens.len() && tokens[pos].token_type == TokenType::BinaryAssign {
                pos += 1; // consume =
                let (init_expr, new_pos) = self.parse_expression(tokens, pos);
                pos = new_pos;
                Some(Box::new(init_expr))
            } else {
                None
            };

        (
            VariableDeclaration {
                name,
                array_size,
                initializer,
            },
            pos,
        )
    }

    /// Shift-reduce expression parser.
    ///
    /// This is the heart of expression parsing. It replaces the Kotlin
    /// `ShiftReduceExpressionParser`, `Shifter`, `Reducer`, `NodeReducer`,
    /// `OperatorReducer`, and all the reducer classes (~25 classes total).
    ///
    /// The algorithm:
    /// 1. Shift: push the next token as a node or operator
    /// 2. Reduce: combine operators and operands on the stack by precedence
    /// 3. Repeat until we reach a non-expression token
    fn parse_expression(&self, tokens: &[Token], start: usize) -> (Expression, usize) {
        let mut stack: Vec<StackItem> = Vec::new();
        let mut pos = start;
        let mut paren_depth: i32 = 0;
        let mut bracket_depth: i32 = 0;
        let mut has_seen_paren = false;

        loop {
            // Shift
            pos = self.shift(tokens, pos, &mut stack);

            // Check look-ahead
            if pos >= tokens.len() {
                break;
            }

            let look_ahead = &tokens[pos];
            let look_value = look_ahead.value.as_str();

            // Reduce based on look-ahead
            match look_value {
                "(" => {
                    has_seen_paren = true;
                    paren_depth += 1;
                    stack.push(StackItem::Operator("(".to_string()));
                    pos += 1;
                }
                ")" => {
                    paren_depth -= 1;
                    pos += 1;
                    // Reduce everything back to the matching (
                    self.reduce_to_paren(&mut stack);
                }
                "[" => {
                    bracket_depth += 1;
                    stack.push(StackItem::Operator("[".to_string()));
                    pos += 1;
                }
                "]" => {
                    bracket_depth -= 1;
                    pos += 1;
                    // Reduce the array index expression
                    self.reduce_to_bracket(&mut stack);
                }
                "++" | "--" if self.is_postfix_context(&stack) => {
                    // Postfix operator
                    let op = look_value.to_string();
                    pos += 1;
                    let operand = match stack.pop().unwrap() {
                        StackItem::Node(n) => n,
                        _ => panic!("Expected node for postfix operator"),
                    };
                    stack.push(StackItem::Node(Expression::UnaryPost {
                        operand: Box::new(operand),
                        operator: op,
                    }));
                    // Reduce after postfix
                    self.reduce_after_postfix(&mut stack);
                }
                _ => {
                    // Is this an operator we should continue with?
                    let should_break =
                        self.reduce_node(&mut stack, look_value, paren_depth, bracket_depth, !has_seen_paren);

                    if should_break {
                        break;
                    }

                    // Check if we should continue parsing
                    if !is_expression_token(&look_ahead.token_type) {
                        break;
                    }
                    if !has_seen_paren && paren_depth == 0 && !is_expression_token(&look_ahead.token_type) {
                        break;
                    }
                }
            }
        }

        // Final reduction: reduce everything remaining on the stack
        self.reduce_all(&mut stack);

        let result = match stack.pop().unwrap() {
            StackItem::Node(n) => n,
            _ => panic!("Expected final expression node on stack"),
        };

        (result, pos)
    }

    /// Shift: push the next token onto the stack as a node or operator.
    fn shift(&self, tokens: &[Token], pos: usize, stack: &mut Vec<StackItem>) -> usize {
        if pos >= tokens.len() {
            return pos;
        }

        let token = &tokens[pos];
        match token.token_type {
            TokenType::Integer | TokenType::FloatingPoint => {
                stack.push(StackItem::Node(Expression::Constant(
                    token.value.clone(),
                )));
                pos + 1
            }
            TokenType::Identifier => {
                stack.push(StackItem::Node(Expression::Variable(
                    token.value.clone(),
                )));
                pos + 1
            }
            TokenType::PlusMinus
            | TokenType::BinaryOperator
            | TokenType::BinaryRelationalOperator
            | TokenType::BinaryAnd
            | TokenType::BinaryOr
            | TokenType::BinaryAssign
            | TokenType::BinaryAssignOp => {
                stack.push(StackItem::Operator(token.value.clone()));
                pos + 1
            }
            TokenType::PrePost => {
                // Prefix: ++x or --x
                stack.push(StackItem::Operator(token.value.clone()));
                pos + 1
            }
            TokenType::UnaryNot => {
                stack.push(StackItem::Operator("!".to_string()));
                pos + 1
            }
            TokenType::BitNegation => {
                stack.push(StackItem::Operator("~".to_string()));
                pos + 1
            }
            _ => pos,
        }
    }

    /// Check if the top of the stack context suggests a postfix operator.
    fn is_postfix_context(&self, stack: &[StackItem]) -> bool {
        if let Some(StackItem::Node(_)) = stack.last() {
            return true;
        }
        false
    }

    /// Reduce node: handle the look-ahead operator by reducing higher-precedence
    /// operators already on the stack.
    fn reduce_node(
        &self,
        stack: &mut Vec<StackItem>,
        look_ahead: &str,
        paren_depth: i32,
        bracket_depth: i32,
        has_not_seen_paren: bool,
    ) -> bool {
        let look_prec = precedence(look_ahead);
        let look_is_assign = is_assign_operator(look_ahead);

        // Determine if this operator is part of our expression
        if look_prec == 99 {
            // Not an operator we recognize in expression context
            if paren_depth == 0 && bracket_depth == 0 {
                return true; // break
            }
        }

        // Reduce higher-precedence operators on the stack
        reduce_while_higher_precedence(stack, look_prec, look_is_assign);

        false
    }

    /// Reduce everything back to a matching ( and wrap in Inner.
    fn reduce_to_paren(&self, stack: &mut Vec<StackItem>) {
        // Reduce all operators above the (
        loop {
            let top_op_idx = find_top_operator(stack);
            match top_op_idx {
                Some(idx) => {
                    let is_paren = matches!(&stack[idx], StackItem::Operator(op) if op == "(");
                    if is_paren {
                        stack.remove(idx);
                        // Wrap the node in Inner
                        if let Some(StackItem::Node(n)) = stack.pop() {
                            stack.push(StackItem::Node(Expression::Inner(Box::new(n))));
                        }
                        break;
                    }
                    let op = match stack.remove(idx) {
                        StackItem::Operator(op) => op,
                        _ => unreachable!(),
                    };
                    reduce_binary(stack, op);
                }
                None => break,
            }
        }
    }

    /// Reduce back to a matching [ and create array access.
    fn reduce_to_bracket(&self, stack: &mut Vec<StackItem>) {
        loop {
            let top_op_idx = find_top_operator(stack);
            match top_op_idx {
                Some(idx) => {
                    let is_bracket = matches!(&stack[idx], StackItem::Operator(op) if op == "[");
                    if is_bracket {
                        stack.remove(idx);
                        // index is on top, array name below
                        let index = match stack.pop().unwrap() {
                            StackItem::Node(n) => n,
                            _ => panic!("Expected index expression"),
                        };
                        let array = match stack.pop().unwrap() {
                            StackItem::Node(n) => n,
                            _ => panic!("Expected array name"),
                        };
                        stack.push(StackItem::Node(Expression::BinaryArrayAccess {
                            array: Box::new(array),
                            index: Box::new(index),
                        }));
                        break;
                    }
                    let op = match stack.remove(idx) {
                        StackItem::Operator(op) => op,
                        _ => unreachable!(),
                    };
                    reduce_binary(stack, op);
                }
                None => break,
            }
        }
    }

    /// Reduce after a postfix operator (++/--).
    fn reduce_after_postfix(&self, stack: &mut Vec<StackItem>) {
        // Check if there's a higher-precedence operator to reduce
        reduce_while_higher_precedence(stack, precedence("++"), false);
    }

    /// Reduce all remaining operators on the stack to produce a single expression.
    fn reduce_all(&self, stack: &mut Vec<StackItem>) {
        loop {
            let top_op_idx = find_top_operator(stack);
            match top_op_idx {
                Some(idx) => {
                    let op = match &stack[idx] {
                        StackItem::Operator(op) => op.clone(),
                        _ => unreachable!(),
                    };
                    // Handle unary operators (prefix ++ / -- / ! / ~)
                    // If operator is at position idx and there's no node to the left
                    // (or the item to the left is also an operator), it's unary.
                    let is_unary = idx == 0
                        || matches!(&stack[idx - 1], StackItem::Operator(_));

                    if is_unary {
                        stack.remove(idx);
                        match op.as_str() {
                            "++" | "--" => {
                                let operand = match stack.remove(idx) {
                                    StackItem::Node(n) => n,
                                    _ => panic!("Expected operand for unary prefix {}", op),
                                };
                                stack.insert(
                                    idx,
                                    StackItem::Node(Expression::UnaryPre {
                                        operator: op,
                                        operand: Box::new(operand),
                                    }),
                                );
                            }
                            "!" => {
                                let operand = match stack.remove(idx) {
                                    StackItem::Node(n) => n,
                                    _ => panic!("Expected operand for unary !"),
                                };
                                stack.insert(
                                    idx,
                                    StackItem::Node(Expression::UnaryNot {
                                        operand: Box::new(operand),
                                    }),
                                );
                            }
                            "~" => {
                                let operand = match stack.remove(idx) {
                                    StackItem::Node(n) => n,
                                    _ => panic!("Expected operand for unary ~"),
                                };
                                stack.insert(
                                    idx,
                                    StackItem::Node(Expression::Unary {
                                        operator: op,
                                        operand: Box::new(operand),
                                    }),
                                );
                            }
                            "+" | "-" => {
                                // Unary plus/minus: treat as part of a constant
                                let operand = match stack.remove(idx) {
                                    StackItem::Node(n) => n,
                                    _ => panic!("Expected operand for unary {}", op),
                                };
                                stack.insert(
                                    idx,
                                    StackItem::Node(Expression::Unary {
                                        operator: op,
                                        operand: Box::new(operand),
                                    }),
                                );
                            }
                            _ => {
                                // Shouldn't happen for well-formed input
                                panic!("Unexpected unary operator: {}", op);
                            }
                        }
                    } else {
                        stack.remove(idx);
                        reduce_binary(stack, op);
                    }
                }
                None => break,
            }
        }
    }

    /// Assert the next token has the expected type and return its value.
    fn expect(&self, tokens: &[Token], pos: usize, expected: TokenType) -> (String, usize) {
        if pos >= tokens.len() {
            panic!(
                "Unexpected end of tokens, expected {:?} at position {}",
                expected, pos
            );
        }
        let token = &tokens[pos];
        if token.token_type != expected {
            panic!(
                "Expected {:?} but found {:?} ('{}') at position {}",
                expected, token.token_type, token.value, pos
            );
        }
        (token.value.clone(), pos + 1)
    }
}

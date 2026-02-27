/// Parsed AST nodes.
///
/// In the Kotlin version, there were ~30 separate classes/interfaces to
/// represent the parsed AST:
///   - IParsedExpressionNode, IParsedBinaryExpressionNode, IParsedValueExpressionNode, ...
///   - ParsedBinaryOperatorExpressionNode, ParsedConstantExpressionNode, ...
///   - ParsedIfNode, ParsedWhileNode, ParsedForNode, ParsedDoWhileNode, ...
///   - ParsedBasicBlockNode, ParsedProgramRootNode, ParsedFunctionDeclarationNode, ...
///
/// In Rust, a single `enum` with data variants replaces the entire class
/// hierarchy. Pattern matching replaces `is`/`as` casts and the visitor pattern.

/// An expression in the parsed AST.
#[derive(Debug, Clone)]
pub enum Expression {
    Constant(String),           // integer or float literal
    Variable(String),           // identifier
    BinaryOp {
        left: Box<Expression>,
        operator: String,
        right: Box<Expression>,
    },
    BinaryRelationalOp {
        left: Box<Expression>,
        operator: String,
        right: Box<Expression>,
    },
    BinaryAndOp {
        left: Box<Expression>,
        right: Box<Expression>,
    },
    BinaryOrOp {
        left: Box<Expression>,
        right: Box<Expression>,
    },
    BinaryAssign {
        left: Box<Expression>,
        right: Box<Expression>,
    },
    BinaryAssignOp {
        left: Box<Expression>,
        operator: String,
        right: Box<Expression>,
    },
    BinaryArrayAccess {
        array: Box<Expression>,
        index: Box<Expression>,
    },
    UnaryPre {
        operator: String,
        operand: Box<Expression>,
    },
    UnaryPost {
        operand: Box<Expression>,
        operator: String,
    },
    UnaryNot {
        operand: Box<Expression>,
    },
    Unary {
        operator: String,       // ~ (bitwise not)
        operand: Box<Expression>,
    },
    Inner(Box<Expression>),     // parenthesized expression
}

/// A single variable declaration (with optional array size and initializer).
#[derive(Debug, Clone)]
pub struct VariableDeclaration {
    pub name: String,
    pub array_size: Option<Box<Expression>>,
    pub initializer: Option<Box<Expression>>,
}

/// A statement in the parsed AST.
#[derive(Debug, Clone)]
pub enum Statement {
    ExpressionStatement(Expression),
    Return(Expression),
    If {
        condition: Expression,
        then_body: Box<Statement>,
        else_body: Option<Box<Statement>>,
    },
    While {
        condition: Expression,
        body: Box<Statement>,
    },
    DoWhile {
        body: Box<Statement>,
        condition: Expression,
    },
    For {
        init: Expression,
        condition: Expression,
        update: Expression,
        body: Box<Statement>,
    },
    Block(Vec<Statement>),
    VariableDeclarationList {
        type_name: String,
        declarations: Vec<VariableDeclaration>,
    },
}

/// A top-level declaration (function or global variable list).
#[derive(Debug, Clone)]
pub enum Declaration {
    Function {
        return_type: String,
        name: String,
        body: Statement,        // always a Block
    },
    VariableDeclarationList {
        type_name: String,
        declarations: Vec<VariableDeclaration>,
    },
}

/// Root of the parsed program.
#[derive(Debug, Clone)]
pub struct Program {
    pub declarations: Vec<Declaration>,
}

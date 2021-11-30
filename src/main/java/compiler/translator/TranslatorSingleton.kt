package compiler.translator

import compiler.core.nodes.VariableDeclarationListNode
import compiler.core.nodes.parsed.*
import compiler.translator.impl.*
import compiler.translator.impl.StackGenerator
import compiler.translator.impl.DeclarationStatementTranslator
import compiler.translator.impl.ExpressionStatementTranslator
import compiler.translator.impl.FunctionDeclarationTranslator
import compiler.translator.impl.ReturnStatementTranslator
import compiler.translator.impl.Translator

enum class TranslatorSingleton {
    INSTANCE;

    private val variableTypeRecorder = VariableTypeRecorder()

    private val tempGenerator = TempGenerator()
    private val typeDeterminer = TypeDeterminer()
    private val tempDeclarationCodeGenerator = TempDeclarationCodeGenerator()
    private val expressionTranslatorStackPusher = ExpressionTranslatorStackPusher()
    private val arrayCodeGenerator = ArrayCodeGenerator()
    private val assignCodeGenerator = AssignCodeGenerator()
    private val operationCodeGenerator = OperationCodeGenerator()

    private val binaryAssignVariableLValueExpressionTranslator = BinaryAssignVariableLValueExpressionTranslator(
        expressionTranslatorStackPusher,
        assignCodeGenerator
    )
    private val binaryAssignArrayLValueExpressionTranslator = BinaryAssignArrayLValueExpressionTranslator(
        expressionTranslatorStackPusher,
        arrayCodeGenerator,
        assignCodeGenerator
    )
    private val binaryAssignExpressionTranslator = BinaryAssignExpressionTranslator(
        binaryAssignVariableLValueExpressionTranslator,
        binaryAssignArrayLValueExpressionTranslator
    )

    private val binaryOperatorExpressionTranslator = BinaryOperatorExpressionTranslator(
        expressionTranslatorStackPusher,
        tempGenerator,
        typeDeterminer,
        operationCodeGenerator,
        tempDeclarationCodeGenerator
    )

    private val binaryArrayExpressionTranslator = BinaryArrayExpressionTranslator(
        expressionTranslatorStackPusher,
        tempGenerator,
        arrayCodeGenerator,
        tempDeclarationCodeGenerator
    )

    private val unaryExpressionTranslator = UnaryExpressionTranslator(
        expressionTranslatorStackPusher,
        tempGenerator,
        tempDeclarationCodeGenerator
    )

    private val innerExpressionTranslator = InnerExpressionTranslator()
    private val variableExpressionTranslator = VariableExpressionTranslator(
        tempGenerator,
        tempDeclarationCodeGenerator
    )
    private val constantExpressionTranslator = ConstantExpressionTranslator()

    private val expressionTranslatorOrchestrator = ExpressionTranslatorOrchestrator(
        binaryAssignExpressionTranslator,
        binaryOperatorExpressionTranslator,
        binaryArrayExpressionTranslator,
        unaryExpressionTranslator,
        innerExpressionTranslator,
        variableExpressionTranslator,
        constantExpressionTranslator,
        tempGenerator,
        tempDeclarationCodeGenerator,
        expressionTranslatorStackPusher,
        assignCodeGenerator,
        arrayCodeGenerator
    )

    private val labelGenerator = LabelGenerator()
    private val gotoCodeGenerator = GotoCodeGenerator()
    private val conditionalGotoCodeGenerator = ConditionalGotoCodeGenerator(
        operationCodeGenerator,
        gotoCodeGenerator
    )
    private val labelCodeGenerator = LabelCodeGenerator()

    private val booleanExpressionTranslatorStackPusher = BooleanExpressionTranslatorStackPusher()

    private val binaryAndOperatorExpressionTranslator = BinaryAndOperatorExpressionTranslator(
        labelGenerator,
        booleanExpressionTranslatorStackPusher,
        labelCodeGenerator
    )

    private val binaryOrOperatorExpressionTranslator = BinaryOrOperatorExpressionTranslator(
        labelGenerator,
        booleanExpressionTranslatorStackPusher,
        labelCodeGenerator
    )

    private val binaryRelationalOperatorExpressionTranslator = BinaryRelationalOperatorExpressionTranslator(
        expressionTranslatorOrchestrator,
        conditionalGotoCodeGenerator,
        gotoCodeGenerator
    )

    private val unaryNotExpressionNodeTranslator = UnaryNotExpressionTranslator(
        booleanExpressionTranslatorStackPusher
    )
    private val innerBooleanExpressionTranslator = InnerBooleanExpressionTranslator(
        booleanExpressionTranslatorStackPusher
    )

    private val stackGenerator = StackGenerator()

    private val booleanExpressionTranslatorMap = mapOf(
        ParsedBinaryAndOperatorExpressionNode::class.java to binaryAndOperatorExpressionTranslator,
        ParsedBinaryOrOperatorExpressionNode::class.java to binaryOrOperatorExpressionTranslator,
        ParsedBinaryRelationalOperatorExpressionNode::class.java to binaryRelationalOperatorExpressionTranslator,
        ParsedUnaryNotOperatorExpressionNode::class.java to unaryNotExpressionNodeTranslator,
        ParsedInnerExpressionNode::class.java to innerBooleanExpressionTranslator
    )

    private val booleanExpressionTranslatorOrchestrator = BooleanExpressionTranslatorOrchestrator(
        stackGenerator,
        booleanExpressionTranslatorMap
    )
    private val expressionStatementTranslator = ExpressionStatementTranslator(expressionTranslatorOrchestrator)
    private val returnStatementTranslator = ReturnStatementTranslator(expressionStatementTranslator)

    private val basicBlockStatementTranslator = BasicBlockStatementTranslator()
    private val doWhileStatementTranslator = DoWhileStatementTranslator(
        labelGenerator,
        booleanExpressionTranslatorOrchestrator
    )
    private val forStatementTranslator = ForStatementTranslator(
        labelGenerator,
        expressionTranslatorOrchestrator,
        booleanExpressionTranslatorOrchestrator
    )
    private val ifStatementTranslator = IfStatementTranslator(
        labelGenerator,
        booleanExpressionTranslatorOrchestrator
    )
    private val whileStatementTranslator = WhileStatementTranslator(
        labelGenerator,
        booleanExpressionTranslatorOrchestrator
    )
    private val variableDeclarationStatementTranslator = VariableDeclarationStatementTranslator(variableTypeRecorder)
    private val statementTranslatorMap = mapOf(
        ParsedBasicBlockNode::class.java to basicBlockStatementTranslator,
        ParsedDoWhileNode::class.java to doWhileStatementTranslator,
        ParsedExpressionStatementNode::class.java to expressionStatementTranslator,
        ParsedForNode::class.java to forStatementTranslator,
        ParsedIfNode::class.java to ifStatementTranslator,
        ParsedReturnNode::class.java to returnStatementTranslator,
        ParsedWhileNode::class.java to whileStatementTranslator,
        VariableDeclarationListNode::class.java to variableDeclarationStatementTranslator
    )

    private val statementTranslator = StatementTranslatorOrchestrator(
        stackGenerator,
        statementTranslatorMap,
    )

    private val functionDeclarationTranslator = FunctionDeclarationTranslator(
        statementTranslator
    )

    private val declarationStatementTranslator = DeclarationStatementTranslator(
        functionDeclarationTranslator,
        variableTypeRecorder
    )

    val translator: ITranslator = Translator(declarationStatementTranslator)
}
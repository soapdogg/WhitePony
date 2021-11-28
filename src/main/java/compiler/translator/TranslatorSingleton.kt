package compiler.translator

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

    private val expressionTranslator = ExpressionTranslator(
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
        expressionTranslator,
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

    private val translatorMap = mapOf(
        ParsedBinaryAndOperatorExpressionNode::class.java to binaryAndOperatorExpressionTranslator,
        ParsedBinaryOrOperatorExpressionNode::class.java to binaryOrOperatorExpressionTranslator,
        ParsedBinaryRelationalOperatorExpressionNode::class.java to binaryRelationalOperatorExpressionTranslator,
        ParsedUnaryNotOperatorExpressionNode::class.java to unaryNotExpressionNodeTranslator,
        ParsedInnerExpressionNode::class.java to innerBooleanExpressionTranslator
    )

    private val booleanExpressionTranslator = BooleanExpressionTranslator(
        stackGenerator,
        translatorMap
    )
    private val expressionStatementTranslator = ExpressionStatementTranslator(expressionTranslator)
    private val returnStatementTranslator = ReturnStatementTranslator(expressionStatementTranslator)

    private val statementTranslator = StatementTranslator(
        variableTypeRecorder,
        expressionTranslator,
        booleanExpressionTranslator,
        returnStatementTranslator,
        expressionStatementTranslator
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
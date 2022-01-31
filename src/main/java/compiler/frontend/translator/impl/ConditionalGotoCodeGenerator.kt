package compiler.frontend.translator.impl

import compiler.core.constants.PrinterConstants
import compiler.frontend.translator.impl.internal.IConditionalGotoCodeGenerator
import compiler.frontend.translator.impl.internal.IGotoCodeGenerator
import compiler.frontend.translator.impl.internal.IOperationCodeGenerator

internal class ConditionalGotoCodeGenerator(
    private val operationCodeGenerator: IOperationCodeGenerator,
    private val gotoCodeGenerator: IGotoCodeGenerator
): IConditionalGotoCodeGenerator {
    override fun generateConditionalGotoCode(
        leftAddress: String,
        operator: String,
        rightAddress: String,
        trueLabel: String
    ): String {
        val operationCode = operationCodeGenerator.generateOperationCode(leftAddress, operator, rightAddress)
        val gotoTrueLabelCode = gotoCodeGenerator.generateGotoCode(trueLabel)
        return PrinterConstants.IF +
                PrinterConstants.SPACE +
                PrinterConstants.LEFT_PARENTHESES +
                operationCode +
                PrinterConstants.RIGHT_PARENTHESES +
                PrinterConstants.SPACE +
                gotoTrueLabelCode
    }
}
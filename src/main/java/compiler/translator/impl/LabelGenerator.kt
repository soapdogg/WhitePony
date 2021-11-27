package compiler.translator.impl

import compiler.translator.impl.internal.ILabelGenerator

internal class LabelGenerator: ILabelGenerator {
    override fun generateLabel(labelCounter: Int): Pair<String, Int> {
        val label = "_l$labelCounter"
        return Pair(label, labelCounter + 1)
    }
}
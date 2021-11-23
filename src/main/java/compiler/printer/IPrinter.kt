package compiler.printer

import compiler.core.IProgramRootNode

interface IPrinter {
    fun printNode(node: IProgramRootNode): String
}
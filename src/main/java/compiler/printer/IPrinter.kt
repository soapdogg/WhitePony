package compiler.printer

import compiler.core.nodes.IProgramRootNode

interface IPrinter {
    fun printNode(node: IProgramRootNode, appendSemicolon: Boolean): String
}
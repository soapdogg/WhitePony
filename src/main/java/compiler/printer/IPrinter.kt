package compiler.printer

import compiler.core.ParsedProgramRootNode

interface IPrinter {
    fun printParsedNode(node: ParsedProgramRootNode): String
}
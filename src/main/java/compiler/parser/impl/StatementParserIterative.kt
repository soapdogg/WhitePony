package compiler.parser.impl

import compiler.core.*
import compiler.parser.impl.internal.*
import compiler.parser.impl.internal.IExpressionParser
import compiler.parser.impl.internal.IReturnStatementParser
import compiler.parser.impl.internal.IStatementParser
import compiler.parser.impl.internal.ITokenTypeAsserter
import compiler.parser.impl.internal.IVariableDeclarationListParser

internal class StatementParserIterative(
    private val tokenTypeAsserter: ITokenTypeAsserter,
    private val expressionParser: IExpressionParser,
    private val returnStatementParser: IReturnStatementParser,
    private val variableDeclarationListParser: IVariableDeclarationListParser,
    private val expressionStatementParser: IExpressionStatementParser
): IStatementParser {
//    override fun parse(
//        tokens: List<Token>,
//        startingPosition: Int
//    ): Pair<IStatementNode, Int> {
//
//        val programStack = Stack<IParseStackItem>()
//        val stackItem = BlockParseStackItem(listOf())
//        programStack.push(stackItem)
//
//
//        var currentPosition = startingPosition + 1
//        var latestStatement = Stack<IStatementNode>()
//        var shouldExtract = true
//        while(
//            programStack.isNotEmpty()
//        ) {
//            val tokenType = tokens[currentPosition].type
//            when {
//                tokenType == TokenType.DO -> {
//                    val (_, positionAfterDo) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.DO)
//                    currentPosition = positionAfterDo
//                    programStack.push(DoParseStackItem())
//                    continue
//                }
//                tokenType == TokenType.WHILE -> {
//                    val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.WHILE)
//                    val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterWhile, TokenType.LEFT_PARENTHESES)
//                    val (expression, positionAfterExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses, setOf(TokenType.RIGHT_PARENTHESES))
//                    val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.RIGHT_PARENTHESES)
//                    currentPosition = positionAfterRightParentheses
//                    val stackItem = WhileParseStackItem(expression)
//                    programStack.push(stackItem)
//                    continue
//                }
//                tokenType == TokenType.FOR -> {
//                    val (_, positionAfterFor) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.FOR)
//                    val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterFor, TokenType.LEFT_PARENTHESES)
//                    val (initExpression, positionAfterInitExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses, setOf(TokenType.SEMICOLON))
//                    val (_, positionAfterFirstSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterInitExpression, TokenType.SEMICOLON)
//                    val (testExpression, positionAfterTestExpression) = expressionParser.parse(tokens, positionAfterFirstSemi, setOf(TokenType.SEMICOLON))
//                    val (_, positionAfterSecondSemi) = tokenTypeAsserter.assertTokenType(tokens, positionAfterTestExpression, TokenType.SEMICOLON)
//                    val (incrementExpression, positionAfterIncrementExpression) = expressionParser.parse(tokens, positionAfterSecondSemi, setOf(TokenType.RIGHT_PARENTHESES))
//                    val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens,  positionAfterIncrementExpression, TokenType.RIGHT_PARENTHESES)
//                    currentPosition = positionAfterRightParentheses
//                    val stackItem = ForParseStackItem(initExpression, testExpression, incrementExpression)
//                    programStack.push(stackItem)
//                    continue
//                }
//                tokenType == TokenType.IF -> {
//                    val (_, positionAfterIf) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.IF)
//                    val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterIf, TokenType.LEFT_PARENTHESES)
//                    val (booleanExpression, positionAfterBooleanExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses, setOf(TokenType.RIGHT_PARENTHESES))
//                    val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterBooleanExpression, TokenType.RIGHT_PARENTHESES)
//                    currentPosition = positionAfterRightParentheses
//                    val stackItem = IfParseStackItem(booleanExpression)
//                    programStack.push(stackItem)
//                    continue
//                }
//                tokenType == TokenType.LEFT_BRACE -> {
//                    val (_, positionAfterLeftBrace) = tokenTypeAsserter.assertTokenType(
//                        tokens,
//                        currentPosition,
//                        TokenType.LEFT_BRACE
//                    )
//                    val statements = mutableListOf<IStatementNode>()
//                    currentPosition = positionAfterLeftBrace
//                    val stackItem = BlockParseStackItem(statements)
//                    programStack.push(stackItem)
//                    continue
//                }
//            }
//
//            val localStatement = if(shouldExtract){
//                when (tokenType) {
//                    TokenType.RETURN -> {
//                        val(returnStatement, positionAfterReturn) = returnStatementParser.parse(tokens, currentPosition)
//                        currentPosition = positionAfterReturn
//                        returnStatement
//                    }
//                    TokenType.TYPE -> {
//                        val(variableDeclarationListStatement, positionAfterVariableDeclarationList) = variableDeclarationListParser.parse(tokens, currentPosition)
//                        currentPosition = positionAfterVariableDeclarationList
//                        variableDeclarationListStatement
//                    }
//                    TokenType.IDENTIFIER, TokenType.PRE_POST -> {
//                        val(expressionStatement, positionAfterExpressionStatement) = expressionStatementParser.parse(tokens, currentPosition)
//                        currentPosition = positionAfterExpressionStatement
//                        expressionStatement
//                    }
//                    else -> {
//                        latestStatement.pop()
//                    }
//                }
//            } else {
//                latestStatement.pop()
//            }
//
//            val top = programStack.pop()
//
//            when {
//                top.getType() == StatementType.DO_STATEMENT -> {
//                    val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.WHILE)
//                    val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterWhile, TokenType.LEFT_PARENTHESES)
//                    val (expression, positionAfterExpression) = expressionParser.parse(tokens, positionAfterLeftParentheses, setOf(TokenType.RIGHT_PARENTHESES))
//                    val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(tokens, positionAfterExpression, TokenType.RIGHT_PARENTHESES)
//                    val (_, positionAfterSemicolon) = tokenTypeAsserter.assertTokenType(tokens, positionAfterRightParentheses, TokenType.SEMICOLON)
//                    val doStatement = DoWhileNode(
//                        expression,
//                        localStatement
//                    )
//                    latestStatement.push(doStatement)
//                    currentPosition = positionAfterSemicolon
//                }
//                top.getType() == StatementType.WHILE_STATEMENT -> {
//                    val whileStackItem = top as WhileParseStackItem
//                    val whileStatement = WhileNode(whileStackItem.expression, localStatement)
//                    latestStatement.push(whileStatement)
//                }
//                top.getType() == StatementType.FOR_STATEMENT -> {
//                    val forStackItem = top as ForParseStackItem
//                    val forStatement = ForNode(
//                        forStackItem.initExpression,
//                        forStackItem.testExpression,
//                        forStackItem.incrementExpression,
//                        localStatement
//                    )
//                    latestStatement.push(forStatement)
//                }
//                top.getType() == StatementType.IF_STATEMENT -> {
//                    val ifStackItem = top as IfParseStackItem
//                    val ifStatement = IfNode(
//                        ifStackItem.booleanExpression,
//                        localStatement,
//                        null
//                    )
//                    latestStatement.push(ifStatement)
//                    shouldExtract = false
//                }
//                top.getType() == StatementType.BLOCK_STATEMENT -> {
//                    val blockStackItem = top as BlockParseStackItem
//                    val statements = blockStackItem.statements + listOf(localStatement)
//                    if (tokens[currentPosition].type == TokenType.RIGHT_BRACE) {
//                        val (_, positionAfterRightBrace) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.RIGHT_BRACE)
//                        val blockStatement = BasicBlockNode(
//                            statements
//                        )
//                        latestStatement.push(blockStatement)
//                        currentPosition = positionAfterRightBrace
//                        shouldExtract = false
//                    } else {
//                        val blockStackItem2 = BlockParseStackItem(statements)
//                        programStack.push(blockStackItem2)
//                        shouldExtract = true
//                    }
//                }
//            }
//        }
//
//        return Pair(latestStatement.pop(), tokens.size)
//    }


    override fun parse(
        tokens: List<Token>,
        startingPosition: Int
    ): Pair<IStatementNode, Int> {

        val programStack = Stack<Pair<MutableList<IStatementNode>, Stack<IStackItem>>>()
        programStack.push(Pair(mutableListOf(), Stack()))


        var currentPosition = startingPosition + 1
        var result = mutableListOf<IStatementNode>()
        while(
            programStack.isNotEmpty()
        ) {
            val top = programStack.peek()
            val tokenType = tokens[currentPosition].type
            if (tokenType == TokenType.LEFT_BRACE) {
                val (_, positionAfterLeftBrace) = tokenTypeAsserter.assertTokenType(
                    tokens,
                    currentPosition,
                    TokenType.LEFT_BRACE
                )
                programStack.push(Pair(mutableListOf(), Stack()))
                currentPosition = positionAfterLeftBrace
                continue
            }


            when (tokenType) {
                TokenType.DO -> {
                    val (_, positionAfterDo) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.DO)
                    currentPosition = positionAfterDo
                    top.second.push(DoStackItem())
                    continue
                }
                TokenType.FOR -> {
                    val (_, positionAfterFor) = tokenTypeAsserter.assertTokenType(
                        tokens,
                        currentPosition,
                        TokenType.FOR
                    )
                    val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(
                        tokens,
                        positionAfterFor,
                        TokenType.LEFT_PARENTHESES
                    )
                    val (initExpression, positionAfterInitExpression) = expressionParser.parse(
                        tokens,
                        positionAfterLeftParentheses,
                        setOf(TokenType.SEMICOLON)
                    )
                    val (_, positionAfterFirstSemi) = tokenTypeAsserter.assertTokenType(
                        tokens,
                        positionAfterInitExpression,
                        TokenType.SEMICOLON
                    )
                    val (testExpression, positionAfterTestExpression) = expressionParser.parse(
                        tokens,
                        positionAfterFirstSemi,
                        setOf(TokenType.SEMICOLON)
                    )
                    val (_, positionAfterSecondSemi) = tokenTypeAsserter.assertTokenType(
                        tokens,
                        positionAfterTestExpression,
                        TokenType.SEMICOLON
                    )
                    val (incrementExpression, positionAfterIncrementExpression) = expressionParser.parse(
                        tokens,
                        positionAfterSecondSemi,
                        setOf(TokenType.RIGHT_PARENTHESES)
                    )
                    val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(
                        tokens,
                        positionAfterIncrementExpression,
                        TokenType.RIGHT_PARENTHESES
                    )
                    currentPosition = positionAfterRightParentheses
                    top.second.push(ForStackItem(initExpression, testExpression, incrementExpression))
                    continue
                }
                TokenType.IF -> {
                    val (_, positionAfterIf) = tokenTypeAsserter.assertTokenType(tokens, currentPosition, TokenType.IF)
                    val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(
                        tokens,
                        positionAfterIf,
                        TokenType.LEFT_PARENTHESES
                    )
                    val (booleanExpression, positionAfterBooleanExpression) = expressionParser.parse(
                        tokens,
                        positionAfterLeftParentheses,
                        setOf(TokenType.RIGHT_PARENTHESES)
                    )
                    val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(
                        tokens,
                        positionAfterBooleanExpression,
                        TokenType.RIGHT_PARENTHESES
                    )
                    currentPosition = positionAfterRightParentheses
                    top.second.push(IfStackItem(booleanExpression))
                    continue
                }
                TokenType.ELSE -> {
                    val (_, positionAfterElse) = tokenTypeAsserter.assertTokenType(
                        tokens,
                        currentPosition,
                        TokenType.ELSE
                    )
                    currentPosition = positionAfterElse
                    top.second.push(ElseStackItem())
                    continue
                }
                TokenType.WHILE -> {
                    val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(
                        tokens,
                        currentPosition,
                        TokenType.WHILE
                    )
                    val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(
                        tokens,
                        positionAfterWhile,
                        TokenType.LEFT_PARENTHESES
                    )
                    val (expression, positionAfterExpression) = expressionParser.parse(
                        tokens,
                        positionAfterLeftParentheses,
                        setOf(TokenType.RIGHT_PARENTHESES)
                    )
                    val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(
                        tokens,
                        positionAfterExpression,
                        TokenType.RIGHT_PARENTHESES
                    )
                    currentPosition = positionAfterRightParentheses
                    top.second.push(WhileStackItem(expression))
                    continue
                }
            }


                val localStatement = when {
                    top.first.isNotEmpty() && top.first.last() is BasicBlockNode -> {
                        top.first.removeLast()
                    }
                    tokenType == TokenType.RETURN -> {
                        val (returnStatement, positionAfterReturn) = returnStatementParser.parse(
                            tokens,
                            currentPosition
                        )
                        currentPosition = positionAfterReturn
                        returnStatement
                    }
                    tokenType == TokenType.TYPE -> {
                        val (variableDeclarationStatement, positionAfter) = variableDeclarationListParser.parse(
                            tokens,
                            currentPosition
                        )
                        currentPosition = positionAfter
                        variableDeclarationStatement
                    }
                    tokenType == TokenType.IDENTIFIER || tokenType == TokenType.PRE_POST -> {
                        val (expressionStatement, positionAfter) = expressionStatementParser.parse(
                            tokens,
                            currentPosition
                        )
                        currentPosition = positionAfter
                        expressionStatement
                    }
                    else -> {
                        top.first.removeLast()
                    }
                }

                if (top.second.isNotEmpty()) {
                    val stackItem = top.second.pop()
                    when {
                        stackItem.getType() == StackItemType.DO -> {
                            val (_, positionAfterWhile) = tokenTypeAsserter.assertTokenType(
                                tokens,
                                currentPosition,
                                TokenType.WHILE
                            )
                            val (_, positionAfterLeftParentheses) = tokenTypeAsserter.assertTokenType(
                                tokens,
                                positionAfterWhile,
                                TokenType.LEFT_PARENTHESES
                            )
                            val (expression, positionAfterExpression) = expressionParser.parse(
                                tokens,
                                positionAfterLeftParentheses,
                                setOf(TokenType.RIGHT_PARENTHESES)
                            )
                            val (_, positionAfterRightParentheses) = tokenTypeAsserter.assertTokenType(
                                tokens,
                                positionAfterExpression,
                                TokenType.RIGHT_PARENTHESES
                            )
                            val (_, positionAfterSemicolon) = tokenTypeAsserter.assertTokenType(
                                tokens,
                                positionAfterRightParentheses,
                                TokenType.SEMICOLON
                            )
                            val doStatement = DoWhileNode(
                                expression,
                                localStatement
                            )
                            top.first.add(doStatement)
                            currentPosition = positionAfterSemicolon
                        }
                        stackItem.getType() == StackItemType.WHILE -> {
                            val whileStackItem = stackItem as WhileStackItem
                            val whileStatement = WhileNode(whileStackItem.expression, localStatement)
                            top.first.add(whileStatement)
                        }
                        stackItem.getType() == StackItemType.FOR -> {
                            val forStackItem = stackItem as ForStackItem
                            val forStatement = ForNode(
                                forStackItem.initExpression,
                                forStackItem.testExpression,
                                forStackItem.incrementExpression,
                                localStatement
                            )
                            top.first.add(forStatement)
                        }
                        stackItem.getType() == StackItemType.IF -> {
                            val ifStackItem = stackItem as IfStackItem
                            val ifStatement = IfNode(
                                ifStackItem.booleanExpression,
                                localStatement,
                            )
                            top.first.add(ifStatement)
                        }
                        stackItem.getType() == StackItemType.ELSE -> {
                            val elseStatement = ElseNode(
                                localStatement
                            )
                            top.first.add(elseStatement)
                        }
                    }
                } else {
                    top.first.add(localStatement)
                }

            if (tokenType == TokenType.RIGHT_BRACE) {
                val (_, positionAfterLeftBrace) = tokenTypeAsserter.assertTokenType(
                    tokens,
                    currentPosition,
                    TokenType.RIGHT_BRACE
                )
                result = programStack.pop().first
                if (programStack.isNotEmpty()) {
                    programStack.peek().first.add(BasicBlockNode(result))
                }
                currentPosition = positionAfterLeftBrace
            }

        }

        return Pair(BasicBlockNode(result), currentPosition)
    }
}
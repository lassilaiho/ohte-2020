package com.lassilaiho.calculator.core.parser;

import java.util.ArrayList;
import java.util.List;
import com.lassilaiho.calculator.core.lexer.*;

/**
 * {@link Parser} parses a list of lexemes into an abstract syntax tree.
 *
 * Parsers are single-use objects. To parse another expression, construct a new {2link Parser}
 * object.
 */
public class Parser {
    private final List<Lexeme> lexemes;
    /**
     * Current index to {@link #lexemes}.
     */
    private int current = 0;

    /**
     * Constructs a new parser that parses lexemes.
     *
     * @param lexemes The lexems to parse. Modifying the list during parsing is undefined behavior.
     */
    public Parser(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    /**
     * Parses an expression.
     * 
     * Calls after the first call always return null.
     * 
     * @return the resulting expression
     */
    public Expression parseExpression() {
        if (current > 0 || peek().type == LexemeType.EOF) {
            return null;
        }
        return parseBinaryExpression(Operator.MIN_PRECEDENCE);
    }

    private Expression parseBinaryExpression(int precedence) {
        if (precedence > Operator.MAX_PRECEDENCE) {
            return parseUnaryExpression();
        }
        var left = parseBinaryExpression(precedence + 1);
        while (true) {
            var operator = parseBinaryOperator(precedence);
            if (operator == null) {
                return left;
            }
            var right = parseBinaryExpression(precedence + 1);
            left = new BinaryExpression(left, operator, right);
        }
    }

    private Operator parseBinaryOperator(int precedence) {
        Operator operator;
        switch (peek().type) {
            case PLUS:
                operator = Operator.ADD;
                break;
            case MINUS:
                operator = Operator.SUBTRACT;
                break;
            case ASTERISK:
                operator = Operator.MULTIPLY;
                break;
            case SLASH:
                operator = Operator.DIVIDE;
                break;
            default:
                operator = null;
                break;
        }
        if (operator != null && operator.precedence() == precedence) {
            advance();
            return operator;
        }
        return null;
    }

    private Expression parseUnaryExpression() {
        var operator = parseUnaryOperator();
        if (operator == null) {
            return parseTerminal();
        }
        return new UnaryExpression(operator, parseUnaryExpression());
    }

    private Operator parseUnaryOperator() {
        if (peek().type == LexemeType.MINUS) {
            advance();
            return Operator.NEGATE;
        }
        return null;
    }

    private Expression parseTerminal() {
        var lexeme = peek();
        switch (lexeme.type) {
            case EOF:
                throw new ParserException("unexpected end of expression");
            case NUMBER:
                advance();
                return new NumberNode((double) lexeme.value);
            case IDENTIFIER:
                if (peek(1).type == LexemeType.LEFT_PAREN) {
                    return parseFunctionCall();
                }
                advance();
                return new VariableNode((String) lexeme.value);
            case LEFT_PAREN:
                advance();
                var subExpression = parseBinaryExpression(Operator.MIN_PRECEDENCE);
                parseToken(LexemeType.RIGHT_PAREN);
                return subExpression;
            default:
                throw new ParserException("unexpected lexeme: " + lexeme);
        }
    }

    private Expression parseFunctionCall() {
        var name = (String) parseToken(LexemeType.IDENTIFIER).value;
        parseToken(LexemeType.LEFT_PAREN);
        var arguments = new ArrayList<Expression>();
        var lexeme = peek();
        if (lexeme.type == LexemeType.RIGHT_PAREN) {
            advance();
            return new FunctionCallNode(name, arguments);
        }
        while (true) {
            arguments.add(parseBinaryExpression(Operator.MIN_PRECEDENCE));
            lexeme = peek();
            if (lexeme.type == LexemeType.RIGHT_PAREN) {
                advance();
                return new FunctionCallNode(name, arguments);
            } else if (lexeme.type != LexemeType.COMMA) {
                throwUnexpectedLexeme(LexemeType.RIGHT_PAREN, lexeme.type);
            }
            advance();
        }
    }

    private Lexeme parseToken(LexemeType type) {
        var lexeme = peek();
        if (lexeme.type != type) {
            throwUnexpectedLexeme(type, lexeme.type);
        }
        advance();
        return lexeme;
    }

    private void throwUnexpectedLexeme(LexemeType expected, LexemeType found) {
        throw new ParserException(
            "expected lexeme of type " + expected + ", found " + found);
    }

    private Lexeme peek() {
        return peek(0);
    }

    private Lexeme peek(int count) {
        if (current + count >= lexemes.size()) {
            return new Lexeme(LexemeType.EOF);
        }
        return lexemes.get(current + count);
    }

    private void advance() {
        current++;
    }
}

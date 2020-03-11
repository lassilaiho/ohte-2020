package com.lassilaiho.calculator.core.parser;

import java.util.List;
import com.lassilaiho.calculator.core.Operator;
import com.lassilaiho.calculator.core.lexer.*;

/**
 * Parser parses a list of lexemes into an abstract syntax tree.
 *
 * Parsers are single-use objects. To parse another expression, construct a new parser object.
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
        var lexeme = peek();
        if (lexeme.type == LexemeType.OPERATOR) {
            var operator = (Operator) lexeme.value;
            if (operator.precedence() == precedence) {
                advance();
                return operator;
            }
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
        var lexeme = peek();
        if (lexeme.type == LexemeType.OPERATOR) {
            var operator = (Operator) lexeme.value;
            if (operator == Operator.SUBTRACT) {
                advance();
                return operator;
            }
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
                return new Number((double) lexeme.value);
            case LEFT_PAREN:
                advance();
                var subExpression = parseBinaryExpression(Operator.MIN_PRECEDENCE);
                parseToken(LexemeType.RIGHT_PAREN);
                return subExpression;
            default:
                throw new ParserException("unexpected lexeme: " + lexeme);
        }
    }

    private void parseToken(LexemeType type) {
        if (peek().type != type) {
            throw new ParserException(
                "expected lexeme of type " + type + "found " + peek().type);
        }
        advance();
    }

    private Lexeme peek() {
        return lexemes.get(current);
    }

    private void advance() {
        current++;
    }
}

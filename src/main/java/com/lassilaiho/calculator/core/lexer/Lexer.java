package com.lassilaiho.calculator.core.lexer;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public final class Lexer {
    private final PushbackReader reader;

    /**
     * Constructs a new Lexer that reads from reader.
     * 
     * @param reader the reader to read characters from
     */
    public Lexer(Reader reader) {
        this.reader = new PushbackReader(reader);
    }

    /**
     * Performs lexical analysis on the reader passed to the constructor.
     * 
     * @return                a list of lexemes
     * 
     * @throws IOException    if an error occurs during reading
     * @throws LexerException if en error occurs during lexing
     */
    public List<Lexeme> lex() throws IOException, LexerException {
        var result = new ArrayList<Lexeme>();
        while (true) {
            var c = reader.read();
            if (c == -1) {
                result.add(new Lexeme(LexemeType.EOF));
                return result;
            }
            switch (c) {
                case '+':
                    result.add(new Lexeme(LexemeType.PLUS));
                    break;
                case '-':
                    result.add(new Lexeme(LexemeType.MINUS));
                    break;
                case '*':
                    result.add(new Lexeme(LexemeType.ASTERISK));
                    break;
                case '/':
                    result.add(new Lexeme(LexemeType.SLASH));
                    break;
                case '(':
                    result.add(new Lexeme(LexemeType.LEFT_PAREN));
                    break;
                case ')':
                    result.add(new Lexeme(LexemeType.RIGHT_PAREN));
                    break;
                default:
                    if (Character.isDigit(c)) {
                        unread(c);
                        result.add(lexNumber());
                        continue;
                    } else if (isIdentifierHead(c)) {
                        unread(c);
                        result.add(lexIdentifier());
                        continue;
                    } else if (Character.isWhitespace(c)) {
                        continue;
                    }
                    throw new LexerException(
                        "unexpected character: " + new String(Character.toChars(c)));
            }
        }
    }

    private Lexeme lexNumber() throws IOException {
        var builder = new StringBuilder();
        while (true) {
            var character = reader.read();
            if (Character.isDigit(character) || character == '.') {
                builder.append(Character.toChars(character));
            } else {
                unread(character);
                var numberLiteral = builder.toString();
                try {
                    return new Lexeme(LexemeType.NUMBER, Double.valueOf(numberLiteral));
                } catch (NumberFormatException e) {
                    throw new LexerException("invalid number: " + numberLiteral);
                }
            }
        }
    }

    private Lexeme lexIdentifier() throws IOException {
        var character = reader.read();
        if (!isIdentifierHead(character)) {
            throw new LexerException("identifier cannot begin with character: "
                + Character.toString(character));
        }
        var builder = new StringBuilder();
        builder.append(Character.toChars(character));
        while (true) {
            character = reader.read();
            if (isIdentifierTail(character)) {
                builder.append(Character.toChars(character));
            } else {
                unread(character);
                return new Lexeme(LexemeType.IDENTIFIER, builder.toString());
            }
        }
    }

    private void unread(int c) throws IOException {
        if (c != -1) {
            reader.unread(c);
        }
    }

    private boolean isIdentifierHead(int codePoint) {
        return Character.isLetter(codePoint) || codePoint == '_';
    }

    private boolean isIdentifierTail(int codePoint) {
        return isIdentifierHead(codePoint) || Character.isDigit(codePoint);
    }
}

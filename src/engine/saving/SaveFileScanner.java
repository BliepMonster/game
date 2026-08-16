package engine.saving;

import java.util.ArrayList;

import static engine.saving.SaveFileTokenType.*;

public final class SaveFileScanner {
    private final String src;
    private int index, start;
    private final ArrayList<SaveFileToken> tokens = new ArrayList<>();
    public SaveFileScanner(String src) {
        this.src = src;
        index = start = 0;
    }
    public ArrayList<SaveFileToken> scan() {
        while (!isAtEnd()) {
            tokens.add(scanOnce());
        }
        if (tokens.getLast().type() != EOF)
            tokens.add(makeToken(EOF));
        return tokens;
    }

    SaveFileToken scanOnce() {
        skipWhiteSpace();
        if (isAtEnd())
            return makeToken(EOF);
        start = index;
        char c = advance();
        if (c == '{') {
            return makeToken(LBRACE);
        }
        if (c == '}') {
            return makeToken(RBRACE);
        }
        if (c == '"') {
            return string();
        }
        if (c == 'T') {
            return makeToken(TRUE);
        }
        if (c == 'F') {
            return makeToken(FALSE);
        }
        if (c == 'N') {
            return makeToken(NULL);
        }
        if (c == '%') {
            return makeToken(PERCENT);
        }
        if (c == '*') {
            return makeToken(STAR);
        }
        if (c == '/') {
            return makeToken(SLASH);
        }
        if (c == '_') {
            return makeToken(UNDERSCORE);
        }
        if (c == '[') {
            return makeToken(LBRACKET);
        }
        if (c == ']') {
            return makeToken(RBRACKET);
        }
        if (c == ';') {
            return makeToken(SEMICOLON);
        }
        // negative numbers
        if (c == '-') {
            return number();
        }
        if (isNumber(c))
            return number();
        throw new RuntimeException("Invalid token: "+c);
    }
    boolean isAtEnd() {
        return index >= src.length();
    }
    char advance() {
        return src.charAt(index++);
    }
    char peek() {
        if (index == src.length())
            return '\0';
        return src.charAt(index);
    }
    private void skipWhiteSpace() {
        while (true) {
            char c = peek();
            if (c == ' ' || c == '\t' || c == '\n') {
                advance();
            } else {
                return;
            }
        }
    }
    SaveFileToken number() {
        char c = peek();
        boolean e = false;
        while (isNumber(c = peek()) || c == 'e' || c == 'E' || (c == '-' && e)) {
            e = c == 'E' || c == 'e';
            advance();
        }
        return makeToken(NUMBER);
    }
    SaveFileToken makeToken(SaveFileTokenType type) {
        return new SaveFileToken(src.substring(start, index), type);
    }
    SaveFileToken string() {
        while (peek() != '"' && !isAtEnd()) advance();
        advance();
        return makeToken(STRING);
    }
    boolean isNumber(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }
}

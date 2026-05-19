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
        switch (c) {
            case '{' -> {
                return makeToken(LBRACE);
            }
            case '}' -> {
                return makeToken(RBRACE);
            }
            case '"' -> {
                return string();
            }
            case 'T' -> {
                return makeToken(TRUE);
            }
            case 'F' -> {
                return makeToken(FALSE);
            }
            case 'N' -> {
                return makeToken(NULL);
            }
            case '%' -> {
                return makeToken(PERCENT);
            }
            case '*' -> {
                return makeToken(STAR);
            }
            case '/' -> {
                return makeToken(SLASH);
            }
            case '_' -> {
                return makeToken(UNDERSCORE);
            }
            case '[' -> {
                return makeToken(LBRACKET);
            }
            case ']' -> {
                return makeToken(RBRACKET);
            }
            case ';' -> {
                return makeToken(SEMICOLON);
            }
            // negative numbers
            case '-' -> {
                return number();
            }
            default -> {
                if (isNumber(c))
                    return number();
            }
        }
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
            switch (c) {
                case ' ', '\t', '\n' -> advance();
                default -> {
                    return;
                }
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

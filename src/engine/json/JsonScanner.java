package engine.json;

import java.util.ArrayList;

import static engine.json.JsonToken.Type.*;

public class JsonScanner {
    private final String src;
    private int current;
    public JsonScanner(String src) {
        this.src = src;
        this.current = 0;
    }
    public ArrayList<JsonToken> scan() {
        ArrayList<JsonToken> tokens = new ArrayList<>();
        while (!isAtEnd()) {
            tokens.add(scanOnce());
        }
        return tokens;
    }
    JsonToken scanOnce() {
        skipWhitespace();
        char c = advance();
        return switch (c) {
            case '{' -> makeToken(LBRACE);
            case '}' -> makeToken(RBRACE);
            case '[' -> makeToken(LBRACKET);
            case ']' -> makeToken(RBRACKET);
            case ',' -> makeToken(COMMA);
            case ':' -> makeToken(COLON);
            case '"' -> string();
            case '-' -> number(c);
            default -> {
                if (Character.isDigit(c)) {
                    yield number(c);
                } else if (c == 't') {
                    if (advance() != 'r' || advance() != 'u' || advance() != 'e')
                        throw new RuntimeException("Invalid token");
                    yield makeToken(TRUE);
                } else if (c == 'f') {
                    if (advance() != 'a' || advance() != 'l' || advance() != 's' || advance() != 'e')
                        throw new RuntimeException("Invalid token");
                    yield makeToken(FALSE);
                } else if (c == 'n') {
                    if (advance() != 'u' || advance() != 'l' || advance() != 'l')
                        throw new RuntimeException("Invalid token");
                    yield makeToken(NULL);
                }
                throw new RuntimeException("Invalid token");
            }
        };
    }
    char parseBackspace() {
        advance(); // consume \
        return switch (advance()) {
            case '\\' -> '\\';
            case '\'' -> '\'';
            case 't' -> '\t';
            case 'r' -> '\r';
            case 'n' -> '\n';
            case '"' -> '"';
            default -> throw new RuntimeException("Invalid escape sequence");
        };
    }
    JsonToken string() {
        StringBuilder sb = new StringBuilder();
        while (peek() != '"') {
            if (peek() == '\\')
                sb.append(parseBackspace());
            else
                sb.append(advance());
        }
        advance(); // consume "
        return new JsonToken(sb.toString(), STRING);
    }
    JsonToken number(char first) {
        StringBuilder sb = new StringBuilder();
        sb.append(first);
        while (Character.isDigit(peek()) || peek() == '.') {
            sb.append(advance());
        }
        return new JsonToken(Double.parseDouble(sb.toString()), NUMBER);
    }
    JsonToken makeToken(JsonToken.Type type) {
        return new JsonToken(null, type);
    }
    char advance() {
        return src.charAt(current++);
    }
    char peek() {
        if (isAtEnd())
            return '\0';
        return src.charAt(current);
    }
    void skipWhitespace() {
        while (Character.isWhitespace(peek())) {
            advance();
        }
    }
    boolean isAtEnd() {
        return current >= src.length();
    }
}

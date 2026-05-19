package engine.json;

public record JsonToken(Object token, Type type) {
    enum Type {
        STRING,
        NUMBER,
        TRUE,
        FALSE,
        NULL,
        LBRACE,
        RBRACE,
        LBRACKET,
        RBRACKET,
        COMMA,
        COLON
    }
}

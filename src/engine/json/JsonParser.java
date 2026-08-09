package engine.json;

import java.util.ArrayList;

public class JsonParser {
    private ArrayList<JsonToken> tokens;
    private int index = 0;
    public JsonFile parse(String json) {
        try {
            tokens = new JsonScanner(json).scan();
            index = 0;
            JsonToken.Type type = advance().type();
            JsonFile file = new JsonFile();
            if (type != JsonToken.Type.LBRACE) throw new RuntimeException("Expected {");
            if (!match(JsonToken.Type.RBRACE)) {
                do {
                    String skey = advance().token().toString();
                    if (!match(JsonToken.Type.COLON)) throw new RuntimeException("Expected :");
                    JsonValue svalue = value();
                    file.values.put(skey, svalue);
                } while (match(JsonToken.Type.COMMA));
            }
            return file;
        } catch (Exception e) {
            System.out.println(json);
            throw new RuntimeException(e);
        }
    }
    JsonValue value() {
        JsonToken token = advance();
        return switch (token.type()) {
            case STRING -> new JsonString(token.token().toString());
            case NUMBER -> new JsonNumber((float) (double) token.token());
            case TRUE -> new JsonBoolean(true);
            case FALSE -> new JsonBoolean(false);
            case NULL -> JsonNull.INSTANCE;
            case LBRACE -> object();
            case LBRACKET -> array();
            default -> throw new RuntimeException("Unexpected token");
        };
    }
    JsonObject object() {
        JsonObject obj = new JsonObject();
        if (peek().type() != JsonToken.Type.RBRACE) {
            do {
                String s = advance().token().toString();
                JsonToken.Type type = advance().type();
                if (type != JsonToken.Type.COLON) throw new RuntimeException("Expected :");
                obj.values.put(s, value());
            } while (match(JsonToken.Type.COMMA));
        }
        JsonToken.Type type = advance().type();
        if (type != JsonToken.Type.RBRACE) throw new RuntimeException("Expected }");
        return obj;
    }
    JsonArray array() {
        JsonArray obj = new JsonArray();
        if (peek().type() != JsonToken.Type.RBRACKET) {
            do {
                obj.values.add(value());
            } while (match(JsonToken.Type.COMMA));
        }
        JsonToken.Type type = advance().type();
        if (type != JsonToken.Type.RBRACKET) throw new RuntimeException("Expected }");
        return obj;
    }
    boolean match(JsonToken.Type type) {
        if (isAtEnd()) return false;
        if (peek().type() == type) {
            advance();
            return true;
        }
        return false;
    }
    JsonToken advance() {
        return tokens.get(index++);
    }
    JsonToken peek() {
        if (isAtEnd()) return null;
        return tokens.get(index);
    }
    boolean isAtEnd() {
        return index >= tokens.size();
    }
}

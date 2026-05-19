package engine.saving;

import engine.saving.objects.*;

import java.util.ArrayList;
import java.util.HashMap;

import static engine.saving.SaveFileTokenType.*;

class SaveFileParserException extends RuntimeException {
    public SaveFileParserException(String message) {
        super(message);
    }
}

public class SaveFileParser {
    private final ArrayList<SaveFileToken> tokens;
    private int index = 0;
    public SaveFileParser(ArrayList<SaveFileToken> tokens) {
        this.tokens = tokens;
    }
    public HashMap<String, SavedObject> parse() {
        consume(LBRACE);
        HashMap<String, SavedObject> map = new HashMap<>();
        while (!match(RBRACE)) {
            SaveFileToken token = advance();
            if (token.type() == STRING)
                map.put(token.s().substring(1, token.s().length()-1), object());
            else
                throw new SaveFileParserException("Expected string but got "+token.type());
        }
        consume(EOF);
        return map;
    }
    SaveFileToken advance() {
        if (index >= tokens.size())
            throw new SaveFileParserException("Unexpected end of input");
        return tokens.get(index++);
    }
    SaveFileToken peek() {
        if (index >= tokens.size())
            throw new SaveFileParserException("Unexpected end of input");
        return tokens.get(index);
    }
    boolean match(SaveFileTokenType... types) {
        SaveFileToken token = peek();
        for (SaveFileTokenType type : types)
            if (token.type() == type) {
                advance();
                return true;
            }
        return false;
    }
    void consume(SaveFileTokenType type) {
        if (!match(type)) {
            throw new SaveFileParserException("Expected "+type+" but got "+peek().type());
        }
    }
    SavedObject object() {
        SaveFileToken t = advance();
        switch (t.type()) {
            case STRING -> {
                return new SavedString(t.s().substring(1, t.s().length()-1));
            }
            case TRUE -> {
                return new SavedBoolean(true);
            }
            case FALSE -> {
                return new SavedBoolean(false);
            }
            case NULL -> {
                return new SavedNull();
            }
            case EOF -> throw new SaveFileParserException("Unexpected EOF");
            case RBRACE -> throw new SaveFileParserException("Unexpected RBRACE");
            case LBRACE -> {
                return dictionary();
            }
            case NUMBER -> {
                return new SavedFloat(HexConverter.decodeFloat(t.s()));
            }
            case RBRACKET -> throw new SaveFileParserException("Unexpected RBRACKET");
            case LBRACKET -> {
                return array();
            }
            default -> throw new SaveFileParserException("Unexpected token "+t.type());
        }
    }
    SavedDictionary dictionary() {
        HashMap<String, SavedObject> map = new HashMap<>();
        while (!match(RBRACE)) {
            SaveFileToken token = advance();
            if (token.type() == STRING)
                map.put(token.s().substring(1, token.s().length()-1), object());
            else
                throw new SaveFileParserException("Expected string but got "+token.type());
        }
        return new SavedDictionary(map);
    }
    SaveFileToken previous() {
        return tokens.get(index-1);
    }
    SavedArray array() {
        ArrayList<SavedObject> array = new ArrayList<>();
        while (!match(RBRACKET)) {
            int rep = 1;
            while (match(PERCENT, STAR, SLASH, UNDERSCORE)) {
                SaveFileTokenType t = previous().type();
                rep += t == PERCENT ? 1 : t == STAR ? 2 : t == SLASH ? 3 : 5;
            }
            SavedObject o = object();
            for (int i = 0; i < rep; i++)
                array.add(o);
            if (!(peek().type() == RBRACKET))
                match(SEMICOLON);
        }
        return new SavedArray(array);
    }
}

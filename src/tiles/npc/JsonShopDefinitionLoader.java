package tiles.npc;

import crafting.json.JsonRecipeLoader;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.textures.Texture;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import engine.json.*;
import main.TextureIdMap;
import stats.inventory.items.ItemDefinition;
import tiles.npc.dialogue.*;
import tiles.npc.ui.ShopWrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonShopDefinitionLoader {
    public static MoneyRequirement loadMReq(JsonObject object) {
        return new MoneyRequirement(
                Currency.valueOf(((JsonString) object.values.get("currency")).value),
                (int) ((JsonNumber) object.values.get("amount")).num
        );
    }
    public static ResourceGiver loadRGiver(JsonObject object) {
        return new ResourceGiver(
                ((JsonString) object.values.get("id")).value,
                (int) ((JsonNumber) object.values.get("amount")).num
        );
    }
    public static UniqueItemGiver loadUIGiver(JsonObject object) {
        return new UniqueItemGiver(
                ItemDefinition.get(((JsonString) object.values.get("id")).value)
        );
    }
    public static ItemGiver loadIGiver(JsonObject object) {
        String type = ((JsonString) object.values.get("type")).value;
        return switch (type) {
            case "resource" -> loadRGiver(object);
            case "item" -> loadUIGiver(object);
            default -> throw new RuntimeException(type);
        };
    }
    public static ShopEntry loadEntry(JsonObject object) {
        var preCraft = JsonRecipeLoader.loadPReqs(
                (JsonArray) object.values.get("requirements")
        );
        var ingredients = JsonRecipeLoader.loadIReqs(
                (JsonArray) object.values.get("ingredients")
        );
        var price = loadMReq(
                (JsonObject) object.values.get("price")
        );
        var texture = ((JsonString) object.values.get("texture")).value;
        return new ShopEntry(
                preCraft,
                ingredients,
                price,
                loadIGiver(
                        (JsonObject) object.values.get("result")
                ),
                new Texture(texture)
        );
    }
    public static ShopEntry[] loadEntries(JsonArray array) {
        var entries = new ShopEntry[array.values.size()];
        for (int i = 0; i < entries.length; ++i) {
            entries[i] = loadEntry((JsonObject) array.values.get(i));
        }
        return entries;
    }
    public static DialoguePieceBuilder loadPiece(JsonObject object) {
        String s = ((JsonString) object.values.get("type")).value;
        return switch (s) {
            case "shop" -> (font, controller, context, storage) ->
                    new ShopWrapper(new ShopDefinition(loadEntries((JsonArray) object.values.get("entries"))), font, storage, context, controller);
            case "text" -> (font, controller, _, _) ->
                    new TextDialogue(controller, TextureIdMap.get(((JsonString) object.values.get("tex")).value), ((JsonString) object.values.get("name")).value, ((JsonString) object.values.get("message")).value, font);
            case "cond" -> (font, controller, context, storage) ->
                    new ConditionalDialoguePiece(loadPiece((JsonObject) object.values.get("yes")).build(font, controller, context, storage),
                            loadPiece((JsonObject) object.values.get("no")).build(font, controller, context, storage),
                            ((JsonString) object.values.get("attrib")).value, controller);
            case "list" ->  (font, controller, context, storage) -> {
                JsonArray arr = (JsonArray) object.values.get("array");
                DialoguePiece[] pieces = new DialoguePiece[arr.values.size()];
                var v = new DialogueControllerPiece(pieces, controller);
                for (var i = 0; i < pieces.length; ++i) {
                    pieces[i] = loadPiece((JsonObject) arr.values.get(i)).build(font, controller, context, storage);
                }
                v.set(pieces, context);
                return v;
            };
            case "attrib" -> (font, controller, context, storage) -> new DialoguePiece(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(0), new RelativeSizeConstraint(0), controller) {
                @Override
                public void display(DrawManager draw, float x, float y, float width, float height) {
                    context.attributes().add(((JsonString) object.values.get("attrib")).value);
                    controller.advance();
                }

                @Override
                public void detectClick(Screen screen, float x, float y) {

                }

                @Override
                public void detectContinuousPress(Screen screen, float x, float y) {

                }
            };
            default -> throw new RuntimeException();
        };
    }
    public static DialoguePieceBuilder[] loadPieces(JsonArray array) {
        DialoguePieceBuilder[] pieces = new DialoguePieceBuilder[array.values.size()];
        for (int i = 0; i < pieces.length; ++i) {
            pieces[i] = loadPiece((JsonObject) array.values.get(i));
        }
        return pieces;
    }
    public static NpcDefinition loadDefinition(JsonFile file) {
        return new NpcDefinition(
                ((JsonString) file.values.get("name")).value,
                ((JsonString) file.values.get("id")).value,
                new Texture(((JsonString) file.values.get("texture")).value),
                (float) ((JsonNumber) file.values.get("height")).num,
                file.values.containsKey("z_offset") ? (float) ((JsonNumber) file.values.get("z_offset")).num : 0f,
                !file.values.containsKey("standable") || ((JsonBoolean) file.values.get("standable")).bool,
                loadPieces((JsonArray) file.values.get("dialogue"))
        );
    }
    public static void registerAll() {
        try (var paths = Files.walk(Path.of("res/shops"))) {
            paths.filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return Files.readString(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(string ->
                            ShopDefinitionRegistry.register(
                                    JsonShopDefinitionLoader.loadDefinition(
                                            new JsonParser().parse(string)
                                    )
                            )
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package alchemy;

import engine.Maths;
import engine.core.DrawManager;
import engine.core.textures.Texture;
import engine.gui.components.*;
import engine.gui.components.Container;
import engine.gui.components.shapes.RectangularComponent;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.Main;
import main.TextureIdMap;

import java.awt.*;

public class BaseAlchemyComponent extends Container {
    AlchemyStationPanel panel;
    public BaseAlchemyComponent() {
        super(new Texture("res/textures/gray.png"), CenterConstraint.CENTER, CenterConstraint.CENTER,
                new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(1.2f));

        this.add(new TexturedButton(new RelativeYConstraint(.3f), new RelativeYConstraint(.5f),
                new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f), TextureIdMap.get("base:empty")) {
            @Override
            public void invoke() {
                if (panel.opened > System.currentTimeMillis()-200) return;
                panel.close();
            }
        });
        this.add(new RectangularComponent(Color.red, new RelativeYConstraint(.3f), new RelativeYConstraint(.5f),
                new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f)));
        this.add(new DecorativeComponent(CenterConstraint.CENTER, new RelativeYConstraint(-.2f), new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(.8f)) {
            @Override
            public void display(DrawManager draw, float x, float y, float width, float height) {
                if (panel.part.recipe != null) draw.image(panel.part.recipe.icon(), x, y, width, height);
            }
        });
        this.add(new DecorativeComponent(CenterConstraint.CENTER, new RelativeYConstraint(-.4f), new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(.2f)) {
            @Override
            public void display(DrawManager draw, float x, float y, float width, float height) {
                if (panel.part.recipe == null) return;
                var font = Main.font;
                var str = Maths.countdownString(panel.part.end-System.currentTimeMillis());
                var size = Math.min(font.getSizeForWidth(str, width), font.getWidthForSize(str, height));
                draw.string(str, Color.white, x+width/2-font.getWidthForSize(str, size)/2, y+height/2-font.getHeight(size)/2, font, size);
            }
        });
        this.add(new TexturedButton(CenterConstraint.CENTER, new RelativeYConstraint(-.6f),
                new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(.2f), TextureIdMap.get("base:empty")) {
            @Override
            public void invoke() {
                if (panel.opened > System.currentTimeMillis()-200) return;
                if (panel.part.isDone()) panel.claim();
                else panel.setState(AlchemyStationPanel.STATE_SELECT);
            }
        });
        this.add(new DecorativeComponent(CenterConstraint.CENTER, new RelativeYConstraint(-.6f),
                new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(.2f)) {
            @Override
            public void display(DrawManager draw, float x, float y, float width, float height) {
                draw.rect(panel.part.isDone() ? Color.yellow : Color.green, x, y, width, height);
            }
        });
        this.add(new DecorativeComponent(CenterConstraint.CENTER, new RelativeYConstraint(-.6f),
                new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(.2f)) {
            @Override
            public void display(DrawManager draw, float x, float y, float width, float height) {
                var font = Main.font;
                var str = panel.part.isDone() ? "Claim" : "Select recipe";
                var size = Math.min(font.getSizeForWidth(str, width), font.getSizeForHeight(height));
                draw.string(str, Color.black, x+width/2-font.getWidthForSize(str, size)/2, y+height/2-font.getHeight(size)/2, font, size);
            }
        });
    }
    public void setPanel(AlchemyStationPanel panel) {
        this.panel = panel;
    }
}

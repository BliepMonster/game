package tiles.resources;

import engine.core.DrawManager;
import entities.Player;
import main.Game;
import main.ItemConstants;
import stats.StatHolder;
import stats.skills.SkillSet;

import java.awt.*;

import static java.awt.Color.red;

public class StrawberryTilePart extends ResourceCreator {
    public static final Color green = new Color(0, 160, 0);
    public static final ResourceDefinition DEFINITION = new ResourceDefinition(ItemConstants.ITEM_STRAWBERRY, 4, 1, SkillSet.SkillType.FORAGING, StatHolder.Stat.FORAGING_YIELD, Game.TICKS_PER_SECOND*5);
    public StrawberryTilePart() {
        super(DEFINITION);
    }
    public void renderUnready(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(green, x+w/4, y+h/4, w/2, h/2);
    }
    public void renderReady(DrawManager draw, float x, float y, float w, float h) {
        renderUnready(draw, x, y, w, h);
        draw.rect(red, x+w/3, y+h/3, w/3, h/3);
    }
    public boolean isStandable() {
        return true; // players & entities can stand on strawberry bushes
    }
}

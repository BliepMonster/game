package tiles.resources;

import engine.core.DrawManager;
import main.Game;
import main.ItemConstants;
import stats.StatHolder;
import stats.skills.SkillSet;

import java.awt.*;

import static java.awt.Color.*;

public class WheatTilePart extends ResourceCreator {
    public static final Color UNREADY = new Color(128, 222, 117);
    public static final ResourceDefinition DEFINITION = new ResourceDefinition(ItemConstants.ITEM_WHEAT, 3, 1, SkillSet.SkillType.FARMING, StatHolder.Stat.FARMING_YIELD, Game.TICKS_PER_SECOND*10);
    public WheatTilePart() {
        super(DEFINITION);
    }
    public boolean isStandable() {
        return true;
    }
    public void renderReady(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(yellow, x, y, w, h*.8f);
    }
    public void renderUnready(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(UNREADY, x, y, w, h*.8f);
    }
}

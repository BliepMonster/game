package buildings.ui;

import buildings.RepairTask;
import crafting.FullItemStorage;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.components.Container;
import engine.gui.components.TexturedButton;
import engine.gui.components.labels.CenteredLabel;
import engine.gui.components.shapes.RectangularComponent;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.Camera;
import main.FadeAttributeEffect;
import main.TextureIdMap;
import stats.inventory.resources.InventoryStorage;

import java.awt.*;
import java.util.Map;

public class RepairTaskDisplayPanel extends Container {
    public RepairTaskDisplayPanel(RepairTask task, Font font, Camera camera, FullItemStorage storage) {
        super(null, CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(.9f), new RelativeSizeConstraint(1.2f));
        add(new RectangularComponent(Color.white, getXConstraint(), getYConstraint(), getWidthConstraint(), getHeightConstraint()));
        add(new RectangularComponent(Color.red, new RelativeYConstraint(.35f), new RelativeYConstraint(.5f), new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f)));
        add(new TexturedButton(new RelativeYConstraint(.35f), new RelativeYConstraint(.5f), new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f), TextureIdMap.get("base:empty")) {
            @Override
            public void invoke() {
                GUI.scheduleClear(0);
                GUI.toggle(62);
            }
        });

        InventoryStorage resources = task.resources();
        int i = 0;
        for (Map.Entry<String, Integer> entry : resources) {
            boolean has = storage.storage.contains(entry.getKey(), entry.getValue());
            add(new CenteredLabel(new RelativeYConstraint(.1f), new RelativeYConstraint(.3f - i * .1f), new RelativeSizeConstraint(.7f), new RelativeSizeConstraint(.08f), entry.getKey() + ": " + entry.getValue(), has ? Color.black : Color.red, font));
            i++;
        }

        if (task.coins() > 0) {
            boolean has = storage.currency.hasCoins(task.coins());
            add(new CenteredLabel(new RelativeYConstraint(.1f), new RelativeYConstraint(.3f - i * .1f), new RelativeSizeConstraint(.7f), new RelativeSizeConstraint(.08f), task.coins() + " coins", has ? Color.black : Color.red, font));
        }

        boolean canAfford = canAfford(task, storage);

        add(new Component(new RelativeYConstraint(-.2f), new RelativeYConstraint(-.45f), new RelativeSizeConstraint(.4f), new RelativeSizeConstraint(.1f)) {
            @Override
            public void display(DrawManager draw, float x, float y, float w, float h) {
                draw.rect(canAfford ? Color.orange : Color.gray, x, y, w, h);
                float fsize = Math.min(font.getSizeForWidth("Build", w), font.getSizeForHeight(h));
                draw.string("Build", canAfford ? Color.black : Color.darkGray, x, y, font, fsize);
            }

            @Override
            public void detectClick(Screen screen, float x, float y) {
                if (isInBounds(screen, x, y) && canAfford) {
                    for (Map.Entry<String, Integer> entry : task.resources()) {
                        storage.storage.spend(entry.getKey(), entry.getValue());
                    }
                    if (task.coins() > 0) {
                        storage.currency.spend(task.coins());
                    }
                    GUI.scheduleClear(0);
                    GUI.toggle(62);
                    camera.addEffect(new FadeAttributeEffect(task.attrib(), 1000));
                }
            }

            @Override
            public void detectContinuousPress(Screen screen, float x, float y) {}
        });
    }

    private static boolean canAfford(RepairTask task, FullItemStorage storage) {
        for (Map.Entry<String, Integer> entry : task.resources()) {
            if (!storage.storage.contains(entry.getKey(), entry.getValue())) return false;
        }
        if (task.coins() > 0 && !storage.currency.hasCoins(task.coins())) return false;
        return true;
    }
}

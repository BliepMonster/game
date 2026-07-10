package lighting;

import engine.hitboxes.Positioned;

public interface LightSource extends Positioned {
    float getLightLevel();
}

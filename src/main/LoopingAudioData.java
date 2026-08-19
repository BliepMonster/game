package main;

import engine.core.audio.AudioMaster;
import engine.core.audio.AudioSource;
import org.joml.Vector2f;

public record LoopingAudioData(String file, float volume, float pitch, float x, float y, boolean relative, float rolloff, float referenceDistance, float maxDistance) {
    public AudioSource createSource() {
        int buffer = AudioMaster.loadSound(file);
        var source = new AudioSource()
                .withVolume(volume)
                .withPitch(pitch)
                .withPosition(new Vector2f(x, y))
                .withRelativePositioning(relative)
                .withRolloffFactor(rolloff)
                .withReferenceDistance(referenceDistance)
                .withMaxDistance(maxDistance)
                .withLooping(true);
        source.play(buffer);
        return source;
    }
}

package engine.core.audio;

import org.joml.Vector2f;
import org.lwjgl.openal.AL10;

import static org.lwjgl.openal.AL10.*;

public class AudioSource {
    private int id;
    public AudioSource() {
        id = AL10.alGenSources();
        AL10.alSourcef(id, AL10.AL_GAIN, 1);
        AL10.alSourcef(id, AL10.AL_PITCH, 1);
        AL10.alSource3f(id, AL10.AL_POSITION, 0, 0, 0);
    }
    public void play(int buffer) {
        AL10.alSourcei(id, AL_BUFFER, buffer);
        AL10.alSourcePlay(id);
    }
    public void delete() {
        alDeleteSources(id);
    }
    public AudioSource withVolume(float volume) {
        AL10.alSourcef(id, AL_GAIN, volume);
        return this;
    }
    public AudioSource withPitch(float volume) {
        AL10.alSourcef(id, AL_PITCH, volume);
        return this;
    }
    public AudioSource withPosition(Vector2f position) {
        alSource3f(id, AL_POSITION, position.x, position.y, 0);
        return this;
    }
    public AudioSource withVelocity(Vector2f position) {
        alSource3f(id, AL_VELOCITY, position.x, position.y, 0);
        return this;
    }
    public AudioSource withLooping(boolean looping) {
        AL10.alSourcei(id, AL_LOOPING, looping ? 1 : 0);
        return this;
    }
    public AudioSource withRelativePositioning(boolean relative) {
        AL10.alSourcei(id, AL_SOURCE_RELATIVE, relative ? AL_TRUE : AL_FALSE);
        return this;
    }
    public AudioSource withRolloffFactor(float rolloff) {
        AL10.alSourcef(id, AL_ROLLOFF_FACTOR, rolloff);
        return this;
    }
    public AudioSource withReferenceDistance(float distance) {
        AL10.alSourcef(id, AL_REFERENCE_DISTANCE, distance);
        return this;
    }
    public AudioSource withMaxDistance(float distance) {
        AL10.alSourcef(id, AL_MAX_DISTANCE, distance);
        return this;
    }
    public boolean isPlaying() {
        return AL10.alGetSourcei(id, AL_SOURCE_STATE) == AL_PLAYING;
    }
    public void pause() {
        AL10.alSourcePause(id);
    }
    public void continuePlaying() {
        AL10.alSourcePlay(id);
    }
    public void stop() {
        AL10.alSourceStop(id);
    }
}

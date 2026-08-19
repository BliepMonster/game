package engine.core.audio;

import org.joml.Vector2f;
import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class AudioMaster {
    private static List<Integer> buffers = new ArrayList<>();
    private static long context, device;
    private static ALCCapabilities alcCapabilities;
    private static ALCapabilities alCapabilities;
    public static void init() {
            String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
            device = ALC10.alcOpenDevice(defaultDeviceName);
            alcCapabilities = ALC.createCapabilities(device);

            context = ALC10.alcCreateContext(device, (IntBuffer) null);
            ALC10.alcMakeContextCurrent(context);

            alCapabilities = AL.createCapabilities(alcCapabilities);
    }
    public static void setListenerData(Vector2f position) {
        AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, 0);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
    }
    public static void cleanup() {
        for (int buf : buffers) {
            AL10.alDeleteBuffers(buf);
        }
        ALC10.alcMakeContextCurrent(MemoryUtil.NULL);
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }
    public static int loadSound(String file) {
        int buffer = AL10.alGenBuffers();
        buffers.add(buffer);
        WaveData wave = WaveData.create(file);
        AL10.alBufferData(buffer, wave.format, wave.data, wave.samplerate);
        wave.dispose();
        return buffer;
    }
}

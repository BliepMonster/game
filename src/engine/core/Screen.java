package engine.core;

import engine.core.input.Console;
import engine.core.input.Input;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Screen {
    protected int width;
    protected int height;
    public static final int NO_FLAGS = 0;
    public static final int UNDECORATED = 1;
    public static final int MAXIMIZED = 2;
    public static final int FULLSCREEN = 4;
    public static final int VSYNC = 8;
    protected String title;
    protected long window;
    protected GLFWWindowSizeCallback sizeCallback;
    protected GLFWMonitorCallback monitorCallback;
    protected GLFWWindowPosCallback windowPosCallback;
    protected boolean needsMonitorFit = true;
    protected boolean isResized;
    protected boolean isFullscreen;
    protected int windowPosX;
    protected int windowPosY;
    protected long timeThisFrame = System.currentTimeMillis();
    protected long timePreviousFrame = System.currentTimeMillis();
    protected long timeDiff = 50;
    protected double FPS = 60;
    protected long nanoTime = System.nanoTime();
    protected long pastNanoTime = System.nanoTime();
    protected long nanoDiff = System.nanoTime();
    protected boolean active;
    protected Color bgColor = Color.BLACK;
    /**
     * Creates a window with size 1280x720 and title "Untitled".
     */
    public Screen() {
        width = 1280;
        height = 720;
        title = "Untitled";
    }

    /**
     * Creates a window with custom size and title "Untitled".
     * @param width The width of the screen, in pixels.
     * @param height The height of the screen, in pixels.
     */
    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        title = "Untitled";
    }

    /**
     * Creates a screen with size 1280x720 and a custom title.
     * @param title The title of your screen.
     */
    public Screen(String title) {
        width = 1280;
        height = 720;
        this.title = title;
    }

    /**
     * Creates a screen with custom width, height, and title.
     * @param width The width of your screen.
     * @param height The height of your screen.
     * @param title The title of your screen.
     */
    public Screen(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public String getTitle() {
        return title;
    }

    /**
     * Will create this Screen and its callbacks.
     *
     * @param console The console (with the input) to use to create the callbacks.
     */
    public void create(Console console, int flags) {
        boolean initSucceed = GLFW.glfwInit();
        if (!initSucceed)
            throw new RuntimeException("There was a problem starting the screen.");

        GLFW.glfwWindowHint(GLFW_DECORATED, (flags & UNDECORATED) == 0 ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        window = GLFW.glfwCreateWindow(width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
        if (window == 0) {
            throw new RuntimeException("The window wasn't created.");
        }
        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if (videoMode == null)
            throw new RuntimeException("Failed to fetch the VideoMode.");
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        createCallbacks(console.input);
        monitorCallback = new GLFWMonitorCallback() {
            public void invoke(long monitor, int event) {
                needsMonitorFit = true;
            }
        };
        GLFW.glfwSetMonitorCallback(monitorCallback);
        windowPosCallback = new GLFWWindowPosCallback() {
            public void invoke(long window, int xpos, int ypos) {
                needsMonitorFit = true;
            }
        };
        GLFW.glfwSetWindowPosCallback(window, windowPosCallback);
        GLFW.glfwSwapInterval((flags & VSYNC) == 0 ? 0 : 1);
        active = true;
        DrawManager.init();
        if ((flags & MAXIMIZED) != 0) {
            glfwSetWindowAttrib(window, GLFW_DECORATED, GLFW_FALSE);
            int mx = 0, my = 0;
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer x = stack.mallocInt(1);
                IntBuffer y = stack.mallocInt(1);
                GLFW.glfwGetMonitorPos(GLFW.glfwGetPrimaryMonitor(), x, y);
                mx = x.get(0);
                my = y.get(0);
            }
            glfwSetWindowSize(window, videoMode.width(), videoMode.height());
            glfwSetWindowPos(window, mx, my);
        } else if ((flags & FULLSCREEN) != 0) {
            setFullscreen(true);
        } else {
            windowPosX = (videoMode.width() - width) / 2;
            windowPosY = (videoMode.height() - height) / 2;
            GLFW.glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        }

        setMouseVisible(true);
        glfwShowWindow(window);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
    }

    /**
     * Creates all callbacks.
     * @param input The input to use to create callbacks.
     */
    private void createCallbacks(Input input) {
        GLFW.glfwSetKeyCallback(window, input.getKeyboard());
        GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtons());
        GLFW.glfwSetCursorPosCallback(window, input.getMouseMove());
        sizeCallback = new GLFWWindowSizeCallback() {
            public void invoke(long window, int width, int height) {
                Screen.this.width = width;
                Screen.this.height = height;
                isResized = true;
                needsMonitorFit = true;
            }
        };
        GLFW.glfwSetWindowSizeCallback(window, sizeCallback);
    }

    /**
     * Resets the screen. Fails if: screen is inactive, window is 0
     * @return Did it succeed?
     */
    public boolean update() {
        boolean succeed = true;
        if (active) {
            if (needsMonitorFit) {
                needsMonitorFit = false;
                fitWindowToMonitor();
            }
            if (isResized) {
                GL11.glViewport(0, 0, width, height);
                isResized = false;
            }
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            timeThisFrame = System.currentTimeMillis();
            nanoTime = System.nanoTime();
            timeDiff = timeThisFrame - timePreviousFrame;
            nanoDiff = nanoTime - pastNanoTime;
            if (timeDiff != 0) {
                double timeDiffSeconds2 = nanoDiff / 1000000000f;
                FPS = 1 / timeDiffSeconds2;
                timePreviousFrame = timeThisFrame;
                pastNanoTime = nanoTime;
            }
        } else
            succeed = false;

        if (glfwWindowShouldClose(window))
            succeed = false;

        if (window != 0)
            GLFW.glfwPollEvents();
        else
            succeed = false;

        return succeed;
    }

    /**
     * Sets the background color to a value of red, green, and blue.
     * @param red The red value.
     * @param green The green value.
     * @param blue The blue value.
     * @see #setBackground(Color) setBackgroundColor(Color)
     */
    public void setBackground(int red, int green, int blue) {
        bgColor = new Color(red, green, blue);
        glClearColor(red/255f, green/255f, blue/255f, 1f);
    }

    /**
     * Updates the screen's display by swapping the buffers and showing the screen.
     */
    public void display() {
        if (window == 0)
            throw new RuntimeException("An error occurred with the display, as the window wasn't correctly set up.");

        GLFW.glfwSwapBuffers(window);
    }

    /**
     * Sets the screen's background color.
     * @param color The background color.
     * @see #setBackground(int, int, int) setBackgroundColor(int, int, int)
     */
    public void setBackground(Color color) {
        glClearColor(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
        bgColor = color;
    }

    /**
     * Checks if the screen is fullscreen.
     * @return Boolean showing whether the screen is fullscreen.
     */
    public boolean isFullscreen() {
        return isFullscreen;
    }
    public void setFullscreen(boolean fullscreen) {
        isFullscreen = fullscreen;
        isResized = true;
        if (fullscreen)
            GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
        else
            GLFW.glfwSetWindowMonitor(window, 0, windowPosX, windowPosY, width, height, 0);
    }

    /**
     * Resizes the window to fill whichever monitor its center is currently on,
     * and moves it onto that monitor. Called whenever monitors are added/removed
     * or the window changes position, so the window always matches the aspect
     * ratio of the monitor it is displayed on.
     */
    public void fitWindowToMonitor() {
        if (window == 0)
            return;
        PointerBuffer monitors = GLFW.glfwGetMonitors();
        if (monitors.capacity() == 0)
            return;
        int cx, cy, wx, wy;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            GLFW.glfwGetWindowPos(window, x, y);
            wx = x.get(0);
            wy = y.get(0);
            cx = wx + width / 2;
            cy = wy + height / 2;
        }
        for (int i = 0; i < monitors.capacity(); ++i) {
            long monitor = monitors.get(i);
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer x = stack.mallocInt(1);
                IntBuffer y = stack.mallocInt(1);
                GLFW.glfwGetMonitorPos(monitor, x, y);
                GLFWVidMode mode = GLFW.glfwGetVideoMode(monitor);
                if (mode == null)
                    continue;
                if (cx < x.get(0) || cx >= x.get(0) + mode.width())
                    continue;
                if (cy < y.get(0) || cy >= y.get(0) + mode.height())
                    continue;
                if (wx != x.get(0) || wy != y.get(0))
                    glfwSetWindowPos(window, x.get(0), y.get(0));
                if (width != mode.width() || height != mode.height()) {
                    glfwSetWindowSize(window, mode.width(), mode.height());
                    width = mode.width();
                    height = mode.height();
                    isResized = true;
                }
                return;
            }
        }
    }
    public boolean isResized() {
        return isResized;
    }
    public void setResized(boolean resized) {
        isResized = resized;
    }

    public double getFPS() {
        return FPS;
    }

    public long getFrameTimeDifference() {
        return timeDiff;
    }

    public long getTimeThisFrame() {
        return timeThisFrame;
    }
    public void destroy() {
        GLFW.glfwDestroyWindow(this.window);
        active = false;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(window, title);
        this.title = title;
    }

    public void setMouseVisible(boolean visible) {
        glfwSetInputMode(window, GLFW_CURSOR, visible ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
    }
    public float getAspectRatio() {
        return (float)getWidth()/(float)getHeight();
    }
    public void maximize() {
        GLFW.glfwMaximizeWindow(window);
    }
    private static ByteBuffer convertToByteBuffer(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        // Allocate 4 bytes per pixel (RGBA)
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];

                // Unpack ARGB into RGBA bytes
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                buffer.put((byte) (pixel & 0xFF));         // Blue
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
            }
        }

        // Reset position to 0 so it can be read from the start
        buffer.flip();

        return buffer;
    }
    public void setCursor(BufferedImage image, Vector2i hotspot) {
        try (GLFWImage imageStruct = GLFWImage.malloc()) {
            imageStruct.set(image.getWidth(), image.getHeight(), convertToByteBuffer(image));
            long cursor = GLFW.glfwCreateCursor(imageStruct, hotspot.x, hotspot.y);
            if (cursor != 0)
                GLFW.glfwSetCursor(window, cursor);
        }
    }
}
package engine.core.shaders;

import engine.core.textures.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Shader {
    protected final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;
    public final int transformLocation;
    public final int texCoordsLocation;
    public final int textureLocation;
    public final int colorLocation;
    public final int lightLevelLocation;
    public final int lightLevelsLocation;
    private static final float[] matrixData = new float[16];

    public Shader(String vertexFile, String fragmentFile) {
        System.out.println("Compiling " + vertexFile);
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        System.out.println("Compilation successful!");
        System.out.println("Compiling " + fragmentFile);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttribute(0, "position");
        bindAttribute(1, "texCoords");
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        transformLocation = getLocation("transform");
        colorLocation = getLocation("color");
        texCoordsLocation = getLocation("texCoordsMultiplier");
        textureLocation = getLocation("tex");
        lightLevelLocation = getLocation("lightLevel");
        lightLevelsLocation = getLocation("lightLevels");
        if (transformLocation == colorLocation || transformLocation == texCoordsLocation || transformLocation == textureLocation || colorLocation == texCoordsLocation || colorLocation == textureLocation || texCoordsLocation == textureLocation) {
            System.err.println("Warning: Shader has a location that is used by more than one uniform!");
        }
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            throw new RuntimeException();
        }
        return shaderID;
    }

    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    public void bind() {
        GL30.glUseProgram(programID);
        GL30.glUniform1i(textureLocation, 0);
    }

    public void unbind() {
        GL30.glUseProgram(0);
    }

    public void loadMatrix(int location, Matrix4f matrix) {
        matrix.get(matrixData);
        GL46.glUniformMatrix4fv(location, false, matrixData);
    }

    public void loadColor(int location, Color color) {
        GL46.glUniform4f(location, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    public int getLocation(String location) {
        return GL46.glGetUniformLocation(programID, location);
    }

    public void loadVec2(int location, Vector2f vector) {
        GL46.glUniform2f(location, vector.x, vector.y);
    }

    public void loadVec2(int location, Vector2i vector) {
        GL46.glUniform2f(location, vector.x, vector.y);
    }

    public void loadFloat(int location, float value) {
        GL46.glUniform1f(location, value);
    }

    public void loadLightLevels(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        GL46.glUniform4f(lightLevelsLocation, topLeft, topRight, bottomRight, bottomLeft);
    }

    public void loadTexCoords(Texture texture, int location) {
        if (texture == null) {
            GL46.glUniform4f(location, 0, 0, 0, 0);
            return;
        }
        float[] dim = texture.getDimensions();
        GL46.glUniform4f(location, dim[0], dim[1], dim[2], dim[3]);
    }

    public void destroy() {
        unbind();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Shader)) return false;
        if (!super.equals(other)) return false;
        var otherCast = (Shader) other;
        if (this.programID != otherCast.programID) return false;
        if (this.vertexShaderID != otherCast.vertexShaderID) return false;
        if (this.fragmentShaderID != otherCast.fragmentShaderID) return false;
        if (this.transformLocation != otherCast.transformLocation) return false;
        if (this.texCoordsLocation != otherCast.texCoordsLocation) return false;
        if (this.textureLocation != otherCast.textureLocation) return false;
        return this.colorLocation == otherCast.colorLocation;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Integer.hashCode(programID);
        result = 31 * result + Integer.hashCode(vertexShaderID);
        result = 31 * result + Integer.hashCode(fragmentShaderID);
        result = 31 * result + Integer.hashCode(transformLocation);
        result = 31 * result + Integer.hashCode(texCoordsLocation);
        result = 31 * result + Integer.hashCode(textureLocation);
        result = 31 * result + Integer.hashCode(colorLocation);
        return result;
    }

    public int getProgramID() {
        return this.programID;
    }

    public int getVertexShaderID() {
        return this.vertexShaderID;
    }

    public int getFragmentShaderID() {
        return this.fragmentShaderID;
    }

    @Override
    public String toString() {
        return "Shader[" + "programID=" + programID + ", " + "vertexShaderID=" + vertexShaderID + ", " + "fragmentShaderID=" + fragmentShaderID + ", " + "transformLocation=" + transformLocation + ", " + "texCoordsLocation=" + texCoordsLocation + ", " + "textureLocation=" + textureLocation + ", " + "colorLocation=" + colorLocation + "]";
    }
}

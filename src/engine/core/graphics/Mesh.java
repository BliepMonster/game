package engine.core.graphics;

import engine.core.Texture;
import engine.core.shaders.Shader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL13;

import java.awt.*;

import static engine.core.DrawManager.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh implements AutoCloseable {
    private final float[] vertices;
    private final float[] texCoords;
    private int VAO;
    private int VBO, TBO;

    public Mesh(float[] vertices, float[] texCoords) {
        this.texCoords = texCoords;
        this.vertices = vertices;
    }

    public Mesh create() {
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);
        setLastBoundVAO(VAO);
        VBO = glGenBuffers();
        TBO = glGenBuffers();
        storeDataInAttributeList(0, vertices, VBO);
        storeDataInAttributeList(1, texCoords, TBO);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        return this;
    }

    public void destroy() {
        glDeleteBuffers(VBO);
        glDeleteBuffers(TBO);
        glDeleteVertexArrays(VAO);
        if (getLastBoundVAO() == VAO) {
            setLastBoundVAO(-1);
        }
    }

    public int getVAO() {
        return VAO;
    }

    public float[] getTexCoords() {
        return texCoords;
    }

    public float[] getVertices() {
        return vertices;
    }

    public int getVBO() {
        return VBO;
    }

    public int getTBO() {
        return TBO;
    }

    public void render(Texture texture, Shader shader, Color color, Matrix4f transform) {
        bindShader(shader);

        shader.loadMatrix(shader.transformLocation, transform);
        shader.loadColor(shader.colorLocation, color);
        if (getLastBoundVAO() != VAO) {
            glBindVertexArray(VAO);
            setLastBoundVAO(VAO);
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        swapTexture(texture);
        shader.loadTexCoords(texture, shader.texCoordsLocation);
        glDrawArrays(GL_TRIANGLES, 0, vertices.length / 2);
    }

    public void close() {
        destroy();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (other.getClass() != getClass()) return false;
        var otherCast = (Mesh) other;
        if (!java.util.Arrays.equals(this.vertices, otherCast.vertices)) return false;
        if (!java.util.Arrays.equals(this.texCoords, otherCast.texCoords)) return false;
        if (this.VAO != otherCast.VAO) return false;
        if (this.VBO != otherCast.VBO) return false;
        if (this.TBO != otherCast.TBO) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + java.util.Arrays.hashCode(vertices);
        result = 31 * result + java.util.Arrays.hashCode(texCoords);
        result = 31 * result + Integer.hashCode(VAO);
        result = 31 * result + Integer.hashCode(VBO);
        result = 31 * result + Integer.hashCode(TBO);
        return result;
    }

    @Override
    public String toString() {
        return "Mesh[" + "vertices=" + java.util.Arrays.toString(vertices) + ", " + "texCoords=" + java.util.Arrays.toString(texCoords) + ", " + "VAO=" + VAO + ", " + "VBO=" + VBO + ", " + "TBO=" + TBO + "]";
    }
}

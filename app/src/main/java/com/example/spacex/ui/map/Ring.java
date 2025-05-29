package com.example.spacex.ui.map;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class Ring {
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureCoordBuffer;
    private int textureId;
    private int program;

    private int positionHandle;
    private int textureCoordHandle;
    private int mvpMatrixHandle;
    private int textureHandle;

    private final int vertexCount = 4;
    private final int vertexStride = 3 * 4;
    private final int textureCoordStride = 2 * 4;

    public Ring(float innerRadius, float outerRadius, int textureId) {
        this.textureId = textureId;

        float[] vertices = {
                -outerRadius, 0, -outerRadius,
                outerRadius, 0, -outerRadius,
                -outerRadius, 0, outerRadius,
                outerRadius, 0, outerRadius
        };

        float[] textureCoords = {
                0, 0,
                1, 0,
                0, 1,
                1, 1
        };

        vertexBuffer = createFloatBuffer(vertices);
        textureCoordBuffer = createFloatBuffer(textureCoords);

        initShaders();
    }

    private FloatBuffer createFloatBuffer(float[] array) {
        FloatBuffer buffer = ByteBuffer.allocateDirect(array.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(array).position(0);
        return buffer;
    }

    private void initShaders() {
        String vertexShaderCode =
                "uniform mat4 uMVPMatrix;\n" +
                        "attribute vec4 aPosition;\n" +
                        "attribute vec2 aTexCoord;\n" +
                        "varying vec2 vTexCoord;\n" +
                        "void main() {\n" +
                        "  gl_Position = uMVPMatrix * aPosition;\n" +
                        "  vTexCoord = aTexCoord;\n" +
                        "}\n";

        String fragmentShaderCode =
                "precision highp float;\n" +
                        "varying vec2 vTexCoord;\n" +
                        "uniform sampler2D uTexture;\n" +
                        "void main() {\n" +
                        "  vec4 texColor = texture2D(uTexture, vTexCoord);\n" +
                        "  if (texColor.a < 0.1) discard;\n" +
                        "  gl_FragColor = texColor;\n" +
                        "}\n";

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        textureCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoord");
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        textureHandle = GLES20.glGetUniformLocation(program, "uTexture");
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(textureHandle, 0);

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        GLES20.glEnableVertexAttribArray(textureCoordHandle);
        GLES20.glVertexAttribPointer(textureCoordHandle, 2, GLES20.GL_FLOAT, false, textureCoordStride, textureCoordBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(textureCoordHandle);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        if (shader == 0) throw new RuntimeException("Error creating shader");

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            String log = GLES20.glGetShaderInfoLog(shader);
            GLES20.glDeleteShader(shader);
            throw new RuntimeException("Shader compilation failed:\n" + log);
        }
        return shader;
    }
}
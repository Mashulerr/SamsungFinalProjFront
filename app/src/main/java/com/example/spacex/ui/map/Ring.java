package com.example.spacex.ui.map;

import android.opengl.GLES32;

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

        int vertexShader = loadShader(GLES32.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES32.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES32.glCreateProgram();
        GLES32.glAttachShader(program, vertexShader);
        GLES32.glAttachShader(program, fragmentShader);
        GLES32.glLinkProgram(program);

        positionHandle = GLES32.glGetAttribLocation(program, "aPosition");
        textureCoordHandle = GLES32.glGetAttribLocation(program, "aTexCoord");
        mvpMatrixHandle = GLES32.glGetUniformLocation(program, "uMVPMatrix");
        textureHandle = GLES32.glGetUniformLocation(program, "uTexture");
    }

    public void draw(float[] mvpMatrix) {
        GLES32.glUseProgram(program);

        GLES32.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        GLES32.glActiveTexture(GLES32.GL_TEXTURE0);
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureId);
        GLES32.glUniform1i(textureHandle, 0);

        GLES32.glEnableVertexAttribArray(positionHandle);
        GLES32.glVertexAttribPointer(positionHandle, 3, GLES32.GL_FLOAT, false, vertexStride, vertexBuffer);

        GLES32.glEnableVertexAttribArray(textureCoordHandle);
        GLES32.glVertexAttribPointer(textureCoordHandle, 2, GLES32.GL_FLOAT, false, textureCoordStride, textureCoordBuffer);

        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, 0, vertexCount);

        GLES32.glDisableVertexAttribArray(positionHandle);
        GLES32.glDisableVertexAttribArray(textureCoordHandle);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES32.glCreateShader(type);
        if (shader == 0) throw new RuntimeException("Error creating shader");

        GLES32.glShaderSource(shader, shaderCode);
        GLES32.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES32.glGetShaderiv(shader, GLES32.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            String log = GLES32.glGetShaderInfoLog(shader);
            GLES32.glDeleteShader(shader);
            throw new RuntimeException("Shader compilation failed:\n" + log);
        }
        return shader;
    }
}
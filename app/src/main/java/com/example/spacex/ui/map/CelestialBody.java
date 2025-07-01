package com.example.spacex.ui.map;


import android.opengl.GLES32;

public class CelestialBody {
    private float[] modelMatrix = new float[16];
    private float radius;
    private Sphere sphere;
    private int textureId;
    private float[] emissionColor;
    private boolean isSelected;
    private float selectionPulse;
    private int shaderProgram;

    public CelestialBody(float radius, int textureId, float[] emissionColor) {
        this.radius = radius;
        this.textureId = textureId;
        this.emissionColor = emissionColor;
        this.sphere = new Sphere(radius, 32, 32);
        initShaders();
    }

    private void initShaders() {
        String vertexShaderCode =
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 aPosition;" +
                        "attribute vec2 aTexCoord;" +
                        "varying vec2 vTexCoord;" +
                        "void main() {" +
                        "  gl_Position = uMVPMatrix * aPosition;" +
                        "  vTexCoord = aTexCoord;" +
                        "}";

        String fragmentShaderCode =
                "precision highp float;" +
                        "uniform sampler2D uTexture;" +
                        "uniform vec4 uEmissionColor;" +
                        "uniform float uSelectionPulse;" +
                        "uniform bool uIsSelected;" +
                        "varying vec2 vTexCoord;" +
                        "void main() {" +
                        "  vec4 texColor = texture2D(uTexture, vTexCoord);" +
                        "  if (uIsSelected) {" +
                        "    float edge = clamp(uSelectionPulse - length(vTexCoord - vec2(0.5)), 0.0, 1.0);" +
                        "    texColor.rgb = mix(texColor.rgb, vec3(1.0), edge * 0.7);" +
                        "  }" +
                        "  texColor.rgb += uEmissionColor.rgb * uEmissionColor.a;" +
                        "  gl_FragColor = texColor;" +
                        "}";

        int vertexShader = loadShader(GLES32.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES32.GL_FRAGMENT_SHADER, fragmentShaderCode);

        shaderProgram = GLES32.glCreateProgram();
        GLES32.glAttachShader(shaderProgram, vertexShader);
        GLES32.glAttachShader(shaderProgram, fragmentShader);
        GLES32.glLinkProgram(shaderProgram);
    }

    public void draw(float[] mvpMatrix) {
        GLES32.glUseProgram(shaderProgram);

        int mvpHandle = GLES32.glGetUniformLocation(shaderProgram, "uMVPMatrix");
        GLES32.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0);

        int texHandle = GLES32.glGetUniformLocation(shaderProgram, "uTexture");
        GLES32.glActiveTexture(GLES32.GL_TEXTURE0);
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, textureId);
        GLES32.glUniform1i(texHandle, 0);

        int emissionHandle = GLES32.glGetUniformLocation(shaderProgram, "uEmissionColor");
        GLES32.glUniform4fv(emissionHandle, 1, emissionColor, 0);

        int selectionHandle = GLES32.glGetUniformLocation(shaderProgram, "uIsSelected");
        GLES32.glUniform1i(selectionHandle, isSelected ? 1 : 0);

        int pulseHandle = GLES32.glGetUniformLocation(shaderProgram, "uSelectionPulse");
        GLES32.glUniform1f(pulseHandle, selectionPulse);

        sphere.draw(shaderProgram);
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if (!selected) selectionPulse = 0f;
    }

    public float[] getPositionWorld() {
        return new float[]{modelMatrix[12], modelMatrix[13], modelMatrix[14]};
    }

    public void setModelMatrix(float[] matrix) {
        System.arraycopy(matrix, 0, modelMatrix, 0, 16);
    }

    public float getRadius() {
        return radius;
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES32.glCreateShader(type);
        GLES32.glShaderSource(shader, shaderCode);
        GLES32.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES32.glGetShaderiv(shader, GLES32.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            String error = GLES32.glGetShaderInfoLog(shader);
            GLES32.glDeleteShader(shader);
            throw new RuntimeException("Shader compilation error: " + error);
        }
        return shader;
    }
}
package com.example.spacex.ui.map;


import android.opengl.GLES20;


public class CelestialBody {
    private static final String TAG = "CelestialBody";

    private float[] modelMatrix = new float[16];
    private float radius;


    private Sphere sphere;



    private int textureId;
    private float[] emissionColor;
    private boolean hasAtmosphere;
    private boolean hasClouds;
    private boolean isLightSource;


    private float rotationAngle = 0f;
    private float rotationSpeed = 1f;
    private float cloudRotationAngle = 0f;
    private float cloudRotationSpeed = 0.8f;


    private boolean isSelected = false;
    private float selectionPulse = 0f;
    private float pulseSpeed = 0.05f;


    private int shaderProgram;
    private int vertexShader;
    private int fragmentShader;

    public CelestialBody(float radius, int textureId, float[] emissionColor,
                         boolean hasAtmosphere, boolean hasClouds, boolean isLightSource) {
        this.radius = radius;
        this.textureId = textureId;
        this.emissionColor = emissionColor;
        this.hasAtmosphere = hasAtmosphere;
        this.hasClouds = hasClouds;
        this.isLightSource = isLightSource;

        this.sphere = new Sphere(radius, 32, 32);

        initShaders();
    }

    private void initShaders() {

        String vertexShaderCode =
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 aPosition;" +
                        "attribute vec2 aTexCoord;" +
                        "attribute vec3 aNormal;" +
                        "varying vec2 vTexCoord;" +
                        "varying vec3 vNormal;" +
                        "varying vec3 vPosition;" +
                        "void main() {" +
                        "  gl_Position = uMVPMatrix * aPosition;" +
                        "  vTexCoord = aTexCoord;" +
                        "  vNormal = aNormal;" +
                        "  vPosition = vec3(aPosition);" +
                        "}";


        String fragmentShaderCode =
                "precision highp float;" +
                        "uniform sampler2D uTexture;" +
                        "uniform vec4 uEmissionColor;" +
                        "uniform float uSelectionPulse;" +
                        "uniform bool uIsSelected;" +
                        "uniform bool uIsLightSource;" +
                        "varying vec2 vTexCoord;" +
                        "varying vec3 vNormal;" +
                        "varying vec3 vPosition;" +
                        "void main() {" +
                        "  vec4 texColor = texture2D(uTexture, vTexCoord);" +


                        "  if (uIsSelected) {" +
                        "    float edge = clamp(uSelectionPulse - length(vTexCoord - vec2(0.5)), 0.0, 1.0);" +
                        "    texColor.rgb = mix(texColor.rgb, vec3(1.0, 1.0, 1.0), edge * 0.7);" +
                        "  }" +


                        "  if (uEmissionColor.a > 0.0) {" +
                        "    texColor.rgb += uEmissionColor.rgb * uEmissionColor.a;" +
                        "  }" +


                        "  if (!uIsLightSource) {" +
                        "    float diffuse = max(dot(normalize(vNormal), vec3(0.0, 1.0, 0.0)), 0.2);" +
                        "    texColor.rgb *= diffuse;" +
                        "  }" +

                        "  gl_FragColor = texColor;" +
                        "}";
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);
    }



    public void update() {

        rotationAngle += rotationSpeed;
        if (rotationAngle >= 360f) rotationAngle -= 360f;

        if (hasClouds) {
            cloudRotationAngle += cloudRotationSpeed;
            if (cloudRotationAngle >= 360f) cloudRotationAngle -= 360f;
        }


        if (isSelected) {
            selectionPulse += pulseSpeed;
            if (selectionPulse > 1f || selectionPulse < 0f) {
                pulseSpeed = -pulseSpeed;
            }
        } else {
            selectionPulse = 0f;
        }
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(shaderProgram);

        int lightSourceHandle = GLES20.glGetUniformLocation(shaderProgram, "uIsLightSource");
        GLES20.glUniform1i(lightSourceHandle, isLightSource ? 1 : 0);


        int mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);


        int textureHandle = GLES20.glGetUniformLocation(shaderProgram, "uTexture");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(textureHandle, 0);


        int emissionHandle = GLES20.glGetUniformLocation(shaderProgram, "uEmissionColor");
        GLES20.glUniform4fv(emissionHandle, 1, emissionColor, 0);


        int selectionHandle = GLES20.glGetUniformLocation(shaderProgram, "uIsSelected");
        GLES20.glUniform1i(selectionHandle, isSelected ? 1 : 0);

        int pulseHandle = GLES20.glGetUniformLocation(shaderProgram, "uSelectionPulse");
        GLES20.glUniform1f(pulseHandle, selectionPulse);


        sphere.draw(shaderProgram);
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if (!selected) {
            selectionPulse = 0f;
        }
    }

    public float[] getPositionWorld() {

        return new float[] {
                modelMatrix[12],
                modelMatrix[13],
                modelMatrix[14]
        };
    }



    public void setModelMatrix(float[] matrix) {
        System.arraycopy(matrix, 0, modelMatrix, 0, 16);
    }


    public float getRadius() {
        return radius;
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        if (shader == 0) {
            throw new RuntimeException("Error creating shader");
        }

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            String error = GLES20.glGetShaderInfoLog(shader);
            GLES20.glDeleteShader(shader);
            throw new RuntimeException("Shader compilation error: " + error);
        }

        return shader;
    }

}
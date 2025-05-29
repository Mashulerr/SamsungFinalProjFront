package com.example.spacex.ui.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.example.spacex.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SolarSystemRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "SolarSystemRenderer";


    private int starTextureId;
    private FloatBuffer starVertexBuffer;
    private FloatBuffer starTextureCoordBuffer;
    private int starProgram;


    private final float FRICTION = 0.98f;
    private final float ROTATION_SPEED = 0.5f;


    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] mvpMatrix = new float[16];
    private final float[] tempMatrix = new float[16];
    private final float[] pickMatrix = new float[16];


    private CelestialBody sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune;
    private Ring saturnRing;
    private Map<String, CelestialBody> planets = new HashMap<>();
    private String selectedPlanet = null;


    private float[] cameraPosition = {0f, 0f, 15f};
    private float[] lookAt = {0f, 0f, 0f};
    private float[] upVector = {0f, 1f, 0f};


    private float scaleFactor = 1f;
    private boolean autoRotation = true;
    private float userRotationX = 0;
    private float userRotationY = 0;
    private float lastTouchX, lastTouchY;
    private float autoRotationAngle = 0f;
    private boolean isUserInteracting = false;

    private final Context context;
    private int[] viewport = new int[4];

    public SolarSystemRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);

        initCelestialBodies();
        initPlanetMap();
        initStarBackground();
    }

    private void initCelestialBodies() {
        sun = new CelestialBody(1.5f, loadTexture(R.drawable.sun_texture),
                new float[]{1.0f, 0.8f, 0.5f, 0.8f}, false, true, true);
        mercury = new CelestialBody(0.2f, loadTexture(R.drawable.mercury_texture), new float[]{0.7f, 0.7f, 0.7f, 0.0f}, false, false, false);
        venus = new CelestialBody(0.3f, loadTexture(R.drawable.venus_texture), new float[]{0.9f, 0.6f, 0.4f, 0.3f}, true, false, false);
        earth = new CelestialBody(0.3f, loadTexture(R.drawable.earth_texture), new float[]{0.2f, 0.4f, 0.8f, 0.5f}, true, true, false);
        mars = new CelestialBody(0.25f, loadTexture(R.drawable.mars_texture), new float[]{0.8f, 0.4f, 0.3f, 0.2f}, true, true, false);
        jupiter = new CelestialBody(0.7f, loadTexture(R.drawable.jupiter_texture), new float[]{0.8f, 0.6f, 0.4f, 0.4f}, true, true, false);
        saturn = new CelestialBody(0.6f, loadTexture(R.drawable.saturn_texture), new float[]{0.9f, 0.8f, 0.6f, 0.3f}, true, true, false);
        saturnRing = new Ring(0.8f, 1.4f, loadTexture(R.drawable.saturn_rings_texture));
        uranus = new CelestialBody(0.4f, loadTexture(R.drawable.uranus_texture), new float[]{0.4f, 0.7f, 0.8f, 0.5f}, true, true, false);
        neptune = new CelestialBody(0.4f, loadTexture(R.drawable.neptune_texture), new float[]{0.1f, 0.1f, 0.8f, 0.6f}, true, true, false);
    }

    private void initPlanetMap() {
        planets.put("Sun", sun);
        planets.put("Mercury", mercury);
        planets.put("Venus", venus);
        planets.put("Earth", earth);
        planets.put("Mars", mars);
        planets.put("Jupiter", jupiter);
        planets.put("Saturn", saturn);
        planets.put("Uranus", uranus);
        planets.put("Neptune", neptune);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        viewport[0] = 0;
        viewport[1] = 0;
        viewport[2] = width;
        viewport[3] = height;

        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 100);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);


        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        drawStarBackground();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        if (autoRotation) {
            autoRotationAngle += 0.2f;
            if (autoRotationAngle >= 360) autoRotationAngle -= 360;
        }


        Matrix.setLookAtM(viewMatrix, 0, cameraPosition[0], cameraPosition[1], cameraPosition[2], lookAt[0], lookAt[1], lookAt[2], upVector[0], upVector[1], upVector[2]);

        Matrix.rotateM(viewMatrix, 0, userRotationY, 0, 1, 0);
        Matrix.rotateM(viewMatrix, 0, userRotationX, 1, 0, 0);


        drawCelestialBody(sun, 0, 0, 0);
        drawCelestialBody(mercury, 2.5f, 4.0f, 0);
        drawCelestialBody(venus, 3.5f, 2.5f, 0);
        drawCelestialBody(earth, 4.5f, 2.0f, 0);
        drawCelestialBody(mars, 5.5f, 1.5f, 0);
        drawCelestialBody(jupiter, 7.0f, 1.0f, 0);
        drawSaturnWithRings(9.0f, 0.8f);
        drawCelestialBody(uranus, 11.0f, 0.6f, 0);
        drawCelestialBody(neptune, 13.0f, 0.5f, 0);
    }



    private void drawCelestialBody(CelestialBody body, float distance, float speed, float tilt) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.scaleM(modelMatrix, 0, scaleFactor, scaleFactor, scaleFactor);

        if (autoRotation) {
            Matrix.rotateM(modelMatrix, 0, autoRotationAngle * speed, 0, 1, 0);
        }

        if (tilt != 0) {
            Matrix.rotateM(modelMatrix, 0, tilt, 0, 0, 1);
        }

        Matrix.translateM(modelMatrix, 0, distance, 0, 0);

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);


        body.setSelected(body == planets.get(selectedPlanet));
        body.update();
        body.draw(mvpMatrix);
    }

    private void drawSaturnWithRings(float distance, float speed) {

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.scaleM(modelMatrix, 0, scaleFactor, scaleFactor, scaleFactor);

        if (autoRotation) {
            Matrix.rotateM(modelMatrix, 0, autoRotationAngle * speed, 0, 1, 0);
        }

        Matrix.translateM(modelMatrix, 0, distance, 0, 0);

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);

        saturn.setSelected("Saturn".equals(selectedPlanet));
        saturn.update();
        saturn.draw(mvpMatrix);


        Matrix.setIdentityM(tempMatrix, 0);
        Matrix.scaleM(tempMatrix, 0, scaleFactor, scaleFactor, scaleFactor);

        if (autoRotation) {
            Matrix.rotateM(tempMatrix, 0, autoRotationAngle * speed, 0, 1, 0);
        }

        Matrix.translateM(tempMatrix, 0, distance, 0, 0);
        Matrix.rotateM(tempMatrix, 0, 26.7f, 1, 0, 0); // Наклон колец

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, tempMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0);

        saturnRing.draw(mvpMatrix);
    }

    public String checkPlanetSelection(float x, float y) {
        float[] ray = convertScreenToWorldRay(x, y);
        String closestPlanet = null;
        float closestDistance = Float.MAX_VALUE;

        for (Map.Entry<String, CelestialBody> entry : planets.entrySet()) {
            float distance = checkRaySphereIntersection(ray, entry.getValue());
            if (distance > 0 && distance < closestDistance) {
                closestDistance = distance;
                closestPlanet = entry.getKey();
            }
        }

        selectedPlanet = closestPlanet;
        return selectedPlanet;
    }

    private float[] convertScreenToWorldRay(float x, float y) {
        float[] normalizedCoords = new float[4];
        float[] rayEye = new float[4];
        float[] rayWorld = new float[4];


        normalizedCoords[0] = (2.0f * x) / viewport[2] - 1.0f;
        normalizedCoords[1] = 1.0f - (2.0f * y) / viewport[3];
        normalizedCoords[2] = -1.0f; // Ближняя плоскость
        normalizedCoords[3] = 1.0f;

        float[] inverseProjection = new float[16];
        float[] inverseView = new float[16];

        Matrix.invertM(inverseProjection, 0, projectionMatrix, 0);
        Matrix.invertM(inverseView, 0, viewMatrix, 0);


        Matrix.multiplyMV(rayEye, 0, inverseProjection, 0, normalizedCoords, 0);
        rayEye[2] = -1.0f;
        rayEye[3] = 0.0f;


        Matrix.multiplyMV(rayWorld, 0, inverseView, 0, rayEye, 0);


        float length = (float) Math.sqrt(rayWorld[0]*rayWorld[0] + rayWorld[1]*rayWorld[1] + rayWorld[2]*rayWorld[2]);
        rayWorld[0] /= length;
        rayWorld[1] /= length;
        rayWorld[2] /= length;


        return new float[] {
                cameraPosition[0], cameraPosition[1], cameraPosition[2],
                rayWorld[0], rayWorld[1], rayWorld[2]
        };
    }

    private float checkRaySphereIntersection(float[] ray, CelestialBody body) {

        float[] planetPos = body.getPositionWorld();
        float radius = body.getRadius() * scaleFactor;

        float[] L = new float[] {
                planetPos[0] - ray[0],
                planetPos[1] - ray[1],
                planetPos[2] - ray[2]
        };

        float tca = L[0]*ray[3] + L[1]*ray[4] + L[2]*ray[5];
        if (tca < 0) return -1;

        float d2 = (L[0]*L[0] + L[1]*L[1] + L[2]*L[2]) - tca*tca;
        float radius2 = radius * radius;
        if (d2 > radius2) return -1;

        float thc = (float) Math.sqrt(radius2 - d2);
        float t0 = tca - thc;
        float t1 = tca + thc;

        if (t0 < 0) t0 = t1;
        if (t0 < 0) return -1;

        return t0;
    }


    public void updateUserRotation(float dx, float dy) {


        userRotationY += dx * 0.5f;
        userRotationX += dy * 0.5f;
        userRotationX = Math.max(-90, Math.min(90, userRotationX));
        isUserInteracting = true;
    }

    public void handlePinchZoom(float scaleFactorDelta) {
        scaleFactor *= scaleFactorDelta;
        scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
        isUserInteracting = true;
    }




    private void initStarBackground() {
        starTextureId = loadTexture(R.drawable.star_texture);

        float[] vertices = {
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
                1.0f,  1.0f, -1.0f
        };

        float[] textureCoords = {
                0.0f, 0.0f,
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f
        };

        starVertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        starVertexBuffer.put(vertices).position(0);

        starTextureCoordBuffer = ByteBuffer.allocateDirect(textureCoords.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        starTextureCoordBuffer.put(textureCoords).position(0);

        String vertexShaderCode =
                "attribute vec4 aPosition;\n" +
                        "attribute vec2 aTexCoord;\n" +
                        "varying vec2 vTexCoord;\n" +
                        "void main() {\n" +
                        "  gl_Position = aPosition;\n" +
                        "  vTexCoord = aTexCoord;\n" +
                        "}";

        String fragmentShaderCode =
                "precision mediump float;\n" +
                        "varying vec2 vTexCoord;\n" +
                        "uniform sampler2D uTexture;\n" +
                        "void main() {\n" +
                        "  gl_FragColor = texture2D(uTexture, vTexCoord);\n" +
                        "}";

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        starProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(starProgram, vertexShader);
        GLES20.glAttachShader(starProgram, fragmentShader);
        GLES20.glLinkProgram(starProgram);
    }

    private void drawStarBackground() {
        GLES20.glUseProgram(starProgram);

        int positionHandle = GLES20.glGetAttribLocation(starProgram, "aPosition");
        int texCoordHandle = GLES20.glGetAttribLocation(starProgram, "aTexCoord");
        int textureHandle = GLES20.glGetUniformLocation(starProgram, "uTexture");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, starTextureId);
        GLES20.glUniform1i(textureHandle, 0);

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, starVertexBuffer);

        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, starTextureCoordBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
    }


    private int loadTexture(int resourceId) {
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            GLES20.glDeleteTextures(1, textureHandle, 0);
            throw new RuntimeException("Failed to decode texture resource.");
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        return textureHandle[0];
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
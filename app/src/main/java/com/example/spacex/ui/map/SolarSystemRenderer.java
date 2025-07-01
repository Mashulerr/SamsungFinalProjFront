package com.example.spacex.ui.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES32;
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
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] mvpMatrix = new float[16];
    private final float[] tempMatrix = new float[16];

    private int backgroundTextureId;
    private int backgroundShaderProgram;
    private FloatBuffer backgroundVertexBuffer;
    private FloatBuffer backgroundTexCoordBuffer;

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
    private float autoRotationAngle = 0f;

    private final Context context;
    private int[] viewport = new int[4];

    public SolarSystemRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES32.glClearColor(0,0,0,1);
        GLES32.glEnable(GLES32.GL_DEPTH_TEST);
        GLES32.glEnable(GLES32.GL_BLEND);
        GLES32.glBlendFunc(GLES32.GL_SRC_ALPHA, GLES32.GL_ONE_MINUS_SRC_ALPHA);
        GLES32.glDisable(GLES32.GL_CULL_FACE);

        initBackground();
        initCelestialBodies();
        initPlanetMap();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES32.glViewport(0,0,width,height);
        viewport[0]=0; viewport[1]=0; viewport[2]=width; viewport[3]=height;

        float ratio = (float)width/height;
        Matrix.frustumM(projectionMatrix,0,-ratio,ratio,-1,1,3,100);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);

        if (autoRotation) {
            autoRotationAngle += 0.2f;
            if (autoRotationAngle >= 360) autoRotationAngle -= 360;
        }

        Matrix.setLookAtM(viewMatrix,0,
                cameraPosition[0],cameraPosition[1],cameraPosition[2],
                lookAt[0],lookAt[1],lookAt[2],
                upVector[0],upVector[1],upVector[2]);

        Matrix.rotateM(viewMatrix,0,userRotationY,0,1,0);
        Matrix.rotateM(viewMatrix,0,userRotationX,1,0,0);

        drawBackground();

        drawPlanet(sun,0,0,0);
        drawPlanet(mercury,2.5f,4f,0);
        drawPlanet(venus,3.5f,2.5f,0);
        drawPlanet(earth,4.5f,2f,0);
        drawPlanet(mars,5.5f,1.5f,0);
        drawPlanet(jupiter,7f,1f,0);
        drawSaturn(9f,0.8f);
        drawPlanet(uranus,11f,0.6f,0);
        drawPlanet(neptune,13f,0.5f,0);
    }

    private void initBackground() {
        backgroundTextureId = loadTexture(R.drawable.space_background);

        float[] vertices = {
                -1f, -1f, 0f,
                1f, -1f, 0f,
                -1f,  1f, 0f,
                1f,  1f, 0f
        };
        float[] texCoords = {
                0f,1f,
                1f,1f,
                0f,0f,
                1f,0f
        };
        backgroundVertexBuffer = ByteBuffer.allocateDirect(vertices.length*4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        backgroundVertexBuffer.put(vertices).position(0);
        backgroundTexCoordBuffer = ByteBuffer.allocateDirect(texCoords.length*4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        backgroundTexCoordBuffer.put(texCoords).position(0);

        String vShader =
                "attribute vec4 aPosition;" +
                        "attribute vec2 aTexCoord;" +
                        "varying vec2 vTexCoord;" +
                        "void main(){" +
                        "  gl_Position=aPosition;" +
                        "  vTexCoord=aTexCoord;" +
                        "}";
        String fShader =
                "precision mediump float;" +
                        "uniform sampler2D uTexture;" +
                        "varying vec2 vTexCoord;" +
                        "void main(){" +
                        "  gl_FragColor=texture2D(uTexture,vTexCoord);" +
                        "}";

        int vs = loadShader(GLES32.GL_VERTEX_SHADER, vShader);
        int fs = loadShader(GLES32.GL_FRAGMENT_SHADER, fShader);
        backgroundShaderProgram = GLES32.glCreateProgram();
        GLES32.glAttachShader(backgroundShaderProgram,vs);
        GLES32.glAttachShader(backgroundShaderProgram,fs);
        GLES32.glLinkProgram(backgroundShaderProgram);
    }

    private void drawBackground() {
        GLES32.glUseProgram(backgroundShaderProgram);
        int pos = GLES32.glGetAttribLocation(backgroundShaderProgram,"aPosition");
        int tc = GLES32.glGetAttribLocation(backgroundShaderProgram,"aTexCoord");
        GLES32.glEnableVertexAttribArray(pos);
        GLES32.glVertexAttribPointer(pos,3,GLES32.GL_FLOAT,false,0,backgroundVertexBuffer);
        GLES32.glEnableVertexAttribArray(tc);
        GLES32.glVertexAttribPointer(tc,2,GLES32.GL_FLOAT,false,0,backgroundTexCoordBuffer);

        GLES32.glActiveTexture(GLES32.GL_TEXTURE0);
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, backgroundTextureId);
        int tex = GLES32.glGetUniformLocation(backgroundShaderProgram,"uTexture");
        GLES32.glUniform1i(tex,0);

        GLES32.glDisable(GLES32.GL_DEPTH_TEST);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP,0,4);
        GLES32.glEnable(GLES32.GL_DEPTH_TEST);

        GLES32.glDisableVertexAttribArray(pos);
        GLES32.glDisableVertexAttribArray(tc);
    }

    private void initCelestialBodies() {
        sun     = new CelestialBody(1.5f, loadTexture(R.drawable.sun_texture), new float[]{1f,0.8f,0.5f,0.8f});
        mercury = new CelestialBody(0.2f, loadTexture(R.drawable.mercury_texture), new float[]{0.7f,0.7f,0.7f,0f});
        venus   = new CelestialBody(0.3f, loadTexture(R.drawable.venus_texture), new float[]{0.9f,0.6f,0.4f,0.3f});
        earth   = new CelestialBody(0.3f, loadTexture(R.drawable.earth_texture), new float[]{0.2f,0.4f,0.8f,0.5f});
        mars    = new CelestialBody(0.25f,loadTexture(R.drawable.mars_texture), new float[]{0.8f,0.4f,0.3f,0.2f});
        jupiter = new CelestialBody(0.7f, loadTexture(R.drawable.jupiter_texture), new float[]{0.8f,0.6f,0.4f,0.4f});
        saturn  = new CelestialBody(0.6f, loadTexture(R.drawable.saturn_texture), new float[]{0.9f,0.8f,0.6f,0.3f});
        saturnRing = new Ring(0.8f,1.4f, loadTexture(R.drawable.saturn_rings_texture));
        uranus  = new CelestialBody(0.5f, loadTexture(R.drawable.uranus_texture), new float[]{0.5f,0.9f,0.9f,0.4f});
        neptune = new CelestialBody(0.5f, loadTexture(R.drawable.neptune_texture), new float[]{0.3f,0.4f,0.9f,0.6f});
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

    private void drawPlanet(CelestialBody body, float distance, float speed, float tilt) {
        Matrix.setIdentityM(modelMatrix,0);
        Matrix.scaleM(modelMatrix,0,scaleFactor,scaleFactor,scaleFactor);
        if (autoRotation) Matrix.rotateM(modelMatrix,0,autoRotationAngle*speed,0,1,0);
        if (tilt!=0) Matrix.rotateM(modelMatrix,0,tilt,0,0,1);
        Matrix.translateM(modelMatrix,0,distance,0,0);

        Matrix.multiplyMM(mvpMatrix,0,viewMatrix,0,modelMatrix,0);
        Matrix.multiplyMM(mvpMatrix,0,projectionMatrix,0,mvpMatrix,0);

        body.setSelected(body == planets.get(selectedPlanet));
        body.draw(mvpMatrix);
    }

    private void drawSaturn(float distance, float speed) {
        drawPlanet(saturn, distance, speed, 0);

        // draw ring
        Matrix.setIdentityM(tempMatrix,0);
        Matrix.scaleM(tempMatrix,0,scaleFactor,scaleFactor,scaleFactor);
        if (autoRotation) Matrix.rotateM(tempMatrix,0,autoRotationAngle*speed,0,1,0);
        Matrix.translateM(tempMatrix,0,distance,0,0);
        Matrix.rotateM(tempMatrix,0,26.7f,1,0,0);

        Matrix.multiplyMM(mvpMatrix,0,viewMatrix,0,tempMatrix,0);
        Matrix.multiplyMM(mvpMatrix,0,projectionMatrix,0,mvpMatrix,0);

        saturnRing.draw(mvpMatrix);
    }

    public void updateUserRotation(float dx, float dy) {
        userRotationY += dx * 0.5f;
        userRotationX += dy * 0.5f;
        userRotationX = Math.max(-90, Math.min(90, userRotationX));
    }

    public void handlePinchZoom(float factor) {
        scaleFactor *= factor;
        scaleFactor = Math.max(0.1f, Math.min(5f, scaleFactor));
    }

    private int loadTexture(int resId) {
        int[] h = new int[1];
        GLES32.glGenTextures(1,h,0);
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D,h[0]);
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_MIN_FILTER,GLES32.GL_LINEAR_MIPMAP_LINEAR);
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_MAG_FILTER,GLES32.GL_LINEAR);

        Bitmap b = BitmapFactory.decodeResource(context.getResources(),resId);
        GLUtils.texImage2D(GLES32.GL_TEXTURE_2D,0,b,0);
        GLES32.glGenerateMipmap(GLES32.GL_TEXTURE_2D);
        b.recycle();
        return h[0];
    }

    private int loadShader(int type, String code) {
        int s = GLES32.glCreateShader(type);
        GLES32.glShaderSource(s, code);
        GLES32.glCompileShader(s);
        return s;
    }
}

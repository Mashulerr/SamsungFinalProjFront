package com.example.spacex.ui.map;

import android.opengl.GLES32;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Sphere {
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureCoordBuffer;
    private FloatBuffer normalBuffer;
    private ShortBuffer indexBuffer;
    private int numIndices;


    private final int COORDS_PER_VERTEX = 3;
    private final int TEXCOORDS_PER_VERTEX = 2;


    private final int vertexStride = COORDS_PER_VERTEX * 4;
    private final int texCoordStride = TEXCOORDS_PER_VERTEX * 4;

    public Sphere(float radius, int stacks, int slices) {

        float[] vertices = new float[(stacks + 1) * (slices + 1) * 3];
        float[] texCoords = new float[(stacks + 1) * (slices + 1) * 2];
        float[] normals = new float[(stacks + 1) * (slices + 1) * 3];
        short[] indices = new short[stacks * slices * 6];


        int vertexIndex = 0;
        int texCoordIndex = 0;
        int normalIndex = 0;

        for (int i = 0; i <= stacks; ++i) {
            float v = i / (float) stacks;
            float phi = v * (float) Math.PI;

            for (int j = 0; j <= slices; ++j) {
                float u = j / (float) slices;
                float theta = u * (float) (Math.PI * 2);


                float x = (float) (Math.cos(theta) * Math.sin(phi));
                float y = (float) Math.cos(phi);
                float z = (float) (Math.sin(theta) * Math.sin(phi));

                vertices[vertexIndex++] = x * radius;
                vertices[vertexIndex++] = y * radius;
                vertices[vertexIndex++] = z * radius;


                texCoords[texCoordIndex++] = u;
                texCoords[texCoordIndex++] = 1 - v;


                normals[normalIndex++] = x;
                normals[normalIndex++] = y;
                normals[normalIndex++] = z;
            }
        }


        int index = 0;
        for (short i = 0; i < stacks; ++i) {
            for (short j = 0; j < slices; ++j) {
                short first = (short) ((i * (slices + 1)) + j);
                short second = (short) (first + slices + 1);

                indices[index++] = first;
                indices[index++] = second;
                indices[index++] = (short) (first + 1);

                indices[index++] = second;
                indices[index++] = (short) (second + 1);
                indices[index++] = (short) (first + 1);
            }
        }
        numIndices = index;


        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        textureCoordBuffer = tbb.asFloatBuffer();
        textureCoordBuffer.put(texCoords);
        textureCoordBuffer.position(0);

        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
        nbb.order(ByteOrder.nativeOrder());
        normalBuffer = nbb.asFloatBuffer();
        normalBuffer.put(normals);
        normalBuffer.position(0);

        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    public void draw(int shaderProgram) {

        int positionHandle = GLES32.glGetAttribLocation(shaderProgram, "aPosition");
        int texCoordHandle = GLES32.glGetAttribLocation(shaderProgram, "aTexCoord");
        int normalHandle = GLES32.glGetAttribLocation(shaderProgram, "aNormal");


        if (positionHandle >= 0) {
            GLES32.glEnableVertexAttribArray(positionHandle);
            GLES32.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES32.GL_FLOAT, false, vertexStride, vertexBuffer);
        }

        if (texCoordHandle >= 0) {
            GLES32.glEnableVertexAttribArray(texCoordHandle);
            GLES32.glVertexAttribPointer(texCoordHandle, TEXCOORDS_PER_VERTEX, GLES32.GL_FLOAT, false, texCoordStride, textureCoordBuffer);
        }

        if (normalHandle >= 0) {
            GLES32.glEnableVertexAttribArray(normalHandle);
            GLES32.glVertexAttribPointer(normalHandle, COORDS_PER_VERTEX, GLES32.GL_FLOAT, false, vertexStride, normalBuffer);
        }


        GLES32.glDrawElements(GLES32.GL_TRIANGLES, numIndices, GLES32.GL_UNSIGNED_SHORT, indexBuffer);


        if (positionHandle >= 0) {
            GLES32.glDisableVertexAttribArray(positionHandle);
        }
        if (texCoordHandle >= 0) {
            GLES32.glDisableVertexAttribArray(texCoordHandle);
        }
        if (normalHandle >= 0) {
            GLES32.glDisableVertexAttribArray(normalHandle);
        }
    }
}


package com.example.sensetime.myapplication;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by sensetime on 2017/8/8.
 */

public class Square {
    private float[] vertices =  {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f };
    private short[] drawOrder = {0,1,2,0,2,3};
    private float[] color = {0.0f, 0.0f, 0.0f, 1.0f};
    private String verticesShader =
            "attribute vec4 aPosition;\n" +
                    "void main(){\n" +
                    "gl_Position = aPosition;\n" +
                    "}";
    private String fragmentShader =
            "precision mediump float;\n" +
                    "uniform vec4 vColor;\n" +
                    "void main(){\n" +
                    "gl_FragColor = vColor;\n" +
                    "}";
    private FloatBuffer buffer;
    private ShortBuffer oBuffer;
    public Square() {

    }

    public void draw() {
        ByteBuffer vBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        vBuffer.order(ByteOrder.nativeOrder());
        buffer = vBuffer.asFloatBuffer();
        buffer.put(vertices);
        buffer.position(0);

        ByteBuffer fBuffer = ByteBuffer.allocateDirect(drawOrder.length * 2);
        fBuffer.order(ByteOrder.nativeOrder());
        oBuffer = fBuffer.asShortBuffer();
        oBuffer.put(drawOrder);
        oBuffer.position(0);

        int vShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vShader, verticesShader);
        GLES20.glCompileShader(vShader);

        int cShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(cShader, fragmentShader);
        GLES20.glCompileShader(cShader);

        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, cShader);
        GLES20.glLinkProgram(program);
        GLES20.glUseProgram(program);

        int vertiecsHandle = GLES20.glGetAttribLocation(program, "aPosition");
        GLES20.glVertexAttribPointer(vertiecsHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, buffer);
        GLES20.glEnableVertexAttribArray(vertiecsHandle);


        int colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, oBuffer);
        GLES20.glDisableVertexAttribArray(vertiecsHandle);
    }
}

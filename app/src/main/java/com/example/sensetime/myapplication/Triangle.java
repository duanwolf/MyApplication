package com.example.sensetime.myapplication;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by sensetime on 2017/8/7.
 */

public class Triangle {
    private final String verticesShader =
            "attribute vec4 aPosition;\n" +
                    "void main(){\n" +
                    "gl_Position = aPosition;\n" +
                    "}";

    private final String colorShader =
            "precision mediump float;\n" +
                    "uniform vec4 vColor;\n" +
                    "void main(){\n" +
                    "gl_FragColor = vColor;\n" +
                    "}";
    private float[] vertices = {
            0f,1f,0f,
            -1f,-1f,0f,
            1f,0f,0f
    };
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
    private FloatBuffer buffer;

    public Triangle() {

    }

    public void draw() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        buffer = byteBuffer.asFloatBuffer();
        buffer.put(vertices);
        buffer.position(0);
        byteBuffer.position(0);
        int shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(shader, verticesShader);
        GLES20.glCompileShader(shader);

        int cShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(cShader, colorShader);
        GLES20.glCompileShader(cShader);

        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, shader);
        GLES20.glAttachShader(program, cShader);
        GLES20.glLinkProgram(program);
        GLES20.glUseProgram(program);

        int verticesHandle = GLES20.glGetAttribLocation(program, "aPosition");
        GLES20.glEnableVertexAttribArray(verticesHandle);
        GLES20.glVertexAttribPointer(verticesHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, buffer);

        int colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES20.glDisableVertexAttribArray(verticesHandle);
    }
}

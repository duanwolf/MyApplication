package com.example.sensetime.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Matrix;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by sensetime on 2017/8/8.
 */

public class Texture {
    private final String matrixShader =
            "uniform mat4 uMatrix;\n" +
                    "attribute vec4 position;\n" +
                    "attribute vec2 a_texCoord;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() {\n" +
                    "gl_Position = position;\n" +
                    "v_texCoord = a_texCoord;\n" +
                    "}";
    private final String texShader =
            "precision mediump float;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "void main(){\n" +
                    "gl_FragColor = texture2D(s_texture, v_texCoord);\n" +
                    "}";
    private static final float[] VERTEX = {   // in counterclockwise order:
            1, 0, 0,   // top right
            -1, 0, 0,  // top left
            0, -1, 0, // bottom left
            0, 1, 0,  // bottom right
    };
    private static final short[] VERTEX_INDEX = {
            0, 2, 3, 1, 2, 3
    };
    private float[] TEX_VERTEX = {
            1f, 0,  // bottom right
            0, 0,  // bottom left
            0, 1f,  // top left
            1f, 1f,  // top right
    };
    private float[] mMatrix = new float[16];
    private ShortBuffer orderBuffer;
    private FloatBuffer texBuffer, matBuffer;
    public Texture(){

    }

    public void changeMatrix(int width, int height) {
        Matrix.perspectiveM(mMatrix, 0, 45f, (float)width / height, 0.1f, 100f );
        Matrix.translateM(mMatrix, 0, 0f, 0f, -5f);
    }
    public void draw(Context context) {
        texBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEX_VERTEX);
        texBuffer.position(0);
        orderBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(VERTEX_INDEX);
        orderBuffer.position(0);
        matBuffer = ByteBuffer.allocateDirect(VERTEX.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VERTEX);
        matBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, matrixShader);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, texShader);

        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
        GLES20.glUseProgram(program);

        int vPositionHandle = GLES20.glGetAttribLocation(program, "position");
        GLES20.glEnableVertexAttribArray(vPositionHandle);
        GLES20.glVertexAttribPointer(vPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, matBuffer);

        int vTexCoordHandle = GLES20.glGetAttribLocation(program, "a_texCoord");
        GLES20.glEnableVertexAttribArray(vTexCoordHandle);
        GLES20.glVertexAttribPointer(vTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0, texBuffer);

        int matrixHandle = GLES20.glGetUniformLocation(program, "uMatrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mMatrix, 0);

        int texSamplerHandle = GLES20.glGetUniformLocation(program, "s_texture");
        GLES20.glUniform1i(texSamplerHandle, 0);

        int[] texName = new int[1];
        GLES20.glGenTextures(1, texName, 0);
        int mTexName = texName[0];

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, VERTEX_INDEX.length, GLES20.GL_UNSIGNED_SHORT, orderBuffer);

    }

    private int loadShader(int type, String shader) {
        int lShader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(lShader, shader);
        GLES20.glCompileShader(lShader);
        return lShader;
    }
}

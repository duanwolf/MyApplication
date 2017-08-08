package com.example.sensetime.myapplication;

import android.content.Context;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by sensetime on 2017/8/7.
 */

public class MyRenderer implements GLSurfaceView.Renderer {
    Triangle triangle;
    Square square;
    Texture texture;
    private Context context;
    public MyRenderer(Context context) {
        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.1f, 1.0f);
        triangle = new Triangle();
        square = new Square();
        texture = new Texture();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0,0,width,height);
        texture.changeMatrix(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GLES20.GL_COLOR_BUFFER_BIT );
        /*triangle.draw();
        square.draw();*/
        texture.draw(context);
    }
}

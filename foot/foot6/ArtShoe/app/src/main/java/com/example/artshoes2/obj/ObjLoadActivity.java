package com.example.artshoes2.obj;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.artshoes2.R;
import com.example.artshoes2.ToolBarUnits;
import com.example.artshoes2.Utils.Gl2Utils;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wuwang on 2017/1/7
 */

public class ObjLoadActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;
    private ObjFilter mFilter;
    private Obj3D obj;
    private float dx=0;
    private float dy=0;
    private float downX;
    private float downY;
    private int flag=1;
    private int mode=0;//0代表没有手指触控；1代表单点触控；2代表单指移动旋转；3代表双指落下；4代表双指移动放缩；
    private float preDistance=1;
    private float nowDistance=1;
    private float factorFinger=1;//记录现在手指放大的倍数；
    private float factorNow=1;//记录现在的放大倍数，最大允许2，最小为0.5；
    private float factorActural;//目前应该对矩阵的放大倍数；

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objview);
        Toolbar tl_head = (Toolbar) findViewById(R.id.tl_head);
        //对导航栏进行设置
        tl_head.setTitle("脚型模型");
        setSupportActionBar(tl_head);
        tl_head.setNavigationIcon(R.drawable.ic_back);
        mGLView= (GLSurfaceView) findViewById(R.id.mGLView);
        mGLView.setEGLContextClientVersion(2);
        mFilter=new ObjFilter(getResources());
        obj=new Obj3D();
        Intent intent=getIntent();
        String filepath=intent.getStringExtra("filepath");
//        try {
//            FileInputStream stream = new FileInputStream(filepath);
//            ObjReader.read(stream,obj);
//            mFilter.setObj3D(obj);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            ObjReader.read(getAssets().open("3dres/222.obj"),obj);
            mFilter.setObj3D(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mGLView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                mFilter.create();
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                mFilter.onSizeChanged(width, height);
                float[] matrix= Gl2Utils.getOriginalMatrix();
                Matrix.scaleM(matrix,0,0.2f,0.2f*width/height,0.2f);
                mFilter.setMatrix(matrix);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                if(mode==3) {
                    Matrix.rotateM(mFilter.getMatrix(), 0, 2.0f, dy, 0, -dx);
                }
                mFilter.draw();
            }
        });
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_objview, menu);
        ToolBarUnits.setIconVisable(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()& MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mode=1;
                //将按下时的坐标存储
//                Toast.makeText(ObjLoadActivity.this,"单指落下",Toast.LENGTH_SHORT).show();
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                preDistance = getDistance(event);
                factorNow=1;
                if (preDistance > 10f) {
//                    Toast.makeText(ObjLoadActivity.this,"你双指触摸了屏幕",Toast.LENGTH_SHORT).show();
                    mode = 2;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                break;
            case MotionEvent.ACTION_UP:
                mode=0;
                dx=0;
                dx=0;
                break;
            case MotionEvent.ACTION_MOVE:
                //当两指缩放，计算缩放比例
                if (mode == 2) {

                    nowDistance=getDistance(event);
                    Float distance=nowDistance-preDistance;
                    if(Math.abs(distance)>50){
//                        Toast.makeText(ObjLoadActivity.this,"双指移动",Toast.LENGTH_SHORT).show();
                        factorFinger=nowDistance/preDistance;
                        factorActural=factorFinger/factorNow;
                        factorNow=factorFinger;
                        Matrix.scaleM(mFilter.getMatrix(),0,factorActural,factorActural ,factorActural);
                    }
                }else if(mode==1||mode==3){
                    float x= event.getX();
                    float y = event.getY();
                    dx=x-downX;
                    dy=y-downY;
                    if (Math.abs(dx)>50||Math.abs(dy)>50){
//                        Toast.makeText(ObjLoadActivity.this,"单指移动",Toast.LENGTH_SHORT).show();
                        mode=3;
                    }
                }

                break;
        }

        return super.onTouchEvent(event);
    }
    private float getDistance(MotionEvent event) {
        float x = event.getX(1) - event.getX(0);
        float y = event.getY(1) - event.getY(0);
        float distance = (float) Math.sqrt(x * x + y * y);//两点间的距离
        return distance;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }
}

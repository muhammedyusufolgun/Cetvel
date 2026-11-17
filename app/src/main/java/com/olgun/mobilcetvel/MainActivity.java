package com.olgun.mobilcetvel;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.view.View;
import android.view.WindowManager;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(attrs);
        }
        FrameLayout root=new FrameLayout(this);
        root.addView(new RulerView(),new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        float density=metrics.density;
        int buttonWidth=Math.round(28*density), buttonHeight=Math.round(36*density);
        int centerY=Math.round(140f*metrics.ydpi/25.4f);
        TextView close=new TextView(this);
        close.setText("X"); close.setTextSize(23); close.setTextColor(0xff333333);
        close.setGravity(Gravity.CENTER); close.setIncludeFontPadding(false);
        close.setContentDescription("Uygulamayı kapat");
        GradientDrawable bg=new GradientDrawable(); bg.setColor(0xffffd0d0); bg.setCornerRadius(6*density);
        close.setBackground(bg); close.setClickable(true); close.setFocusable(true);
        close.setOnClickListener(v -> finishAndRemoveTask());
        FrameLayout.LayoutParams buttonParams=new FrameLayout.LayoutParams(buttonWidth,buttonHeight,Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        buttonParams.topMargin=centerY-buttonHeight/2;
        root.addView(close,buttonParams);
        setContentView(root);
    }

    private final class RulerView extends View {
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final float density = getResources().getDisplayMetrics().density;
        private final float xdpi = getResources().getDisplayMetrics().xdpi;
        private final float ydpi = getResources().getDisplayMetrics().ydpi;
        private final float band = 100 * density;
        RulerView() { super(MainActivity.this); setLayerType(View.LAYER_TYPE_SOFTWARE, null); setBackgroundColor(0xffffffff); }

        private void drawSteel(Canvas c, float w, float h) {
            p.setStyle(Paint.Style.FILL);
            p.setShader(new LinearGradient(0, 0, band, 0,
                    new int[]{0xffd4c7a8,0xfffff7e4,0xffeee1c5,0xfffff9e9,0xffd9c9a7},
                    new float[]{0f,0.08f,0.5f,0.92f,1f}, Shader.TileMode.CLAMP));
            c.drawRect(0,0,band,h,p);
            p.setShader(new LinearGradient(w-band, 0, w, 0,
                    new int[]{0xffd4c7a8,0xfffff7e4,0xffeee1c5,0xfffff9e9,0xffd9c9a7},
                    new float[]{0f,0.08f,0.5f,0.92f,1f}, Shader.TileMode.CLAMP));
            c.drawRect(w-band,0,w,h,p); p.setShader(null);
            p.setColor(0xffffffff); p.setStrokeWidth(density*0.8f);
            c.drawLine(2*density,0,2*density,h,p); c.drawLine(w-2*density,0,w-2*density,h,p);
        }

        private void tick(Canvas c, float x1,float y,float x2,float width) {
            p.setShader(null); p.setColor(0xff101417); p.setStrokeWidth(width); p.setStyle(Paint.Style.STROKE);
            c.drawLine(x1,y,x2,y,p); p.setStyle(Paint.Style.FILL);
        }

        private void number(Canvas c,String value,float x,float y,boolean left) {
            p.setShader(null); p.setColor(0xff101417); p.setTextSize(21*density);
            p.setTypeface(Typeface.create("sans-serif-condensed",Typeface.NORMAL)); p.setTextAlign(Paint.Align.CENTER);
            c.save(); c.rotate(left?90:-90,x,y); c.drawText(value,x,y+5*density,p); c.restore();
        }

        private void edgeScale(Canvas c,float w,float h,boolean left) {
            float pixelsPerMm=ydpi/25.4f;
            float pixelsPerMmX=xdpi/25.4f;
            int count=(int)(h/pixelsPerMm);
            float cmMarkLength=46*density;
            float labelGap=2*pixelsPerMmX;
            float labelHalfHeight=10.5f*density;
            float labelX=left?cmMarkLength+labelGap+labelHalfHeight:w-cmMarkLength-labelGap-labelHalfHeight;
            for(int mm=0;mm<=count;mm++) {
                float y=left?mm*pixelsPerMm:h-mm*pixelsPerMm;
                int unit=mm%10;
                float length=(unit==0?46:unit==5?34:22)*density;
                float start=left?0:w;
                float end=left?length:w-length;
                if (y > 0.5f) tick(c,start,y,end,unit==0?1.8f*density:unit==5?1.35f*density:0.9f*density);
                if(unit==0) number(c,Integer.toString(mm/10),labelX,y,left);
            }
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas); float w=getWidth(),h=getHeight(); canvas.drawColor(0xffffffff);
            drawSteel(canvas,w,h); edgeScale(canvas,w,h,true); edgeScale(canvas,w,h,false);
            p.setShader(null); p.setTextAlign(Paint.Align.CENTER); p.setColor(0xff111111);
            p.setTypeface(Typeface.create("sans-serif",Typeface.NORMAL)); p.setTextSize(28*density);
            float cy=h/2f; canvas.drawText("Mobil Cetvel",w/2f,50f*ydpi/25.4f,p);
            p.setColor(0xffb5b5b5); p.setTypeface(Typeface.create("sans-serif",Typeface.NORMAL)); p.setTextSize(14*density);
            canvas.drawText("Muhammed Yusuf OLGUN",w/2f,150f*ydpi/25.4f,p);
        }
    }
}
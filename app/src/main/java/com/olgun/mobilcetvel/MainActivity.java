package com.olgun.mobilcetvel;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.view.View;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(new RulerView());
    }

    private final class RulerView extends View {
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final float density = getResources().getDisplayMetrics().density;
        private final float ydpi = getResources().getDisplayMetrics().ydpi;
        private final float band = 64 * density;
        private final float inset = 6 * density;
        RulerView() { super(MainActivity.this); setLayerType(View.LAYER_TYPE_SOFTWARE, null); setBackgroundColor(0xffffffff); }

        private void drawSteel(Canvas c, float w, float h) {
            p.setStyle(Paint.Style.FILL);
            p.setShader(new LinearGradient(0, 0, w, 0,
                    new int[]{0xff9da5aa,0xfff8fafb,0xffc6cdd1,0xfffdfefe,0xffaab2b7},
                    new float[]{0f,0.035f,0.5f,0.965f,1f}, Shader.TileMode.CLAMP));
            c.drawRect(0,0,band,h,p); c.drawRect(w-band,0,w,h,p); p.setShader(null);
            p.setColor(0xff687177); p.setStrokeWidth(Math.max(1f,density));
            c.drawLine(band-inset,0,band-inset,h,p); c.drawLine(w-band+inset,0,w-band+inset,h,p);
            p.setColor(0xffffffff); p.setStrokeWidth(density*0.8f);
            c.drawLine(2*density,0,2*density,h,p); c.drawLine(w-2*density,0,w-2*density,h,p);
        }

        private void tick(Canvas c, float x1,float y,float x2,float width) {
            p.setShader(null); p.setColor(0xff101417); p.setStrokeWidth(width); p.setStyle(Paint.Style.STROKE);
            c.drawLine(x1,y,x2,y,p); p.setStyle(Paint.Style.FILL);
        }

        private void number(Canvas c,String value,float x,float y,boolean left) {
            p.setShader(null); p.setColor(0xff101417); p.setTextSize(16*density);
            p.setTypeface(Typeface.create("sans-serif-condensed",Typeface.BOLD)); p.setTextAlign(Paint.Align.CENTER);
            c.save(); c.rotate(left?90:-90,x,y); c.drawText(value,x,y+5*density,p); c.restore();
        }

        private void edgeScale(Canvas c,float w,float h,boolean left) {
            float pixelsPerMm=ydpi/25.4f;
            int count=(int)(h/pixelsPerMm);
            float base=left?band-inset:w-band+inset;
            for(int mm=0;mm<=count;mm++) {
                float y=left?mm*pixelsPerMm:h-mm*pixelsPerMm;
                int unit=mm%10;
                float length=(unit==0?30:unit==5?22:14)*density;
                float end=left?base-length:base+length;
                tick(c,base,y,end,unit==0?1.8f*density:unit==5?1.35f*density:0.9f*density);
                if(unit==0) number(c,Integer.toString(mm/10),left?band*0.49f:w-band*0.49f,y,left);
            }
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas); float w=getWidth(),h=getHeight(); canvas.drawColor(0xffffffff);
            drawSteel(canvas,w,h); edgeScale(canvas,w,h,true); edgeScale(canvas,w,h,false);
            p.setShader(null); p.setTextAlign(Paint.Align.CENTER); p.setColor(0xff111111);
            p.setTypeface(Typeface.create("sans-serif",Typeface.BOLD)); p.setTextSize(28*density);
            float cy=h/2f; canvas.drawText("Mobil Cetvel",w/2f,cy-10*density,p);
            p.setColor(0xff888888); p.setTypeface(Typeface.create("sans-serif",Typeface.NORMAL)); p.setTextSize(14*density);
            canvas.drawText("Muhammed Yusuf OLGUN",w/2f,cy+38*density,p);
        }
    }
}
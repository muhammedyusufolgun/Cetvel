package com.olgun.mobilcetvel;
import android.app.Activity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

public class MainActivity extends Activity {
 @Override public void onCreate(Bundle state) {
  super.onCreate(state);
  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
  getWindow().setStatusBarColor(0xffffffff);
  getWindow().setNavigationBarColor(0xffffffff);
  setContentView(new RulerView());
 }
 private final class RulerView extends View {
  private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final float density = getResources().getDisplayMetrics().density;
  private final float xdpi = getResources().getDisplayMetrics().xdpi;
  private final float ydpi = getResources().getDisplayMetrics().ydpi;
  RulerView() { super(MainActivity.this); setLayerType(View.LAYER_TYPE_SOFTWARE,null); setBackgroundColor(0xffffffff); }
  private void line(Canvas c,float x1,float y1,float x2,float y2,float width) {
   p.setColor(0xff111111); p.setStrokeWidth(width); p.setStyle(Paint.Style.STROKE); c.drawLine(x1,y1,x2,y2,p); p.setStyle(Paint.Style.FILL);
  }
  private void label(Canvas c,String s,float x,float y,float size) {
   p.setColor(0xff111111); p.setTextSize(size); p.setTypeface(Typeface.create("sans-serif",Typeface.BOLD)); p.setTextAlign(Paint.Align.CENTER); c.drawText(s,x,y,p);
  }
  @Override protected void onDraw(Canvas canvas) {
   super.onDraw(canvas); canvas.drawColor(0xffffffff);
   float w=getWidth(), h=getHeight(), sx=xdpi/25.4f, sy=ydpi/25.4f, stroke=Math.max(1f,density*0.7f);
   for(int i=0,n=(int)(w/sx);i<=n;i++) {
    float x=i*sx; int mm=i%10; float len=(mm==0?18:mm==5?13:8)*density;
    line(canvas,x,0,x,len,mm==0?stroke*1.4f:stroke);
    if(mm==0) label(canvas,Integer.toString(i/10),x+10*density,31*density,11*density);
    line(canvas,x,h,x,h-len,mm==0?stroke*1.4f:stroke);
    if(mm==0) label(canvas,Integer.toString(i/10),x+10*density,h-7*density,11*density);
   }
   for(int i=0,n=(int)(h/sy);i<=n;i++) {
    float y=i*sy; int mm=i%10; float len=(mm==0?18:mm==5?13:8)*density;
    line(canvas,0,y,len,y,mm==0?stroke*1.4f:stroke);
    if(mm==0) { canvas.save(); canvas.rotate(90,31*density,y+10*density); label(canvas,Integer.toString(i/10),31*density,y+10*density,11*density); canvas.restore(); }
    line(canvas,w,y,w-len,y,mm==0?stroke*1.4f:stroke);
    if(mm==0) { canvas.save(); canvas.rotate(-90,w-31*density,y-10*density); label(canvas,Integer.toString(i/10),w-31*density,y-10*density,11*density); canvas.restore(); }
   }
   float cy=h/2f;
   p.setColor(0xff111111); p.setTextAlign(Paint.Align.CENTER); p.setTypeface(Typeface.create("sans-serif",Typeface.BOLD)); p.setTextSize(27*density);
   canvas.drawText("Mobil Cetvel",w/2f,cy-2*density,p);
   p.setTypeface(Typeface.create("sans-serif",Typeface.NORMAL)); p.setTextSize(14*density); p.setColor(0xff888888);
   canvas.drawText("Muhammed Yusuf OLGUN",w/2f,cy+48*density,p);
  }
 }
}
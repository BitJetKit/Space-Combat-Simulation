package com.example.hyperion.spacecombatsimulation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ThreadLocalRandom;

public class MenuBackground extends View {

    private Paint paint = new Paint();
    private int width, height;
    private int random(int min, int max) { return ThreadLocalRandom.current().nextInt(min, max); }
    //private Bitmap background1, background2;

    public MenuBackground(Context context) {
        super(context);
        init(context);
    }

    public MenuBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuBackground(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("DEBUG","onSizeChanged");
        width = w; height = h;
        Main.height = h;
        //background1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //background2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    protected void init(Context context) {
        Log.d("DEBUG","init");
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(8,0,0, Color.BLUE);

        //moveBackground.run();
    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable moveBackground = new Runnable() {
        public void run() {
            invalidate();
            handler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 1; i <= 1000; i++) {
            int bright = random(0, 220), size = random(1, 3);
            int posX = random(0, width), posY = random(0, height);
            paint.setColor(Color.argb(bright, bright, bright, bright));
            canvas.drawCircle(posX, posY, size, paint);
        }
    }
}

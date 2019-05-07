package cs4330.cs.utep.pkrunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation
{
    private Bitmap[] frames;
    private int framesIndex;
    private float frameTime;
    private long lastFrame;

    private boolean isPlaying = false;

    public Animation(Bitmap[] frames, float animationTime)
    {
        this.frames = frames;
        framesIndex = 0;

        frameTime = animationTime/frames.length;

        lastFrame = System.currentTimeMillis();
    }

    public boolean isPlaying()
    {
        return isPlaying;
    }

    public void play()
    {
        isPlaying = true;
        framesIndex = 0;
        lastFrame = System.currentTimeMillis();
    }
    public void stop()
    {
        isPlaying = false;
    }

    public void update()
    {
        if(!isPlaying)
            return;
        if(System.currentTimeMillis() - lastFrame > frameTime*1000)
        {
            framesIndex++;
            framesIndex = framesIndex >= frames.length ? 0 : framesIndex;
            lastFrame = System.currentTimeMillis();
        }
    }

    public void draw(Canvas canvas, Rect destination)
    {
        if(!isPlaying)
            return;

       //scaleRect(destination);

        canvas.drawBitmap(frames[framesIndex],null,destination,new Paint());
    }
/*
    private void scaleRect(Rect rect)
    {
        float whRatio = (float)(frames[framesIndex].getWidth()/frames[framesIndex].getHeight());
        if(rect.width() > rect.height() )
            rect.left = rect.right - (int)(rect.width()*whRatio);
        else
            rect.top = rect.bottom - (int)(rect.height()*(1/whRatio));


    }*/


}

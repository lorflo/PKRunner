package cs4330.cs.utep.pkrunner;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene
{
    public void reciveTouch(MotionEvent event);
    public void update();
    public void draw(Canvas canvas);
    public void  terminate();
}

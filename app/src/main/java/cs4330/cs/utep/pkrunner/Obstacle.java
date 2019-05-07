package cs4330.cs.utep.pkrunner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle implements GameObject {
    private Rect stableRect,scoreRect;
    private Rect gsRect;
    private int color, state = 0;

    public  Obstacle(int rectHeight, int color,int right, int left, int playerGap )
    {
        this.color = color;

        stableRect = new Rect(left,Constants.SCREEN_HEIGHT - rectHeight,right, (Constants.SCREEN_HEIGHT/8) * 7);
        gsRect = new Rect(left,Constants.SCREEN_HEIGHT/4,right,Constants.SCREEN_HEIGHT/2);
        scoreRect = new Rect(left,gsRect.bottom,right, stableRect.top);

    }

    public void incrementY(float y)
    {
        stableRect.left -= y;
        stableRect.right -= y;
        gsRect.left -= y;
        gsRect.right -= y;
        scoreRect.left -= y;
        scoreRect.right -= y;
        growShrink(y);
    }

    public Rect getRectangle()
    {
        return stableRect;
    }

    public boolean playerCollide(Player player)
    {
      return Rect.intersects(stableRect,player.getRectangle()) ||
                Rect.intersects(gsRect,player.getRectangle());
    }
    public boolean scored(Player player)
    {
        return Rect.intersects(scoreRect,player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(stableRect,paint);
        canvas.drawRect(gsRect,paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(scoreRect, paint);

    }

    public void growShrink(float y)
    {
        if(gsRect.bottom <= gsRect.top)
            state = 0;
        else if(gsRect.bottom >= (Constants.SCREEN_HEIGHT/2) +1)
            state = 1;

        switch(state)
        {
            case 0:
                gsRect.bottom += y;
                break;
            case 1:
                gsRect.bottom -= y;
                break;
        }
    }
    @Override
    public void update() {

    }
}

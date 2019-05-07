package cs4330.cs.utep.pkrunner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

public class Player implements GameObject
{
    private Rect spriteArea;
    private  Animation idle,walkRight,walkLeft,jumping;
    private  AnimationManager animManager;
    private int state = 0;
    private int startingY;
    private Point newPoint = new Point();

    public  Player(Rect rectangle)
    {
        spriteArea = rectangle;
        startingY = spriteArea.top;

        BitmapFactory bf = new BitmapFactory();
        Bitmap idleImage = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.alienpink);
        Bitmap walk1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.alienpink_walk1);
        Bitmap walk2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.alienpink_walk2);
        Bitmap jump = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.alienpink_jump);

        idle = new Animation(new Bitmap[]{idleImage},.5f);
        walkRight = new Animation(new Bitmap[]{walk1,walk2},.5f);
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);
        walkLeft = new Animation(new Bitmap[]{walk1,walk2},.5f);
        jumping =  new Animation(new Bitmap[]{jump},.5f);

        animManager = new AnimationManager(new Animation[]{idle,walkRight,walkLeft,jumping});
    }

    public Rect getRectangle()
    {
        return spriteArea;
    }
    @Override
    public void draw(Canvas canvas)
    {
        animManager.draw(canvas, spriteArea);
    }

    public void update(char dir,Point point)
    {

        float oldLeft = spriteArea.left;
        float oldTop = spriteArea.top;
        newPoint = point;

        switch(dir)
        {
            case 'l':
                movingLeft(newPoint);
                break;
            case 'r':
                movingRight(newPoint);
                break;
            case 'u':
                jump(newPoint);
                break;
            case 'd':
                down(newPoint);

        }


        spriteArea.set(newPoint.x - spriteArea.width()/2,newPoint.y - spriteArea.height()/2,
                newPoint.x + spriteArea.width()/2,newPoint.y + spriteArea.height()/2);//updates the point with the new position
        int state = 0;//animation state idle
        if( spriteArea.left - oldLeft >= 5)//moved 5 or more pixels to the left
            state =1;//animation state walking left
        else if(spriteArea.left - oldLeft <= -5)//moved 5 or more pixels to the right
            state = 2;//animation state walking right
        else if(spriteArea.top - oldTop <= -5 || spriteArea.top - oldTop >= 5)
            state = 3;//Animation state jumping

        animManager.playAnim(state);//plays the animation
        animManager.update();//updates the animation

    }
/*
 * Moves the left and right edges of the rectangle that contains the sprite image
 * to the left by 5 pixels, or to the right by 5 pixels.
 */

    public void movingLeft(Point point)
    {
        if(spriteArea.bottom >= (startingY + 1050))
            point.x -= 20;
        else
            down(point);

    }
    public void movingRight(Point point)
    {
        if(spriteArea.bottom >= (startingY + 1050))
            point.x +=20;
        else
            down(point);
    }

    /*
     * Similar to growShrink of obstacles
     * Moves sprite up for a little then down for a little till
     * the user stops pressing the jump button
     */
    public void jump(Point point)
    {
            if(spriteArea.top <= Constants.SCREEN_HEIGHT/2 +2 )
                state = 0;
            else if(spriteArea.bottom >= (startingY + 1050))
                state = 1;
        switch(state)
        {
            case 0:
                down(point);
                return;
            case 1:
                point.y -= 35;
                point.x +=20;
                break;
        }

    }
    public void down(Point point)
    {
        if(spriteArea.bottom >= (startingY + 1050))
        {
            return;
        }
        else
            {
            point.y += 17;
            point.x += 20;
        }
    }

    @Override
    public void update()
    {
        animManager.update();
    }
}

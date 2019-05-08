package cs4330.cs.utep.pkrunner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class gamePlayScene implements Scene
{
    private Player player;
    private Point playerPoint;
    private ObstacleManager manager;
    private boolean movingPlayer = false;
    private boolean gameOver = false,gameWon = false;
    private long gameOverTime = 0;
    private Rect floor, ceiling,leftB, rightB,jumpB;
    private Rect r = new Rect();
    private char direction = 'n';
    private BitmapFactory bf = new BitmapFactory();
    private Bitmap leftArrow,rightArrow,jumpArrow;
    private int score = 0,level = 0,totalScore = 0;
    private  int[] colors = new int[]{Color.WHITE,Color.BLUE,Color.YELLOW,Color.GRAY};

    public gamePlayScene()
    {
        player = new Player(new Rect(210,210,400,400));
        playerPoint = new Point(Constants.SCREEN_WIDTH/4,(Constants.SCREEN_HEIGHT/8) * 6 + 85);
        player.update(direction,playerPoint);

        floor = new Rect(0,(Constants.SCREEN_HEIGHT/8) * 7,
                Constants.SCREEN_WIDTH + 200,Constants.SCREEN_HEIGHT);
        ceiling = new Rect(0,0,
                Constants.SCREEN_WIDTH + 200,Constants.SCREEN_HEIGHT/4);
        leftB = new Rect(50,(Constants.SCREEN_HEIGHT/8) * 7 + 10,
                350,Constants.SCREEN_HEIGHT);
        rightB = new Rect(leftB.right +100,(Constants.SCREEN_HEIGHT/8) * 7 + 10,
                leftB.right + 400,Constants.SCREEN_HEIGHT);
        jumpB = new Rect(Constants.SCREEN_WIDTH - rightB.left,(Constants.SCREEN_HEIGHT/8) * 7 + 10,
                Constants.SCREEN_WIDTH -50,Constants.SCREEN_HEIGHT);


        rightArrow = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.normal_arrow_right_seagreen_ben_01);
        Matrix m = new Matrix();//used to flip image on vertical axis
        m.preScale(-1, 1);
        leftArrow = Bitmap.createBitmap(rightArrow, 0, 0, rightArrow.getWidth(), rightArrow.getHeight(), m, false);
        jumpArrow = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.normal_arrow_up_seagreen_ben_01);

        manager = new ObstacleManager(200,1000,350,Color.RED);

    }
    public void reset()
    {
        playerPoint = new Point(Constants.SCREEN_WIDTH/4,(Constants.SCREEN_HEIGHT/8) * 6 + 80);
        manager = new ObstacleManager(210,1000,400,Color.RED);
        movingPlayer = false;
        player.update(direction,playerPoint);
        gameOverTime = 0;
        score = 0;
    }
    @Override
    public void reciveTouch(MotionEvent event)//when the screen is touched
    {
        switch (event.getActionMasked())
        {
                case MotionEvent.ACTION_DOWN:
                    //checks to see if the game is not over and a button is being pressed
                    if (!gameOver && leftB.contains((int) event.getX(), (int) event.getY())
                            || rightB.contains((int) event.getX(), (int) event.getY())) {
                        if (leftB.contains((int) event.getX(), (int) event.getY()))//left button is pressed
                            direction = 'l';
                        if (rightB.contains((int) event.getX(), (int) event.getY()))//right button is pressed
                            direction = 'r';
                        if (gameOver || gameWon && System.currentTimeMillis() - gameOverTime >= 2000)//waits for two seconds
                        {                                                                // before user can resume playing after game won/over
                            reset();
                            gameOver = false;
                            gameWon = false;
                        }
                    }
                case MotionEvent.ACTION_POINTER_DOWN://when screen is pressed down on
                    if (jumpB.contains((int) event.getX(), (int) event.getY())) {
                        direction = 'u';
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_UP:
                   direction = 'd';
            }

    }

    @Override
    public void update()
    {
        if(!gameOver && !gameWon)
        {
            if(manager.playerScored(player))
            {
                score++;
                if(score > totalScore)
                    totalScore++;
                if(score > 100)
                {
                    //intenet back to start;
                    gameWon = true;
                }
            }

            player.update(direction, playerPoint);
            manager.update();
        }
        if(manager.playerCollide(player))
        {
            gameOver = true;

            if(gameOverTime != 0)
                return;
            else
                gameOverTime = System.currentTimeMillis();
        }
        if(gameWon)
        {
            if(gameOverTime != 0)
                return;
            else
                gameOverTime = System.currentTimeMillis();
        }

    }

    @Override
    public void draw(Canvas canvas)
    {

        if(score > 20)
        {
            if(level == colors.length-1)
            {
                level = 0;
            }
            else
                {
                    level++;
                    score =0;
                }

        }
        canvas.drawColor(colors[level]);//the background
        manager.draw(canvas);//the obstacles
        player.draw(canvas);//the player


        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(floor,paint);//the floor
        canvas.drawRect(ceiling,paint);//the ceiling



        canvas.drawBitmap(leftArrow,null,leftB,new Paint());//left button
        canvas.drawBitmap(rightArrow,null,rightB,new Paint());//right button
        canvas.drawBitmap(jumpArrow,null,jumpB,new Paint());
        paint.setTextSize(100);
        paint.setColor(Color.YELLOW);
        if(!gameOver && !gameWon)
            canvas.drawText(String.valueOf(score),1500,200,paint);

        if(gameOver)//shows game over in middle of screen
        {
            paint.setTextSize(100);
            paint.setColor(Color.RED);
            drawTextCenter(canvas,paint,"Game Over:press right arrow to continue");
            canvas.drawText(String.valueOf("High Score:" + totalScore),1500,200,paint);
        }
        if(gameWon)//shows game over in middle of screen
        {
            paint.setTextSize(100);
            paint.setColor(Color.BLACK);
            drawTextCenter(canvas,paint,"You Win!");
            canvas.drawText(String.valueOf(totalScore),1500,201,paint);

            paint.setColor(Color.YELLOW);
            drawTextCenter(canvas,paint,"You Win!");
            canvas.drawText(String.valueOf(totalScore),1500,200,paint);

        }
    }

    private void drawTextCenter(Canvas canvas, Paint paint, String text) //puts the text in the center of the screen
    {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void terminate()//resets the active scene
    {
        SceneManager.ACTIVE_SCENE = 0;
    }
}

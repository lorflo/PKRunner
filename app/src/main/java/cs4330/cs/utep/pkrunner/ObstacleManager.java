package cs4330.cs.utep.pkrunner;

import android.graphics.Canvas;

import java.util.ArrayList;

public class ObstacleManager
{
    //higher index = lower on screen = higher y values
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private  int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color)
    {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;
        startTime = System.currentTimeMillis();
        obstacles = new ArrayList<>();

        populateObstacles();
    }

    private void populateObstacles()
    {
        int left = Constants.SCREEN_WIDTH +3000;
        while(left > Constants.SCREEN_WIDTH  )
        {
            int right = left + playerGap;
            obstacles.add(new Obstacle(obstacleHeight,color,right,left,playerGap));
            left -= obstacleGap + playerGap;
        }
    }

    public  boolean playerCollide(Player player)
    {
        for(Obstacle ob: obstacles)
        {
            if(ob.playerCollide(player))
            {
                return true;
            }
        }
        return false;
    }

    public  boolean playerScored(Player player)
    {
        for(Obstacle ob: obstacles)
        {
            if(ob.scored(player))
            {
                return true;
            }
        }
        return false;
    }

    public  void  update()
    {
        int elapsedTime = (int)(System.currentTimeMillis() - startTime) *2;
        startTime = System.currentTimeMillis();
        float speed = (Constants.SCREEN_HEIGHT/10000.0f);
        int left = obstacles.get(0).getRectangle().left + (obstacleGap + playerGap);


        for(Obstacle ob: obstacles)
        {
            ob.incrementY(speed * elapsedTime);//move across screen
        }
        if(obstacles.get(obstacles.size() - 1).getRectangle().right <= 0)
        {
           obstacles.add(0,new Obstacle(obstacleHeight, color,left + playerGap,
                   left, playerGap));
           obstacles.remove(obstacles.size() -1);
        }


    }
    public  void  draw(Canvas canvas)
    {
        for(Obstacle ob: obstacles)
            ob.draw(canvas);
    }

}

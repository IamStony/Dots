package game.dots;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class Dot {
    public int x, y, x2, y2; //x2 and y2 are copies I'm using to revert animations
    public int color;
    public Paint dotPaint;
    public RectF circle, circle2; //circle2 is a copy I'm using to revert animations
    int cX, cY;

    public Dot(int x, int y) {
        this.x = this.x2 = x; //testing x2
        this.y = this.y2 = y; //testing y2
        circle = new RectF();
        circle2 = new RectF(); //testing
        dotPaint = new Paint();
        setColor(this);
        dotPaint.setColor(this.color);
        dotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        dotPaint.setAntiAlias(true);
    }

    static private void setColor(Dot dot) {
        Random random = new Random();
        int randomColor = random.nextInt(5);
        switch (randomColor){
            case 0:
                dot.color = Color.WHITE;
                break;
            case 1:
                dot.color = Color.CYAN;
                break;
            case 2:
                dot.color = Color.GRAY;
                break;
            case 3:
                dot.color = Color.RED;
                break;
            case 4:
                dot.color = Color.GREEN;
                break;
            default:
                dot.color = Color.WHITE;
                break;
        }
    }

    public boolean adjacent(Dot lastDot) {
        int dx = Math.abs(this.x - lastDot.x);
        int dy = Math.abs(this.y - lastDot.y);
        return (dx + dy == 1);
    }

    public void changeColor() {
        setColor(this);
        dotPaint.setColor(this.color);
    }

    public void changeColor(int color) {
        this.color = color;
        dotPaint.setColor(this.color);
    }


}

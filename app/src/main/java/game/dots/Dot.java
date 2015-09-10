package game.dots;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class Dot {
    public int x, y;
    public String color;
    public Paint dotPaint;
    public RectF circle;

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
        circle = new RectF();
        dotPaint = new Paint();
        dotPaint.setColor(Color.parseColor(getColor(this)));
        dotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        dotPaint.setAntiAlias(true);
    }

    static private String getColor(Dot dot) {
        Random random = new Random();
        int randomColor = random.nextInt(5);
        switch (randomColor){
            case 0:
                dot.color = "WHITE";
                return "WHITE";
            case 1:
                dot.color = "CYAN";
                return "CYAN";
            case 2:
                dot.color = "GRAY";
                return "GRAY";
            case 3:
                dot.color = "RED";
                return "RED";
            case 4:
                dot.color = "GREEN";
                return "GREEN";
            default:
                dot.color = "WHITE";
                return "WHITE";
        }
    }
}

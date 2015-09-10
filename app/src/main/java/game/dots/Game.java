package game.dots;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Game extends View {
    SharedPreferences m_sp;
    boolean moving;
    private Rect m_rect;
    private Paint m_paint;
    private Path m_path;
    private Paint m_paintPath;
    private int NUM_CELLS, m_cellWidth, m_cellHeight;

    ArrayList<Dot> m_dots;
    List<Point> m_dotPath = new ArrayList<Point>();

    public Game(Context context, AttributeSet attributeSet) {
        /**Initializing*/
        super(context, attributeSet);
        m_sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        moving = false;
        m_rect = new Rect();
        m_paint = new Paint();
        m_path = new Path();
        m_paintPath = new Paint();
        m_dots = new ArrayList<>();

        /**Getting values and configuring settings*/
        NUM_CELLS = Integer.parseInt(m_sp.getString("gridSize", "6"));

        m_paint.setColor(Color.WHITE);
        m_paint.setStyle(Paint.Style.STROKE);
        m_paint.setStrokeWidth(2);
        m_paint.setAntiAlias(true);

        m_paintPath.setColor(Color.BLACK);
        m_paintPath.setStrokeWidth(10);
        m_paintPath.setStrokeJoin(Paint.Join.ROUND);
        m_paintPath.setStrokeCap(Paint.Cap.ROUND);
        m_paintPath.setStyle(Paint.Style.STROKE);
        m_paintPath.setAntiAlias(true);
    }

    private void createDots() {
        for(int row = 0; row < NUM_CELLS; ++row) {
            for(int col = 0; col < NUM_CELLS; ++col) {
                int x = col * m_cellWidth;
                int y = row * m_cellHeight;
                Dot dot = new Dot(xToCol(x), yToRow(y));
                dot.circle.set(x, y, m_cellWidth + x, m_cellHeight + y);
                dot.circle.offset(getPaddingLeft(), getPaddingTop());
                dot.circle.inset(m_cellWidth * 0.2f, m_cellHeight * 0.2f);
                m_dots.add(dot);
            }
        }
    }

    private int xToCol( int x ) {
        return (x - getPaddingLeft()) / m_cellWidth;
    }
    private int yToRow( int y ) {
        return (y - getPaddingTop()) / m_cellHeight;
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        int boardWidth = (xNew - getPaddingLeft() - getPaddingRight());
        int boardHeight = (yNew - getPaddingTop() - getPaddingBottom());
        m_cellWidth = boardWidth / NUM_CELLS;
        m_cellHeight = boardHeight / NUM_CELLS;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(m_dots.isEmpty()) {
            createDots();
        }
        /**Draw the grid, used while coding, remove once finished*/
        for (int row = 0; row < NUM_CELLS; row++) {
            for (int col = 0; col < NUM_CELLS; col++) {
                int x = col * m_cellWidth;
                int y = row * m_cellHeight;
                m_rect.set(x, y, x + m_cellWidth, y + m_cellHeight);
                m_rect.offset(getPaddingLeft(), getPaddingTop());
                canvas.drawRect(m_rect, m_paint);
            }
        }

        /**Draw the dots*/
        for(int i = 0; i < m_dots.size(); i++) {
            Dot current = m_dots.get(i);
            canvas.drawOval(current.circle, current.dotPaint);
        }

        /**Draw the connection*/
        if(!m_dotPath.isEmpty()) {
            m_path.reset();
            Point point = m_dotPath.get(0);
            m_path.moveTo( (point.x * m_cellWidth) + m_cellWidth / 2, (point.y * m_cellHeight) + m_cellHeight / 2 );
            for( int i = 1; i < m_dotPath.size(); i++ ) {
                point = m_dotPath.get(i);
                m_path.lineTo( (point.x * m_cellWidth) + m_cellWidth / 2, (point.y * m_cellHeight) + m_cellHeight / 2 );
            }
            canvas.drawPath(m_path, m_paintPath);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        /*if(x < 0) { x = 0; }
        if(x > (NUM_CELLS * m_cellWidth) ) { x = NUM_CELLS * m_cellWidth - 10; }
        if(y < 0) { y = 0; }
        if(y > (NUM_CELLS * m_cellHeight) ) { y = NUM_CELLS * m_cellHeight - 10; }*/

        //System.out.println("cWidth: " + m_cellWidth);
        //System.out.println("cHeight: " + m_cellHeight);

        /*int xMax = getPaddingLeft() + m_cellWidth * NUM_CELLS;
        int yMax = getPaddingTop() + m_cellHeight * NUM_CELLS;
        x = Math.max( getPaddingLeft(), Math.min( x, (int) (xMax - m_circle.width())));
        y = Math.max( getPaddingTop(), Math.min( y, (int) (yMax - m_circle.height())));*/

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int squareX = x / m_cellWidth;
            int squareY = y / m_cellHeight;
            System.out.println("X: " + x + "     ->     X: " + squareX);
            System.out.println("Y: " + y + "     ->     Y: " + squareY);
            m_dotPath.add(new Point(squareX, squareY));
            moving = true;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE && moving) {
            int squareX = x / m_cellWidth;
            int squareY = y / m_cellHeight;
            System.out.println("X: " + x + "     ->     X: " + squareX);
            System.out.println("Y: " + y + "     ->     Y: " + squareY);
            if(x < 0) { x = 0; }
            if(x >= NUM_CELLS) { x = NUM_CELLS - 1; }
            if(y < 0) { y = 0; }
            if(y >= NUM_CELLS) { y = NUM_CELLS - 1; }
            Point point = new Point(squareX, squareY);
            if(!m_dotPath.contains(point)) {
                //TODO Get color and compare
                m_dotPath.add(point);
                invalidate(); //To draw the line
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            m_dotPath.clear();
            moving = false;
            invalidate();
        }
        return true;
    }


}
package game.dots;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
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
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

public class Game extends View {
    SharedPreferences m_sp;
    boolean moving;
    private Rect m_rect;
    private Paint m_paint;
    private Path m_path;
    private Paint m_paintPath;
    private int NUM_CELLS, m_cellWidth, m_cellHeight;

    ArrayList<Dot> m_dots;
    List<Point> m_dotPath;

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
        m_dotPath = new ArrayList<>();

        /**Getting values and configuring settings*/
        NUM_CELLS = Integer.parseInt(m_sp.getString("gridSize", "6"));

        m_paint.setColor(Color.WHITE);
        m_paint.setStyle(Paint.Style.STROKE);
        m_paint.setStrokeWidth(2);
        m_paint.setAntiAlias(true);

        //m_paintPath.setColor(Color.BLACK);
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
                Dot dot = new Dot(x / m_cellWidth, y / m_cellHeight);
                dot.circle.set(x, y, m_cellWidth + x, m_cellHeight + y);
                dot.circle.offset(getPaddingLeft(), getPaddingTop());
                dot.circle.inset(m_cellWidth * 0.2f, m_cellHeight * 0.2f);
                m_dots.add(dot);
            }
        }
    }

    private int squareN(int n) {
        int square = n / m_cellHeight; //m_cellHeight == m_cellWidth
        if(square < 0) square = 0;
        if(square >= NUM_CELLS - 1) square = NUM_CELLS - 1;
        return square;
    }

    private Dot getDot(int squareX, int squareY) {
        return m_dots.get((NUM_CELLS * squareY) + squareX);
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

        /**Draw the dots*/
        for(int i = 0; i < m_dots.size(); i++) {
            Dot current = m_dots.get(i);
            canvas.drawOval(current.circle, current.dotPaint);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int squareX = squareN(x);
        int squareY = squareN(y);
        //System.out.println("X: " + x + "     ->     X: " + squareX);
        //System.out.println("Y: " + y + "     ->     Y: " + squareY);
        Dot current = getDot(squareX, squareY);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            m_dotPath.add(new Point(squareX, squareY));
            m_paintPath.setColor(current.color);
            moving = true;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE && moving) {
            Point currentPoint = new Point(squareX, squareY);
            if(!m_dotPath.contains(currentPoint)) {
                Point lastPoint = m_dotPath.get(m_dotPath.size() - 1);
                Dot lastDot = getDot(lastPoint.x, lastPoint.y);
                if(current.color == lastDot.color) {
                    if(current.adjacent(lastDot)) {
                        m_dotPath.add(currentPoint);
                        invalidate(); //To draw the line
                    }
                }
            }
            else if(m_dotPath.size() > 1) {
                Point secondLast = m_dotPath.get(m_dotPath.size() - 2);
                if(secondLast.equals(currentPoint)) {
                    m_dotPath.remove(m_dotPath.size() - 1);
                    invalidate();
                }
            }

        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if(m_dotPath.size() > 1) {
                moveDots();
            }
            m_dotPath.clear();
            moving = false;
            invalidate();
        }
        return true;
    }

    private void moveDots() {
        Point currentPoint;
        Dot currentDot;
        int swap;
        int pathSize = m_dotPath.size();

        //Sorting the path so we always start at the highest
        //y-line where a dot is positioned
        Collections.sort(m_dotPath, new Comparator<Point>() {
            public int compare(Point o1, Point o2) {
                return Integer.compare(o1.y, o2.y);
            }
        });

        //Mirroring dots (putting them above the canvas in a mirror image)
        //current.x og current
        for(int i = 0; i < pathSize; i++) {
            currentPoint = m_dotPath.get(i);
            int x = currentPoint.x;
            int y = currentPoint.y;
            currentDot = getDot(x, y);

            while(y-- > 0) {
                Dot temp = getDot(x, y);

                //Swapping the color upwards
                swap = currentDot.color;
                currentDot.changeColor(temp.color);
                temp.changeColor(swap);

                animateMove(temp, currentDot);

                currentDot = temp;
            }

            currentDot.changeColor();
        }
    }

    public void animateMove(Dot from, Dot to) {
        System.out.println("Animation?");
        //from.animate().x(to.x).y(to.y);

        /*AnimatorSet anims = new AnimatorSet();
        ValueAnimator values = new ValueAnimator();
        values.ofObject()
        //values.setObjectValues(from.x, from.y, to.x, to.y);
        anims.playSequentially(

                ObjectAnimator.ofObject(TypeEvaluator(from), values);
        );
        anims.setDuration(500);
        anims.start();*/

        /*AnimatorSet as = new AnimatorSet();
        as.playSequentially(ObjectAnimator.ofFloat(...), // anim 1
        ObjectAnimator.ofInt(), // anim 2
        ObjectAnimator.ofFloat(...), // anim 3
        ObjectAnimator.ofFloat(...)); // anim 4
        as.setDuration(600);
        as.start();*/
    }

}
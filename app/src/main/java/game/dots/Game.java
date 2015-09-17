package game.dots;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Game extends View {
    SharedPreferences m_sp;
    boolean moving;
    private Rect m_rect;
    private Paint m_paint;
    private Path m_path;
    private Paint m_paintPath;
    private int NUM_CELLS, m_cellWidth, m_cellHeight;
    int score;
    TextView scoreview;
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

        m_paintPath.setStrokeWidth(10);
        m_paintPath.setStrokeJoin(Paint.Join.ROUND);
        m_paintPath.setStrokeCap(Paint.Cap.ROUND);
        m_paintPath.setStyle(Paint.Style.STROKE);
        m_paintPath.setAntiAlias(true);
        View v = (View) getParent();
        scoreview = (TextView) v.findViewById(R.id.score);
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
                dot.cX = x;
                dot.cY = y;
                m_dots.add(dot);
            }
        }
    }

    private int squareN(int n) { //Virkar fyrir bæði x og y hnit
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
        /**Draw the grid*/
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
            animations.clear();
            animatorSet = new AnimatorSet();
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
                setScore(m_dotPath.size());
                moveDots();
            }
            m_dotPath.clear();
            moving = false;
            invalidate();
        }
        return true;
    }
    public void setScore(int i)
    {
        score += i;
        scoreview.setText("Score: " + Integer.toString(score));
    }

    private void moveDots() {
        Point currentPoint;
        Dot currentDot = null;
        final Dot lastDot;
        int pathSize = m_dotPath.size();

        //Sorting the path so we always start at the highest
        //y-line where a dot is positioned
        Collections.sort(m_dotPath, new Comparator<Point>() {
            public int compare(Point o1, Point o2) {
                return Integer.compare(o1.y, o2.y);
            }
        });

        for(int i = 0; i < pathSize; i++) {
            currentPoint = m_dotPath.get(i);
            int x = currentPoint.x;
            int y = currentPoint.y;
            currentDot = getDot(x, y);

            while(y-- > 0) {
                Dot temp = getDot(x, y);

                animateMove(temp, currentDot);

                currentDot = temp;
            }
        }
        lastDot = currentDot;

        animatorSet.playTogether(animations);
        animatorSet.setDuration(200);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                System.out.println("Starting animation");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                assert lastDot != null;
                lastDot.changeColor();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                //skip
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                System.out.println("Repeating animation");
            }
        });
    }

    static List<Animator> animations = new LinkedList<>();

    static AnimatorSet animatorSet = new AnimatorSet();


    public void animateMove(final Dot from, final Dot to) {
        final float topFrom = from.circle.top;
        final float topTo = to.circle.top;
        final ValueAnimator animator = new ValueAnimator();
        animator.removeAllUpdateListeners();
        animator.setFloatValues(0.0f, 1.0f);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = (float) animation.getAnimatedValue();
                int y = (int) ((1.0 - ratio) * topFrom + ratio * topTo);
                from.circle.offsetTo(from.circle.left, y);
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                int x = from.cX;
                int y = from.cY;
                from.circle.set(x, y, m_cellWidth + x, m_cellHeight + y);
                from.circle.offset(getPaddingLeft(), getPaddingTop());
                from.circle.inset(m_cellWidth * 0.2f, m_cellHeight * 0.2f);

                int col = from.color;
                from.changeColor(to.color);
                to.changeColor(col);
                from.circle.offsetTo(from.circle.left, from.circle.top);

            }
        });
        animations.add(animator);
    }

}
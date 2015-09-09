package game.dots;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Game extends View {
    SharedPreferences m_sp;
    private Rect m_rect;
    private Paint m_paint;
    private int NUM_CELLS, m_cellWidth, m_cellHeight;


    public Game(Context context, AttributeSet attributeSet) {
        /**Initializing*/
        super(context, attributeSet);
        m_sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        m_rect = new Rect();
        m_paint = new Paint();

        /**Getting values and configuring settings*/
        NUM_CELLS = Integer.parseInt(m_sp.getString("gridSize", "6"));

        m_paint.setColor(Color.WHITE);
        m_paint.setStyle(Paint.Style.STROKE);
        m_paint.setStrokeWidth(2);
        m_paint.setAntiAlias(true);
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

        for (int row = 0; row < NUM_CELLS; row++) {
            for (int col = 0; col < NUM_CELLS; col++) {
                int x = col * m_cellWidth;
                int y = row * m_cellHeight;
                m_rect.set(x, y, x + m_cellWidth, y + m_cellHeight);
                m_rect.offset(getPaddingLeft(), getPaddingTop());
                canvas.drawRect(m_rect, m_paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("kiss me");
        }
        return true;
    }
}
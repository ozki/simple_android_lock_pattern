package com.asghonim.salp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordGrid extends GridLayout implements PasswordListener {

    private OnPasswordCompleteListener listener;

    public PasswordGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getInteger(R.integer.path_width));
        paint.setColor(getResources().getColor(R.color.path_color));
        paint.setAlpha(getResources().getInteger(R.integer.path_alpha));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public PasswordGrid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordGrid(Context context) {
        this(context, null, 0);
    }


    private final Paint paint;
    Path path = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        boolean first = true;
        for (Point point : points) {
            if (first) {
                first = false;
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, paint);
    }

    private final List<Point> points = new ArrayList<Point>();

    @Override
    public void onPasswordComplete(String s) {
        if (listener != null) {
            listener.onPasswordComplete(s);
        }
        points.clear();
    }

    private boolean roaming = false;

    @Override
    public void onPasswordButtonTouched(PasswordButton passwordButton) {
        Log.v(PasswordGrid.class.getName(), "TOUCH " + passwordButton.getTag() + " " + Arrays.deepToString(points.toArray()));
        Point point = new Point();
        point.set((int) (passwordButton.getX() + (passwordButton.getMeasuredWidth() / 2)), (int) (passwordButton.getY() + (passwordButton.getMeasuredHeight() / 2)));
        if (points.isEmpty()) {
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof PasswordButton) {
                    getChildAt(i).setBackgroundResource(R.drawable.loginbuttonunpressed);
                }
            }
            points.add(point);
        } else {
            points.set(points.size() - 1, point);
        }
        roaming = false;
        invalidate();
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DRAG_LOCATION) {
            Point p = new Point((int) event.getX(), (int) event.getY());
            if (!roaming) {
                roaming = true;
                points.add(p);
            } else {
                points.set(points.size() - 1, p);
            }
            invalidate();
        }
        return true;
    }

    public void setListener(OnPasswordCompleteListener listener) {
        this.listener = listener;
    }
}

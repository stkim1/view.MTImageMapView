package stkim1.view.MTImageMapView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import stkim1.view.geom.MTPoint;
import stkim1.view.geom.MTPolygon;

public class MTImageMapView extends AppCompatImageView
                            implements ViewTreeObserver.OnGlobalLayoutListener {

    // starts with an identity matrix for an inverse of the touch point matrix

    private Paint pathColor;
    private boolean showPath;
    private Matrix touchConvMat;
    private Rect touchArea;
    private MTImageMapTouch touchedMapReceiver;
    private final List<MTPolygon> polygons = new ArrayList<>();

    // Instance initialization block
    {
        pathColor = new Paint();
        pathColor.setStyle(Paint.Style.STROKE);
        showPath = false;
        touchConvMat = new Matrix();
        touchArea = new Rect();
        touchedMapReceiver = null;
    }

    public MTImageMapView(Context context) {
        super(context);
    }

    public MTImageMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MTImageMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MTImageMapView view = this;
        view.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        MTImageMapView view = this;
        view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        super.onDetachedFromWindow();
    }

    // once this view is measured, layered, and drawn, we can get the actual drawable matrix,
    // which would guide us to map a touch point on the drawable.
    @Override
    public void onGlobalLayout () {
        // drawable frame
        int height = this.getHeight();
        int width = this.getWidth();
        int top = this.getPaddingTop();
        int bottom = this.getPaddingBottom();
        int right = this.getPaddingRight();
        int left = this.getPaddingLeft();

        this.touchArea = new Rect(left, top, width - right, height - bottom);

        // density factor scale
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float scaleX = DisplayMetrics.DENSITY_DEFAULT / metrics.xdpi;
        float scaleY = DisplayMetrics.DENSITY_DEFAULT / metrics.ydpi;

        // If the image matrix cannot be inverted, its inversion will be saved anyway
        // as it will be an identity matrix.
        Matrix invImgMat = new Matrix();
        this.getImageMatrix().invert(invImgMat);
        invImgMat.postScale(scaleX, scaleY);

        this.touchConvMat = invImgMat;
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);

        if (this.showPath) {
            this.polygons.forEach(polygon -> {
                Path path = polygon.getVerticesPath();
                canvas.drawPath(path, this.pathColor);
            });
        }
    }

    // https://developer.android.com/reference/android/view/View#performClick()
    // Call this view's OnClickListener, if it is defined. Performs all normal actions associated
    // with clicking: reporting accessibility event, playing a sound, etc.
    // return : True there was an assigned OnClickListener that was called,
    //          false otherwise is returned.

    @Override
    public boolean performClick () {
        super.performClick();
        return true;
    }

    @Override
    public boolean onTouchEvent (MotionEvent tEvent) {

        // find polygons only when click action happens.
        // when action is 'MotionEvent.ACTION_CANCEL' we'll ignore.
        switch (tEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP: {
                this.performClick();
                break;
            }
            default: {
                return true;
            }
        }

        // when there's no receiver, we don't need to go through point-in-polygons calculations.
        if (this.touchedMapReceiver == null) {
            return true;
        }

        // If there's no polygon, return fast with the zero point to avoid expensive calculations.
        if (this.polygons.isEmpty()) {
            MTPoint tPoint = new MTPoint();
            this.touchedMapReceiver.onImageMapClicked(tEvent, tPoint, Collections.emptyList());
            return true;
        }

        // Convert the touch point for the polygons' DP coordinates.
        double tX = tEvent.getX();
        double tY = tEvent.getY();

       // if the event isn't contained in the touchable area of image, reject and stop
        if (!this.touchArea.contains((int)tX, (int)tY)) {
            MTPoint tPoint = new MTPoint();
            this.touchedMapReceiver.onImageMapClicked(tEvent, tPoint, Collections.emptyList());
            return true;
        }

        // converting the matrix;
        float[] tPoint = {(float)tX - this.touchArea.left, (float)tY - this.touchArea.top};
        if (!this.touchConvMat.isIdentity()) {
            float[] sPoint = Arrays.copyOf(tPoint, tPoint.length);
            this.touchConvMat.mapPoints(tPoint, sPoint);
        }

        // check first check if a point is contained in bounding box.
        // Then see if the point is in polygon.
        MTPoint cPoint = new MTPoint(tPoint);
        List<MTPolygon> tPolygons = this.polygons.stream()
                        .filter(plg -> plg.isPointInBBox(cPoint))
                        .filter(plg -> plg.isPointInPolygon(cPoint))
                        .collect(Collectors.toList());

        this.touchedMapReceiver.onImageMapClicked(tEvent, cPoint, tPolygons);
        return true;
    }

    public void setPathColor(@NonNull Paint pColor) {
        this.pathColor = pColor;
    }

    public void setShowPath(boolean pShow) {
        this.showPath = pShow;
    }

    public void setTouchedMapReceiver(MTImageMapTouch tReceiver) {
        this.touchedMapReceiver = tReceiver;
    }

    public void setPolygons(List<MTPolygon> pList) {
        this.polygons.clear();
        if (pList == null || pList.isEmpty()) {
            return;
        }

        this.polygons.addAll(pList);

        if (this.showPath) {
            invalidate();
        }
    }
}
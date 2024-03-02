package stkim1.view.MTImageMapView;

import android.view.MotionEvent;

import androidx.annotation.NonNull;

import java.util.List;

import stkim1.view.geom.MTPoint;
import stkim1.view.geom.MTPolygon;

public interface MTImageMapTouch {
    void onImageMapClicked(@NonNull MotionEvent event, @NonNull MTPoint point, @NonNull List<MTPolygon> polygons);
}

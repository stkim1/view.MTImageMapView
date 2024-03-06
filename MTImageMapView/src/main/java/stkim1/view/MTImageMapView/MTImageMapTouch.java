package stkim1.view.MTImageMapView;

import android.view.MotionEvent;

import androidx.annotation.NonNull;

import java.util.List;

import stkim1.view.geom.MTPoint;
import stkim1.view.geom.MTPolygon;

/**
 * MTImageMapTouch interface delivers a selected polygon map.
 *
 * @author      stkim1
 * @version     %I%, %G%
 * @since       0.1
 */
public interface MTImageMapTouch {
    /**
     * Passes a touch event on <code>MTImageMapView</code>, a point in the logical space of
     * <code>dp</code> unit, and a <code>List{@literal <}{@link MTPolygon}{@literal >}</code> of
     * selected polygons.
     *
     * @param event unaltered <code>android.view.MotionEvent</code> from
     *      <code>android.view.View</code>.<p>
     *
     * @param point a <code>MTPoint</code> in the logical space of <code>dp</code> unit.
     *      If (1) <code>MTImageMapView</code> does not have a polygon map, or (2) the touch event
     *      happens out of the valid view area, a zero point <code>MTPoint</code> object will be
     *      delivered. Otherwise, a <code>MTPoint</code> object in the logical space of
     *      <code>dp</code> unit will come.<p>
     *
     * @param polygons a <code>List{@literal <}{@link MTPolygon}{@literal >}</code> of selected polygons.
     *      If (1) no polygon is selected, (2) <code>MTImageMapView</code> does not contains a polygon
     *      map, (3) the touch event happens out of the valid view area, the polygon list will be
     *      an empty list.
     */
    void onImageMapClicked(@NonNull MotionEvent event, @NonNull MTPoint point, @NonNull List<MTPolygon> polygons);
}

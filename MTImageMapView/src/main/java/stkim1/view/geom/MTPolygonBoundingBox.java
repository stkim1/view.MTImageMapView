package stkim1.view.geom;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * MTPolygonBoundingBox is a utility class of MTPolygon.
It does not thoroughly check the sanity of input values.
 * Thus, when you are to utilize outside of MTPolygon,
 * be aware of its uninitialized min and max values.
 *
 * @author      stkim1
 * @version     %I%, %G%
 * @since       0.1
 */
class MTPolygonBoundingBox {

    MTPoint minVertex, maxVertex;

    MTPolygonBoundingBox() {
        super();
    }

    void findBox(@NonNull List<MTPoint> vts) {
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        final int vsz = vts.size();

        for (int i = 0; i < vsz; i++) {
            MTPoint p = vts.get(i);
            if (p.x < minX) {
                minX = p.x;
            }
            if (p.y < minY) {
                minY = p.y;
            }
            if (maxX < p.x) {
                maxX = p.x;
            }
            if (maxY < p.y) {
                maxY = p.y;
            }
        }

        this.minVertex = new MTPoint(minX, minY);
        this.maxVertex = new MTPoint(maxX, maxY);
    }
}

package stkim1.view.geom;

import android.graphics.Path;

import androidx.annotation.NonNull;

import java.io.InvalidObjectException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MTPolygon {

    // polygon ID is not enforced as it is here for an utility field, i.e. you can implement an id
    // in case you need a fine-grained indexing of polygons.
    private Object polygonId = null;
    private final ArrayList<MTPoint> vertices = new ArrayList<MTPoint>();
    private final MTPolygonBoundingBox boundingBox = new MTPolygonBoundingBox();
    private boolean closed = false;

    // The points in the polygon is henceforth called vertices (vts).
    // when a polygon is instantiated with vertices, it becomes a final polygon,
    // which cannot be further modified. Thus, we check if at least three vertices are provided,
    // and close this polygon if condition is met.
    public MTPolygon(Object pid, @NonNull List<MTPoint> vts) throws Exception {
        super();

        if (pid != null) {
            this.polygonId = pid;
        }

        if (vts.size() < 3) {
            throw new InvalidParameterException("MTPolygon must be instantiated with at least three vertices.");
        }

        this.vertices.addAll(vts);

        // now this polygon cannot be modified further
        this.closed = true;

        // find bounding box of this polygon
        this.boundingBox.findBox(this.vertices);
    }

    public MTPolygon() {
        super();
    }

    public void setPolygonId(Object pid) {
            this.polygonId = pid;
    }

    public Object getPolygonId() {
        return this.polygonId;
    }

    public void addVertex(@NonNull MTPoint v) throws Exception {
        if (this.closed) {
            throw new InvalidObjectException("A closed MTPolygon cannot take a further vertex.");
        }
        this.vertices.add(v);
    }

    public void addVertices(@NonNull MTPoint[] vts) throws Exception {
        if (this.closed) {
            throw new InvalidObjectException("A closed MTPolygon cannot take a further vertex.");
        }
        if (vts.length == 0) {
            throw new InvalidParameterException("You need to provide at least one point to add.");
        }
        Collections.addAll(this.vertices, vts);
    }

    public void close() throws Exception {
        if (this.vertices.size() < 3) {
            throw new InvalidObjectException("MTPolygon must have at least three vertices.");
        }

        // now this polygon cannot be modified further.
        this.closed = true;

        // find bounding box of this polygon
        this.boundingBox.findBox(this.vertices);
    }

    // when a polygon is closed, you cannot add a point to it and it becomes a final
    public boolean isClosed() {
        return this.closed;
    }

    public Path getVerticesPath() {
        Path path = new Path();

        int vsz = this.vertices.size();
        for (int i = 0; i < vsz; i++) {
            MTPoint v = this.vertices.get(i);
            if (i == 0) {
                path.moveTo((float)v.x,(float)v.y);
            } else {
                path.lineTo((float)v.x,(float)v.y);
            }
        }

        if (this.closed) {
            path.close();
        }

        return path;
    }

    public boolean isPointInBBox(@NonNull MTPoint point) {
        return checkIfPointInBBox(this, point);
    }

    public boolean isPointInPolygon(@NonNull MTPoint point) {
        return (0 != fastWindingNumber(this, point));
    }

    // It is possible to upgrade a simple Bounding Box checker into a diamond bound checker
    private static boolean checkIfPointInBBox(@NonNull MTPolygon polygon, @NonNull MTPoint point) {
        // if a polygon is not closed, the point is not contained. simple.
        if (!polygon.closed) {
            return false;
        }

        return polygon.boundingBox.minVertex.x <= point.x &&
                point.x <= polygon.boundingBox.maxVertex.x &&
                polygon.boundingBox.minVertex.y <= point.y &&
                point.y <= polygon.boundingBox.maxVertex.y;
    }


    // This portion is from Dan Sunday's Fast Winding Number Algorithm
    //
    // Copyright 2000 softSurfer, 2012 Dan Sunday
    // This code may be freely used and modified for any purpose
    // providing that this copyright notice is included with it.
    // SoftSurfer makes no warranty for this code, and cannot be held
    // liable for any real or imagined damage resulting from its use.
    // Users of this code must verify correctness for their application.
    //
    // fastWindingNumber(): winding number test for a point in a polygon
    //      Input : polygon = vertex points of a polygon V[n+1] with V[n]=V[0]
    //              point = a point
    //      Return:  wn = the winding number (=0 only when the point is outside)
    private static int fastWindingNumber(@NonNull MTPolygon polygon, @NonNull MTPoint point) {
        // if a polygon is not closed, the point is not contained.
        if (!polygon.isClosed()) {
            return 0;
        }

        // loop through all edges of the polygon
        int wn = 0;
        int vsz = polygon.vertices.size();
        for (int i = 0; i < vsz; i++) {
            MTPoint v0 = polygon.vertices.get(i);
            MTPoint v1;
            if (i == vsz - 1) {
                v1 = polygon.vertices.get(0);
            } else {
                v1 = polygon.vertices.get(i + 1);
            }

            // start v0.y <= point.y
            if (v0.y <= point.y) {
                // an upward crossing
                if (v1.y > point.y) {
                    // if the point is at the left of edge (v0-v1), we have a valid up intersect.
                    if (point.isLeft(v0, v1) > 0) {
                        ++wn;
                    }
                }

            // start v0.y > point.y (no test needed)
            } else {
                // a downward crossing
                if (v1.y <= point.y) {
                    // if the point is at the right of edge (v0-v1), we have a valid down intersect.
                    if (point.isLeft(v0, v1) < 0) {
                        --wn;
                    }
                }
            }
        }
        return wn;
    }
}
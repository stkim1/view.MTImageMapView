package stkim1.view.geom;

import android.graphics.Path;

import androidx.annotation.NonNull;

import java.io.InvalidObjectException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MTPolygon contains a polygon map made with a group of <code>MTPoint</code>, and can check
 * if a point is contained.
 *
 * @author      stkim1
 * @version     %I%, %G%
 * @since       0.1
 */
public class MTPolygon {

    private Object polygonId = null;
    private final ArrayList<MTPoint> vertices = new ArrayList<MTPoint>();
    private final MTPolygonBoundingBox boundingBox = new MTPolygonBoundingBox();
    private boolean closed = false;

    /**
     * The points in MTPolygon is henceforth called <code>vertices</code>. When a polygon is
     * instantiated with vertices, it becomes a closed polygon, which cannot be further modified.
     * Thus, we check if at least three vertices are provided, and close the spawned polygon
     * if the condition is met.
     * <p>
     * polygon id is not enforced as it is an utility, i.e. you can implement an id only if you need
     * to identify or index polygons.
     *
     * @param pid a nullable <code>Object</code> as the id of a polygon.
     * @param vts a list of at least three or more points (vertices).
     * @throws NullPointerException if the vertices list in the arguments is null.
     * @throws InvalidParameterException if the number of vertices are less than three.
     */
    public MTPolygon(Object pid, @NonNull List<MTPoint> vts) throws Exception {
        super();

        if (vts.size() < 3) {
            throw new InvalidParameterException("MTPolygon must be instantiated with at least three vertices.");
        }

        if (pid != null) {
            this.polygonId = pid;
        }

        this.vertices.addAll(vts);

        // now this polygon cannot be modified further
        this.closed = true;

        // find bounding box of this polygon
        this.boundingBox.findBox(this.vertices);
    }

    /**
     * When a polygon is instantiated without vertices, it is an open polygon, which can be further
     * modified.
     */
    public MTPolygon() {
        super();
    }

    /**
     * A polygon id is to identify which polygon is selected. Id can be set to anything;
     * <code>String</code>, <code>Object</code>, and/or <code>null</code> if you want.
     * Polygon id is not enforced as it is an utility, i.e. you can implement an id only if you need
     * to identify or index polygons.
     *
     * @param pid a nullable <code>Object</code> as the id of a polygon.
     */
    public void setPolygonId(Object pid) {
            this.polygonId = pid;
    }

    /**
     * A polygon id is to identify which polygon is selected.
     *
     * @return the polygon id
     */
    public Object getPolygonId() {
        return this.polygonId;
    }

    /**
     * A point in MTPolygon is henceforth called <code>vertex</code>. When a polygon is
     * instantiated without vertices, it is an open polygon, which can take more vertex.
     *
     * @param vertex a vertex of <code>MTPoint</code>.
     * @throws NullPointerException if the vertex is null.
     * @throws InvalidObjectException if this polygon is already closed
     *      and you are trying to add another vertex.
     */
    public void addVertex(@NonNull MTPoint vertex) throws Exception {
        if (this.closed) {
            throw new InvalidObjectException("A closed MTPolygon cannot take a further vertex.");
        }
        this.vertices.add(vertex);
    }

    /**
     * Points in MTPolygon is called <code>vertices</code>. When a polygon is instantiated without
     * vertices, it is an open polygon, which can take more vertices.
     *
     * @param vts a list of vertices in <code>MTPoint</code>.
     * @throws NullPointerException if the vertices is null.
     * @throws InvalidObjectException if this polygon is already closed
     *          and you are trying to add more vertices.
     * @throws InvalidParameterException if the vertices list is empty.
     */
    public void addVertices(@NonNull MTPoint[] vts) throws Exception {
        if (this.closed) {
            throw new InvalidObjectException("A closed MTPolygon cannot take a further vertex.");
        }
        if (vts.length == 0) {
            throw new InvalidParameterException("You need to provide at least one point to add.");
        }
        Collections.addAll(this.vertices, vts);
    }

    /**
     * An open polygon can be closed only if at least three <code>MTPoint</code> vertices are
     * present in its <code>vertices</code> list. Otherwise, the polygon throws an exception.
     *
     * @throws InvalidObjectException if there is less than three vertices in the
     *      <code>vertices</code> list and you are trying to close this polygon.
     */
    public void close() throws Exception {
        if (this.vertices.size() < 3) {
            throw new InvalidObjectException("MTPolygon must have at least three vertices.");
        }

        // now this polygon cannot be modified further.
        this.closed = true;

        // find bounding box of this polygon
        this.boundingBox.findBox(this.vertices);
    }

    /**
     * When a polygon is closed, you cannot add a vertex to it.
     *
     * @return <code>true</code> if this polygon is closed, <code>false</code> otherwise.
     */
    public boolean isClosed() {
        return this.closed;
    }

    /**
     * The vertices in this polygon can be drawn on the screen in a line with
     * <code>android.graphics.Path</code> for debugging purpose. The polygon line will be closed
     * if this polygon is closed, otherwise the line is open.
     * <p>
     * This is expensive function as it spawns <code>android.graphics.Path</code> object. It becomes
     * as much expensive as the number of polygons and vertices increases.
     * Please use it with caution at your discretion.
     *
     * @return <code>android.graphics.Path</code> of <code>vertices</code>
     */
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

    /**
     * Tells you if a point is in the bonding box of this polygon. This is to quickly cull unlikely
     * polygons and to speed up pin-pointing exact polygons the point is contained in.
     *
     * @param point a point in <code>MTPoint</code>.
     * @return <code>true</code> if the point is in the bounding box of this polygon,
     *      otherwise <code>false</code>.
     */
    public boolean isPointInBBox(@NonNull MTPoint point) {
        return checkIfPointInBBox(this, point);
    }

    /**
     * Tells you if a point is in this polygon. Dan Sunday's "Fast Winding Number Algorithm" is
     * implemented to check precisely if the point really is contained in this polygon.
     *
     * @param point a point in <code>MTPoint</code>.
     * @return <code>true</code> if the point is contained in this polygon,
     *      otherwise <code>false</code>.
     */
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
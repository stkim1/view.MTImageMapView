package stkim1.view.geom;

import androidx.annotation.NonNull;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * MTPoint contains a single (x,y) coordinate of 1) a polygon map
 * or 2) a touch point in the logical space of <code>dp</code> unit.
 *
 * @author      stkim1
 * @version     $I$, $G$
 * @since       0.1
 */
public class MTPoint {
    public final double x, y;

    /**
     * Build a <code>MTPoint</code> object to (0, 0).
     */
    public MTPoint() {
        super();
        this.x = 0.0;
        this.y = 0.0;
    }

    /**
     * Construct a <code>MTPoint</code> object to (x, y).
     *
     * @param  x  x part of a coordinate
     * @param  y  y part of a coordinate
     */
    public MTPoint(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a <code>MTPoint</code> object to (x, y).
     * The coordinate argument must be a <code>float[]</code> array in the length of two (2).
     *
     * @param  point a coordinate of (x, y) pair in <code>float[]</code> array.
     * @throws IllegalArgumentException if the argument is <code>null</code>.
     * @throws InvalidParameterException If the argument array length is not <code>two (2)</code>.
     */
    public MTPoint(@NonNull float[] point) {
        super();
        if (point.length != 2) {
            throw new InvalidParameterException("MTPoint must be instantiated with two points.");
        }
        this.x = point[0];
        this.y = point[1];
    }

    /**
     * Returns a <code>MTPoint</code> object to (x, y).
     * The coordinate argument must be a <code>double[]</code> array in the length of two (2).
     *
     * @param point a coordinate of (x, y) pair in <code>double[]</code> array.
     * @throws IllegalArgumentException if the argument is <code>null</code>.
     * @throws InvalidParameterException If the argument array length is not <code>two (2)</code>.
     */
    public MTPoint(@NonNull double[] point) {
        super();
        if (point.length != 2) {
            throw new InvalidParameterException("MTPoint must be instantiated with two points.");
        }
        this.x = point[0];
        this.y = point[1];
    }

    /**
     * Returns a <code>MTPoint</code> object to (x, y).
     * The coordinate argument must be a <code>List{@literal <}{@link Double}{@literal >}</code>
     * array in the size of two (2).
     *
     * @param  point a coordinate of (x, y) pair in <code>List{@literal <}{@link Double}{@literal >}</code>
     * @throws IllegalArgumentException if the argument is <code>null</code>.
     * @throws InvalidParameterException If the argument list size is not <code>two (2)</code>.
     */
    public MTPoint(@NonNull List<Double> point) {
        super();
        if (point.size() != 2) {
            throw new InvalidParameterException("MTPoint must be instantiated with two points.");
        }
        this.x = point.get(0).doubleValue();
        this.y = point.get(1).doubleValue();
    }

    // This portion is from Dan Sunday's Fast Winding Number Algorithm
    //
    // Copyright 2000 softSurfer, 2012 Dan Sunday
    // This code may be freely used and modified for any purpose
    // providing that this copyright notice is included with it.
    // SoftSurfer makes no warranty for this code, and cannot be held
    // liable for any real or imagined damage resulting from its use.
    // Users of this code must verify correctness for their application.

    // isLeft(): tests if the object's point (hence the point) is Left|On|Right of an infinite line.
    // Input   : the point, P0, and P1
    // Return  : >0 for the point is at the left of the line through P0 and P1
    //           =0 for the point is on the line
    //           <0 for the point is at the right of the line
    int isLeft(@NonNull MTPoint P0 ,@NonNull MTPoint P1) {
        return (int)( (P1.x - P0.x) * (this.y - P0.y) - (this.x -  P0.x) * (P1.y - P0.y) );
    }
}
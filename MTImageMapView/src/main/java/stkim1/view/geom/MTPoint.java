package stkim1.view.geom;

import androidx.annotation.NonNull;

import java.security.InvalidParameterException;
import java.util.List;

public class MTPoint {
    public final double x, y;

    public MTPoint() {
        super();
        this.x = 0.0;
        this.y = 0.0;
    }

    public MTPoint(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public MTPoint(@NonNull float[] point) {
        super();
        if (point.length != 2) {
            throw new InvalidParameterException("MTPoint must be instantiated with two points.");
        }
        this.x = point[0];
        this.y = point[1];
    }

    public MTPoint(@NonNull double[] point) {
        super();
        if (point.length != 2) {
            throw new InvalidParameterException("MTPoint must be instantiated with two points.");
        }
        this.x = point[0];
        this.y = point[1];
    }

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
    public int isLeft( MTPoint P0, MTPoint P1) {
        return (int)( (P1.x - P0.x) * (this.y - P0.y) - (this.x -  P0.x) * (P1.y - P0.y) );
    }
}
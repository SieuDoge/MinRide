package utils;

import models.Location;

public class distance {
    // Tính khoảng cách Euclidean giữa 2 điểm (x1, y1) và (x2, y2)
    public static double calculate(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // Tính khoảng cách giữa 2 Location object
    public static double calculate(Location loc1, Location loc2) {
        return calculate(loc1.getX(), loc1.getY(), loc2.getX(), loc2.getY());
    }
}
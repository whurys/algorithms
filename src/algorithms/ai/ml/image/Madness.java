package algorithms.ai.ml.image;

import algorithms.io.StdDraw;

public class Madness {

    public static void main(String[] args) {
        StdDraw.setXscale(-1.7, +1.7);
        StdDraw.setYscale(-1.1, +1.1);
        double x0 = -0.7, y0 = 1.0;
        for (double t = 0.0; true; t += 0.01) {
            double x1 = Math.sin(0.99*t) - 0.7*Math.cos(3.01*t);
            double y1 = Math.cos(1.01*t) + 0.1*Math.sin(15.03*t);
            StdDraw.line(x0, y0, x1, y1);
            x0 = x1;
            y0 = y1;
        }
    }
}

import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import java.util.TreeSet;
import edu.princeton.cs.algs4.StdDraw;
/**
 * Created by zhanlgu on 16/10/22.
 */
public class PointSET {
    private TreeSet<Point2D> pointSet;

    public PointSET() {
        pointSet = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D p) {
        pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        return pointSet.contains(p);
    }

    public void draw() {
        for (Point2D p : pointSet)
            StdDraw.point(p.x(), p.y());
    }

    public Iterable<Point2D> range(RectHV rect) {
        TreeSet<Point2D> rangeSet = new TreeSet<Point2D>();
        for (Point2D p : pointSet) {
            if (rect.contains(p))
                rangeSet.add(p);
        }
        return rangeSet;
    }

    public Point2D nearest(Point2D p) {
        Point2D nearestPoint = null;
        double minDistance = Double.MAX_VALUE;
        for (Point2D point : pointSet) {
            double distance = point.distanceTo(p);
            if (distance < minDistance) {
                nearestPoint = point;
                minDistance = distance;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {

    }
}

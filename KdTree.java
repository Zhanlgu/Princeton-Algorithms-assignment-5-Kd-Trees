import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
/**
 * Created by zhanlgu on 16/10/22.
 */
public class KdTree {
    private static class KdNote {
        private Point2D p;
        private RectHV rect;
        private KdTree.KdNote lb;
        private KdTree.KdNote rt;

        public KdNote(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            lb = null;
            rt = null;
        }
    }

    private KdNote root;
    private int size;
    private RectHV canvas = new RectHV(0, 0, 1, 1);

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private int compareTo(Point2D x, Point2D y, int isVertical) {
        if (x.equals(y)) return 0;
        if (isVertical == 1) {
            if (x.x() < y.x()) return -1;
            else return 1;
        } else {
            if (x.y() < y.y()) return -1;
            else return 1;
        }
    }

    public void insert(Point2D p) {
        root = insert(root, p, 1,
                canvas.xmin(), canvas.ymin(), canvas.xmax(), canvas.ymax());
    }

    private KdNote insert(KdNote note, Point2D p, int isVertical,
                         double xmin, double ymin, double xmax, double ymax) {
        if (note == null) {
            size++;
            return new KdNote(p, new RectHV(xmin, ymin, xmax, ymax));
        }
        int cmpResult = compareTo(p, note.p, isVertical);
        double xxmin = xmin, yymin = ymin, xxmax = xmax, yymax = ymax;
        if (cmpResult < 0) {
            if (isVertical == 1) xxmax = note.p.x();
            else yymax = note.p.y();
            note.lb = insert(note.lb, p, 1-isVertical, xxmin, yymin, xxmax, yymax);
        }
        if (cmpResult > 0) {
            if (isVertical == 1) xxmin = note.p.x();
            else yymin = note.p.y();
            note.rt = insert(note.rt, p, 1-isVertical, xxmin, yymin, xxmax, yymax);
        }
        return note;
    }

    public boolean contains(Point2D p) {
        return contains(root, p, 1);
    }

    private boolean contains(KdNote note, Point2D p, int isVertical) {
        if (note == null) return false;
        int cmpResult = compareTo(p, note.p, isVertical);
        if (cmpResult < 0)
            return contains(note.lb, p, 1-isVertical);
        if (cmpResult > 0)
            return  contains(note.rt, p, 1-isVertical);
        return true;
    }

    public void draw() {
        StdDraw.setScale();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        canvas.draw();
        draw(root, 1);
    }

    private void draw(KdNote note, int isVertical) {
        if (note == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        note.p.draw();
        if (isVertical == 1) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(note.p.x(), note.rect.ymin(), note.p.x(), note.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(note.rect.xmin(), note.p.y(), note.rect.xmax(), note.p.y());
        }
        draw(note.lb, 1-isVertical);
        draw(note.rt, 1-isVertical);
    }

    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> inPointQ = new Queue<Point2D>();
        Queue<KdNote> needSearchArea = new Queue<KdNote>();
        if (root == null) return inPointQ;
        needSearchArea.enqueue(root);
        while (!needSearchArea.isEmpty()) {
            KdNote note = needSearchArea.dequeue();
            // if (rect.distanceSquaredTo(note.p) == 0) inPointQ.enqueue(note.p);
            if (rect.contains(note.p)) inPointQ.enqueue(note.p);
            if (note.lb != null && rect.intersects(note.lb.rect))
                needSearchArea.enqueue(note.lb);
            if (note.rt != null && rect.intersects(note.rt.rect))
                needSearchArea.enqueue(note.rt);
        }
        return inPointQ;
    }

    public Point2D nearest(Point2D p) {
        if (root == null) return null;
        Stack<KdNote> needSearchPoint = new Stack<KdNote>();
        Point2D nearestPoint = null;
        double minDistance = Double.MAX_VALUE;
        needSearchPoint.push(root);
        while (!needSearchPoint.isEmpty()) {
            KdNote note = needSearchPoint.pop();
            double distance = note.p.distanceSquaredTo(p);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = note.p;
            }
            if (note.lb != null && note.lb.rect.contains(p)) {
                if (note.rt != null && note.rt.rect.distanceSquaredTo(p) < minDistance)
                    needSearchPoint.push(note.rt);
                needSearchPoint.push(note.lb);
            } else if (note.rt != null && note.rt.rect.contains(p)) {
                if (note.lb != null && note.lb.rect.distanceSquaredTo(p) < minDistance)
                    needSearchPoint.push(note.lb);
                needSearchPoint.push(note.rt);
            } else {
                if (note.lb != null && note.lb.rect.distanceSquaredTo(p) < minDistance)
                    needSearchPoint.push(note.lb);
                if (note.rt != null && note.rt.rect.distanceSquaredTo(p) < minDistance)
                    needSearchPoint.push(note.rt);
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {

    }
}
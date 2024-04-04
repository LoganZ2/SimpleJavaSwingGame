import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import lombok.Data;
@Data

public abstract class Movable {
    private int x;
    private int y;
    private int width;
    private int height;
    private Polygon shape;
    private int[] xPoints;
    private int[] yPoints;
    private int nPoints;
    private int[] centroid;
    private int velocityX;
    private int velocityY;
    private boolean hostile;

    public boolean collision(ArrayList<Movable> movableList) {
        for (Movable m: movableList) {
            if (this == m) {
                continue;
            }
            boolean alignX = this.x == m.x + m.width || this.x + this.width == m.x || this.x == m.x || this.x + this.width == m.x + m.width;
            boolean alignY = this.y == m.y + m.height || this.y + this.height == m.y || this.y == m.y || this.y + this.height == m.y + m.height;
            if (alignX && alignY) {
                return true;
            }
        }
        return false;
    }
    public void setShape(int[] xPoints, int[] yPoints, int nPoints) {
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.nPoints = nPoints;
        shape = new Polygon(xPoints, yPoints, nPoints);
        x = (int)shape.getBounds2D().getX();
        y = (int)shape.getBounds2D().getY();
        width = (int)shape.getBounds2D().getWidth();
        height = (int)shape.getBounds2D().getHeight();
        this.centroid = new int[]{(x + width) / 2, (y + height) / 2};
    }
    public void setShape() {
        shape = new Polygon(xPoints, yPoints, nPoints);
        x = (int)shape.getBounds2D().getX();
        y = (int)shape.getBounds2D().getY();
        width = (int)shape.getBounds2D().getWidth();
        height = (int)shape.getBounds2D().getHeight();
        this.centroid = new int[]{(x + width) / 2, (y + height) / 2};
    }


    public void moveUp(int step, int height, int width) {
        if (y > 0) yPoints = Arrays.stream(yPoints).map(y -> y - step).toArray();
        setShape();
    }
    public void moveDown(int step, int height, int width) {
        if (y + this.height < height) yPoints = Arrays.stream(yPoints).map(y -> y + step).toArray();
        setShape();
    }
    public void moveLeft(int step, int height, int width) {
        if (x > 0) xPoints = Arrays.stream(xPoints).map(x -> x - step).toArray();
        setShape();
    }
    public void moveRight(int step, int height, int width) {
        if (x + this.width < width) xPoints = Arrays.stream(xPoints).map(x -> x + step).toArray();
        setShape();
    }

    public static void setPosition(Movable m, int x, int y) {
        m.setXPoints(Arrays.stream(m.getXPoints()).map(p -> p + x).toArray());
        m.setYPoints(Arrays.stream(m.getYPoints()).map(p -> p + y).toArray());
        m.setShape();
    }
}

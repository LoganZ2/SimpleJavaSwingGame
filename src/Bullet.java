import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;
@Data

public class Bullet extends Movable {
    public Bullet(int[] xPoints, int[] yPoints, int nPoints, boolean hostile) {
        setXPoints(xPoints);
        setYPoints(yPoints);
        setNPoints(nPoints);
        setHostile(hostile);

    }


    public Bullet up(int step, int height, int width) {
        List<Bullet> toRemove = new ArrayList<>();
        if (this.getY() > 0) {
            setYPoints(Arrays.stream(getYPoints()).map(y -> y - step).toArray());
            setShape();
        } else {
            return this;
        }
        return null;
    }
    public Bullet down(int step, int height, int width) {
        List<Bullet> toRemove = new ArrayList<>();
        if (this.getY() < height) {
            setYPoints(Arrays.stream(getYPoints()).map(y -> y + step).toArray());
            setShape();
        } else {
            return this;
        }
        return null;
    }
    public Bullet left(int step, int height, int width) {
        List<Bullet> toRemove = new ArrayList<>();
        if (this.getX() > 0) {
            setXPoints(Arrays.stream(getYPoints()).map(x -> x - step).toArray());
            setShape();
        } else {
            return this;

        }
        return null;
    }
    public Bullet right(int step, int height, int width) {
        List<Bullet> toRemove = new ArrayList<>();
        if (this.getX() < width) {
            setXPoints(Arrays.stream(getYPoints()).map(x -> x - step).toArray());
            setShape();
        } else {
            return this;
        }
        return null;
    }
    @Override
    public boolean collision(ArrayList<Movable> movableList) {
        for (Movable m: movableList) {
            boolean check = this.equals(m) || !(m.isHostile() ^ this.isHostile());
            if (check) continue;
            boolean alignX = this.getX() <= m.getX() + m.getWidth() && this.getX() + this.getWidth() >= m.getX();
            boolean alignY = this.getY() <= m.getY() + m.getHeight() && this.getY() + this.getHeight() >= m.getY();
            if (alignX && alignY) return true;
        }
        return false;
    }
    @Override
    @Deprecated
    public void moveUp(int step, int height, int width) {
    }
    @Override
    @Deprecated
    public void moveDown(int step, int height, int width) {
    }
    @Override
    @Deprecated
    public void moveLeft(int step, int height, int width) {
    }
    @Override
    @Deprecated
    public void moveRight(int step, int height, int width) {
    }
}

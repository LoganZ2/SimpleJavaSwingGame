import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;


@Data
public class Plane extends Movable{
    private boolean alive;
    private int health;
    private int damage;
    private int bounceCount;
    private int[] bounceDirection = {0,0};
    public Plane() {

    }
    public Plane(int health, int damage, int velocity) {
        this.health = health;
        this.damage = damage;
        this.setVelocityX(velocity);
        this.setVelocityY(velocity);
    }
    public void attack(Bullet bullet, ArrayList<Movable> movableList, LinkedList<Bullet> bulletList) {
        bulletList.offer(bullet);
        movableList.add(bullet);
        setPosition(bullet, this.getX(), this.getY());
    }
    public void attacked(int damage) {
        health -= damage;
    }

    public void bounce(int height, int width) {
        int x = bounceDirection[0];
        int y = bounceDirection[1];
        if (x < 0)  {
            moveLeft(-x, height, width);
        } else {
            moveRight(x, height, width);
        }
        if (y < 0) {
            moveUp(-y, height, width);
        } else {
            moveDown(y, height, width);
        }
        this.bounceCount -= 1;
        if (bounceCount == 0) {
            bounceDirection = new int[]{0,0};
        }
    }

    @Override
    public boolean collision(ArrayList<Movable> movableList) {
        for (Movable m : movableList) {
            boolean check = this.equals(m);
            check = check || !(m.isHostile() ^ this.isHostile());
            if (check) {
                continue;
            }

            boolean alignX = this.getX() <= m.getX() + m.getWidth() && this.getX() + this.getWidth() >= m.getX();
            boolean alignY = this.getY() <= m.getY() + m.getHeight() && this.getY() + this.getHeight() >= m.getY();
            if (m instanceof Bullet) {
                alignX = this.getX() -15 <= m.getX() + m.getWidth() && this.getX() + this.getWidth() +15 >= m.getX();
                alignY = this.getY() -15 <= m.getY() + m.getHeight() && this.getY() + this.getHeight() +15 >= m.getY();
            }

            if (alignX && alignY) {
                // 计算两个物体中心的差异
                double dx = (this.getX() + this.getWidth() / 2.0) - (m.getX() + m.getWidth() / 2.0);
                double dy = (this.getY() + this.getHeight() / 2.0) - (m.getY() + m.getHeight() / 2.0);

                // 评估碰撞面并决定反弹方向
                if (Math.abs(dx) > Math.abs(dy)) {
                    // 水平方向的重叠更少，垂直方向发生碰撞
                    bounceDirection[0] = dx > 0 ? 1 : -1; // 如果dx为正，说明this在m的右侧，应向右反弹
                    bounceDirection[1] = 0;
                } else {
                    // 垂直方向的重叠更少，水平方向发生碰撞
                    bounceDirection[0] = 0;
                    bounceDirection[1] = dy > 0 ? 1 : -1; // 如果dy为正，说明this在m的下方，应向下反弹
                }

                bounceCount = 25; // 根据需要调整
                return true;
            }
        }
        return false;
    }




}

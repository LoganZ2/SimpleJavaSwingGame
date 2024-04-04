import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleGame extends JFrame {

    public SimpleGame() {
        this.setTitle("Simple Java Game");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new GamePanel());
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleGame();
    }

    class GamePanel extends JPanel {

        int counter = 0;
        private int x = 100;
        private int y = 100;
        private final int SIZE = 50;
        private final int STEP = 5;
        char[] score  = {'0'};

        private boolean upPressed = false;
        private boolean downPressed = false;
        private boolean leftPressed = false;
        private boolean rightPressed = false;
        private boolean attack = false;
        private Plane plane1 = new Plane(10, 10, 1);
        private Plane plane2 = new Plane(10, 20, 5);
        int[] xP1 = {350, 300, 400};
        int[] yP1 = {300, 400, 400};

        int[] xPoints = {412, 412, 411, 411, 410, 409, 407, 406, 404, 402, 400, 397, 395, 392, 389, 386, 383, 380, 376, 372, 369, 365, 361, 357, 353, 350, 346, 342, 338, 334, 330, 327, 323, 319, 316, 313, 310, 307, 304, 302, 299, 297, 295, 293, 292, 290, 289, 288, 288, 287, 287, 287, 288, 288, 289, 290, 292, 293, 295, 297, 299, 302, 304, 307, 310, 313, 316, 319, 323, 327, 330, 334, 338, 342, 346, 350, 353, 357, 361, 365, 369, 372, 376, 380, 383, 386, 389, 392, 395, 397, 400, 402, 404, 406, 407, 409, 410, 411, 411, 412};

        int[] yPoints = {366, 370, 374, 378, 382, 385, 389, 393, 396, 400, 403, 406, 409, 412, 414, 417, 419, 421, 423, 424, 425, 426, 427, 428, 428, 428, 428, 428, 427, 426, 425, 424, 423, 421, 419, 417, 414, 412, 409, 406, 403, 400, 396, 393, 389, 385, 382, 378, 374, 370, 366, 362, 358, 354, 351, 347, 343, 340, 336, 333, 330, 326, 324, 321, 318, 316, 314, 312, 310, 308, 307, 306, 305, 304, 304, 304, 304, 304, 305, 306, 307, 308, 310, 312, 314, 316, 318, 321, 324, 326, 330, 333, 336, 340, 343, 347, 351, 354, 358, 362};

        int[] xP2 = {10, 0, 20};
        int[] yP2 = {0, 20, 20};
        private ArrayList<Movable> movableList = new ArrayList<>();
        private HashMap<Movable, LinkedList<Bullet>> bulletList = new HashMap<>();
        public void register(Plane plane, int[] xPoints, int[] yPoints, int nPoints) {
            plane.setShape(xPoints, yPoints, nPoints);
            bulletList.put(plane, new LinkedList<Bullet>());
            movableList.add(plane);
        }
        public void register(Plane plane, int[] xPoints, int[] yPoints, int nPoints, int initialPositionX, int initialPositionY) {
            plane.setShape(xPoints, yPoints, nPoints);
            bulletList.put(plane, new LinkedList<Bullet>());

            movableList.add(plane);
            Movable.setPosition(plane, initialPositionX, initialPositionY);
        }
        public GamePanel() {
            setFocusable(true);
            requestFocusInWindow();
            register(plane1, xP2, yP2, 3, 100, 100);
            register(plane2, xPoints, yPoints, 100);
            plane2.setHostile(true);
            plane1.setHostile(false);

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            upPressed = true;
                            break;
                        case KeyEvent.VK_DOWN:
                            downPressed = true;
                            break;
                        case KeyEvent.VK_LEFT:
                            leftPressed = true;
                            break;
                        case KeyEvent.VK_RIGHT:
                            rightPressed = true;
                            break;
                        case KeyEvent.VK_J:
                            attack = true;

                            break;
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            upPressed = false;
                            break;
                        case KeyEvent.VK_DOWN:
                            downPressed = false;
                            break;
                        case KeyEvent.VK_LEFT:
                            leftPressed = false;
                            break;
                        case KeyEvent.VK_RIGHT:
                            rightPressed = false;
                            break;
                    }
                }
            });

            startGameLoop();
        }
        public void playerMove() {
            if (upPressed)
                plane1.moveUp(STEP, getHeight(), getWidth());
            if (downPressed)
                plane1.moveDown(STEP, getHeight(), getWidth());
            if (leftPressed)
                plane1.moveLeft(STEP, getHeight(), getWidth());
            if (rightPressed)
                plane1.moveRight(STEP, getHeight(), getWidth());
            if (attack) {
                Bullet bb = new Bullet(xP2, yP2, 3, false);
                plane1.attack(bb, movableList, bulletList.get(plane1));
                attack = false;
            }
        }

        public void enemyMove() {


        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Movable m : movableList) {
                g.fillPolygon(m.getShape());
//                g.drawRect(m.getX(),m.getY(),m.getWidth(),m.getHeight());
            }
        }

        private void update() {
            ArrayList<Plane> planeList = movableList.stream().filter(m -> m instanceof Plane).map(m -> (Plane)m).collect(Collectors.toCollection(ArrayList::new));
            for (Plane plane : planeList) {
                plane.collision(movableList);
                if (plane.getBounceCount() > 0){
                    plane.bounce(getHeight(), getWidth());
                }
                else {
                    playerMove();
                    enemyMove();
                }
            }
            ArrayList<Bullet> bulletsCopy = new ArrayList<>();
            if (bulletList.get(plane1) != null) {
                bulletsCopy = new ArrayList<>(bulletList.get(plane1));
            }
            for (Bullet bullet: bulletsCopy) {
                Bullet toRemove = bullet.up(STEP*2, getHeight(), getWidth());
                bulletList.replace(plane1, bulletList.get(plane1).stream().filter(x -> x != toRemove).collect(Collectors.toCollection(LinkedList::new)));
                movableList = movableList.stream().filter(x -> x != toRemove).collect(Collectors.toCollection(ArrayList::new));
            }
            for (Bullet bullet: bulletsCopy) {
                Bullet toRemove;
                if (bullet.collision(movableList)) {
                    toRemove = bullet;
                    bulletList.replace(plane1, bulletList.get(plane1).stream().filter(x -> x != toRemove).collect(Collectors.toCollection(LinkedList::new)));
                    movableList = movableList.stream().filter(x -> x != toRemove).collect(Collectors.toCollection(ArrayList::new));
                }

            }
        }

        private void startGameLoop() {
            Thread gameThread = new Thread(() -> {
                long lastTime = System.nanoTime();
                double ns = 1000000000 / 165;
                double delta = 0;
                while (true) {
                    long now = System.nanoTime();
                    delta += (now - lastTime) / ns;
                    lastTime = now;
                    if (delta >= 1) {
                        update();
                        repaint();
                        delta--;
                    }
                }
            });
            gameThread.start();
        }
    }


}

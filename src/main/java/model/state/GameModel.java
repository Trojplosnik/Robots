package model.state;

import java.awt.Point;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class GameModel extends Observable {
    public static final String KEY_REDRAW = "redraw";
    public static final String KEY_MODEL_UPDATE = "update";

    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private final Robot robot;
    private final Target target;

    public GameModel() {
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setChanged();
                notifyObservers(KEY_REDRAW);
                clearChanged();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setChanged();
                notifyObservers(KEY_MODEL_UPDATE);
                clearChanged();
            }
        }, 0, 10);
        robot = new Robot(100, 100);
        target = new Target(100, 100);
    }

    public void setTargetPosition(Point p) {
        target.setX(p.x);
        target.setY(p.y);
    }

    public double robotX() {
        return robot.getX();
    }
    //Robot.Position.X

    public double robotY() {
        return robot.getY();
    }

    public double robotDirection() {
        return robot.getDirection();
    }

    public int targetX() {
        return (int) target.getX();
    }

    public int targetY() {
        return (int) target.getY();
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    public void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, Robot.maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -Robot.maxAngularVelocity, Robot.maxAngularVelocity);
        double newX = robot.getX() + velocity / angularVelocity *
                (Math.sin(robot.getDirection() + angularVelocity * duration) -
                        Math.sin(robot.getDirection()));
        if (!Double.isFinite(newX)) {
            newX = robot.getX() + velocity * duration * Math.cos(robot.getDirection());
        }
        double newY = robot.getY() - velocity / angularVelocity *
                (Math.cos(robot.getDirection() + angularVelocity * duration) -
                        Math.cos(robot.getDirection()));
        if (!Double.isFinite(newY)) {
            newY = robot.getY() + velocity * duration * Math.sin(robot.getDirection());
        }
        robot.setX(newX);
        robot.setY(newY);
        double newDirection = asNormalizedRadians(robot.getDirection() + angularVelocity * duration);
        robot.setDirection(newDirection);
    }

    public static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }
}


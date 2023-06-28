package model.state;

import java.awt.*;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class GameModel extends Observable {
    public static final String KEY_MODEL_UPDATE = "update";

    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private final Robot robot;
    private final Target target;

    public GameModel() {
        robot = new Robot(100, 100);
        target = new Target(100, 100);

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 10);
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


    protected void onModelUpdateEvent() {
        double distance = Calculator.distance(target.getX(), target.getY(), robot.getX(), robot.getY());
        if (distance < 0.5) {
            setChanged();
            notifyObservers(KEY_MODEL_UPDATE);
            clearChanged();
            return;
        }
        double angleToTarget = Calculator.angleTo(robot.getX(), robot.getY(), target.getX(), target.getY());

        double angularVelocity = Calculator.calculateAngularVelocity(angleToTarget,
                robot.getDirection(), Robot.maxAngularVelocity);

        moveRobot(Robot.maxVelocity, angularVelocity, 10);
        setChanged();
        notifyObservers(KEY_MODEL_UPDATE);
        clearChanged();
    }

    public void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = Calculator.applyLimits(velocity, 0, Robot.maxVelocity);
        angularVelocity = Calculator.applyLimits(angularVelocity, -Robot.maxAngularVelocity, Robot.maxAngularVelocity);


        double newDirection = calculateNewDirection(angularVelocity, duration, robot.getDirection());

        double newX = calculateNewX(velocity, angularVelocity, robot.getX(), duration,
                newDirection, robot.getDirection());
        double newY = calculateNewY(velocity, angularVelocity, robot.getY(), duration,
                newDirection, robot.getDirection());

        robot.setX(newX);
        robot.setY(newY);

        robot.setDirection(newDirection);
    }
    public static double calculateNewX(double velocity, double angularVelocity,
                                       double currentX, double duration,
                                       double newDirection, double currentDirection) {
        double newX = currentX + (velocity / angularVelocity) * (Math.sin(newDirection) - Math.sin(currentDirection));
        if (!Double.isFinite(newX)) {
            newX = currentX + velocity * duration * Math.cos(currentDirection);
        }
        return newX;
    }

    public static double calculateNewY(double velocity, double angularVelocity,
                                       double currentY, double duration,
                                       double newDirection, double currentDirection) {
        double newY = currentY - (velocity / angularVelocity) * (Math.cos(newDirection) - Math.cos(currentDirection));
        if (!Double.isFinite(newY)) {
            newY = currentY + velocity * duration * Math.sin(currentDirection);
        }
        return newY;
    }

    public static double calculateNewDirection(double angularVelocity, double duration, double currentDirection) {
        return Calculator.asNormalizedRadians(angularVelocity * duration + currentDirection);
    }

}


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
        setChanged();
        notifyObservers(KEY_MODEL_UPDATE);
        clearChanged();
        if (distance < 0.5) {
            return;
        }
        double angleToTarget = Calculator.angleTo(robot.getX(), robot.getY(), target.getX(), target.getY());

        double angularVelocity = Calculator.calculateAngularVelocity(angleToTarget,
                robot.getDirection(), Robot.maxAngularVelocity);

        moveRobot(Robot.maxVelocity, angularVelocity, 10);
    }

    public void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = Calculator.applyLimits(velocity, 0, Robot.maxVelocity);
        angularVelocity = Calculator.applyLimits(angularVelocity, -Robot.maxAngularVelocity, Robot.maxAngularVelocity);


        double newDirection = Calculator.calculateNewDirection(angularVelocity, duration, robot.getDirection());

        double newX = Calculator.calculateNewX(velocity, angularVelocity, robot.getX(), duration,
                newDirection, robot.getDirection());
        double newY = Calculator.calculateNewY(velocity, angularVelocity, robot.getY(), duration,
                newDirection, robot.getDirection());

        robot.setX(newX);
        robot.setY(newY);

        robot.setDirection(newDirection);
    }

}


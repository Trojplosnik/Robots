package model.state;

import language.LanguageTranslator;

public class Calculator {

    public static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
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

    public static int round(double value) {
        return (int) (value + 0.5);
    }

    public static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    public static double calculateAngularVelocity(double angleToTarget, double currentDirection, double maxAngularVelocity) {
        double angle = Calculator.asNormalizedRadians(angleToTarget - currentDirection);
        if (angle < Math.PI / 2) {
            return maxAngularVelocity;
        } else if (angle > Math.PI / 2) {
            return -maxAngularVelocity;
        }
        return 0;
    }

    public static double calculateNewDirection(double angularVelocity, double duration, double currentDirection) {
        return asNormalizedRadians(angularVelocity * duration + currentDirection);
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
}

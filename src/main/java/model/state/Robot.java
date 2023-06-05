package model.state;

public class Robot {
    private final Position robotPosition;
    private volatile double robotDirection = 0;
    public static final double maxVelocity = 0.1;
    public static final double maxAngularVelocity = 0.0025;

    public Robot(double x, double y){
        robotPosition = new Position(x, y);
    }

    public double getDirection(){return robotDirection;}
    public double getX(){return robotPosition.getX();}
    public double getY(){return robotPosition.getY();}
    public void setX(double x){robotPosition.setX(x);}
    public void setY(double y){robotPosition.setY(y);}
    public void setDirection(double direction){robotDirection = direction;}
}

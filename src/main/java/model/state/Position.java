package model.state;

public class Position {
    private volatile double m_X;
    private volatile double m_Y;

    public Position(double x, double y){
        m_X = x;
        m_Y = y;
    }

    public double getX(){return m_X;}
    public double getY(){return m_Y;}
    public void setX(double x){m_X = x;}
    public void setY(double y){m_Y = y;}
}

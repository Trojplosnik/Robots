package gui;



import model.state.Calculator;
import model.state.GameModel;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class GameVisualizer extends JPanel {
    private final GameModel robotModel;


    public GameVisualizer(GameModel Model) {
        robotModel = Model;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            drawRobot(g2d, robotModel);
            drawTarget(g2d, robotModel);
        } finally {
            g2d.dispose();
        }
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, GameModel model)
    {
        int robotCenterX = Calculator.round(model.robotX());
        int robotCenterY = Calculator.round(model.robotY());
        AffineTransform t = AffineTransform.getRotateInstance(model.robotDirection(), robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }

    private void drawTarget(Graphics2D g, GameModel model)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, model.targetX(), model.targetY(), 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, model.targetX(), model.targetY(), 5, 5);
    }



}

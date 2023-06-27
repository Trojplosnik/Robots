package gui;



import model.state.Calculator;
import model.state.GameModel;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class GameVisualizer extends JPanel {
    private final GameModel gameModel;


    public GameVisualizer(GameModel Model) {
        gameModel = Model;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawRobot(g2d, gameModel);
        drawTarget(g2d, gameModel);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D graphics, GameModel model)
    {
        int robotCenterX = Calculator.round(model.robotX());
        int robotCenterY = Calculator.round(model.robotY());
        AffineTransform t = AffineTransform.getRotateInstance(model.robotDirection(), robotCenterX, robotCenterY);
        Graphics2D g = (Graphics2D)graphics.create();
        try {
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
        finally {
            g.dispose();
        }
    }

    private void drawTarget(Graphics2D graphics, GameModel model)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        Graphics2D g = (Graphics2D)graphics.create();
        try {
            g.setTransform(t);
            g.setColor(Color.GREEN);
            fillOval(g, model.targetX(), model.targetY(), 5, 5);
            g.setColor(Color.BLACK);
            drawOval(g, model.targetX(), model.targetY(), 5, 5);
        }
        finally {
            g.dispose();
        }
    }



}

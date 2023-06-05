package gui;

import model.state.GameModel;
import model.state.Robot;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;


public class GameWindow extends JInternalFrame implements Observer {
    private final GameModel robotModel;

    public GameWindow(GameModel model, String title) {
        super(title, true, true, true, true);

        robotModel = model;
        robotModel.addObserver(this);

        GameVisualizer gameVisualizer = new GameVisualizer(robotModel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                robotModel.setTargetPosition(e.getPoint());
            }
        });

        getContentPane().add(panel);
        pack();
    }


    private boolean areEqual(Object o1, Object o2) {
        if (o1 == null)
            return o2 == null;
        return o1.equals(o2);
    }

    @Override
    public void update(Observable o, Object key) {
        if (areEqual(robotModel, o)) {
            if (areEqual(GameModel.KEY_REDRAW, key))
                onRedrawEvent();
            else if (areEqual(GameModel.KEY_MODEL_UPDATE, key))
                onModelUpdateEvent();
        }
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return GameModel.asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    protected void onModelUpdateEvent() {
        double distance = distance(robotModel.targetX(), robotModel.targetY(),
                robotModel.robotX(), robotModel.robotY());
        if (distance < 0.5) {
            return;
        }
        double velocity = Robot.maxVelocity;
        double angleToTarget = angleTo(robotModel.robotX(), robotModel.robotY(),
                robotModel.targetX(), robotModel.targetY());
        double angularVelocity = 0;

        double angle = GameModel.asNormalizedRadians(angleToTarget - robotModel.robotDirection());
        if (angle < Math.PI / 2) {
            angularVelocity = Robot.maxAngularVelocity;
        } else if (angle > Math.PI / 2) {
            angularVelocity = -Robot.maxAngularVelocity;
        }

        robotModel.moveRobot(velocity, angularVelocity, 10);
    }
}

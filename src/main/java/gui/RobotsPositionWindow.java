package gui;

import model.state.GameModel;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.TextArea;
import java.util.Observable;
import java.util.Observer;

public class RobotsPositionWindow extends JInternalFrame implements Observer {
    private final TextArea coordinates;
    private int m_X = 0;
    private int m_Y = 0;
    private final GameModel gameModel;

    public RobotsPositionWindow(GameModel model, String title) {
        super(title, true, true, true, true);

        this.coordinates = new TextArea("");
        coordinates.setSize(200, 500);

        this.gameModel = model;
        gameModel.addObserver(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(coordinates, BorderLayout.CENTER);
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
        if (areEqual(gameModel, o)) {
            if (areEqual(GameModel.KEY_MODEL_UPDATE, key))
                onModelUpdateEvent();
        }
    }

    private void onModelUpdateEvent() {
        if (m_X != round(gameModel.robotX()) && m_Y != round(gameModel.robotY())) {
            m_X = round(gameModel.robotX());
            m_Y = round(gameModel.robotY());
            coordinates.append("X coordinate: " + m_X + ", Y coordinate: " + m_Y + "\n");
        }
    }

    public static int round(double value) {
        return (int) (value + 0.5);
    }
}

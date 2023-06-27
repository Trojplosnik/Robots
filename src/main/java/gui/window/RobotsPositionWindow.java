package gui.window;

import model.state.Calculator;
import model.state.GameModel;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.TextArea;

import java.util.Observable;
import java.util.Observer;

public class RobotsPositionWindow extends JInternalFrame implements Observer {
    private final JLabel labelX;
    private final JLabel labelY;
    private final GameModel gameModel;

    public RobotsPositionWindow(GameModel model, String title) {
        super(title, false, true, true, true);

        labelX = new JLabel("X coordinate: " + Calculator.round(model.robotX()));
        labelY = new JLabel("Y coordinate: " + Calculator.round(model.robotY()));

        gameModel = model;
        gameModel.addObserver(this);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(labelX, BorderLayout.NORTH);
        panel.add(labelY, BorderLayout.CENTER);

        setSize(130, 70);

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
                updateCoordinates(gameModel);
        }
    }

    private void updateCoordinates(GameModel model) {
        labelX.setText("X coordinate: " + Calculator.round(model.robotX()));
        labelY.setText("Y coordinate: " + Calculator.round(model.robotY()));
    }


}

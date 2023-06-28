package gui.window;

import language.LanguageTranslator;
import model.state.Calculator;
import model.state.GameModel;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import java.util.Observable;
import java.util.Observer;

import static language.LanguageTranslator.TRANSLATOR;

public class RobotsPositionWindow extends JInternalFrame implements Observer {
    private final JLabel labelX;
    private final JLabel labelY;
    private final GameModel gameModel;

    public RobotsPositionWindow(GameModel model) {
        super(TRANSLATOR.translate("coordinates"), false, true, true, true);

        labelX = new JLabel(TRANSLATOR.translate("X_coordinate") + Calculator.round(model.robotX()));
        labelY = new JLabel(TRANSLATOR.translate("Y_coordinate") + Calculator.round(model.robotY()));

        gameModel = model;
        gameModel.addObserver(this);
        TRANSLATOR.addObserver(this);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(labelX, BorderLayout.NORTH);
        panel.add(labelY, BorderLayout.CENTER);

        setSize(1300, 70);

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
        if (areEqual(TRANSLATOR, o)) {
            if (areEqual(LanguageTranslator.KEY_CHANGE_LANGUAGE, key))
                setTitle(TRANSLATOR.translate("coordinates"));
        }
    }

    private void updateCoordinates(GameModel model) {
        labelX.setText(TRANSLATOR.translate("X_coordinate") + Calculator.round(model.robotX()));
        labelY.setText(TRANSLATOR.translate("Y_coordinate") + Calculator.round(model.robotY()));
    }


}

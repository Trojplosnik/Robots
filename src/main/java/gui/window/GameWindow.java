package gui.window;

import gui.GameVisualizer;
import language.LanguageTranslator;
import model.state.GameModel;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import static language.LanguageTranslator.TRANSLATOR;


public class GameWindow extends JInternalFrame implements Observer {

    public GameWindow(GameModel model) {
        super(TRANSLATOR.translate("game_field"), true, true, true, true);

        TRANSLATOR.addObserver(this);

        GameVisualizer gameVisualizer = new GameVisualizer(model);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
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
        if (areEqual(TRANSLATOR, o)) {
            if (areEqual(LanguageTranslator.KEY_CHANGE_LANGUAGE, key))
                setTitle(TRANSLATOR.translate("game_field"));
        }
    }

}

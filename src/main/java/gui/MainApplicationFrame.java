package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import configuration.FrameState;
import gui.window.GameWindow;
import gui.window.LogWindow;
import gui.window.RobotsPositionWindow;
import model.log.Logger;
import model.state.GameModel;
import org.json.JSONObject;

import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JInternalFrame;

import com.fasterxml.jackson.databind.ObjectMapper;

import static language.LanguageTranslator.TRANSLATOR;
import static configuration.FrameSerializer.SERIALIZER;

public class MainApplicationFrame extends JFrame {
    private static final JDesktopPane desktopPane = new JDesktopPane();


    private static final GameModel robotModel = new GameModel();

    private LogWindow logWindow;
    private GameWindow gameWindow;
    private RobotsPositionWindow positionWindow;


    public void setLogWindow(LogWindow logWindow) {
        this.logWindow = logWindow;
        addWindow(this.logWindow);
    }

    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        addWindow(this.gameWindow);
    }

    public void setPositionWindow(RobotsPositionWindow positionWindow) {
        this.positionWindow = positionWindow;
        addWindow(this.positionWindow);
    }

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);
        logWindow = createLogWindow();
        gameWindow = createGameWindow();
        positionWindow = createRobotsPositionWindow();

        loadWindows();

        setJMenuBar(new MenuBar(this));

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExitEvent();
            }
        });
    }

    protected void confirmExitEvent() {
        String[] options = {TRANSLATOR.translate("yes"), TRANSLATOR.translate("no")};
        int exit = JOptionPane.showOptionDialog(null,
                TRANSLATOR.translate("exit_confirm"),
                TRANSLATOR.translate("exit"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);
        if (exit == JOptionPane.YES_OPTION){
            saveWindows();
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    protected GameWindow createGameWindow() {
        GameWindow window = new GameWindow(robotModel);
        window.setSize(600, 600);
        int x = (this.getWidth() - window.getWidth()) / 2;
        int y = (this.getHeight() - window.getHeight()) / 2;
        window.setLocation(x, y);
        return window;
    }

    protected LogWindow createLogWindow() {
        LogWindow window = new LogWindow(Logger.getDefaultLogSource());
        window.setLocation(10, 10);
        window.setSize(300, 800);
        Logger.debug("The protocol works");
        setMinimumSize(window.getSize());
        return window;
    }

    protected RobotsPositionWindow createRobotsPositionWindow() {
        RobotsPositionWindow window = new RobotsPositionWindow(robotModel);
        window.setSize(250, 70);
        window.setLocation(350, 10);
        return window;
    }

    protected void addWindow(JInternalFrame frame) {
        for (JInternalFrame currentFrame : desktopPane.getAllFrames()) {
            if (currentFrame.getTitle().equals(frame.getTitle()))
                return;
        }
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void saveWindows(){
        JSONObject json = new JSONObject();
        json.put("Language", TRANSLATOR.getCurrentLanguage());
        json.put("baseName", TRANSLATOR.getCurrentBaseName());
        json.put("gameWindow",new JSONObject(new FrameState(gameWindow)));
        json.put("logWindow", new JSONObject(new FrameState(logWindow)));
        json.put("positionWindow",new JSONObject(new FrameState(positionWindow)));
        SERIALIZER.save(json);
    }

    protected void loadWindows() {
        JSONObject json = SERIALIZER.load();
        ObjectMapper objectMapper = new ObjectMapper();
        if(json != null) {
            if (json.has("baseName") && json.has("Language")) {
                TRANSLATOR.changeLanguage(json.getString("baseName"), json.getString("Language"));
            }
            if (json.has("gameWindow")) {
                try {
                    FrameState frameState =
                            objectMapper.readValue(json.get("gameWindow").toString(), FrameState.class);
                    frameState.restoreFrame(gameWindow);
                }
                catch (Exception e){
                    // e.printStackTrace();
                    // ignore
                }
            }
            if (json.has("logWindow")) {
                try {
                    FrameState frameState =
                            objectMapper.readValue(json.get("logWindow").toString(), FrameState.class);
                    frameState.restoreFrame(logWindow);
                }
                catch (Exception e){
                    // e.printStackTrace();
                    // ignore
                }
            }
            if (json.has("positionWindow")) {
                try {
                    FrameState frameState =
                            objectMapper.readValue(json.get("positionWindow").toString(), FrameState.class);
                    frameState.restoreFrame(positionWindow);
                }
                catch (Exception e){
                    // e.printStackTrace();
                    // ignore
                }
            }
        }
        if (!gameWindow.isClosed())
            addWindow(gameWindow);
        if (!logWindow.isClosed())
            addWindow(logWindow);
        if (!positionWindow.isClosed())
            addWindow(positionWindow);
    }
}

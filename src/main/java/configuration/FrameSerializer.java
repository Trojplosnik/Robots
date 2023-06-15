package configuration;


import gui.window.GameWindow;
import gui.window.LogWindow;
import gui.window.RobotsPositionWindow;
import model.log.Logger;
import model.state.GameModel;

import javax.swing.JInternalFrame;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import static language.LanguageTranslator.TRANSLATOR;

public class FrameSerializer {
    private final File configFile = new File(System.getProperty("user.home")
            + File.separator + "RobotsConfig" + File.separator +  "config.dat");

    public static final FrameSerializer SAVELOAD = new FrameSerializer();

    public FrameSerializer() {
        createConfigFile();
    }

    private void createConfigFile(){
        try {
            if(!configFile.exists())
                if(!configFile.getParentFile().exists())
                {
                    if (configFile.getParentFile().mkdir())
                        configFile.createNewFile();

                }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }



    public void save(JInternalFrame[] frames) {

        createConfigFile();


        FrameState[] frameStates = new FrameState[frames.length];


        for (int i = 0; i < frames.length; i++) {
            frameStates[i] = new FrameState(frames[i]);
        }


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configFile))) {
            oos.writeObject(frameStates);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JInternalFrame[] load(GameModel robotModel) {
        JInternalFrame[] frames;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFile))) {

            FrameState[] frameStates = (FrameState[]) ois.readObject();
            frames = new JInternalFrame[frameStates.length];
            for (int i = 0; i < frameStates.length; i++) {
                if (frameStates[i].getTitle().equals(TRANSLATOR.translate("game_field"))) {
                    frames[i] = new GameWindow(robotModel, frameStates[i].getTitle());
                } else if (frameStates[i].getTitle().equals(TRANSLATOR.translate("work_protocol"))) {
                    frames[i] = new LogWindow(Logger.getDefaultLogSource(), frameStates[i].getTitle());
                    Logger.debug("The protocol works");
                } else if (frameStates[i].getTitle().equals(TRANSLATOR.translate("coordinates"))) {
                    frames[i] = new RobotsPositionWindow(robotModel, frameStates[i].getTitle());
                } else {
                    throw new IllegalStateException("Unexpected value: " + frameStates[i].getTitle());
                }
                frameStates[i].restoreFrame(frames[i]);
            }

        } catch (Exception ex) {
//            ex.printStackTrace();
            return null;
        }


        return frames;
    }
}

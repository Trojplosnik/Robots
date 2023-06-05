package configuration;


import gui.GameWindow;
import gui.LogWindow;
import gui.RobotsPositionWindow;
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

public class SaveLoad {
    private final File configFile = new File(System.getProperty("user.home")
            + File.separator + "RobotsConfig" + File.separator +  "config.dat");

    public static final SaveLoad SAVELOAD = new SaveLoad();

    public SaveLoad() {
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



        FrameDataStorage[] frameDataStorages = new FrameDataStorage[frames.length];


        for (int i = 0; i < frames.length; i++) {
            frameDataStorages[i] = new FrameDataStorage(frames[i].getTitle(),
                    frames[i].isClosed(), frames[i].isIcon(),
                    frames[i].getBounds());
        }


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configFile))) {
            oos.writeObject(frameDataStorages);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JInternalFrame[] load(GameModel robotModel) {
        JInternalFrame[] frames;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFile))) {

            FrameDataStorage[] frameDataStorages = (FrameDataStorage[]) ois.readObject();
            frames = new JInternalFrame[frameDataStorages.length];
            for (int i = 0; i < frameDataStorages.length; i++) {
                if (frameDataStorages[i].getTitle().equals(TRANSLATOR.translate("game_field"))) {
                    frames[i] = new GameWindow(robotModel, frameDataStorages[i].getTitle());
                } else if (frameDataStorages[i].getTitle().equals(TRANSLATOR.translate("work_protocol"))) {
                    frames[i] = new LogWindow(Logger.getDefaultLogSource(), frameDataStorages[i].getTitle());
                    Logger.debug("The protocol works");
                } else if (frameDataStorages[i].getTitle().equals(TRANSLATOR.translate("coordinates"))) {
                    frames[i] = new RobotsPositionWindow(robotModel, frameDataStorages[i].getTitle());
                } else {
                    throw new IllegalStateException("Unexpected value: " + frameDataStorages[i].getTitle());
                }
                frames[i].setTitle(frameDataStorages[i].getTitle());
                frames[i].setBounds(frameDataStorages[i].getBounds());
                frames[i].setClosed(frameDataStorages[i].isClosed());
                frames[i].setIcon(frameDataStorages[i].isIcon());
            }

        } catch (Exception ex) {
            return null;
        }


        return frames;
    }
}

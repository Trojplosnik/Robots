package configuration;


import gui.GameWindow;
import gui.LogWindow;
import log.Logger;

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
                    frames[i].isClosed(), frames[i].isIcon(), frames[i].getBounds());
        }


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configFile))) {
            oos.writeObject(frameDataStorages);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JInternalFrame[] load() {
        JInternalFrame[] frames;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFile))) {

            FrameDataStorage[] frameDataStorages = (FrameDataStorage[]) ois.readObject();
            frames = new JInternalFrame[frameDataStorages.length];
            for (int i = 0; i < frameDataStorages.length; i++) {
                if (frameDataStorages[i].getTitle().equals(TRANSLATOR.translate("game_field"))) {
                    frames[i] = new GameWindow();
                } else if (frameDataStorages[i].getTitle().equals(TRANSLATOR.translate("work_protocol"))) {
                    frames[i] = new LogWindow(Logger.getDefaultLogSource());
                    Logger.debug("The protocol works");
                } else {
                    throw new IllegalStateException("Unexpected value: " + frameDataStorages[i].getTitle());
                }
                frames[i].setTitle(frameDataStorages[i].getTitle());
                frames[i].setBounds(frameDataStorages[i].getBounds());
                frames[i].setClosed(frameDataStorages[i].isIcon());
                frames[i].setIcon(frameDataStorages[i].isClosed());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return frames;
    }
}

package configuration;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class FrameSerializer {
    private final File configFile = new File(System.getProperty("user.home")
            + File.separator + "RobotsConfig" + File.separator + "config.json");

    public static final FrameSerializer SERIALIZER = new FrameSerializer();

    private FrameSerializer() {
    }

    private void createConfigFile() {
        try {
            if (!configFile.exists())
                if (!configFile.getParentFile().exists()) {
                    if (configFile.getParentFile().mkdir())
                        configFile.createNewFile();

                }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void save(JSONObject json) {
        createConfigFile();

        try (FileWriter fileWriter = new FileWriter(configFile.getAbsolutePath(), StandardCharsets.UTF_8)) {
            fileWriter.write(json.toString());
        } catch (IOException e) {
            // e.printStackTrace();
            // ignore
        }
    }

    public JSONObject load() {
        if (configFile.exists()) {
            try (FileReader fileReader = new FileReader(configFile, StandardCharsets.UTF_8)) {
                try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    return new JSONObject(stringBuilder.toString());
                } catch (Exception ex) {
                    // ex.printStackTrace();
                    // ignore
                }
            } catch (IOException e) {
                // e.printStackTrace();
                // ignore
            }
        }
        return null;
    }
}

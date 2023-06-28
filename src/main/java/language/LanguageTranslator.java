package language;


import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

public class LanguageTranslator extends Observable {

    private final static String DEFAULT_LANGUAGE = "ru";

    private static String currentLanguage = DEFAULT_LANGUAGE;


    private static String currentBaseName;

    public static final String KEY_CHANGE_LANGUAGE = "language changed";
    private static ResourceBundle rb;
    private LanguageTranslator()
    {
        currentBaseName = "locale";
        rb = ResourceBundle.getBundle(currentBaseName, Locale.of(DEFAULT_LANGUAGE));
    }

    public static final LanguageTranslator TRANSLATOR = new LanguageTranslator();



    public void changeLanguage(String baseName, String language) {
        try {
            rb = ResourceBundle.getBundle(baseName, Locale.of(language));
            currentLanguage = language;
            currentBaseName = baseName;
        }
        catch (Exception e)
        {
            rb = ResourceBundle.getBundle(currentBaseName, Locale.of(DEFAULT_LANGUAGE));
        }
        setChanged();
        notifyObservers(KEY_CHANGE_LANGUAGE);
        clearChanged();
    }

    public String translate(String msg) {
        return rb.getString(msg);
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }
    public String getCurrentBaseName() {
        return currentBaseName;
    }
}

package co.id.dicoding.moviecatalogueapi.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageUtil {

    public static void updateBaseContextLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        updateResourcesLocaleLegacy(context, locale);
    }

    private static void updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public String getPrefLanguage(Context context) {
        Locale current = context.getResources().getConfiguration().locale;
        return current.toString();
    }
}

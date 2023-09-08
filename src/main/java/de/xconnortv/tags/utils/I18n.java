package de.xconnortv.tags.utils;

import de.xconnortv.tags.Tags;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Objects;

public class I18n {
    private final String LANGUAGE_PATH = "language";
    private final String TRANSLATION_PATH = "translations";
    @Getter
    private String currentLanguage;
    @Getter
    private final CustomConfig i18nConfig;
    @Getter
    private final HashMap<String, ConfigurationSection> translations;
    private final String defaultLanguage;

    public I18n(Tags instance, String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
        this.i18nConfig = new CustomConfig("i18n.yml", instance);
        this.translations = new HashMap<>();
    }

    public void reload() {
        this.i18nConfig.reloadConfig();
    }

    public void updateLanguage(String language, boolean config) {
        this.currentLanguage = language;
        if(config) storeLanguage(language);
    }


    private void loadTranslations() {
        this.translations.clear();
        if(!this.i18nConfig.get().contains(TRANSLATION_PATH)) {
            return;
        }
        for(String langKey : Objects.requireNonNull(
                this.i18nConfig.get().getConfigurationSection(TRANSLATION_PATH)
        ).getKeys(false)) {
            translations.put(langKey, this.i18nConfig.get().getConfigurationSection(TRANSLATION_PATH + "." + langKey));
        }

        if(this.i18nConfig.get().contains(LANGUAGE_PATH)) {
            updateLanguage(this.i18nConfig.get().getString(LANGUAGE_PATH), false);
        }else {
            updateLanguage(defaultLanguage, true);
        }
    }

    public void loadConfig() {
        this.i18nConfig.copyDefault();
        loadTranslations();
    }

    public String getTranslation(String path) {
        String translation = path;
        ConfigurationSection section = this.translations.get(this.currentLanguage);
        if(section != null && section.contains(path)) translation = section.getString(path);
        if(translation != null) return ChatColor.translateAlternateColorCodes('&', translation);
        return translation;
    }

    private void storeLanguage(String lang) {
        i18nConfig.get().set(LANGUAGE_PATH, lang);
        i18nConfig.saveConfig();
    }

    public ConfigurationSection getLangTranslations(String lang) {
        if(lang == null) lang = this.currentLanguage;
        return this.translations.get(lang);
    }
    public ConfigurationSection getLangTranslations() {
        return this.getLangTranslations(null);
    }


}

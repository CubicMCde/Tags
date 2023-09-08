package de.xconnortv.tags.utils;

import de.xconnortv.tags.Tags;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public class Translatable {
    private final Tags instance;
    @Getter
    private String namespace;

    public Translatable(Tags instance, String namespace) {
        this.instance = instance;
        this.namespace = namespace;
    }


    public String resolve(String path, @Nullable String namespace) {
        if(namespace != null) path = namespace + "." + path;
        ConfigurationSection section = instance.getI18n().getLangTranslations();
        char separator = instance.getConfig().options().pathSeparator();
        int i1 = -1;
        int i2;
        String key;
        while((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            key = path.substring(i2, i1);
            if(!section.contains(key, true)) return path;
            section = section.getConfigurationSection(key);
            if(section == null) return path;
        }
        key = path.substring(i2);
        if(!section.isString(key)) return path;
        String res = section.getString(key);
        if(res == null) return path;
        return ChatColor.translateAlternateColorCodes('&', res);
    }
    public String resolve(String path) {
        return this.resolve(path, this.namespace);
    }
}

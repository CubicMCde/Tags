package de.xconnortv.tags;

import de.xconnortv.tags.commands.TagsCommand;
import de.xconnortv.tags.manager.DatabaseManager;
import de.xconnortv.tags.manager.TagManager;
import de.xconnortv.tags.placeholder.TagPlaceholder;
import de.xconnortv.tags.utils.AbstractCommand;
import de.xconnortv.tags.utils.CustomConfig;
import de.xconnortv.tags.utils.I18n;
import lombok.Getter;
import mc.obliviate.inventory.InventoryAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@Getter
public final class Tags extends JavaPlugin {
    private I18n i18n;
    private CustomConfig defaultConfig;
    private DatabaseManager databaseManager;
    private TagManager tagManager;
    private static Tags instance;

    private String PREFIX = "§8[§6Tags§8]";

    @Override
    public void onEnable() {
        instance = this;
        new InventoryAPI(this).init();

        new TagPlaceholder().register();

        defaultConfig = new CustomConfig("config.yml", this);
        defaultConfig.copyDefault();

        if(defaultConfig.get().contains("prefix")) {
            PREFIX = ChatColor.translateAlternateColorCodes('&',
                    Objects.requireNonNull(defaultConfig.get().getString("prefix"))
            );
        }


        i18n = new I18n(this, "de_DE");
        i18n.loadConfig();

        databaseManager = new DatabaseManager(this);
        databaseManager.connect();

        tagManager = new TagManager(this);

        this.registerCommand(new TagsCommand(this), null);
    }

    public void reload() {
        defaultConfig.reloadConfig();
        i18n.reload();
        databaseManager.disconnect();
        databaseManager.connect();
    }


    @Override
    public void onDisable() {
        databaseManager.disconnect();
    }

    public String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', PREFIX + " " + message);
    }

    public void registerCommand(@NotNull AbstractCommand abstractCommand, @Nullable List<String> aliases) {
        this.registerCommand(abstractCommand.getName(), abstractCommand, aliases);
    }

    public void registerCommand(@NotNull String name, @NotNull TabExecutor executor, @Nullable List<String> aliases) {
        PluginCommand command = getCommand(name);
        if(command == null) return;
        command.setExecutor(executor);
        command.setTabCompleter(executor);
        if(aliases == null) return;
        command.setAliases(aliases);
    }

    public static Tags getInstance(){
        return Tags.instance;
    }

}

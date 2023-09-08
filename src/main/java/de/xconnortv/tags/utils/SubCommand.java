package de.xconnortv.tags.utils;

import de.xconnortv.tags.Tags;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
@Getter
public abstract class SubCommand extends Translatable {
    private final String name;
    @Getter
    private final AbstractCommand cmd;
    private final boolean playerOnly;

    public SubCommand(@NotNull String name, @NotNull AbstractCommand cmd, boolean playerOnly) {
        super(cmd.getInstance(), "messages");
        this.name = name.toLowerCase();
        this.cmd = cmd;
        this.playerOnly = playerOnly;
    }
    public Tags getInstance(){
        return cmd.getInstance();
    }
    public String getCommandName(){
        return getName();
    }

    public abstract boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args);
    public abstract List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings);

    public String getPermission(){
        return getCmd().getInstance().getName().toLowerCase() + "." + getCommandName() + "." + this.getName();
    }

}

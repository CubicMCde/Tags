package de.xconnortv.tags.commands;

import de.xconnortv.tags.utils.AbstractCommand;
import de.xconnortv.tags.utils.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Reload extends SubCommand {
    public Reload(@NotNull AbstractCommand cmd) {
        super("reload", cmd, false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        getInstance().reload();
        commandSender.sendMessage(getInstance().formatMessage("&aSuccessfully reloaded!"));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }

}

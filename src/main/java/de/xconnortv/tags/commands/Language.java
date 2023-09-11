package de.xconnortv.tags.commands;

import de.xconnortv.tags.utils.AbstractCommand;
import de.xconnortv.tags.utils.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Language extends SubCommand {
    public Language(@NotNull AbstractCommand cmd) {
        super("language", cmd, false);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            commandSender.sendMessage(getInstance().formatMessage(resolve("language.current").replace("%s", getInstance().getI18n().getCurrentLanguage())));
            return true;
        }
        if(!getInstance().getI18n().getTranslations().containsKey(args[0])) {
            commandSender.sendMessage(getInstance().formatMessage(resolve("language.notfound").replace("%s", args[0])));
            return true;
        }

        getInstance().getI18n().updateLanguage(args[0], true);
        commandSender.sendMessage(getInstance().formatMessage(resolve("language.changed").replace("%s", args[0])));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length > 1) return Collections.emptyList();
        List<String> list = new ArrayList<>();
        completeAction(list, args[0]);
        return list;
    }

    public void completeAction(List<String> list, String prefix){
        String text = prefix.toLowerCase();

        getInstance().getI18n().getTranslations().keySet().forEach(command -> {
            if(command.toLowerCase().startsWith(text)) {
                list.add(command);
            }
        });
    }
}

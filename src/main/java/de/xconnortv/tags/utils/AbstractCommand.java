package de.xconnortv.tags.utils;

import de.xconnortv.tags.Tags;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class AbstractCommand extends Translatable implements TabExecutor {


    private final Tags instance;
    private final String name;
    private final List<SubCommand> subCommands;
    public AbstractCommand(@NotNull String name, @NotNull Tags instance) {
        super(instance, "messages");
        this.name = name.toLowerCase();
        this.instance = instance;
        this.subCommands = new ArrayList<>();
    }

    public void register(@NotNull SubCommand command){
        this.subCommands.add(command);
    }

    private boolean validate(SubCommand subCommand, @NotNull CommandSender sender, @NotNull Command command, @NotNull String[] args){
        if(subCommand == null) return false;
        return subCommand.isPlayerOnly() || sender instanceof Player;
    }

    public SubCommand getSubCommand(String cmd) {
        for(SubCommand subCommand : this.subCommands){
            if(!subCommand.getCommandName().equalsIgnoreCase(cmd)) continue;
            return subCommand;
        }
        return null;
    }

    public void completeSubCommand(List<String> list, String prefix, CommandSender sender){
        String text = prefix.toLowerCase();
        this.subCommands.forEach(command -> {
            if(command.getCommandName().startsWith(text) && sender.hasPermission(command.getPermission())) {
                list.add(command.getCommandName());
            }
        });
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!commandSender.hasPermission(this.instance.getName().toLowerCase() + " " + this.name)) {
            commandSender.sendMessage(instance.formatMessage(resolve("noperms")));
            return true;
        }
        if(args.length == 0) return onRootCommand(commandSender, command);

        SubCommand subCommand = this.getSubCommand(args[0]);
        if(!validate(subCommand, commandSender, command, args)) return true;
        if(!commandSender.hasPermission(subCommand.getPermission())) {
            commandSender.sendMessage(instance.formatMessage(resolve("noperms")));
            return true;
        };


        return subCommand.onCommand(commandSender, command, label, Arrays.copyOfRange(args, 1, args.length));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SubCommand subCommand;
        List<String> list = new ArrayList<>();
        if(args.length == 1) {
            completeSubCommand(list, args[0], commandSender);
            return list;
        }
        if(args.length > 1 && (subCommand = getSubCommand(args[0])) != null && commandSender.hasPermission(subCommand.getPermission())) {
            list = subCommand.onTabComplete(commandSender, command, label, Arrays.copyOfRange(args, 1, args.length));
        }
        return list;
    }

    public boolean onRootCommand(@NotNull CommandSender commandSender, @NotNull Command command) {
        return true;
    }

}

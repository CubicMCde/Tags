package de.xconnortv.tags.commands;

import de.xconnortv.tags.Tags;
import de.xconnortv.tags.gui.TagsGUI;
import de.xconnortv.tags.utils.AbstractCommand;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TagsCommand extends AbstractCommand {
    public TagsCommand(@NotNull Tags instance) {
        super("tags", instance);
    }

    @Override
    public boolean onRootCommand(@NotNull CommandSender commandSender, @NotNull Command command) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cYou must be a player to execute this command!");
            return true;
        }

        Player player = (Player) commandSender;
        new TagsGUI(player).open();
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 2);
        return true;
    }
}

package de.xconnortv.tags.placeholder;

import de.xconnortv.tags.Tags;
import de.xconnortv.tags.manager.tags.Tag;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TagPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "tags";
    }

    @Override
    public @NotNull String getAuthor() {
        return "xConnorTV";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if(params.equals("tag")){
            Optional<Tag> tag = Tags.getInstance().getTagManager().getPlayerTag(player);
            return tag.map(value -> value.getSymbol() + " ").orElse("");
        }
        return null;
    }
}

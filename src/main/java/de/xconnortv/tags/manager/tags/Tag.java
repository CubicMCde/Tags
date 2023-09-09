package de.xconnortv.tags.manager.tags;

import de.xconnortv.tags.utils.ItemStackBuilder;
import de.xconnortv.tags.Tags;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

@Getter
public class Tag {
    private final UUID uuid;
    private final String name;
    private final String symbol;
    public Tag(UUID uuid, String name, String symbol) {
        this.uuid = uuid;
        this.name = name;
        this.symbol = ChatColor.translateAlternateColorCodes('&', symbol);
    }

    public ItemStack getItemStack(Material material, Tags instance, String name, String lore) {
        ItemStack stack = new ItemStackBuilder(material)
                .setDisplayName(name.replace("%s", this.name))
                .setLore(new String[] {
                        lore.replace("%s", this.symbol)
                })
                .build();
        ItemMeta meta = stack.getItemMeta();
        if(meta == null) return null;

        meta.getPersistentDataContainer().set(
                new NamespacedKey(instance, "tag_uuid"),
                PersistentDataType.STRING,
                this.uuid.toString());
        stack.setItemMeta(meta);

        return stack;
    }
}
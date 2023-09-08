package de.xconnortv.tags.gui;

import de.xconnortv.tags.SkullsRegistry;
import de.xconnortv.tags.Tags;
import de.xconnortv.tags.manager.tags.Tag;
import de.xconnortv.tags.utils.SkullCreator;
import de.xconnortv.tags.utils.Translatable;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class TagsGUI extends Gui {

    private final PaginationManager pagination = new PaginationManager(this);
    private final Translatable translatable;
    public TagsGUI(Player player) {
        super(player, "gui.tags", "...", 6);
        this.pagination.registerPageSlotsBetween(9, (9 * 5) - 1);
        this.translatable = new Translatable(Tags.getInstance(), getId());
        sendTitleUpdate(translatable.resolve(getId() + ".title", null));
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillRow(new Icon(Material.BLACK_STAINED_GLASS_PANE).setName("§7"), 0);
        fillRow(new Icon(Material.BLACK_STAINED_GLASS_PANE).setName("§7"), 5);
        calculateAndUpdatePagination();


            addItem(getSize() - 6, new Icon(
                    SkullCreator.itemFromBase64(SkullsRegistry.LEFT_SKULL)
            ).setName(translatable.resolve("previous", "pagination")).onClick(e -> {
                if (pagination.getCurrentPage() != 0) {
                    pagination.goPreviousPage();
                    calculateAndUpdatePagination();
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
                    return;
                }
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 2);
            }));


        Icon icon = new Icon(
                SkullCreator.itemFromBase64(SkullsRegistry.INFO_SKULL)
        ).setName(translatable.resolve("info"));

        icon.setLore(PlaceholderAPI.setPlaceholders(player, "§7§oAktueller Tag: §6§l%tags_tag%"), translatable.resolve("remove"));

        icon.onClick(e -> {
            Tags.getInstance().getTagManager().removePlayerTag(player);
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
            open();
        });

        addItem(getSize()-5, icon);


            addItem(getSize()-4, new Icon(
                    SkullCreator.itemFromBase64(SkullsRegistry.RIGHT_SKULL)
            ).setName(translatable.resolve("next", "pagination")).onClick(e -> {
                if (!pagination.isLastPage()) {
                    pagination.goNextPage();
                    calculateAndUpdatePagination();
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
                    return;
                }
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 2);
            }));

    }

    private void createPerkItem(Tag tag) {
        pagination.addItem(new Icon(
                tag.getItemStack(Material.NAME_TAG, Tags.getInstance())
        ).onClick(event -> {
            Tags.getInstance().getTagManager().setPlayerTag(player, tag);
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
            open();
        }));
    }

    private void calculateProducts() {
        pagination.getItems().clear();
        Tags.getInstance().getTagManager().getTags().ifPresent(tags -> {
            for(Tag t : tags) {
                createPerkItem(t);
            }
        });

    }

    private void calculateAndUpdatePagination() {
        calculateProducts();
        this.pagination.update();
    }

}
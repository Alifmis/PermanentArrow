package org.alifmis.permanentArrow;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class PermanentArrow extends JavaPlugin implements Listener {

    private static final String PERMANENT_NAME = "permanent";

    private NamespacedKey permanentKey;

    @Override
    public void onEnable() {
        this.permanentKey = new NamespacedKey(this, "permanent");
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("PermanentArrow enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("PermanentArrow disabled");
    }

    @EventHandler
    public void onArrowLaunch(ProjectileLaunchEvent event) {
        // Only care about arrow-type projectiles shot by players
        if (!(event.getEntity() instanceof AbstractArrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player)) return;

        // Check if the arrow's item stack has the display name "permanent"
        ItemStack arrowItem = arrow.getItemStack();
        if (arrowItem == null || arrowItem.getType() == Material.AIR) return;

        ItemMeta meta = arrowItem.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        if (!meta.getDisplayName().equalsIgnoreCase(PERMANENT_NAME)) return;

        // Tag the arrow entity as permanent via PDC
        arrow.getPersistentDataContainer().set(permanentKey, PersistentDataType.BOOLEAN, true);
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow arrow)) return;

        // Only process permanent arrows
        if (!arrow.getPersistentDataContainer().has(permanentKey, PersistentDataType.BOOLEAN)) return;

        // Prevent despawn: save across chunk loads and set lifetime to infinite
        arrow.setPersistent(true);
        arrow.setLifetimeTicks(Integer.MAX_VALUE);

        // Allow any player to pick it up
        arrow.setPickupRule(AbstractArrow.PickupRule.ALLOWED);

        ItemStack item = arrow.getItemStack();
        if (item != null && item.getType() != Material.AIR) {
            item.editMeta(meta -> meta.displayName(Component.text(PERMANENT_NAME)));
            arrow.setItemStack(item);
        }
    }
}

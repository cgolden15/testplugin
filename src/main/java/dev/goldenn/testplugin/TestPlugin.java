package dev.goldenn.testplugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.UUID;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public final class TestPlugin extends JavaPlugin implements @NotNull Listener {

    @Override
    public void onEnable() {
        getLogger().info("TestPlugin has been enabled!");
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("hello").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("TestPlugin has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("hello")) {
            sender.sendMessage("Hello World!");
            return true;
        }
        if (label.equalsIgnoreCase("givepickaxe")) {
            ItemStack diamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
            ItemMeta pickaxeMeta = diamondPickaxe.getItemMeta();

            if (pickaxeMeta != null) {
                pickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 255, true);  // Efficiency 255
                pickaxeMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 255, true);  // Fortune 255

                AttributeModifier hasteModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
                pickaxeMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, hasteModifier);
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Haste 3");
                pickaxeMeta.setLore(lore);

                diamondPickaxe.setItemMeta(pickaxeMeta);

                player.getInventory().addItem(diamondPickaxe);
                player.sendMessage(ChatColor.GREEN + "You have received a Diamond Pickaxe with Efficiency 255 and Fortune 255!");
            } else {
                player.sendMessage(ChatColor.RED + "Failed to create Diamond Pickaxe!");
            }

            return true;
        }
        return false;
    }
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if (item != null && item.getType() == Material.DIAMOND_PICKAXE) {
            ItemMeta itemMeta = item.getItemMeta(); // get item meta

            // check for custom Enchants & apply
            if (itemMeta != null && itemMeta.hasLore() && itemMeta.getLore().contains(ChatColor.GRAY + "Haste 3")) {
                // Apply Haste 3 when holding the Diamond Pickaxe with "Haste 3" lore
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 2, true, false));
            }
        } else {
            // Remove Haste effect when not holding the Diamond Pickaxe or without "Haste 3" lore
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
        }
    }
}

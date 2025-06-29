package com.example.plotmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Simple PlotMenu plugin for Minecraft 1.21.
 */
public class PlotMenu extends JavaPlugin implements Listener {

    private String menuTitle;

    private String item(String path, String def) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path, def));
    }

    private void loadValues() {
        menuTitle = item("menu-title", "&aPlot Menu");
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadValues();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("PlotMenu enabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("plotmenu.reload")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission.");
                return true;
            }
            reloadConfig();
            loadValues();
            sender.sendMessage(ChatColor.GREEN + "PlotMenu configuration reloaded.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can open the menu.");
            return true;
        }

        Player player = (Player) sender;
        openMenu(player);
        return true;
    }

    private void openMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, 27, menuTitle);

        inv.setItem(10, createItem(Material.GRASS_BLOCK, item("items.claim", "&aClaim Plot")));
        inv.setItem(11, createItem(Material.ENDER_PEARL, item("items.home", "&bTeleport Home")));
        inv.setItem(12, createItem(Material.WHITE_BED, item("items.sethome", "&eSet Home")));
        inv.setItem(14, createItem(Material.PLAYER_HEAD, item("items.addplayer", "&eAdd Player")));
        inv.setItem(15, createItem(Material.REDSTONE_BLOCK, item("items.removeplayer", "&cRemove Player")));
        inv.setItem(19, createItem(Material.TNT, item("items.clear", "&cClear Plot")));
        inv.setItem(20, createItem(Material.PAPER, item("items.reset", "&cReset Plot")));
        inv.setItem(22, createItem(Material.BOOK, item("items.info", "&9Plot Info")));
        inv.setItem(26, createItem(Material.BARRIER, item("items.close", "&4Close")));

        player.openInventory(inv);
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(menuTitle)) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        switch (clicked.getType()) {
            case GRASS_BLOCK:
                player.performCommand("plot claim");
                break;
            case ENDER_PEARL:
                player.performCommand("plot home");
                break;
            case WHITE_BED:
                player.performCommand("plot sethome");
                break;
            case PLAYER_HEAD:
                player.sendMessage(ChatColor.YELLOW + "Use /plot trust <player>");
                player.closeInventory();
                break;
            case REDSTONE_BLOCK:
                player.sendMessage(ChatColor.YELLOW + "Use /plot deny <player>");
                player.closeInventory();
                break;
            case TNT:
                player.performCommand("plot clear");
                break;
            case PAPER:
                player.performCommand("plot reset");
                break;
            case BOOK:
                player.performCommand("plot info");
                break;
            case BARRIER:
                player.closeInventory();
                break;
            default:
                break;
        }
    }
}

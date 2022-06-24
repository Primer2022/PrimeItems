package ru.primer.mc.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.primer.mc.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        FileConfiguration cfg = Main.getInstance().getConfig();
        Player p = (Player)s;
        if(!(s instanceof Player)) {
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("not-player")));
            return true;
        }
        if(p.hasPermission("primeitems.use")) {
            if(args.length == 0) {
                cfg.getStringList("help").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                return true;
            }
            switch (args[0]) {
                case ("info"):
                    ItemStack itemInfo = p.getInventory().getItemInMainHand();
                    if (itemInfo.getType() != Material.AIR) {
                        ItemMeta meta = itemInfo.getItemMeta();
                        String material = itemInfo.getType().name();
                        int amount = itemInfo.getAmount();
                        String name = meta.getDisplayName();
                        String amountStr = String.valueOf(amount);
                        if(name == "") {
                            String nameNull = ChatColor.translateAlternateColorCodes('&', "&c(Нету)");
                            cfg.getStringList("item-info").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%name%", nameNull).replace("%material%", material).replace("%amount%", amountStr))));
                        }
                        if(name != "") {
                            cfg.getStringList("item-info").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%name%", name).replace("%material%", material).replace("%amount%", amountStr))));
                        }
                        if(meta.getLore() != null) {
                            List<String> lore = meta.getLore();
                            for (int i = 0; i < lore.size(); i++) {
                                int index = i + 1;
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + index + ".&f " + lore.get(i)));
                            }
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Пусто"));
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("invalid-item")));
                break;
                case ("setname"):
                    if(args.length >= 2){
                        ItemStack itemName = p.getInventory().getItemInMainHand();
                        if(itemName.getType() != Material.AIR) {
                            p.getInventory().remove(itemName);
                            int index = p.getInventory().getHeldItemSlot();
                            ItemMeta meta = itemName.getItemMeta();
                            StringBuilder sb = new StringBuilder();
                            for (int i = 1; i < args.length; i++) sb.append(args[i]).append(' ');
                            if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
                            String name = sb.toString();
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                            itemName.setItemMeta(meta);
                            p.getInventory().setItem(index, itemName);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("succes-set-name").replace("%name%", name)));
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("invalid-item")));
                        return true;
                    }
                    cfg.getStringList("help").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                break;
                case ("addlore"):
                    if(args.length >= 2){
                        ItemStack itemLore = p.getInventory().getItemInMainHand();
                        if(itemLore.getType() != Material.AIR) {
                            p.getInventory().remove(itemLore);
                            int index = p.getInventory().getHeldItemSlot();
                            ItemMeta meta = itemLore.getItemMeta();
                            StringBuilder sb = new StringBuilder();
                            for (int i = 1; i < args.length; i++) sb.append(args[i]).append(' ');
                            if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
                            String lore = sb.toString();
                            if(meta.getLore() == null) {
                                meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore)));
                                itemLore.setItemMeta(meta);
                                p.getInventory().setItem(index, itemLore);
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("succes-add-lore").replace("%lore%", lore)));
                                return true;
                            }
                            List<String> loreList = meta.getLore();
                            loreList.add(ChatColor.translateAlternateColorCodes('&',lore));
                            meta.setLore(loreList);
                            itemLore.setItemMeta(meta);
                            p.getInventory().setItem(index, itemLore);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("succes-add-lore").replace("%lore%", lore)));
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("invalid-item")));
                        return true;
                    }
                    cfg.getStringList("help").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                break;
                case ("dellore"):
                    if(args.length >= 2){
                        try {
                            int number = Integer.parseInt(args[1]);
                            ItemStack itemLore = p.getInventory().getItemInMainHand();
                            if(itemLore.getType() != Material.AIR) {
                                ItemMeta meta = itemLore.getItemMeta();
                                if(meta.getLore() == null) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("no-lore")));
                                    return true;
                                }
                                int index = p.getInventory().getHeldItemSlot();
                                List<String> loreList = meta.getLore();
                                int amount = number - 1;
                                if(loreList.size() < number) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("lore-size")));
                                    return true;
                                }
                                p.getInventory().remove(itemLore);
                                    if (loreList.get(amount) != null){
                                        if(loreList.size() == 1) {
                                            loreList = null;
                                            meta.setLore(loreList);
                                            itemLore.setItemMeta(meta);
                                            p.getInventory().setItem(index, itemLore);
                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("succes-del-lore").replace("%number%", args[1])));
                                            return true;
                                        }
                                        loreList.remove(amount);
                                        meta.setLore(loreList);
                                        itemLore.setItemMeta(meta);
                                        p.getInventory().setItem(index, itemLore);
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("succes-del-lore").replace("%number%", args[1])));
                                        return true;
                                    }
                                }
                        }catch (NumberFormatException e) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("not-int")));
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("invalid-item")));
                        return true;
                    }
                break;
            }
            return true;
        }
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("no-permission")));
        return true;
    }
}

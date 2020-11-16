package xyz.velium.uhc.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Inventories {

    private UHC uhc;

    public Inventories(UHC uhc) {
        this.uhc = uhc;
    }

    public void loadLobbyHotBar(Player player) {
        if(uhc.getTeamManager().isTeams()) {
            player.getInventory().setItem(2, new ItemStackBuilder().setMaterial(Material.NETHER_STAR).setName(ChatColor.AQUA + "Scenarios").build());
            player.getInventory().setItem(4, new ItemStackBuilder().setName(ChatColor.LIGHT_PURPLE + "Your Team").buildCustomHead(player));
            player.getInventory().setItem(6, new ItemStackBuilder().setMaterial(Material.BOOK).setName(ChatColor.YELLOW + "Configuration").build());
        } else {
            player.getInventory().setItem(3, new ItemStackBuilder().setMaterial(Material.NETHER_STAR).setName(ChatColor.AQUA + "Scenarios").build());
            player.getInventory().setItem(5, new ItemStackBuilder().setMaterial(Material.BOOK).setName(ChatColor.YELLOW + "Configuration").build());

        }
    }

    public void startGameHotBar(Player player) {
        player.getInventory().addItem(new ItemStackBuilder().setMaterial(Material.COOKED_BEEF).setAmount(uhc.getConfigManager().getStarterFood()).build());
    }

    public void loadPracticeHotBar(Player player) {
        player.getInventory().setHelmet(new ItemStackBuilder().setMaterial(Material.IRON_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).build());
        player.getInventory().setChestplate(new ItemStackBuilder().setMaterial(Material.IRON_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).build());
        player.getInventory().setLeggings(new ItemStackBuilder().setMaterial(Material.IRON_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).build());
        player.getInventory().setBoots(new ItemStackBuilder().setMaterial(Material.IRON_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).build());

        player.getInventory().setItem(0, new ItemStackBuilder().setMaterial(Material.IRON_SWORD).addEnchantment(Enchantment.DAMAGE_ALL, 1).setUnbreakable(true).build());
        player.getInventory().setItem(1, new ItemStackBuilder().setMaterial(Material.FISHING_ROD).setUnbreakable(true).build());
        player.getInventory().setItem(2, new ItemStackBuilder().setMaterial(Material.BOW).addEnchantment(Enchantment.ARROW_INFINITE, 1).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setUnbreakable(true).build());
        player.getInventory().setItem(3, new ItemStackBuilder().setMaterial(Material.WATER_BUCKET).build());
        player.getInventory().setItem(20, new ItemStackBuilder().setMaterial(Material.ARROW).build());
    }

    public void openScenarioInventory(Player player) {
        Inventory scenarioInventory = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE + "Scenarios");

        for (Scenarios scenario : Scenarios.VALUES) {
            if (scenario.isEnabled()) {
                ItemStack itemStack = scenario.getItemStack();
                ItemMeta itemMeta = scenario.getItemStack().getItemMeta();
                itemMeta.setLore(Arrays.asList(scenario.getDescription()));
                itemMeta.setDisplayName(scenario.getScenarioName());
                itemStack.setItemMeta(itemMeta);
                scenarioInventory.addItem(itemStack);
            }
        }

        player.openInventory(scenarioInventory);
    }

    public void openScenarioEditorInventory(Player player) {

        Inventory scenarioEditorInventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + "Scenarios");

        scenarioEditorInventory.clear();
        for (Scenarios scenarios : Scenarios.VALUES) {
            ItemStack itemStack = scenarios.getItemStack().clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<String>();
            if (scenarios.isEnabled()) {
                lore.add(ChatColor.GRAY + "» " + ChatColor.GREEN + "Enabled");
            } else {
                lore.add(ChatColor.GRAY + "» " + ChatColor.RED + "Disabled");
            }
            itemMeta.setDisplayName(scenarios.getScenarioName());
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            scenarioEditorInventory.addItem(itemStack);
        }

        player.openInventory(scenarioEditorInventory);
    }

    public void openCurrentTeamInventory(Player player) {
        Inventory teamInventory = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE + "Your Team");
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhc.getTeamManager().isTeams()) {
            if(uhcPlayer.getTeam() != null) {
                for(UUID uuid : uhcPlayer.getTeam().getPlayers()) {
                    Player teamPlayers = Bukkit.getPlayer(uuid);
                    teamInventory.addItem(new ItemStackBuilder().setName(ChatColor.LIGHT_PURPLE + teamPlayers.getName()).buildCustomHead(teamPlayers));
                }

                player.openInventory(teamInventory);
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You are not in a Team");
            }
        } else {
            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Teams are currently disabled");
        }
    }

    public void openConfigInventory(Player player) {
        Inventory configInventory = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "Configuration");

        //Glass Panes
        configInventory.setItem(0, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(1, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(2, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(3, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(4, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(5, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(6, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(7, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(8, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());

        //Information Panes
        configInventory.setItem(9, new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.AQUA + "Border Information").build());
        configInventory.setItem(18, new ItemStackBuilder().setMaterial(Material.WATCH).setName(ChatColor.AQUA + "Time Information").build());
        configInventory.setItem(27, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8194).setName(ChatColor.AQUA + "Potion Information").build());
        configInventory.setItem(36, new ItemStackBuilder().setMaterial(Material.NETHER_STAR).setName(ChatColor.AQUA + "Game Information").build());

        //Red Glass Panes
        configInventory.setItem(10, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());
        configInventory.setItem(19, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());
        configInventory.setItem(28, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());
        configInventory.setItem(37, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());

        //Config Items
        configInventory.setItem(11, new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.AQUA + "Border Size")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getGameManager().getBorderSize()).build());
        configInventory.setItem(12, new ItemStackBuilder().setMaterial(Material.WATCH).setName(ChatColor.AQUA + "Border Time")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + (uhc.getConfigManager().getBorderTime() + 5) + " Minutes").build());
        configInventory.setItem(20, new ItemStackBuilder().setMaterial(Material.DIAMOND_SWORD).setName(ChatColor.AQUA + "PvP Time")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().getPvpTime() + " Minutes").build());
        configInventory.setItem(21, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8261).setName(ChatColor.AQUA + "Heal Time")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().getHealTime() + " Minutes").build());
        configInventory.setItem(29, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8194).setName(ChatColor.AQUA + "Speed I")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isSpeed1()).build());
        configInventory.setItem(30, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8226).setName(ChatColor.AQUA + "Speed II")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isSpeed2()).build());
        configInventory.setItem(31, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8201).setName(ChatColor.AQUA + "Strength I")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isStrenght1()).build());
        configInventory.setItem(32, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8233).setName(ChatColor.AQUA + "Strength II")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isStrenght2()).build());
        configInventory.setItem(38, new ItemStackBuilder().setMaterial(Material.GOLDEN_APPLE).setDurability((byte) 1).setName(ChatColor.AQUA + "God Apples")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isGodApples()).build());
        configInventory.setItem(39, new ItemStackBuilder().setMaterial(Material.ENDER_PEARL).setName(ChatColor.AQUA + "Enderpearl Damage")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isEnderpearlDamage()).build());
        configInventory.setItem(40, new ItemStackBuilder().setMaterial(Material.SKULL_ITEM).setName(ChatColor.AQUA + "Max Players")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().getMaxPlayers()).build());
        configInventory.setItem(41, new ItemStackBuilder().setMaterial(Material.NETHERRACK).setName(ChatColor.AQUA + "Nether")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isNether()).build());
        configInventory.setItem(42, new ItemStackBuilder().setMaterial(Material.RED_ROSE).setName(ChatColor.AQUA + "Teams")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getTeamManager().isTeams()).build());
        configInventory.setItem(43, new ItemStackBuilder().setMaterial(Material.GOLD_HELMET).setName(ChatColor.AQUA + "Team Size")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + "To" + uhc.getTeamManager().getTeamSize()).build());


        //Glass Panes
        configInventory.setItem(45, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(46, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(47, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(48, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(49, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(50, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(51, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(52, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(53, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());

        player.openInventory(configInventory);
    }

    public void openConfigEditor(Player player) {
        Inventory configInventory = Bukkit.createInventory(null, 54, ChatColor.AQUA + "Edit Configuration");

        //Glass Panes
        configInventory.setItem(0, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(1, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(2, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(3, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(4, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(5, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(6, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(7, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(8, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());

        //Information Panes
        configInventory.setItem(9, new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.AQUA + "Border Information").build());
        configInventory.setItem(18, new ItemStackBuilder().setMaterial(Material.WATCH).setName(ChatColor.AQUA + "Time Information").build());
        configInventory.setItem(27, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8194).setName(ChatColor.AQUA + "Potion Information").build());
        configInventory.setItem(36, new ItemStackBuilder().setMaterial(Material.NETHER_STAR).setName(ChatColor.AQUA + "Game Information").build());

        //Red Glass Panes
        configInventory.setItem(10, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());
        configInventory.setItem(19, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());
        configInventory.setItem(28, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());
        configInventory.setItem(37, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());

        //Config Items
        configInventory.setItem(11, new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.AQUA + "Border Size")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getGameManager().getBorderSize()).build());
        configInventory.setItem(12, new ItemStackBuilder().setMaterial(Material.WATCH).setName(ChatColor.AQUA + "Border Time")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + (uhc.getConfigManager().getBorderTime() + 5) + " Minutes")
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Right Click -5 Minutes")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Left Click +5 Minutes")
                .build());
        configInventory.setItem(20, new ItemStackBuilder().setMaterial(Material.DIAMOND_SWORD).setName(ChatColor.AQUA + "PvP Time")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().getPvpTime() + " Minutes")
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Right Click -5 Minutes")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Left Click +5 Minutes")
                .build());
        configInventory.setItem(21, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8261).setName(ChatColor.AQUA + "Heal Time")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().getHealTime() + " Minutes")
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Right Click -5 Minutes")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Left Click +5 Minutes")
                .build());
        configInventory.setItem(29, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8194).setName(ChatColor.AQUA + "Speed I")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isSpeed1())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(30, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8226).setName(ChatColor.AQUA + "Speed II")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isSpeed2())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(31, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8201).setName(ChatColor.AQUA + "Strength I")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isStrenght1())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(32, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8233).setName(ChatColor.AQUA + "Strength II")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isStrenght2())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(38, new ItemStackBuilder().setMaterial(Material.GOLDEN_APPLE).setDurability((byte) 1).setName(ChatColor.AQUA + "God Apples")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isGodApples())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(39, new ItemStackBuilder().setMaterial(Material.ENDER_PEARL).setName(ChatColor.AQUA + "Enderpearl Damage")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isEnderpearlDamage())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(40, new ItemStackBuilder().setMaterial(Material.SKULL_ITEM).setName(ChatColor.AQUA + "Max Players")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().getMaxPlayers())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Right Click +10 Slots")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Left Click -10 Slots")
                .build());
        configInventory.setItem(41, new ItemStackBuilder().setMaterial(Material.NETHERRACK).setName(ChatColor.AQUA + "Nether")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isNether())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(42, new ItemStackBuilder().setMaterial(Material.RED_ROSE).setName(ChatColor.AQUA + "Teams")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getTeamManager().isTeams())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(43, new ItemStackBuilder().setMaterial(Material.GOLD_HELMET).setName(ChatColor.AQUA + "Team Size")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + "To" + uhc.getTeamManager().getTeamSize())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Right Click -1 Minutes")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Left Click +1 Minutes")
                .build());


        //Glass Panes
        configInventory.setItem(45, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(46, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(47, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(48, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(49, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(50, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(51, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(52, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(53, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());

        player.openInventory(configInventory);
    }

    public void reloadConfigEditInventory(Player player, Inventory configInventory) {
        //Glass Panes
        configInventory.setItem(0, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(1, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(2, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(3, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(4, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(5, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(6, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(7, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(8, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());

        //Information Panes
        configInventory.setItem(9, new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.AQUA + "Border Information").build());
        configInventory.setItem(18, new ItemStackBuilder().setMaterial(Material.WATCH).setName(ChatColor.AQUA + "Time Information").build());
        configInventory.setItem(27, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8194).setName(ChatColor.AQUA + "Potion Information").build());
        configInventory.setItem(36, new ItemStackBuilder().setMaterial(Material.NETHER_STAR).setName(ChatColor.AQUA + "Game Information").build());

        //Red Glass Panes
        configInventory.setItem(10, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());
        configInventory.setItem(19, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());
        configInventory.setItem(28, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());
        configInventory.setItem(37, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 14).setName(" ").build());

        //Config Items
        configInventory.setItem(11, new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.AQUA + "Border Size")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getGameManager().getBorderSize()).build());
        configInventory.setItem(12, new ItemStackBuilder().setMaterial(Material.WATCH).setName(ChatColor.AQUA + "Border Time")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + (uhc.getConfigManager().getBorderTime() + 5) + " Minutes")
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Right Click -5 Minutes")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Left Click +5 Minutes")
                .build());
        configInventory.setItem(20, new ItemStackBuilder().setMaterial(Material.DIAMOND_SWORD).setName(ChatColor.AQUA + "PvP Time")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().getPvpTime() + " Minutes")
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Right Click -5 Minutes")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Left Click +5 Minutes")
                .build());
        configInventory.setItem(21, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8261).setName(ChatColor.AQUA + "Heal Time")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().getHealTime() + " Minutes")
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Right Click -5 Minutes")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Left Click +5 Minutes")
                .build());
        configInventory.setItem(29, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8194).setName(ChatColor.AQUA + "Speed I")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isSpeed1())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(30, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8226).setName(ChatColor.AQUA + "Speed II")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isSpeed2())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(31, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8201).setName(ChatColor.AQUA + "Strength I")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isStrenght1())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(32, new ItemStackBuilder().setMaterial(Material.POTION).setDurability((byte) 8233).setName(ChatColor.AQUA + "Strength II")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isStrenght2())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(38, new ItemStackBuilder().setMaterial(Material.GOLDEN_APPLE).setDurability((byte) 1).setName(ChatColor.AQUA + "God Apples")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isGodApples())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(39, new ItemStackBuilder().setMaterial(Material.ENDER_PEARL).setName(ChatColor.AQUA + "Enderpearl Damage")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isEnderpearlDamage())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(40, new ItemStackBuilder().setMaterial(Material.SKULL_ITEM).setName(ChatColor.AQUA + "Max Players")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().getMaxPlayers())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Right Click -10 Slots")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Left Click +10 Slots")
                .build());
        configInventory.setItem(41, new ItemStackBuilder().setMaterial(Material.NETHERRACK).setName(ChatColor.AQUA + "Nether")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getConfigManager().isNether())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(42, new ItemStackBuilder().setMaterial(Material.RED_ROSE).setName(ChatColor.AQUA + "Teams")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + uhc.getTeamManager().isTeams())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Click to enable or disable")
                .build());
        configInventory.setItem(43, new ItemStackBuilder().setMaterial(Material.GOLD_HELMET).setName(ChatColor.AQUA + "Team Size")
                .addLore(ChatColor.GRAY + "» " + ChatColor.WHITE + "To" + uhc.getTeamManager().getTeamSize())
                .addLore(" ")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Right Click -1 Minutes")
                .addLore(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Left Click +1 Minutes")
                .build());

        //Glass Panes
        configInventory.setItem(45, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(46, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(47, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(48, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(49, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(50, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(51, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(52, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        configInventory.setItem(53, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
    }

    public void openBorderEditor(Player player) {
        Inventory borderInventory = Bukkit.createInventory(null, 18, ChatColor.DARK_GREEN + "Border Size");

        borderInventory.addItem(new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.DARK_GREEN + "2000").build());
        borderInventory.addItem(new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.DARK_GREEN + "1500").build());
        borderInventory.addItem(new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.DARK_GREEN + "1000").build());
        borderInventory.addItem(new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.DARK_GREEN + "500").build());
        borderInventory.addItem(new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.DARK_GREEN + "100").build());
        borderInventory.addItem(new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.DARK_GREEN + "50").build());
        borderInventory.addItem(new ItemStackBuilder().setMaterial(Material.BEDROCK).setName(ChatColor.DARK_GREEN + "25").build());

        player.openInventory(borderInventory);
    }

    //Staff Inventories
    public void loadSpectatorHotBar(Player player) {
        player.getInventory().setItem(0, new ItemStackBuilder().setMaterial(Material.COMPASS)
                .setName(ChatColor.YELLOW + "Alive Players").build());
    }

    public void loadStaffHotBar(Player player) {
        player.getInventory().setItem(0, new ItemStackBuilder().setMaterial(Material.COMPASS)
                .setName(ChatColor.YELLOW + "Alive Players").build());
        player.getInventory().setItem(1, new ItemStackBuilder().setMaterial(Material.DIAMOND_PICKAXE)
                .setName(ChatColor.AQUA + "Mining Players").build());
        player.getInventory().setItem(2, new ItemStackBuilder().setMaterial(Material.NETHER_BRICK)
                .setName(ChatColor.RED + "Nether Players").build());

        player.getInventory().setItem(5, new ItemStackBuilder().setMaterial(Material.CARPET)
                .setName(ChatColor.GOLD + "Teleport 0,0").build());
        player.getInventory().setItem(6, new ItemStackBuilder().setMaterial(Material.NETHER_STAR)
                .setName(ChatColor.DARK_AQUA + "Worlds").build());
        player.getInventory().setItem(7, new ItemStackBuilder().setMaterial(Material.MINECART)
                .setName(ChatColor.LIGHT_PURPLE + "Player Information").build());
        player.getInventory().setItem(8, new ItemStackBuilder().setMaterial(Material.BOOK)
                .setName(ChatColor.GREEN + "View Inventory").build());
    }

    public void openAlivePlayersInventory(Player player) {
        Inventory alivePlayersInventory = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "Alive Players");

        for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
            UHCPlayer uhcPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
            if(uhcPlayers.getPlayerStates() == PlayerStates.INGAME) {
                alivePlayersInventory.addItem(new ItemStackBuilder().setName(onlinePlayers.getName()).buildCustomHead(onlinePlayers));
            }
        }

        player.openInventory(alivePlayersInventory);
    }

    public void openMiningPlayersInventory(Player player) {
        Inventory miningPlayersInventory = Bukkit.createInventory(null, 54, ChatColor.AQUA + "Mining Players");

        for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
            UHCPlayer uhcPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
            if(uhcPlayers.getPlayerStates() == PlayerStates.INGAME) {
                if(onlinePlayers.getLocation().getBlockY() < 40) {
                    miningPlayersInventory.addItem(new ItemStackBuilder().setName(onlinePlayers.getName()).buildCustomHead(onlinePlayers));
                }
            }
        }

        player.openInventory(miningPlayersInventory);
    }

    public void openNetherPlayersInventory(Player player) {
        Inventory netherPlayersInventory = Bukkit.createInventory(null, 54, ChatColor.RED + "Nether Players");

        for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
            UHCPlayer uhcPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
            if(uhcPlayers.getPlayerStates() == PlayerStates.INGAME) {
                if(onlinePlayers.getWorld().getName().equalsIgnoreCase(uhc.getWorldManager().getUhc_nether())) {
                    netherPlayersInventory.addItem(new ItemStackBuilder().setName(onlinePlayers.getName()).buildCustomHead(onlinePlayers));
                }
            }
        }

        player.openInventory(netherPlayersInventory);
    }

    public void openWorldInventory(Player player) {
        Inventory worldInventory = Bukkit.createInventory(null, 9, ChatColor.DARK_AQUA + "Worlds");

        worldInventory.setItem(2, new ItemStackBuilder().setMaterial(Material.GRASS).setName(ChatColor.DARK_AQUA + "Game World").build());
        worldInventory.setItem(4, new ItemStackBuilder().setMaterial(Material.NETHERRACK).setName(ChatColor.DARK_AQUA + "Nether World").build());
        worldInventory.setItem(6, new ItemStackBuilder().setMaterial(Material.BRICK).setName(ChatColor.DARK_AQUA + "Lobby World").build());

        player.openInventory(worldInventory);
    }

    public void openViewInventory(Player player, Player target) {
        Inventory viewInventory = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Inventory");

        viewInventory.setItem(0, target.getInventory().getHelmet());
        viewInventory.setItem(1, target.getInventory().getChestplate());
        viewInventory.setItem(2, target.getInventory().getLeggings());
        viewInventory.setItem(3, target.getInventory().getBoots());
        viewInventory.setItem(4, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        viewInventory.setItem(5, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        viewInventory.setItem(6, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        viewInventory.setItem(7, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());
        viewInventory.setItem(8, new ItemStackBuilder().setMaterial(Material.STAINED_GLASS_PANE).setDurability((byte) 15).setName(" ").build());

        for (int i = 9; i < 45; i++) {
            int slot = i - 9;
            viewInventory.setItem(i, target.getInventory().getItem(slot));
        }

        player.openInventory(viewInventory);
    }

    public void openPlayerInformation(Player player, Player target) {
        Inventory playerInformation = Bukkit.createInventory(null, 9, ChatColor.LIGHT_PURPLE + "Player Information");

        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(target.getUniqueId());

        playerInformation.setItem(0, new ItemStackBuilder().setMaterial(Material.MOB_SPAWNER).setName(ChatColor.YELLOW + "Spawners: " + ChatColor.WHITE + uhcPlayer.getMinedSpaners()).build());
        playerInformation.setItem(1, new ItemStackBuilder().setMaterial(Material.DIAMOND_ORE).setName(ChatColor.AQUA + "Diamonds Mined: " + ChatColor.WHITE + uhcPlayer.getMinedDiamonds()).build());
        playerInformation.setItem(2, new ItemStackBuilder().setMaterial(Material.GOLD_ORE).setName(ChatColor.GOLD + "Gold Mined: " + ChatColor.WHITE + uhcPlayer.getMinedGold()).build());

        playerInformation.setItem(4, new ItemStackBuilder().setMaterial(Material.DIAMOND_SWORD).setName(ChatColor.DARK_AQUA + "Kills: " + ChatColor.WHITE + uhcPlayer.getKills()).build());

        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        ArrayList<String> killedPlayers = new ArrayList<>();
        for(String names : uhcPlayer.getKilledPlayers()) {
            killedPlayers.add(ChatColor.GRAY + "- " + ChatColor.WHITE + names);
        }

        itemMeta.setLore(killedPlayers);
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Killed Players");
        itemStack.setItemMeta(itemMeta);

        playerInformation.setItem(8, itemStack);

        player.openInventory(playerInformation);
    }
}

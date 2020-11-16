package xyz.velium.uhc.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.Scenarios;

public class InventoryClickListener implements Listener {

    private UHC uhc;

    public InventoryClickListener(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        ClickType clickType = event.getClick();

        if(inventory == null) return;
        //Canceled
        if(inventory.getName().equalsIgnoreCase(ChatColor.GREEN + "Inventory") || inventory.getName().equalsIgnoreCase(ChatColor.DARK_PURPLE + "Scenarios") || inventory.getName().equalsIgnoreCase(ChatColor.GREEN + "Inventory") || inventory.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Player Information") || inventory.getName().equalsIgnoreCase(ChatColor.YELLOW + "Configuration") || inventory.getName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Your Team")) {
            if(itemStack == null) return;
            if(itemStack.getItemMeta() == null) return;
            if(itemStack.getItemMeta().getDisplayName() == null) return;

            event.setCancelled(true);
        }
        //Border Inventory
        if(inventory.getName().equalsIgnoreCase(ChatColor.DARK_GREEN + "Border Size")) {
            if(itemStack == null) return;
            if(itemStack.getItemMeta() == null) return;
            if(itemStack.getItemMeta().getDisplayName() == null) return;

             if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GREEN + "2000")) {
                player.getOpenInventory().close();

                uhc.getGameManager().setBorderStartingNumber(0);

                if (uhc.getGameStates() == GameStates.INGAME) {
                    uhc.getBorderManager().createBorderlayer(uhc.getWorldManager().getUhc_world(), 2000);
                }

                Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The " + ChatColor.GREEN + "Border Size " + ChatColor.YELLOW + "has been set to " + itemStack.getItemMeta().getDisplayName() + "x" + itemStack.getItemMeta().getDisplayName());
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GREEN + "1500")) {
                player.getOpenInventory().close();

                uhc.getGameManager().setBorderStartingNumber(1);

                if (uhc.getGameStates() == GameStates.INGAME) {
                    uhc.getBorderManager().createBorderlayer(uhc.getWorldManager().getUhc_world(), 1500);
                }

                Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The " + ChatColor.GREEN + "Border Size " + ChatColor.YELLOW + "has been set to " + itemStack.getItemMeta().getDisplayName() + "x" + itemStack.getItemMeta().getDisplayName());
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GREEN + "1000")) {
                player.getOpenInventory().close();

                uhc.getGameManager().setBorderStartingNumber(2);

                if (uhc.getGameStates() == GameStates.INGAME) {
                    uhc.getBorderManager().createBorderlayer(uhc.getWorldManager().getUhc_world(), 1000);
                }

                Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The " + ChatColor.GREEN + "Border Size " + ChatColor.YELLOW + "has been set to " + itemStack.getItemMeta().getDisplayName() + "x" + itemStack.getItemMeta().getDisplayName());
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GREEN + "500")) {
                player.getOpenInventory().close();

                uhc.getGameManager().setBorderStartingNumber(3);

                if (uhc.getGameStates() == GameStates.INGAME) {
                    uhc.getBorderManager().createBorderlayer(uhc.getWorldManager().getUhc_world(), 500);
                }

                Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The " + ChatColor.GREEN + "Border Size " + ChatColor.YELLOW + "has been set to " + itemStack.getItemMeta().getDisplayName() + "x" + itemStack.getItemMeta().getDisplayName());
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GREEN + "100")) {
                player.getOpenInventory().close();

                uhc.getGameManager().setBorderStartingNumber(4);

                if (uhc.getGameStates() == GameStates.INGAME) {
                    uhc.getBorderManager().createBorderlayer(uhc.getWorldManager().getUhc_world(), 100);
                }

                Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The " + ChatColor.GREEN + "Border Size " + ChatColor.YELLOW + "has been set to " + itemStack.getItemMeta().getDisplayName() + "x" + itemStack.getItemMeta().getDisplayName());
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GREEN + "50")) {
                player.getOpenInventory().close();

                uhc.getGameManager().setBorderStartingNumber(5);

                if (uhc.getGameStates() == GameStates.INGAME) {
                    uhc.getBorderManager().createBorderlayer(uhc.getWorldManager().getUhc_world(), 50);
                }

                Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The " + ChatColor.GREEN + "Border Size " + ChatColor.YELLOW + "has been set to " + itemStack.getItemMeta().getDisplayName() + "x" + itemStack.getItemMeta().getDisplayName());
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GREEN + "25")) {
                player.getOpenInventory().close();

                uhc.getGameManager().setBorderStartingNumber(6);

                if (uhc.getGameStates() == GameStates.INGAME) {
                    uhc.getBorderManager().createBorderlayer(uhc.getWorldManager().getUhc_world(), 25);
                }

                Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The " + ChatColor.GREEN + "Border Size " + ChatColor.YELLOW + "has been set to " + itemStack.getItemMeta().getDisplayName() + "x" + itemStack.getItemMeta().getDisplayName());
            }
        }

        //Config Editor
        if(inventory.getName().equalsIgnoreCase(ChatColor.AQUA + "Edit Configuration")) {
            if(itemStack == null) return;
            if(itemStack.getItemMeta() == null) return;
            if(itemStack.getItemMeta().getDisplayName() == null) return;

            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Border Size")) {
                player.getOpenInventory().close();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        uhc.getInventories().openBorderEditor(player);
                        this.cancel();
                    }
                }.runTaskLater(uhc, 2);

                event.setCancelled(true);
            }
            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Border Time")) {
                if(clickType == ClickType.LEFT) {
                    uhc.getConfigManager().setBorderTime(uhc.getConfigManager().getBorderTime() + 5);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Border Time " + ChatColor.YELLOW + "has been set to " + ChatColor.LIGHT_PURPLE + (uhc.getConfigManager().getBorderTime() + 5) + ChatColor.YELLOW + " Minutes");
                    event.setCancelled(true);
                } else if(clickType == ClickType.RIGHT) {
                    uhc.getConfigManager().setBorderTime(uhc.getConfigManager().getBorderTime() - 5);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() +  ChatColor.AQUA + "Border Time " + ChatColor.YELLOW + "has been set to " + ChatColor.LIGHT_PURPLE + (uhc.getConfigManager().getBorderTime() + 5) + ChatColor.YELLOW + " Minutes");
                    event.setCancelled(true);
                }
            } if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Team Size")) {
                if(clickType == ClickType.LEFT) {
                    uhc.getTeamManager().setTeamSize(uhc.getTeamManager().getTeamSize() + 1);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Team Size " + ChatColor.YELLOW + "has been set to " + ChatColor.LIGHT_PURPLE + uhc.getTeamManager().getTeamSize());
                    event.setCancelled(true);
                } else if(clickType == ClickType.RIGHT) {
                    uhc.getTeamManager().setTeamSize(uhc.getTeamManager().getTeamSize() - 1);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() +  ChatColor.AQUA + "Team Size " + ChatColor.YELLOW + "has been set to " + ChatColor.LIGHT_PURPLE + uhc.getTeamManager().getTeamSize());
                    event.setCancelled(true);
                }
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "PvP Time")) {
                if(clickType == ClickType.LEFT) {
                    uhc.getConfigManager().setPvpTime(uhc.getConfigManager().getPvpTime() + 5);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "PvP Time " + ChatColor.YELLOW + "has been set to " + ChatColor.LIGHT_PURPLE + uhc.getConfigManager().getPvpTime() + ChatColor.YELLOW + " Minutes");
                    event.setCancelled(true);
                } else if(clickType == ClickType.RIGHT) {
                    uhc.getConfigManager().setPvpTime(uhc.getConfigManager().getPvpTime() - 5);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "PvP Time " + ChatColor.YELLOW + "has been set to " + ChatColor.LIGHT_PURPLE + uhc.getConfigManager().getPvpTime() + ChatColor.YELLOW + " Minutes");
                    event.setCancelled(true);
                }
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Heal Time")) {
                if(clickType == ClickType.LEFT) {
                    uhc.getConfigManager().setHealTime(uhc.getConfigManager().getHealTime() + 5);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Heal Time " + ChatColor.YELLOW + "has been set to " + ChatColor.LIGHT_PURPLE + uhc.getConfigManager().getHealTime() + ChatColor.YELLOW + " Minutes");
                    event.setCancelled(true);
                } else if(clickType == ClickType.RIGHT) {
                    uhc.getConfigManager().setHealTime(uhc.getConfigManager().getHealTime() - 5);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Heal Time " + ChatColor.YELLOW + "has been set to " + ChatColor.LIGHT_PURPLE + uhc.getConfigManager().getHealTime() + ChatColor.YELLOW + " Minutes");
                    event.setCancelled(true);
                }
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Max Players")) {
                if(clickType == ClickType.LEFT) {
                    uhc.getConfigManager().setMaxPlayers(uhc.getConfigManager().getMaxPlayers() + 10);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Max Players " + ChatColor.YELLOW + "has been set to " + ChatColor.LIGHT_PURPLE + uhc.getConfigManager().getMaxPlayers());
                    event.setCancelled(true);
                } else if(clickType == ClickType.RIGHT) {
                    uhc.getConfigManager().setMaxPlayers(uhc.getConfigManager().getMaxPlayers() - 10);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Max Players " + ChatColor.YELLOW + "has been set to " + ChatColor.LIGHT_PURPLE + uhc.getConfigManager().getMaxPlayers());
                    event.setCancelled(true);
                }
            }  else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Teams")) {
                if(uhc.getTeamManager().isTeams()) {
                    uhc.getTeamManager().setTeams(false);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Teams " + ChatColor.YELLOW + "has been " + ChatColor.RED + "Disabled");
                    event.setCancelled(true);
                } else {
                    uhc.getTeamManager().setTeams(true);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Teams " + ChatColor.YELLOW + "has been " + ChatColor.GREEN + "Enabled");
                    event.setCancelled(true);
                }
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Speed I")) {
                if(uhc.getConfigManager().isSpeed1()) {
                    uhc.getConfigManager().setSpeed1(false);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Speed I " + ChatColor.YELLOW + "has been " + ChatColor.RED + "Disabled");
                    event.setCancelled(true);
                } else {
                    uhc.getConfigManager().setSpeed1(true);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Speed I " + ChatColor.YELLOW + "has been " + ChatColor.GREEN + "Enabled");
                    event.setCancelled(true);
                }
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Speed II")) {
                if(uhc.getConfigManager().isSpeed2()) {
                    uhc.getConfigManager().setSpeed2(false);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Speed II " + ChatColor.YELLOW + "has been " + ChatColor.RED + "Disabled");
                    event.setCancelled(true);
                } else {
                    uhc.getConfigManager().setSpeed2(true);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Speed II " + ChatColor.YELLOW + "has been " + ChatColor.GREEN + "Enabled");
                    event.setCancelled(true);
                }
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Strength II")) {
                if(uhc.getConfigManager().isStrenght2()) {
                    uhc.getConfigManager().setStrenght2(false);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Strength II " + ChatColor.YELLOW + "has been " + ChatColor.RED + "Disabled");
                    event.setCancelled(true);
                } else {
                    uhc.getConfigManager().setStrenght2(true);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Strength II " + ChatColor.YELLOW + "has been " + ChatColor.GREEN + "Enabled");
                    event.setCancelled(true);
                }
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Strength I")) {
                if(uhc.getConfigManager().isStrenght1()) {
                    uhc.getConfigManager().setStrenght1(false);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Strength I " + ChatColor.YELLOW + "has been " + ChatColor.RED + "Disabled");
                    event.setCancelled(true);
                } else {
                    uhc.getConfigManager().setStrenght1(true);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Strength I " + ChatColor.YELLOW + "has been " + ChatColor.GREEN + "Enabled");
                    event.setCancelled(true);
                }
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Enderpearl Damage")) {
                if(uhc.getConfigManager().isEnderpearlDamage()) {
                    uhc.getConfigManager().setEnderpearlDamage(false);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Enderpearl Damage " + ChatColor.YELLOW + "has been " + ChatColor.RED + "Disabled");
                    event.setCancelled(true);
                } else {
                    uhc.getConfigManager().setEnderpearlDamage(true);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Enderpearl Damage " + ChatColor.YELLOW + "has been " + ChatColor.GREEN + "Enabled");
                    event.setCancelled(true);
                }
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "God Apples")) {
                if(uhc.getConfigManager().isGodApples()) {
                    uhc.getConfigManager().setGodApples(false);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "God Apples " + ChatColor.YELLOW + "has been " + ChatColor.RED + "Disabled");
                    event.setCancelled(true);
                } else {
                    uhc.getConfigManager().setGodApples(true);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "God Apples " + ChatColor.YELLOW + "has been " + ChatColor.GREEN + "Enabled");
                    event.setCancelled(true);
                }
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Nether")) {
                if(uhc.getConfigManager().isNether()) {
                    uhc.getConfigManager().setNether(false);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Nether " + ChatColor.YELLOW + "has been " + ChatColor.RED + "Disabled");
                    event.setCancelled(true);
                } else {
                    uhc.getConfigManager().setNether(true);
                    uhc.getInventories().reloadConfigEditInventory(player, player.getOpenInventory().getTopInventory());
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Nether " + ChatColor.YELLOW + "has been " + ChatColor.GREEN + "Enabled");
                    event.setCancelled(true);
                }
            }
            event.setCancelled(true);
        }

        //Scenario Editor
        if (inventory.getName().equalsIgnoreCase(ChatColor.DARK_GREEN + "Scenarios")) {
            if(itemStack == null) return;
            if(itemStack.getItemMeta() == null) return;
            if(itemStack.getItemMeta().getDisplayName() == null) return;

            for (Scenarios scenarios : Scenarios.VALUES) {
                if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(scenarios.getScenarioName())) {
                    if (scenarios.isEnabled()) {
                        scenarios.disableScenario();
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.RED + scenarios.getScenarioName() + ChatColor.YELLOW + " has been Disabled");
                        player.getOpenInventory().close();
                        uhc.getInventories().openScenarioEditorInventory(player);
                        event.setCancelled(true);
                        return;
                    } else {
                        scenarios.enableScenario();
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.GREEN + scenarios.getScenarioName() + ChatColor.YELLOW + " has been Enabled");
                        player.getOpenInventory().close();
                        uhc.getInventories().openScenarioEditorInventory(player);
                        event.setCancelled(true);
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}

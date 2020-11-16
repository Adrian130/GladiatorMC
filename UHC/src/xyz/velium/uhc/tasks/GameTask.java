package xyz.velium.uhc.tasks;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.BorderSize;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

@Getter
@Setter
public class GameTask {

    private UHC uhc;

    public GameTask(UHC uhc) {
        this.uhc = uhc;
    }

    private int spectatorTask;

    private int gameTask;
    private int gameTime;

    private int borderTime = 61;
    
    private void checkSpectatorRadius() {
        spectatorTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayers : uhc.getOnlinePlayers()) {
                    UHCPlayer uhcPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
                    if (uhcPlayers.getPlayerStates() == PlayerStates.SPECTATOR) {
                        if (onlinePlayers.getLocation().getX() >= 50 || onlinePlayers.getLocation().getY() < 45 || onlinePlayers.getLocation().getZ() >= 50 || onlinePlayers.getLocation().getX() <= -50 || onlinePlayers.getLocation().getZ() <= -50) {
                            Location location = new Location(Bukkit.getWorld(uhc.getWorldManager().getUhc_world()), 0,  Bukkit.getWorld(uhc.getWorldManager().getUhc_world()).getHighestBlockYAt(0, 0) + 5, 0);
                            onlinePlayers.teleport(location);
                            onlinePlayers.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You can´t go further than 50x50");
                        }
                    }
                }
            }
        }, 60, 60);
    }

    private void finalHeal() {
        for (Player player : uhc.getOnlinePlayers()) {
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
            if(uhcPlayer.getPlayerStates() == PlayerStates.INGAME) {
                player.setHealth(20);
                player.setFoodLevel(20);
            }
        }
        for (Player player : uhc.getOnlinePlayers()) {
            player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "All players have been healed");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        }
    }

    private void enablePvP() {
        uhc.getConfigManager().setPvp(true);

        for (Player player : uhc.getOnlinePlayers()) {
            player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "PvP has beeen Enabled, Good Luck!");
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
        }
    }
    
    public int getBorderTimeInSeconds(int number) {
        return (uhc.getConfigManager().getBorderTime() + (number * 5)) * 60;
    }
    

    private void shrinkBorder(final int radius) {
        (new BukkitRunnable() {
            public void run() {
                borderTime--;
                if (borderTime == 60 || borderTime == 45 || borderTime == 30 || borderTime == 10 || borderTime == 5 || borderTime == 4 || borderTime == 3 || borderTime == 2 || borderTime == 1) {

                    BorderSize borderSize = BorderSize.getBorderByNumber(uhc.getGameManager().getBorderNumber(), uhc.getGameManager().getBorderStartingNumber());

                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The Border will shrink to " + ChatColor.LIGHT_PURPLE + borderSize.getSize() + "x" + borderSize.getSize() + ChatColor.YELLOW + " in " + ChatColor.LIGHT_PURPLE + borderTime + " second(s)");

                    for (Player onlinePlayers : uhc.getOnlinePlayers()) {
                        onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.ORB_PICKUP, 2, 2);
                    }

                } else if (borderTime == 0) {
                    BorderSize borderSize = BorderSize.getBorderByNumber(uhc.getGameManager().getBorderNumber(), uhc.getGameManager().getBorderStartingNumber());

                    uhc.getGameManager().setBorderNumber(uhc.getGameManager().getBorderNumber() + 1);

                    uhc.getBorderManager().createBorderlayer(uhc.getWorldManager().getUhc_world(), borderSize.getSize());

                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The Border has shrunk to " + ChatColor.LIGHT_PURPLE + borderSize.getSize() + "x" + borderSize.getSize());

                    for (Player onlinePlayers : uhc.getOnlinePlayers()) {
                        onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.ORB_PICKUP, 2, 2);
                    }

                    borderTime = 61;
                    this.cancel();
                }
            }
        }).runTaskTimer(uhc, 0L, 20L);
    }

    public void startUHCGame() {

        uhc.getCombatLoggerTask().startCombatLoggerTask();

        this.checkSpectatorRadius();

        this.gameTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, new BukkitRunnable() {
            @Override
            public void run() {

                gameTime++;

                if (gameTime == uhc.getConfigManager().getHealTime() * 60 ) {
                    finalHeal();
                } else if (gameTime == uhc.getConfigManager().getPvpTime() * 60 ) {
                    enablePvP();
                }

                if(gameTime == uhc.getConfigManager().getBorderTime() - 5 * 60) {
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.AQUA + "Permanent Day has been enabled");
                }

                if(uhc.getGameManager().getBorderSize() >= 25) {
                    if (gameTime == getBorderTimeInSeconds(uhc.getGameManager().getBorderNumber()) - (5 * 60)) {
                        BorderSize borderSize = BorderSize.getBorderByNumber(uhc.getGameManager().getBorderNumber(), uhc.getGameManager().getBorderStartingNumber());
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The Border will shrink to " + ChatColor.LIGHT_PURPLE + borderSize.getSize() + "x" + borderSize.getSize() + ChatColor.YELLOW + " in " + ChatColor.LIGHT_PURPLE + "5" + ChatColor.YELLOW + " Minutes");
                    } else if (gameTime == getBorderTimeInSeconds(uhc.getGameManager().getBorderNumber()) - (4 * 60)) {
                        BorderSize borderSize = BorderSize.getBorderByNumber(uhc.getGameManager().getBorderNumber(), uhc.getGameManager().getBorderStartingNumber());
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The Border will shrink to " + ChatColor.LIGHT_PURPLE + borderSize.getSize() + "x" + borderSize.getSize() + ChatColor.YELLOW + " in " + ChatColor.LIGHT_PURPLE + "4" + ChatColor.YELLOW + " Minutes");
                    } else if (gameTime == getBorderTimeInSeconds(uhc.getGameManager().getBorderNumber()) - (3 * 60)) {
                        BorderSize borderSize = BorderSize.getBorderByNumber(uhc.getGameManager().getBorderNumber(), uhc.getGameManager().getBorderStartingNumber());
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The Border will shrink to " + ChatColor.LIGHT_PURPLE + borderSize.getSize() + "x" + borderSize.getSize() + ChatColor.YELLOW + " in " + ChatColor.LIGHT_PURPLE + "3" + ChatColor.YELLOW + " Minutes");
                    }  else if (gameTime == getBorderTimeInSeconds(uhc.getGameManager().getBorderNumber()) - (2 * 60)) {
                        BorderSize borderSize = BorderSize.getBorderByNumber(uhc.getGameManager().getBorderNumber(), uhc.getGameManager().getBorderStartingNumber());
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The Border will shrink to " + ChatColor.LIGHT_PURPLE + borderSize.getSize() + "x" + borderSize.getSize() + ChatColor.YELLOW + " in " + ChatColor.LIGHT_PURPLE + "2" + ChatColor.YELLOW + " Minutes");
                    }else if (gameTime == getBorderTimeInSeconds(uhc.getGameManager().getBorderNumber()) - 60) {
                        BorderSize borderSize = BorderSize.getBorderByNumber(uhc.getGameManager().getBorderNumber(), uhc.getGameManager().getBorderStartingNumber());
                        shrinkBorder(borderSize.getSize());
                    }
                }
            }
        }, 20, 20);
    }
}

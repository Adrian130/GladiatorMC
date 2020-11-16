package xyz.velium.uhc.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class GameManager {

    private UHC uhc;

    public GameManager(UHC uhc) {
        this.uhc = uhc;
    }

    private boolean practice = true;

    //Winner
    private ArrayList<UUID> winners = new ArrayList<>();

    private int borderSize = 200;
    private int practiceBorderSize = 50;

    private int borderNumber = 0;
    private int borderStartingNumber = 0;

    private boolean reset;
    private boolean end = false;

    public void prepareGame() {
        //Lobby World
        World world = Bukkit.getWorld(uhc.getWorldManager().getLobby_world());
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setTime(1300);
        world.setThundering(false);
        world.setStorm(false);

        //Practice
        uhc.getWorldManager().deleteWorld(uhc.getWorldManager().getUhc_practice());

        uhc.getWorldManager().createPracticeWorld();
        uhc.getBorderManager().createBorderlayer(uhc.getWorldManager().getUhc_practice(), this.practiceBorderSize);

        if(this.isReset()) {
            uhc.setGameStates(GameStates.PREPARE);
            uhc.getWorldManager().deleteWorld(uhc.getWorldManager().getUhc_world());
            uhc.getWorldManager().deleteWorld(uhc.getWorldManager().getUhc_nether());

            uhc.getWorldManager().createNetherWorld();
            uhc.getWorldManager().createUHCWorld();

            uhc.getWorldManager().loadWorld(uhc.getWorldManager().getUhc_world(), this.borderSize, 900);
        } else {
            uhc.setGameStates(GameStates.LOBBY);
        }
    }

    public void checkWin() {
        if(!uhc.getTeamManager().isTeams()) {
            if(uhc.getPlayerManager().getAlivePlayers().size() == 1) {
                if(!this.isEnd()) {
                    Player winner = Bukkit.getPlayer(uhc.getPlayerManager().getAlivePlayers().get(0));

                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "Congratulations to " + ChatColor.DARK_AQUA + winner.getName() + ChatColor.YELLOW + " for winning this UHC");
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.RED + "The Server will restart in " + ChatColor.DARK_RED + "60" + ChatColor.RED + " seconds");

                    uhc.getGameManager().setReset(true);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.shutdown();
                        }
                    }.runTaskLater(uhc, 20 * 60);
                }
            }
        } else {
            if(uhc.getTeamManager().getAliveTeams().size() == 1) {
                if(!this.isEnd()) {
                    this.setEnd(true);

                    List<String> winnerNames = new ArrayList<>();

                    for (UUID players : uhc.getPlayerManager().getAlivePlayers()) {
                        OfflinePlayer allPlayers = Bukkit.getOfflinePlayer(players);
                        winnerNames.add(allPlayers.getName());
                        this.winners.add(allPlayers.getUniqueId());
                    }

                    String[] stringArray = winnerNames.toArray(new String[winnerNames.size()]);

                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "Congratulations to " + ChatColor.DARK_AQUA + Arrays.toString(stringArray).replace("[", "").replace("]", "") + ChatColor.YELLOW + " for winning this UHC");
                    Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.RED + "The Server will restart in " + ChatColor.DARK_RED + "60" + ChatColor.RED + " seconds");

                    uhc.getConfigManager().setReset(true);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.shutdown();
                        }
                    }.runTaskLater(uhc, 20 * 60);
                }
            }
        }
    }

    public void setReset(boolean reset) {
        this.reset = reset;
        uhc.getConfig().set("reset", reset);
        uhc.saveConfig();
        uhc.reloadConfig();
    }
}

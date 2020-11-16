package xyz.velium.uhc.tasks;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_7_R4.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.entities.MobUtil;
import xyz.velium.uhc.entities.PlaceHolder;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.tean.Team;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

@Getter @Setter
public class StartTask implements Listener {

    private UHC uhc;
    private Random random;
    private ArrayList<UUID> playersToScatter;

    public StartTask(UHC uhc) {
        this.uhc = uhc;
        this.random = new Random();
        this.playersToScatter = new ArrayList<>();
    }

    private int scatterTask;

    private int startingTask;
    private int startingTime;

    private boolean started = false;

    public void scatterPlayersMethode() {
        int nextPlayer = this.random.nextInt(this.playersToScatter.size());

        Player player = Bukkit.getPlayer(this.playersToScatter.get(nextPlayer));
        player.teleport(uhc.getPlayerManager().getRandomLocation(uhc.getWorldManager().getUhc_world(), uhc.getGameManager().getBorderSize()));

        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if(uhc.getTeamManager().isTeams()) {
            //Team Scatter
            for(UUID teamMembers : uhcPlayer.getTeam().getPlayers()) {
                this.playersToScatter.remove(teamMembers);

                Player teamPlayers = Bukkit.getPlayer(teamMembers);
                UHCPlayer uhcPlayers = uhc.getPlayerManager().getUhcPlayers().get(teamMembers);

                if(teamPlayers != null) {
                    teamPlayers.teleport(player);
                }

                uhcPlayer.setupPlayer();
                uhcPlayers.setPlayerStates(PlayerStates.SCATTER);

                teamPlayers.getOpenInventory().close();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();
                        final PlaceHolder placeHolder = new PlaceHolder(worldServer);
                        MobUtil.spawnEntity(placeHolder, player.getLocation());

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                placeHolder.setInvisible(true);
                            }
                        }.runTaskLater(uhc, 4);

                        Horse horse = (Horse)placeHolder.getBukkitEntity();
                        horse.setTamed(true);
                        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                        horse.setPassenger(teamPlayers);
                    }
                }.runTaskLater(uhc, 30);
            }
        } else {
            this.playersToScatter.remove(player.getUniqueId());

            uhcPlayer.setupPlayer();
            uhcPlayer.setPlayerStates(PlayerStates.SCATTER);

            player.getOpenInventory().close();

            new BukkitRunnable() {
                @Override
                public void run() {
                    WorldServer worldServer = ((CraftWorld)player.getWorld()).getHandle();
                    final PlaceHolder placeHolder = new PlaceHolder(worldServer);
                    MobUtil.spawnEntity(placeHolder, player.getLocation());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            placeHolder.setInvisible(true);
                        }
                    }.runTaskLater(uhc, 4);

                    Horse horse = (Horse)placeHolder.getBukkitEntity();
                    horse.setTamed(true);
                    horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                    horse.setPassenger(player);
                }
            }.runTaskLater(uhc, 30);
        }
    }

    public void startScatterTask() {

        uhc.setGameStates(GameStates.SCATTER);
        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.GREEN + "The Scatter has started");

        //Disable Practice
        for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
            UHCPlayer uhcPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
            if(uhcPlayers.getPlayerStates() == PlayerStates.PRACTICE) {
                uhcPlayers.handlePractice();
            }
        }

        //Add Players to Scatter
        for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
            UHCPlayer uhcPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
            if(uhcPlayers.getPlayerStates() == PlayerStates.LOBBY || uhcPlayers.getPlayerStates() == PlayerStates.PRACTICE) {
                uhcPlayers.setupPlayer();
                uhcPlayers.setPlayerStates(PlayerStates.SCATTER);

                this.playersToScatter.add(onlinePlayers.getUniqueId());

                if(uhc.getTeamManager().isTeams()) {
                    if(uhcPlayers.getTeam() == null) {
                        uhc.getTeamManager().createTeam(Bukkit.getPlayer(uhcPlayers.getUuid()));
                    }
                }
            }
        }

        this.scatterTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, new BukkitRunnable() {
            @Override
            public void run() {
                if(playersToScatter.size() == 0) {
                    startGame();
                    Bukkit.getScheduler().cancelTask(scatterTask);
                    this.cancel();
                    return;
                } else {
                    scatterPlayersMethode();
                }
            }
        }, 20, 20);
    }

    public void startGame() {

        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.GREEN + "The Scatter has finished");
        this.setStartingTime(60);

        this.startingTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, new BukkitRunnable() {
            @Override
            public void run() {
                startingTime--;

                switch (startingTime) {
                    case 60: case 45: case 30: case 15: case 10: case 5: case 3: case 2: case 1:
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The Game will start in " + ChatColor.GREEN + startingTime + ChatColor.YELLOW + " seconds");
                        break;
                    case 0:
                        uhc.setGameStates(GameStates.INGAME);
                        uhc.getGameTask().startUHCGame();
                        uhc.getScenarioManager().startNoCleanTask();
                        Bukkit.broadcastMessage(uhc.getPREFIX() + ChatColor.YELLOW + "The game has started");


                        for(Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
                            onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.LEVEL_UP, 1, 1);
                            onlinePlayers.getOpenInventory().close();

                            UHCPlayer uhcPLayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
                            if(uhcPLayers.getPlayerStates() == PlayerStates.SCATTER) {
                                if(onlinePlayers.isInsideVehicle()) {
                                    onlinePlayers.getVehicle().remove();
                                }

                                uhcPLayers.setHasplayed(true);
                                uhcPLayers.setupPlayer();
                                uhcPLayers.setPlayerStates(PlayerStates.INGAME);

                                uhc.getInventories().startGameHotBar(onlinePlayers);
                                uhc.getPlayerManager().getAlivePlayers().add(onlinePlayers.getUniqueId());

                                if(uhc.getTeamManager().isTeams()) {
                                    Team team = uhcPLayers.getTeam();
                                    for(int i = 0; i < team.getPlayers().size(); i++) {
                                        if(!team.getAlivePlayers().contains(team.getPlayers().get(i))) {
                                            team.getAlivePlayers().add(team.getPlayers().get(i));
                                        }
                                    }
                                }
                            }
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for(Player player : uhc.getOnlinePlayers()) {
                                    player.setHealth(20);
                                    player.setFoodLevel(20);
                                    player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You health and hunger has been restored");
                                }
                            }
                        }.runTaskLater(uhc, 20 * 2);

                        Bukkit.getScheduler().cancelTask(startingTask);
                        break;
                }
            }
        }, 20, 20);
    }
}

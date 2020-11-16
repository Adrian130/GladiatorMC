package xyz.velium.uhc.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.tean.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UHCPlayer {

    private UHC uhc = UHC.getInstance();

    private UUID uuid;
    private UUID combatloggerUUID;

    private boolean hasplayed = false;

    private int kills = 0;

    private List<String> killedPlayers = new ArrayList<>();

    private int minedSpaners = 0;
    private int minedDiamonds = 0;
    private int minedGold = 0;

    private int practiceKills = 0;

    private int logoutTime = 600;

    private boolean staffNotifications = true;

    private int levels;
    private Location logoutLocation;
    private ItemStack[] playerArmor;
    private ItemStack[] playerInventory;

    private Team team = null;

    private PlayerStates playerStates;

    public UHCPlayer(UUID uuid) {
        this.uuid = uuid;
        this.playerStates = PlayerStates.LOBBY;
    }

    public void setupPlayer() {
        Player player = Bukkit.getPlayer(this.uuid);

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setFireTicks(0);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setSprinting(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().setArmorContents(null);
        player.getInventory().clear();

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public void spawnCombatVillager() {
        Player player = Bukkit.getPlayer(this.uuid);
        Villager villager = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);

        this.setCombatloggerUUID(villager.getUniqueId());

        villager.setCustomName(player.getName());
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 127));
        villager.setRemoveWhenFarAway(false);
    }

    public void removeCombatLogger() {
        Player player = Bukkit.getPlayer(this.uuid);

        if (this.playerStates == PlayerStates.INGAME) {
            for (Entity entity : Bukkit.getWorld(uhc.getWorldManager().getUhc_world()).getEntities()) {
                if (entity.getType() == EntityType.VILLAGER) {
                    Villager villager = (Villager) entity;
                    if (this.combatloggerUUID == villager.getUniqueId()) {
                        villager.remove();
                    }
                    if(villager.getCustomName() != null) {
                        if(villager.getCustomName().equalsIgnoreCase(player.getName())) {
                            villager.remove();
                        }
                    }
                }
            }
            for (Entity entity : Bukkit.getWorld(uhc.getWorldManager().getUhc_nether()).getEntities()) {
                if (entity.getType() == EntityType.VILLAGER) {
                    Villager villager = (Villager) entity;
                    if (this.combatloggerUUID == villager.getUniqueId()) {
                        villager.remove();
                    }
                    if(villager.getCustomName() != null) {
                        if(villager.getCustomName().equalsIgnoreCase(player.getName())) {
                            villager.remove();
                        }
                    }
                }
            }
        }
    }

    public void respawnPlayer() {
        Player player = Bukkit.getPlayer(this.uuid);

        for (Player onlinePlayers : uhc.getOnlinePlayers()) {
            onlinePlayers.showPlayer(player);
        }

        if (player.isInsideVehicle()) {
            player.getVehicle().remove();
        }

        this.setPlayerStates(PlayerStates.INGAME);
        this.setupPlayer();

        player.teleport(this.getLogoutLocation());
        player.getInventory().setContents(this.getPlayerInventory());
        player.getInventory().setArmorContents(this.getPlayerArmor());
        player.setLevel(this.getLevels());
        player.updateInventory();

        uhc.getPlayerManager().getAlivePlayers().add(this.uuid);

        if (uhc.getTeamManager().isTeams()) {
            if (this.getTeam() == null) {
                uhc.getTeamManager().createTeam(Bukkit.getPlayer(this.getUuid()));
                this.team.getAlivePlayers().add(player.getUniqueId());
            } else {
                this.team.getAlivePlayers().add(this.uuid);

                if(!uhc.getTeamManager().getAliveTeams().contains(this.team)) {
                    uhc.getTeamManager().getAliveTeams().add(team);
                }
            }
        }
    }

    public void teleportSpawn() {
        Player player = Bukkit.getPlayer(this.uuid);
        try {
            player.teleport(uhc.getLocationManager().getLocation("spawn"));
        } catch (IllegalArgumentException illegalArgumentException) {
        }
    }

    public void addSpectator() {
        Player player = Bukkit.getPlayer(this.uuid);

        this.setupPlayer();
        this.setPlayerStates(PlayerStates.SPECTATOR);

        Location location = new Location(Bukkit.getWorld(uhc.getWorldManager().getUhc_world()), 0, Bukkit.getWorld(uhc.getWorldManager().getUhc_world()).getHighestBlockYAt(0, 0) + 5, 0);
        player.teleport(location);
        player.sendMessage(uhc.getPREFIX() + ChatColor.YELLOW + "You are now in Spectator mode");

        uhc.getInventories().loadSpectatorHotBar(player);

        for (Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
            onlinePlayers.hidePlayer(player);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.CREATIVE);
            }
        }.runTaskLater(uhc, 5);
    }

    public void savePlayer() {
        Player player = Bukkit.getPlayer(this.uuid);

        this.setLevels(player.getLevel());
        this.setPlayerArmor(player.getInventory().getArmorContents());
        this.setPlayerInventory(player.getInventory().getContents());
        this.setLogoutLocation(player.getLocation());
    }

    public void handleDeath() {
        Player player = Bukkit.getPlayer(this.uuid);

        this.savePlayer();

        uhc.getPlayerManager().getAlivePlayers().remove(this.uuid);
        uhc.getPlayerManager().getLogoutPlayers().remove(this.uuid);

        if (uhc.getTeamManager().isTeams()) {
            if (this.getTeam() != null) {
                Team team = this.getTeam();

                team.getAlivePlayers().remove(player.getUniqueId());

                if (team.getAlivePlayers().isEmpty()) {
                    uhc.getTeamManager().getAliveTeams().remove(team);
                }
            }
        }

        uhc.getGameManager().checkWin();
    }

    public void hideSpectators() {
        Player player = Bukkit.getPlayer(this.uuid);

        for (Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
            UHCPlayer uhcPlayers = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
            if (uhcPlayers.getPlayerStates() == PlayerStates.SPECTATOR || uhcPlayers.getPlayerStates() == PlayerStates.MODERATOR || uhcPlayers.getPlayerStates() == PlayerStates.HOST) {
                player.hidePlayer(onlinePlayers);
            }
        }
    }

    public void dropPlayerDeathInventory() {
        ItemStack[] inventoryStack = this.playerInventory;
        ItemStack[] armorStack = this.playerArmor;

        for (ItemStack itemStack : inventoryStack) {
            if (itemStack != null) {
                this.logoutLocation.getWorld().dropItemNaturally(this.logoutLocation, itemStack);
            }
        }

        for (ItemStack itemStack : armorStack) {
            if (itemStack != null) {
                if (itemStack.getType() != Material.AIR) {
                    this.logoutLocation.getWorld().dropItemNaturally(this.logoutLocation, itemStack);
                }
            }
        }
    }

    //Handle Practice
    public void handlePractice() {
        Player player = Bukkit.getPlayer(this.uuid);

        if (uhc.getPlayerManager().getPracticePlayers().contains(player.getUniqueId())) {
            //Leave Practice
            this.setupPlayer();
            this.teleportSpawn();
            this.setPlayerStates(PlayerStates.LOBBY);

            uhc.getPlayerManager().getPracticePlayers().remove(player.getUniqueId());
            uhc.getInventories().loadLobbyHotBar(player);

            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You successfully left Practice");
            return;
        } else {
            //Join Practice
            this.setupPlayer();
            this.setPlayerStates(PlayerStates.PRACTICE);

            uhc.getPlayerManager().getPracticePlayers().add(player.getUniqueId());
            uhc.getInventories().loadPracticeHotBar(player);

            player.teleport(uhc.getPlayerManager().getRandomLocation(uhc.getWorldManager().getUhc_practice(), uhc.getGameManager().getPracticeBorderSize()));
            player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You successfully joined Practice");
        }
    }

    public void lateScatter() {
        Player player = Bukkit.getPlayer(this.uuid);

        for (Player onlinePlayers : uhc.getOnlinePlayers()) {
            onlinePlayers.showPlayer(player);
        }

        if (player.isInsideVehicle()) {
            player.getVehicle().remove();
        }

        this.setHasplayed(true);
        this.setupPlayer();
        this.setPlayerStates(PlayerStates.INGAME);

        uhc.getInventories().startGameHotBar(player);
        uhc.getPlayerManager().getAlivePlayers().add(player.getUniqueId());

        if (uhc.getTeamManager().isTeams()) {
            if (this.getTeam() == null) {
                uhc.getTeamManager().createTeam(Bukkit.getPlayer(this.getUuid()));
                this.team.getAlivePlayers().add(player.getUniqueId());
            }
        }

        player.teleport(uhc.getPlayerManager().getRandomLocation(uhc.getWorldManager().getUhc_world(), uhc.getGameManager().getBorderSize() -5));
        player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You are now able to play the UHC");

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setHealth(20);
                player.setFoodLevel(20);
                player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You health and hunger has been restored");
            }
        }.runTaskLater(uhc, 20 * 2);
    }
}

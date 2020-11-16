package xyz.velium.uhc.manager;

import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.util.JavaUtils;

import java.io.File;

public class WorldManager {

    private UHC uhc;

    private String lobby_world = "world";
    private String uhc_world = "uhc_world";
    private String uhc_nether = "uhc_nether";
    private String uhc_practice = "uhc_practice";

    public WorldManager(UHC uhc) {
        this.uhc = uhc;
    }

    public void createUHCWorld() {
        World world = Bukkit.createWorld((new WorldCreator(this.uhc_world)).environment(World.Environment.NORMAL).type(WorldType.NORMAL).generator("TerrainControl"));
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRuleValue("naturalRegeneration", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setTime(1300);
        world.setThundering(false);
        world.setStorm(false);
    }

    public void createNetherWorld() {
        World netherWorld = Bukkit.createWorld((new WorldCreator(this.uhc_nether)).environment(World.Environment.NETHER).type(WorldType.NORMAL));
        netherWorld.setDifficulty(Difficulty.NORMAL);
        netherWorld.setGameRuleValue("naturalRegeneration", "false");
    }

    public void createPracticeWorld() {
        World practiceWorld = Bukkit.createWorld((new WorldCreator(this.uhc_practice)).environment(World.Environment.NORMAL).type(WorldType.FLAT));
        practiceWorld.setDifficulty(Difficulty.PEACEFUL);
        practiceWorld.setGameRuleValue("naturalRegeneration", "false");
        practiceWorld.setGameRuleValue("doDaylightCycle", "false");
        practiceWorld.setTime(1300);
        practiceWorld.setAnimalSpawnLimit(0);
        practiceWorld.setMonsterSpawnLimit(0);
        practiceWorld.setThundering(false);
        practiceWorld.setStorm(false);
    }

    public void deleteWorld(String worldname) {
        Bukkit.getServer().unloadWorld(worldname, false);
        File world = new File(Bukkit.getWorldContainer(), worldname);
        JavaUtils.deleteDirectory(world);
    }

    public void loadWorld(final String world, final int radius, final int speed) {
        (new BukkitRunnable() {
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb shape square");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + world + " set " + radius + " " + radius + " 0 0");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + world + " fill " + speed);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + world + " fill confirm");
            }
        }).runTaskLater(uhc, 300L);
    }

    public void setWorldBorder(final String world, final int radius) {
        (new BukkitRunnable() {
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb shape square");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + world + " set " + radius + " " + radius + " 0 0");
            }
        }).runTaskLater(uhc, 300L);
    }

    public String getUhc_world() {
        return uhc_world;
    }

    public void setUhc_world(String uhc_world) {
        this.uhc_world = uhc_world;
    }

    public String getUhc_nether() {
        return uhc_nether;
    }

    public void setUhc_nether(String uhc_nether) {
        this.uhc_nether = uhc_nether;
    }

    public String getLobby_world() {
        return lobby_world;
    }

    public void setLobby_world(String lobby_world) {
        this.lobby_world = lobby_world;
    }

    public String getUhc_practice() {
        return uhc_practice;
    }

    public void setUhc_practice(String uhc_practice) {
        this.uhc_practice = uhc_practice;
    }
}

package xyz.velium.uhc.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class BorderManager {

    private UHC uhc;

    public BorderManager(UHC uhc) {
        this.uhc = uhc;
    }

    private Random random = new Random();

    public void createBorderlayer(String gameWorld, int radius) {
        World world = Bukkit.getWorld(gameWorld);

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb shape square");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + gameWorld + " set " + radius + " " + radius + " 0 0");

        if(gameWorld.equalsIgnoreCase(uhc.getWorldManager().getUhc_practice())) {
            uhc.getGameManager().setPracticeBorderSize(radius);
        } else if(gameWorld.equalsIgnoreCase(uhc.getWorldManager().getUhc_world())) {
            uhc.getGameManager().setBorderSize(radius);
        }

        int posX = radius;
        int negX = 0 - radius;

        int posZ = radius;
        int negZ = 0 - radius;

        final Queue<Location> locations1 = new ArrayDeque<Location>();
        final Queue<Location> locations2 = new ArrayDeque<Location>();

        final Queue<Location> locations3 = new ArrayDeque<Location>();
        final Queue<Location> locations4 = new ArrayDeque<Location>();

        final Queue<Location> locations5 = new ArrayDeque<Location>();
        final Queue<Location> locations6 = new ArrayDeque<Location>();

        final Queue<Location> locations7 = new ArrayDeque<Location>();
        final Queue<Location> locations8 = new ArrayDeque<Location>();

        for (int t = posX; t >= 0; t--) {
            int min = world.getHighestBlockYAt(t, posZ);
            int max = min + 4;
            if (max < 256) {
                for (int y = min; y < max; y++) {
                    locations1.add(new Location(world, t, y, posZ));
                }
            }
        }

        for (int t = negX; t <= 0; t++) {
            int min = world.getHighestBlockYAt(t, posZ);
            int max = min + 4;
            if (max < 256) {
                for (int y = min; y < max; y++) {
                    locations2.add(new Location(world, t, y, posZ));
                }
            }
        }

        for (int t = posX; t >= 0; t--) {
            int min = world.getHighestBlockYAt(t, negZ);
            int max = min + 4;
            if (max < 256) {
                for (int y = min; y < max; y++) {
                    locations3.add(new Location(world, t, y, negZ));
                }
            }
        }

        for (int t = negX; t <= 0; t++) {
            int min = world.getHighestBlockYAt(t, negZ);
            int max = min + 4;
            if (max < 256) {
                for (int y = min; y < max; y++) {
                    locations4.add(new Location(world, t, y, negZ));
                }
            }
        }

        for (int t = posZ; t >= 0; t--) {
            int min = world.getHighestBlockYAt(posX, t);
            int max = min + 4;
            if (max < 256) {
                for (int y = min; y < max; y++) {
                    locations5.add(new Location(world, posX, y, t));
                }
            }
        }

        for (int t = negZ; t <= 0; t++) {
            int min = world.getHighestBlockYAt(posX, t);
            int max = min + 4;
            if (max < 256) {
                for (int y = min; y < max; y++) {
                    locations6.add(new Location(world, posX, y, t));
                }
            }
        }

        for (int t = posZ; t >= 0; t--) {
            int min = world.getHighestBlockYAt(negX, t);
            int max = min + 4;
            if (max < 256) {
                for (int y = min; y < max; y++) {
                    locations7.add(new Location(world, negX, y, t));
                }
            }
        }

        for (int t = negZ; t <= 0; t++) {
            int min = world.getHighestBlockYAt(negX, t);
            int max = min + 4;
            if (max < 256) {
                for (int y = min; y < max; y++) {
                    locations8.add(new Location(world, negX, y, t));
                }
            }
        }

        (new BukkitRunnable() {
            public void run() {
                for (int x = 0; x < 6; x++) {
                    if (!locations1.isEmpty()) {
                        ((Location) locations1.poll()).getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations2.isEmpty()) {
                        ((Location) locations2.poll()).getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations3.isEmpty()) {
                        ((Location) locations3.poll()).getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations4.isEmpty()) {
                        ((Location) locations4.poll()).getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations5.isEmpty()) {
                        ((Location) locations5.poll()).getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations6.isEmpty()) {
                        ((Location) locations6.poll()).getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations7.isEmpty()) {
                        ((Location) locations7.poll()).getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations8.isEmpty()) {
                        ((Location) locations8.poll()).getBlock().setType(Material.BEDROCK);
                    } else {
                        cancel();
                    }
                }
            }
        }).runTaskTimer(uhc, 0L, 1L);
    }
}

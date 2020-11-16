package xyz.velium.uhc.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.player.UHCPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@Getter @Setter
public class PlayerManager {

    private UHC uhc;
    private Random random;

    public PlayerManager(UHC uhc) {
        this.uhc = uhc;
        this.random = new Random();
    }

    private ArrayList<UUID> alivePlayers = new ArrayList<>();
    private HashMap<UUID, UHCPlayer> uhcPlayers = new HashMap<>();
    private ArrayList<UUID> practicePlayers = new ArrayList<>();
    private ArrayList<UUID> logoutPlayers = new ArrayList<>();

    public Location getRandomLocation(String world, int borderSize) {
        World locationWorld = Bukkit.getWorld(world);

        int z = this.random.nextInt(borderSize + 1 - borderSize * -1) + borderSize * -1;
        int x = this.random.nextInt(borderSize + 1 - borderSize * -1) + borderSize * -1;

        Location location = new Location(locationWorld, x, locationWorld.getHighestBlockYAt(x, z) + 1, z);

        if (location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.WATER && location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.STATIONARY_WATER && location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.LAVA && location
                .getBlock().getRelative(BlockFace.DOWN).getType() != Material.STATIONARY_LAVA && location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.CACTUS) {
            return location.add(0.0D, 1.0D, 0.0D);
        }
        return getRandomLocation(world, borderSize);
    }
}

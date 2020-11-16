package xyz.velium.uhc.tean;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import xyz.velium.uhc.UHC;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class Team {

    private UHC uhc = UHC.getInstance();

    private UUID leader;
    private int teamNumber;
    private int teamKills = 0;

    private ArrayList<UUID> alivePlayers = new ArrayList<>();
    private ArrayList<UUID> players = new ArrayList<>();
    private ArrayList<UUID> invitedPlayers = new ArrayList<>();

    private Inventory backPack = Bukkit.createInventory(null, 27, ChatColor.YELLOW + "Backpack");

    public Team(int teamID, UUID uuid) {
        this.teamNumber = teamID;
        this.leader = uuid;
        this.players.add(uuid);
    }
}

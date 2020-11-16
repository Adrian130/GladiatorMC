package xyz.velium.uhc.manager.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import xyz.velium.uhc.UHC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SimpleBoardManager extends BukkitRunnable implements Listener {

    private UHC uhc;

    private Map<UUID, SimpleBoard> boards;
    private BoardProvider boardProvider;

    public SimpleBoardManager(UHC uhc, BoardProvider boardProvider) {
        this.uhc = uhc;
        this.boards = new HashMap<>();
        this.boardProvider = boardProvider;

        this.runTaskTimerAsynchronously(uhc, 10L, 10L);
    }

    public BoardProvider getBoardProvider() {
        return boardProvider;
    }

    public void setBoardProvider(BoardProvider boardProvider) {
        this.boardProvider = boardProvider;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SimpleBoard simpleBoard = this.boards.get(player.getUniqueId());

        if (simpleBoard == null) {
            simpleBoard = new SimpleBoard(event.getPlayer());
            this.boards.put(player.getUniqueId(), simpleBoard);
        }

        simpleBoard.updateTitle(this.boardProvider.getTitle(player));
        simpleBoard.show();
    }

    @EventHandler
    public void onPlayerQuiteEvent(PlayerQuitEvent event) {
        this.boards.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getScoreboard().getObjectives().stream().noneMatch(objective -> objective.getName().equals("list"))) {
                Objective healthList = player.getScoreboard().registerNewObjective("list", "health");
                healthList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            }

            if (player.getScoreboard().getObjectives().stream().noneMatch(objective -> objective.getName().equals("name"))) {
                Objective healthName = player.getScoreboard().registerNewObjective("name", "health");
                healthName.setDisplaySlot(DisplaySlot.BELOW_NAME);
                healthName.setDisplayName(ChatColor.DARK_RED + "\u2764");
            }

            SimpleBoard simpleBoard = boards.get(player.getUniqueId());

            if (simpleBoard == null) return;

            List<String> list = this.boardProvider.getBoardLines(player);
            simpleBoard.update(list);
            simpleBoard.updateTitle(this.boardProvider.getTitle(player));
        }
    }
}

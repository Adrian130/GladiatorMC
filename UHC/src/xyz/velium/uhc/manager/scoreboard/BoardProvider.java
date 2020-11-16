package xyz.velium.uhc.manager.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

public interface BoardProvider {

    String getTitle(Player player);

    List<String> getBoardLines(Player player);
}

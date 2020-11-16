package xyz.velium.uhc.manager.scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;
import xyz.velium.uhc.enums.Scenarios;
import xyz.velium.uhc.player.UHCPlayer;

import java.util.ArrayList;
import java.util.List;

public class UHCBoardProvider implements BoardProvider {

    private UHC uhc;

    public UHCBoardProvider(UHC uhc) {
        this.uhc = uhc;
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.GOLD + ChatColor.BOLD.toString() + "UHC";
    }

    private String getFormatTime() {
        int hours = uhc.getGameTask().getGameTime() / 3600;
        int secondsLeft = uhc.getGameTask().getGameTime() - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours >= 1) {
            if (hours < 10)
                formattedTime += "0";
            formattedTime += hours + ":";
        }

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds;

        return formattedTime;
    }

    private String gameType() {
        if (uhc.getTeamManager().isTeams()) {
            return "To" + uhc.getTeamManager().getTeamSize();
        } else {
            return "FFA";
        }
    }

    public String getBorderInfo() {

        int timer = uhc.getGameTask().getGameTime();
        int borderNumber = uhc.getGameManager().getBorderNumber();

        if (uhc.getGameManager().getBorderSize() == 50 || uhc.getGameManager().getBorderSize() == 25) {
            return "";
        } else if (timer <= uhc.getGameTask().getBorderTimeInSeconds(borderNumber) - (4 * 60) && timer >= uhc.getGameTask().getBorderTimeInSeconds(borderNumber) - (5 * 60)) {
            return ChatColor.GRAY + "(" + ChatColor.RED + "5m" + ChatColor.GRAY + ")";
        } else if (timer <= uhc.getGameTask().getBorderTimeInSeconds(borderNumber) - (3 * 60) && timer >= uhc.getGameTask().getBorderTimeInSeconds(borderNumber) - (4 * 60)) {
            return ChatColor.GRAY + "(" + ChatColor.RED + "4m" + ChatColor.GRAY + ")";
        } else if (timer <= uhc.getGameTask().getBorderTimeInSeconds(borderNumber) - (2 * 60) && timer >= uhc.getGameTask().getBorderTimeInSeconds(borderNumber) - (3 * 60)) {
            return ChatColor.GRAY + "(" + ChatColor.RED + "3m" + ChatColor.GRAY + ")";
        } else if (timer <= uhc.getGameTask().getBorderTimeInSeconds(borderNumber) - 60 && timer >= uhc.getGameTask().getBorderTimeInSeconds(borderNumber) - (2 * 60)) {
            return ChatColor.GRAY + "(" + ChatColor.RED + "2m" + ChatColor.GRAY + ")";
        } else if (timer <= uhc.getGameTask().getBorderTimeInSeconds(borderNumber) - 1 && timer >= uhc.getGameTask().getBorderTimeInSeconds(borderNumber) - 60) {
            int number = uhc.getGameTask().getBorderTimeInSeconds(borderNumber) - timer;
            return ChatColor.GRAY + "(" + ChatColor.RED + number + "s" + ChatColor.GRAY + ")";
        }
        return "";
    }

    @Override
    public List<String> getBoardLines(Player player) {
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        List<String> lines = new ArrayList<>();

        if(uhc.getGameStates() == GameStates.PREPARE) {
            lines.add(" ");
            lines.add(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "Server");
            lines.add(ChatColor.RED + ChatColor.ITALIC.toString() + "Preapring");
            lines.add(" ");
            lines.add(ChatColor.GRAY + "eu.velium.xyz");
        } else if(uhc.getGameStates() == GameStates.LOBBY) {
            lines.add(" ");
            lines.add(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "Game");
            lines.add(ChatColor.YELLOW + "Players: " + ChatColor.WHITE + uhc.getOnlinePlayers().size());
            lines.add(ChatColor.YELLOW + "Game Type: " + ChatColor.WHITE + this.gameType());
            lines.add(" ");
            lines.add(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "Scenarios");
            for(Scenarios scenarios : Scenarios.values()) {
                if(scenarios.isEnabled()) {
                    lines.add(ChatColor.WHITE + "- " + scenarios.getScenarioName());
                }
            }
            lines.add(" ");
            lines.add(ChatColor.GRAY + "eu.velium.xyz");
        } else if(uhc.getGameStates() == GameStates.SCATTER) {
            lines.add(" ");
            lines.add(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "Time");
            lines.add(ChatColor.YELLOW + "Starting: " + ChatColor.WHITE + uhc.getStartTask().getStartingTime());
            lines.add(" ");
            lines.add(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "Scatter");
            lines.add(ChatColor.YELLOW + "Scattering: " + ChatColor.WHITE + uhc.getStartTask().getPlayersToScatter().size());
            lines.add(ChatColor.YELLOW + "Scatterd: " + ChatColor.WHITE + (uhc.getOnlinePlayers().size() - uhc.getStartTask().getPlayersToScatter().size()));
            lines.add(" ");
            lines.add(ChatColor.GRAY + "eu.velium.xyz");
        } else if(uhc.getGameStates() == GameStates.INGAME) {
            lines.add(" ");
            lines.add(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "Game");
            lines.add(ChatColor.YELLOW + "Game Time: " + ChatColor.WHITE + this.getFormatTime());
            lines.add(ChatColor.YELLOW + "Players: " + ChatColor.WHITE + uhc.getPlayerManager().getAlivePlayers().size());
            if(uhc.getTeamManager().isTeams()) {
                lines.add(ChatColor.YELLOW + "Teams: " + ChatColor.WHITE + uhc.getTeamManager().getAliveTeams().size());
            }
            lines.add(ChatColor.YELLOW + "Border: " + ChatColor.WHITE + uhc.getGameManager().getBorderSize() + " " + this.getBorderInfo());
            lines.add(" ");
            lines.add(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "Stats");
            lines.add(ChatColor.YELLOW + "Kills: " + ChatColor.WHITE + uhcPlayer.getKills());
            if(uhc.getTeamManager().isTeams()) {
                if(uhcPlayer.getTeam() != null) {
                    lines.add(ChatColor.YELLOW + "Team Kills: " + ChatColor.WHITE + uhcPlayer.getTeam().getTeamKills());
                } else {
                    lines.add(ChatColor.YELLOW + "Team Kills: " + ChatColor.WHITE + "0");
                }
            }
            if(uhc.getScenarioManager().getNoCleanTime().containsKey(player.getUniqueId())) {
                lines.add(" ");
                lines.add(ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + "Scenarios");
                lines.add(ChatColor.YELLOW + "NoClean: " + ChatColor.WHITE + uhc.getScenarioManager().getNoCleanTime().get(player.getUniqueId()));
            }
            lines.add(" ");
            lines.add(ChatColor.GRAY + "eu.velium.xyz");
        }



        return lines;
    }
}

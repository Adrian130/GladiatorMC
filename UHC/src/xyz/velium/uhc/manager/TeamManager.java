package xyz.velium.uhc.manager;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.player.UHCPlayer;
import xyz.velium.uhc.tean.Team;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class TeamManager {

    private UHC uhc;

    public TeamManager(UHC uhc) {
        this.uhc = uhc;
    }

    private int teamNumbers = 1;
    private int teamSize = 2;
    private boolean teams = false;

    private ArrayList<Team> aliveTeams = new ArrayList<>();

    public void createTeam(Player player) {
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        if (uhcPlayer.getTeam() == null) {
            Team team = new Team(this.teamNumbers, player.getUniqueId());
            uhcPlayer.setTeam(team);
            player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You Successfully created the Team " + ChatColor.GRAY + "#" + this.teamNumbers);
            this.teamNumbers++;

            this.aliveTeams.add(team);
        } else {
            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You are already in a Team");
        }
    }

    public void sendTeamInvite(Player invitedPlayers, Player teamLeader) {
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(invitedPlayers.getUniqueId());
        UHCPlayer uhcLeader = uhc.getPlayerManager().getUhcPlayers().get(teamLeader.getUniqueId());

        if (uhcPlayer.getTeam() == null) {
            if (uhcLeader.getTeam() != null) {
                Team team = uhcLeader.getTeam();
                if (teamLeader.getUniqueId().equals(team.getLeader())) {
                    TextComponent acceptComponent = new TextComponent();
                    acceptComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team accept " + teamLeader.getName()));
                    acceptComponent.setText(ChatColor.GREEN + ChatColor.BOLD.toString() + "[Accept]");

                    TextComponent deniedComponent = new TextComponent();
                    deniedComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team deny " + teamLeader.getName()));
                    deniedComponent.setText(ChatColor.RED + ChatColor.BOLD.toString() + "[Deny]");

                    invitedPlayers.sendMessage(uhc.getPREFIX() + ChatColor.YELLOW + teamLeader.getName() + ChatColor.GREEN + " invited you to the Team");
                    invitedPlayers.spigot().sendMessage(acceptComponent);
                    invitedPlayers.spigot().sendMessage(deniedComponent);

                    teamLeader.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You successfully invited " + ChatColor.YELLOW + invitedPlayers.getName() + ChatColor.GREEN + " to the Team");

                    team.getInvitedPlayers().add(invitedPlayers.getUniqueId());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            team.getInvitedPlayers().remove(invitedPlayers.getUniqueId());

                            if (!team.getPlayers().contains(invitedPlayers.getUniqueId())) {
                                teamLeader.sendMessage(uhc.getPREFIX() + ChatColor.RED + invitedPlayers.getName() + " Team invite has expired");
                                invitedPlayers.sendMessage(uhc.getPREFIX() + ChatColor.RED + teamLeader.getName() + " Team invitation has expired");
                            }
                        }
                    }.runTaskLater(uhc, 20 * 20);
                } else {
                    teamLeader.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Only the Team Leader can invite players to the Team");
                }
            } else {
                teamLeader.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You are not in a Team");
            }
        } else {
            teamLeader.sendMessage(uhc.getPREFIX() + ChatColor.RED + invitedPlayers.getName() + " is already in a Team");
        }
    }

    public void joinTeam(Player player, Player target) {
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        UHCPlayer targetPlayer = uhc.getPlayerManager().getUhcPlayers().get(target.getUniqueId());

        if (uhcPlayer.getTeam() == null) {
            Team team = targetPlayer.getTeam();
            if (team.getInvitedPlayers().contains(player.getUniqueId())) {
                if (team.getPlayers().size() <= this.teamSize) {
                    for (UUID teamMembers : team.getPlayers()) {
                        Player teamPlayers = Bukkit.getPlayer(teamMembers);
                        teamPlayers.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + player.getName() + " has joined the Team");
                    }

                    team.getPlayers().add(player.getUniqueId());
                    team.getInvitedPlayers().remove(player.getUniqueId());
                    uhcPlayer.setTeam(team);

                    player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You successfully joined the Team");
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + target.getName() + "´s Team is already full");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You have no pending Team invite");
            }
        } else {
            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You are already in a Team");
        }
    }

    public void denyTeamRequest(Player player, Player target) {
        UHCPlayer targetPlayer = uhc.getPlayerManager().getUhcPlayers().get(target.getUniqueId());
        Team team = targetPlayer.getTeam();

        if (team.getInvitedPlayers().contains(player.getUniqueId())) {
            for (UUID teamMembers : team.getPlayers()) {
                Player teamPlayers = Bukkit.getPlayer(teamMembers);
                teamPlayers.sendMessage(uhc.getPREFIX() + ChatColor.RED + player.getName() + " has denied the Team request");
            }

            team.getInvitedPlayers().remove(player.getUniqueId());
            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You successfully denied the Team request");
        } else {
            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You have no pending Team invite");
        }
    }


    public void kickPlayerFromTeam(Player kickedPlayer, Player teamLeader) {
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(teamLeader.getUniqueId());
        UHCPlayer kickedUHCPlayer = uhc.getPlayerManager().getUhcPlayers().get(kickedPlayer.getUniqueId());

        if (uhcPlayer.getTeam() != null) {
            Team team = uhcPlayer.getTeam();
            if (teamLeader.getUniqueId().equals(team.getLeader())) {
                if (team.getPlayers().contains(kickedPlayer.getUniqueId())) {
                    team.getAlivePlayers().remove(kickedPlayer.getUniqueId());
                    team.getPlayers().remove(kickedPlayer.getUniqueId());
                    kickedUHCPlayer.setTeam(null);
                    for (UUID teamMembers : team.getPlayers()) {
                        Player teamPlayers = Bukkit.getPlayer(teamMembers);
                        teamPlayers.sendMessage(uhc.getPREFIX() + ChatColor.RED + kickedPlayer.getName() + " has been kicked from the Team");
                    }
                    kickedPlayer.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You have been kicked from your Team");
                    teamLeader.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "You successfully kicked " + kickedPlayer.getName() + " from the Team");
                } else {
                    teamLeader.sendMessage(uhc.getPREFIX() + ChatColor.RED + "This Players is not in your Team");
                }
            } else {
                teamLeader.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Only the Team Leader can kick players from the Team");
            }
        } else {
            teamLeader.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You are not in a Team");
        }
    }

    public void leaveTeam(Player player) {
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());

        if (uhcPlayer.getTeam() != null) {
            Team team = uhcPlayer.getTeam();
            if (!player.getUniqueId().equals(team.getLeader())) {
                team.getAlivePlayers().remove(player.getUniqueId());
                team.getPlayers().remove(player.getUniqueId());
                uhcPlayer.setTeam(null);
                for (UUID teamMembers : team.getPlayers()) {
                    Player teamPlayers = Bukkit.getPlayer(teamMembers);
                    teamPlayers.sendMessage(uhc.getPREFIX() + ChatColor.RED + player.getName() + " has left the Team");
                }
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You successfully left the Team");
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You can´t leave the Team");
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Use /team delete to delete your Team");
            }
        } else {
            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You are not in a Team");
        }
    }

    public void deleteTeam(Player player) {
        UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(player.getUniqueId());
        if (uhcPlayer.getTeam() != null) {
            Team team = uhcPlayer.getTeam();
            if (player.getUniqueId().equals(team.getLeader())) {
                try {
                    for (UUID teamMembers : team.getPlayers()) {
                        UHCPlayer uhcPlayers = uhc.getPlayerManager().getUhcPlayers().get(teamMembers);

                        Player teamPlayers = Bukkit.getPlayer(teamMembers);
                        teamPlayers.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Your Team has ben deleted");

                        uhcPlayers.setTeam(null);

                        team.getAlivePlayers().clear();
                        team.getPlayers().clear();
                        this.aliveTeams.remove(team);
                    }
                } catch (ConcurrentModificationException concurrentModificationException) {
                    System.out.println("Team Deleted");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Only the Team Leader can delete the Team");
            }
        } else {
            player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You are not in a Team");
        }
    }
}

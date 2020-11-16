package xyz.velium.uhc.commands.team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.GameStates;

public class TeamCommand implements CommandExecutor {

    private UHC uhc;

    public TeamCommand(UHC uhc) {
        this.uhc = uhc;
    }

    private void sendTeamInformation(Player player) {
        player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Team Commands");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.YELLOW + "/team create " + ChatColor.GRAY + " - create a Team");
        player.sendMessage(ChatColor.YELLOW + "/team invite <player> " + ChatColor.GRAY + " - invite a player to your Team");
        player.sendMessage(ChatColor.YELLOW + "/team leave " + ChatColor.GRAY + " - leave your current Team");
        player.sendMessage(ChatColor.YELLOW + "/team delete " + ChatColor.GRAY + " - delete your current Team");
        player.sendMessage(ChatColor.YELLOW + "/team deny <player> " + ChatColor.GRAY + " - deny a Team request");
        player.sendMessage(ChatColor.YELLOW + "/team accept <player> " + ChatColor.GRAY + " - accept a Team request");
        player.sendMessage(ChatColor.YELLOW + "/team kick <player> " + ChatColor.GRAY + " - remove a player from your Team");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if(commandSender instanceof Player) {
            Player player  = (Player) commandSender;
            if(uhc.getTeamManager().isTeams()) {
                if(uhc.getGameStates() == GameStates.LOBBY) {
                    if(arguments.length == 1) {
                        if(arguments[0].equalsIgnoreCase("create")) {
                            uhc.getTeamManager().createTeam(player);
                        } else if(arguments[0].equalsIgnoreCase("leave")) {
                            uhc.getTeamManager().leaveTeam(player);
                        } else if(arguments[0].equalsIgnoreCase("delete")) {
                            uhc.getTeamManager().deleteTeam(player);
                        } else {
                            this.sendTeamInformation(player);
                        }
                    } else if(arguments.length == 2) {
                        if(arguments[0].equalsIgnoreCase("invite")) {
                            Player target = Bukkit.getPlayer(arguments[1]);
                            if(target != null) {
                                uhc.getTeamManager().sendTeamInvite(target, player);
                            } else {
                                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[1] + " could not be found");
                            }
                        } else if(arguments[0].equalsIgnoreCase("kick")) {
                            Player target = Bukkit.getPlayer(arguments[1]);
                            if(target != null) {
                                uhc.getTeamManager().kickPlayerFromTeam(target, player);
                            } else {
                                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[1] + " could not be found");
                            }
                        } else if(arguments[0].equalsIgnoreCase("accept")) {
                            Player target = Bukkit.getPlayer(arguments[1]);
                            if(target != null) {
                                uhc.getTeamManager().joinTeam(player, target);
                            } else {
                                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[1] + " could not be found");
                            }
                        } else if(arguments[0].equalsIgnoreCase("deny")) {
                            Player target = Bukkit.getPlayer(arguments[1]);
                            if(target != null) {
                                uhc.getTeamManager().denyTeamRequest(player, target);
                            } else {
                                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + arguments[1] + " could not be found");
                            }
                        } else {
                            this.sendTeamInformation(player);
                        }
                    } else {
                        this.sendTeamInformation(player);
                    }
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You can´t edit Teams now");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Teams are currently disabled");
            }
        }
        return false;
    }
}

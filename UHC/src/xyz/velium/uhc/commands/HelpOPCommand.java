package xyz.velium.uhc.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.velium.uhc.UHC;
import xyz.velium.uhc.enums.PlayerStates;
import xyz.velium.uhc.player.UHCPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class HelpOPCommand implements CommandExecutor {

    private UHC uhc;
    private ArrayList<UUID> helpOPCooldown;

    public HelpOPCommand(UHC uhc) {
        this.uhc = uhc;
        this.helpOPCooldown = new ArrayList<>();
    }

    private void sendHelpOP(Player player, String message) {
        for(Player onlinePlayers : uhc.getOnlinePlayers()) {
            UHCPlayer uhcPlayer = uhc.getPlayerManager().getUhcPlayers().get(onlinePlayers.getUniqueId());
            if(uhcPlayer.getPlayerStates() == PlayerStates.HOST || uhcPlayer.getPlayerStates() == PlayerStates.MODERATOR) {
                TextComponent helpOP = new TextComponent(uhc.getPREFIX() + ChatColor.DARK_GREEN + "[HelpOP]: " + ChatColor.GREEN + player.getName() + ": " + message);
                helpOP.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + player.getName()));
                onlinePlayers.spigot().sendMessage(helpOP);
            }
        }
        player.sendMessage(uhc.getPREFIX() + ChatColor.GREEN + "Your message has been send to the staff team");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String invoke, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (arguments.length >= 1) {
                String message = String.join(" ", arguments);
                if (!this.helpOPCooldown.contains(player.getUniqueId())) {
                    this.sendHelpOP(player, message);
                    this.helpOPCooldown.add(player.getUniqueId());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            helpOPCooldown.remove(player.getUniqueId());
                            this.cancel();
                        }
                    }.runTaskLater(uhc, 30 * 20);
                } else {
                    player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "You can use HelpOP only every 30 seconds");
                }
            } else {
                player.sendMessage(uhc.getPREFIX() + ChatColor.RED + "Usage: /helpop <message>");
            }
        }
        return false;
    }
}

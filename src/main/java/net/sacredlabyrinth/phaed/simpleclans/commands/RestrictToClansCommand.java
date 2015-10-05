package net.sacredlabyrinth.phaed.simpleclans.commands;

import net.sacredlabyrinth.phaed.simpleclans.ChatBlock;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.HardcoreTeamPvP;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author xtphty
 */
public class RestrictToClansCommand {
    HardcoreTeamPvP plugin;

    public RestrictToClansCommand() {
        plugin = HardcoreTeamPvP.getInstance();
    }

    /**
     * Restricts the servers to only players on a clan
     *
     * @param sender the command runner
     * @param arg command arguments
     */
    public void execute(CommandSender sender, String[] arg) {
        Player playerSender = null;
        if (sender instanceof Player){
            playerSender = (Player) sender;
        }

        // Make sure sender has permissions for this action
        if (sender.hasPermission("simpleclans.admin.restrict-to-clans")) {
            if ((arg == null) || (arg.length != 1)){
                ChatBlock.sendMessage(sender, ChatColor.RED + plugin.getLang("usage.restrict"));
                return;
            }

            boolean restrictionState = plugin.isRestrictedToClans();
            if (StringUtils.equalsIgnoreCase(arg[0], "enable")){
                restrictionState = true;
                ChatBlock.sendMessage(sender, ChatColor.GREEN + plugin.getLang("message.restrict.enable"));
            } else if (StringUtils.equalsIgnoreCase(arg[0], "disable")) {
                restrictionState = false;
                ChatBlock.sendMessage(sender, ChatColor.GREEN + plugin.getLang("message.restrict.disable"));
            } else {
                ChatBlock.sendMessage(sender, ChatColor.RED + plugin.getLang("usage.restrict"));
                return;
            }

            plugin.setRestrictedToClans(restrictionState);

            if (!plugin.isRestrictedToClans()){
                // Restriction disabled, dont kick
                return;
            }

            // Kick all online players that are not on a clan, except OPs
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.isOp()) {
                    continue;
                }

                if (plugin.getClanManager().getClanByPlayerUniqueId(onlinePlayer.getUniqueId()) == null) {
                    onlinePlayer.kickPlayer("You were kicked for not joining a clan before the round started, solo players not allowed.");
                }
            }

        } else {
            ChatBlock.sendMessage(sender, ChatColor.RED + plugin.getLang("insufficient.permissions"));
        }
    }
}

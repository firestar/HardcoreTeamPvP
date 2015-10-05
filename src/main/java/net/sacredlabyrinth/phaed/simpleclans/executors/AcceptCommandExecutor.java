package net.sacredlabyrinth.phaed.simpleclans.executors;

import net.sacredlabyrinth.phaed.simpleclans.*;
import net.sacredlabyrinth.phaed.simpleclans.utils.HardcoreTeamUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class AcceptCommandExecutor implements CommandExecutor
{
    HardcoreTeamPvP plugin;

    public AcceptCommandExecutor()
    {
        plugin = HardcoreTeamPvP.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        Player player = (Player) commandSender;

        if (plugin.getSettingsManager().isBanned(player.getName()))
        {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("banned"));
            return false;
        }

        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

        if (cp != null)
        {
            Clan clan = cp.getClan();

            if (clan.isLeader(player))
            {
                if (plugin.getRequestManager().hasRequest(clan.getTag()))
                {
                    if (cp.getVote() == null)
                    {
                        plugin.getRequestManager().accept(cp);
                        HardcoreTeamUtils.teamColor(player);
                        clan.leaderAnnounce(ChatColor.GREEN + MessageFormat.format(plugin.getLang("voted.to.accept"), Helper.capitalize(player.getName())));
                    }
                    else
                    {
                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("you.have.already.voted"));
                    }
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.to.accept"));
                }
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
            }
        }
        else
        {
            if (plugin.getRequestManager().hasRequest(player.getName().toLowerCase()))
            {
                if (HardcoreTeamPvP.getInstance().hasUUID())
                {
                    cp = plugin.getClanManager().getCreateClanPlayer(player.getUniqueId());
                }
                else
                {
                    cp = plugin.getClanManager().getCreateClanPlayer(player.getName());
                }
                plugin.getRequestManager().accept(cp);
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.to.accept"));
            }
        }
        return false;
    }
}

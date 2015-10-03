package net.sacredlabyrinth.phaed.simpleclans.commands;

import net.sacredlabyrinth.phaed.simpleclans.*;
import net.sacredlabyrinth.phaed.simpleclans.HardcoreTeamPvP;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import net.sacredlabyrinth.phaed.simpleclans.uuid.UUIDMigration;

/**
 *
 * @author phaed
 */
public class KickCommand
{
    public KickCommand()
    {
    }

    /**
     * Execute the command
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg)
    {
        HardcoreTeamPvP plugin = HardcoreTeamPvP.getInstance();

        if (plugin.getPermissionsManager().has(player, "simpleclans.leader.kick"))
        {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null)
            {
                Clan clan = cp.getClan();

                if (clan.isLeader(player))
                {
                    if (arg.length == 1)
                    {
                        String kicked = arg[0];

                        if (kicked != null)
                        {
                            if (!kicked.equalsIgnoreCase(player.getName()))
                            {
                                if (clan.isMember(kicked))
                                {
                                    if (!clan.isLeader(kicked))
                                    {
                                        clan.addBb(player.getName(),  ChatColor.AQUA + MessageFormat.format(plugin.getLang("has.been.kicked.by"), Helper.capitalize(kicked), player.getName()));
                                        if (HardcoreTeamPvP.getInstance().hasUUID())
                                        {
                                            clan.removePlayerFromClan(UUIDMigration.getForcedPlayerUUID(kicked));
                                        } else 
                                        {
                                            clan.removePlayerFromClan(kicked);
                                        }
                                    }
                                    else
                                    {
                                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("you.cannot.kick.another.leader"));
                                    }
                                }
                                else
                                {
                                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("the.player.is.not.a.member.of.your.clan"));
                                }
                            }
                            else
                            {
                                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("you.cannot.kick.yourself"));
                            }
                        }
                        else
                        {
                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.player.matched"));
                        }
                    }
                    else
                    {
                        ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.kick.player"), plugin.getSettingsManager().getCommandClan()));
                    }
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
                }
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("not.a.member.of.any.clan"));
            }
        }
        else
        {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
        }
    }
}

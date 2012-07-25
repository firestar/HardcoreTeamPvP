package net.sacredlabyrinth.phaed.simpleclans.commands;

import java.text.MessageFormat;
import net.sacredlabyrinth.phaed.simpleclans.*;
import net.sacredlabyrinth.phaed.simpleclans.beta.GenericPlayerCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author phaed
 */
public class TrustCommand extends GenericPlayerCommand
{

    private SimpleClans plugin;

    public TrustCommand(SimpleClans plugin)
    {
        super("Trust");
        this.plugin = plugin;
        setArgumentRange(1, 1);
        setUsages(String.format(plugin.getLang("usage.trust"), plugin.getSettingsManager().getCommandClan()));
        setIdentifiers(plugin.getLang("trust.command"));
    }

    @Override
    public String getMenu(ClanPlayer cp, CommandSender sender)
    {
        if (cp != null) {
            if (cp.isLeader() && cp.getClan().isVerified() && plugin.getPermissionsManager().has(sender, "simpleclans.leader.settrust")) {
                return MessageFormat.format(plugin.getLang("0.trust.untrust.player.1.set.trust.level1"), plugin.getSettingsManager().getCommandClan(), ChatColor.WHITE);
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String label, String[] args)
    {

        if (plugin.getPermissionsManager().has(player, "simpleclans.leader.settrust")) {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null) {
                Clan clan = cp.getClan();

                if (clan.isLeader(player)) {
                    String trusted = args[0];

                    if (trusted != null) {
                        if (!trusted.equals(player.getName())) {
                            if (clan.isMember(trusted)) {
                                if (!clan.isLeader(trusted)) {
                                    ClanPlayer tcp = plugin.getClanManager().getCreateClanPlayer(trusted);

                                    if (!tcp.isTrusted()) {
                                        clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("has.been.given.trusted.status.by"), Helper.capitalize(trusted), player.getName()));
                                        tcp.setTrusted(true);
                                        plugin.getStorageManager().updateClanPlayer(tcp);
                                    } else {
                                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("this.player.is.already.trusted"));
                                    }
                                } else {
                                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("leaders.are.already.trusted"));
                                }
                            } else {
                                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("the.player.is.not.a.member.of.your.clan"));
                            }
                        } else {
                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("you.cannot.trust.yourself"));
                        }
                    } else {
                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.player.matched"));
                    }
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
                }
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("not.a.member.of.any.clan"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
        }
    }
}

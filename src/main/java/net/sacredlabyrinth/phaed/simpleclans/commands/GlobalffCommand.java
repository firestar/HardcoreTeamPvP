package net.sacredlabyrinth.phaed.simpleclans.commands;

import net.sacredlabyrinth.phaed.simpleclans.ChatBlock;
import net.sacredlabyrinth.phaed.simpleclans.HardcoreTeamPvP;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class GlobalffCommand
{
    public GlobalffCommand()
    {
    }

    /**
     * Execute the command
     *
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg)
    {
        HardcoreTeamPvP plugin = HardcoreTeamPvP.getInstance();

        if (arg.length == 1)
        {
            String action = arg[0];

            if (action.equalsIgnoreCase(plugin.getLang("allow")))
            {
                if (plugin.getSettingsManager().isGlobalff())
                {
                    ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("global.friendly.fire.is.already.being.allowed"));
                }
                else
                {
                    plugin.getSettingsManager().setGlobalff(true);
                    ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("global.friendly.fire.is.set.to.allowed"));
                }
            }
            else if (action.equalsIgnoreCase(plugin.getLang("auto")))
            {
                if (!plugin.getSettingsManager().isGlobalff())
                {
                    ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("global.friendy.fire.is.already.being.managed.by.each.clan"));
                }
                else
                {
                    plugin.getSettingsManager().setGlobalff(false);
                    ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("global.friendy.fire.is.now.managed.by.each.clan"));
                }
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.globalff.allow.auto"), plugin.getSettingsManager().getCommandClan()));
            }
        }
        else
        {
            ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.ff.allow.auto"), plugin.getSettingsManager().getCommandClan()));
        }
    }
}

package net.sacredlabyrinth.phaed.simpleclans.executors;

import net.sacredlabyrinth.phaed.simpleclans.*;
import net.sacredlabyrinth.phaed.simpleclans.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author phaed
 */
public final class ClanCommandExecutor implements CommandExecutor
{
    private HardcoreTeamPvP plugin;
    private MenuCommand menuCommand;
    private Map<String, Object> commands = new HashMap<String, Object>();

    /**
     *
     */
    public ClanCommandExecutor()
    {
        plugin = HardcoreTeamPvP.getInstance();
        menuCommand = new MenuCommand();
        commands.put(plugin.getLang("create.command").toLowerCase(), new CreateCommand());
        commands.put(plugin.getLang("list.command").toLowerCase(), new ListCommand());
        commands.put(plugin.getLang("bank.command").toLowerCase(), new BankCommand());
        commands.put(plugin.getLang("profile.command").toLowerCase(), new ProfileCommand());
        commands.put(plugin.getLang("roster.command").toLowerCase(), new RosterCommand());
        commands.put(plugin.getLang("lookup.command").toLowerCase(), new LookupCommand());
        commands.put(plugin.getLang("home.command").toLowerCase(), new HomeCommand());
        commands.put(plugin.getLang("leaderboard.command").toLowerCase(), new LeaderboardCommand());
        commands.put(plugin.getLang("alliances.command").toLowerCase(), new AlliancesCommand());
        commands.put(plugin.getLang("rivalries.command").toLowerCase(), new RivalriesCommand());
        commands.put(plugin.getLang("vitals.command").toLowerCase(), new VitalsCommand());
        commands.put(plugin.getLang("coords.command").toLowerCase(), new CoordsCommand());
        commands.put(plugin.getLang("stats.command").toLowerCase(), new StatsCommand());
        commands.put(plugin.getLang("ally.command").toLowerCase(), new AllyCommand());
        commands.put(plugin.getLang("rival.command").toLowerCase(), new RivalCommand());
        commands.put(plugin.getLang("bb.command").toLowerCase(), new BbCommand());
        commands.put(plugin.getLang("modtag.command").toLowerCase(), new ModtagCommand());
        commands.put(plugin.getLang("toggle.command").toLowerCase(), new ToggleCommand());
        commands.put(plugin.getLang("cape.command").toLowerCase(), new CapeCommand());
        commands.put(plugin.getLang("invite.command").toLowerCase(), new InviteCommand());
        commands.put(plugin.getLang("kick.command").toLowerCase(), new KickCommand());
        commands.put(plugin.getLang("trust.command").toLowerCase(), new TrustCommand());
        commands.put(plugin.getLang("untrust.command").toLowerCase(), new UntrustCommand());
        commands.put(plugin.getLang("promote.command").toLowerCase(), new PromoteCommand());
        commands.put(plugin.getLang("demote.command").toLowerCase(), new DemoteCommand());
        commands.put(plugin.getLang("clanff.command").toLowerCase(), new ClanffCommand());
        commands.put(plugin.getLang("ff.command").toLowerCase(), new FfCommand());
        commands.put(plugin.getLang("resign.command").toLowerCase(), new ResignCommand());
        commands.put(plugin.getLang("disband.command").toLowerCase(), new DisbandCommand());
        commands.put(plugin.getLang("verify.command").toLowerCase(), new VerifyCommand());
        commands.put(plugin.getLang("ban.command").toLowerCase(), new BanCommand());
        commands.put(plugin.getLang("unban.command").toLowerCase(), new UnbanCommand());
        commands.put(plugin.getLang("reload.command").toLowerCase(), new ReloadCommand());
        commands.put(plugin.getLang("globalff.command").toLowerCase(), new GlobalffCommand());
        commands.put(plugin.getLang("war.command").toLowerCase(), new WarCommand());
        commands.put(plugin.getLang("kills.command").toLowerCase(), new KillsCommand());
        commands.put(plugin.getLang("mostkilled.command").toLowerCase(), new MostKilledCommand());
        commands.put(plugin.getLang("setrank.command").toLowerCase(), new SetRankCommand());
        commands.put(plugin.getLang("place.command").toLowerCase(), new PlaceCommand());
        commands.put(plugin.getLang("resetkdr.command").toLowerCase(), new ResetKDRCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        try
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;

                if (plugin.getSettingsManager().isBlacklistedWorld(player.getLocation().getWorld().getName()))
                {
                    return false;
                }

                if (plugin.getSettingsManager().isBanned(player.getName()))
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("banned"));
                    return false;
                }

                if (args.length == 0)
                {
                    menuCommand.execute(player);
                }
                else
                {
                    String subcommand = args[0];
                    String[] subargs = Helper.removeFirst(args);
                    
                	if(commands.containsKey(subcommand.toLowerCase()))
                	{
                		Object obj = commands.get(subcommand.toLowerCase());
                		obj.getClass().getMethod("execute", Player.class, String[].class).invoke(obj, player, subargs);
                	}
                    else
                    {
                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("does.not.match"));
                    }
                }
            }
            else
            {
                if (args.length == 0)
                {
                    menuCommand.executeSender(sender);
                }
                else
                {
                    String subcommand = args[0];
                    String[] subargs = Helper.removeFirst(args);

                    if(commands.containsKey(subcommand.toLowerCase()))
                	{
                		Object obj = commands.get(subcommand.toLowerCase());
                		obj.getClass().getMethod("execute", CommandSender.class, String[].class).invoke(obj, sender, subargs);
                	}
                    else
                    {
                        ChatBlock.sendMessage(sender, ChatColor.RED + plugin.getLang("does.not.match"));
                    }
                }
            }
        }
        catch (Exception ex)
        {
            HardcoreTeamPvP.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + MessageFormat.format(plugin.getLang("simpleclans.command.failure"), ex.getMessage()));
            for (StackTraceElement el : ex.getStackTrace())
            {
                System.out.print(el.toString());
            }
        }

        return false;
    }
}
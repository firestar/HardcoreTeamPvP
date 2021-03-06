package net.sacredlabyrinth.phaed.simpleclans.listeners;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.HardcoreTeamPvP;
import net.sacredlabyrinth.phaed.simpleclans.Helper;
import net.sacredlabyrinth.phaed.simpleclans.executors.*;
import net.sacredlabyrinth.phaed.simpleclans.utils.HardcoreTeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @author phaed
 */
public class SCPlayerListener implements Listener {
    private static Logger LOGGER = Logger.getLogger(SCPlayerListener.class.getName());

    private HardcoreTeamPvP plugin;

    /**
     *
     */
    public SCPlayerListener() {
        plugin = HardcoreTeamPvP.getInstance();
    }

    /**
     * Prevent solo players from joining if the restriction is enabled
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        if (!plugin.isRestrictedToClans()) {
            return;
        } else if (player.isOp()) {
            return;
        } else {
            Clan clan = plugin.getClanManager().getClanByPlayerUniqueId(player.getUniqueId());
            if (clan != null) {
                Bukkit.broadcastMessage(clan.getColorTag() + " " + player.getDisplayName() + " has connected.");
            } else {
                LOGGER.info("Clan restriction is active, kicking solo player: "
                        + player.getDisplayName());
                event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, plugin.getLang("message.restrict.disallow"));
            }
        }
    }

    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        if (plugin.getSettingsManager().isBlacklistedWorld(player.getLocation().getWorld().getName())) {
            return;
        }

        if (event.getMessage().length() == 0) {
            return;
        }

        String[] split = event.getMessage().substring(1).split(" ");

        if (split.length == 0) {
            return;
        }

        String command = split[0];

        if (plugin.getSettingsManager().isTagBasedClanChat() && plugin.getClanManager().isClan(command)) {
            if (!plugin.getSettingsManager().getClanChatEnable()) {
                return;
            }

            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp == null) {
                return;
            }

            if (cp.getTag().equalsIgnoreCase(command)) {
                event.setCancelled(true);

                if (split.length > 1) {
                    plugin.getClanManager().processClanChat(player, cp.getTag(), Helper.toMessage(Helper.removeFirst(split)));
                }
            }
        }
        if (command.equals(".")) {
            if (!plugin.getSettingsManager().getClanChatEnable()) {
                return;
            }

            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp == null) {
                return;
            }

            event.setCancelled(true);

            if (split.length > 1) {
                plugin.getClanManager().processClanChat(player, cp.getTag(), Helper.toMessage(Helper.removeFirst(split)));
            }
        }

        if (plugin.getSettingsManager().isForceCommandPriority()) {
            if (command.equalsIgnoreCase(plugin.getSettingsManager().getCommandAlly())) {
                if (!plugin.getServer().getPluginCommand(plugin.getSettingsManager().getCommandAlly()).equals(plugin.getCommand(plugin.getSettingsManager().getCommandAlly()))) {
                    new AllyCommandExecutor().onCommand(player, null, null, Helper.removeFirst(split));
                    event.setCancelled(true);
                }
            } else if (command.equalsIgnoreCase(plugin.getSettingsManager().getCommandGlobal())) {
                if (!plugin.getServer().getPluginCommand(plugin.getSettingsManager().getCommandGlobal()).equals(plugin.getCommand(plugin.getSettingsManager().getCommandGlobal()))) {
                    new GlobalCommandExecutor().onCommand(player, null, null, Helper.removeFirst(split));
                    event.setCancelled(true);
                }
            } else if (command.equalsIgnoreCase(plugin.getSettingsManager().getCommandClan())) {
                if (!plugin.getServer().getPluginCommand(plugin.getSettingsManager().getCommandClan()).equals(plugin.getCommand(plugin.getSettingsManager().getCommandClan()))) {
                    new ClanCommandExecutor().onCommand(player, null, null, Helper.removeFirst(split));
                    event.setCancelled(true);
                }
            } else if (command.equalsIgnoreCase(plugin.getSettingsManager().getCommandAccept())) {
                if (!plugin.getServer().getPluginCommand(plugin.getSettingsManager().getCommandAccept()).equals(plugin.getCommand(plugin.getSettingsManager().getCommandAccept()))) {
                    new AcceptCommandExecutor().onCommand(player, null, null, Helper.removeFirst(split));
                    event.setCancelled(true);
                }
            } else if (command.equalsIgnoreCase(plugin.getSettingsManager().getCommandDeny())) {
                if (!plugin.getServer().getPluginCommand(plugin.getSettingsManager().getCommandDeny()).equals(plugin.getCommand(plugin.getSettingsManager().getCommandDeny()))) {
                    new DenyCommandExecutor().onCommand(player, null, null, Helper.removeFirst(split));
                    event.setCancelled(true);
                }
            } else if (command.equalsIgnoreCase(plugin.getSettingsManager().getCommandMore())) {
                if (!plugin.getServer().getPluginCommand(plugin.getSettingsManager().getCommandMore()).equals(plugin.getCommand(plugin.getSettingsManager().getCommandMore()))) {
                    new MoreCommandExecutor().onCommand(player, null, null, Helper.removeFirst(split));
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (plugin.getSettingsManager().isBlacklistedWorld(event.getPlayer().getLocation().getWorld().getName())) {
            return;
        }

        if (event.getPlayer() == null) {
            return;
        }

        String message = event.getMessage();
        ClanPlayer cp = plugin.getClanManager().getClanPlayer(event.getPlayer());

        if (cp != null) {
            if (cp.getChannel().equals(ClanPlayer.Channel.CLAN)) {
                plugin.getClanManager().processClanChat(event.getPlayer(), message);
                event.setCancelled(true);
            } else if (cp.getChannel().equals(ClanPlayer.Channel.ALLY)) {
                plugin.getClanManager().processAllyChat(event.getPlayer(), message);
                event.setCancelled(true);
            }
        }

        if (!plugin.getPermissionsManager().has(event.getPlayer(), "simpleclans.mod.nohide")) {
            boolean isClanChat = event.getMessage().contains("" + ChatColor.RED + ChatColor.WHITE + ChatColor.RED + ChatColor.BLACK);
            boolean isAllyChat = event.getMessage().contains("" + ChatColor.AQUA + ChatColor.WHITE + ChatColor.AQUA + ChatColor.BLACK);

            for (Iterator iter = event.getRecipients().iterator(); iter.hasNext(); ) {
                Player player = (Player) iter.next();

                ClanPlayer rcp = plugin.getClanManager().getClanPlayer(player);

                if (rcp != null) {
                    if (!rcp.isClanChat()) {
                        if (isClanChat) {
                            iter.remove();
                            continue;
                        }
                    }

                    if (!rcp.isAllyChat()) {
                        if (isAllyChat) {
                            iter.remove();
                            continue;
                        }
                    }

                    if (!rcp.isGlobalChat()) {
                        if (!isAllyChat && !isClanChat) {
                            iter.remove();
                        }
                    }
                }
            }
        }

        if (plugin.getSettingsManager().isCompatMode()) {
            if (plugin.getSettingsManager().isChatTags()) {
                if (cp != null && cp.isTagEnabled()) {
                    String tagLabel = cp.getClan().getTagLabel(cp.isLeader());

                    Player player = event.getPlayer();

                    if (player.getDisplayName().contains("{clan}")) {
                        player.setDisplayName(player.getDisplayName().replace("{clan}", tagLabel));
                    } else if (event.getFormat().contains("{clan}")) {
                        event.setFormat(event.getFormat().replace("{clan}", tagLabel));
                    } else {
                        String format = event.getFormat();
                        event.setFormat(tagLabel + format);
                    }
                } else {
                    event.setFormat(event.getFormat().replace("{clan}", ""));
                    event.setFormat(event.getFormat().replace("tagLabel", ""));
                }
            }
        } else {
            plugin.getClanManager().updateDisplayName(event.getPlayer());
        }
    }

    /**
     * @param event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (HardcoreTeamPvP.getInstance().getSettingsManager().isBlacklistedWorld(player.getLocation().getWorld().getName())) {
            return;
        }
        
        HardcoreTeamUtils.teamColor(player);
        
        ClanPlayer cp;
        if (HardcoreTeamPvP.getInstance().getSettingsManager().getUseBungeeCord()) {
            cp = HardcoreTeamPvP.getInstance().getClanManager().getClanPlayerJoinEvent(player);
        } else {
            cp = HardcoreTeamPvP.getInstance().getClanManager().getClanPlayer(player);
        }

        if (cp == null) {
            return;
        }
        cp.setName(player.getName());
        HardcoreTeamPvP.getInstance().getClanManager().updateLastSeen(player);
        HardcoreTeamPvP.getInstance().getClanManager().updateDisplayName(player);
        if (HardcoreTeamPvP.getInstance().hasUUID()) {
            HardcoreTeamPvP.getInstance().getSpoutPluginManager().processPlayer(cp.getUniqueId());
        } else {
            HardcoreTeamPvP.getInstance().getSpoutPluginManager().processPlayer(cp.getName());
        }
        HardcoreTeamPvP.getInstance().getPermissionsManager().addPlayerPermissions(cp);

        if (plugin.getSettingsManager().isBbShowOnLogin()) {
            if (cp.isBbEnabled()) {
                cp.getClan().displayBb(player);
            }
        }

        HardcoreTeamPvP.getInstance().getPermissionsManager().addClanPermissions(cp);

        if (event.getPlayer().isOp()) {
            for (String message : HardcoreTeamPvP.getInstance().getMessages()) {
                event.getPlayer().sendMessage(ChatColor.YELLOW + message);
            }
        }
    }

    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (plugin.getSettingsManager().isBlacklistedWorld(event.getPlayer().getLocation().getWorld().getName())) {
            return;
        }

        if (plugin.getSettingsManager().isTeleportOnSpawn()) {
            Player player = event.getPlayer();

            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null) {
                Location loc = cp.getClan().getHomeLocation();

                if (loc != null) {
                    event.setRespawnLocation(loc);
                }
            }
        }
    }

    /**
     * @param event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getSettingsManager().isBlacklistedWorld(event.getPlayer().getLocation().getWorld().getName())) {
            return;
        }

        ClanPlayer cp = plugin.getClanManager().getClanPlayer(event.getPlayer());

        HardcoreTeamPvP.getInstance().getPermissionsManager().removeClanPlayerPermissions(cp);
        plugin.getClanManager().updateLastSeen(event.getPlayer());
        plugin.getRequestManager().endPendingRequest(event.getPlayer().getName());
    }

    /**
     * @param event
     */
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (plugin.getSettingsManager().isBlacklistedWorld(event.getPlayer().getLocation().getWorld().getName())) {
            return;
        }

        plugin.getClanManager().updateLastSeen(event.getPlayer());
    }

    /**
     * @param event
     */
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (plugin.getSettingsManager().isBlacklistedWorld(event.getPlayer().getLocation().getWorld().getName())) {
            return;
        }

        plugin.getSpoutPluginManager().processPlayer(event.getPlayer());
    }

    /**
     * @param event
     */
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        plugin.getSpoutPluginManager().processPlayer(event.getPlayer());
    }
}
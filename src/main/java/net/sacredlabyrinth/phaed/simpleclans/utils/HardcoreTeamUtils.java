package net.sacredlabyrinth.phaed.simpleclans.utils;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.getspout.spout.player.SpoutCraftPlayer;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.HardcoreTeamPvP;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;

public class HardcoreTeamUtils {
	public static Team staticColor;
	public static void teamColor(Player p){
		HardcoreTeamPvP plugin = HardcoreTeamPvP.getInstance();
		if(plugin.getClanManager()==null || plugin.getClanManager().getClanPlayer(p)==null){
			return;
		}
		Clan clan = plugin.getClanManager().getClanPlayer(p).getClan();
		if(clan!=null){
			try {
	            Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
	            Team color = board.getTeam(p.getName());
	            if (color == null) {
	                color = board.registerNewTeam(p.getName());
	            }
	            color.setPrefix(String.valueOf('\u00a7')+clan.getColorTag()+" .:. ");
	            color.addPlayer((OfflinePlayer)p);
	            p.setScoreboard(board);
	            staticColor = color;
	        }
	        catch (NullPointerException board) {
	            // empty catch block
	        }
		}
    }
}

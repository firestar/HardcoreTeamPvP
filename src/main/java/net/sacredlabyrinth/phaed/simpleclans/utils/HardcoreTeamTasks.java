package net.sacredlabyrinth.phaed.simpleclans.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.HardcoreTeamPvP;

public class HardcoreTeamTasks {
	public static Clan disbandClan=null;
	public static void startTasks(HardcoreTeamPvP plugin){
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
            	HardcoreTeamPvP plugin = (HardcoreTeamPvP) Bukkit.getPluginManager().getPlugin("HardcoreTeamPvP");
            	for(Player p: Bukkit.getServer().getOnlinePlayers()){
            		HardcoreTeamUtils.teamColor(p);
        		}
            }
        }, 100L, 100L);
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
            	Clan toKick = disbandClan;
            	if(toKick!=null){
            		HardcoreTeamPvP plugin = (HardcoreTeamPvP) Bukkit.getPluginManager().getPlugin("HardcoreTeamPvP");
            		for(ClanPlayer p :toKick.getMembers()){
            			toKick.removeMember(p.getUniqueId());
					}
            		toKick.disband();
            		disbandClan=null;
            	}
            	
            }
        }, 20L, 20L);
	}
}

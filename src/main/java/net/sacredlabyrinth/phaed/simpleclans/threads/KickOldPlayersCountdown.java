package net.sacredlabyrinth.phaed.simpleclans.threads;

import org.bukkit.Bukkit;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.HardcoreTeamPvP;

public class KickOldPlayersCountdown implements Runnable{
	private int repeatTime=1;
	public KickOldPlayersCountdown(int repeatTime){
		repeatTime = repeatTime;
	}
	@Override
	public void run() {
		HardcoreTeamPvP plugin = HardcoreTeamPvP.getInstance();
		while(true){
			try {
				Thread.sleep(repeatTime*60*60*1000); // sleep for 1 hour by default, then execute checks
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				clanChecks(plugin);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void clanChecks(HardcoreTeamPvP plugin) throws Exception{
		int lowest=-1;
		Clan lowestClan = null;
		for(Clan c: plugin.getClanManager().getClans()){
			if(lowest==-1 || lowest>c.getKillCount()){
				lowest = c.getKillCount();
				lowestClan=c;
			}
		}
		System.out.println("Lowest clan is "+lowestClan+" with "+lowest+" kills");
	}

}

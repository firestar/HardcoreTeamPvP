package net.sacredlabyrinth.phaed.simpleclans.threads;

import org.bukkit.Bukkit;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.HardcoreTeamPvP;

public class KickOldPlayersCountdown implements Runnable{
	private int repeatTime=1;
	private int countdown = 10;
	private int countdownTime = 0;
	public KickOldPlayersCountdown(int repeatTime, int countdownTime){
		countdown = countdownTime;
		this.countdownTime = countdownTime;
		this.repeatTime = repeatTime;
	}
	@Override
	public void run() {
		HardcoreTeamPvP plugin = HardcoreTeamPvP.getInstance();
		if(repeatTime!=0 && repeatTime>0){
			while(true){
				try {
					Thread.sleep(repeatTime*60*60*1000); // sleep for 1 hour by default, then execute checks
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(countdown-1==0){
					try {
						clanChecks(plugin);
					} catch (Exception e) {
						e.printStackTrace();
					}
					countdown = countdownTime;
				}else{
					countdown--;
				}
			}
		}else{
			System.out.println("Lowest kill clan kick disabled ");
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

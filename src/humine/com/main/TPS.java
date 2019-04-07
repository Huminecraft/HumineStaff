package humine.com.main;


import java.util.logging.Level;

import org.bukkit.Bukkit;

public class TPS {
	public static boolean enabled = FileManager.getTPSConfig();
        private static double tps;
        
        public static void startTPSManager() {
            if (enabled) {
        	Bukkit.getServer().getScheduler().runTaskTimer(StaffMain.getInstance(), new Runnable() {
            	    
    		    long secstart;
    		    long secend;

    		    int ticks;

    		    @Override
    		    public void run() {
    			secstart = (System.currentTimeMillis() / 1000);

    			if (secstart == secend) {
    			    ticks++;
    			} else {
    			    secend = secstart;
    			    tps = (tps == 0) ? ticks : ((tps + ticks) / 2);
    			    ticks = 1;
    			}
    		    }

    		}, 0, 1);
    		
    		Bukkit.getServer().getScheduler().runTaskTimer(StaffMain.getInstance(), new Runnable() {
    		    @Override
    		    public void run() {
    			if (tps < 18.5) {
    			    Bukkit.getServer().shutdown();
    			}
    			Bukkit.getServer().getLogger().log(Level.INFO, "Current TPS : " + Math.round(tps));
    		    }

    		}, 1200, 1200);
            } else {
        	System.out.println("Le moniteur de TPS est désactivé!");
            }
              
        }
}

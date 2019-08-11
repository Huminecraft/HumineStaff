package humine.com.main;


import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("ALL")
public class TPS {
	public static boolean enabled = FileManager.getTPSConfig();
	private static double tps;

		@Contract(pure = true)
		public static double getTPS(){
			return tps;
		}
        
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
    		    	if (enabled) {
						ArrayList<Double> history = new ArrayList<Double>();

						history.add(tps);

						if (tps < 18.5) {
							Bukkit.getServer().shutdown();
						}
						Bukkit.getServer().getLogger().log(Level.INFO, "TPS Actuel : " + Math.round(tps));
						Bukkit.getServer().getLogger().log(Level.INFO, "Historique des TPS : " + history);
					}
    		    }}, 1200, 1200);
			} else {
        		System.out.println("Le moniteur de TPS est désactivé!");
            }

        }

}

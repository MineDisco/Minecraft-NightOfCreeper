package nightofcreeper.schedulers;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.scheduler.BukkitRunnable;


public class NightDetector extends BukkitRunnable {

    private World world;

    public NightDetector(Server server) {
        this.world = null;
        for (World w : server.getWorlds()) {
            if(w.getEnvironment().equals(Environment.NORMAL)) this.world = w ;
            break;
        }
        
    }

    @Override
    public void run() {
        if (this.world != null && (this.world.getTime() < 12950 || this.world.getTime() > 23050)) {
            this.world.setTime(12951);
        }
    }
}
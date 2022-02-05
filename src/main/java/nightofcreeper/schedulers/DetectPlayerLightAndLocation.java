package nightofcreeper.schedulers;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class DetectPlayerLightAndLocation extends BukkitRunnable {

    private final Server server;

    public DetectPlayerLightAndLocation(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        
        for(Player p : this.server.getOnlinePlayers()) {
            Location loc = p.getLocation();
            World world = loc.getWorld();
            if (world.getHighestBlockAt(loc).getLocation().getY() < loc.getY()) {
                p.sendMessage("You have to go outside, or... ");
            }
            
            p.sendMessage(loc.getBlock().getLightLevel() + "");
        }
   
    }
   
}
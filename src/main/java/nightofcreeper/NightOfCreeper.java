package nightofcreeper;

import org.bukkit.plugin.java.JavaPlugin;

import nightofcreeper.listeners.EntitySpawnEventListener;
import nightofcreeper.listeners.PlayerLoginListener;
import nightofcreeper.schedulers.NightDetector;


/**
 *
 */
public final class NightOfCreeper extends JavaPlugin {


    @Override
    public void onEnable() {
        this.saveDefaultConfig();

       NightDetector night = new NightDetector(this.getServer());
       night.runTaskTimer(this, 0, 200);

       getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
       getServer().getPluginManager().registerEvents(new EntitySpawnEventListener(), this);

    }

    @Override
    public void onDisable() {

        getLogger().info("Night of terror is disabled");
    }

   

}

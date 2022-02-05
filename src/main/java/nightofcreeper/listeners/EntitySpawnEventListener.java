package nightofcreeper.listeners;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World.Environment;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import nightofcreeper.NightOfCreeper;

import org.bukkit.attribute.*;
import org.bukkit.block.Block;

public class EntitySpawnEventListener implements Listener {
    private Random r;
    private final int CRAFT_ITEM_CREEPER_SOUND_PROBABILITY;
    private final int BLOCK_BREAK_CREEPER_PROBABILITY;
    private final int CREEPER_EXPLOSION_PRIMED_PROBABILITY;
    private final int BOAT_LIGHTNING_PROBABILITY;
    private final int NETHER_NOT_CREEPER_PROBABILITY;
    private final int END_GHAST_PROBABILITY;
    private final int SUPER_CREEPER_PROBABILITY;
    private final int POWERED_CREEPER_PROBABILITY;


    public EntitySpawnEventListener() {
        this.r = new Random();
        this.CRAFT_ITEM_CREEPER_SOUND_PROBABILITY = NightOfCreeper.getPlugin(NightOfCreeper.class).getConfig().getInt("CRAFT_ITEM_CREEPER_SOUND_PROBABILITY");
        this.BLOCK_BREAK_CREEPER_PROBABILITY = NightOfCreeper.getPlugin(NightOfCreeper.class).getConfig().getInt("BLOCK_BREAK_CREEPER_PROBABILITY");
        this.CREEPER_EXPLOSION_PRIMED_PROBABILITY = NightOfCreeper.getPlugin(NightOfCreeper.class).getConfig().getInt("CREEPER_EXPLOSION_PRIMED_PROBABILITY");
        this.BOAT_LIGHTNING_PROBABILITY = NightOfCreeper.getPlugin(NightOfCreeper.class).getConfig().getInt("BOAT_LIGHTNING_PROBABILITY");
        this.NETHER_NOT_CREEPER_PROBABILITY = NightOfCreeper.getPlugin(NightOfCreeper.class).getConfig().getInt("NETHER_NOT_CREEPER_PROBABILITY");
        this.END_GHAST_PROBABILITY = NightOfCreeper.getPlugin(NightOfCreeper.class).getConfig().getInt("END_GHAST_PROBABILITY");
        this.SUPER_CREEPER_PROBABILITY = NightOfCreeper.getPlugin(NightOfCreeper.class).getConfig().getInt("SUPER_CREEPER_PROBABILITY");
        this.POWERED_CREEPER_PROBABILITY = NightOfCreeper.getPlugin(NightOfCreeper.class).getConfig().getInt("POWERED_CREEPER_PROBABILITY");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void CraftItemHandler(CraftItemEvent event) {
        if (r.nextInt(100) < CRAFT_ITEM_CREEPER_SOUND_PROBABILITY) {
            event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_CREEPER_PRIMED,
                    SoundCategory.HOSTILE,
                    100, 100);
        }

        if(event.getInventory().getResult().getType().equals(Material.TORCH)) {
            event.getInventory().setResult(new ItemStack(Material.SOUL_TORCH, event.getInventory().getResult().getAmount()));
        }

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void BlockBreakHandler(BlockBreakEvent event) {
        if (r.nextInt(100) <= BLOCK_BREAK_CREEPER_PROBABILITY) {
            Location l = event.getPlayer().getLocation();
            Location l1 = event.getBlock().getLocation();
            l1.add(l1.getX() > 0 ? 0.5 : -0.5, 0.0, l1.getZ() > 0 ? 0.5 : -0.5);

            if (!(event.getBlock().getWorld().getHighestBlockAt(l).getLocation().getY() < l.getY() + 1)) {
                Creeper c = (Creeper) event.getBlock().getLocation().getWorld().spawnEntity(l1, EntityType.CREEPER);
                c.ignite();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void EntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.CREEPER && event.getCause() == DamageCause.SUFFOCATION) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void ExplosionPrime(ExplosionPrimeEvent event) {
        if (event.getEntityType() == EntityType.CREEPER && r.nextInt(100) <= CREEPER_EXPLOSION_PRIMED_PROBABILITY) {
            Creeper c = (Creeper) event.getEntity();
            c.explode();
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void ProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Ghast) {
            event.setCancelled(true);
            Ghast shooter = (Ghast) event.getEntity().getShooter();

            Block targetBlock = shooter.getTargetBlock(null, 250);
            double closestDist = 100.0;
            Player closestPlayerToTarget = null;
            for (Entity e1 : shooter.getNearbyEntities(150, 150, 150)) {
                if (e1 instanceof Player) {
                    if (e1.getLocation().distance(targetBlock.getLocation()) < closestDist)
                        closestPlayerToTarget = (Player) e1;
                }
            }
            if (closestPlayerToTarget == null)
                return;
            Projectile fireball = event.getEntity();
            Location fireballLoc = fireball.getLocation();

            Double vX = closestPlayerToTarget.getLocation().getX() - fireballLoc.getX();
            Double vY = closestPlayerToTarget.getLocation().getY() - fireballLoc.getY();
            Double vZ = closestPlayerToTarget.getLocation().getZ() - fireballLoc.getZ();
            Vector vectorToTarget = new Vector(vX, vY, vZ);
            fireball.remove();

            fireballLoc.setDirection(vectorToTarget);
            Fireball fireball2 = closestPlayerToTarget.getWorld().spawn(fireballLoc, Fireball.class);

            fireball2.setIsIncendiary(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (this.r.nextInt(100) > BOAT_LIGHTNING_PROBABILITY) {
            return;
        }

        if (player.isInsideVehicle()) {
            if (player.getVehicle() instanceof Boat) {
                if (e.getTo().getChunk() != e.getFrom().getChunk()) {
                    if (e.getTo().getBlockX() != e.getFrom().getBlockX()
                            || e.getTo().getBlockY() != e.getFrom().getBlockY()
                            || e.getTo().getBlockZ() != e.getFrom().getBlockZ()) {
                        int radius = 36;
                        Location focusLocation = e.getTo();
                        double xLocation1 = focusLocation.getX() - Math.round(this.r.nextInt(radius) - 18);
                        double zLocation1 = focusLocation.getZ() - Math.round(this.r.nextInt(radius) - 18);
                        e.getTo().getWorld()
                                .strikeLightning(new Location(focusLocation.getWorld(), xLocation1, focusLocation.getY(), zLocation1));
                    }
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void EntitySpawnEventHandler(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == SpawnReason.CUSTOM) {
            return;
        }

        if (event.getSpawnReason() == SpawnReason.NATURAL && event.getEntityType() == EntityType.CREEPER) {
            event.getEntity().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
            event.getEntity().getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100.0);

        }

        if (event.getLocation().getWorld().getEnvironment().equals(Environment.NETHER)) {
            if (r.nextInt(100) <= NETHER_NOT_CREEPER_PROBABILITY) {
                return;
            }
            if (event.getEntityType() == EntityType.ZOMBIFIED_PIGLIN) {
                event.setCancelled(true);
            }
        }

        if (event.getEntityType() == EntityType.ENDERMAN
                && event.getLocation().getWorld().getEnvironment().equals(Environment.THE_END)) {
            if (r.nextInt(100) <= END_GHAST_PROBABILITY) {
                Location l = event.getLocation();
                l.setY(event.getLocation().getWorld().getHighestBlockAt(l).getLocation().getY() + 20);
                Ghast g = (Ghast) event.getLocation().getWorld().spawnEntity(l, EntityType.GHAST);
                g.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
                g.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(1.4);
                return;
            }
        }

        if ((event.getSpawnReason() == SpawnReason.NATURAL || event.getSpawnReason() == SpawnReason.REINFORCEMENTS
                || event.getSpawnReason() == SpawnReason.DROWNED || event.getSpawnReason() == SpawnReason.SPAWNER
                || event.getSpawnReason() == SpawnReason.SILVERFISH_BLOCK
                || event.getSpawnReason() == SpawnReason.VILLAGE_INVASION)
                && checkIfMobToChange(event.getEntityType())) {
            event.setCancelled(true);

            if (r.nextInt(100) <= SUPER_CREEPER_PROBABILITY) {
                Location l = event.getLocation();
                l.setY(event.getLocation().getWorld().getHighestBlockAt(l).getLocation().getY() + 30);
                Ghast g = (Ghast) event.getLocation().getWorld().spawnEntity(l, EntityType.GHAST);
                g.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
                g.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(1.4);
            } else {
                Creeper c = (Creeper) event.getLocation().getWorld().spawnEntity(event.getLocation(),
                        EntityType.CREEPER);
                c.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(50.0);
                if (r.nextInt(100) <= POWERED_CREEPER_PROBABILITY) {
                    c.setPowered(true);
                }
            }
        }
    }

    private boolean checkIfMobToChange(EntityType type) {
        if (type == EntityType.CAVE_SPIDER || type == EntityType.DROWNED || type == EntityType.HOGLIN
                || type == EntityType.PIGLIN || type == EntityType.PILLAGER || type == EntityType.SLIME
                || type == EntityType.HUSK || type == EntityType.HOGLIN || type == EntityType.STRAY
                || type == EntityType.VINDICATOR || type == EntityType.WITHER_SKELETON || type == EntityType.ZOGLIN
                || type == EntityType.ZOMBIE_VILLAGER || type == EntityType.ZOMBIFIED_PIGLIN) {
            return true;
        } else if (type == EntityType.SKELETON) {
            return true;
        } else if (type == EntityType.ZOMBIE) {
            return true;
        } else if (type == EntityType.WITCH) {
            return true;
        } else if (type == EntityType.SPIDER) {
            return true;
        }

        return false;
    }

}

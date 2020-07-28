package lt.Arnas.BlazeBoss;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by User on 2019-01-01.
 */
public class Boss implements Listener{

    private Entity bossEntity;
    private EntityType entityType;
    private Double health;
    private Location spawnLocation;
    private Config config;
    private boolean isAlive = false;
    private ParticleManager particleManager;
    private ArmorStand BossBar;
    private boolean forceKill = false;

    public void forceKill(){
        this.forceKill = true;
        death();
    }

    private HashMap<Player, Double> playerDamage = new HashMap<>();

    public Double getHealth(){
        return health;
    }

    public Boss(EntityType entityType, String bossName, Double health, Location spawnLocation, Config config){
        this.entityType = entityType;
        this.health = health;
        this.spawnLocation = spawnLocation;
        this.config = config;
        Main.bossExists = true;
        Main.stopCountdown();
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);

        bossEntity = spawnLocation.getWorld().spawnEntity(spawnLocation, entityType);
        setEntityNoAI(bossEntity);
        Damageable bossDamageable = (Damageable) bossEntity;
        bossEntity.setCustomName(ChatColor.translateAlternateColorCodes('&',bossName));
        bossDamageable.setMaxHealth(200);
        ((Damageable) bossEntity).setHealth(200);

        BossBar = (ArmorStand) spawnLocation.getWorld().spawnEntity(spawnLocation.add(0,0.6, 0), EntityType.ARMOR_STAND);
        BossBar.setCustomName("███████");
        BossBar.setVisible(false);
        updateBar();
        BossBar.setCustomNameVisible(true);

        isAlive = true;
        particleManager = new ParticleManager();
        specialAttackTimer();
        reminderNotification();
    }

    public void reminderNotification(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if(isAlive) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.reminderNotification).replace("{NAME}", config.bossName).replace("{HEALTH}", Long.toString(Math.round(health))).replace("{TIME}", Integer.toString(config.defaultSpawnTime)));
                }
            }
        }.runTaskTimer(Main.plugin, 0L, config.reminderNotification_delay*20);
    }

    public void setEntityNoAI(Entity entity) {
        final net.minecraft.server.v1_8_R3.Entity nms = ((CraftEntity) entity).getHandle();
        final NBTTagCompound tag = new NBTTagCompound();
        nms.c(tag);
        tag.setBoolean("NoAI", true);
        final EntityLiving entitys = (EntityLiving) nms;
        entitys.a(tag);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(isAlive){
            if(bossEntity != null){
                if(bossEntity.getNearbyEntities(config.nearbyPushbackRadius, config.nearbyPushbackRadius, config.nearbyPushbackRadius).contains(e.getPlayer())){
                    Vector directionVector = e.getPlayer().getLocation().getDirection().normalize();
                    e.getPlayer().setVelocity(e.getPlayer().getVelocity().add(directionVector.multiply(-0.35)));
                }
            }
        }
    }

    public void specialAttackTimer(){
        if(isAlive) {
            Random random = new Random();
            new BukkitRunnable() {
                int delay = random.nextInt(config.time_delayBetweenAttacks) + 1;
                @Override
                public void run() {
                    delay--;
                    if(isAlive && delay == 0) {

                        int attack = random.nextInt(4);
                        if(attack == 0) {
                            particleManager.spawnShape(ParticleManager.ShapeType.RING_WAVE, EnumParticle.LAVA, ((LivingEntity) bossEntity).getEyeLocation(), null, 1, config.fireRadius);
                            for (Entity entity : bossEntity.getNearbyEntities(config.fireRadius,3, config.fireRadius)){
                                if(entity instanceof Player) {
                                    entity.setFireTicks(config.fireTicks);
                                }
                            }
                        } else if (attack == 1){
                            particleManager.spawnShape(ParticleManager.ShapeType.RING_WAVE, EnumParticle.SPELL, bossEntity.getLocation().add(0,0.2,0), null, 1, config.debuffRadius);
                            for (Entity entity : bossEntity.getNearbyEntities(config.debuffRadius,3, config.debuffRadius)){
                                if(entity instanceof Player) {
                                    int potionEff = random.nextInt(4);
                                    if (potionEff == 0) {
                                        ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, config.debuffTicks, 1));
                                    } else if(potionEff == 1){
                                        ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, config.debuffTicks, 1));
                                    } else if(potionEff == 2){
                                        ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, config.debuffTicks, 3));
                                    } else {
                                        ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, config.debuffTicks, 1));
                                    }
                                }
                            }
                        } else if(attack == 2){
                            particleManager.spawnShape(ParticleManager.ShapeType.RING_WAVE, EnumParticle.SMOKE_LARGE, ((LivingEntity) bossEntity).getEyeLocation(), null, 1, config.lightningRadius);
                            for(Entity entity : bossEntity.getNearbyEntities(config.lightningRadius,3, config.lightningRadius)){
                                if(entity instanceof Player){
                                    entity.getWorld().strikeLightning(entity.getLocation());
                                }
                            }
                        } else{
                            particleManager.spawnShape(ParticleManager.ShapeType.RING_WAVE, EnumParticle.FIREWORKS_SPARK, bossEntity.getLocation().add(0,0.2,0), null, 1, config.pushbackRadius);
                            for(Entity entity : bossEntity.getNearbyEntities(config.pushbackRadius, 3, config.pushbackRadius)){
                                if(entity instanceof Player){
                                    entity.setVelocity(entity.getLocation().getDirection().multiply(-2));
                                }
                            }
                        }
                        delay = random.nextInt(config.time_delayBetweenAttacks) + 1;
                    }
                }
            }.runTaskTimer(Main.plugin, 0L, 20L);
        }
    }


    public void death() {

        if(bossEntity != null){
            ((Damageable) bossEntity).damage(300);
        }
        System.out.print(forceKill);
        if(!forceKill) {
            for (Map.Entry<ItemStack, Double> entry : config.dropRate.entrySet()) {
                try {
                    if (Math.random() < entry.getValue()) {
                        spawnLocation.getWorld().dropItemNaturally(spawnLocation, entry.getKey());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (Map.Entry<Player, Double> entry : playerDamage.entrySet()) {
                if (entry.getValue() >= config.minDamage) {
                    for (ItemStack item : config.dropTable) {
                        if (!config.dropRate.containsKey(item)) {
                            if (entry.getKey().getInventory().firstEmpty() != -1) {
                                entry.getKey().getInventory().addItem(item);
                            } else {
                                entry.getKey().getLocation().getWorld().dropItemNaturally(entry.getKey().getLocation(), item);
                            }
                        }
                    }
                }
            }
        }

        forceKill = false;
        isAlive = false;
        Main.bossExists = false;
        Main.boss = null;
        this.bossEntity = null;
        removeBar();
        if(Main.canTP){
            Main.canTP = false;
        }
        Main.startBossCountdown();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if(e.getEntity() == bossEntity){
            e.getDrops().clear();
            if(!forceKill) {
                death();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(isAlive) {
            if (e.getEntity() instanceof Player) {
                if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
                    if(bossEntity.getNearbyEntities(config.fireRadius, 10, config.fireRadius).contains(e.getEntity())){
                        e.setDamage(config.fireDamage);
                    }
                } else if(e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)){
                    if(bossEntity.getNearbyEntities(config.lightningRadius, 10, config.lightningRadius).contains(e.getEntity())){
                        e.setDamage(config.lightningDamage);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityTakeDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() == bossEntity){
            if(e.getDamager() instanceof Player) {
                if(playerDamage.get(e.getDamager()) != null){
                    double damage = playerDamage.get(e.getDamager());
                    playerDamage.remove(e.getDamager());
                    playerDamage.put((Player)e.getDamager(), damage+e.getDamage());
                } else{
                    playerDamage.put((Player)e.getDamager(), e.getDamage());
                }
                health -= e.getDamage();
                if (health <= 0) {
                    ((Damageable) e.getEntity()).damage(200.0);
                } else {
                    ((Damageable) e.getEntity()).setHealth(200.0);
                }
            }
            updateBar();
        }
    }

    private void updateBar(){
        if(health > 0) {
            if (BossBar.getCustomName().length() != Math.round(health / config.boss_maxHealth * 36)) {
                ChatColor color;
                if((int)Math.round(health / config.boss_maxHealth*36) >= 23){
                    color = ChatColor.GREEN;
                } else if((int)Math.round(health / config.boss_maxHealth*36) < 23 && (int)Math.round(health / config.boss_maxHealth*36) >= 17){
                    color = ChatColor.YELLOW;
                } else if((int)Math.round(health / config.boss_maxHealth*36) < 17 && (int)Math.round(health / config.boss_maxHealth*36) >= 12){
                    color = ChatColor.GOLD;
                } else if((int)Math.round(health / config.boss_maxHealth*36) < 12 && (int)Math.round(health / config.boss_maxHealth*36) >= 6){
                    color = ChatColor.RED;
                } else{
                    color = ChatColor.DARK_RED;
                }
                BossBar.setCustomName(color + StringUtils.repeat("█", (int)Math.round(health / config.boss_maxHealth*36)));
            }
        } else{
            removeBar();
        }
    }

    private void removeBar(){
        BossBar.setCustomNameVisible(false);
        BossBar.remove();
    }



}

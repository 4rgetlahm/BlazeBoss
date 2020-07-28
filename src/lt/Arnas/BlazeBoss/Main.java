package lt.Arnas.BlazeBoss;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by User on 2019-01-01.
 */
public class Main extends JavaPlugin {

    /*
    * CONFIG:
    * BOSS SPAWN LOCATION
    * PLAYER SPAWN LOCATION
    * MESSAGES BEFORE SPAWN
    * */

    public static JavaPlugin plugin = null;
    public static Config config;
    public static boolean bossExists = false;
    public static Boss boss;
    public static int countdown;
    public static boolean stopCountdown = false;
    public static boolean canTP = false;

    public void onEnable(){
        Bukkit.getServer().getLogger().log(Level.WARNING, "[BlazeBoss] Pluginas isijungia.");
        plugin = this;

        config = new Config();
        config.loadDefaultConfig();
        config.readConfig();

        for(Entity entity : Bukkit.getServer().getWorld(config.boss_world).getEntities()){
            if(entity.getType() == EntityType.ARMOR_STAND){
                if(entity.getCustomName().contains("█")){
                    entity.remove();
                }
            }
            if(entity.getType() == EntityType.BLAZE){
                if(entity.getCustomName().equals(config.bossName)){
                    entity.remove();
                }
            }
        }

        startBossCountdown();
    }

    public static void startBossCountdown(){
        countdown = config.defaultSpawnTime;

        new BukkitRunnable() {
            int currentGoal = config.time_notificationList.get(0);
            int goalID = 0;
            int currentTime = config.defaultSpawnTime;

            @Override
            public void run() {
                if(stopCountdown) this.cancel();
                currentTime--;
                if (currentTime == currentGoal) {
                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.notificationMessage.replace("{TIME}", Integer.toString(currentTime)).replace("{NAME}", config.bossName)));
                    if (goalID == (config.time_notificationList.size() - 1)) {
                        this.cancel();
                    } else {
                        goalID++;
                        currentGoal = config.time_notificationList.get(goalID);
                    }
                }
            }
        }.runTaskTimer(Main.plugin, 0, 20);

        new BukkitRunnable(){
            @Override
            public void run() {
                if(stopCountdown) this.cancel();
                countdown--;
                if(countdown == 0) {
                    boss = new Boss(EntityType.BLAZE, config.bossName, config.boss_maxHealth, new Location(Bukkit.getWorld(config.boss_world), config.boss_spawnX, config.boss_spawnY, config.boss_spawnZ), config);
                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.spawnMessage.replace("{NAME}", config.bossName)));
                }
            }
        }.runTaskTimer(Main.plugin, 0, 20);

        stopCountdown = false;
    }

    public static void stopCountdown(){
        stopCountdown = true;
    }

    public void onDisable(){
        Bukkit.getServer().getLogger().log(Level.WARNING, "[BlazeBoss] Pluginas isjungiamas.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("boss")){
            if(args.length != 1) return true;
            if(args[0].equalsIgnoreCase( "setspawn")) {
                if (player.hasPermission(config.perm_setBossSpawn)) {
                    try {
                        Location location = player.getLocation();
                        config.boss_world = location.getWorld().getName();
                        config.boss_spawnX = location.getX();
                        config.boss_spawnY = location.getY();
                        config.boss_spawnZ = location.getZ();
                        config.saveConfig();
                        player.sendMessage(ChatColor.GREEN + "Boso atsiradimo pozicija išsaugota!");
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Įvyko klaida!");
                        e.printStackTrace();
                    }
                }
            }
            /*else if(args[0].equalsIgnoreCase( "additem")) {
                if (player.hasPermission(config.perm_addItem)) {
                    config.saveItemStack(player.getItemInHand());
                    config.dropTable.add(player.getItemInHand());
                    player.sendMessage(ChatColor.GREEN + player.getItemInHand().toString() + " pridetas į boso metamų daiktų sąrašą.");
                }
            }*/

            else if(args[0].equalsIgnoreCase( "spawn")) {
                if (player.hasPermission(config.perm_spawnBoss)) {
                    if (bossExists) {
                        player.sendMessage(ChatColor.RED + "Bosas jau egzistuoja!");
                        return true;
                    }
                    stopCountdown();
                    sender.sendMessage(ChatColor.GREEN + "Bosas atsiras už " + (config.time_delayBeforeSpawn-1) + "s.");
                    bossExists = true;
                    canTP = true;
                    new BukkitRunnable() {
                        int currentGoal = config.time_notificationList.get(0);
                        int goalID = 0;
                        int currentTime = config.time_delayBeforeSpawn;
                        @Override
                        public void run() {
                            currentTime--;
                            if (currentTime == currentGoal) {
                                Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.notificationMessage.replace("{TIME}", Integer.toString(currentTime)).replace("{NAME}", config.bossName)));
                                if (goalID == (config.time_notificationList.size() - 1)) {
                                    boss = new Boss(EntityType.BLAZE, config.bossName, config.boss_maxHealth, new Location(Bukkit.getWorld(config.boss_world), config.boss_spawnX, config.boss_spawnY, config.boss_spawnZ), config);
                                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.spawnMessage.replace("{NAME}", config.bossName).replace("{HEALTH}", Double.toString(boss.getHealth()))));
                                    this.cancel();
                                } else {
                                    goalID++;
                                    currentGoal = config.time_notificationList.get(goalID);
                                }
                            }
                        }
                    }.runTaskTimer(this, 0, 20);
                }
            }
            else if(args[0].equalsIgnoreCase( "setplayerspawn")) {
                if (player.hasPermission(config.perm_setPlayerSpawn)) {
                    try {
                        Location location = player.getLocation();
                        config.player_spawnX = location.getX();
                        config.player_spawnY = location.getY();
                        config.player_spawnZ = location.getZ();
                        config.saveConfig();
                        player.sendMessage(ChatColor.GREEN + "Žaidėjų atsiradimo pozicija išsaugota!");
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Įvyko klaida!");
                        e.printStackTrace();
                    }
                }
            }

            else if(args[0].equalsIgnoreCase( "kill")) {
                if(player.isOp() == true){
                    if(bossExists){
                        if(boss != null){
                            boss.forceKill();
                            player.sendMessage(ChatColor.RED + "Bosas sunaikintas");
                        }
                    }
                }
            }

            return true;
        }


        if(cmd.getName().equalsIgnoreCase("bosas")){
            if(player.hasPermission(config.perm_teleport)) {
                if(canTP){
                    player.teleport(new Location(Bukkit.getWorld(config.boss_world), config.player_spawnX, config.player_spawnY, config.player_spawnZ));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.teleportMessage).replace("{NAME}", config.bossName).replace("{TIME}", Integer.toString(config.defaultSpawnTime)));
                }
                else if (bossExists && boss != null) {
                    player.teleport(new Location(Bukkit.getWorld(config.boss_world), config.player_spawnX, config.player_spawnY, config.player_spawnZ));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.teleportMessage).replace("{HEALTH}", boss.getHealth().toString()).replace("{NAME}", config.bossName).replace("{TIME}", Integer.toString(config.defaultSpawnTime)));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.bossNotExist).replace("{NAME}", config.bossName).replace("{TIME}", Integer.toString(config.defaultSpawnTime)));
                }
            }
            return true;
        }




        return false;
    }

}

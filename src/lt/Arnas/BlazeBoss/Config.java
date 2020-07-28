package lt.Arnas.BlazeBoss;

import org.apache.commons.io.IOUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.*;
import java.util.*;

/**
 * Created by User on 2019-01-01.
 */
public class Config {

    public static FileConfiguration config = null;
    //private File configFile;

    public static double boss_spawnX, boss_spawnY, boss_spawnZ;
    public static String boss_world;
    public static double boss_maxHealth;
    public static String bossName = null;

    public static double player_spawnX, player_spawnY, player_spawnZ;
    public static File configFile;

    public static String perm_setPlayerSpawn;
    public static String perm_setBossSpawn;
    public static String perm_spawnBoss;
    public static String perm_addItem;
    public static String perm_teleport;

    public static int time_delayBeforeSpawn;
    public static ArrayList<Integer> time_notificationList = new ArrayList<>();

    public static String notificationMessage;
    public static String spawnMessage;
    public static String teleportMessage;
    public static String bossNotExist;

    public static int time_delayBetweenAttacks;

    public static int fireTicks;
    public static double fireRadius;

    public static int debuffTicks;
    public static double debuffRadius;

    public static double lightningRadius;
    public static double pushbackRadius;

    public static int reminderNotification_delay;
    public static String reminderNotification;

    public static double lightningDamage;
    public static double fireDamage;

    public static int defaultSpawnTime;

    public static int nearbyPushbackRadius;

    private static final Map<String, Enchantment> ENCHANTMENTS = new HashMap();

    public static ArrayList<ItemStack> dropTable = new ArrayList<>();
    public static HashMap<ItemStack, Double> dropRate = new HashMap<>();

    public Config(){
        configFile = new File(Main.plugin.getDataFolder(), "config.yml");
        loadEnchantments();
    }

    public static double minDamage;


    public void loadEnchantments(){
        ENCHANTMENTS.put("alldamage", Enchantment.DAMAGE_ALL);
        ENCHANTMENTS.put("alldmg", Enchantment.DAMAGE_ALL);
        ENCHANTMENTS.put("sharpness", Enchantment.DAMAGE_ALL);
        ENCHANTMENTS.put("sharp", Enchantment.DAMAGE_ALL);
        ENCHANTMENTS.put("dal", Enchantment.DAMAGE_ALL);
        ENCHANTMENTS.put("ardmg", Enchantment.DAMAGE_ARTHROPODS);
        ENCHANTMENTS.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);
        ENCHANTMENTS.put("baneofarthropod", Enchantment.DAMAGE_ARTHROPODS);
        ENCHANTMENTS.put("arthropod", Enchantment.DAMAGE_ARTHROPODS);
        ENCHANTMENTS.put("dar", Enchantment.DAMAGE_ARTHROPODS);
        ENCHANTMENTS.put("undeaddamage", Enchantment.DAMAGE_UNDEAD);
        ENCHANTMENTS.put("smite", Enchantment.DAMAGE_UNDEAD);
        ENCHANTMENTS.put("du", Enchantment.DAMAGE_UNDEAD);
        ENCHANTMENTS.put("digspeed", Enchantment.DIG_SPEED);
        ENCHANTMENTS.put("efficiency", Enchantment.DIG_SPEED);
        ENCHANTMENTS.put("minespeed", Enchantment.DIG_SPEED);
        ENCHANTMENTS.put("cutspeed", Enchantment.DIG_SPEED);
        ENCHANTMENTS.put("ds", Enchantment.DIG_SPEED);
        ENCHANTMENTS.put("eff", Enchantment.DIG_SPEED);
        ENCHANTMENTS.put("durability", Enchantment.DURABILITY);
        ENCHANTMENTS.put("dura", Enchantment.DURABILITY);
        ENCHANTMENTS.put("unbreaking", Enchantment.DURABILITY);
        ENCHANTMENTS.put("d", Enchantment.DURABILITY);
        ENCHANTMENTS.put("thorns", Enchantment.THORNS);
        ENCHANTMENTS.put("highcrit", Enchantment.THORNS);
        ENCHANTMENTS.put("thorn", Enchantment.THORNS);
        ENCHANTMENTS.put("highercrit", Enchantment.THORNS);
        ENCHANTMENTS.put("t", Enchantment.THORNS);
        ENCHANTMENTS.put("fireaspect", Enchantment.FIRE_ASPECT);
        ENCHANTMENTS.put("fire", Enchantment.FIRE_ASPECT);
        ENCHANTMENTS.put("meleefire", Enchantment.FIRE_ASPECT);
        ENCHANTMENTS.put("meleeflame", Enchantment.FIRE_ASPECT);
        ENCHANTMENTS.put("fa", Enchantment.FIRE_ASPECT);
        ENCHANTMENTS.put("knockback", Enchantment.KNOCKBACK);
        ENCHANTMENTS.put("kback", Enchantment.KNOCKBACK);
        ENCHANTMENTS.put("kb", Enchantment.KNOCKBACK);
        ENCHANTMENTS.put("k", Enchantment.KNOCKBACK);
        ENCHANTMENTS.put("blockslootbonus", Enchantment.LOOT_BONUS_BLOCKS);
        ENCHANTMENTS.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        ENCHANTMENTS.put("fort", Enchantment.LOOT_BONUS_BLOCKS);
        ENCHANTMENTS.put("lbb", Enchantment.LOOT_BONUS_BLOCKS);
        ENCHANTMENTS.put("mobslootbonus", Enchantment.LOOT_BONUS_MOBS);
        ENCHANTMENTS.put("mobloot", Enchantment.LOOT_BONUS_MOBS);
        ENCHANTMENTS.put("looting", Enchantment.LOOT_BONUS_MOBS);
        ENCHANTMENTS.put("lbm", Enchantment.LOOT_BONUS_MOBS);
        ENCHANTMENTS.put("oxygen", Enchantment.OXYGEN);
        ENCHANTMENTS.put("respiration", Enchantment.OXYGEN);
        ENCHANTMENTS.put("breathing", Enchantment.OXYGEN);
        ENCHANTMENTS.put("breath", Enchantment.OXYGEN);
        ENCHANTMENTS.put("o", Enchantment.OXYGEN);
        ENCHANTMENTS.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        ENCHANTMENTS.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
        ENCHANTMENTS.put("protect", Enchantment.PROTECTION_ENVIRONMENTAL);
        ENCHANTMENTS.put("p", Enchantment.PROTECTION_ENVIRONMENTAL);
        ENCHANTMENTS.put("explosionsprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS.put("explosionprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS.put("expprot", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS.put("blastprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS.put("bprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS.put("bprotect", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS.put("blastprotect", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS.put("pe", Enchantment.PROTECTION_EXPLOSIONS);
        ENCHANTMENTS.put("fallprotection", Enchantment.PROTECTION_FALL);
        ENCHANTMENTS.put("fallprot", Enchantment.PROTECTION_FALL);
        ENCHANTMENTS.put("featherfall", Enchantment.PROTECTION_FALL);
        ENCHANTMENTS.put("featherfalling", Enchantment.PROTECTION_FALL);
        ENCHANTMENTS.put("pfa", Enchantment.PROTECTION_FALL);
        ENCHANTMENTS.put("fireprotection", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS.put("flameprotection", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS.put("fireprotect", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS.put("flameprotect", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS.put("fireprot", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS.put("flameprot", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS.put("pf", Enchantment.PROTECTION_FIRE);
        ENCHANTMENTS.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
        ENCHANTMENTS.put("projprot", Enchantment.PROTECTION_PROJECTILE);
        ENCHANTMENTS.put("pp", Enchantment.PROTECTION_PROJECTILE);
        ENCHANTMENTS.put("silktouch", Enchantment.SILK_TOUCH);
        ENCHANTMENTS.put("softtouch", Enchantment.SILK_TOUCH);
        ENCHANTMENTS.put("st", Enchantment.SILK_TOUCH);
        ENCHANTMENTS.put("waterworker", Enchantment.WATER_WORKER);
        ENCHANTMENTS.put("aquaaffinity", Enchantment.WATER_WORKER);
        ENCHANTMENTS.put("watermine", Enchantment.WATER_WORKER);
        ENCHANTMENTS.put("ww", Enchantment.WATER_WORKER);
        ENCHANTMENTS.put("firearrow", Enchantment.ARROW_FIRE);
        ENCHANTMENTS.put("flame", Enchantment.ARROW_FIRE);
        ENCHANTMENTS.put("flamearrow", Enchantment.ARROW_FIRE);
        ENCHANTMENTS.put("af", Enchantment.ARROW_FIRE);
        ENCHANTMENTS.put("arrowdamage", Enchantment.ARROW_DAMAGE);
        ENCHANTMENTS.put("power", Enchantment.ARROW_DAMAGE);
        ENCHANTMENTS.put("arrowpower", Enchantment.ARROW_DAMAGE);
        ENCHANTMENTS.put("ad", Enchantment.ARROW_DAMAGE);
        ENCHANTMENTS.put("arrowknockback", Enchantment.ARROW_KNOCKBACK);
        ENCHANTMENTS.put("arrowkb", Enchantment.ARROW_KNOCKBACK);
        ENCHANTMENTS.put("punch", Enchantment.ARROW_KNOCKBACK);
        ENCHANTMENTS.put("arrowpunch", Enchantment.ARROW_KNOCKBACK);
        ENCHANTMENTS.put("ak", Enchantment.ARROW_KNOCKBACK);
        ENCHANTMENTS.put("infinitearrows", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS.put("infarrows", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS.put("infinity", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS.put("infinite", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS.put("unlimited", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS.put("unlimitedarrows", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS.put("ai", Enchantment.ARROW_INFINITE);
        ENCHANTMENTS.put("luck", Enchantment.LUCK);
        ENCHANTMENTS.put("luckofsea", Enchantment.LUCK);
        ENCHANTMENTS.put("luckofseas", Enchantment.LUCK);
        ENCHANTMENTS.put("rodluck", Enchantment.LUCK);
        ENCHANTMENTS.put("lure", Enchantment.LURE);
        ENCHANTMENTS.put("rodlure", Enchantment.LURE);
    }

    public void loadDefaultConfig() {
        if (configFile.exists()) return;
        Main.plugin.saveDefaultConfig();
    }

    public void saveConfig(){
        Main.plugin.getConfig().set("Boss.Spawn.World", boss_world);
        Main.plugin.getConfig().set("Boss.Spawn.X", boss_spawnX);
        Main.plugin.getConfig().set("Boss.Spawn.Y", boss_spawnY);
        Main.plugin.getConfig().set("Boss.Spawn.Z", boss_spawnZ);

        Main.plugin.getConfig().set("Player.Spawn.X", player_spawnX);
        Main.plugin.getConfig().set("Player.Spawn.Y", player_spawnY);
        Main.plugin.getConfig().set("Player.Spawn.Z", player_spawnZ);
        Main.plugin.saveConfig();
    }

    public void saveItemStack(ItemStack itemStack){
        Main.plugin.getConfig().set("Lootable." + Integer.toString(dropTable.size()), itemStack);
        saveConfig();
    }

    public void readConfig(){
        boss_world = Main.plugin.getConfig().getString("Boss.Spawn.World");
        boss_spawnX = Main.plugin.getConfig().getDouble("Boss.Spawn.X");
        boss_spawnY = Main.plugin.getConfig().getDouble("Boss.Spawn.Y");
        boss_spawnZ = Main.plugin.getConfig().getDouble("Boss.Spawn.Z");
        bossName = Main.plugin.getConfig().getString("Boss.Spawn.Name");
        boss_maxHealth = Main.plugin.getConfig().getDouble("Boss.MaxHealth");

        player_spawnX = Main.plugin.getConfig().getDouble("Player.Spawn.X");
        player_spawnY = Main.plugin.getConfig().getDouble("Player.Spawn.Y");
        player_spawnZ = Main.plugin.getConfig().getDouble("Player.Spawn.Z");

        perm_setPlayerSpawn = Main.plugin.getConfig().getString("Permissions.SetPlayerSpawn");
        perm_setBossSpawn = Main.plugin.getConfig().getString("Permissions.SetBossSpawn");
        perm_spawnBoss = Main.plugin.getConfig().getString("Permissions.SpawnBoss");
        perm_addItem = Main.plugin.getConfig().getString("Permissions.AddItem");
        perm_teleport = Main.plugin.getConfig().getString("Permissions.Teleport");

        time_delayBeforeSpawn = Main.plugin.getConfig().getInt("Time.DelayBeforeSpawn");
        time_delayBetweenAttacks = Main.plugin.getConfig().getInt("Time.DelayBetweenAttacks");

        String time_notifications = Main.plugin.getConfig().getString("Time.Notifications");
        for(String string : time_notifications.split(";")){
            time_notificationList.add(Integer.parseInt(string));
        }

        notificationMessage = Main.plugin.getConfig().getString("Messages.NotificationMessage");
        spawnMessage = Main.plugin.getConfig().getString("Messages.SpawnMessage");
        teleportMessage = Main.plugin.getConfig().getString("Messages.TeleportMesage");
        bossNotExist = Main.plugin.getConfig().getString("Messages.BossDoesntExist");
        reminderNotification = Main.plugin.getConfig().getString("Messages.ReminderNotification");

        fireTicks = Main.plugin.getConfig().getInt("Attacks.Fire.Length")*20;
        fireRadius = Main.plugin.getConfig().getDouble("Attacks.Fire.Radius");

        debuffTicks = Main.plugin.getConfig().getInt("Attacks.Debuff.Length")*20;
        debuffRadius = Main.plugin.getConfig().getDouble("Attacks.Debuff.Radius");

        lightningRadius = Main.plugin.getConfig().getDouble("Attacks.Lightning.Radius");
        pushbackRadius = Main.plugin.getConfig().getDouble("Attacks.Pushback.Radius");

        minDamage = Main.plugin.getConfig().getDouble("Boss.minDamage");
        reminderNotification_delay = Main.plugin.getConfig().getInt("Time.ReminderNotification");
        defaultSpawnTime = Main.plugin.getConfig().getInt("Time.defaultSpawnTime");
        defaultSpawnTime++;
        time_delayBeforeSpawn++;

        fireDamage = Main.plugin.getConfig().getDouble("Damage.Fire");
        lightningDamage = Main.plugin.getConfig().getDouble("Damage.Lightning");

        nearbyPushbackRadius = Main.plugin.getConfig().getInt("Boss.PushbackRadius");

        dropRate.clear();
        dropTable.clear();
        try{
            List<?> itemList = Main.plugin.getConfig().getList("Lootable");
            for(Object obj : itemList){
                String str = obj.toString();
                String[] itemData = str.split(" ");
                ItemStack itemStack;
                if(itemData[0].contains(":")){
                    String[] split = itemData[0].split(":");
                    itemStack = new ItemStack(Material.getMaterial(Integer.parseInt(split[0])), Integer.parseInt(itemData[1]), Short.parseShort(split[1]));
                } else{
                    itemStack = new ItemStack(Material.getMaterial(Integer.parseInt(itemData[0])), Integer.parseInt(itemData[1]));
                }

                double dropChance = -1f;

                for(String itemInfo : itemData){
                    if(itemInfo != itemData[0] && itemInfo != itemData[1]){ // jeigu nera panaudoti itemo id ir kiekis
                        if(itemInfo.contains("name")){
                            String[] split = itemInfo.split(":");
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', split[1]).replace("_", " "));
                            itemStack.setItemMeta(itemMeta);
                        }
                        else if(itemInfo.contains("lore")){
                            String[] split = itemInfo.split(":");
                            System.out.print(split[1]);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            String[] lore = (ChatColor.translateAlternateColorCodes('&', split[1]).replace("_", " ")).split("\\|");
                            List<String> apply = Arrays.asList(lore);
                            itemMeta.setLore(apply);
                            itemStack.setItemMeta(itemMeta);
                        } else if(itemInfo.contains("droprate")){
                            String[] split = itemInfo.split(":");
                            dropChance = Double.parseDouble(split[1]);
                        } else if(itemInfo.contains("player")){
                            String[] split = itemInfo.split(":");
                            SkullMeta meta = (SkullMeta)itemStack.getItemMeta();
                            meta.setOwner(split[1]);
                            itemStack.setItemMeta(meta);
                        }
                        else {
                            String[] split = itemInfo.split(":");
                            itemStack.addUnsafeEnchantment(ENCHANTMENTS.get(split[0]), Integer.parseInt(split[1]));
                        }
                    }
                }
                dropTable.add(itemStack);
                if(dropChance != -1f){
                    dropChance /= 100;
                    dropRate.put(itemStack, dropChance);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            //System.out.print("Boso dropai nenustatyti!");
        }
    }

}

package anCot.Monster.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import anCot.Monster.main;

public class startGame implements CommandExecutor {
	
	public Player monster;
	
	public int players;
	
	public int borderSize;
	
	public boolean gameStarted = false;
	
	public ItemStack[] items = {new ItemStack(Material.ENDER_PEARL,64)};
	
	public int currentMonsterHealth = 20;
	
	public ArrayList<Player> p;
	
	public main mRef;
	
	public HashMap<String,Location> prevLoc;
	
	public int monstDamage = 1;
	
	public startGame(main mRef) {
		this.mRef = mRef;
		prevLoc = new HashMap<String,Location>();
	}
	
	
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			if(args.length > 0 && !gameStarted) {
				borderSize = Integer.parseInt(args[0]);
				Start();
				gameStarted = true;
				

			}
			else {
				System.out.println("Must specify world size! Or game has already started!");
			}
		}
		else {
			System.out.println("Must be a player to run this command!");
		}
		
		return true;
	}
	
	public void Start() {
		
		int random = new Random().nextInt(Bukkit.getOnlinePlayers().size());
		
		p = new ArrayList<Player>(Bukkit.getServer().getOnlinePlayers());
		
		monster =  p.get(random);
		
		players = Bukkit.getOnlinePlayers().size() - 1;
		
		Bukkit.getServer().getWorld("default").getWorldBorder().setCenter(new Location(Bukkit.getServer().getWorld("default"),0,0,0));
		Bukkit.getServer().getWorld("default").getWorldBorder().setSize(borderSize);
		
		for(Player q : p) {
			
			int randomx = new Random().nextInt(borderSize) - borderSize/2;
			
			int randomz =  new Random().nextInt(borderSize) - borderSize/2;
			
			q.teleport(new Location(Bukkit.getServer().getWorld("default"),randomx,150,randomz));
			
			q.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,500,1));
			
			q.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,500,1));
			
	        AttributeInstance pattribute = q.getAttribute(Attribute.GENERIC_MAX_HEALTH);
	        
	        pattribute.setBaseValue(20);
	        
			prevLoc.put(q.getName(),q.getLocation());
			
			pattribute = monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            
            pattribute.setBaseValue(1);
            
			
			
		}
		
		monster.getInventory().addItem(items);
		
		monster.sendMessage("YOU ARE THE MONSTER! EVERY PLAYER YOU KILL MAKES YOU STRONGER");
		
        AttributeInstance mattribute = monster.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        
        mattribute.setBaseValue(20);
        
		mattribute = monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        
        mattribute.setBaseValue(monstDamage);
        
        wallTimer();
        
        monstTimers();
		
	}
	
	public void wallTimer() {
		
		//handles all of the scheduled events such as the town hall meetings!
		
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        
        scheduler.scheduleSyncRepeatingTask(mRef, new Runnable() {
            @Override
            public void run() {
            	
            	Bukkit.getServer().broadcastMessage("TOWNHALL MEETING BEGIN! YOU HAVE 2 MINUTES!");
            	
            	Bukkit.getServer().getWorld("default").getWorldBorder().setSize(40);
            	
        		for(Player q : p) {

        			prevLoc.replace(q.getName(),q.getLocation());
        			
        			q.teleport(new Location(Bukkit.getServer().getWorld("default"),0,150,0));
        			
        			q.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,500,1));
        			
        			q.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,500,1));
        			
        			
        			
        		}
        		
        		
                scheduler.scheduleSyncDelayedTask(mRef, new Runnable() {
                    @Override
                    public void run() {
                    	Bukkit.getServer().broadcastMessage("TOWN HALL MEETING OVER!");
                    	
                		for(Player q : p) {
                			
                			q.teleport(prevLoc.get(q.getName()));

                		}
                		
                    	Bukkit.getServer().getWorld("default").getWorldBorder().setSize(borderSize);

                    }
                },2400L);
            	
            }
        },6000L, 8400L);
        
        scheduler.scheduleSyncRepeatingTask(mRef, new Runnable() {
            @Override
            public void run() {
            	Bukkit.getServer().broadcastMessage("1 MINUTE UNTIL TOWN HALL MEETING!PREPARE TO BE TRANSPORTED!");

            }
        },4800L, 8400L);
        
        
	}
	
	public void monstTimers() {
		
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        
        scheduler.scheduleSyncRepeatingTask(mRef, new Runnable() {
            @Override
            public void run() {
            	ItemStack item = new ItemStack(Material.STICK,1);
            	
            	ItemMeta meta = item.getItemMeta();
            	
            	meta.setDisplayName("PLAYER FINDER");
            	
            	item.setItemMeta(meta);
            	
            	ItemStack playerFinder[] = {item};
            	
            	monster.getInventory().addItem(playerFinder);

            }
        },1200L, 1200L);
	}
	
}

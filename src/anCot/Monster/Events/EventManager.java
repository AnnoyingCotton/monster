package anCot.Monster.Events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import anCot.Monster.Commands.startGame;

public class EventManager implements Listener {
	
	public startGame gameManager;
	
	public int applesEaten = 0;
	
	public int maxApples = 5;
	
	public EventManager(startGame gameManager) {
		this.gameManager = gameManager;
	}

	@EventHandler
	public void playerDeath(PlayerDeathEvent player) {
		
		if(gameManager.gameStarted) {
			
			if(player.getEntity().getName() != gameManager.monster.getName()) {
				
				gameManager.players--;
				
				player.getEntity().setGameMode(GameMode.SPECTATOR);
			}
			
			if(player.getEntity().getKiller() == gameManager.monster) {
				
				gameManager.monster.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,200,1));
				
				gameManager.monster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,200,2));
				
				gameManager.currentMonsterHealth += 2;
				
	            AttributeInstance attribute = gameManager.monster.getAttribute(Attribute.GENERIC_MAX_HEALTH);
	            
	            attribute.setBaseValue(gameManager.currentMonsterHealth);
				
				
			}
			
			if(gameManager.players == 0) {
				Bukkit.getServer().broadcastMessage("GAME IS OVER! MONSTER WINS!");
			}
			else if(player.getEntity().getName() == gameManager.monster.getName()) {
				
				Bukkit.getServer().broadcastMessage("GAME IS OVER! SURVIVORS WIN!");
				
			}
			
			gameManager.p.remove(gameManager.p.indexOf(player.getEntity().getPlayer()));
		}
		
		
	}
	
	@EventHandler
	public void itemUse(PlayerInteractEvent player) {
		if(player.getAction() == Action.RIGHT_CLICK_AIR && gameManager.gameStarted) {
			
			if(player.getPlayer() == gameManager.monster && player.getItem().getType() == Material.GOLDEN_APPLE && applesEaten != maxApples) {
				
	            AttributeInstance attribute = gameManager.monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
	            
	            applesEaten++;
	            
	            gameManager.monstDamage++;
	            
	            attribute.setBaseValue(gameManager.monstDamage);
	            
	            int amount = player.getItem().getAmount();
	            
	            amount--;
	            if(amount != 0) {
	                player.getPlayer().getInventory().setItem(player.getPlayer().getInventory().getHeldItemSlot(),new ItemStack(player.getItem().getType(),amount)); 
	            }
	            else {
	                player.getPlayer().getInventory().setItem(player.getPlayer().getInventory().getHeldItemSlot(),null); 
	            }
	            

			}
			else if(player.getPlayer() == gameManager.monster && player.getItem().getType() == Material.GOLDEN_APPLE) {
				gameManager.monster.sendMessage("Max apples eaten reached");
			}
			
		  if(player.getPlayer() == gameManager.monster && player.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("PLAYER FINDER") && player.getItem().getType() == Material.STICK) {
			  
			  gameManager.monster.sendMessage("--LOCATING PLAYER--");
			  
			  double minDistance = 10000;
			  
			  Player closestPlayer = null;
			  
			  Location monstLoc = gameManager.monster.getLocation();
			  
			  for(Player q: gameManager.p) {
				  
				  Location playerLoc = q.getLocation();
				  
				  if(monstLoc.distance(playerLoc) < minDistance && q != gameManager.monster){
					  
					  minDistance = monstLoc.distance(playerLoc);
					  
					  closestPlayer = q;
					  
				  }
				  
			  }
			  	  
			  gameManager.monster.sendMessage("NEAREST PLAYER: " + closestPlayer.getLocation().getBlockX() + " " + closestPlayer.getLocation().getBlockY() + " " + closestPlayer.getLocation().getBlockZ());
			  
			  gameManager.monster.getInventory().setItem(player.getPlayer().getInventory().getHeldItemSlot(),null); 
			  
		  }
			
		}
		
	}
	
	@EventHandler
	public void leaveGame(PlayerQuitEvent player) {
		
		if(player.getPlayer() == gameManager.monster) {
			
			Bukkit.getServer().broadcastMessage("GAME IS OVER! SURVIVORS WIN!");
			
		}
		else {
			player.getPlayer().setGameMode(GameMode.SPECTATOR);
			gameManager.players--;
			
			if(gameManager.players == 0) {
				Bukkit.getServer().broadcastMessage("GAME IS OVER! MONSTER WINS!");
			}
		}
	}
	

}

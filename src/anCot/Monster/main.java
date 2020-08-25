package anCot.Monster;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import anCot.Monster.Commands.startGame;
import anCot.Monster.Events.EventManager;

public class main extends JavaPlugin {
	
	public startGame gameHandler = new startGame(this);

	public void onEnable() {
		
		this.getCommand("startgame").setExecutor((CommandExecutor) gameHandler);
		
		System.out.println("MONSTER LOADED!");
		
		getServer().getPluginManager().registerEvents(new EventManager(gameHandler), this);
		
	}
	
	public void onDisable() {
		
	}
}

package us.corenetwork.core.respawn;

import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import us.corenetwork.core.util.PlayerUtils;

public class RespawnTeamManager {

	private LinkedList<Player> respawnTeam;
	private RespawnCountdown countdown;
	
	Location respawnLocation;
	Player respawnLocationSource;
	
	private CommandTeamDisplay commDisplay;
	
	public RespawnTeamManager()
	{
		respawnTeam = new LinkedList<Player>();
		commDisplay = new CommandTeamDisplay();
		countdown = new RespawnCountdown();
	}
	
	/**
	 * Adds player to respawnTeam list. Starts the countdown when first player is added. 
	 * Initializes the respawn location when first player is added.
	 * @param player - the player to be added to the list 
	 */
	public void addToTeam(Player player)
	{
		//Start the countdown when adding the first player
		if(respawnTeam.size() == 0)
		{
			initializeLocation(player);
		}
		countdown.start();
		respawnTeam.add(player);
		commDisplay.set(respawnTeam);
	}
	
	
	
	/**
	 * Removes player from respawnTeam list, stopping the countdown if last player is removed.
	 * @param player - the player to be removed from the list
	 */
	public void removeFromTeam(Player player)
	{
		//Stop the countdown when removing last player from the list
		respawnTeam.remove(player);

		commDisplay.set(respawnTeam);
		if(respawnTeam.size() == 0)
		{
			countdown.stop();
		}
		else
		{
			checkLocation(player);
		}
	}
	
	public void playerSpawned(Player player)
	{
		//Stop the countdown when removing last player from the list
		respawnTeam.remove(player);
		commDisplay.set(respawnTeam);
		
		if(respawnTeam.size() == 0)
		{
			countdown.stop();
		}
	}
	
	private void initializeLocation(Player player)
	{
		respawnLocation = RespawnModule.manager.getRespawnLocation(player);
		respawnLocationSource = player;
	}
	
	
	
	/**
	 * Checks if the spawn location needs to be generated again, and does it if necessary
	 * @param player - player that left the team
	 */
	private void checkLocation(Player player)
	{
		if(respawnLocationSource.equals(player) && respawnTeam.size() > 0)
		{
			initializeLocation(respawnTeam.getFirst());
		}
	}
	
	public void clear()
	{
		for(Player player : respawnTeam)
		{
			PlayerUtils.Message(RespawnSettings.MESSAGE_GROUP_REMAIN.string(), player);
		}
		commDisplay.clear();
		respawnTeam.clear();
	}

	public LinkedList<Player> getTeam()
	{
		return respawnTeam;
	}

	public boolean isInTeam(Player player)
	{
		return respawnTeam.contains(player);
	}

	public Location getTeamRespawnLocation()
	{
		return respawnLocation;
	}

	
}

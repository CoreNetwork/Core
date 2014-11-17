package us.corenetwork.core.map;

import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import us.corenetwork.core.CLog;

public class ClearOrbTask implements Runnable {

	@Override
	public void run()
	{
		Collection<ExperienceOrb> coll = Bukkit.getWorld("world_the_end").getEntitiesByClass(ExperienceOrb.class);
		//CLog.debug(coll.size()+"");
		
		double halfOfTheSize = CheckpointsSettings.ORB_CLEARING_BOX_SIZE.integer() / 2.0;
		boolean deleteThisOne;
		for(ExperienceOrb orb : coll)
		{
			deleteThisOne = true;
			for(Entity e :orb.getNearbyEntities(halfOfTheSize, halfOfTheSize, halfOfTheSize))
			{
				if (e instanceof Player)
				{
					deleteThisOne = false;
					break;
				}
			}
			if(deleteThisOne)
			{
				orb.remove();
			}
		}
	}

}

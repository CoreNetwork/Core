package us.corenetwork.core.respawn;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.security.auth.login.Configuration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ginaf on 2015-01-12.
 */
public class LuckyBoosterManager {

    private final String AMOUNT = "amount.";
    private final String RUNNING = "running";

    private final String START_TIME = "startTime";
    private final String END_TIME = "endTime";
    private final String UUID = "uuid";
    private final String NAME = "name";

    public void addPass(OfflinePlayer offlinePlayer)
    {
        setAmount(offlinePlayer, getAmount(offlinePlayer) + 1);
    }

    public void removePass(OfflinePlayer offlinePlayer)
    {
        setAmount(offlinePlayer, getAmount(offlinePlayer) - 1);
    }

    private String getAmountPath(OfflinePlayer offlinePlayer)
    {
        return AMOUNT+offlinePlayer.getUniqueId().toString();
    }

    public int getAmount(OfflinePlayer offlinePlayer)
    {
        return RespawnModule.instance.storageConfig.getInt(getAmountPath(offlinePlayer));
    }

    private void setAmount(OfflinePlayer offlinePlayer, int amount)
    {
        RespawnModule.instance.storageConfig.set(getAmountPath(offlinePlayer), amount);
        RespawnModule.instance.saveStorageYaml();
    }

    public void runPass(Player player)
    {
        long startTime = System.currentTimeMillis();
        //if last pass in storage ends past the currentTimeMillis, move start
        LuckyPass lastPass = getLast();
        if(lastPass != null && lastPass.endTime > startTime)
            startTime = lastPass.endTime;
        long endTime = startTime + getDurationInMilis();
        String uuid = player.getUniqueId().toString();
        String name = player.getName();
        saveRun(new LuckyPass(startTime,endTime,uuid,name));
    }

    private void saveRun(LuckyPass pass)
    {
        List<Map<?,?>> listOfRunning = getListOfMaps();
        listOfRunning.add(pass.getMap());
        RespawnModule.instance.storageConfig.set(RUNNING, listOfRunning);
        RespawnModule.instance.saveStorageYaml();
    }

    private long getDurationInMilis()
    {
        return RespawnSettings.LUCKY_BOOSTER_DURATION_MINUTES.integer()*60*1000;
    }

    public boolean isActive()
    {
        return getFirstRunning() != null;
    }


    public LuckyPass getFirstRunning()
    {
        List<Map<?,?>> listOfRunning = getListOfMaps();
        for(Map<?,?> runRecord : listOfRunning)
        {
            Object val = runRecord.get(END_TIME);
            long endTime = 0;
            if(val instanceof Integer)
                endTime = (Integer)val;
            if(val instanceof Long)
                endTime = (Long)val;
            if(endTime >= System.currentTimeMillis())
            {
                return new LuckyPass(runRecord);
            }
        }
        return null;
    }

    private LuckyPass getLast()
    {
        List<Map<?,?>> listOfRunning = getListOfMaps();
        if(listOfRunning.size() == 0)
            return null;

        Map<?,?> last = listOfRunning.get(listOfRunning.size()-1);
        return new LuckyPass(last);
    }

    private List<Map<?,?>> getListOfMaps()
    {
        List<Map<?,?>> listOfRunning = RespawnModule.instance.storageConfig.getMapList(RUNNING);
        return listOfRunning != null ? listOfRunning : new ArrayList<Map<?, ?>>();
    }

    public void removeExpired()
    {
        List<Map<?,?>> listOfRunning = getListOfMaps();
        Iterator it = listOfRunning.iterator();
        while(it.hasNext())
        {
            Map<?,?> map = (Map<?, ?>) it.next();
            if((Long) map.get(END_TIME) < System.currentTimeMillis())
                it.remove();
        }
        RespawnModule.instance.storageConfig.set(RUNNING, listOfRunning);
        RespawnModule.instance.saveStorageYaml();
    }

    public String getLuckyActiveMessage()
    {
        LuckyPass activePass = getFirstRunning();

        long timeLeftMilis = activePass.endTime - System.currentTimeMillis();
        String message = RespawnSettings.MESSAGE_SPAWN_LUCKY_WITH_BOOSTER.string().replace("<Player>", activePass.name);

        String timeMessage;
        long hours = timeLeftMilis /1000/60/60;
        long minutes = (timeLeftMilis /1000/60) % 60;
        timeMessage = RespawnSettings.MESSAGE_TIME_SYNTAX.string();
        timeMessage = timeMessage.replace("<Hours>", hours+"");
        timeMessage = timeMessage.replace("<Minutes>", minutes+"");

        message = message.replace("<Time>", timeMessage);
        return message;
    }


    class LuckyPass
    {
        long startTime;
        long endTime;
        String uuid;
        String name;
        public LuckyPass(long startTime, long endTime, String uuid, String name)
        {
            this.startTime = startTime;
            this.endTime = endTime;
            this.uuid = uuid;
            this.name = name;
        }

        public LuckyPass(Map <?, ?> map)
        {
            this.startTime = (Long) map.get(START_TIME);
            this.endTime = (Long) map.get(END_TIME);
            this.uuid = (String) map.get(UUID);
            this.name = (String) map.get(NAME);
        }

        public Map<String, Object> getMap()
        {
            HashMap<String, Object> toSave = new HashMap<String, Object>();
            toSave.put(START_TIME, startTime);
            toSave.put(END_TIME, endTime);
            toSave.put(UUID, uuid);
            toSave.put(NAME, name);
            return toSave;
        }
    }

}

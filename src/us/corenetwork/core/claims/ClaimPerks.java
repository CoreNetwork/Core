package us.corenetwork.core.claims;

import me.ryanhamshire.GriefPrevention.Claim;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.minecraft.server.v1_8_R2.NBTTagList;
import net.minecraft.server.v1_8_R2.TileEntityBanner;
import net.minecraft.server.v1_8_R2.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.block.CraftBlock;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import us.corenetwork.core.CLog;

/**
 * Created by Ginaf on 2015-01-18.
 */
public class ClaimPerks {

    public void onRemoveClaim(Claim claim) {
        Location start = claim.getLesserBoundaryCorner();
        Location end = claim.getGreaterBoundaryCorner();

        for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
            for (int y = 0; y <= 255; y++) {
                for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                    Block block = start.getWorld().getBlockAt(x, y, z);
                     if(isPerkBlock(block))
                        block.setType(Material.AIR);
                }
            }
        }


        Chunk startChunk = start.getChunk();
        Chunk endChunk = end.getChunk();

        World world = start.getWorld();

        for (int x = startChunk.getX(); x <= endChunk.getX(); x++) {
            for (int z = startChunk.getZ(); z <= endChunk.getZ(); z++) {
                Chunk chunk = world.getChunkAt(x,z);
                for(Entity e : chunk.getEntities())
                {
                    if(e.getType() == EntityType.ARMOR_STAND)
                    {
                        int xLoc = e.getLocation().getBlockX();
                        int zLoc = e.getLocation().getBlockZ();
                        if( xLoc >= start.getBlockX() && xLoc <= end.getBlockX() && zLoc >= start.getBlockZ() && zLoc <= end.getBlockZ())
                        {
                            ArmorStand as = (ArmorStand) e;
                            if(as.hasArms())
                            {
                                e.remove();
                            }
                        }
                    }
                }
            }
        }
    }


    public boolean isPerkBlock(Block block)
    {

        if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
        {
            Sign sign = (Sign) block.getState();

            return doesSignHaveColors(sign);
        }
        else if (block.getType() == Material.ARMOR_STAND)
        {
            CLog.debug("woot");
        }
        else if (block.getType() == Material.STANDING_BANNER || block.getType() == Material.WALL_BANNER)
        {
            CraftBlock craftBlock = (CraftBlock) block;
            WorldServer nmsWorld = ((CraftWorld) block.getWorld()).getHandle();
            TileEntityBanner bannerTileEntity = (TileEntityBanner) nmsWorld.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));

            NBTTagList patterns = bannerTileEntity.patterns;
            return doesPatternListContainPerkPatterns(patterns);
        }
        else if (block.getType() == Material.SKULL)
        {

            Skull skull = (Skull) block.getState();
            if(skull.getSkullType() == SkullType.PLAYER)
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Convenience method to quickly check if sign has any colored text in it
     * @param sign Sign to check
     * @return true if sign has any colored text.
     */
    public boolean doesSignHaveColors(Sign sign)
    {
        String colorSymbol = "\u00A7";
        for (String line : sign.getLines())
        {
            if (line.contains(colorSymbol))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Check if list of patterns contains patterns reserved for subscribers
     * @param patterns NBTTagList containing string patterns
     */
    public boolean doesPatternListContainPerkPatterns(NBTTagList patterns)
    {
        for (int i = 0; i < patterns.size(); i++)
        {
            NBTTagCompound pattern = patterns.get(i);
            String patternType = pattern.getString("Pattern");

            //cbo = Curly border, bri = Bricks
            if ("cbo".equals(patternType) || "bri".equals(patternType))
                return true;
        }

        return false;
    }
}

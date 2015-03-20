package us.corenetwork.core.claims;

import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class ResizeClaimWorker extends BlockWorker {
    private final Location bStart;
    private final Location bEnd;
    private final Claim claim;
    private World world;
    private ClaimFluids.Range lava, water;
    private int x, y, z;
    private Chunk startChunk;
    private Chunk endChunk;
    private ClaimCache claimCache;

    boolean blocksDone = false;

    public ResizeClaimWorker(Claim claim, Location bStart, Location bEnd) {
        this.claim = claim;
        this.bStart = bStart;
        this.bEnd = bEnd;
        this.claimCache = new ClaimCache(bStart, bEnd);
    }

    @Override
    public void init() {
        ClaimsModule.instance.untouchableClaims.add(claimCache);
        world = claim.getLesserBoundaryCorner().getWorld();
        lava = ClaimsModule.instance.claimFluids.getRange(Material.LAVA, world.getName());
        water = ClaimsModule.instance.claimFluids.getRange(Material.WATER, world.getName());
        x = bStart.getBlockX();
        y = 0;
        z = bStart.getBlockZ();


        startChunk = bStart.getChunk();
        endChunk = bEnd.getChunk();
    }

    @Override
    public void onDone() {
        ClaimsModule.instance.untouchableClaims.remove(claimCache);
    }

    @Override
    public long getTaskSize() {
        return (bEnd.getBlockX() - bStart.getBlockX() + 1) * (bEnd.getBlockZ() - bStart.getBlockZ() + 1) * 256 +
                (endChunk.getX() - startChunk.getX() + 1) * (endChunk.getZ() - startChunk.getZ() + 1);
    }

    @Override
    public long work(long items) {
        int worked = 0;
        boolean begin = true;

        if (!blocksDone) {
            for (; x <= bEnd.getBlockX(); x++) {
                if (!begin) {
                    y = 0;
                }
                for (; y <= 255; y++) {
                    if (!begin) {
                        z = bStart.getBlockZ();
                    }
                    for (; z <= bEnd.getBlockZ(); z++) {
                        worked++;
                        begin = false;
                        Block block = world.getBlockAt(x, y, z);
                        if (claim.contains(block.getLocation(), false, false)) {
                            continue;
                        }
                        Material mat = ClaimFluids.getLiquidType(block.getType());
                        if (mat == Material.WATER && (water == null || (y < water.getMin() || y > water.getMax()))) {
                            block.setType(Material.AIR);
                        }
                        if (mat == Material.LAVA && (lava == null || (y < lava.getMin() || y > lava.getMax()))) {
                            block.setType(Material.AIR);
                        }

                        if (ClaimsModule.instance.claimPerks.isPerkBlock(block)) {
                            block.setType(Material.AIR);
                        }

                        if (worked >= items) {
                            return worked;
                        }
                    }
                }
            }

            blocksDone = true;

            x = startChunk.getX();
            z = startChunk.getZ();

        }
        begin = true;

        for (; x <= endChunk.getX(); x++) {
            if (!begin) {
                z = startChunk.getZ();
            }
            for (; z <= endChunk.getZ(); z++) {
                worked ++;
                begin = false;
                Chunk chunk = world.getChunkAt(x, z);
                for (Entity e : chunk.getEntities()) {
                    if (e.getType() == EntityType.ARMOR_STAND) {
                        int xLoc = e.getLocation().getBlockX();
                        int zLoc = e.getLocation().getBlockZ();

                        //is in original claim
                        if (xLoc >= bStart.getBlockX() && xLoc <= bEnd.getBlockX() && zLoc >= bStart.getBlockZ() && zLoc <= bEnd.getBlockZ()) {
                            Location current = e.getLocation();
                            //is in new claim
                            if (!claim.contains(current, true, false)) {
                                ArmorStand as = (ArmorStand) e;
                                if (as.hasArms()) {
                                    e.remove();
                                }
                            }

                        }
                    }
                }

                if (worked >= items) {
                    return worked;
                }
            }
        }
        return worked;
    }
}

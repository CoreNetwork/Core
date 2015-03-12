package us.corenetwork.core.claims;

import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ClearFluidsInClaimWorker extends BlockWorker {
    private Claim toRemove;
    private Location start;
    private Location end;
    private ClaimFluids.Range lava;
    private ClaimFluids.Range water;
    private int x;
    private int y;
    private int z;
    private Player player;
    private Runnable finish;

    public ClearFluidsInClaimWorker(Claim toRemove, Player player, Runnable finish) {
        this.toRemove = toRemove;
        this.player = player;
        this.finish = finish;
    }

    @Override
    public void init() {
        ClaimsModule.instance.untouchableClaims.add(toRemove);
        start = toRemove.getLesserBoundaryCorner();
        end = toRemove.getGreaterBoundaryCorner();

        String world = start.getWorld().getName();
        lava = ClaimsModule.instance.claimFluids.getRange(Material.LAVA, world);
        water = ClaimsModule.instance.claimFluids.getRange(Material.WATER, world);
        x = start.getBlockX();
        y = 255;
        z = start.getBlockZ();
    }

    @Override
    public void onDone() {
        ClaimsModule.instance.untouchableClaims.remove(toRemove);
        finish.run();
    }

    @Override
    public long getTaskSize() {
        return Math.abs(end.getBlockX() - start.getBlockX()) * Math.abs(end.getBlockZ() - start.getBlockZ()) * 256;
    }

    @Override
    public long work(long items) {
        boolean first = true;

        long worked = 0;

        outer: for (; y >= 0; y--) {
            if (!first) {
                x = start.getBlockX();
            }
            for (; x <= end.getBlockX(); x++) {
                if (!first) {
                    z = start.getBlockZ();
                }
                for (; z <= end.getBlockZ(); z++) {
                    first = false;
                    worked++;
                    Block block = start.getWorld().getBlockAt(x, y, z);
                    Material mat = ClaimFluids.getLiquidType(block.getType());
                    if (mat == Material.WATER && (water == null || (y < water.getMin() || y > water.getMax()))) {
                        block.setType(Material.AIR);
                    }
                    if (mat == Material.LAVA && (lava == null || (y < lava.getMin() || y > lava.getMax()))) {
                        block.setType(Material.AIR);
                    }
                    if (worked >= items) {
                        break outer;
                    }
                }
            }
        }



        return worked;
    }
}

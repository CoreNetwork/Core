package us.corenetwork.core.claims;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class ClaimFluids implements Listener {
    private static class Range {
        private int min, max;

        private Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        @Override
        public String toString() {
            return "[" + min + "," + max + "]";
        }
    }

    private static class LiquidIndex {
        private Material liquid;
        private String world;

        private LiquidIndex(Material liquid, String world) {
            this.liquid = liquid;
            this.world = world;
        }

        public Material getLiquid() {
            return liquid;
        }

        public String getWorld() {
            return world;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LiquidIndex that = (LiquidIndex) o;

            if (liquid != that.liquid) return false;
            if (world != null ? !world.equals(that.world) : that.world != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = liquid != null ? liquid.hashCode() : 0;
            result = 31 * result + (world != null ? world.hashCode() : 0);
            return result;
        }
    }

    private HashMap<LiquidIndex, Range> ranges = new HashMap<LiquidIndex, Range>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlayerUseBucket(PlayerBucketEmptyEvent event) {
        Material mat = event.getBucket();
        mat = getLiquidType(mat);
        if (mat != null) {
            if (!isFluidAllowed(mat, event.getBlockClicked().getRelative(event.getBlockFace()), event.getPlayer(), null)) {
                event.setCancelled(true);
                // TODO inform player why this was blocked.
            }
        }
    }

    /**
     * Clears liquid type
     * stationary liquid or liquid in a bucket returns the contained liquid
     * if material is not liquid, return null
     * @param mat the type to clear
     * @return LAVA, WATER or null
     */
    public static Material getLiquidType(Material mat) {
        if (mat == Material.STATIONARY_LAVA || mat == Material.LAVA_BUCKET) {
            mat = Material.LAVA;
        }
        if (mat == Material.STATIONARY_WATER || mat == Material.WATER_BUCKET) {
            mat = Material.WATER;
        }
        if (mat != Material.LAVA && mat != Material.WATER) {
            return null;
        }
        return mat;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBlockFromTo(BlockFromToEvent event) {
        Material mat = getLiquidType(event.getBlock().getType());
        if (mat != null) {
            if (!isFluidAllowed(mat, event.getToBlock(), null, event.getBlock().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onDispense(BlockDispenseEvent event) {
        // dispenser places fluid
        Material mat = getLiquidType(event.getItem().getType());
        if (mat != null) {
            Vector direction = event.getVelocity().normalize();
            Block place = event.getBlock().getRelative(direction.getBlockX(), direction.getBlockY(), direction.getBlockZ());
            if (!isFluidAllowed(mat, place, null, event.getBlock().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    public boolean isFluidAllowed(Material material, Block block, Player player, Location origin) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), false, null);
        if (claim != null) {
            if (player != null) {
                return claim.allowBuild(player, material) == null;
                // griefprevention returns an error message if not allowed, null when allowed
            } else {
                Claim originClaim = GriefPrevention.instance.dataStore.getClaimAt(origin, false, null);
                return originClaim == claim;
            }
        }
        LiquidIndex index = new LiquidIndex(material, block.getWorld().getName());
        Range range = ranges.get(index);
        return range != null && block.getY() >= range.getMin() && block.getY() <= range.getMax();
    }

    public void loadConfig(Configuration configuration) {
        ranges.clear();
        ConfigurationSection liquidSection = configuration.getConfigurationSection("ClaimLiquids.Worlds");
        if (liquidSection != null) {
            for (String world : liquidSection.getValues(false).keySet()) {
                ConfigurationSection worldSection = liquidSection.getConfigurationSection(world);
                ConfigurationSection tmp = worldSection.getConfigurationSection("Water");
                if (tmp != null) {
                    loadAtomicLiquid(Material.WATER, world, tmp);
                }
                tmp = worldSection.getConfigurationSection("Lava");
                if (tmp != null) {
                    loadAtomicLiquid(Material.LAVA, world, tmp);
                }
            }
        }
    }

    private void loadAtomicLiquid(Material liquid, String world, ConfigurationSection section) {
        LiquidIndex index = new LiquidIndex(liquid, world);
        int min = section.getInt("MinHeight", 0);
        int max = section.getInt("MaxHeight", 256);

        Range range = new Range(min, max);

        ranges.put(index, range);
    }

    public void onRemoveClaim(Claim claim) {
        Location start = claim.getLesserBoundaryCorner();
        Location end = claim.getGreaterBoundaryCorner();

        String world = start.getWorld().getName();
        Range lava = ranges.get(new LiquidIndex(Material.LAVA, world));
        Range water = ranges.get(new LiquidIndex(Material.WATER, world));

        for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
            for (int y = 0; y <= 255; y++) {
                for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                    Block block = start.getWorld().getBlockAt(x, y, z);
                    Material mat = getLiquidType(block.getType());
                    if (mat == Material.WATER && (water == null || (y < water.getMin() || y > water.getMax()))) {
                        block.setType(Material.AIR);
                    }
                    if (mat == Material.LAVA && (lava == null || (y < lava.getMin() || y > lava.getMax()))) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}

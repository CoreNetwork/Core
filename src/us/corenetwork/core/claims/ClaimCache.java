package us.corenetwork.core.claims;

import org.bukkit.Location;

public class ClaimCache {
    private Location lowerBoundary, upperBoundary;

    public ClaimCache(Location lowerBoundary, Location upperBoundary) {
        this.lowerBoundary = lowerBoundary;
        this.upperBoundary = upperBoundary;
    }

    public boolean isInside(Location point) {
        return lowerBoundary.getBlockX() <= point.getBlockX()
                && lowerBoundary.getBlockZ() <= point.getBlockZ()
                && point.getBlockX() <= upperBoundary.getBlockX()
                && point.getBlockZ() <= upperBoundary.getBlockZ();
    }

    public Location getLowerBoundary() {
        return lowerBoundary;
    }

    public Location getUpperBoundary() {
        return upperBoundary;
    }
}

package us.corenetwork.core.map.clicks;

import org.bukkit.Material;

public class ClickAliasIndex {
    private Material blockType;
    private String region;

    public ClickAliasIndex(Material blockType, String region) {
        this.blockType = blockType;
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public Material getBlockType() {
        return blockType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClickAliasIndex that = (ClickAliasIndex) o;

        if (blockType != that.blockType) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = blockType != null ? blockType.hashCode() : 0;
        result = 31 * result + (region != null ? region.hashCode() : 0);
        return result;
    }
}

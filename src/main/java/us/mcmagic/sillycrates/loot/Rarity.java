package us.mcmagic.sillycrates.loot;

import org.bukkit.Color;
import us.mcmagic.sillycrates.SillyCratesMessage;

public enum Rarity {
    VERY_COMMON(500, "&8Very Common", Color.BLACK),
    COMMON(400, "&7Common", Color.GRAY),
    UNCOMMON(300, "&2Uncommon", Color.WHITE),
    UNIQUE(200, "&aUnique", Color.GREEN),
    RARE(100, "&1Rare", Color.BLUE),
    ULTRA_RARE(80, "&9Ultra Rare", Color.fromBGR(55, 0, 0)),
    SUPER_RARE(60, "&dSuper Rare", Color.FUCHSIA),
    LEGENDARY(1, "&5Legendary", Color.PURPLE);
    private final Color color;
    private final int weight;
    private final String label;
    Rarity(int weight, String label, Color color) {
        this.weight = weight;
        this.label = label;
        this.color = color;
    }
    public int getWeight() {
        return this.weight;
    }
    public String getLabel() {
        return this.label;
    }
    public Color getFireworkColor() {
        return color;
    }

    @Override
    public String toString() {
        return SillyCratesMessage.format(label, '&');
    }
}
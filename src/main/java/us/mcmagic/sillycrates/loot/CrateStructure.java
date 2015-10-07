package us.mcmagic.sillycrates.loot;

import org.bukkit.Location;
import org.bukkit.Material;
import us.mcmagic.sillycrates.util.FileUtil;
import us.mcmagic.sillycrates.util.TerrainManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class CrateStructure implements ICrateRandom {

    protected Rarity rarity;
    protected File schematic;
    protected List<TerrainManager> editors;

    public CrateStructure(Rarity value, String schematic) {
        this.rarity = value;
        this.schematic = new File(FileUtil.schematics, schematic);
        editors = new ArrayList<>();
    }

    @Override
    public Rarity getRarity() {
        return this.rarity;
    }

    public File getSchematic() {
        return schematic;
    }

    public List<TerrainManager> getEditors() {
        return editors;
    }

    public abstract void play(Location location);

    public final void undo(int i) {
        if (editors != null) {
            editors.get(i).getEditSession().undo(editors.get(i).getEditSession());
        }

    }

    protected final void undo(int i, Location location) {
        undo(i);
        location.add(0, 1, 0).getBlock().setType(Material.AIR);
    }
}


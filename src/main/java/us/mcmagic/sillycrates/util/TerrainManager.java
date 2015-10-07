package us.mcmagic.sillycrates.util;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@SuppressWarnings("deprecation")
public class TerrainManager {
    private final WorldEdit we;
    private final LocalSession localSession;
    private final EditSession editSession;
    private final LocalPlayer localPlayer;

    /**
     * Constructor
     *
     * @param wep    the WorldEdit plugin instance
     * @param player the player to work with
     */
    public TerrainManager(WorldEditPlugin wep, Player player) {
        we = wep.getWorldEdit();
        localPlayer = wep.wrapPlayer(player);
        localSession = we.getSession(localPlayer);
        editSession = localSession.createEditSession(localPlayer);
    }

    /**
     * Constructor
     *
     * @param wep   the WorldEdit plugin instance
     * @param world the world to work in
     */
    public TerrainManager(WorldEditPlugin wep, World world) {
        we = wep.getWorldEdit();
        localPlayer = null;
        localSession = new LocalSession(we.getConfiguration());
        editSession = new EditSession(new BukkitWorld(world), we.getConfiguration().maxChangeLimit);
    }

    /**
     * Write the terrain bounded by the given locations to the given file as a MCedit format
     * schematic.
     *
     * @param saveFile a File representing the schematic file to create
     * @param l1       one corner of the region to save
     * @param l2       the corner of the region to save, opposite to l1
     * @throws com.sk89q.worldedit.data.DataException
     * @throws IOException
     */
    public void saveTerrain(File saveFile, Location l1, Location l2) throws FilenameException, DataException, IOException {
        Vector min = getMin(l1, l2);
        Vector max = getMax(l1, l2);

        saveFile = we.getSafeSaveFile(localPlayer, saveFile.getParentFile(), saveFile.getName(), ".schematic", ".schematic");

        editSession.enableQueue();
        CuboidClipboard clipboard = new CuboidClipboard(max.subtract(min).add(new Vector(1, 1, 1)), min);
        clipboard.copy(editSession);
        SchematicFormat.MCEDIT.save(clipboard, saveFile);
        editSession.flushQueue();
    }

    public void loadSchematic(File saveFile, Location loc, boolean noAir) throws FilenameException, DataException, IOException, MaxChangedBlocksException, EmptyClipboardException {
        saveFile = we.getSafeSaveFile(localPlayer, saveFile.getParentFile(), saveFile.getName(), ".schematic", ".schematic");
        editSession.enableQueue();
        localSession.setClipboard(SchematicFormat.MCEDIT.load(saveFile));
        localSession.getClipboard().place(editSession, getPastePosition(loc), noAir);
        editSession.flushQueue();
        we.flushBlockBag(localPlayer, editSession);
    }

    public void loadSchematic(File saveFile, boolean noAir) throws FilenameException, DataException, IOException, MaxChangedBlocksException, EmptyClipboardException {
        loadSchematic(saveFile, null, noAir);
    }

    private Vector getPastePosition(Location loc) throws EmptyClipboardException {
        if (loc == null) {
            return localSession.getClipboard().getOrigin();
        } else {
            return new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        }
    }

    private Vector getMin(Location l1, Location l2) {
        return new Vector(
                Math.min(l1.getBlockX(), l2.getBlockX()),
                Math.min(l1.getBlockY(), l2.getBlockY()),
                Math.min(l1.getBlockZ(), l2.getBlockZ())
        );
    }

    private Vector getMax(Location l1, Location l2) {
        return new Vector(
                Math.max(l1.getBlockX(), l2.getBlockX()),
                Math.max(l1.getBlockY(), l2.getBlockY()),
                Math.max(l1.getBlockZ(), l2.getBlockZ())
        );
    }

    public final EditSession getEditSession() {
        return editSession;
    }
}
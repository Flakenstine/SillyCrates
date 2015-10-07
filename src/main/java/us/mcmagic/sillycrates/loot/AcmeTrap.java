package us.mcmagic.sillycrates.loot;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.FilenameException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import us.mcmagic.sillycrates.CratesPlugin;
import us.mcmagic.sillycrates.util.TerrainManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AcmeTrap extends CrateStructure implements Listener {

    private int dropHeight = 32;
    private List<BlockState> cleared = new ArrayList<>();

    public AcmeTrap(Rarity value, String schematic) {
        super(value, schematic);
        Bukkit.getPluginManager().registerEvents(this, CratesPlugin.getInstance());
    }

    public AcmeTrap(Rarity value, String schematic, int height) {
        super(value, schematic);
        this.dropHeight = height;
        Bukkit.getPluginManager().registerEvents(this, CratesPlugin.getInstance());
    }

    @Override
    public void play(final Location location) {
        final int i = editors.size();
        editors.add(new TerrainManager(CratesPlugin.getInstance().getWorldEdit(), location.getWorld()));
        try {
            editors.get(i).loadSchematic(schematic, location, false);
        } catch (FilenameException | DataException | IOException | MaxChangedBlocksException | EmptyClipboardException e) {
            e.printStackTrace();
        }
        location.add(1, dropHeight, 1).getBlock().setType(Material.ANVIL);
        Bukkit.getScheduler().runTaskLater(CratesPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                undo(i, location.add(0, -dropHeight, 0));
            }
        }, 80L);
    }

    @EventHandler
    public void anvilFall(EntityChangeBlockEvent event) {
        if (event.getTo() == Material.ANVIL) {
            if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("ACME")) {
                event.setCancelled(true);
            }
        }
    }
}

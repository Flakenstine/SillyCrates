package us.mcmagic.sillycrates;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            p.getInventory().addItem(CratesPlugin.getCrateItem());
            p.updateInventory();
            return true;
        }
        return false;
    }
}

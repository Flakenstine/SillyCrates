package us.mcmagic.sillycrates;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.mcmagic.sillycrates.time.TrackedPlayer;
import us.mcmagic.sillycrates.util.SillyCratesMessage;

public class CrateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (args.length == 1) {
                handle(p, args[0]);
                return true;
            } else {
                commandHelp(p);
            }
            return true;
        }
        return false;
    }

    public void handle(final Player player, final String argument) {
        switch (argument) {
            case "about":
                CratesPlugin.getInstance().aboutMessage(player);
                break;
            case "amount":
                TrackedPlayer tracked1 = CratesPlugin.getInstance().getTimeTracker().findTrackedPlayer(player.getUniqueId());
                if (tracked1 != null) {
                    if (tracked1.getAvailableCrates() == 1) {
                        SillyCratesMessage.send("&eYou currently have " + tracked1.getAvailableCrates() + " &c&l/crate &eavailable.", player);
                    } else {
                        SillyCratesMessage.send("&eYou currently have " + tracked1.getAvailableCrates() + " &c&l/crates &eavailable.", player);
                    }

                } else {
                    SillyCratesMessage.send("&cAn issue occurred whilst retrieving your player file, please relog.", player);
                }
                break;
            case "reload":
                if (player.isOp()) {
                    CratesPlugin.getInstance().reload();
                    SillyCratesMessage.send("&ePlugin successfully reloaded.", player);
                }
                break;
            case "unlock":
                TrackedPlayer tracked2 = CratesPlugin.getInstance().getTimeTracker().findTrackedPlayer(player.getUniqueId());
                if (tracked2 != null) {
                    tracked2.unlockCrate();
                } else {
                    SillyCratesMessage.send("&cAn issue occurred whilst retrieving your player file, please relog.", player);
                }
                break;
            default:
                commandHelp(player);
        }
    }

    public void commandHelp(final Player player) {
        if (player.isOp()) {
            SillyCratesMessage.sendWithoutHeader("&cUsage: /crate(s) <about/amount/override/reload/unlock>", player);
        } else {
            SillyCratesMessage.sendWithoutHeader("&cUsage: /crate(s) <about/amount/unlock>", player);
        }
    }

}

package us.mcmagic.sillycrates;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class SillyCratesMessage {

    private SillyCratesMessage() { }

    public static void send(String message, Player player) {
        SillyCratesMessage.send(CratesPlugin.CHAT_PREFIX, message, player);
    }

    public static void send(String message, List<Player> players) {
        send(CratesPlugin.CHAT_PREFIX, message, players);
    }

    public static void send(String header, String message, Player player) {
        player.sendMessage(format(header + " " + message, '&'));
    }

    public static void send(String header, String message, List<Player> players) {
        for (Player player : players) {
            if (player == null) {
                continue;
            }
            SillyCratesMessage.send(header, message, player);
        }
    }

    public static void sendWithoutHeader(String message, Player player) {
        player.sendMessage(format(message, '&'));
    }

    public static String format(String message, char colorIdentifier) {
        return ChatColor.translateAlternateColorCodes(colorIdentifier, message);
    }
}

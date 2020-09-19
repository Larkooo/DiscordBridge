package com.larko.DiscordBridge;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.larko.DiscordBridge.Webhook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends JavaPlugin implements Listener {
    Date dateNow = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        config.addDefault("webhookUrl", "https://canary.discordapp.com/api/webhooks/756928609670791169/FlFZ6yHIpVCNEOvlDQ8GPjSBFnGxPeCUuTVzX9fxZZBlkq6Gq1s9-AlmCd4gPyvlBk44");
        config.addDefault("leaveJoinMessage", true);
        config.addDefault("serverStartStopMessage", true);
        config.addDefault("chatBroadcastToDiscord", true);
        config.addDefault("serverName", "Change the server name in the config file");
        config.options().copyDefaults(true);
        saveConfig();

        getServer().getPluginManager().registerEvents(this, this);

        if (config.getBoolean("serverStartStopMessage")) {
            try {
                Webhook.send("[" +
                        "{" + "\"author\": {\"name\": \"" + config.getString("serverName") + "\", \"icon_url\": \"https://i.imgur.com/rxsK7U3.png\"}, \"description\": \"Server started\", \"timestamp\": \"" + formatter.format(dateNow) + "\"" + "}"
                        + "]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        if (config.getBoolean("serverStartStopMessage")) {
            try {
                Webhook.send("[" +
                        "{" + "\"author\": {\"name\": \"" + config.getString("serverName") + "\", \"icon_url\": \"https://i.imgur.com/rxsK7U3.png\"}, \"description\": \"Server closing\", \"timestamp\": \"" + formatter.format(dateNow) + "\"" + "}"
                        + "]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) throws IOException {
        if (config.getBoolean("leaveJoinMessage")) {
            Player player = event.getPlayer();
            Webhook.send("[" +
                    "{" + "\"author\": {\"name\": \"" + player.getName() + "\", \"icon_url\": \"https://minotar.net/avatar/" + player.getName() + "\"}, \"description\": \"" + player.getName() + " joined the server!\", \"timestamp\": \"" + formatter.format(dateNow) + "\"" + "}"
                    + "]");
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) throws IOException {
        if (config.getBoolean("chatBroadcastToDiscord")) {
            Player player = event.getPlayer();
            Webhook.send("[" +
                    "{" + "\"author\": {\"name\": \"" + player.getName() + "\", \"icon_url\": \"https://minotar.net/avatar/" + player.getName() + "\"}, \"description\": \""+ event.getMessage() +"\", \"timestamp\": \"" + formatter.format(dateNow) + "\"" + "}"
                    + "]");
        }
    }
}

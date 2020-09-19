package com.larko.DiscordBridge;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import com.larko.DiscordBridge.Webhook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends JavaPlugin implements Listener {
    Date dateNow = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");

    FileConfiguration config = getConfig();
    String serverIconUrl;
    @Override
    public void onEnable() {
        config.addDefault("webhookUrl", "https://canary.discordapp.com/api/webhooks/756928609670791169/FlFZ6yHIpVCNEOvlDQ8GPjSBFnGxPeCUuTVzX9fxZZBlkq6Gq1s9-AlmCd4gPyvlBk44");
        config.addDefault("leaveJoinMessage", true);
        config.addDefault("serverStartStopMessage", true);
        config.addDefault("playerHarvestBlockMessage", false);
        config.addDefault("playerAdvancementMessage", false);
        config.addDefault("playerDropItemMessage", false);
        config.addDefault("playerDeathMessage", false);
        config.addDefault("serverName", "Change the server name in the config file");
        config.addDefault("serverIconUrl", "https://i.imgur.com/rxsK7U3.png");
        config.options().copyDefaults(true);
        saveConfig();

        getServer().getPluginManager().registerEvents(this, this);

        serverIconUrl = config.getString("serverIconUrl");

        if (config.getBoolean("serverStartStopMessage")) {
            try {
                Webhook.send(config.getString("webhookUrl"), "[" +
                        "{" + "\"author\": {\"name\": \"" + config.getString("serverName") + "\", \"icon_url\": \"" + serverIconUrl + "\"}, \"description\": \"Server started\", \"timestamp\": \"" + formatter.format(dateNow) + "\", \"color\": \"4437377\"" + "}"
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
                Webhook.send(config.getString("webhookUrl"), "[" +
                        "{" + "\"author\": {\"name\": \"" + config.getString("serverName") + "\", \"icon_url\": \"" + serverIconUrl + "\"}, \"description\": \"Server closing\", \"timestamp\": \"" + formatter.format(dateNow) + "\", \"color\": \"15746887\"" + "}"
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
            Webhook.send(config.getString("webhookUrl"), "[" +
                    "{" + "\"author\": {\"name\": \"" + player.getName() + "\", \"icon_url\": \"https://minotar.net/avatar/" + player.getName() + "\"}, \"description\": \"" + player.getName() + " joined the server!\", \"timestamp\": \"" + formatter.format(dateNow) + "\", \"color\": \"7506394\"" + "}"
                    + "]");
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) throws IOException {
        if (config.getBoolean("leaveJoinMessage")) {
            Player player = event.getPlayer();
            Webhook.send(config.getString("webhookUrl"), "[" +
                    "{" + "\"author\": {\"name\": \"" + player.getName() + "\", \"icon_url\": \"https://minotar.net/avatar/" + player.getName() + "\"}, \"description\": \"" + player.getName() + " left the server!\", \"timestamp\": \"" + formatter.format(dateNow) + "\", \"color\": \"16426522\"" + "}"
                    + "]");
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) throws IOException {
        if (config.getBoolean("playerDropItemMessage")) {
            Player player = event.getPlayer();
            Webhook.send(config.getString("webhookUrl"), "[" +
                    "{" + "\"author\": {\"name\": \"" + player.getName() + "\", \"icon_url\": \"https://minotar.net/avatar/" + player.getName() + "\"}, \"description\": \"" + player.getName() + " dropped " + event.getItemDrop().getName() + "!\", \"timestamp\": \"" + formatter.format(dateNow) + "\", \"color\": \"16737792\"" + "}"
                    + "]");
        }
    }

    @EventHandler
    public void onHarvest(PlayerHarvestBlockEvent event) throws IOException {
        if (config.getBoolean("playerHarvestBlockMessage")) {
            Player player = event.getPlayer();
            Webhook.send(config.getString("webhookUrl"), "[" +
                    "{" + "\"author\": {\"name\": \"" + player.getName() + "\", \"icon_url\": \"https://minotar.net/avatar/" + player.getName() + "\"}, \"description\": \"" + player.getName() + " harvested " + event.getHarvestedBlock().toString() + "!\", \"timestamp\": \"" + formatter.format(dateNow) + "\", \"color\": \"16737792\"" + "}"
                    + "]");
        }
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) throws IOException {
        if (config.getBoolean("playerAdvancementMessage")) {
            Player player = event.getPlayer();
            Webhook.send(config.getString("webhookUrl"), "[" +
                    "{" + "\"author\": {\"name\": \"" + player.getName() + "\", \"icon_url\": \"https://minotar.net/avatar/" + player.getName() + "\"}, \"description\": \"" + player.getName() + " got the " + event.getAdvancement().toString() + " advancement!\", \"timestamp\": \"" + formatter.format(dateNow) + "\", \"color\": \"16737792\"" + "}"
                    + "]");
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) throws IOException {
        if (config.getBoolean("playerDeathMessage")) {
            Player player = event.getEntity();
            Webhook.send(config.getString("webhookUrl"), "[" +
                    "{" + "\"author\": {\"name\": \"" + player.getName() + "\", \"icon_url\": \"https://minotar.net/avatar/" + player.getName() + "\"}, \"description\": \""+ event.getDeathMessage() + "\", \"timestamp\": \"" + formatter.format(dateNow) + "\", \"color\": \"16737792\"" + "}"
                    + "]");
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) throws IOException {
        if (config.getBoolean("chatBroadcastToDiscord")) {
            Player player = event.getPlayer();
            Webhook.send(config.getString("webhookUrl"), "[" +
                    "{" + "\"author\": {\"name\": \"" + player.getName() + "\", \"icon_url\": \"https://minotar.net/avatar/" + player.getName() + "\"}, \"description\": \""+ event.getMessage() +"\", \"timestamp\": \"" + formatter.format(dateNow) + "\"" + "}"
                    + "]");
        }
    }
}

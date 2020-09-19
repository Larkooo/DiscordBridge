package com.larko.DiscordBridge;

import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.larko.DiscordBridge.Webhook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends JavaPlugin implements Listener {
    Date dateNow = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        try {
            Webhook.main("[" +
                    "{" + "\"author\": {\"name\": \"" + this.getServer().getIp() + "\", \"icon_url\": \"https://eu.mc-api.net/v3/server/favicon/" + this.getServer().getIp() + "\"}, \"description\": \"Server started\", \"timestamp\": \""+ formatter.format(dateNow) +"\"" + "}"
                    + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) throws IOException {
        Player player = event.getPlayer();
        Webhook.main("[" +
                "{" + "\"author\": {\"name\": \"" + player.getName() + "\", \"icon_url\": \"https://minotar.net/avatar/" + player.getName() + "\"}, \"description\": \""+ player.getName() +" joined the server!\", \"timestamp\": \""+ formatter.format(dateNow) +"\"" + "}"
                + "]");
    }
}

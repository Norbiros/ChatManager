package me.h1dd3nxn1nja.chatmanager.listeners;

import com.ryderbelserion.chatmanager.enums.Files;
import com.ryderbelserion.chatmanager.enums.Permissions;
import com.ryderbelserion.vital.paper.plugins.PluginManager;
import com.ryderbelserion.vital.paper.plugins.interfaces.Plugin;
import me.h1dd3nxn1nja.chatmanager.ChatManager;
import me.h1dd3nxn1nja.chatmanager.Methods;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class ListenerPlayerJoin implements Listener {

    @NotNull
    private final ChatManager plugin = ChatManager.get();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void firstJoinMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPlayedBefore()) return;

        if (PluginManager.isEnabled("GenericVanish")) {
            final Plugin plugin = PluginManager.getPlugin("GenericVanish");

            if (plugin.isVanished(player.getUniqueId())) {
                return;
            }
        }

        FileConfiguration config = Files.CONFIG.getConfiguration();

        if (config.getBoolean("Messages.First_Join.Welcome_Message.Enable")) {
            String message = config.getString("Messages.First_Join.Welcome_Message.First_Join_Message");
            event.setJoinMessage(Methods.placeholders(false, player, Methods.color(message)));

            String path = "Messages.First_Join.Welcome_Message.First_Join_Message.sound";
            boolean isEnabled = config.contains(path + ".toggle") && config.getBoolean(path + ".toggle");

            if (isEnabled) Methods.playSound(config, path);
        }

        if (config.getBoolean("Messages.First_Join.Actionbar_Message.Enable")) {
            String message = config.getString("Messages.First_Join.Actionbar_Message.First_Join_Message");

            player.sendActionBar(Methods.placeholders(false, player, Methods.color(message)));
        }

        if (config.getBoolean("Messages.First_Join.Title_Message.Enable")) {
            int fadeIn = config.getInt("Messages.First_Join.Title_Message.Fade_In");
            int stay = config.getInt("Messages.First_Join.Title_Message.Stay");
            int fadeOut = config.getInt("Messages.First_Join.Title_Message.Fade_Out");
            String header = config.getString("Messages.First_Join.Title_Message.First_Join_Message.Header");
            String footer = config.getString("Messages.First_Join.Title_Message.First_Join_Message.Footer");

            player.sendTitle(Methods.placeholders(false, player, Methods.color(header)), Methods.placeholders(false, player, Methods.color(footer)), fadeIn, stay, fadeOut);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void joinMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) return;

        if (PluginManager.isEnabled("GenericVanish")) {
            final Plugin plugin = PluginManager.getPlugin("GenericVanish");

            if (plugin.isVanished(player.getUniqueId())) {
                return;
            }
        }

        FileConfiguration config = Files.CONFIG.getConfiguration();

        if ((config.getBoolean("Messages.Join_Quit_Messages.Join_Message.Enable")) && !(config.getBoolean("Messages.Join_Quit_Messages.Group_Messages.Enable"))) {
            String message = config.getString("Messages.Join_Quit_Messages.Join_Message.Message");
            boolean isAsync = config.getBoolean("Messages.Async", false);

            String path = "Messages.Join_Quit_Messages.Join_Message.sound";
            boolean isEnabled = config.contains(path + ".toggle") && config.getBoolean(path + ".toggle");

            if (isAsync) {
                if (event.getJoinMessage() != null) {
                    event.setJoinMessage(null);

                    this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> this.plugin.getServer().broadcastMessage(Methods.placeholders(false, player, Methods.color(message))));
                }
            } else {
                event.setJoinMessage(Methods.placeholders(false, player, Methods.color(message)));
            }

            if (isEnabled) Methods.playSound(config, path);
        }

        if ((config.getBoolean("Messages.Join_Quit_Messages.Actionbar_Message.Enable")) && !(config.getBoolean("Messages.Join_Quit_Messages.Group_Messages.Enable"))) {
            String message = config.getString("Messages.Join_Quit_Messages.Actionbar_Message.Message");

            player.sendActionBar(Methods.placeholders(false, player, Methods.color(message)));
        }

        if ((config.getBoolean("Messages.Join_Quit_Messages.Title_Message.Enable")) && !(config.getBoolean("Messages.Join_Quit_Messages.Group_Messages.Enable"))) {
            int fadeIn = config.getInt("Messages.Join_Quit_Messages.Title_Message.Fade_In");
            int stay = config.getInt("Messages.Join_Quit_Messages.Title_Message.Stay");
            int fadeOut = config.getInt("Messages.Join_Quit_Messages.Title_Message.Fade_Out");
            String header = config.getString("Messages.Join_Quit_Messages.Title_Message.Message.Header");
            String footer = config.getString("Messages.Join_Quit_Messages.Title_Message.Message.Footer");

            player.sendTitle(Methods.placeholders(false, player, Methods.color(header)), Methods.placeholders(false, player, Methods.color(footer)), fadeIn, stay, fadeOut);
        }

        if (config.getBoolean("Messages.Join_Quit_Messages.Group_Messages.Enable")) {
            for (String key : config.getConfigurationSection("Messages.Join_Quit_Messages.Group_Messages").getKeys(false)) {
                String permission = config.getString("Messages.Join_Quit_Messages.Group_Messages." + key + ".Permission");
                String joinMessage = config.getString("Messages.Join_Quit_Messages.Group_Messages." + key + ".Join_Message");
                String actionbarMessage = config.getString("Messages.Join_Quit_Messages.Group_Messages." + key + ".Actionbar");
                String titleHeader = config.getString("Messages.Join_Quit_Messages.Group_Messages." + key + ".Title.Header");
                String titleFooter = config.getString("Messages.Join_Quit_Messages.Group_Messages." + key + ".Title.Footer");
                int fadeIn = config.getInt("Messages.Join_Quit_Messages.Title_Message.Fade_In");
                int stay = config.getInt("Messages.Join_Quit_Messages.Title_Message.Stay");
                int fadeOut = config.getInt("Messages.Join_Quit_Messages.Title_Message.Fade_Out");

                if (permission != null && player.hasPermission(permission)) {
                    if (config.contains("Messages.Join_Quit_Messages.Group_Messages." + key + ".Join_Message")) {
                        boolean isAsync = config.getBoolean("Messages.Async", false);

                        if (isAsync) {
                            if (event.getJoinMessage() != null) {
                                event.setJoinMessage(null);

                                this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> this.plugin.getServer().broadcastMessage(Methods.placeholders(false, player, Methods.color(joinMessage))));
                            }
                        } else {
                            event.setJoinMessage(Methods.placeholders(false, player, Methods.color(joinMessage)));
                        }
                    }

                    if (config.contains("Messages.Join_Quit_Messages.Group_Messages." + key + ".Actionbar")) {
                        try {
                            player.sendActionBar(Methods.placeholders(false, player, Methods.color(actionbarMessage)));
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (config.contains("Messages.Join_Quit_Messages.Group_Messages." + key + ".Title")) {
                        try {
                            player.sendTitle(Methods.placeholders(false, player, Methods.color(titleHeader)), Methods.placeholders(false, player, Methods.color(titleFooter)), fadeIn, stay, fadeOut);
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }
                    }

                    String path = "Messages.Join_Quit_Messages.Group_Messages." + key + ".sound";

                    Methods.playSound(config, path);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (PluginManager.isEnabled("GenericVanish")) {
            final Plugin plugin = PluginManager.getPlugin("GenericVanish");

            if (plugin.isVanished(player.getUniqueId())) {
                return;
            }
        }

        FileConfiguration config = Files.CONFIG.getConfiguration();

        if ((config.getBoolean("Messages.Join_Quit_Messages.Quit_Message.Enable")) && !(config.getBoolean("Messages.Join_Quit_Messages.Group_Messages.Enable"))) {
            String message = config.getString("Messages.Join_Quit_Messages.Quit_Message.Message");
            event.setQuitMessage(Methods.placeholders(false, player, Methods.color(message)));

            String path = "Messages.Join_Quit_Messages.Quit_Message.sound";

            Methods.playSound(config, path);
        }

        if (config.getBoolean("Messages.Join_Quit_Messages.Group_Messages.Enable")) {
            for (String key : config.getConfigurationSection("Messages.Join_Quit_Messages.Group_Messages").getKeys(false)) {
                String permission = config.getString("Messages.Join_Quit_Messages.Group_Messages." + key + ".Permission");
                String quitMessage = config.getString("Messages.Join_Quit_Messages.Group_Messages." + key + ".Quit_Message");

                if (permission != null && player.hasPermission(permission)) {
                    if (config.contains("Messages.Join_Quit_Messages.Group_Messages." + key + ".Quit_Message")) {
                        event.setQuitMessage(Methods.placeholders(false, player, Methods.color(quitMessage)));
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        FileConfiguration config = Files.CONFIG.getConfiguration();

        int lines = config.getInt("Clear_Chat.Broadcasted_Lines");
        int delay = config.getInt("MOTD.Delay");

        if (config.getBoolean("Clear_Chat.Clear_On_Join")) {
            if (player.hasPermission(Permissions.BYPASS_CLEAR_CHAT_ON_JOIN.getNode())) return;

            for (int i = 0; i < lines; i++) {
                player.sendMessage("");
            }
        }

        if (config.getBoolean("Social_Spy.Enable_On_Join")) {
            if (player.hasPermission(Permissions.SOCIAL_SPY.getNode())) this.plugin.api().getSocialSpyData().addUser(player.getUniqueId());
        }

        if (config.getBoolean("Command_Spy.Enable_On_Join")) {
            if (player.hasPermission(Permissions.COMMAND_SPY.getNode())) this.plugin.api().getCommandSpyData().addUser(player.getUniqueId());
        }

        if (config.getBoolean("Chat_Radius.Enable")) {
            if (config.getString("Chat_Radius.Default_Channel").equalsIgnoreCase("Local")) this.plugin.api().getLocalChatData().addUser(player.getUniqueId());
            if (config.getString("Chat_Radius.Default_Channel").equalsIgnoreCase("Global")) this.plugin.api().getGlobalChatData().addUser(player.getUniqueId());
            if (config.getString("Chat_Radius.Default_Channel").equalsIgnoreCase("World")) this.plugin.api().getWorldChatData().addUser(player.getUniqueId());
        }

        if (config.getBoolean("Chat_Radius.Enable")) {
            if (!config.getBoolean("Chat_Radius.Enable_Spy_On_Join")) return;

            if (player.hasPermission(Permissions.COMMAND_CHATRADIUS_SPY.getNode())) plugin.api().getSpyChatData().addUser(player.getUniqueId());
        }

        if (config.getBoolean("MOTD.Enable")) {
            new BukkitRunnable() {
                public void run() {
                    for (String motd : config.getStringList("MOTD.Message")) {
                        Methods.sendMessage(player, motd, false);
                    }
                }
            }.runTaskLater(this.plugin, 20L * delay);
        }
    }
}
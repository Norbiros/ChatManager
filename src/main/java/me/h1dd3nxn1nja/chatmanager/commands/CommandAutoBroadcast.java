package me.h1dd3nxn1nja.chatmanager.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.ryderbelserion.chatmanager.enums.Files;
import com.ryderbelserion.chatmanager.enums.Messages;
import me.h1dd3nxn1nja.chatmanager.ChatManager;
import com.ryderbelserion.chatmanager.enums.Permissions;
import me.h1dd3nxn1nja.chatmanager.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.h1dd3nxn1nja.chatmanager.utils.World;
import org.jetbrains.annotations.NotNull;

public class CommandAutoBroadcast implements CommandExecutor {

	@NotNull
	private final ChatManager plugin = ChatManager.get();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if (!(sender instanceof Player player)) {
			Methods.sendMessage(sender, "&cError: You can only use that command in-game", true);

			return true;
		}

		FileConfiguration messages = Files.MESSAGES.getConfiguration();
		FileConfiguration autobroadcast = Files.AUTO_BROADCAST.getConfiguration();

		if (cmd.getName().equalsIgnoreCase("autobroadcast")) {
			if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_HELP.getNode())) {
				if (args.length == 0) return checkProtocol(player);
			} else {
				Messages.NO_PERMISSION.sendMessage(player);

				return true;
			}

			if (args[0].equalsIgnoreCase("help")) {
				if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_HELP.getNode())) {
					if (args.length == 1) return checkProtocol(player);
				} else {
					Messages.NO_PERMISSION.sendMessage(player);

					return true;
				}
			}

			if (args[0].equalsIgnoreCase("list")) {
				if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_LIST.getNode())) {
					if (args.length == 1) {
						Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast list [Global|World|Actionbar|Title|Bossbar] [World]", true);

						return true;
					}
				} else {
					Messages.NO_PERMISSION.sendMessage(player);

					return true;
				}

				if (args[1].equalsIgnoreCase("global")) {
					if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_LIST.getNode())) {
						if (args.length == 2) {
							Methods.sendMessage(player, "", true);

							Messages.AUTO_BROADCAST_LIST.sendMessage(player, "{section}", "Global");

							Methods.sendMessage(player, "", true);

							String string = autobroadcast.getString("Auto_Broadcast.Global_Messages.Prefix");

							for (String global : autobroadcast.getStringList("Auto_Broadcast.Global_Messages.Messages")) {
								if (string != null) {
									Methods.sendMessage(player, "&7 - " + global.replace("{Prefix}", string), false);
								}
							}

							Methods.sendMessage(player, "", true);
						} else {
							Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast list global", true);
						}
					} else {
						Messages.NO_PERMISSION.sendMessage(player);
					}
				}

				if (args[1].equalsIgnoreCase("actionbar")) {
					if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_LIST.getNode())) {
						if (args.length == 2) {
							Methods.sendMessage(player, "", true);

							Messages.AUTO_BROADCAST_LIST.sendMessage(player, "{section}", "Actionbar");

							String string = autobroadcast.getString("Auto_Broadcast.Actionbar_Messages.Prefix");

							for (String actionbar : autobroadcast.getStringList("Auto_Broadcast.Actionbar_Messages.Messages")) {
								if (string != null) {
									Methods.sendMessage(player, "&7 - " + actionbar.replace("{Prefix}", string), false);
								}
							}

							Methods.sendMessage(player, "", true);
						} else {
							Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast list actionbar", true);
						}
					} else {
						Messages.NO_PERMISSION.sendMessage(player);
					}
				}

				if (args[1].equalsIgnoreCase("title")) {
					if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_LIST.getNode())) {
						if (args.length == 2) {
							Methods.sendMessage(player, "", true);

							Messages.AUTO_BROADCAST_LIST.sendMessage(player, "{section}", "Title");

							for (String title : autobroadcast.getStringList("Auto_Broadcast.Title_Messages.Messages")) {
								Methods.sendMessage(player, "&7 - " + title, false);
							}

							Methods.sendMessage(player, "", true);
						} else {
							Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast list title", true);
						}
					} else {
						Messages.NO_PERMISSION.sendMessage(player);
					}
				}

				if (args[1].equalsIgnoreCase("bossbar")) {
					if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_LIST.getNode())) {
						if (args.length == 2) {
							Methods.sendMessage(player, "", true);

							Messages.AUTO_BROADCAST_LIST.sendMessage(player, "{section}", "Bossbar");

							for (String bossbar : autobroadcast.getStringList("Auto_Broadcast.Bossbar_Messages.Messages")) {
								Methods.sendMessage(player, "&7 - " + bossbar, false);
							}

							Methods.sendMessage(player, "", true);
						} else {
							Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast list bossbar", true);
						}
					} else {
						Messages.NO_PERMISSION.sendMessage(player);
					}
				}

				if (args[1].equalsIgnoreCase("world")) {
					if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_LIST.getNode())) {
						if (args.length == 3) {
							List<World> worlds = new ArrayList<>();

							String broadcast = autobroadcast.getString("Auto_Broadcast.Per_World_Messages.Prefix");

							for (String key : autobroadcast.getConfigurationSection("Auto_Broadcast.Per_World_Messages.Messages").getKeys(false)) {
								World w = new World(key, autobroadcast.getStringList("Auto_Broadcast.Per_World_Messages.Messages." + key), 0);
								worlds.add(w);

								if (args[2].equals(key)) {
									Methods.sendMessage(player, "", true);

									Messages.AUTO_BROADCAST_LIST.sendMessage(player, "{section}", key);

									for (String bcmsg : autobroadcast.getStringList("Auto_Broadcast.Per_World_Messages.Messages." + key)) {
										if (broadcast != null) {
											Methods.sendMessage(player, "&7 - " + bcmsg.replace("{Prefix}", broadcast), false);
										}
									}

									Methods.sendMessage(player, "", true);
								}
							}
						} else {
							Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast list world <world>", true);
						}
					} else {
						Messages.NO_PERMISSION.sendMessage(player);
					}
				}
			}

			if (args[0].equalsIgnoreCase("add")) {
				if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_ADD.getNode())) {
					if (args.length == 1) {
						Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast add [Global|World|Actionbar|Title|Bossbar] <world> <message>", true);
						return true;
					}
				} else {
					Messages.NO_PERMISSION.sendMessage(player);
					return true;
				}

				if (args[1].equalsIgnoreCase("global")) {
					if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_ADD.getNode())) {
						if (args.length != 2) {
							StringBuilder builder = new StringBuilder();

							for (int i = 2; i < args.length; i++) {
								builder.append(args[i]).append(" ");
							}

							String msg = builder.toString();

							List<String> msgs = autobroadcast.getStringList("Auto_Broadcast.Global_Messages.Messages");
							msgs.add(msg);

							autobroadcast.set("Auto_Broadcast.Global_Messages.Messages", msgs);

							Files.AUTO_BROADCAST.save();

							Messages.AUTO_BROADCAST_ADDED.sendMessage(player, new HashMap<>() {{
								put("{message}", msg);
								put("{section}", "Global");
							}});

							return true;
						} else {
							Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast add global <message>", true);
						}
					} else {
						Messages.NO_PERMISSION.sendMessage(player);
					}
				}
			}

			if (args[1].equalsIgnoreCase("actionbar")) {
				if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_ADD.getNode())) {
					if (args.length != 2) {
						StringBuilder builder = new StringBuilder();

						for (int i = 2; i < args.length; i++) {
							builder.append(args[i]).append(" ");
						}

						String msg = builder.toString();

						List<String> msgs = autobroadcast.getStringList("Auto_Broadcast.Actionbar_Messages.Messages");
						msgs.add(msg);

						autobroadcast.set("Auto_Broadcast.Actionbar_Messages.Messages", msgs);

						Files.AUTO_BROADCAST.save();

						Messages.AUTO_BROADCAST_ADDED.sendMessage(player, new HashMap<>() {{
							put("{message}", msg);
							put("{section}", "Actionbar");
						}});

						return true;
					} else {
						Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast add actionbar <message.", true);
					}
				} else {
					Messages.NO_PERMISSION.sendMessage(player);
				}
			}

			if (args[1].equalsIgnoreCase("title")) {
				if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_ADD.getNode())) {
					if (args.length != 2) {
						StringBuilder builder = new StringBuilder();

						for (int i = 2; i < args.length; i++) {
							builder.append(args[i]).append(" ");
						}

						String msg = builder.toString();

						List<String> msgs = autobroadcast.getStringList("Auto_Broadcast.Title_Messages.Messages");
						msgs.add(msg);

						autobroadcast.set("Auto_Broadcast.Title_Messages.Messages", msgs);

						Files.AUTO_BROADCAST.save();

						Messages.AUTO_BROADCAST_ADDED.sendMessage(player, new HashMap<>() {{
							put("{message}", msg);
							put("{section}", "Title");
						}});

						return true;
					} else {
						Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast add title <message>", true);
					}
				} else {
					Messages.NO_PERMISSION.sendMessage(player);
				}
			}

			if (args[1].equalsIgnoreCase("bossbar")) {
				if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_ADD.getNode())) {
					if (args.length != 2) {
						StringBuilder builder = new StringBuilder();

						for (int i = 2; i < args.length; i++) {
							builder.append(args[i]).append(" ");
						}

						String msg = builder.toString();

						List<String> msgs = autobroadcast.getStringList("Auto_Broadcast.Bossbar_Messages.Messages");
						msgs.add(msg);

						autobroadcast.set("Auto_Broadcast.Bossbar_Messages.Messages", msgs);

						Files.AUTO_BROADCAST.save();

						Messages.AUTO_BROADCAST_ADDED.sendMessage(player, new HashMap<>() {{
							put("{message}", msg);
							put("{section}", "Bossbar");
						}});

						return true;
					} else {
						Methods.sendMessage(player, "&cCommand Usage: &7/butobroadcast add bossbar <message>", true);
					}
				} else {
					Messages.NO_PERMISSION.sendMessage(player);
				}
			}

			if (args[1].equalsIgnoreCase("world")) {
				if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_ADD.getNode())) {
					if (args.length != 2) {
						List<World> worlds = new ArrayList<>();

						for (String key : autobroadcast.getConfigurationSection("Auto_Broadcast.Per_World_Messages.Messages").getKeys(false)) {
							World w = new World(key, autobroadcast.getStringList("Auto_Broadcast.Per_World_Messages.Messages." + key), 0);
							worlds.add(w);

							if (args[2].equals(key)) {
								if (!player.hasPermission("chatmanager.autobroadcast.add")) {
									Messages.NO_PERMISSION.sendMessage(player);
									return true;
								}

								StringBuilder builder = new StringBuilder();

								for (int i = 3; i < args.length; i++) {
									builder.append(args[i]).append(" ");
								}

								String msg = builder.toString();

								List<String> msgs = autobroadcast.getStringList("Auto_Broadcast.Per_World_Messages.Messages." + key);
								msgs.add(msg);

								autobroadcast.set("Auto_Broadcast.Per_World_Messages.Messages." + key, msgs);

								Files.AUTO_BROADCAST.save();

								Messages.AUTO_BROADCAST_ADDED.sendMessage(player, new HashMap<>() {{
									put("{message}", msg);
									put("{section}", key);
								}});

								return true;
							}
						}
					} else {
						Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast add world <world> <message>", true);
					}
				} else {
					Messages.NO_PERMISSION.sendMessage(player);
				}
			}
		}

		if (args[0].equalsIgnoreCase("create")) {
			if (player.hasPermission(Permissions.COMMAND_AUTOBROADCAST_CREATE.getNode())) {
				if (args.length >= 2) {
					StringBuilder builder = new StringBuilder();

					for (int i = 2; i < args.length; i++) {
						builder.append(args[i]).append(" ");
					}

					String msg = builder.toString();

					List<String> msgs = autobroadcast.getStringList("Auto_Broadcast.Per_World_Messages.Messages");
					msgs.add(msg);

					autobroadcast.set("Auto_Broadcast.Per_World_Messages.Messages." + args[1], msgs);

					Files.AUTO_BROADCAST.save();

					Messages.AUTO_BROADCAST_CREATED.sendMessage(player, new HashMap<>() {{
						put("{world}", args[1]);
						put("{message}", msg);
					}});

					return true;
				} else {
					Methods.sendMessage(player, "&cCommand Usage: &7/autobroadcast create <world> <message>", true);
				}
			} else {
				Messages.NO_PERMISSION.sendMessage(player);
			}
		}

		return true;
	}

	private boolean checkProtocol(Player player) {
		Methods.sendMessage(player, "", false);
		Methods.sendMessage(player, " &3Auto-Broadcast Help Menu &f(v" + plugin.getDescription().getVersion() + ")", false);
		Methods.sendMessage(player,"", false);
		Methods.sendMessage(player, " &6<> &f= Required Arguments", false);
		Methods.sendMessage(player, " &2[] &f= Optional Arguments", false);
		Methods.sendMessage(player, " ", false);
		Methods.sendMessage(player, " &f/autobroadcast help &e- Shows a list of commands for Auto-Broadcast.", false);
		Methods.sendMessage(player, "  &f/autobroadcast add &6<Global|World|Actionbar|Title|Bossbar> &2[World] &6<message> &e- Add a message to the Auto-Broadcast to a specific world.", false);
		Methods.sendMessage(player, " &f/autobroadcast create &6<world> <message> &e- Create a new world in the Auto-Broadcast file.", false);
		Methods.sendMessage(player, " &f/autobroadcast list &6<Global|World|Actionbar|Title|Bossbar> &2[World] &e- Shows a list of all the broadcast messages in a world.", false);
		Methods.sendMessage(player, "", false);

		return true;
	}
}
/*
 * Copyright 2018 NAFU_at
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package work.nafu_at.soloservercore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import work.nafu_at.soloservercore.command.HomeCommand;
import work.nafu_at.soloservercore.command.SoloServerCoreCommand;
import work.nafu_at.soloservercore.command.SpawnCommand;
import work.nafu_at.soloservercore.listener.AsyncPlayerChatEventListener;
import work.nafu_at.soloservercore.listener.EntityDamageByEntityEventListener;
import work.nafu_at.soloservercore.listener.PlayerJoinEventListener;
import work.nafu_at.soloservercore.listener.PlayerRespawnEventListener;
import work.nafu_at.soloservercore.sql.MySQLConnector;
import work.nafu_at.soloservercore.sql.PlayerSpawnDatabase;
import work.nafu_at.soloservercore.sql.ShowPlayerDatabase;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SoloServerCore extends JavaPlugin {
    private static SoloServerCore instance;
    private SoloServerConfig config;
    private MessageManager messageManager;
    private MySQLConnector connector;
    private PlayerSpawnDatabase spawnDatabase;
    private ShowPlayerDatabase playerDatabase;
    private ShowPlayersRegistry playersRegistry;

    private SoloServerCoreCommand soloServerCoreCommand;
    private HomeCommand homeCommand;
    private SpawnCommand spawnCommand;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = new SoloServerConfig();
        messageManager = new MessageManager(config.getLanguage());
        connector = new MySQLConnector();
        spawnDatabase = new PlayerSpawnDatabase(connector);
        playerDatabase = new ShowPlayerDatabase(connector);
        try {
            spawnDatabase.mkTable();
            playerDatabase.mkTable();
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "データベースのテーブル作成に失敗しました。", e);
            getServer().getPluginManager().disablePlugin(instance);
        }
        playersRegistry = new ShowPlayersRegistry();

        getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(), instance);
        if (config.enableRandomSpawn())
            getServer().getPluginManager().registerEvents(new PlayerRespawnEventListener(), instance);
        if (config.isLimitedPvP())
            getServer().getPluginManager().registerEvents(new EntityDamageByEntityEventListener(), instance);
        if (config.enableManageChat())
            getServer().getPluginManager().registerEvents(new AsyncPlayerChatEventListener(), instance);

        soloServerCoreCommand = new SoloServerCoreCommand();
        homeCommand = new HomeCommand();
        spawnCommand = new SpawnCommand();

        for (Player player : Bukkit.getOnlinePlayers())
            playersRegistry.loadShowPlaers(player);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : Bukkit.getOnlinePlayers())
            playersRegistry.saveShowPlayers(player);
        if (connector != null)
            connector.close();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
            case "soloservercore":
                return soloServerCoreCommand.onCommand(sender, command, label, args);
            case "home":
                return homeCommand.onCommand(sender, command, label, args);
            case "spawn":
                return spawnCommand.onCommand(sender, command, label, args);
            default:
                return false;
        }
    }

    public static SoloServerCore getInstance() {
        if (instance == null)
            instance = (SoloServerCore) Bukkit.getPluginManager().getPlugin("SoloServerCore");
        return instance;
    }

    public static Logger getPluginLogger() {
        return getInstance().getLogger();
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public SoloServerConfig getSoloServerConfig() {
        return config;
    }

    public PlayerSpawnDatabase getSpawnDatabase() {
        return spawnDatabase;
    }

    public ShowPlayerDatabase getPlayerDatabase() {
        return playerDatabase;
    }

    public ShowPlayersRegistry getPlayersRegistry() {
        return playersRegistry;
    }
}

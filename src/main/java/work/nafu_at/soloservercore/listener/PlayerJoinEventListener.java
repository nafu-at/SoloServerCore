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

package work.nafu_at.soloservercore.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import work.nafu_at.soloservercore.ShowPlayersRegistry;
import work.nafu_at.soloservercore.SoloServerConfig;
import work.nafu_at.soloservercore.SoloServerCore;
import work.nafu_at.soloservercore.sql.PlayerSpawnDatabase;
import work.nafu_at.soloservercore.teleport.RandomTeleporter;

import java.sql.SQLException;
import java.util.logging.Level;

public class PlayerJoinEventListener implements Listener {
    private final SoloServerConfig config = SoloServerCore.getInstance().getSoloServerConfig();
    private final PlayerSpawnDatabase spawnDatabase = SoloServerCore.getInstance().getSpawnDatabase();

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        ShowPlayersRegistry registry = SoloServerCore.getInstance().getPlayersRegistry();
        if (config.disableJoinMessage())
            event.setJoinMessage("");

        int[] location = new int[3];
        try {
            location = spawnDatabase.getLocation(event.getPlayer().getUniqueId());
        } catch (SQLException e) {
            SoloServerCore.getPluginLogger().log(Level.WARNING, "プレイヤーのスポーンロケーションの取得に失敗しました。");
        }
        if (config.enableRandomSpawn()) {
            if (!Bukkit.getOfflinePlayer(event.getPlayer().getUniqueId()).hasPlayedBefore() ||
                    (config.doRegenerateIfNoData() && (location[0] == 0 && location[1] == 0 && location[2] == 0)))
                Bukkit.getScheduler().runTask(SoloServerCore.getInstance(), new RandomTeleporter(event.getPlayer(), true));
        }

        if (config.enableInvisible()) {
            registry.loadShowPlaers(event.getPlayer());
            if (!config.getInvisibleWhitelist().contains(event.getPlayer().getName())) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (!config.getInvisibleWhitelist().contains(online.getName())) {
                        if (!registry.getPlyaers(event.getPlayer()).contains(Bukkit.getOfflinePlayer(online.getUniqueId()))) {
                            event.getPlayer().hidePlayer(SoloServerCore.getInstance(), online);
                            online.hidePlayer(SoloServerCore.getInstance(), event.getPlayer());
                        } else {
                            online.hidePlayer(SoloServerCore.getInstance(), event.getPlayer());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        ShowPlayersRegistry registry = SoloServerCore.getInstance().getPlayersRegistry();
        if (config.disableJoinMessage())
            event.setQuitMessage("");
        if (config.enableInvisible())
            registry.saveShowPlayers(event.getPlayer());
    }
}

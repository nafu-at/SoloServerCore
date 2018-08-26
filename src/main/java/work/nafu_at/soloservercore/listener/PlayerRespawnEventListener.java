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
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import work.nafu_at.soloservercore.SoloServerCore;
import work.nafu_at.soloservercore.sql.PlayerSpawnDatabase;

import java.sql.SQLException;
import java.util.logging.Level;

public class PlayerRespawnEventListener implements Listener {
    private final PlayerSpawnDatabase spawnDatabase = SoloServerCore.getInstance().getSpawnDatabase();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        if (!event.isBedSpawn()) {
            try {
                int[] loc = spawnDatabase.getLocation(event.getPlayer().getUniqueId());
                event.setRespawnLocation(new Location(Bukkit.getServer().getWorlds().get(0), loc[0], loc[1], loc[2]));
            } catch (SQLException e) {
                SoloServerCore.getPluginLogger().log(Level.WARNING, "SQLから座標の取得に失敗しました。", e);
            }
        }
    }

}

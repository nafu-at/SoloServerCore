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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import work.nafu_at.soloservercore.sql.ShowPlayerDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ShowPlayersRegistry {
    private final ShowPlayerDatabase playerDatabase = SoloServerCore.getInstance().getPlayerDatabase();
    private final Map<Player, List<OfflinePlayer>> registry;

    public ShowPlayersRegistry() {
        registry = new HashMap<>();
    }

    public void loadShowPlaers(Player player) {
        String result = null;
        try {
            result = playerDatabase.getData(player.getUniqueId());
        } catch (SQLException e) {
            SoloServerCore.getPluginLogger().log(Level.WARNING, "SQLからデータの取得に失敗しました。", e);
        }
        List<OfflinePlayer> players = new ArrayList<>();
        if (result != null) {
            for (String show : result.split(",", 0)) {
                // TODO: 2018/08/26 UUID管理方式への変更
                OfflinePlayer offlinePlayer = null;
                if (!show.equals(""))
                    offlinePlayer = Bukkit.getOfflinePlayer(show);
                if (offlinePlayer != null)
                    players.add(offlinePlayer);
            }
        }
        registry.put(player, players);
    }

    public void saveShowPlayers(Player player) {
        StringBuilder stringBuilder = new StringBuilder();
        for (OfflinePlayer s : registry.get(player)) {
            stringBuilder.append(s.getName() + ",");
        }
        try {
            playerDatabase.saveData(player.getUniqueId(), stringBuilder.toString());
        } catch (SQLException e) {
            SoloServerCore.getPluginLogger().log(Level.WARNING, "SQLへのデータの保存に失敗しました。", e);
        }
    }

    public void addPlayer(Player player, OfflinePlayer target) {
        List<OfflinePlayer> players = registry.get(player);
        players.add(target);
        registry.put(player, players);
    }

    public void removePlayer(Player player, OfflinePlayer target) {
        List<OfflinePlayer> players = registry.get(player);
        players.remove(target);
        registry.put(player, players);
    }

    public List<OfflinePlayer> getPlyaers(Player player) {
        List<OfflinePlayer> players = registry.get(player);
        return players;
    }

}

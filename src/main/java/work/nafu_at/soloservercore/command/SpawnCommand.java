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

package work.nafu_at.soloservercore.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.nafu_at.soloservercore.MessageManager;
import work.nafu_at.soloservercore.SoloServerCore;
import work.nafu_at.soloservercore.sql.PlayerSpawnDatabase;

import java.sql.SQLException;
import java.util.logging.Level;

public class SpawnCommand implements CommandExecutor {
    private final PlayerSpawnDatabase spawnDatabase = SoloServerCore.getInstance().getSpawnDatabase();
    private final MessageManager message = SoloServerCore.getInstance().getMessageManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            int[] loc;
            try {
                loc = spawnDatabase.getLocation(p.getUniqueId());
                if (loc[0] == 0 && loc[1] == 0 && loc[2] == 0) {
                    sender.sendMessage(message.getMessage("failed-get-location"));
                } else
                    p.teleport(new Location(Bukkit.getServer().getWorlds().get(0), loc[0], loc[1], loc[2]));
            } catch (SQLException e) {
                SoloServerCore.getPluginLogger().log(Level.WARNING, "プレイヤーのスポーンロケーションの取得に失敗しました。");
            }
        }
        return true;
    }
}

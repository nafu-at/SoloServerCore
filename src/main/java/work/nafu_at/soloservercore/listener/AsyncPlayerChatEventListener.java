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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import work.nafu_at.soloservercore.ShowPlayersRegistry;
import work.nafu_at.soloservercore.SoloServerConfig;
import work.nafu_at.soloservercore.SoloServerCore;

public class AsyncPlayerChatEventListener implements Listener {
    private final SoloServerConfig config = SoloServerCore.getInstance().getSoloServerConfig();

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        ShowPlayersRegistry registry = SoloServerCore.getInstance().getPlayersRegistry();
        if (config.disableChat()) {
            if (event.isCancelled())
                return;

            if (config.isLimitedChat()) {
                String format;
                if (config.enableCustomizeChat())
                    format = config.getChatStyle();
                else
                    format = "<%Player%> %Message%";
                String message = format.replaceAll("%Player%", event.getPlayer().getDisplayName())
                        .replaceAll("%Message%", event.getMessage());
                message = ChatColor.translateAlternateColorCodes('&', message);

                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (event.getPlayer().equals(online) ||
                            config.getChatWhitelist().contains(event.getPlayer().getName()) ||
                            config.getChatWhitelist().contains(online.getName()) ||
                            registry.getPlyaers(event.getPlayer()).contains(Bukkit.getOfflinePlayer(online.getUniqueId()))) {
                        online.sendMessage(message);
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}

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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import work.nafu_at.soloservercore.ShowPlayersRegistry;
import work.nafu_at.soloservercore.SoloServerCore;

import java.util.List;

public class EntityDamageByEntityEventListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        ShowPlayersRegistry registry = SoloServerCore.getInstance().getPlayersRegistry();
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        if (entity instanceof Player && damager instanceof Player) {
            Player val1 = (Player) entity;
            Player val2 = (Player) damager;
            List<OfflinePlayer> val3 = registry.getPlyaers(val1);
            List<OfflinePlayer> val4 = registry.getPlyaers(val2);

            if (!(val3.contains(Bukkit.getOfflinePlayer(val2.getUniqueId())) &&
                    val4.contains(Bukkit.getOfflinePlayer(val1.getUniqueId()))))
                event.setCancelled(true);
        }
    }
}

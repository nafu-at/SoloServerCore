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

package work.nafu_at.soloservercore.teleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import work.nafu_at.soloservercore.MessageManager;
import work.nafu_at.soloservercore.SoloServerCore;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.logging.Level;

public class RandomTeleporter implements Runnable {
    private final MessageManager message = SoloServerCore.getInstance().getMessageManager();
    private final int maxX = SoloServerCore.getInstance().getSoloServerConfig().getMaxRandomX();
    private final int maxZ = SoloServerCore.getInstance().getSoloServerConfig().getMaxRandomZ();
    private final Player target;
    private final boolean doSave;

    public RandomTeleporter(Player target, boolean doSave) {
        this.target = target;
        this.doSave = doSave;
    }

    @Override
    public void run() {
        World world = Bukkit.getWorlds().get(0);
        int x;
        int y = 128;
        int z;
        SecureRandom secureRandom = new SecureRandom();

        target.sendMessage(message.getMessage("random-teleport-start"));
        do {
            x = secureRandom.nextInt(maxX * 2) - maxX;
            z = secureRandom.nextInt(maxZ * 2) - maxZ;

            Location location = searchSafeLocation(world, x, y, z);
            if (location != null) {
                try {
                    if (SoloServerCore.getInstance().getSoloServerConfig().doAvoidDuplication() &&
                            SoloServerCore.getInstance().getSpawnDatabase().getUUID(location.getBlockX(), location.getBlockY(), location.getBlockZ()) != null)
                        break;

                    if (doSave)
                        SoloServerCore.getInstance().getSpawnDatabase().saveLocation(
                                target.getUniqueId(), location.getBlockX(), location.getBlockY(), location.getBlockZ());

                    target.teleport(location);
                    target.sendMessage(String.format(
                            message.getMessage("teleported"), target.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                    return;
                } catch (SQLException e) {
                    SoloServerCore.getPluginLogger().log(Level.WARNING, "MySQLとの通信に失敗しました。", e);
                    return;
                }
            }
        } while (true);
    }

    private Location searchSafeLocation(World world, int x, int y, int z) {
        do {
            Location point1 = new Location(world, x, y, z);
            point1.getChunk().load(true);

            if (!world.getBlockAt(point1).getType().equals(Material.AIR)) {
                Location point2 = new Location(world, x, y + 2, z);
                if (world.getBlockAt(point2).getType().equals(Material.AIR) &&
                        !world.getBlockAt(point2).getType().equals(Material.WATER)) {
                    return point2;
                } else {
                    Location point3 = new Location(world, x, y + 4, z);
                    if (world.getBlockAt(point3).getType().equals(Material.AIR) &&
                            !world.getBlockAt(point2).getType().equals(Material.WATER))
                        return point3;
                }
            } else {
                y = -3;
                if (y <= 0)
                    return null;
            }
        } while (true);
    }
}

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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.nafu_at.soloservercore.MessageManager;
import work.nafu_at.soloservercore.SoloServerCore;

public class HomeCommand implements CommandExecutor {
    private MessageManager message = SoloServerCore.getInstance().getMessageManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getBedSpawnLocation() != null)
                player.teleport(player.getBedSpawnLocation());
            else
                player.sendMessage(message.getMessage("not-sleep"));
        } else {
            sender.sendMessage(message.getMessage("ingame-command"));
        }
        return true;
    }
}

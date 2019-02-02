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

package work.nafu_at.soloservercore.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.nafu_at.soloservercore.MessageManager;
import work.nafu_at.soloservercore.ShowPlayersRegistry;
import work.nafu_at.soloservercore.SoloServerCore;

public class ShowHideCommand implements CommandExecutor {
    private final MessageManager message = SoloServerCore.getInstance().getMessageManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ShowPlayersRegistry registry = SoloServerCore.getInstance().getPlayersRegistry();
        if (args.length < 2)
            sender.sendMessage(message.getMessage("noselect-player"));
        else if (sender instanceof Player) {
            if (sender.hasPermission("soloservercore.show")) {
                Player player = (Player) sender;
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(message.getMessage("player-notfound"));
                } else switch (args[0]) {
                    case "hide":
                        player.hidePlayer(SoloServerCore.getInstance(), target);
                        registry.removePlayer(player, Bukkit.getOfflinePlayer(target.getUniqueId()));
                        break;

                    case "show":
                        player.showPlayer(SoloServerCore.getInstance(), target);
                        registry.addPlayer(player, Bukkit.getOfflinePlayer(target.getUniqueId()));
                        break;

                    default:
                        return false;
                }
            } else
                sender.sendMessage(message.getMessage("no-permission"));
        } else
            sender.sendMessage(message.getMessage("ingame-command"));
        return true;
    }
}

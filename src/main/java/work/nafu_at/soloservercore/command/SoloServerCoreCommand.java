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
import work.nafu_at.soloservercore.command.sub.RandomTeleportCommand;
import work.nafu_at.soloservercore.command.sub.ShowHideCommand;

public class SoloServerCoreCommand implements CommandExecutor {
    private final RandomTeleportCommand randomTeleportCommand;
    private final ShowHideCommand showHideCommand;

    public SoloServerCoreCommand() {
        randomTeleportCommand = new RandomTeleportCommand();
        showHideCommand = new ShowHideCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            return false;
        else switch (args[0]) {
            case "randomteleport":
                return randomTeleportCommand.onCommand(sender, command, label, args);
            case "show":
            case "hide":
                return showHideCommand.onCommand(sender, command, label, args);
            default:
                return false;
        }
    }
}

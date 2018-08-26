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

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class SoloServerConfig {
    private String language;
    private String jdbcUrl;
    private String mysqlUser;
    private String mysqlPass;
    private String mysqlPrefix;

    private boolean enableRandomSpawn;
    private boolean avoidDuplication;
    private boolean regenerateIfNoData;
    private int maxRandomX;
    private int maxRandomZ;

    private boolean enableInvisible;
    private boolean disableJoinMessage;
    private boolean dynmapSupport = false;
    private boolean limitedPvP;
    private List<String> invisibleWhitelist;

    private boolean enableManageChat;
    private boolean disableChat;
    private boolean limitedChat;
    private boolean enableCustomizeChat;
    private String chatStyle;
    private List<String> chatWhitelist;

    public SoloServerConfig() {
        reloadConfig();
    }

    public void reloadConfig() {
        FileConfiguration config = SoloServerCore.getInstance().getConfig();

        language = config.getString("language", "ja_JP");

        jdbcUrl = config.getString("jdbc-url");
        mysqlUser = config.getString("mysql-user");
        mysqlPass = config.getString("mysql-pass");
        mysqlPrefix = config.getString("mysql-prefix");

        enableRandomSpawn = config.getBoolean("random-spawn.enable");
        if (enableRandomSpawn) {
            avoidDuplication = config.getBoolean("random-spawn.avoid-duplication");
            regenerateIfNoData = config.getBoolean("random-spawn.regenerate-if-no-data");
            maxRandomX = config.getInt("random-spawn.max-random-x");
            maxRandomZ = config.getInt("random-spawn.max-random-z");
        }

        enableInvisible = config.getBoolean("invisible.enable");
        if (enableInvisible) {
            disableJoinMessage = config.getBoolean("invisible.disable-join-message");
            // TODO: 2018/08/26 Dynmapサポートの追加
            // dynmapSupport = config.getBoolean("invisible.dynmap-support");
            limitedPvP = config.getBoolean("invisible.limited-pvp");
            invisibleWhitelist = config.getStringList("invisible.whitelist");
        }

        enableManageChat = config.getBoolean("manage-chat.enable");
        if (enableManageChat) {
            disableChat = config.getBoolean("manage-chat.disable-chat");
            limitedChat = config.getBoolean("manage-chat.limited-chat");
            enableCustomizeChat = config.getBoolean("manage-chat.customize.enable");
            if (enableCustomizeChat)
                chatStyle = config.getString("manage-chat.customize.chat-style");
            chatWhitelist = config.getStringList("manage-chat.whitelist");
        }
    }

    public String getLanguage() {
        return language;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getMysqlUser() {
        return mysqlUser;
    }

    public String getMysqlPass() {
        return mysqlPass;
    }

    public String getMysqlPrefix() {
        return mysqlPrefix;
    }

    public boolean enableRandomSpawn() {
        return enableRandomSpawn;
    }

    public boolean doAvoidDuplication() {
        return avoidDuplication;
    }

    public boolean doRegenerateIfNoData() {
        return regenerateIfNoData;
    }

    public int getMaxRandomX() {
        return maxRandomX;
    }

    public int getMaxRandomZ() {
        return maxRandomZ;
    }

    public boolean enableInvisible() {
        return enableInvisible;
    }

    public boolean disableJoinMessage() {
        return disableJoinMessage;
    }

    public boolean doDynmapSupport() {
        return dynmapSupport;
    }

    public boolean isLimitedPvP() {
        return limitedPvP;
    }

    public List<String> getInvisibleWhitelist() {
        return invisibleWhitelist;
    }

    public boolean enableManageChat() {
        return enableManageChat;
    }

    public boolean disableChat() {
        return disableChat;
    }

    public boolean isLimitedChat() {
        return limitedChat;
    }

    public boolean enableCustomizeChat() {
        return enableCustomizeChat;
    }

    public String getChatStyle() {
        return chatStyle;
    }

    public List<String> getChatWhitelist() {
        return chatWhitelist;
    }
}

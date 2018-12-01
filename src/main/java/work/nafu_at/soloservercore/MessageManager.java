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

import org.bukkit.ChatColor;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageManager {
    private ResourceBundle resource;

    public MessageManager(String language) {
        String[] split = language.split("_");
        Locale locale = new Locale(split[0], split[1]);
        resource = ResourceBundle.getBundle("language.messages", locale);
    }

    /**
     * ローカライズされたメッセージを取得できます。
     *
     * @param index メッセージ項目名
     * @return メッセージ
     * @deprecated 次回以降のアップデートで無効化予定です。
     */
    @Deprecated
    public String getMessage(String index) {
        return ChatColor.translateAlternateColorCodes('&', resource.getString(index));
    }
}

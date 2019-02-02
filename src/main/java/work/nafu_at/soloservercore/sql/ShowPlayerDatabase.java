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

package work.nafu_at.soloservercore.sql;

import work.nafu_at.soloservercore.SoloServerCore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ShowPlayerDatabase {
    private final MySQLConnector connector;
    private final String table;

    public ShowPlayerDatabase(MySQLConnector connector) {
        this.connector = connector;
        table = SoloServerCore.getInstance().getSoloServerConfig().getMysqlPrefix() + "hide";
    }

    public void mkTable() throws SQLException {
        try (Connection connection = connector.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " + table + " (uuid CHAR(36) PRIMARY KEY, players VARCHAR(2048))")) {
                ps.execute();
            }
        }
    }

    public void saveData(UUID uuid, String s) throws SQLException {
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "REPLACE INTO " + table + " (uuid, players) VALUES (?, ?)")) {
            ps.setString(1, uuid.toString());
            ps.setString(2, s);
            ps.execute();
        }
    }

    public String getData(UUID uuid) throws SQLException {
        String result = null;
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT players FROM " + table + " WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next())
                    result = resultSet.getString("players");
            }
        }
        return result;
    }
}

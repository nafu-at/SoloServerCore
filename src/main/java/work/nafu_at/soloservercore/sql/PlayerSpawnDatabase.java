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

public class PlayerSpawnDatabase extends MySQLConnector {
    private final MySQLConnector connector;
    private final String table;

    public PlayerSpawnDatabase(MySQLConnector connector) {
        this.connector = connector;
        table =  SoloServerCore.getInstance().getSoloServerConfig().getMysqlPrefix() + "location";
    }

    public void mkTable() throws SQLException {
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS " + table + " (uuid CHAR(36) PRIMARY KEY, x INT, y INT, z INT)")) {
            ps.execute();
        }
    }

    public void saveLocation(UUID uuid, int x, int y, int z) throws SQLException {
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "REPLACE INTO " + table + " (uuid, x, y, z) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, uuid.toString());
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setInt(4, z);
            ps.execute();
        }
    }

    public int[] getLocation(UUID uuid) throws SQLException {
        int[] loc = new int[3];
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM " + table + " WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    loc[0] = resultSet.getInt("x");
                    loc[1] = resultSet.getInt("y");
                    loc[2] = resultSet.getInt("z");
                }
            }
        }
        return loc;
    }

    public UUID getUUID(int x, int y, int z) throws SQLException {
        UUID uuid = null;
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM  " + table + " WHERE x = ? AND y = ? AND z = ?")) {
            ps.setInt(1, x);
            ps.setInt(2, y);
            ps.setInt(3, z);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next())
                    uuid = UUID.fromString(resultSet.getString("uuid"));
            }
        }
        return uuid;
    }
}

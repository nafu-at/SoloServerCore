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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import work.nafu_at.soloservercore.SoloServerConfig;
import work.nafu_at.soloservercore.SoloServerCore;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnector {
    private HikariDataSource dataSource;

    public MySQLConnector() {
        SoloServerConfig config = SoloServerCore.getInstance().getSoloServerConfig();
        HikariConfig hconfig = new HikariConfig();
        hconfig.setDriverClassName("com.mysql.jdbc.Driver");
        hconfig.setJdbcUrl(config.getJdbcUrl());
        hconfig.addDataSourceProperty("user", config.getMysqlUser());
        hconfig.addDataSourceProperty("password", config.getMysqlPass());
        dataSource = new HikariDataSource(hconfig);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        dataSource.close();
    }
}

/**
 * Copyright 2010-2014 Axel Fontaine
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sk89q.worldguard.internal.flywaydb.core.internal.dbsupport.postgresql;

import java.sql.SQLException;

import com.sk89q.worldguard.internal.flywaydb.core.internal.dbsupport.DbSupport;
import com.sk89q.worldguard.internal.flywaydb.core.internal.dbsupport.JdbcTemplate;
import com.sk89q.worldguard.internal.flywaydb.core.internal.dbsupport.Schema;
import com.sk89q.worldguard.internal.flywaydb.core.internal.dbsupport.Type;

/**
 * PostgreSQL-specific type.
 */
public class PostgreSQLType extends Type {
    /**
     * Creates a new PostgreSQL type.
     *
     * @param jdbcTemplate The Jdbc Template for communicating with the DB.
     * @param dbSupport    The database-specific support.
     * @param schema       The schema this type lives in.
     * @param name         The name of the type.
     */
    public PostgreSQLType(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name) {
        super(jdbcTemplate, dbSupport, schema, name);
    }

    @Override
    protected void doDrop() throws SQLException {
        jdbcTemplate.execute("DROP TYPE " + dbSupport.quote(schema.getName(), name));
    }
}

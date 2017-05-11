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
package com.sk89q.worldguard.internal.flywaydb.core.internal.command;

import java.sql.Connection;
import java.sql.SQLException;

import com.sk89q.worldguard.internal.flywaydb.core.api.MigrationInfo;
import com.sk89q.worldguard.internal.flywaydb.core.api.MigrationVersion;
import com.sk89q.worldguard.internal.flywaydb.core.api.callback.FlywayCallback;
import com.sk89q.worldguard.internal.flywaydb.core.api.resolver.MigrationResolver;
import com.sk89q.worldguard.internal.flywaydb.core.api.resolver.ResolvedMigration;
import com.sk89q.worldguard.internal.flywaydb.core.internal.info.MigrationInfoImpl;
import com.sk89q.worldguard.internal.flywaydb.core.internal.info.MigrationInfoServiceImpl;
import com.sk89q.worldguard.internal.flywaydb.core.internal.metadatatable.AppliedMigration;
import com.sk89q.worldguard.internal.flywaydb.core.internal.metadatatable.MetaDataTable;
import com.sk89q.worldguard.internal.flywaydb.core.internal.util.ObjectUtils;
import com.sk89q.worldguard.internal.flywaydb.core.internal.util.jdbc.TransactionCallback;
import com.sk89q.worldguard.internal.flywaydb.core.internal.util.jdbc.TransactionTemplate;

/**
 * Handles Flyway's repair command.
 */
public class DbRepair {
    /**
     * The database connection to use for accessing the metadata table.
     */
    private final Connection connection;

    /**
     * The migration infos.
     */
    private final MigrationInfoServiceImpl migrationInfoService;

    /**
     * The metadata table.
     */
    private final MetaDataTable metaDataTable;

    /**
     * This is a list of callbacks that fire before or after the repair task is executed.
     * You can add as many callbacks as you want.  These should be set on the Flyway class
     * by the end user as Flyway will set them automatically for you here.
     */
    private final FlywayCallback[] callbacks;

    /**
     * Creates a new DbRepair.
     *
     * @param connection        The database connection to use for accessing the metadata table.
     * @param migrationResolver The migration resolver.
     * @param metaDataTable     The metadata table.
     * @param callbacks         Callbacks for the Flyway lifecycle.
     */
    public DbRepair(Connection connection, MigrationResolver migrationResolver, MetaDataTable metaDataTable, FlywayCallback[] callbacks) {
        this.connection = connection;
        this.migrationInfoService = new MigrationInfoServiceImpl(migrationResolver, metaDataTable, MigrationVersion.LATEST, true, true);
        this.metaDataTable = metaDataTable;
        this.callbacks = callbacks;
    }

    /**
     * Repairs the metadata table.
     */
    public void repair() {
        for (final FlywayCallback callback : callbacks) {
            new TransactionTemplate(connection).execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction() throws SQLException {
                    callback.beforeRepair(connection);
                    return null;
                }
            });
        }

        new TransactionTemplate(connection).execute(new TransactionCallback<Void>() {
            public Void doInTransaction() {
                metaDataTable.removeFailedMigrations();

                migrationInfoService.refresh();
                for (MigrationInfo migrationInfo : migrationInfoService.all()) {
                    MigrationInfoImpl migrationInfoImpl = (MigrationInfoImpl) migrationInfo;

                    ResolvedMigration resolved = migrationInfoImpl.getResolvedMigration();
                    AppliedMigration applied = migrationInfoImpl.getAppliedMigration();
                    if ((resolved != null) && (applied != null)) {
                        if (!ObjectUtils.nullSafeEquals(resolved.getChecksum(), applied.getChecksum())) {
                            metaDataTable.updateChecksum(migrationInfoImpl.getVersion(), resolved.getChecksum());
                        }
                    }
                }

                return null;
            }
        });

        for (final FlywayCallback callback : callbacks) {
            new TransactionTemplate(connection).execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction() throws SQLException {
                    callback.afterRepair(connection);
                    return null;
                }
            });
        }
    }
}

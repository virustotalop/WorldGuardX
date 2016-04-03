/**
 * Copyright 2010-2016 Boxfuse GmbH
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
package com.sk89q.worldguard.internal.flywaydb.core.internal.util.logging.console;

import com.sk89q.worldguard.internal.flywaydb.core.internal.util.logging.Log;
import com.sk89q.worldguard.internal.flywaydb.core.internal.util.logging.LogCreator;
import com.sk89q.worldguard.internal.flywaydb.core.internal.util.logging.console.ConsoleLog.Level;

/**
 * Log Creator for the Command-Line console.
 */
public class ConsoleLogCreator implements LogCreator {
    private final Level level;

    /**
     * Creates a new Console Log Creator.
     *
     * @param level The minimum level to log at.
     */
    public ConsoleLogCreator(Level level) {
        this.level = level;
    }

    public Log createLogger(Class<?> clazz) {
        return new ConsoleLog(level);
    }
}

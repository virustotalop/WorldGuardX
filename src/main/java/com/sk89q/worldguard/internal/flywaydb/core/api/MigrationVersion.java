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
package com.sk89q.worldguard.internal.flywaydb.core.api;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A version of a migration.
 *
 * @author Axel Fontaine
 */
public final class MigrationVersion implements Comparable<MigrationVersion> {
    /**
     * Version for an empty schema.
     */
    public static final MigrationVersion EMPTY = new MigrationVersion(null, "<< Empty Schema >>");

    /**
     * Latest version.
     */
    public static final MigrationVersion LATEST = new MigrationVersion(Long.MAX_VALUE, "<< Latest Version >>");

    /**
     * Compiled pattern for matching proper version format
     */
    private static Pattern splitPattern = Pattern.compile("\\.(?=\\d)");

    /**
     * The individual parts this version string is composed of. Ex. 1.2.3.4.0 -> [1, 2, 3, 4, 0]
     */
    private final List<Long> versionParts;

    /**
     * The printable text to represent the version.
     */
    private final String displayText;

    /**
     * Factory for creating a MigrationVersion from a version String
     * @param version The version String
     * @return The MigrationVersion
     */
    public static MigrationVersion fromVersion(String version) {
        if (LATEST.getVersion().equals(version)) return LATEST;
        if (version == null) return EMPTY;
        return new MigrationVersion(version);
    }
    /**
     * Creates a Version using this version string.
     *
     * @param version The version in one of the following formats: 6, 6.0, 005, 1.2.3.4, 201004200021. <br/>{@code null}
     *                means that this version refers to an empty schema.
     */
    private MigrationVersion(String version) {
        String normalizedVersion = version.replace('_', '.');
        this.versionParts = tokenizeToLongs(normalizedVersion);
        this.displayText = normalizedVersion;
    }

    /**
     * Creates a Version using this version string.
     *
     * @param version     The version in one of the following formats: 6, 6.0, 005, 1.2.3.4, 201004200021. <br/>{@code null}
     *                    means that this version refers to an empty schema.
     * @param displayText The alternative text to display instead of the version number.
     */
    private MigrationVersion(Long version, String displayText) {
        this.versionParts = new ArrayList<Long>();
        this.versionParts.add(version);
        this.displayText = displayText;
    }

    /**
     * @return The textual representation of the version.
     */
    @Override
    public String toString() {
        return displayText;
    }

    /**
     * @return Numeric version as String
     */
    public String getVersion() {
        if (this.equals(EMPTY)) return null;
        if (this.equals(LATEST)) return Long.toString(Long.MAX_VALUE);
        return displayText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MigrationVersion version1 = (MigrationVersion) o;

        return compareTo(version1) == 0;
    }

    @Override
    public int hashCode() {
        return versionParts == null ? 0 : versionParts.hashCode();
    }

    public int compareTo(MigrationVersion o) {
        if (o == null) {
            return 1;
        }

        if (this == EMPTY) {
            return o == EMPTY ? 0 : Integer.MIN_VALUE;
        }

        if (this == LATEST) {
            return o == LATEST ? 0 : Integer.MAX_VALUE;
        }

        if (o == EMPTY) {
            return Integer.MAX_VALUE;
        }

        if (o == LATEST) {
            return Integer.MIN_VALUE;
        }
        final List<Long> elements1 = versionParts;
        final List<Long> elements2 = o.versionParts;
        int largestNumberOfElements = Math.max(elements1.size(), elements2.size());
        for (int i = 0; i < largestNumberOfElements; i++) {
            final int compared = getOrZero(elements1, i).compareTo(getOrZero(elements2, i));
            if (compared != 0) {
                return compared;
            }
        }
        return 0;
    }

    private Long getOrZero(List<Long> elements, int i) {
        return i < elements.size() ? elements.get(i) : 0;
    }

    /**
     * Splits this string into list of Long
     *
     * @param str        The string to split.
     * @return The resulting array.
     */
    private List<Long> tokenizeToLongs(String str) {
        List<Long> numbers = new ArrayList<Long>();
        for (String number : splitPattern.split(str)) {
            try {
                numbers.add(Long.valueOf(number));
            } catch (NumberFormatException e) {
                throw new FlywayException(
                    "Invalid version containing non-numeric characters. Only 0..9 and . are allowed. Invalid version: "
                    + str);
            }
        }
        for (int i = numbers.size() - 1; i > 0; i--) {
            if (numbers.get(i) != 0) break;
            numbers.remove(i);
        }
        return numbers;
    }
}

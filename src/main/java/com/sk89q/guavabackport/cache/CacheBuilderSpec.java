/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sk89q.guavabackport.cache;

import javax.annotation.Nullable;

import com.google.common.base.Objects;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.concurrent.TimeUnit;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Splitter;
import com.google.common.annotations.Beta;

@Beta
public final class CacheBuilderSpec
{
    private static final Splitter KEYS_SPLITTER;
    private static final Splitter KEY_VALUE_SPLITTER;
    private static final ImmutableMap<Object, Object> VALUE_PARSERS;
    @VisibleForTesting
    Integer initialCapacity;
    @VisibleForTesting
    Long maximumSize;
    @VisibleForTesting
    Long maximumWeight;
    @VisibleForTesting
    Integer concurrencyLevel;
    @VisibleForTesting
    LocalCache.Strength keyStrength;
    @VisibleForTesting
    LocalCache.Strength valueStrength;
    @VisibleForTesting
    Boolean recordStats;
    @VisibleForTesting
    long writeExpirationDuration;
    @VisibleForTesting
    TimeUnit writeExpirationTimeUnit;
    @VisibleForTesting
    long accessExpirationDuration;
    @VisibleForTesting
    TimeUnit accessExpirationTimeUnit;
    @VisibleForTesting
    long refreshDuration;
    @VisibleForTesting
    TimeUnit refreshTimeUnit;
    private final String specification;
    
    private CacheBuilderSpec(final String specification) {
        this.specification = specification;
    }
    
    public static CacheBuilderSpec parse(final String cacheBuilderSpecification) {
        final CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
        if (!cacheBuilderSpecification.isEmpty()) {
            for (final String keyValuePair : CacheBuilderSpec.KEYS_SPLITTER.split((CharSequence)cacheBuilderSpecification)) {
                final List<String> keyAndValue = (List<String>)ImmutableList.copyOf(CacheBuilderSpec.KEY_VALUE_SPLITTER.split((CharSequence)keyValuePair));
                Preconditions.checkArgument(!keyAndValue.isEmpty(), (Object)"blank key-value pair");
                Preconditions.checkArgument(keyAndValue.size() <= 2, "key-value pair %s with more than one equals sign", new Object[] { keyValuePair });
                final String key = keyAndValue.get(0);
                final ValueParser valueParser = (ValueParser)CacheBuilderSpec.VALUE_PARSERS.get((Object)key);
                Preconditions.checkArgument(valueParser != null, "unknown key %s", new Object[] { key });
                final String value = (keyAndValue.size() == 1) ? null : keyAndValue.get(1);
                valueParser.parse(spec, key, value);
            }
        }
        return spec;
    }
    
    public static CacheBuilderSpec disableCaching() {
        return parse("maximumSize=0");
    }
    
    CacheBuilder<Object, Object> toCacheBuilder() {
        final CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (this.initialCapacity != null) {
            builder.initialCapacity(this.initialCapacity);
        }
        if (this.maximumSize != null) {
            builder.maximumSize(this.maximumSize);
        }
        if (this.maximumWeight != null) {
            builder.maximumWeight(this.maximumWeight);
        }
        if (this.concurrencyLevel != null) {
            builder.concurrencyLevel(this.concurrencyLevel);
        }
        if (this.keyStrength != null) {
            switch (this.keyStrength) {
                case WEAK: {
                    builder.weakKeys();
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        if (this.valueStrength != null) {
            switch (this.valueStrength) {
                case SOFT: {
                    builder.softValues();
                    break;
                }
                case WEAK: {
                    builder.weakValues();
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        if (this.recordStats != null && this.recordStats) {
            builder.recordStats();
        }
        if (this.writeExpirationTimeUnit != null) {
            builder.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
        }
        if (this.accessExpirationTimeUnit != null) {
            builder.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
        }
        if (this.refreshTimeUnit != null) {
            builder.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
        }
        return builder;
    }
    
    public String toParsableString() {
        return this.specification;
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(this.toParsableString()).toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(new Object[] { this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(this.refreshDuration, this.refreshTimeUnit) });
    }
    
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CacheBuilderSpec)) {
            return false;
        }
        final CacheBuilderSpec that = (CacheBuilderSpec)obj;
        return Objects.equal((Object)this.initialCapacity, (Object)that.initialCapacity) && Objects.equal((Object)this.maximumSize, (Object)that.maximumSize) && Objects.equal((Object)this.maximumWeight, (Object)that.maximumWeight) && Objects.equal((Object)this.concurrencyLevel, (Object)that.concurrencyLevel) && Objects.equal((Object)this.keyStrength, (Object)that.keyStrength) && Objects.equal((Object)this.valueStrength, (Object)that.valueStrength) && Objects.equal((Object)this.recordStats, (Object)that.recordStats) && Objects.equal((Object)durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), (Object)durationInNanos(that.writeExpirationDuration, that.writeExpirationTimeUnit)) && Objects.equal((Object)durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), (Object)durationInNanos(that.accessExpirationDuration, that.accessExpirationTimeUnit)) && Objects.equal((Object)durationInNanos(this.refreshDuration, this.refreshTimeUnit), (Object)durationInNanos(that.refreshDuration, that.refreshTimeUnit));
    }
    
    @Nullable
    private static Long durationInNanos(final long duration, @Nullable final TimeUnit unit) {
        return (unit == null) ? null : unit.toNanos(duration);
    }
    
    static {
        KEYS_SPLITTER = Splitter.on(',').trimResults();
        KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
        VALUE_PARSERS = ImmutableMap.builder().put((Object)"initialCapacity", (Object)new InitialCapacityParser()).put((Object)"maximumSize", (Object)new MaximumSizeParser()).put((Object)"maximumWeight", (Object)new MaximumWeightParser()).put((Object)"concurrencyLevel", (Object)new ConcurrencyLevelParser()).put((Object)"weakKeys", (Object)new KeyStrengthParser(LocalCache.Strength.WEAK)).put((Object)"softValues", (Object)new ValueStrengthParser(LocalCache.Strength.SOFT)).put((Object)"weakValues", (Object)new ValueStrengthParser(LocalCache.Strength.WEAK)).put((Object)"recordStats", (Object)new RecordStatsParser()).put((Object)"expireAfterAccess", (Object)new AccessDurationParser()).put((Object)"expireAfterWrite", (Object)new WriteDurationParser()).put((Object)"refreshAfterWrite", (Object)new RefreshDurationParser()).put((Object)"refreshInterval", (Object)new RefreshDurationParser()).build();
    }
    
    abstract static class IntegerParser implements ValueParser
    {
        protected abstract void parseInteger(final CacheBuilderSpec p0, final int p1);
        
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, final String value) {
            Preconditions.checkArgument(value != null && !value.isEmpty(), "value of key %s omitted", new Object[] { key });
            try {
                this.parseInteger(spec, Integer.parseInt(value));
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", key, value), e);
            }
        }
    }
    
    abstract static class LongParser implements ValueParser
    {
        protected abstract void parseLong(final CacheBuilderSpec p0, final long p1);
        
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, final String value) {
            Preconditions.checkArgument(value != null && !value.isEmpty(), "value of key %s omitted", new Object[] { key });
            try {
                this.parseLong(spec, Long.parseLong(value));
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", key, value), e);
            }
        }
    }
    
    static class InitialCapacityParser extends IntegerParser
    {
        @Override
        protected void parseInteger(final CacheBuilderSpec spec, final int value) {
            Preconditions.checkArgument(spec.initialCapacity == null, "initial capacity was already set to ", new Object[] { spec.initialCapacity });
            spec.initialCapacity = value;
        }
    }
    
    static class MaximumSizeParser extends LongParser
    {
        @Override
        protected void parseLong(final CacheBuilderSpec spec, final long value) {
            Preconditions.checkArgument(spec.maximumSize == null, "maximum size was already set to ", new Object[] { spec.maximumSize });
            Preconditions.checkArgument(spec.maximumWeight == null, "maximum weight was already set to ", new Object[] { spec.maximumWeight });
            spec.maximumSize = value;
        }
    }
    
    static class MaximumWeightParser extends LongParser
    {
        @Override
        protected void parseLong(final CacheBuilderSpec spec, final long value) {
            Preconditions.checkArgument(spec.maximumWeight == null, "maximum weight was already set to ", new Object[] { spec.maximumWeight });
            Preconditions.checkArgument(spec.maximumSize == null, "maximum size was already set to ", new Object[] { spec.maximumSize });
            spec.maximumWeight = value;
        }
    }
    
    static class ConcurrencyLevelParser extends IntegerParser
    {
        @Override
        protected void parseInteger(final CacheBuilderSpec spec, final int value) {
            Preconditions.checkArgument(spec.concurrencyLevel == null, "concurrency level was already set to ", new Object[] { spec.concurrencyLevel });
            spec.concurrencyLevel = value;
        }
    }
    
    static class KeyStrengthParser implements ValueParser
    {
        private final LocalCache.Strength strength;
        
        public KeyStrengthParser(final LocalCache.Strength strength) {
            this.strength = strength;
        }
        
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, @Nullable final String value) {
            Preconditions.checkArgument(value == null, "key %s does not take values", new Object[] { key });
            Preconditions.checkArgument(spec.keyStrength == null, "%s was already set to %s", new Object[] { key, spec.keyStrength });
            spec.keyStrength = this.strength;
        }
    }
    
    static class ValueStrengthParser implements ValueParser
    {
        private final LocalCache.Strength strength;
        
        public ValueStrengthParser(final LocalCache.Strength strength) {
            this.strength = strength;
        }
        
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, @Nullable final String value) {
            Preconditions.checkArgument(value == null, "key %s does not take values", new Object[] { key });
            Preconditions.checkArgument(spec.valueStrength == null, "%s was already set to %s", new Object[] { key, spec.valueStrength });
            spec.valueStrength = this.strength;
        }
    }
    
    static class RecordStatsParser implements ValueParser
    {
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, @Nullable final String value) {
            Preconditions.checkArgument(value == null, (Object)"recordStats does not take values");
            Preconditions.checkArgument(spec.recordStats == null, (Object)"recordStats already set");
            spec.recordStats = true;
        }
    }
    
    abstract static class DurationParser implements ValueParser
    {
        protected abstract void parseDuration(final CacheBuilderSpec p0, final long p1, final TimeUnit p2);
        
        @Override
        public void parse(final CacheBuilderSpec spec, final String key, final String value) {
            Preconditions.checkArgument(value != null && !value.isEmpty(), "value of key %s omitted", new Object[] { key });
            try {
                final char lastChar = value.charAt(value.length() - 1);
                TimeUnit timeUnit = null;
                switch (lastChar) {
                    case 'd': {
                        timeUnit = TimeUnit.DAYS;
                        break;
                    }
                    case 'h': {
                        timeUnit = TimeUnit.HOURS;
                        break;
                    }
                    case 'm': {
                        timeUnit = TimeUnit.MINUTES;
                        break;
                    }
                    case 's': {
                        timeUnit = TimeUnit.SECONDS;
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException(String.format("key %s invalid format.  was %s, must end with one of [dDhHmMsS]", key, value));
                    }
                }
                final long duration = Long.parseLong(value.substring(0, value.length() - 1));
                this.parseDuration(spec, duration, timeUnit);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", key, value));
            }
        }
    }
    
    static class AccessDurationParser extends DurationParser
    {
        @Override
        protected void parseDuration(final CacheBuilderSpec spec, final long duration, final TimeUnit unit) {
            Preconditions.checkArgument(spec.accessExpirationTimeUnit == null, (Object)"expireAfterAccess already set");
            spec.accessExpirationDuration = duration;
            spec.accessExpirationTimeUnit = unit;
        }
    }
    
    static class WriteDurationParser extends DurationParser
    {
        @Override
        protected void parseDuration(final CacheBuilderSpec spec, final long duration, final TimeUnit unit) {
            Preconditions.checkArgument(spec.writeExpirationTimeUnit == null, (Object)"expireAfterWrite already set");
            spec.writeExpirationDuration = duration;
            spec.writeExpirationTimeUnit = unit;
        }
    }
    
    static class RefreshDurationParser extends DurationParser
    {
        @Override
        protected void parseDuration(final CacheBuilderSpec spec, final long duration, final TimeUnit unit) {
            Preconditions.checkArgument(spec.refreshTimeUnit == null, (Object)"refreshAfterWrite already set");
            spec.refreshDuration = duration;
            spec.refreshTimeUnit = unit;
        }
    }
    
    private interface ValueParser
    {
        void parse(CacheBuilderSpec p0, String p1, @Nullable String p2);
    }
}

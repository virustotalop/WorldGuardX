/*
 * Copyright (C) 2011 The Guava Authors
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

import java.util.concurrent.ConcurrentMap;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

/**
 * This class provides a skeletal implementation of the {@code Cache} interface to minimize the
 * effort required to implement this interface.
 *
 * <p>To implement a cache, the programmer needs only to extend this class and provide an
 * implementation for the {@link #put} and {@link #getIfPresent} methods. {@link #getAllPresent} is
 * implemented in terms of {@link #getIfPresent}; {@link #putAll} is implemented in terms of
 * {@link #put}, {@link #invalidateAll(Iterable)} is implemented in terms of {@link #invalidate}.
 * The method {@link #cleanUp} is a no-op. All other methods throw an
 * {@link UnsupportedOperationException}.
 *
 * @author Charles Fry
 * @since 10.0
 */

@Beta
@GwtCompatible
public abstract class AbstractCache<K, V> implements Cache<K, V>
{
	
	/**
	 * @since 11.0
	 */
    @Override
    public V get(final K key, final Callable<? extends V> valueLoader) throws ExecutionException {
        throw new UnsupportedOperationException();
    }
    
    /**
     * This implementation of {@code getAllPresent} lacks any insight into the internal cache data
     * structure, and is thus forced to return the query keys instead of the cached keys. This is only
     * possible with an unsafe cast which requires {@code keys} to actually be of type {@code K}.
     *
     * {@inheritDoc}
     *
     * @since 11.0
     */
    @Override
    public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
      Map<K, V> result = Maps.newLinkedHashMap();
      for (Object key : keys) {
        if (!result.containsKey(key)) {
          @SuppressWarnings("unchecked")
          K castKey = (K) key;
          result.put(castKey, getIfPresent(key));
        }
      }
      return ImmutableMap.copyOf(result);
    }
    
    @Override
    public void put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        for (final Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void cleanUp() {
    }
    
    @Override
    public long size() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void invalidate(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void invalidateAll(final Iterable<?> keys) {
        for (final Object key : keys) {
            this.invalidate(key);
        }
    }
    
    @Override
    public void invalidateAll() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public CacheStats stats() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ConcurrentMap<K, V> asMap() {
        throw new UnsupportedOperationException();
    }
    
    @Beta
    public static final class SimpleStatsCounter implements StatsCounter
    {
        private final LongAddable hitCount;
        private final LongAddable missCount;
        private final LongAddable loadSuccessCount;
        private final LongAddable loadExceptionCount;
        private final LongAddable totalLoadTime;
        private final LongAddable evictionCount;
        
        public SimpleStatsCounter() {
            this.hitCount = LongAddables.create();
            this.missCount = LongAddables.create();
            this.loadSuccessCount = LongAddables.create();
            this.loadExceptionCount = LongAddables.create();
            this.totalLoadTime = LongAddables.create();
            this.evictionCount = LongAddables.create();
        }
        
        @Override
        public void recordHits(final int count) {
            this.hitCount.add(count);
        }
        
        @Override
        public void recordMisses(final int count) {
            this.missCount.add(count);
        }
        
        @Override
        public void recordLoadSuccess(final long loadTime) {
            this.loadSuccessCount.increment();
            this.totalLoadTime.add(loadTime);
        }
        
        @Override
        public void recordLoadException(final long loadTime) {
            this.loadExceptionCount.increment();
            this.totalLoadTime.add(loadTime);
        }
        
        @Override
        public void recordEviction() {
            this.evictionCount.increment();
        }
        
        @Override
        public CacheStats snapshot() {
            return new CacheStats(this.hitCount.sum(), this.missCount.sum(), this.loadSuccessCount.sum(), this.loadExceptionCount.sum(), this.totalLoadTime.sum(), this.evictionCount.sum());
        }
        
        public void incrementBy(final StatsCounter other) {
            final CacheStats otherStats = other.snapshot();
            this.hitCount.add(otherStats.hitCount());
            this.missCount.add(otherStats.missCount());
            this.loadSuccessCount.add(otherStats.loadSuccessCount());
            this.loadExceptionCount.add(otherStats.loadExceptionCount());
            this.totalLoadTime.add(otherStats.totalLoadTime());
            this.evictionCount.add(otherStats.evictionCount());
        }
    }
    
    @Beta
    public interface StatsCounter
    {
        void recordHits(int p0);
        
        void recordMisses(int p0);
        
        void recordLoadSuccess(long p0);
        
        void recordLoadException(long p0);
        
        void recordEviction();
        
        CacheStats snapshot();
    }
}

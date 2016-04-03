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

import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentMap;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingObject;

@Beta
public abstract class ForwardingCache<K, V> extends ForwardingObject implements Cache<K, V>
{
    protected abstract Cache<K, V> delegate();
    
    @Nullable
    public V getIfPresent(final Object key) {
        return this.delegate().getIfPresent(key);
    }
    
    public V get(final K key, final Callable<? extends V> valueLoader) throws ExecutionException {
        return this.delegate().get(key, valueLoader);
    }
    
    public ImmutableMap<K, V> getAllPresent(final Iterable<?> keys) {
        return this.delegate().getAllPresent(keys);
    }
    
    public void put(final K key, final V value) {
        this.delegate().put(key, value);
    }
    
    public void putAll(final Map<? extends K, ? extends V> m) {
        this.delegate().putAll(m);
    }
    
    public void invalidate(final Object key) {
        this.delegate().invalidate(key);
    }
    
    public void invalidateAll(final Iterable<?> keys) {
        this.delegate().invalidateAll(keys);
    }
    
    public void invalidateAll() {
        this.delegate().invalidateAll();
    }
    
    public long size() {
        return this.delegate().size();
    }
    
    public CacheStats stats() {
        return this.delegate().stats();
    }
    
    public ConcurrentMap<K, V> asMap() {
        return this.delegate().asMap();
    }
    
    public void cleanUp() {
        this.delegate().cleanUp();
    }
    
    @Beta
    public abstract static class SimpleForwardingCache<K, V> extends ForwardingCache<K, V>
    {
        private final Cache<K, V> delegate;
        
        protected SimpleForwardingCache(final Cache<K, V> delegate) {
            this.delegate = (Cache<K, V>)Preconditions.checkNotNull((Object)delegate);
        }
        
        @Override
        protected final Cache<K, V> delegate() {
            return this.delegate;
        }
    }
}

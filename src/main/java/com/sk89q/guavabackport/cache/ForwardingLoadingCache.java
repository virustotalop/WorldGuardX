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
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import com.google.common.annotations.Beta;

@Beta
public abstract class ForwardingLoadingCache<K, V> extends ForwardingCache<K, V> implements LoadingCache<K, V>
{
    @Override
    protected abstract LoadingCache<K, V> delegate();
    
    @Override
    public V get(final K key) throws ExecutionException {
        return this.delegate().get(key);
    }
    
    @Override
    public V getUnchecked(final K key) {
        return this.delegate().getUnchecked(key);
    }
    
    @Override
    public ImmutableMap<K, V> getAll(final Iterable<? extends K> keys) throws ExecutionException {
        return this.delegate().getAll(keys);
    }
    
    @Override
    public V apply(final K key) {
        return this.delegate().apply(key);
    }
    
    @Override
    public void refresh(final K key) {
        this.delegate().refresh(key);
    }
    
    @Beta
    public abstract static class SimpleForwardingLoadingCache<K, V> extends ForwardingLoadingCache<K, V>
    {
        private final LoadingCache<K, V> delegate;
        
        protected SimpleForwardingLoadingCache(final LoadingCache<K, V> delegate) {
            this.delegate = (LoadingCache<K, V>)Preconditions.checkNotNull((Object)delegate);
        }
        
        @Override
        protected final LoadingCache<K, V> delegate() {
            return this.delegate;
        }
    }
}

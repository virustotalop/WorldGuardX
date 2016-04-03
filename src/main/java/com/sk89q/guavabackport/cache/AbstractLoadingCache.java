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

import java.util.Map;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.annotations.Beta;

@Beta
public abstract class AbstractLoadingCache<K, V> extends AbstractCache<K, V> implements LoadingCache<K, V>
{
    @Override
    public V getUnchecked(final K key) {
        try {
            return this.get(key);
        }
        catch (ExecutionException e) {
            throw new UncheckedExecutionException(e.getCause());
        }
    }
    
    @Override
    public ImmutableMap<K, V> getAll(final Iterable<? extends K> keys) throws ExecutionException {
        final Map<K, V> result = Maps.newLinkedHashMap();
        for (final K key : keys) {
            if (!result.containsKey(key)) {
                result.put(key, this.get(key));
            }
        }
        return ImmutableMap.copyOf(result);
    }
    
    @Override
    public final V apply(final K key) {
        return this.getUnchecked(key);
    }
    
    @Override
    public void refresh(final K key) {
        throw new UnsupportedOperationException();
    }
}

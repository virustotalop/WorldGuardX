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

import java.util.concurrent.ConcurrentMap;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public interface Cache<K, V>
{
    @Nullable
    V getIfPresent(Object p0);
    
    V get(K p0, Callable<? extends V> p1) throws ExecutionException;
    
    ImmutableMap<K, V> getAllPresent(Iterable<?> p0);
    
    void put(K p0, V p1);
    
    void putAll(Map<? extends K, ? extends V> p0);
    
    void invalidate(Object p0);
    
    void invalidateAll(Iterable<?> p0);
    
    void invalidateAll();
    
    long size();
    
    CacheStats stats();
    
    ConcurrentMap<K, V> asMap();
    
    void cleanUp();
}

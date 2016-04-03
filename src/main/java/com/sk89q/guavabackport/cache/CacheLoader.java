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

import java.io.Serializable;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import com.google.common.base.Supplier;
import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import java.util.Map;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.util.concurrent.Futures;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public abstract class CacheLoader<K, V>
{
    public abstract V load(final K p0) throws Exception;
    
    @GwtIncompatible("Futures")
    public ListenableFuture<V> reload(final K key, final V oldValue) throws Exception {
        Preconditions.checkNotNull((Object)key);
        Preconditions.checkNotNull((Object)oldValue);
        return (ListenableFuture<V>)Futures.immediateFuture(this.load(key));
    }
    
    public Map<K, V> loadAll(final Iterable<? extends K> keys) throws Exception {
        throw new UnsupportedLoadingOperationException();
    }
    
    @Beta
    public static <K, V> CacheLoader<K, V> from(final Function<K, V> function) {
        return new FunctionToCacheLoader<K, V>(function);
    }
    
    @Beta
    public static <V> CacheLoader<Object, V> from(final Supplier<V> supplier) {
        return (CacheLoader<Object, V>)new SupplierToCacheLoader((com.google.common.base.Supplier<Object>)supplier);
    }
    
    @Beta
    @GwtIncompatible("Executor + Futures")
    public static <K, V> CacheLoader<K, V> asyncReloading(final CacheLoader<K, V> loader, final Executor executor) {
        Preconditions.checkNotNull((Object)loader);
        Preconditions.checkNotNull((Object)executor);
        return new CacheLoader<K, V>() {
            @Override
            public V load(final K key) throws Exception {
                return loader.load(key);
            }
            
            @Override
            public ListenableFuture<V> reload(final K key, final V oldValue) throws Exception {
                final ListenableFutureTask<V> task = (ListenableFutureTask<V>)ListenableFutureTask.create((Callable)new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return (V)loader.reload(key, oldValue).get();
                    }
                });
                executor.execute((Runnable)task);
                return (ListenableFuture<V>)task;
            }
            
            @Override
            public Map<K, V> loadAll(final Iterable<? extends K> keys) throws Exception {
                return loader.loadAll(keys);
            }
        };
    }
    
    private static final class FunctionToCacheLoader<K, V> extends CacheLoader<K, V> implements Serializable
    {
        private final Function<K, V> computingFunction;
        private static final long serialVersionUID = 0L;
        
        public FunctionToCacheLoader(final Function<K, V> computingFunction) {
            this.computingFunction = (Function<K, V>)Preconditions.checkNotNull((Object)computingFunction);
        }
        
        @Override
        public V load(final K key) {
            return (V)this.computingFunction.apply((K) Preconditions.checkNotNull((Object)key));
        }
    }
    
    private static final class SupplierToCacheLoader<V> extends CacheLoader<Object, V> implements Serializable
    {
        private final Supplier<V> computingSupplier;
        private static final long serialVersionUID = 0L;
        
        public SupplierToCacheLoader(final Supplier<V> computingSupplier) {
            this.computingSupplier = (Supplier<V>)Preconditions.checkNotNull((Object)computingSupplier);
        }
        
        @Override
        public V load(final Object key) {
            Preconditions.checkNotNull(key);
            return (V)this.computingSupplier.get();
        }
    }
    
    static final class UnsupportedLoadingOperationException extends UnsupportedOperationException
    {
    }
    
    public static final class InvalidCacheLoadException extends RuntimeException
    {
        public InvalidCacheLoadException(final String message) {
            super(message);
        }
    }
}

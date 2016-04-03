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

import java.util.concurrent.atomic.AtomicLong;
import com.google.common.base.Supplier;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class LongAddables
{
    private static final Supplier<LongAddable> SUPPLIER;
    
    public static LongAddable create() {
        return (LongAddable)LongAddables.SUPPLIER.get();
    }
    
    static {
        Supplier<LongAddable> supplier;
        try {
            new LongAdder();
            supplier = (Supplier<LongAddable>)new Supplier<LongAddable>() {
                public LongAddable get() {
                    return new LongAdder();
                }
            };
        }
        catch (Throwable t) {
            supplier = (Supplier<LongAddable>)new Supplier<LongAddable>() {
                public LongAddable get() {
                    return new PureJavaLongAddable();
                }
            };
        }
        SUPPLIER = supplier;
    }
    
    private static final class PureJavaLongAddable extends AtomicLong implements LongAddable
    {
        @Override
        public void increment() {
            this.getAndIncrement();
        }
        
        @Override
        public void add(final long x) {
            this.getAndAdd(x);
        }
        
        @Override
        public long sum() {
            return this.get();
        }
    }
}

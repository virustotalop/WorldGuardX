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
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class MoreObjects
{
    public static <T> T firstNonNull(@Nullable final T first, @Nullable final T second) {
        return (T)((first != null) ? first : Preconditions.checkNotNull((Object)second));
    }
    
    public static ToStringHelper toStringHelper(final Object self) {
        return new ToStringHelper(self.getClass().getSimpleName());
    }
    
    public static ToStringHelper toStringHelper(final Class<?> clazz) {
        return new ToStringHelper(clazz.getSimpleName());
    }
    
    public static ToStringHelper toStringHelper(final String className) {
        return new ToStringHelper(className);
    }
    
    public static final class ToStringHelper
    {
        private final String className;
        private ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitNullValues;
        
        private ToStringHelper(final String className) {
            this.holderHead = new ValueHolder();
            this.holderTail = this.holderHead;
            this.omitNullValues = false;
            this.className = (String)Preconditions.checkNotNull((Object)className);
        }
        
        public ToStringHelper omitNullValues() {
            this.omitNullValues = true;
            return this;
        }
        
        public ToStringHelper add(final String name, @Nullable final Object value) {
            return this.addHolder(name, value);
        }
        
        public ToStringHelper add(final String name, final boolean value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper add(final String name, final char value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper add(final String name, final double value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper add(final String name, final float value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper add(final String name, final int value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper add(final String name, final long value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper addValue(@Nullable final Object value) {
            return this.addHolder(value);
        }
        
        public ToStringHelper addValue(final boolean value) {
            return this.addHolder(String.valueOf(value));
        }
        
        public ToStringHelper addValue(final char value) {
            return this.addHolder(String.valueOf(value));
        }
        
        public ToStringHelper addValue(final double value) {
            return this.addHolder(String.valueOf(value));
        }
        
        public ToStringHelper addValue(final float value) {
            return this.addHolder(String.valueOf(value));
        }
        
        public ToStringHelper addValue(final int value) {
            return this.addHolder(String.valueOf(value));
        }
        
        public ToStringHelper addValue(final long value) {
            return this.addHolder(String.valueOf(value));
        }
        
        @Override
        public String toString() {
            final boolean omitNullValuesSnapshot = this.omitNullValues;
            String nextSeparator = "";
            final StringBuilder builder = new StringBuilder(32).append(this.className).append('{');
            for (ValueHolder valueHolder = this.holderHead.next; valueHolder != null; valueHolder = valueHolder.next) {
                if (!omitNullValuesSnapshot || valueHolder.value != null) {
                    builder.append(nextSeparator);
                    nextSeparator = ", ";
                    if (valueHolder.name != null) {
                        builder.append(valueHolder.name).append('=');
                    }
                    builder.append(valueHolder.value);
                }
            }
            return builder.append('}').toString();
        }
        
        private ValueHolder addHolder() {
            final ValueHolder valueHolder = new ValueHolder();
            final ValueHolder holderTail = this.holderTail;
            final ValueHolder valueHolder2 = valueHolder;
            holderTail.next = valueHolder2;
            this.holderTail = valueHolder2;
            return valueHolder;
        }
        
        private ToStringHelper addHolder(@Nullable final Object value) {
            final ValueHolder valueHolder = this.addHolder();
            valueHolder.value = value;
            return this;
        }
        
        private ToStringHelper addHolder(final String name, @Nullable final Object value) {
            final ValueHolder valueHolder = this.addHolder();
            valueHolder.value = value;
            valueHolder.name = (String)Preconditions.checkNotNull((Object)name);
            return this;
        }
        
        private static final class ValueHolder
        {
            String name;
            Object value;
            ValueHolder next;
        }
    }
}

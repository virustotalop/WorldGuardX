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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import com.google.common.annotations.Beta;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class Equivalence<T>
{
    public final boolean equivalent(@Nullable final T a, @Nullable final T b) {
        return a == b || (a != null && b != null && this.doEquivalent(a, b));
    }
    
    protected abstract boolean doEquivalent(final T p0, final T p1);
    
    public final int hash(@Nullable final T t) {
        if (t == null) {
            return 0;
        }
        return this.doHash(t);
    }
    
    protected abstract int doHash(final T p0);
    
    public final <S extends T> Wrapper<S> wrap(@Nullable final S reference) {
        return new Wrapper<S>(this, reference);
    }
    
    @Beta
    public final Predicate<T> equivalentTo(@Nullable final T target) {
        return (Predicate<T>)new EquivalentToPredicate((Equivalence<Object>)this, target);
    }
    
    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }
    
    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }
    
    public static final class Wrapper<T> implements Serializable
    {
        private final Equivalence<? super T> equivalence;
        @Nullable
        private final T reference;
        private static final long serialVersionUID = 0L;
        
        private Wrapper(final Equivalence<? super T> equivalence, @Nullable final T reference) {
            this.equivalence = (Equivalence<? super T>)Preconditions.checkNotNull((Object)equivalence);
            this.reference = reference;
        }
        
        @Nullable
        public T get() {
            return this.reference;
        }
        
        @Override
        public boolean equals(@Nullable final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Wrapper) {
                final Wrapper<?> that = (Wrapper<?>)obj;
                if (this.equivalence.equals(that.equivalence)) {
                    final Equivalence<Object> equivalence = (Equivalence<Object>)this.equivalence;
                    return equivalence.equivalent(this.reference, that.reference);
                }
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.equivalence.hash(this.reference);
        }
        
        @Override
        public String toString() {
            return this.equivalence + ".wrap(" + this.reference + ")";
        }
    }
    
    private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable
    {
        private final Equivalence<T> equivalence;
        @Nullable
        private final T target;
        private static final long serialVersionUID = 0L;
        
        EquivalentToPredicate(final Equivalence<T> equivalence, @Nullable final T target) {
            this.equivalence = (Equivalence<T>)Preconditions.checkNotNull((Object)equivalence);
            this.target = target;
        }
        
        public boolean apply(@Nullable final T input) {
            return this.equivalence.equivalent(input, this.target);
        }
        
        @Override
        public boolean equals(@Nullable final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof EquivalentToPredicate) {
                final EquivalentToPredicate<?> that = (EquivalentToPredicate<?>)obj;
                return this.equivalence.equals(that.equivalence) && Objects.equal((Object)this.target, (Object)that.target);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(new Object[] { this.equivalence, this.target });
        }
        
        @Override
        public String toString() {
            return this.equivalence + ".equivalentTo(" + this.target + ")";
        }
    }
    
    static final class Equals extends Equivalence<Object> implements Serializable
    {
        static final Equals INSTANCE;
        private static final long serialVersionUID = 1L;
        
        @Override
        protected boolean doEquivalent(final Object a, final Object b) {
            return a.equals(b);
        }
        
        @Override
        protected int doHash(final Object o) {
            return o.hashCode();
        }
        
        private Object readResolve() {
            return Equals.INSTANCE;
        }
        
        static {
            INSTANCE = new Equals();
        }
    }
    
    static final class Identity extends Equivalence<Object> implements Serializable
    {
        static final Identity INSTANCE;
        private static final long serialVersionUID = 1L;
        
        @Override
        protected boolean doEquivalent(final Object a, final Object b) {
            return false;
        }
        
        @Override
        protected int doHash(final Object o) {
            return System.identityHashCode(o);
        }
        
        private Object readResolve() {
            return Identity.INSTANCE;
        }
        
        static {
            INSTANCE = new Identity();
        }
    }
}

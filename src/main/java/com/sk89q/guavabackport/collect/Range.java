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

package com.sk89q.guavabackport.collect;

import com.google.common.collect.ComparisonChain;
import javax.annotation.Nullable;
import com.google.common.collect.DiscreteDomain;
import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedSet;
import com.google.common.collect.Iterables;
import com.google.common.base.Preconditions;
import com.google.common.collect.BoundType;
import com.google.common.collect.Ordering;
import com.google.common.base.Function;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import com.google.common.base.Predicate;

@GwtCompatible
public final class Range<C extends Comparable> implements Predicate<C>, Serializable
{
    private static final Function<Range, Cut> LOWER_BOUND_FN;
    private static final Function<Range, Cut> UPPER_BOUND_FN;
    static final Ordering<Range<?>> RANGE_LEX_ORDERING;
    private static final Range<Comparable> ALL;
    final Cut<C> lowerBound;
    final Cut<C> upperBound;
    private static final long serialVersionUID = 0L;
    
    @SuppressWarnings("unchecked")
    static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn() {
      return (Function) LOWER_BOUND_FN;
    }
    
    @SuppressWarnings("unchecked")
    static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn() {
      return (Function) UPPER_BOUND_FN;
    }
    
    static <C extends Comparable<?>> Range<C> create(final Cut<C> lowerBound, final Cut<C> upperBound) {
        return new Range<C>(lowerBound, upperBound);
    }
    
    public static <C extends Comparable<?>> Range<C> open(final C lower, final C upper) {
        return create((Cut<C>)Cut.aboveValue((C)lower), (Cut<C>)Cut.belowValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> closed(final C lower, final C upper) {
        return create((Cut<C>)Cut.belowValue((C)lower), (Cut<C>)Cut.aboveValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> closedOpen(final C lower, final C upper) {
        return create((Cut<C>)Cut.belowValue((C)lower), (Cut<C>)Cut.belowValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> openClosed(final C lower, final C upper) {
        return create((Cut<C>)Cut.aboveValue((C)lower), (Cut<C>)Cut.aboveValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> range(final C lower, final BoundType lowerType, final C upper, final BoundType upperType) {
        Preconditions.checkNotNull((Object)lowerType);
        Preconditions.checkNotNull((Object)upperType);
        final Cut<C> lowerBound = (lowerType == BoundType.OPEN) ? Cut.aboveValue(lower) : Cut.belowValue(lower);
        final Cut<C> upperBound = (upperType == BoundType.OPEN) ? Cut.belowValue(upper) : Cut.aboveValue(upper);
        return create(lowerBound, upperBound);
    }
    
    public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) {
        return create(Cut.<C>belowAll(), Cut.belowValue(endpoint));
      }
    
    public static <C extends Comparable<?>> Range<C> atMost(C endpoint) {
        return create(Cut.<C>belowAll(), Cut.aboveValue(endpoint));
      }
    
    public static <C extends Comparable<?>> Range<C> upTo(final C endpoint, final BoundType boundType) {
        switch (boundType) {
            case OPEN: {
                return lessThan(endpoint);
            }
            case CLOSED: {
                return atMost(endpoint);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) {
        return create(Cut.aboveValue(endpoint), Cut.<C>aboveAll());
    }
    
    public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) {
        return create(Cut.belowValue(endpoint), Cut.<C>aboveAll());
      }
    
    public static <C extends Comparable<?>> Range<C> downTo(final C endpoint, final BoundType boundType) {
        switch (boundType) {
            case OPEN: {
                return greaterThan(endpoint);
            }
            case CLOSED: {
                return atLeast(endpoint);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    public static <C extends Comparable<?>> Range<C> all() {
        return (Range<C>)Range.ALL;
    }
    
    public static <C extends Comparable<?>> Range<C> singleton(final C value) {
        return closed(value, value);
    }
    
    private Range(final Cut<C> lowerBound, final Cut<C> upperBound) {
        this.lowerBound = (Cut<C>)Preconditions.checkNotNull((Object)lowerBound);
        this.upperBound = (Cut<C>)Preconditions.checkNotNull((Object)upperBound);
        if (lowerBound.compareTo(upperBound) > 0 || lowerBound == Cut.aboveAll() || upperBound == Cut.belowAll()) {
            throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
        }
    }
    
    public boolean hasLowerBound() {
        return this.lowerBound != Cut.belowAll();
    }
    
    public C lowerEndpoint() {
        return this.lowerBound.endpoint();
    }
    
    public BoundType lowerBoundType() {
        return this.lowerBound.typeAsLowerBound();
    }
    
    public boolean hasUpperBound() {
        return this.upperBound != Cut.aboveAll();
    }
    
    public C upperEndpoint() {
        return this.upperBound.endpoint();
    }
    
    public BoundType upperBoundType() {
        return this.upperBound.typeAsUpperBound();
    }
    
    public boolean isEmpty() {
        return this.lowerBound.equals(this.upperBound);
    }
    
    public boolean contains(final C value) {
        Preconditions.checkNotNull((Object)value);
        return this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value);
    }
    
    @Deprecated
    public boolean apply(final C input) {
        return this.contains(input);
    }
    
    public boolean containsAll(final Iterable<? extends C> values) {
        if (Iterables.isEmpty((Iterable)values)) {
            return true;
        }
        if (values instanceof SortedSet) {
            final SortedSet<? extends C> set = cast(values);
            final Comparator<?> comparator = set.comparator();
            if (Ordering.natural().equals(comparator) || comparator == null) {
                return this.contains((C)set.first()) && this.contains((C)set.last());
            }
        }
        for (final C value : values) {
            if (!this.contains(value)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean encloses(final Range<C> other) {
        return this.lowerBound.compareTo(other.lowerBound) <= 0 && this.upperBound.compareTo(other.upperBound) >= 0;
    }
    
    public boolean isConnected(final Range<C> other) {
        return this.lowerBound.compareTo(other.upperBound) <= 0 && other.lowerBound.compareTo(this.upperBound) <= 0;
    }
    
    public Range<C> intersection(final Range<C> connectedRange) {
        final int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
        final int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
        if (lowerCmp >= 0 && upperCmp <= 0) {
            return this;
        }
        if (lowerCmp <= 0 && upperCmp >= 0) {
            return connectedRange;
        }
        final Cut<C> newLower = (lowerCmp >= 0) ? this.lowerBound : connectedRange.lowerBound;
        final Cut<C> newUpper = (upperCmp <= 0) ? this.upperBound : connectedRange.upperBound;
        return create(newLower, newUpper);
    }
    
    public Range<C> span(final Range<C> other) {
        final int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
        final int upperCmp = this.upperBound.compareTo(other.upperBound);
        if (lowerCmp <= 0 && upperCmp >= 0) {
            return this;
        }
        if (lowerCmp >= 0 && upperCmp <= 0) {
            return other;
        }
        final Cut<C> newLower = (lowerCmp <= 0) ? this.lowerBound : other.lowerBound;
        final Cut<C> newUpper = (upperCmp >= 0) ? this.upperBound : other.upperBound;
        return create(newLower, newUpper);
    }
    
    public Range<C> canonical(final DiscreteDomain<C> domain) {
        Preconditions.checkNotNull((Object)domain);
        final Cut<C> lower = this.lowerBound.canonical(domain);
        final Cut<C> upper = this.upperBound.canonical(domain);
        return (lower == this.lowerBound && upper == this.upperBound) ? this : create(lower, upper);
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object instanceof Range) {
            final Range<?> other = (Range<?>)object;
            return this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
    }
    
    @Override
    public String toString() {
        return toString(this.lowerBound, this.upperBound);
    }
    
    private static String toString(final Cut<?> lowerBound, final Cut<?> upperBound) {
        final StringBuilder sb = new StringBuilder(16);
        lowerBound.describeAsLowerBound(sb);
        sb.append('\u2025');
        upperBound.describeAsUpperBound(sb);
        return sb.toString();
    }
    
    private static <T> SortedSet<T> cast(final Iterable<T> iterable) {
        return (SortedSet<T>)(SortedSet)iterable;
    }
    
    Object readResolve() {
        if (this.equals(Range.ALL)) {
            return all();
        }
        return this;
    }
    
    static int compareOrThrow(final Comparable left, final Comparable right) {
        return left.compareTo(right);
    }
    
    static {
        LOWER_BOUND_FN = (Function)new Function<Range, Cut>() {
            public Cut apply(final Range range) {
                return range.lowerBound;
            }
        };
        UPPER_BOUND_FN = (Function)new Function<Range, Cut>() {
            public Cut apply(final Range range) {
                return range.upperBound;
            }
        };
        RANGE_LEX_ORDERING = new Ordering<Range<?>>() {
            public int compare(final Range<?> left, final Range<?> right) {
                return ComparisonChain.start().compare((Comparable)left.lowerBound, (Comparable)right.lowerBound).compare((Comparable)left.upperBound, (Comparable)right.upperBound).result();
            }
        };
        ALL = new Range<Comparable>((Cut<Comparable>)Cut.belowAll(), (Cut<Comparable>)Cut.aboveAll());
    }
}

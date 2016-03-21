/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.event.filter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.spongepowered.api.event.Listener;
import org.spongepowered.common.event.gen.DefineableClassLoader;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class FilterFactory {

    private final AtomicInteger id = new AtomicInteger();
    private final LoadingCache<Class<?>, DefineableClassLoader> clCache;
    private final LoadingCache<Method, Class<? extends EventFilter>> cache = CacheBuilder.newBuilder()
    		.weakKeys()   // !!
            .concurrencyLevel(1).weakValues().build(new CacheLoader<Method, Class<? extends EventFilter>>() {

                @Override
                public Class<? extends EventFilter> load(Method method) throws Exception {
                    return createClass(method);
                }
            });
    private final String targetPackage;

    public FilterFactory(String targetPackage, LoadingCache<Class<?>, DefineableClassLoader> clCache) {
        checkNotNull(targetPackage, "targetPackage");
        checkArgument(!targetPackage.isEmpty(), "targetPackage cannot be empty");
        this.targetPackage = targetPackage + '.';
        this.clCache = clCache;
    }

    public Class<? extends EventFilter> createFilter(Method method) throws Exception {
        if (method.getParameterCount() == 1 && method.getDeclaredAnnotations().length == 1
                && method.getDeclaredAnnotations()[0].annotationType().equals(Listener.class)) {
            return null;
        }
        return this.cache.get(method);
    }

    private Class<? extends EventFilter> createClass(Method method) {
        Class<?> handle = method.getDeclaringClass();
        Class<?> eventClass = method.getParameterTypes()[0];
        String name = this.targetPackage + eventClass.getSimpleName() + "Filter_" + handle.getSimpleName() + '_'
                + method.getName() + this.id.incrementAndGet();
        byte[] cls = FilterGenerator.getInstance().generateClass(name, method);
        DefineableClassLoader classLoader = clCache.getUnchecked(method.getDeclaringClass());
        return classLoader.defineClass(name, cls);
    }

}

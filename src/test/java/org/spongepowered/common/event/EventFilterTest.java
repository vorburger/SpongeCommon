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
package org.spongepowered.common.event;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.manipulator.mutable.entity.SkinData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.common.event.filter.FilterFactory;
import org.spongepowered.common.event.gen.DefineableClassLoader;
import org.spongepowered.common.event.listener.AllCauseListener;
import org.spongepowered.common.event.listener.BeforeAfterCauseListener;
import org.spongepowered.common.event.listener.CancelledListener;
import org.spongepowered.common.event.listener.DataHasListener;
import org.spongepowered.common.event.listener.DataSupportsListener;
import org.spongepowered.common.event.listener.DoubleListener;
import org.spongepowered.common.event.listener.FirstLastCauseListener;
import org.spongepowered.common.event.listener.IncludeExcludeListener;
import org.spongepowered.common.event.listener.InvalidCancelledListener;
import org.spongepowered.common.event.listener.InvalidIncludeExcludeListener;
import org.spongepowered.common.event.listener.NamedCauseListener;
import org.spongepowered.common.event.listener.RootListener;
import org.spongepowered.common.event.listener.SimpleListener;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Optional;

public class EventFilterTest {

	private final LoadingCache<Class<?>, DefineableClassLoader> clCache = CacheBuilder.newBuilder()
            .concurrencyLevel(1)
            .weakKeys()   // !!
            .weakValues()
            .build(new CacheLoader<Class<?>, DefineableClassLoader>() {

				@Override
				public DefineableClassLoader load(Class<?> key) throws Exception {
					return new DefineableClassLoader(key.getClassLoader());
				}
            });
    private final AnnotatedEventListener.Factory handlerFactory = new ClassEventListenerFactory("org.spongepowered.common.event.listener",
            new FilterFactory("org.spongepowered.common.event.filters", clCache), clCache);

    @Test
    public void testSimpleEvent() throws Exception {
        SimpleListener listener = new SimpleListener();
        AnnotatedEventListener annotatedEventListener = this.getListener(listener, "onEvent");
        annotatedEventListener.handle(new SubEvent(Cause.source(this).build()));

        Assert.assertTrue("Simple listener was not called!", listener.called);
    }

    @Test
    public void testDoubleListener() throws Exception {
        DoubleListener listener = new DoubleListener();
        AnnotatedEventListener annotatedEventListener = this.getListener(listener, "onEvent", ChangeBlockEvent.Break.class, Player.class, ItemStack.class);
    }

    @Test
    public void testCancelledEvent() throws Exception {
        CancelledListener listener = new CancelledListener();
        SubEvent event = new SubEvent(Cause.source(this).build());

        AnnotatedEventListener normalListener = this.getListener(listener, "normalListener");
        AnnotatedEventListener uncalledListener = this.getListener(listener, "uncalledListener");
        AnnotatedEventListener afterCancelledListener = this.getListener(listener, "afterCancelledListener");
        AnnotatedEventListener alwaysCalledListener = this.getListener(listener, "alwaysCalledListener");

        normalListener.handle(event);
        Assert.assertTrue("Un-annotated listener was not called when event was not cancelled!", listener.normalListenerWasCalled);

        alwaysCalledListener.handle(event);
        Assert.assertTrue("Listener annotated with @IsCancelled(Tristate.UNDEFINED) was not called when event was not cancelled!", listener.alwaysCalledWasCalled);

        event.setCancelled(true);
        listener.normalListenerWasCalled = false;

        /*
         * normalListener.handle(event); Assert.assertFalse(
         * "Un-annotated listener was called when event was cancelled!", listener.normalListenerWasCalled);
         */

        uncalledListener.handle(event);
        Assert.assertFalse("Listener annotated with @IsCancelled(Tristate.FALSE) was called!", listener.uncalledWasCalled);

        afterCancelledListener.handle(event);
        Assert.assertTrue("Listener annotated with @IsCancelled was not called!", listener.afterCancelledWasCalled);

        alwaysCalledListener.handle(event);
        Assert.assertTrue("Listener annotated with @IsCancelled(Tristate.UNDEFINED) was not called!", listener.alwaysCalledWasCalled);
    }

    @Test(expected = RuntimeException.class)
    public void testCancelledEvent_InvalidListener() throws Exception {
        this.getListener(new InvalidCancelledListener(), "onEvent", UncancellableEvent.class);
    }

    @Test
    public void testIncludeExcludeListener() throws Exception {
        IncludeExcludeListener listener = new IncludeExcludeListener();
        AnnotatedEventListener includeListener = this.getListener(listener, "includeListener", TestEvent.class);
        AnnotatedEventListener excludeListener = this.getListener(listener, "excludeListener", TestEvent.class);

        TestEvent testEvent = new TestEvent(Cause.source(this).build());
        SubEvent subEvent = new SubEvent(Cause.source(this).build());

        includeListener.handle(testEvent);
        Assert.assertFalse("Listener annotated with @Include was called!", listener.includeListenerCalled);

        includeListener.handle(subEvent);
        Assert.assertTrue("Listener annotated with @Include was not called!", listener.includeListenerCalled);

        excludeListener.handle(subEvent);
        Assert.assertFalse("Listener annotated with @Exclude was called!", listener.exlcudeListenerCalled);

        excludeListener.handle(testEvent);
        Assert.assertTrue("Listener annotated with @Exclude was not called!", listener.exlcudeListenerCalled);
    }

    @Test(expected = RuntimeException.class)
    public void testIncludeExcludeListener_InvalidListener() throws Exception {
        this.getListener(new InvalidIncludeExcludeListener(), "invalidListener", TestEvent.class);
    }

    @Test
    public void testFirstLastCauseListener() throws Exception {
        FirstLastCauseListener listener = new FirstLastCauseListener();
        AnnotatedEventListener firstCauseListener = this.getListener(listener, "firstCauseListener", SubEvent.class, Player.class);
        AnnotatedEventListener lastCauseListener = this.getListener(listener, "lastCauseListener", SubEvent.class, Player.class);
        AnnotatedEventListener firstCauseListenerInc = this.getListener(listener, "firstCauseListenerInclude", SubEvent.class, Player.class);
        AnnotatedEventListener firstCauseListenerEx = this.getListener(listener, "firstCauseListenerExclude", SubEvent.class, Player.class);
        AnnotatedEventListener lastCauseListenerInc = this.getListener(listener, "lastCauseListenerInclude", SubEvent.class, Entity.class);
        AnnotatedEventListener lastCauseListenerEx = this.getListener(listener, "lastCauseListenerExclude", SubEvent.class, Entity.class);

        Cause cause1 = Cause.of(NamedCause.of("foo", "Foo"), NamedCause.of("eh", 'a'));
        Cause cause2 = Cause.of(NamedCause.of("foo", "Foo"), NamedCause.of("Player", mock(Player.class)), NamedCause.of("Seven", 7));

        SubEvent event1 = new SubEvent(cause1);
        SubEvent event2 = new SubEvent(cause2);

        firstCauseListener.handle(event1);
        Assert.assertFalse("Listener was called with improper @First parameter!", listener.firstCauseCalled);

        firstCauseListener.handle(event2);
        Assert.assertTrue("Listener with @First parameter was not called when proper Cause was provided!", listener.firstCauseCalled);

        lastCauseListener.handle(event1);
        Assert.assertFalse("Listener was called with improper @Last parameter!", listener.lastCauseCalled);

        lastCauseListener.handle(event2);
        Assert.assertTrue("Listener with @Last parameter was not called when proper Cause was provided!", listener.lastCauseCalled);

        firstCauseListenerInc.handle(event2);
        Assert.assertTrue("Listener with @First with inclusions was not called when proper Cause was provided!", listener.firstCauseCalledInc);

        firstCauseListenerEx.handle(event2);
        Assert.assertFalse("Listener with @First with exclusions was called when an improper Cause was provided!", listener.firstCauseCalledEx);

        lastCauseListenerInc.handle(event2);
        Assert.assertTrue("Listener with @Last with inclusions was not called when proper Cause was provided!", listener.lastCauseCalledInc);

        lastCauseListenerEx.handle(event2);
        Assert.assertFalse("Listener with @Last with exclusions was called when an improper Cause was provided!", listener.lastCauseCalledEx);
    }

    @Test
    public void testAllCauseListener() throws Exception {
        AllCauseListener listener = new AllCauseListener();
        AnnotatedEventListener emptyListener = this.getListener(listener, "emptyListener", SubEvent.class, BiomeTypes[].class);
        AnnotatedEventListener allCauseListener = this.getListener(listener, "allCauseListener", SubEvent.class, Player[].class);

        Cause cause1 = Cause.source(this).build();
        Cause cause2 = Cause.of(NamedCause.of("Player", mock(Player.class)), NamedCause.of("String", "Hi"), NamedCause.of("OtherPlayer", mock(Player.class)));

        SubEvent event1 = new SubEvent(cause1);
        SubEvent event2 = new SubEvent(cause2);

        emptyListener.handle(event1);
        Assert.assertTrue("Listener with @All(ignoreEmpty = false) was not called!", listener.emptyListenerCalled);

        listener.emptyListenerCalled = false;

        emptyListener.handle(event2);
        Assert.assertTrue("Listener with @All(ignoreEmpty = false) was not called!", listener.emptyListenerCalled);

        allCauseListener.handle(event1);
        Assert.assertFalse("Listener with @All was called with improper array parameter!", listener.allCauseListenerCalled);

        allCauseListener.handle(event2);
        Assert.assertTrue("Listener with @All was not called when proper Cause was provided!", listener.allCauseListenerCalled);
    }

    @Test
    public void testDataSupportsListener() throws Exception {
        DataSupportsListener listener = new DataSupportsListener();
        AnnotatedEventListener supportsSkinListener = this.getListener(listener, "supportsSkinListener", SubEvent.class, Player.class);
        AnnotatedEventListener inverseSupportsSkinListener = this.getListener(listener, "inverseSupportsSkinListener", SubEvent.class, Player.class);

        Player player1 = mock(Player.class);
        when(player1.supports(SkinData.class)).thenReturn(false);

        Player player2 = mock(Player.class);
        when(player2.supports(SkinData.class)).thenReturn(true);

        Cause cause1 = Cause.source(player1).build();
        Cause cause2 = Cause.source(player2).build();

        SubEvent event1 = new SubEvent(cause1);
        SubEvent event2 = new SubEvent(cause2);

        supportsSkinListener.handle(event1);
        Assert.assertFalse("Listener with @Supports was called!", listener.supportsSkinListenerCalled);

        supportsSkinListener.handle(event2);
        Assert.assertTrue("Listener with @Supports was not called!", listener.supportsSkinListenerCalled);

        inverseSupportsSkinListener.handle(event2);
        Assert.assertFalse("Inverse listener with @Supports was called!", listener.inverseSupportsSkinListenerCalled);

        inverseSupportsSkinListener.handle(event1);
        Assert.assertTrue("Inverse listener with @Supports was not called!", listener.inverseSupportsSkinListenerCalled);
    }

    @Test
    public void testDataHasListener() throws Exception {
        DataHasListener listener = new DataHasListener();
        AnnotatedEventListener hasSkinListener = this.getListener(listener, "hasSkinListener", SubEvent.class, Player.class);
        AnnotatedEventListener inversehasSkinListener = this.getListener(listener, "inverseHasSkinListener", SubEvent.class, Player.class);

        Player player1 = mock(Player.class);
        when(player1.get(SkinData.class)).thenReturn(Optional.empty());

        Player player2 = mock(Player.class);
        when(player2.get(SkinData.class)).thenReturn(Optional.of(mock(SkinData.class)));

        Cause cause1 = Cause.source(player1).build();
        Cause cause2 = Cause.source(player2).build();

        SubEvent event1 = new SubEvent(cause1);
        SubEvent event2 = new SubEvent(cause2);

        hasSkinListener.handle(event1);
        Assert.assertFalse("Listener with @Has was called!", listener.hasSkinListenerCalled);

        hasSkinListener.handle(event2);
        Assert.assertTrue("Listener with @Has was not called!", listener.hasSkinListenerCalled);

        inversehasSkinListener.handle(event2);
        Assert.assertFalse("Inverse listener with @Has was called!", listener.inverseHasSkinListenerCalled);

        inversehasSkinListener.handle(event1);
        Assert.assertTrue("Inverse listener with @Has was not called!", listener.inverseHasSkinListenerCalled);
    }

    @Test
    public void testRootListener() throws Exception {
        RootListener listener = new RootListener();
        AnnotatedEventListener rootListener = this.getListener(listener, "rootListener", SubEvent.class, Player.class);
        AnnotatedEventListener rootListenerInc = this.getListener(listener, "rootListenerInclude", SubEvent.class, Object.class);
        AnnotatedEventListener rootListenerEx = this.getListener(listener, "rootListenerExclude", SubEvent.class, Object.class);

        Cause cause1 = Cause.of(NamedCause.of("String", "Hi"), NamedCause.of("Player", mock(Player.class)));
        Cause cause2 = Cause.source(mock(Player.class)).named("derp", 5).build();

        SubEvent event1 = new SubEvent(cause1);
        SubEvent event2 = new SubEvent(cause2);

        rootListener.handle(event1);
        Assert.assertFalse("Listener with @Root was called with improper parameter!", listener.rootListenerCalled);

        rootListener.handle(event2);
        Assert.assertTrue("Listener with @Root was not called when proper Cause was provided!", listener.rootListenerCalled);

        rootListenerInc.handle(event1);
        Assert.assertFalse("Listener with @Root with include was called with improper parameter!", listener.rootListenerCalledInc);

        rootListenerInc.handle(event2);
        Assert.assertTrue("Listener with @Root with include was not called when proper Cause was provided!", listener.rootListenerCalledInc);

        rootListenerEx.handle(event2);
        Assert.assertFalse("Listener with @Root with exclude was called with improper parameter!", listener.rootListenerCalledEx);

        rootListenerEx.handle(event1);
        Assert.assertTrue("Listener with @Root with exclude was not called when proper Cause was provided!", listener.rootListenerCalledEx);
    }

    @Test
    public void testBeforeCauseListener() throws Exception {
        BeforeAfterCauseListener listener = new BeforeAfterCauseListener();
        AnnotatedEventListener beforeCauseListener = this.getListener(listener, "beforeCauseListener", SubEvent.class, Player.class);
        AnnotatedEventListener afterCauseListener = this.getListener(listener, "afterCauseListener", SubEvent.class, Entity.class);
        AnnotatedEventListener beforeCauseListenerInc = this.getListener(listener, "beforeCauseListenerInclude", SubEvent.class, Player.class);
        AnnotatedEventListener beforeCauseListenerEx = this.getListener(listener, "beforeCauseListenerExclude", SubEvent.class, Player.class);
        AnnotatedEventListener afterCauseListenerInc = this.getListener(listener, "afterCauseListenerInclude", SubEvent.class, Entity.class);
        AnnotatedEventListener afterCauseListenerEx = this.getListener(listener, "afterCauseListenerExclude", SubEvent.class, Entity.class);

        Cause cause1 = Cause.source("Foo").named("Player", mock(Player.class)).named("Extent", mock(Extent.class)).build();
        Cause cause2 = Cause.source("Me").named("State", mock(BlockState.class)).named("Entity", mock(Entity.class)).build();

        SubEvent event1 = new SubEvent(cause1);
        SubEvent event2 = new SubEvent(cause2);

        beforeCauseListener.handle(event2);
        Assert.assertFalse("Listener with @Before was called with improper parameter!", listener.beforeCauseCalled);

        beforeCauseListener.handle(event1);
        Assert.assertTrue("Listener with @Before was not called when proper Cause was provided!", listener.beforeCauseCalled);

        afterCauseListener.handle(event1);
        Assert.assertFalse("Listener with @After was called with improper parameter!", listener.afterCauseCalled);

        afterCauseListener.handle(event2);
        Assert.assertTrue("Listener with @After was not called when proper Cause was provided!", listener.afterCauseCalled);

        beforeCauseListenerInc.handle(event1);
        Assert.assertTrue("Listener with @Before with inclusions was not called when proper Cause was provided!", listener.beforeCauseCalledInc);

        beforeCauseListenerEx.handle(event1);
        Assert.assertFalse("Listener with @Before with exclusions was called when an improper Cause was provided!", listener.beforeCauseCalledEx);

        afterCauseListenerInc.handle(event2);
        Assert.assertFalse("Listener with @After with inclusions was called when an improper Cause was provided!", listener.afterCauseCalledInc);

        afterCauseListenerEx.handle(event2);
        Assert.assertTrue("Listener with @After with exclusions was not called when proper Cause was provided!", listener.afterCauseCalledEx);
    }

    @Test
    public void testNamedCauseListener() throws Exception {
        NamedCauseListener listener = new NamedCauseListener();
        AnnotatedEventListener namedCauseListener = this.getListener(listener, "namedCauseListener", SubEvent.class, BlockState.class);
        AnnotatedEventListener namedCauseListenerInc = this.getListener(listener, "namedCauseListenerInclude", SubEvent.class, Object.class);
        AnnotatedEventListener namedCauseListenerEx = this.getListener(listener, "namedCauseListenerExclude", SubEvent.class, Object.class);

        Cause cause1 = Cause.of(NamedCause.of(NamedCause.OWNER, Text.of()));
        Cause cause2 = Cause.of(NamedCause.of(NamedCause.OWNER, mock(BlockState.class)));

        SubEvent event1 = new SubEvent(cause1);
        SubEvent event2 = new SubEvent(cause2);

        namedCauseListener.handle(event1);
        Assert.assertFalse("Listener with @Named was called with improper parameter!", listener.namedCauseListenerCalled);

        namedCauseListener.handle(event2);
        Assert.assertTrue("Listener with @Named was not called when proper Cause was provided!", listener.namedCauseListenerCalled);

        namedCauseListenerInc.handle(event2);
        Assert.assertTrue("Listener with @First with inclusions was not called when proper Cause was provided!", listener.namedCauseListenerCalledInc);

        namedCauseListenerEx.handle(event2);
        Assert.assertFalse("Listener with @First with exclusions was called when an improper Cause was provided!", listener.namedCauseListenerCalledEx);
    }

    public static class TestEvent implements Event, Cancellable {

        private final Cause cause;
        private boolean cancelled = false;

        public TestEvent(Cause cause) {
            this.cause = cause;
        }

        @Override
        public Cause getCause() {
            return this.cause;
        }

        @Override
        public boolean isCancelled() {
            return this.cancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            this.cancelled = cancel;
        }

    }

    public static class SubEvent extends TestEvent {

        public SubEvent(Cause cause) {
            super(cause);
        }
    }

    public static class UncancellableEvent implements Event {

        @Override
        public Cause getCause() {
            return Cause.source(this).build();
        }
    }

    private AnnotatedEventListener getListener(Object listener, String method) throws Exception {
        return this.getListener(listener, method, SubEvent.class);
    }

    private AnnotatedEventListener getListener(Object listener, String method, Class<?>... classes) throws Exception {
        return this.handlerFactory.create(listener, listener.getClass().getMethod(method, classes));
    }
}

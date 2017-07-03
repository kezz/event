/*
 * This file is part of event, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.event;

import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class FilteredEventBusTest {

  private final AtomicInteger result = new AtomicInteger();
  private final EventBus<Object, Object> bus = new SimpleEventBus<>(new ASMEventExecutorFactory<>(), (listener, method) -> method.isAnnotationPresent(SomeFilter.class));

  @Test
  public void testListener() {
    this.bus.register(new TestListener());
    this.bus.post(new TestEvent());
    assertEquals(1, this.result.get());
  }

  @Retention(RetentionPolicy.RUNTIME)
  public @interface SomeFilter {}
  public final class TestEvent {}

  public class TestListener {

    @Subscribe
    public void withoutFilter(final TestEvent event) {
      FilteredEventBusTest.this.result.getAndIncrement();
    }

    @SomeFilter
    @Subscribe
    public void withFilter(final TestEvent event) {
      FilteredEventBusTest.this.result.getAndIncrement();
    }
  }
}

// Copyright 2009 Howard M. Lewis Ship
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.howardlewisship.tapx.templating.internal.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.annotations.IntermediateType;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.ConcurrentBarrier;
import org.apache.tapestry5.ioc.util.TimeInterval;
import org.apache.tapestry5.services.UpdateListenerHub;

import java.util.concurrent.TimeUnit;

/**
 * Adapated from {@link org.apache.tapestry5.internal.services.CheckForUpdatesFilter}, this is specially designed as
 * advice for the {@link com.howardlewisship.tapx.templating.services.TemplateRendererSource} service. This filter doesn't
 * work precisely the same as for a Tapestry servlet application, as much of the eventual processing will occur outside
 * of the concurrent barriers put in place within this method advice, but I suspect the result will be sufficient and
 * the alternative is more awkard (making {@link com.howardlewisship.tapx.templating.TemplateAPI#performTemplateRendererOperation(String,
 * String, String, com.howardlewisship.tapx.templating.TemplateRendererCallback)} the <em>only</em> way to render a
 * template).
 */
public class CheckForUpdatesAdvice implements MethodAdvice
{
    private final long checkInterval;

    private final long updateTimeout;

    private final UpdateListenerHub updateListenerHub;

    private final ConcurrentBarrier barrier = new ConcurrentBarrier();

    private final Runnable checker = new Runnable()
    {
        public void run()
        {
            // On a race condition, multiple threads may hit this method briefly. If we've
            // already done a check, don't run it again.

            if (System.currentTimeMillis() - lastCheck >= checkInterval)
            {

                // Fire the update event which will force a number of checks and then
                // corresponding invalidation events.

                updateListenerHub.fireCheckForUpdates();

                lastCheck = System.currentTimeMillis();
            }
        }
    };

    private long lastCheck = 0;

    /**
     * @param updateListenerHub invoked, at intervals, to spur the process of detecting changes
     * @param checkInterval     interval, in milliseconds, between checks
     * @param updateTimeout     time, in  milliseconds, to wait to obtain update lock.
     */
    public CheckForUpdatesAdvice(UpdateListenerHub updateListenerHub,

                                 @Symbol(SymbolConstants.FILE_CHECK_INTERVAL)
                                 @IntermediateType(TimeInterval.class)
                                 long checkInterval,

                                 @Symbol(SymbolConstants.FILE_CHECK_UPDATE_TIMEOUT)
                                 @IntermediateType(TimeInterval.class)
                                 long updateTimeout)
    {
        this.updateListenerHub = updateListenerHub;
        this.checkInterval = checkInterval;
        this.updateTimeout = updateTimeout;
    }


    public void advise(final Invocation invocation)
    {
        Runnable checkAndProceed = new Runnable()
        {
            public void run()
            {
                if (System.currentTimeMillis() - lastCheck >= checkInterval)
                    barrier.tryWithWrite(checker, updateTimeout, TimeUnit.MILLISECONDS);

                // And, now, back to code within the read lock.


                invocation.proceed();
            }
        };

        // Obtain a read lock while handling the request. This will not impair parallel operations, except when a file check
        // is needed (the exclusive write lock will block threads attempting to get a read lock).

        barrier.withRead(checkAndProceed);
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.worker;

import java.util.ArrayList;
import java.util.List;
import org.apache.ignite.internal.util.worker.GridWorker;
import org.apache.ignite.mxbean.WorkersControlMXBean;

/**
 * MBean that provides control of system workersRegistry.
 */
public class WorkersControlMXBeanImpl implements WorkersControlMXBean {
    /** System worker registry. */
    private final WorkersRegistry workerRegistry;

    /**
     * Constructor.
     *
     * @param registry System worker registry.
     */
    public WorkersControlMXBeanImpl(WorkersRegistry registry) {
        workerRegistry = registry;
    }

    /** {@inheritDoc} */
    @Override public List<String> getWorkerNames() {
        return new ArrayList<>(workerRegistry.names());
    }

    /** {@inheritDoc} */
    @Override public boolean terminateWorker(String name) {
        GridWorker w = workerRegistry.worker(name);

        if (w == null || w.isCancelled())
            return false;

        Thread t = w.runner();

        if (t == null)
            return false;

        t.interrupt();

        return true;
    }
}

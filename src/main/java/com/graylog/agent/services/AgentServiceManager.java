/**
 * This file is part of Graylog.
 *
 * Graylog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.graylog.agent.services;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.graylog.agent.config.ConfigurationRegistry;

import javax.inject.Inject;

public class AgentServiceManager {
    private final ServiceManager serviceManager;
    private final ConfigurationRegistry configuration;

    @Inject
    public AgentServiceManager(ServiceManager serviceManager, ConfigurationRegistry configuration) {
        this.serviceManager = serviceManager;
        this.configuration = configuration;
    }

    public ConfigurationRegistry getConfiguration() {
        return configuration;
    }

    public void start() {
        serviceManager.startAsync().awaitHealthy();
    }

    public void stop() {
        serviceManager.stopAsync().awaitStopped();
    }

    public void awaitStopped() {
        serviceManager.awaitStopped();
    }

    public ImmutableMultimap<Service.State, Service> servicesByState() {
        return serviceManager.servicesByState();
    }
}

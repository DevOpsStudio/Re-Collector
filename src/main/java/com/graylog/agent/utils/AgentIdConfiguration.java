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
package com.graylog.agent.utils;

import com.typesafe.config.Config;

import javax.inject.Inject;

public class AgentIdConfiguration {
    private static final String agentIdStatement = "agent-id";
    private final String agentId;

    @Inject
    public AgentIdConfiguration(Config config) {
        if (config.hasPath(agentIdStatement)) {
            this.agentId = config.getString(agentIdStatement);
        } else {
            this.agentId = "file:config/agent-id";
        }
    }

    public String getAgentId() {
        return agentId;
    }
}


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
package com.graylog.agent.cli.commands;

import com.graylog.agent.AgentVersion;
import io.airlift.airline.Command;

@Command(name = "version", description = "Show version information on STDOUT")
public class Version implements AgentCommand {
    @Override
    public void run() {
        final AgentVersion v = AgentVersion.CURRENT;
        final String message = String.format("Graylog Agent v%s (commit=%s, timestamp=%s)",
                v.version(), v.commitIdShort(), v.timestamp());

        System.out.println(message);
    }

    @Override
    public void stop() {
        // nothing to stop
    }
}

/**
 * This file is part of Graylog.
 * <p>
 * Graylog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Graylog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Graylog.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graylog.collector.outputs;

import org.graylog.collector.guice.CollectorModule;
import org.graylog.collector.outputs.gelf.GelfOutput;
import org.graylog.collector.outputs.gelf.GelfOutputConfiguration;
import org.graylog.collector.outputs.kafka.KafkaOutput;
import org.graylog.collector.outputs.kafka.KafkaOutputConfiguration;
import org.graylog.collector.outputs.stdout.StdoutOutput;
import org.graylog.collector.outputs.stdout.StdoutOutputConfiguration;

public class OutputsModule extends CollectorModule {
    @Override
    protected void configure() {
        registerOutput("gelf",
                GelfOutput.class,
                GelfOutput.Factory.class,
                GelfOutputConfiguration.class,
                GelfOutputConfiguration.Factory.class);

        registerOutput("stdout",
                StdoutOutput.class,
                StdoutOutput.Factory.class,
                StdoutOutputConfiguration.class,
                StdoutOutputConfiguration.Factory.class);

        registerOutput("kafka",
                KafkaOutput.class,
                KafkaOutput.Factory.class,
                KafkaOutputConfiguration.class,
                KafkaOutputConfiguration.Factory.class);

        registerBufferConsumer(OutputRouter.class);
    }
}

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
package com.graylog.agent.file;

import com.google.common.collect.Queues;
import com.graylog.agent.Message;
import com.graylog.agent.buffer.Buffer;

import java.util.concurrent.BlockingQueue;

class CollectingBuffer implements Buffer {

    private final BlockingQueue<Message> messages = Queues.newArrayBlockingQueue(16);
    private boolean outOfCapacity = false;
    private boolean processingDisabled = false;

    @Override
    public void insert(Message message) {
        messages.add(message);
    }

    @Override
    public Message remove() {
        return null;
    }

    public boolean isOutOfCapacity() {
        return outOfCapacity;
    }

    public void setOutOfCapacity(boolean outOfCapacity) {
        this.outOfCapacity = outOfCapacity;
    }

    public boolean isProcessingDisabled() {
        return processingDisabled;
    }

    public void setProcessingDisabled(boolean processingDisabled) {
        this.processingDisabled = processingDisabled;
    }

    public BlockingQueue<Message> getMessageQueue() {
        return messages;
    }

}

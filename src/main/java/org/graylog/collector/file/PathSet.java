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
package org.graylog.collector.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public interface PathSet {
    /**
     * Get the root path of the path set.
     * @return the root path
     */
    Path getRootPath();

    /**
     *  Checks if the given path is in the path set.
     *
     * @param path
     * @return true if given path is in the path set, false otherwise
     */
    boolean isInSet(Path path);

    /**
     * Returns all files of the path set that exist in the file system.
     *
     * @return existing path set files in the file system
     * @throws IOException
     */
    Set<Path> getPaths() throws IOException;
}

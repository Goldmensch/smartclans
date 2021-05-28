/*
 *
 *  * Copyright (C) 2021 Nick Hensel
 *  * This file is part of Smartclans.
 *  *
 *  * Smartclans is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * Smartclans is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see https://www.gnu.org/licenses/.
 *
 */

package de.goldmensch.common.file.fileversion;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileVersionSorter {
    private final String key;
    private final Path dir;
    private final Map<Integer, Path> orderedFiles = new TreeMap<>();
    private List<Path> paths;

    public FileVersionSorter(String key, Path path) {
        this.key = key;
        this.dir = path;
        paths = null;
    }

    public FileVersionSorter(String key, List<Path> files) {
        this.key = key;
        this.paths = files;
        dir = null;
    }

    public Map<Integer, Path> sort() {
        if (dir != null) {
            paths = Arrays.stream(
                    Objects.requireNonNull(dir.toFile().listFiles()))
                    .map(File::toPath)
                    .collect(Collectors.toList());
        }
        sortPaths();
        return orderedFiles;
    }

    private void sortPaths() {
        paths.forEach(path -> {
            AtomicReference<String> version = new AtomicReference<>("");
            path.getFileName().toString().
                    split(key)[1].chars().
                    mapToObj(c -> (char) c).
                    filter(character -> Pattern.compile("[0-9]")
                            .matcher(String.valueOf(character))
                            .matches()).forEach(
                    character -> version.set(version + String.valueOf(character)));

            orderedFiles.put(Integer.valueOf(version.get()), path);
        });
    }
}

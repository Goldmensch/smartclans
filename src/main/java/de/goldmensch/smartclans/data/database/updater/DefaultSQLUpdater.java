/*
 * Copyright (C) 2021 Nick Hensel
 * This file is part of Smartclans.
 *
 * Smartclans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Smartclans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package de.goldmensch.smartclans.data.database.updater;

import de.goldmensch.common.file.ResourceFilesUtils;
import de.goldmensch.common.file.fileversion.FileVersionSorter;
import de.goldmensch.common.file.fileversion.FileVersionSorterUtils;
import de.goldmensch.common.sql.querybuilder.QueryBuilder;
import de.goldmensch.smartclans.config.DatabaseDriver;
import de.goldmensch.smartclans.util.Logger;

import javax.sql.DataSource;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultSQLUpdater implements Updater {

    DataSource source;

    public DefaultSQLUpdater(DataSource source) {
        this.source = source;
    }

    @Override
    public int getVersion() {
        Logger.debug("fetch version from database");

        int version = 0;
        try (Connection conn = source.getConnection(); PreparedStatement statement =
                conn.prepareStatement("SELECT version FROM smartclans_db_version")) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            version = resultSet.getInt("version");
        } catch (SQLException throwables) {
            Logger.debug("database does not exist");
        }
        Logger.debug("database does exists");
        return version;
    }

    @Override
    public void update(DatabaseDriver driver) {
        Logger.debug("start update");
        Map<Integer, Path> patches = getPatchMap(driver);
        int version = getVersion();
        int latest = FileVersionSorterUtils.getLastVersion(patches);

        Logger.debug("database version: " + version);
        Logger.debug("patchfile latest: " + latest);

        if (!(version < latest)) {
            Logger.info("Database upto date");
            return;
        }

        FileVersionSorterUtils.getPatchesBetween(patches, version + 1, latest)
                .forEach((integer, path) -> applyPatch(path.toString()));
    }

    private Map<Integer, Path> getPatchMap(DatabaseDriver driver) {
        ResourceFilesUtils filesUtils = new ResourceFilesUtils();
        String folder = "database" + File.separator;

        switch (driver) {
            case MYSQL:
            case MARIA_DB:
                folder += "mysql";
                break;
        }

        Logger.debug("get patchmap");
        try {
            return new FileVersionSorter("v_", filesUtils.getPathsFromResourceJAR(folder)).sort();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void applyPatch(String path) {
        path = path.substring(1);
        Logger.debug("start apply patch: " + path);
        String patch = "";
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {
            patch = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            Logger.debug("cannot apply patch" + patch);
            e.printStackTrace();
            return;
        }

        Arrays.stream(patch.split(";")).filter(s -> !s.isEmpty()).forEach(query -> QueryBuilder.builder(source, null)
                .queryWithoutParams(query)
                .update()
                .execute());
        Logger.debug("patch applied: " + path);
    }
}

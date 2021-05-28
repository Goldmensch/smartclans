// proudly stolen from: https://github.com/eldoriarpg/eldo-util/tree/feature/threading

package de.goldmensch.common.sql.querybuilder;

public interface UpdateStage {

    /**
     * Execute the update synced.
     *
     * @return number of changed rows
     */
    int executeUpdate();
}

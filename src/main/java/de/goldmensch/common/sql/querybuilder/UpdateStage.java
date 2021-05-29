package de.goldmensch.common.sql.querybuilder;


/**
 * Represents a UpdateStage of a {@link QueryBuilder}.
 * <p>
 * A UpdateStage is used to execute an update and get the changed rows.
 */
public interface UpdateStage {

    /**
     * Execute the update synced.
     *
     * @return Number of changed rows
     */
    int execute();
}

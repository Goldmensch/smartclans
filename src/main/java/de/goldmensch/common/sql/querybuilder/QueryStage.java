// proudly stolen from: https://github.com/eldoriarpg/eldo-util/tree/feature/threading
package de.goldmensch.common.sql.querybuilder;

public interface QueryStage<T> {
    /**
     * Set the query to execute
     *
     * @param query query
     * @return statement stage with query
     */
    StatementStage<T> setQuery(String query);
}

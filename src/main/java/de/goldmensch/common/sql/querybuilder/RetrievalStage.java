// proudly stolen from: https://github.com/eldoriarpg/eldo-util/tree/feature/threading

package de.goldmensch.common.sql.querybuilder;


import java.util.List;
import java.util.Optional;

public interface RetrievalStage<T> {

    /**
     * Retrieve all results synced as a list
     *
     * @return results as list
     */
    List<T> retrieveResults();


    /**
     * Retrieve the first result from the results set synced
     *
     * @return result wrapped into an optional
     */
    Optional<T> retrieveResult();
}

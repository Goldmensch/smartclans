package de.goldmensch.common.sql.querybuilder;

import de.goldmensch.common.consumer.ThrowingConsumer;
import de.goldmensch.common.functions.ThrowingReturnFunction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class QueryBuilder<T> implements QueryStage<T>, StatementStage<T>, ResultStage<T>, RetrievalStage<T>, UpdateStage {
    private final DataSource dataSource;
    private final Queue<QueryTask> tasks = new ArrayDeque<>();
    private final SQLException executionException;

    private String currQuery;
    private ThrowingConsumer<PreparedStatement, SQLException> currStatementConsumer;
    private ThrowingReturnFunction<T, ResultSet, SQLException> currResultMapper;
    private boolean atomic;

    private QueryBuilder(DataSource dataSource, Class<T> clazz) {
        this.dataSource = dataSource;
        executionException = new QueryExecutionException("An error occured while executing a query.");
    }

    /**
     * Create a new query builder with a defined return type. Use it for selects.
     *
     * @param source datasource for connection
     * @param clazz  class of required return type. Doesnt matter if you want a list or single result.
     * @param <T>    type of return type
     * @return a new query builder in a {@link QueryStage}
     */
    public static <T> QueryStage<T> builder(DataSource source, Class<T> clazz) {
        return new QueryBuilder<>(source, clazz);
    }

    /**
     * Create a new Query builder without a defined return type. Use it for updates.
     *
     * @param source datasource for connection
     * @return a new query builder in a {@link QueryStage}
     */
    public static QueryStage<?> builder(DataSource source) {
        return new QueryBuilder<>(source, null);
    }

    // QUERY STAGE

    @Override
    public StatementStage<T> query(String query) {
        this.currQuery = query;
        return this;
    }

    @Override
    public ResultStage<T> queryWithoutParams(String query) {
        this.currQuery = query;
        return emptyParams();
    }

    // STATEMENT STAGE

    @Override
    public ResultStage<T> params(ThrowingConsumer<PreparedStatement, SQLException> stmt) {
        this.currStatementConsumer = stmt;
        return this;
    }

    // RESULT STAGE

    @Override
    public RetrievalStage<T> readRow(ThrowingReturnFunction<T, ResultSet, SQLException> mapper) {
        this.currResultMapper = mapper;
        queueTask();
        return this;
    }

    @Override
    public UpdateStage update() {
        currResultMapper = s -> null;
        queueTask();
        return this;
    }

    @Override
    public QueryStage<T> append() {
        currResultMapper = s -> null;
        queueTask();
        return this;
    }

    private void queueTask() {
        tasks.add(new QueryTask(currQuery, currStatementConsumer, currResultMapper));
    }

    // RETRIEVAL STAGE

    // LISTS

    @Override
    public RetrievalStage<T> notAtomic(boolean isAtomic) {
        this.atomic = isAtomic;
        return this;
    }

    @Override
    public List<T> all() {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(!atomic);
            List<T> results = executeAndGetLast(conn).retrieveResults(conn);
            if (atomic) {
                conn.commit();
            }
            return results;
        } catch (SQLException e) {
            executionException.initCause(e);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    // SINGLE

    @Override
    public Optional<T> first() {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(!atomic);
            Optional<T> t = executeAndGetLast(conn).retrieveResult(conn);
            if (atomic) {
                conn.commit();
            }
            return t;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // UPDATE STAGE

    @Override
    public int execute() {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(!atomic);
            int update = executeAndGetLast(conn).update(conn);
            if (atomic) {
                conn.commit();
            }
            return update;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /*
    Execute all queries, since we are only interested in the result of the last of our queries.
    Thats why we will execute all queries inside this method regardless of the method which calls this method
    We will use a single connection for this, since the user may be interested in the last inserted id or something.
    */
    private QueryTask executeAndGetLast(Connection conn) throws SQLException {
        while (tasks.size() > 1) {
            tasks.poll().execute(conn);
        }
        return tasks.poll();
    }

    private class QueryTask {
        private final String query;
        private final ThrowingConsumer<PreparedStatement, SQLException> statementConsumer;
        private final ThrowingReturnFunction<T, ResultSet, SQLException> resultMapper;


        public QueryTask(String currQuery, ThrowingConsumer<PreparedStatement, SQLException> statementConsumer,
                         ThrowingReturnFunction<T, ResultSet, SQLException> resultMapper) {
            query = currQuery;
            this.statementConsumer = statementConsumer;
            this.resultMapper = resultMapper;
        }

        public List<T> retrieveResults(Connection conn) throws SQLException {
            List<T> results = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                statementConsumer.accept(stmt);
                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    results.add(resultMapper.apply(resultSet));
                }
            }
            return results;
        }

        public Optional<T> retrieveResult(Connection conn) throws SQLException {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                statementConsumer.accept(stmt);
                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    return Optional.ofNullable(resultMapper.apply(resultSet));
                }
            }
            return Optional.empty();
        }

        public void execute(Connection conn) throws SQLException {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                statementConsumer.accept(stmt);
                stmt.execute();
            }
        }

        public int update(Connection conn) throws SQLException {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                statementConsumer.accept(stmt);
                return stmt.executeUpdate();
            }
        }
    }
}

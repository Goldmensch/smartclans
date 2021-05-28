//proudly stolen from: https://github.com/eldoriarpg/eldo-util/tree/feature/threading
package de.goldmensch.common.sql.querybuilder;

import de.goldmensch.common.consumer.ThrowingConsumer;
import de.goldmensch.common.functions.ThrowingReturnFunction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueryBuilder<T> implements QueryStage<T>, StatementStage<T>, ResultStage<T>, RetrievalStage<T>, UpdateStage {
    private final DataSource dataSource;
    ThrowingConsumer<PreparedStatement, SQLException> statementConsumer;
    ThrowingReturnFunction<T, ResultSet, SQLException> resultMapper;
    private String query;

    /**
     * Create a neq query builder
     *
     * @param dataSource data source to use
     * @param clazz      clazz of result
     */
    public QueryBuilder(DataSource dataSource, Class<T> clazz) {
        this.dataSource = dataSource;
    }

    public static <T> QueryStage<T> builder(DataSource source, Class<T> clazz) {
        return new QueryBuilder<>(source, clazz);
    }

    @Override
    public StatementStage<T> setQuery(String query) {
        this.query = query;
        return this;
    }

    @Override
    public ResultStage<T> setStatements(ThrowingConsumer<PreparedStatement, SQLException> stmt) {
        this.statementConsumer = stmt;
        return this;
    }

    @Override
    public RetrievalStage<T> extractResults(ThrowingReturnFunction<T, ResultSet, SQLException> mapper) {
        this.resultMapper = mapper;
        return this;
    }

    @Override
    public UpdateStage update() {
        return this;
    }

    @Override
    public List<T> retrieveResults() {
        List<T> results = new ArrayList<>();
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            statementConsumer.accept(stmt);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                results.add(resultMapper.apply(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public Optional<T> retrieveResult() {
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            statementConsumer.accept(stmt);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(resultMapper.apply(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int executeUpdate() {
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            statementConsumer.accept(stmt);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

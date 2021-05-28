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

package de.goldmensch.common.sql.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ResultSetStream {

    ResultSet resultSet;

    public ResultSetStream(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public Stream<Record> stream() {
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<Record>(
                Long.MAX_VALUE, Spliterator.ORDERED) {
            @Override
            public boolean tryAdvance(Consumer<? super Record> action) {
                try {
                    if (resultSet.next()) {
                        action.accept(new Record(resultSet));
                        return true;
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                return false;
            }
        }, false);
    }

    public <T> T getFirst(String column, Class<T> clazz) {
        Record record = getFirst();
        return record != null ? record.getObject(column, clazz) : null;
    }

    public Record getFirst() {
        try {
            resultSet.beforeFirst();
            return resultSet.next() ? new Record(resultSet) : null;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }
}

package net.ttddyy.dsproxy.example.service;

import io.netty.util.internal.ReflectionUtil;
import org.postgresql.jdbc.PgResultSet;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class RewindService {

    private static final String CURRENT_RESULT_ROW = "currentRow";
    private static final int REWIND_TO_BEGIN = -1;

    public void rewind(ResultSet rs) throws SQLException {
        Class clazz = PgResultSet.class;
        final PgResultSet unwrappedRS = rs.unwrap(PgResultSet.class);
        try {
            Field field = PgResultSet.class.getDeclaredField(CURRENT_RESULT_ROW);
            Optional.ofNullable(ReflectionUtil.trySetAccessible(field, false)).ifPresent(
                throwable -> {
                    throw new IllegalArgumentException("Failed to get the field value reflectively: " + clazz + "." + CURRENT_RESULT_ROW, throwable);
                }
            );
            if (unwrappedRS != null) {
                field.set(unwrappedRS, REWIND_TO_BEGIN);
            }
        } catch (Exception e) {
            System.out.println("!!!>>>  Failed to set value to " + clazz + "." + CURRENT_RESULT_ROW +" ; err= " + e.getMessage());
        }
    }
}

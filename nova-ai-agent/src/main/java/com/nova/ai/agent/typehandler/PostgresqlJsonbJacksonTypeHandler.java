package com.nova.ai.agent.typehandler;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 与 {@link JacksonTypeHandler} 相同，但以 PostgreSQL {@code jsonb} 写入，避免 JDBC 将 JSON 绑定为 varchar
 * 导致 “column ... is of type jsonb but expression is of type character varying”。
 */
@MappedTypes({Object.class})
@MappedJdbcTypes(JdbcType.OTHER)
public class PostgresqlJsonbJacksonTypeHandler extends JacksonTypeHandler {

    public PostgresqlJsonbJacksonTypeHandler(Class<?> type) {
        super(type);
    }

    public PostgresqlJsonbJacksonTypeHandler() {
        super(Object.class);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
            throws SQLException {
        PGobject pg = new PGobject();
        pg.setType("jsonb");
        pg.setValue(toJson(parameter));
        ps.setObject(i, pg);
    }
}

package com.core;

import java.sql.*;

public interface CallBack {
    Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException;
}

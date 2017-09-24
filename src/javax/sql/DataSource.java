package javax.sql;

import java.sql.*;

public interface DataSource extends CommonDataSource, Wrapper{
    Connection getConnection() throws SQLException;
}



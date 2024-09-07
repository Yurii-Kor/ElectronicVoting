package il.ac.sce.elctronicvoting.projectclasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getDBConnection () throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "SYSTEM", "19");
    }
}

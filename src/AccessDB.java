import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.sql.DriverManager.getConnection;


public class AccessDB {
    public void connectionAndQueryTest() {
        try {
            // 1. get a connection to database
            Connection myConn = getConnection("jdbc:mysql://localhost:3306/ap", "root", "periergos77AS");

            // 2. create a statement
            Statement myStmt = myConn.createStatement();

            // 3. execute SQL query
            ResultSet myRs = myStmt.executeQuery("select * from invoices");

            // 4. process the result set
            while (myRs.next()) {
                System.out.println(myRs.getString("invoice_id"));
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
}


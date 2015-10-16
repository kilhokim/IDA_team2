package dbConnection;

import operator.ProjectEvaluator;

import java.sql.*;
import util.Keys;

public class DBConn {
    public static Connection con = null;
    public static PreparedStatement pstmt = null;
    public static String url = Keys.DB_URL;
    public static String user = Keys.DB_USERNAME;
    public static String pwd = Keys.DB_PWD;

    public static void connect(boolean test) {
        try {
            if (con == null) {
                Class.forName("org.gjt.mm.mysql.Driver");
                if(test) con = DriverManager.getConnection(ProjectEvaluator.testUrl,
                        ProjectEvaluator.testUser, ProjectEvaluator.testPwd);
                if(!test) con = DriverManager.getConnection(url, user, pwd);
            } else if (con.isClosed()) {
                Class.forName("org.gjt.mm.mysql.Driver");
                if(test) con = DriverManager.getConnection(ProjectEvaluator.testUrl,
                        ProjectEvaluator.testUser, ProjectEvaluator.testPwd);
                if(!test) con = DriverManager.getConnection(url, user, pwd);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet execQuery(String sql, boolean test) {
        ResultSet rs = null;
        connect(test);
        try {
            Statement stmt = con.createStatement();
            // System.out.println("DBConn.execQuery(): sql=" + sql);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void close() {
        if (con != null) {
            try {
                if (!con.isClosed())
                    con.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        con = null;
    }
}

package cn.zhengsigen.java.aop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Main {

    public int age = 0;
    //查询年龄操作
    public synchronized int getAge(Connection connection) throws SQLException {
        ResultSet rs = connection.createStatement ().executeQuery ("select age from user where id = 100");
        rs.next ();
        int age = rs.getInt (1);
        return age;
    }
    //修改年龄操作
    public synchronized void run(Connection connection, int age, Main main) throws SQLException {
        if (age > 0) {
            connection.setAutoCommit (false);// 事务
            connection.createStatement ().execute ("update user set age =age -1 where id=100");
            int age1 = main.getAge (connection);
            if (age1 < 0) {
                System.out.println ("转账失败，余额不足");
                connection.rollback ();
            } else {
                connection.commit ();
                System.out.println("转账成功，当前余额:"+age1);            }
        } else {
            System.out.println ("余额不足");
        }
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Main main = new Main ();
        Connection connection = DriverManager.getConnection ("jdbc:mysql://127.0.0.1:3306/0412spring?user=root&password=123&useSSL=false");

        for(int i =0;i<201;i++){
            new Thread () {
                @Override
                public void run() {
                    try {
                        main.age = main.getAge (connection);
                        main.run (connection, main.getAge (connection), main);
                    } catch (SQLException e) {
                        e.printStackTrace ();
                    }
                }
            }.start ();
        }
    }
}

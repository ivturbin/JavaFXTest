package UseDatabase;

import java.io.PrintWriter;
import java.sql.*;

public class Database {
    PrintWriter pw = new PrintWriter(System.out, true);

    private String username;
    private String password;
    private Connection connection;

    public void connect(String username, String password) {
        this.username = username;
        this.password = password;
        connection = connection();
    }

    private Connection connection() {
        Connection m_connection = null;

        try {
            Class.forName(Config.Driver);
            pw.println("Драйвер подключен");
        } catch (ClassNotFoundException e) {
            pw.println("Драйвер не подключен");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            m_connection = DriverManager.getConnection(Config.URL, username, password);
            pw.println("Соединение установлено");
        } catch (SQLException e) {
            pw.println("Соединение не установлено");
            e.printStackTrace();
        } catch (Exception e) {
            pw.println("Пароль введен некорректно");
            e.printStackTrace();
        }

        return m_connection;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public Statement getStatement() {
        Statement m_statement = null;
        try {
            m_statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY) ; //Чтобы можно было неоднократно проходить ResultSet
            pw.println("Интерфейс доступа к БД создан");
        } catch (SQLException e){
            pw.println("Интерфейс доступа к БД не создан");
        }
        return m_statement;
    }
}

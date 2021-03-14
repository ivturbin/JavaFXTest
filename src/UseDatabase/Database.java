package UseDatabase;

import java.io.PrintWriter;

import java.sql.*;
import org.apache.commons.codec.digest.DigestUtils;

public class Database {
    PrintWriter pw = new PrintWriter(System.out, true);

    private String username;
    private String password;
    private Connection connection;
    private Statement statement = null;

    public void connect(String username, String password) {
        this.username = username;
        this.password = password;
        connection = connection();

        if (connection == null) {
            return;
        }

        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY) ; //Чтобы можно было неоднократно проходить ResultSet
            pw.println("Интерфейс доступа к БД создан");
        } catch (SQLException e){
            pw.println("Интерфейс доступа к БД не создан");
        }
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
            pw.println("Соединение с БД установлено");
        } catch (SQLException e) {
            pw.println("Соединение с БД не установлено");
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
        return statement;
    }

    public boolean hasUser(String username) {
        System.out.println("Проверка наличия пользователя");
        ResultSet resultSet = null;
        try {
            resultSet = getStatement().executeQuery("SELECT rolname FROM pg_roles where rolname = '" + username + "'");
        } catch (SQLException sqlException) {
            System.out.println("Ошибка выполнения запроса при проверке наличия пользователя");
            sqlException.printStackTrace();
        }

        try {
            assert resultSet != null;
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка получения данных при проверке наличия пользователя");
        }
        return false;
    }

    public boolean correctPassword(String username, String password) {
        System.out.println("Проверка корректности пароля");
        ResultSet resultSet = null;
        String myHash = DigestUtils.md5Hex(password+username); //Пароль в постгрес хранится в виде хэш суммы для логина и пароля. Проверить правильность введенного пароля вычисляется эта сумма для сравнения

        try {
            resultSet = getStatement().executeQuery("SELECT usename FROM pg_shadow where usename = '" + username + "' and passwd = 'md5" + myHash + "'");
        } catch (SQLException sqlException) {
            System.out.println("Ошибка выполнения запроса при проверке корректности пароля");
            sqlException.printStackTrace();
        }

        try {
            assert resultSet != null;
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}

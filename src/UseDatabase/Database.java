package UseDatabase;

import java.sql.*;
import java.util.ArrayList;

import javafx.util.Pair;
import org.apache.commons.codec.digest.DigestUtils;

public class Database {

    private static String username;
    private static String password;
    private static Connection connection;
    private static Statement statement = null;

    private static int groupId;

    public static boolean isNotConnected() {
        return connection == null;
    }

    public static void connect() {
        username = Config.SUPERUSER_NAME;
        password = Config.SUPERUSER_PASSWORD;

        try {
            Class.forName(Config.DRIVER);
            System.out.println("Драйвер подключен");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер не подключен");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            connection = DriverManager.getConnection(Config.URL, username, password);
            System.out.println("Соединение с БД установлено");
        } catch (SQLException e) {
            System.out.println("Соединение с БД не установлено");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Пароль введен некорректно");
            e.printStackTrace();
        }

        if (statement == null) {
            try {
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY) ; //Чтобы можно было неоднократно проходить ResultSet
                System.out.println("Интерфейс доступа к БД создан");
            } catch (SQLException e){
                System.out.println("Интерфейс доступа к БД не создан");
            }
        }
    }

    public static boolean hasUser(String username) {
        System.out.println("Проверка наличия пользователя");
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT usname FROM users where usname = '" + username + "'");
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

    public static boolean correctPassword(String username, String password) {
        System.out.println("Проверка корректности пароля");

        ResultSet resultSet = null;
        /*
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
        return false;*/

        String myHash = DigestUtils.md5Hex(password+username);
        System.out.println(myHash);

        try {
            resultSet = statement.executeQuery("SELECT usname FROM users where usname = '" + username + "' and passwd = '" + myHash + "'");
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

    public static ArrayList<Pair<Integer, String>> getGroups() {
        ResultSet resultSet = null;
        ArrayList<Pair<Integer, String>> result = new ArrayList<>();
        try {
            resultSet = statement.executeQuery("SELECT id_group, name FROM groups");
        } catch (SQLException sqlException) {
            System.out.println("Ошибка получения групп из БД");
            sqlException.printStackTrace();
        }
        try {
            while (true) {
                assert resultSet != null;
                if (!resultSet.next()) break;
                result.add(new Pair<>(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка получения данных групп из интерфейса");
        }
        return result;
    }

    public static  ArrayList<Student> getStudents() {
        ArrayList<Student> result = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("select id_student, name, coalesce(second_name, ''), last_name, " +
                    "coalesce(birth_date, date'1900-1-1'), coalesce(is_enrolled, false), id_group from students"); //coalesce заменяет null на значение по дефолту
        } catch (SQLException sqlException) {
            System.out.println("Ошибка получения студентов из БД");
            sqlException.printStackTrace();
        }
        try {
            while (true) {
                assert resultSet != null;
                if (!resultSet.next()) break;
                result.add(new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getDate(5).toLocalDate(), resultSet.getBoolean(6), resultSet.getInt(7)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка получения данных студентов из интерфейса");
        }
        return result;
    }

    public static boolean addStudent(Student student) {
        try {
            statement.execute("insert into students (last_name, name, second_name, birth_date, is_enrolled, id_group) " +
                    "values ('" + student.getLastName() + "', '" + student.getName() + "', '" + (student.getSecondName().isEmpty() ? null : student.getSecondName()) + "', '"
                    + student.getBirthDate() + "', " + student.isEnrolled() + ", " + student.getGroupId() + ")");
            System.out.println("Студент добавлен");
        } catch(SQLException sqlException) {
            System.out.println("Студент не добавлен");
            return false;
        }
        return true;
    }

    public static void setGroupId(int id) {
        groupId = id;
    }

    public static int getGroupId() {
        return groupId;
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("База не отключена");
        }
    }
}
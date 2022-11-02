package exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

class AppTest {

    private Connection connection;

    @BeforeEach
    public void init() throws SQLException, IOException {
        connection = DriverManager.getConnection("jdbc:h2:mem:");

        Statement statement = connection.createStatement();
        String init = getFileContent("init.sql");
        statement.execute(init);
    }

    private String getFileContent(String fileName) throws IOException {
        Path pathToSolution = Paths.get(fileName).toAbsolutePath();
        return Files.readString(pathToSolution).trim();
    }

    @Test
    void test() throws IOException, SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String sql = getFileContent("solution.sql");
        ResultSet rs = statement.executeQuery(sql);

        List<Map<String, String>> actual = new ArrayList<>();

        while (rs.next()) {
            actual.add(Map.of(
                "first_name", rs.getString("first_name"),
                "birthday", rs.getString("birthday")
                )
            );
        }

        List<Map<String, String>> expected = List.of(
            Map.of(
                "first_name", "Arya",
                "birthday", "2003-03-29 00:00:00"
            ),
            Map.of(
                "first_name", "Brienne",
                "birthday", "2001-04-04 00:00:00"
            ),
            Map.of("first_name", "Robb",
                "birthday", "1999-11-23 00:00:00"
            )
        );

        assertThat(actual).isEqualTo(expected);
    }

    @AfterEach
    public void closeConnection() throws SQLException {
        connection.close();
    }
}

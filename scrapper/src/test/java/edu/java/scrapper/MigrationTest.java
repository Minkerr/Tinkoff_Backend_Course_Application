package edu.java.scrapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MigrationTest extends IntegrationTest {
    @Test
    public void integrationTest() throws SQLException {
        //arrange
        String exp = "link";
        Connection connection = POSTGRES.createConnection("");
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO links (url) VALUES ('link')");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM links WHERE url = 'link'");
        //act
        String act = null;
        while (resultSet.next()) {
            act = resultSet.getString("url");
        }
        //assert
        assertThat(act).isEqualTo(exp);
    }
}

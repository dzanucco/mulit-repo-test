import spock.lang.*
import javax.sql.DataSource
import org.sqlite.SQLiteDataSource
import java.sql.Connection
import java.sql.Statement

class SQLiteDataSourceTest extends Specification {

    DataSource dataSource
    Connection connection

    def setup() {
        // Configure SQLite DataSource
        SQLiteDataSource ds = new SQLiteDataSource()
        ds.url = "jdbc:sqlite::memory:" // In-memory database
        dataSource = ds

        // Establish connection
        connection = dataSource.getConnection()
    }

    def cleanup() {
        // Close the connection if it exists
        if (connection != null) {
            connection.close()
        }
    }

    def "test SQLite database operations using DataSource and Statement"() {
        Statement stmt = null

        given: "A connection to SQLite and a new table"
        try {
            stmt = connection.createStatement()
            stmt.execute("CREATE TABLE test_table (id INTEGER PRIMARY KEY, name TEXT)")
        } finally {
            if (stmt != null) stmt.close()
        }

        when: "Inserting a value into the table"
        try {
            stmt = connection.createStatement()
            stmt.execute("INSERT INTO test_table (id, name) VALUES (1, 'Test Name')")
        } finally {
            if (stmt != null) stmt.close()
        }

        then: "The value should be present in the table"
        def resultName
        try {
            stmt = connection.createStatement()
            def resultSet = stmt.executeQuery("SELECT name FROM test_table WHERE id = 1")
            if (resultSet.next()) {
                resultName = resultSet.getString("name")
            }
        } finally {
            if (stmt != null) stmt.close()
        }
        resultName == 'Test Name'

        and: "The table contains one row"
        def rowCount
        try {
            stmt = connection.createStatement()
            def resultSet = stmt.executeQuery("SELECT COUNT(*) as rowCount FROM test_table")
            if (resultSet.next()) {
                rowCount = resultSet.getInt("rowCount")
            }
        } finally {
            if (stmt != null) stmt.close()
        }
        rowCount == 1
    }
}

package Modules

import javax.sql.DataSource
import org.sqlite.SQLiteDataSource

def GetDataSource(tempDbFile)
{
    // Configure SQLite DataSource
    SQLiteDataSource ds = new SQLiteDataSource()
    ds.url = "jdbc:sqlite:${tempDbFile.absolutePath}"
    dataSource = ds
}
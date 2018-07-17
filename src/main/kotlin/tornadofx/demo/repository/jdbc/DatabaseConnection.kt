package tornadofx.demo.repository.jdbc

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

data class DatabaseConnection(
        val databaseUrl: String = "jdbc:postgresql://localhost:5432/produto_sefaz",
        val user: String = "postgres",
        val password: String = "1234asd"){

    var connection: Connection? = null

    init {
        val props = Properties()
        props["user"] = user
        props["password"] = password

        try {
            connection = DriverManager.getConnection(databaseUrl, props)

        }catch (e: SQLException){
            e.printStackTrace()
        }
    }

    fun close(){
        try {
            if (connection != null) {
                connection!!.close()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }


}
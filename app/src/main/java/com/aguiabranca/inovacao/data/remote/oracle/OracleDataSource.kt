package com.aguiabranca.inovacao.data.remote.oracle

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager

object OracleDataSource {

    private const val JDBC_URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL"
    private const val DB_USER  = "RM563592"
    private const val DB_PASS  = "070589"

    init {
        try {
            Class.forName("oracle.jdbc.OracleDriver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    suspend fun getConnection(): Connection = withContext(Dispatchers.IO) {
        DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)
    }

    suspend fun <T> execute(block: suspend (Connection) -> T): Result<T> =
        withContext(Dispatchers.IO) {
            runCatching {
                DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS).use { conn ->
                    block(conn)
                }
            }
        }
}


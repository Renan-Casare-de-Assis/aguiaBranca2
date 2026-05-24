package com.aguiabranca.inovacao.data.remote.oracle

@Deprecated("Acesso Oracle direto no app foi descontinuado. Use API backend via Retrofit.")
object OracleDataSource {
    fun error(): Nothing = throw UnsupportedOperationException(
        "OracleDataSource não deve ser usado no app Android. Use API backend."
    )
}


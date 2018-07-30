package sefaz.ms.produtos.model

import java.time.LocalDateTime

data class ProdutoSefaz(
        val id: Int,
        val prodCprod: String,
        val prodNcm: String,
        val prodXprod: String,
        val emitXnome: String,
        val emitCnpjCpf: String,
        val dtInsercao: LocalDateTime,
        var codProdSefaz: Int
)
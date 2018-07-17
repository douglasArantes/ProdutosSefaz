package tornadofx.demo.model

import java.time.LocalDateTime

data class ProdutoSefaz(
        val id: Int,
        val prodCprod: String,
        val prodNcm: Int,
        val prodXprod: String,
        val emitXnome: String,
        val emitCnpjCpf: String,
        val dtInsercao: LocalDateTime,
        var codProdSefaz: Int
)
package tornadofx.demo.repository

import tornadofx.demo.model.ProdutoSefaz
import tornadofx.demo.repository.jdbc.DatabaseConnection

object ProdutosRepository {


    fun produtos() : List<ProdutoSefaz> {

        val produtosSefaz = ArrayList<ProdutoSefaz>()

        val connection = DatabaseConnection().connection
        val stmt = connection?.createStatement()

        val resultSet = stmt?.executeQuery("SELECT * FROM uplan_st_combustiveis_produtos_novos")

        while (resultSet?.next()!!) {
            val id = resultSet.getInt("id")
            val prodCprod = resultSet.getString("prod_cprod")
            val prodNcm = resultSet.getInt("prod_ncm")
            val codProdSefaz = resultSet.getInt("cod_prod_sefaz")
            val prodXprod = resultSet.getString("prod_xprod")
            val emitXnome = resultSet.getString("emit_xnome")
            val emitCnpjCpf = resultSet.getString("emit_cnpj_cpf")
            val dtInsercao = resultSet.getTimestamp("dt_insercao").toLocalDateTime()


            val prodSefaz = ProdutoSefaz(
                    id,
                    prodCprod,
                    prodNcm,
                    prodXprod,
                    emitXnome,
                    emitCnpjCpf,
                    dtInsercao,
                    codProdSefaz)

            produtosSefaz += prodSefaz
        }

        return produtosSefaz
    }

    fun salvarAlteraçoes(produtosSefaz: List<ProdutoSefaz>){

        var connection = DatabaseConnection().connection

        val updadeStatement = """
            UPDATE uplan_st_combustiveis_produtos_novos
            SET cod_prod_sefaz = ?
            WHERE id = ?"""

        for(produto in produtosSefaz) {

            if (produto.codProdSefaz != 0) {

                val prepStmt = connection?.prepareStatement(updadeStatement)

                prepStmt?.setInt(1, produto.codProdSefaz)
                prepStmt?.setInt(2, produto.id)

                prepStmt?.executeUpdate()
            }
        }
    }
}
package sefaz.ms.produtos.repository

import sefaz.ms.produtos.model.ProdutoSefaz
import sefaz.ms.produtos.repository.jdbc.DatabaseConnection

object ProdutosRepository {


    fun produtos() : List<ProdutoSefaz> {

        val produtosSefaz = ArrayList<ProdutoSefaz>()

        val connection = DatabaseConnection().connection
        val stmt = connection?.createStatement()

        val resultSet = stmt?.executeQuery("SELECT * FROM MINING_SEFAZ.SRJUNIOR.UPLAN_ST_COMBUSTIVEIS_PRODUTOS_NOVOS")

        while (resultSet?.next()!!) {
            val id = resultSet.getInt("ID")
            val prodCprod = resultSet.getString("PROD_CPROD")
            val prodNcm = resultSet.getString("PROD_NCM")
            val codProdSefaz = resultSet.getInt("COD_PROD_SEFAZ")
            val prodXprod = resultSet.getString("PROD_XPROD")
            val emitXnome = resultSet.getString("EMIT_XNOME")
            val emitCnpjCpf = resultSet.getString("EMIT_CNPJ_CPF")
            val dtInsercao = resultSet.getTimestamp("DT_INSERCAO").toLocalDateTime()


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
            UPDATE MINING_SEFAZ.SRJUNIOR.UPLAN_ST_COMBUSTIVEIS_PRODUTOS_NOVOS
            SET COD_PROD_SEFAZ = ?
            WHERE ID = ?"""

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
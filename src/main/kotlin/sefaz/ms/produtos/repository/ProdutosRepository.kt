package sefaz.ms.produtos.repository

import sefaz.ms.produtos.model.ProdutoSefaz
import sefaz.ms.produtos.repository.jdbc.DatabaseConnection

object ProdutosRepository {

    fun codigosProdutos(): List<Int> {

        val codigos = ArrayList<Int>()

        val connection = DatabaseConnection().connection

        connection.use {
            val stmt = connection?.createStatement()

            val resultSet = stmt?.executeQuery("""SELECT DISTINCT (COD_PROD_SEFAZ)
                FROM MINING_SEFAZ.SRJUNIOR.UPLAN_ST_COMBUSTIVEIS_PRODUTOS_NOVOS ORDER BY COD_PROD_SEFAZ""")

            while (resultSet?.next()!!) {
                val codigo = resultSet.getInt("COD_PROD_SEFAZ")

                codigos += codigo
            }
        }
        return codigos
    }

    fun produtos(): List<ProdutoSefaz> {

        val produtosSefaz = ArrayList<ProdutoSefaz>()

        val connection = DatabaseConnection().connection

        connection.use {

            val stmt = connection?.createStatement()

            val resultSet = stmt?.executeQuery("SELECT * FROM MINING_SEFAZ.SRJUNIOR.UPLAN_ST_COMBUSTIVEIS_PRODUTOS_NOVOS")
            //val resultSet = stmt?.executeQuery("SELECT * FROM uplan_st_combustiveis_produtos_novos")

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
        }

        return produtosSefaz
    }

    fun salvarAlteraçoes(produtosSefaz: List<Pair<Int, Int>>) {

        var connection = DatabaseConnection().connection

        val updadeStatement = """
             UPDATE MINING_SEFAZ.SRJUNIOR.UPLAN_ST_COMBUSTIVEIS_PRODUTOS_NOVOS
             SET COD_PROD_SEFAZ = ?
             WHERE ID = ?"""

       /* val updadeStatement = """
            UPDATE uplan_st_combustiveis_produtos_novos
            SET cod_prod_sefaz = ?
            WHERE id = ?"""*/

        connection.use {

            connection?.autoCommit = false

            val prepStmt = connection?.prepareStatement(updadeStatement)

            for (produto in produtosSefaz) {
                prepStmt?.setInt(1, produto.second)
                prepStmt?.setInt(2, produto.first)

                prepStmt?.addBatch()
            }

            if (prepStmt != null) {
                prepStmt.executeBatch()
                connection?.commit()
                prepStmt.clearBatch()
            }
        }
    }
}

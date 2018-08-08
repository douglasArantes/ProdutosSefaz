package sefaz.ms.produtos.view

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import tornadofx.*
import sefaz.ms.produtos.model.ProdutoSefaz
import sefaz.ms.produtos.repository.ProdutosRepository
import sefaz.ms.produtos.app.Styles.Companion.successButton

class ProdutosView : View("ProdutosApp") {

    private val items: ObservableList<ProdutoSefaz>
    private val editedRows = ArrayList<Pair<Int, Int>>()

    init {
        items = FXCollections.observableArrayList(ProdutosRepository.produtos())
    }

    override val root = vbox(10.0) {

        hbox(8.0) {
            button("Salvar Alterações") {
                action {

                    val editedRowsCopy = ArrayList<Pair<Int, Int>>(editedRows.size)
                    editedRowsCopy.addAll(editedRows)
                    editedRows.clear()

                    runAsync {
                        ProdutosRepository.salvarAlteraçoes(editedRowsCopy)
                    }
                }
                addClass(successButton)
            }
        }
        tableview(items, {
            column("ID", ProdutoSefaz::id)
            column("DT_INSERCAO", ProdutoSefaz::dtInsercao)
            column("PROD_XPROD", ProdutoSefaz::prodXprod)
            column("PROD_NCM", ProdutoSefaz::prodNcm)
            column("PROD_CPROD", ProdutoSefaz::prodCprod)
            column("EMIT_CNPJ_CPF", ProdutoSefaz::emitCnpjCpf)
            column("EMIT_XNOME", ProdutoSefaz::emitXnome)
            column("COD_PROD_SEFAZ", ProdutoSefaz::codProdSefaz)
                    .makeEditable().setOnEditCommit {
                        editedRows += Pair(it.rowValue.id, it.newValue)

                        it.rowValue.codProdSefaz = it.newValue
                    }

            setPrefSize(667.0, 376.0)
            columnResizePolicy = CONSTRAINED_RESIZE_POLICY
            vgrow = Priority.ALWAYS

            multiSelect(true)
            enableCellEditing()

            setOnKeyPressed { it ->
                if (it.code == KeyCode.SPACE) {
                    dialog {
                        vbox(6.0) {
                            label("Código de Produto")

                            var codigoProduto = textfield()


                            codigoProduto.setOnKeyPressed {
                                if (it.code == KeyCode.ENTER) {
                                    var numero = codigoProduto.text.toIntOrNull()

                                    if (numero != null) {
                                        selectionModel.selectedItems
                                                .forEach({
                                                    it.codProdSefaz = numero
                                                    editedRows += Pair(it.id, numero)
                                                })

                                        refresh()
                                        close()
                                    } else {
                                        codigoProduto.text = ""
                                    }
                                }

                            }

                            button("Preencher") {
                                action {

                                    var numero = codigoProduto.text.toIntOrNull()

                                    if (numero != null) {
                                        selectionModel.selectedItems
                                                .forEach({
                                                    it.codProdSefaz = numero
                                                    editedRows += Pair(it.id, numero)
                                                })

                                        refresh()
                                        close()
                                    } else {
                                        codigoProduto.text = ""
                                    }
                                }
                                addClass(successButton)
                            }
                        }
                    }
                }
            }
        })

        padding = Insets(10.0)
    }
}


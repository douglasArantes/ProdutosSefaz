package sefaz.ms.produtos.view

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import org.controlsfx.control.CheckComboBox
import org.controlsfx.control.Notifications
import tornadofx.*
import sefaz.ms.produtos.model.ProdutoSefaz
import sefaz.ms.produtos.repository.ProdutosRepository
import sefaz.ms.produtos.app.Styles.Companion.successButton

class ProdutosView : View("ProdutosApp") {

    private val produtos: ObservableList<ProdutoSefaz>
    private val codigosDePodutos: ObservableList<Int>

    private val editedRows = ArrayList<Pair<Int, Int>>()

    private var table: TableView<ProdutoSefaz> = TableView()

    private var data: SortedFilteredList<ProdutoSefaz>

    lateinit var idTextField: TextField
    lateinit var dtInsercaoTextField: TextField
    lateinit var prodXprodTextField: TextField
    lateinit var prodNcmTextField: TextField
    lateinit var prodCprodTextField: TextField
    lateinit var emitCnpjTextField: TextField
    lateinit var emitXnomeTextField: TextField
    lateinit var codigoProdutoSefazCheckComboBox: CheckComboBox<Int>

    private var codProdSefazChecked: ObservableList<Int>

    init {
        produtos = FXCollections.observableArrayList(ProdutosRepository.produtos())
        codigosDePodutos = FXCollections.observableArrayList(ProdutosRepository.codigosProdutos())
        codProdSefazChecked = FXCollections.observableArrayList()
        data = SortedFilteredList(produtos).bindTo(table)
    }

    fun filter() {
        data.predicate = {

            var filterId = true
            var filterDtInsercao = true
            var filterXprod = true
            var filterCprod = true
            var filterNcm = true
            var filterEmitCnpjCpf = true
            var filterEmitXnome = true
            var filterCodProdSefaz = true

            if (idTextField.text.isNotEmpty()) {
                filterId = it.id.toString().startsWith(idTextField.text, true)
            }

            if (dtInsercaoTextField.text.isNotEmpty()) {
                filterDtInsercao = it.dtInsercao.toString().startsWith(dtInsercaoTextField.text, true)
            }

            if (prodXprodTextField.text.isNotEmpty()) {
                filterXprod = it.prodXprod.startsWith(prodXprodTextField.text, true)
            }

            if (prodCprodTextField.text.isNotEmpty()) {
                filterCprod = it.prodCprod.startsWith(prodCprodTextField.text, true)
            }

            if (prodNcmTextField.text.isNotEmpty()) {
                filterNcm = it.prodNcm.startsWith(prodNcmTextField.text, true)
            }

            if (emitCnpjTextField.text.isNotEmpty()) {
                filterEmitCnpjCpf = it.emitCnpjCpf.startsWith(emitCnpjTextField.text, true)
            }

            if (emitXnomeTextField.text.isNotEmpty()) {
                filterEmitXnome = it.emitXnome.startsWith(emitXnomeTextField.text, true)
            }

            if (codProdSefazChecked.size > 0) {
                filterCodProdSefaz = it.codProdSefaz in codProdSefazChecked

                //println("${it.codProdSefaz} IN ${codProdSefazChecked} = ${it.codProdSefaz in codProdSefazChecked}")
            }

            (filterId and filterDtInsercao and filterXprod and filterCprod and filterNcm
                    and filterEmitCnpjCpf and filterEmitXnome and filterCodProdSefaz
                    )

        }
    }

    override val root = vbox(10.0) {

        hbox(8.0) {
            button("Salvar Alterações") {
                action {

                    val editedRowsCopy = ArrayList<Pair<Int, Int>>(editedRows.size)
                    editedRowsCopy.addAll(editedRows)

                    val quantidadeRegistrosAlterados = editedRowsCopy.size

                    if (quantidadeRegistrosAlterados > 0) {

                        val mensagem = if (quantidadeRegistrosAlterados == 1) {
                            "$quantidadeRegistrosAlterados registro está sendo salvo"
                        } else {
                            "$quantidadeRegistrosAlterados registros estão sendo salvos"
                        }

                        Notifications.create()
                                .text(mensagem)
                                .owner(this@vbox)
                                .position(Pos.TOP_RIGHT)
                                .showInformation()

                    } else {
                        Notifications.create()
                                .text("Você não alterou nenhum registro")
                                .owner(this@vbox)
                                .position(Pos.TOP_RIGHT)
                                .showWarning()
                    }

                    editedRows.clear()

                    runAsync {
                        ProdutosRepository.salvarAlteraçoes(editedRowsCopy)
                    }
                }
                addClass(successButton)
            }


        }


        table = tableview(data, {
            column("", ProdutoSefaz::id).graphic = vbox {
                label("ID")
                idTextField = textfield {
                    setOnKeyReleased {
                        filter()
                    }
                }
            }
            column("", ProdutoSefaz::dtInsercao).graphic = vbox {
                label("DT_INSERCAO")
                dtInsercaoTextField = textfield {
                    setOnKeyReleased {
                        filter()
                    }
                }
            }
            column("", ProdutoSefaz::prodXprod).graphic = vbox {
                label("PROD_XPROD")
                prodXprodTextField = textfield {
                    setOnKeyReleased {
                        filter()
                    }
                }
            }
            column("", ProdutoSefaz::prodNcm).graphic = vbox {
                label("PROD_NCM")
                prodNcmTextField = textfield {
                    setOnKeyReleased {
                        filter()
                    }
                }
            }
            column("", ProdutoSefaz::prodCprod).graphic = vbox {
                label("PROD_CPROD")
                prodCprodTextField = textfield {
                    setOnKeyReleased {
                        filter()
                    }
                }
            }
            column("", ProdutoSefaz::emitCnpjCpf).graphic = vbox {
                label("EMIT_CNPJ_CPF")
                emitCnpjTextField = textfield {
                    setOnKeyReleased {
                        filter()
                    }
                }
            }
            column("", ProdutoSefaz::emitXnome).graphic = vbox {
                label("EMIT_XNOME")
                emitXnomeTextField = textfield {
                    setOnKeyReleased {
                        filter()
                    }
                }
            }

            val colCodProdSefaz = column("", ProdutoSefaz::codProdSefaz)

            colCodProdSefaz.graphic = vbox {
                label("COD_PROD_SEFAZ")

                codigoProdutoSefazCheckComboBox = CheckComboBox<Int>(codigosDePodutos)

                codigoProdutoSefazCheckComboBox.maxWidth = colCodProdSefaz.maxWidth

                add(codigoProdutoSefazCheckComboBox)

                codigoProdutoSefazCheckComboBox.checkModel.checkedItems.onChange {
                    codProdSefazChecked = codigoProdutoSefazCheckComboBox.checkModel.checkedItems
                    filter()

                }
                padding = Insets(1.0, 1.0, 5.0, 1.0)
            }

            colCodProdSefaz.makeEditable().setOnEditCommit {
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

                            val codigoProduto = textfield()


                            codigoProduto.setOnKeyPressed {
                                if (it.code == KeyCode.ENTER) {
                                    val numero = codigoProduto.text.toIntOrNull()

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

                                    val numero = codigoProduto.text.toIntOrNull()

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
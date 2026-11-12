/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.dev.miguel.dukemarket.javafx.controller;

import br.dev.miguel.dukemarket.javafx.DAO.ProdutoDAO;
import br.dev.miguel.dukemarket.javafx.model.Produto;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author sesi2dib
 */
public class ScrProdutosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Vincula as colunas da TableView à classe Produto.
        bindColumns();

        // Busca Dados
        carregaDados();

    }
    
    /**
     * ScrProdutosController Class
     * 
     * @author Miguel
     */
    
    @FXML
    TableView <Produto> tblProduto;

    @FXML
    TableColumn <Produto, Integer> tcoId;

    @FXML
    TableColumn <Produto, String> tcoCodBarras;

    @FXML
    TableColumn <Produto, String> tcoDescricao;

    @FXML
    TableColumn <Produto, Double> tcoQtd;

    @FXML
    TableColumn <Produto, Double> tcoValorCompra;

    @FXML
    TableColumn <Produto, Double> tcoValorVenda;

    @FXML
    TableColumn <Produto, Calendar> tcoDataCadastro;

    @FXML
    TextField txtId;

    @FXML
    TextField txtCodBarras;

    @FXML
    TextField txtDescricao;

    @FXML
    TextField txtQtd;

    @FXML
    TextField txtValorCompra;

    @FXML
    TextField txtValorVenda;

    @FXML
    DatePicker dtpDataCadastro;

    ProdutoDAO pDAO;
    Produto pClicked;    // Armazena o produto clicado na tabela.
    
    boolean flagNovo =false;
    
    /**
     * Conecta as colunas da TableView com os atributos da classe Produto.
     * @return void
     */
    private void bindColumns() {

        // Vincula a coluna tcoId com o atributo id da classe Produto
        tcoId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Adiciona uma configuração extra de alinhamento à direita.
        tcoId.setStyle("-fx-alignment: CENTER_RIGHT;");

        tcoCodBarras.setCellValueFactory(new PropertyValueFactory<>("codBarras"));
        tcoCodBarras.setStyle("-fx-alignment: CENTER_RIGHT;");

        tcoDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        tcoQtd.setCellValueFactory(new PropertyValueFactory<>("qtd"));
        tcoQtd.setStyle("-fx-alignment: CENTER_RIGHT;");

        tcoValorCompra.setCellValueFactory(new PropertyValueFactory<>("valorCompra"));
        tcoValorCompra.setStyle("-fx-alignment: CENTER_RIGHT;");

        tcoValorVenda.setCellValueFactory(new PropertyValueFactory<>("valorVenda"));
        tcoValorVenda.setStyle("-fx-alignment: CENTER_RIGHT;");

        tcoDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
        tcoDataCadastro.setStyle("-fx-alignment: CENTER_RIGHT;");
    }
    
    /**
     * Carrega os dados de Produto que estão na base e vincula a TableView.
     */
    public void carregaDados(){
        
        this.pDAO = new ProdutoDAO();
        
        // Carrega view tblProduto com todos os resultados de produto.
        this.tblProduto.getItems().setAll(pDAO.getResults());
    }
    
    /**
     * Action do botão btnNovo.
     * Limpa os campos e posiciona o focus em txtId.
     * 
     * @return void
     */
    @FXML
    private void btnNovoClick() {
        txtId.setText("auto");
        txtId.setEditable(false);

        txtCodBarras.setText("");
        txtCodBarras.requestFocus();

        txtDescricao.setText("");
        txtQtd.setText("");
        txtValorCompra.setText("");
        txtValorVenda.setText("");
        dtpDataCadastro.setValue(null);
        
        this.flagNovo = true;
    }
    
    /**
     * Gerencia o click na tabela, identificando qual registro foi selecionado e
     * preenchendo os TextFields.
     */
    @FXML
    public void tblProdutoOnMouseClicked() {

        // getSelectedItem devolve um objeto Produto.
        // pClicked representa o Produto Clicado
        this.pClicked = tblProduto.getSelectionModel().getSelectedItem();

        if (pClicked != null) {

            txtId.setText(String.valueOf(pClicked.getId()));
            txtId.setEditable(false);
            txtCodBarras.setText(pClicked.getCodBarras());
            txtDescricao.setText(pClicked.getDescricao());
            txtQtd.setText(String.valueOf(pClicked.getQtd()));
            txtValorCompra.setText(String.valueOf(pClicked.getValorCompra()));
            txtValorVenda.setText(String.valueOf(pClicked.getValorVenda()));

            // Tratamento da data do cadastro:
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            formatter = formatter.withLocale(Locale.US);
            LocalDate date = LocalDate.parse(pClicked.getDataCadastro(), formatter);

            dtpDataCadastro.setValue(date);
            dtpDataCadastro.setEditable(false);

            this.flagNovo = false;
        }
    }
    
    /**
     * Gerencia o click do botão Salvar. Adiciona um novo registro quando
     * flagNovo == true ou atualiza quando flagNovo == false;
     * 
     */
    @FXML
    public void btnSalvarClick() {

        Produto p = new Produto();

        p.setCodBarras(txtCodBarras.getText());
        p.setDescricao(txtDescricao.getText());
        p.setQtd(Double.parseDouble(txtQtd.getText()));
        p.setValorCompra(Double.parseDouble(txtValorCompra.getText()));
        p.setValorVenda(Double.parseDouble(txtValorVenda.getText()));

        if (this.flagNovo) {
            // Novo produto...

            this.pDAO.create(p);

        } else {
            // Ops.. é uma atualização!
            p.setId(Integer.parseInt(txtId.getText()));
            this.pDAO.update(p);

        }

        this.carregaDados();
    }
    
    /**
     * Gerencia o click do botão Excluir. Remove o registro selecionado -
     * carregado em pClicked.
     * 
     */
    @FXML
    public void btnExcluirClick() {

        if (this.pClicked != null) {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirma Exclusão de Produto?");
            alert.setHeaderText(pClicked.getDescricao());
            alert.setContentText("Tem certeza que deseja excluir ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                this.pDAO.delete(this.pClicked.getId());

                this.tblProduto.getItems().remove(this.pClicked);

            }
            this.tblProduto.getSelectionModel().clearSelection();
            pClicked = null;

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Ops!");
            alert.setHeaderText("Atenção");
            alert.setContentText("Você deve selecionar um registro antes de excluí-lo!");

            alert.showAndWait();

        }
    }
}

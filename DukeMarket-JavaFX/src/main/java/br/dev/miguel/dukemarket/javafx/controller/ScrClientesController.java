/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.dev.miguel.dukemarket.javafx.controller;

import br.dev.miguel.dukemarket.javafx.DAO.ClienteDAO;
import br.dev.miguel.dukemarket.javafx.model.Cliente;
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
public class ScrClientesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Vincula as colunas da TableView à classe Cliente.
        bindColumns();

        // Busca Dados
        carregaDados();

    }

    /**
     * ScrClientesController Class
     *
     * @author Miguel
     */
    @FXML
    TableView<Cliente> tblCliente;

    @FXML
    TableColumn<Cliente, Integer> tcoId;

    @FXML
    TableColumn<Cliente, Long> tcoCpf;

    @FXML
    TableColumn<Cliente, String> tcoNome;

    @FXML
    TableColumn<Cliente, String> tcoEndereco;

    @FXML
    TableColumn<Cliente, String> tcoCidade;

    @FXML
    TableColumn<Cliente, String> tcoUf;

    @FXML
    TableColumn<Cliente, Calendar> tcoDataCadastro;

    @FXML
    TextField txtId;

    @FXML
    TextField txtCpf;

    @FXML
    TextField txtNome;

    @FXML
    TextField txtEndereco;

    @FXML
    TextField txtCidade;

    @FXML
    TextField txtUf;

    @FXML
    DatePicker dtpDataCadastro;

    ClienteDAO cDAO;
    Cliente cClicked;    // Armazena o cliente clicado na tabela.

    boolean flagNovo = false;

    /**
     * Conecta as colunas da TableView com os atributos da classe Cliente.
     *
     * @return void
     */
    private void bindColumns() {

        // Vincula a coluna tcoId com o atributo id da classe Cliente
        tcoId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Adiciona uma configuração extra de alinhamento à direita.
        tcoId.setStyle("-fx-alignment: CENTER_RIGHT;");

        tcoCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tcoCpf.setStyle("-fx-alignment: CENTER_RIGHT;");

        tcoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tcoEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        tcoCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));

        tcoUf.setCellValueFactory(new PropertyValueFactory<>("uf"));
        tcoUf.setStyle("-fx-alignment: CENTER;");

        tcoDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
        tcoDataCadastro.setStyle("-fx-alignment: CENTER_RIGHT;");
    }

    /**
     * Carrega os dados de Cliente que estão na base e vincula a TableView.
     */
    public void carregaDados() {

        this.cDAO = new ClienteDAO();

        // Carrega view tblCliente com todos os resultados de cliente.
        this.tblCliente.getItems().setAll(cDAO.getResults());
    }

    /**
     * Action do botão btnNovo. Limpa os campos e posiciona o focus em txtCpf.
     *
     * @return void
     */
    @FXML
    public void btnNovoClick() {
        txtId.setText("auto");
        txtId.setEditable(false);

        txtCpf.setText("");
        txtCpf.requestFocus();

        txtNome.setText("");
        txtEndereco.setText("");
        txtCidade.setText("");
        txtUf.setText("");
        dtpDataCadastro.setValue(null);

        this.flagNovo = true;
    }

    /**
     * Gerencia o click do botão Salvar. Adiciona um novo registro quando
     * flagNovo == true ou atualiza quando flagNovo == false;
     */
    @FXML
    public void btnSalvarClick() {

        Cliente c = new Cliente();

        c.setCpf(Long.parseLong(txtCpf.getText()));
        c.setNome(txtNome.getText());
        c.setEndereco(txtEndereco.getText());
        c.setCidade(txtCidade.getText());
        c.setUf(txtUf.getText());

        if (this.flagNovo) {
            // Novo cliente...
            this.cDAO.create(c);

        } else {
            // Ops.. é uma atualização!
            c.setId(Integer.parseInt(txtId.getText()));
            this.cDAO.update(c);

        }

        this.carregaDados();
    }

    /**
     * Gerencia o click do botão Excluir. Remove o registro selecionado -
     * carregado em cClicked.
     */
    @FXML
    public void btnExcluirClick() {

        if (this.cClicked != null) {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirma Exclusão de Cliente?");
            alert.setHeaderText(cClicked.getNome());
            alert.setContentText("Tem certeza que deseja excluir ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                this.cDAO.delete(this.cClicked.getId());

                this.tblCliente.getItems().remove(this.cClicked);

            }
            this.tblCliente.getSelectionModel().clearSelection();
            cClicked = null;

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Ops!");
            alert.setHeaderText("Atenção");
            alert.setContentText("Você deve selecionar um registro antes de excluí-lo!");

            alert.showAndWait();

        }
    }
    
    /**
     * Gerencia o click na tabela, identificando qual registro foi selecionado e
     * preenchendo os TextFields.
     */
    @FXML
    public void tblClientesOnMouseClicked() {

        // getSelectedItem devolve um objeto Cliente.
        // cClicked representa o Cliente Clicado
        this.cClicked = tblCliente.getSelectionModel().getSelectedItem();

        if (cClicked != null) {

            txtId.setText(String.valueOf(cClicked.getId()));
            txtId.setEditable(false);

            txtCpf.setText(String.valueOf(cClicked.getCpf()));
            txtNome.setText(cClicked.getNome());

            txtEndereco.setText(cClicked.getEndereco());
            txtCidade.setText(cClicked.getCidade());
            txtUf.setText(cClicked.getUf());

            // Tratamento da data do cadastro:
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            formatter = formatter.withLocale(Locale.US);
            LocalDate date = LocalDate.parse(cClicked.getDataCadastro(), formatter);

            dtpDataCadastro.setValue(date);
            dtpDataCadastro.setEditable(false);

            this.flagNovo = false;
        }
    }
}    
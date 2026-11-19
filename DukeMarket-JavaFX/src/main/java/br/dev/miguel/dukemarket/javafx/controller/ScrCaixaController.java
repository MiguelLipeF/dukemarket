/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dev.miguel.dukemarket.javafx.controller;

import br.dev.miguel.dukemarket.javafx.DAO.ClienteDAO;
import br.dev.miguel.dukemarket.javafx.DAO.ProdutoDAO;
import br.dev.miguel.dukemarket.javafx.model.Caixa;
import br.dev.miguel.dukemarket.javafx.model.Cliente;
import br.dev.miguel.dukemarket.javafx.model.Produto;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScrCaixaController implements Initializable {

    @FXML
    public TableView<Caixa> tblCarrinho;
    @FXML
    public TableColumn<Caixa, String> tcoProduto;
    @FXML
    public TableColumn<Caixa, Double> tcoQtd;
    @FXML
    public TableColumn<Caixa, Double> tcoUnitario;
    @FXML
    public TableColumn<Caixa, Double> tcoSubtotal;

    @FXML
    public Label lblClienteStatus;
    @FXML
    public Label lblTotalEsquerda;
    @FXML
    public Label lblTotalDireita;

    @FXML
    public Label lblDestaqueNome;
    @FXML
    public Label lblDestaqueInfo;

    @FXML
    public TextField txtCodBarras;
    @FXML
    public TextField txtProdutoAtual;
    @FXML
    public TextField txtPrecoAtual;

    public ProdutoDAO pDAO;
    public ClienteDAO cDAO;
    public List<Produto> listaProdutos;
    public ObservableList<Caixa> carrinho;
    public Cliente clienteIdentificado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pDAO = new ProdutoDAO();
        cDAO = new ClienteDAO();
        listaProdutos = pDAO.getResults();
        carrinho = FXCollections.observableArrayList();

        bindColumns();
        tblCarrinho.setItems(carrinho);
        focarLeitor();
    }

    public void focarLeitor() {
        Platform.runLater(() -> txtCodBarras.requestFocus());
    }

    public void bindColumns() {
        tcoProduto.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        tcoQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tcoQtd.setStyle("-fx-alignment: CENTER_RIGHT;");
        tcoUnitario.setCellValueFactory(new PropertyValueFactory<>("valorUnitario"));
        tcoUnitario.setStyle("-fx-alignment: CENTER_RIGHT;");
        tcoSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tcoSubtotal.setStyle("-fx-alignment: CENTER_RIGHT;");
    }

    @FXML
    public void txtCodBarrasAction() {
        String codigo = txtCodBarras.getText().trim();
        if (codigo.isEmpty()) {
            focarLeitor();
            return;
        }

        for (Produto p : listaProdutos) {
            if (p.getCodBarras() != null && p.getCodBarras().equals(codigo)) {
                adicionarProduto(p);
                txtCodBarras.clear();
                focarLeitor();
                return;
            }
        }

        exibeAlerta(AlertType.WARNING, "Produto não encontrado", "Código de barras não cadastrado: " + codigo);
        txtCodBarras.clear();
        focarLeitor();
    }

    public void adicionarProduto(Produto p) {
        for (Caixa item : carrinho) {
            if (item.getProduto().getId() == p.getId()) {
                if (item.getQuantidade() + 1 > p.getQtd()) {
                    exibeAlerta(AlertType.WARNING, "Estoque insuficiente",
                            "Quantidade máxima em estoque atingida para " + p.getDescricao());
                    return;
                }
                item.setQuantidade(item.getQuantidade() + 1);
                tblCarrinho.refresh();
                tblCarrinho.getSelectionModel().select(item);
                atualizarPainelDireito(p);
                atualizarTotais();
                return;
            }
        }

        if (p.getQtd() < 1) {
            exibeAlerta(AlertType.WARNING, "Sem estoque", "Produto indisponível no estoque.");
            return;
        }

        Caixa novo = new Caixa(p, 1);
        carrinho.add(novo);
        tblCarrinho.getSelectionModel().select(novo);
        atualizarPainelDireito(p);
        atualizarTotais();
    }

    public void atualizarPainelDireito(Produto p) {
        lblDestaqueNome.setText(p.getDescricao().toUpperCase());
        lblDestaqueInfo.setText(String.format("Cód: %s  |  Estoque restante: %.0f", p.getCodBarras(), p.getQtd()));
        txtProdutoAtual.setText(p.getDescricao());
        txtPrecoAtual.setText(String.format("R$ %.2f", p.getValorVenda()));
    }

    public void atualizarTotais() {
        double total = 0;
        for (Caixa item : carrinho) {
            total += item.getSubtotal();
        }
        String formatado = String.format("R$   %.2f", total);
        lblTotalEsquerda.setText(formatado);
        lblTotalDireita.setText(formatado);
    }

    @FXML
    public void btnCancelarClick() {
        if (carrinho.isEmpty()) {
            focarLeitor();
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Cancelar Venda");
        confirm.setHeaderText("Deseja cancelar toda a venda?");
        confirm.setContentText("Todos os itens bipados serão removidos.");

        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            limparTela();
        }
        focarLeitor();
    }

    @FXML
    public void btnCatalogoClick() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Produtos sem Código");

        TilePane tile = new TilePane();
        tile.setHgap(10);
        tile.setVgap(10);
        tile.setPrefColumns(3);
        tile.setPadding(new Insets(15));

        listaProdutos = pDAO.getResults();
        for (Produto p : listaProdutos) {
            Button btn = new Button(String.format("%s\nR$ %.2f", p.getDescricao(), p.getValorVenda()));
            btn.setPrefSize(175, 95);
            btn.setWrapText(true);
            btn.setTextAlignment(TextAlignment.CENTER);
            btn.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-background-color: white; -fx-border-color: #cccccc;");

            if (p.getQtd() <= 0) {
                btn.setDisable(true);
            } else {
                btn.setOnAction(e -> {
                    adicionarProduto(p);
                    dialog.close();
                    focarLeitor();
                });
            }
            tile.getChildren().add(btn);
        }

        ScrollPane scroll = new ScrollPane(tile);
        scroll.setFitToWidth(true);

        Scene scene = new Scene(scroll, 600, 450);
        dialog.setScene(scene);
        dialog.showAndWait();
        focarLeitor();
    }

    @FXML
    public void btnDigitarCodigoClick() {
        abrirTecladoTouch("Digitar Código de Barras", valor -> {
            txtCodBarras.setText(valor);
            txtCodBarrasAction();
        });
    }

    @FXML
    public void btnInformarCpfClick() {
        abrirTecladoTouch("Informar CPF do Cliente", cpfStr -> {
            String limpo = cpfStr.replaceAll("\\D", "");
            if (limpo.isEmpty()) {
                return;
            }
            long cpf = Long.parseLong(limpo);
            for (Cliente c : cDAO.getResults()) {
                if (c.getCpf() == cpf) {
                    clienteIdentificado = c;
                    lblClienteStatus.setText("Cliente: " + c.getNome());
                    exibeAlerta(AlertType.INFORMATION, "Cliente Identificado", "Bem-vindo(a), " + c.getNome() + "!");
                    return;
                }
            }
            exibeAlerta(AlertType.WARNING, "CPF não encontrado", "Nenhum cliente cadastrado com este CPF.");
        });
    }

    @FXML
    public void btnFinalizarClick() {
        if (carrinho.isEmpty()) {
            exibeAlerta(AlertType.WARNING, "Carrinho vazio", "Passe pelo menos um item pelo leitor antes de finalizar.");
            focarLeitor();
            return;
        }

        for (Caixa item : carrinho) {
            Produto p = item.getProduto();
            p.setQtd(p.getQtd() - item.getQuantidade());
            pDAO.update(p);
        }

        String clienteNome = (clienteIdentificado != null) ? clienteIdentificado.getNome() : "Consumidor Final";
        exibeAlerta(AlertType.INFORMATION, "Compra Finalizada!",
                "Obrigado pela preferência!\nCliente: " + clienteNome + "\nTotal Pago: " + lblTotalDireita.getText());

        listaProdutos = pDAO.getResults();
        limparTela();
        focarLeitor();
    }

    public void limparTela() {
        carrinho.clear();
        clienteIdentificado = null;
        lblClienteStatus.setText("CPF: Não informado");
        lblDestaqueNome.setText("AGUARDANDO LEITURA...");
        lblDestaqueInfo.setText("Aproxime o código de barras do leitor");
        txtCodBarras.clear();
        txtProdutoAtual.clear();
        txtPrecoAtual.clear();
        atualizarTotais();
    }

    public void abrirTecladoTouch(String titulo, Consumer<String> aoConfirmar) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(titulo);

        VBox root = new VBox(12);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);

        TextField visor = new TextField();
        visor.setPrefHeight(50);
        visor.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-alignment: CENTER;");

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);

        String[][] teclas = {
            {"7", "8", "9"},
            {"4", "5", "6"},
            {"1", "2", "3"},
            {"LIMPAR", "0", "OK"}
        };

        for (int r = 0; r < teclas.length; r++) {
            for (int c = 0; c < teclas[r].length; c++) {
                String txt = teclas[r][c];
                Button b = new Button(txt);
                b.setPrefSize(95, 65);
                b.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                if (txt.equals("LIMPAR")) {
                    b.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #f8d7da;");
                    b.setOnAction(e -> visor.clear());
                } else if (txt.equals("OK")) {
                    b.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #77b82a; -fx-text-fill: white;");
                    b.setOnAction(e -> {
                        dialog.close();
                        aoConfirmar.accept(visor.getText());
                    });
                } else {
                    b.setOnAction(e -> visor.setText(visor.getText() + txt));
                }
                grid.add(b, c, r);
            }
        }

        root.getChildren().addAll(visor, grid);
        dialog.setScene(new Scene(root, 340, 380));
        dialog.showAndWait();
        focarLeitor();
    }

    public void exibeAlerta(AlertType tipo, String titulo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
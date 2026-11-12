/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.dev.miguel.dukemarket.javafx.DAO;

import br.dev.miguel.dukemarket.javafx.model.Cliente;
import connection2.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sesi2dib
 */
public class ClienteDAO {

    private static final String SQL_INSERT = "INSERT INTO cliente (cpf, nome, endereco, cidade, uf) "
            + "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM cliente";
    private static final String SQL_SELECT_ID = "SELECT * FROM cliente "
            + "WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE cliente SET cpf = ?, "
            + "nome = ?, endereco = ?, cidade = ?, uf = ? "
            + "WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM cliente WHERE id = ?";

    /**
     * Insere um cliente na base de dados.
     *
     * @param c Cliente a ser inserido.
     */
    public void create(Cliente c) {
        Connection conn = MySQLConnection.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setLong(1, c.getCpf());
            stmt.setString(2, c.getNome());
            stmt.setString(3, c.getEndereco());
            stmt.setString(4, c.getCidade());
            stmt.setString(5, c.getUf());

            int auxRetorno = stmt.executeUpdate();

            Logger.getLogger(ClienteDAO.class.getName()).log(Level.INFO, null,
                    "Inclusao: " + auxRetorno);

        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            MySQLConnection.closeConnection(conn, stmt);
        }
    }

    /**
     * Retorna todos os registros da tabela cliente.
     *
     * @return Lista de clientes.
     */
    public List<Cliente> getResults() {
        Connection conn = MySQLConnection.getConnection();
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Cliente c = null;
        List<Cliente> listaClientes = null;

        try {
            stmt = conn.prepareStatement(SQL_SELECT_ALL);
            rs = stmt.executeQuery();

            listaClientes = new ArrayList<>();

            while (rs.next()) {
                c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setCpf(rs.getLong("cpf"));
                c.setNome(rs.getString("nome"));
                c.setEndereco(rs.getString("endereco"));
                c.setCidade(rs.getString("cidade"));
                c.setUf(rs.getString("uf"));
                c.setDataCadastro(rs.getString("dataCadastro"));

                listaClientes.add(c);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            MySQLConnection.closeConnection(conn, stmt);
        }

        return listaClientes;
    }

    /**
     * Retorna um cliente da tabela cliente com base no ID fornecido.
     *
     * @param id Identificação do Cliente.
     * @return Objeto Cliente encontrado ou null.
     */
    public Cliente getResultById(int id) {
        Connection conn = MySQLConnection.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cliente c = null;

        try {
            stmt = conn.prepareStatement(SQL_SELECT_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setCpf(rs.getLong("cpf"));
                c.setNome(rs.getString("nome"));
                c.setEndereco(rs.getString("endereco"));
                c.setCidade(rs.getString("cidade"));
                c.setUf(rs.getString("uf"));
                c.setDataCadastro(rs.getString("dataCadastro"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            MySQLConnection.closeConnection(conn, stmt);
        }

        return c;
    }

    /**
     * Atualiza um registro na tabela cliente.
     *
     * @param c Cliente a ser atualizado.
     */
    public void update(Cliente c) {
        Connection conn = MySQLConnection.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setLong(1, c.getCpf());
            stmt.setString(2, c.getNome());
            stmt.setString(3, c.getEndereco());
            stmt.setString(4, c.getCidade());
            stmt.setString(5, c.getUf());
            stmt.setInt(6, c.getId());

            int auxRetorno = stmt.executeUpdate();

            Logger.getLogger(ClienteDAO.class.getName()).log(Level.INFO, null,
                    "Update: " + auxRetorno);

        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            MySQLConnection.closeConnection(conn, stmt);
        }
    }

    /**
     * Exclui um cliente com base no ID fornecido.
     *
     * @param id Identificação do cliente.
     */
    public void delete(int id) {
        Connection conn = MySQLConnection.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, id);

            int auxRetorno = stmt.executeUpdate();

            Logger.getLogger(ClienteDAO.class.getName()).log(Level.INFO, null,
                    "Delete: " + auxRetorno);

        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            MySQLConnection.closeConnection(conn, stmt);
        }
    }
}
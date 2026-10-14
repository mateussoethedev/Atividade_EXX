package conexao_bd;

import backEnd.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {

    public void salvar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, email, cpf, telefone, senha, cep, logradouro, numero, complemento, bairro, cidade, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection connection = Conexao.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getTelefone());
            stmt.setString(5, cliente.getSenha());
            stmt.setString(6, cliente.getCep());
            stmt.setString(7, cliente.getLogradouro());
            stmt.setString(8, cliente.getNumero()); //
            stmt.setString(9, cliente.getComplemento());
            stmt.setString(10, cliente.getBairro());
            stmt.setString(11, cliente.getCidade());
            stmt.setString(12, cliente.getEstado());

            // Como usamos RETURNING id, usamos executeQuery para pegar o resultado do ID gerado
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente.setId(rs.getLong(1));
                }
            }

            System.out.println("Cliente salvo com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente: " + e.getMessage());
        }
    }
}
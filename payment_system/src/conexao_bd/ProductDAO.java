package conexao_bd;

import backEnd.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDAO {

    public void salvarProduct(Produto produto) {
        String sql = "INSERT INTO produto (nome, preco, quantidade, sku) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setBigDecimal(2, produto.getPreco());
            stmt.setLong(3, produto.getQuantidade());
            stmt.setString(4, produto.getSku());

            stmt.executeUpdate();
            System.out.println("Produto salvo com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o Produto: " + e.getMessage());
        }
    }
}
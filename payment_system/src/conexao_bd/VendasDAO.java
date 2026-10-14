package conexao_bd;

import backEnd.Produto;
import backEnd.Vendas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VendasDAO {

    public void salvarVendas(Vendas vendas) {
        String sqlVenda = "INSERT INTO vendas (id_cliente, data_venda, cep, logradouro, numero, complemento, bairro, cidade, estado, forma_pagamento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING num_venda";
        String sqlBuscaIdProduto = "SELECT id FROM produto WHERE sku = ?";
        String sqlItem = "INSERT INTO item_vendas (id_venda, id_produto, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);

            long numVendaGerado = 0;

            // 1. Salva a venda principal preenchendo os dados do cliente/endereço e recuperando o num_venda gerado
            try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda)) {
                stmtVenda.setLong(1, vendas.getCliente().getId());
                stmtVenda.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                stmtVenda.setString(3, vendas.getCliente().getCep());
                stmtVenda.setString(4, vendas.getCliente().getLogradouro());
                stmtVenda.setString(5, vendas.getCliente().getNumero());
                stmtVenda.setString(6, vendas.getCliente().getComplemento());
                stmtVenda.setString(7, vendas.getCliente().getBairro());
                stmtVenda.setString(8, vendas.getCliente().getCidade());
                stmtVenda.setString(9, vendas.getCliente().getEstado());
                stmtVenda.setString(10, vendas.getForma_pagamento());

                try (ResultSet rs = stmtVenda.executeQuery()) {
                    if (rs.next()) {
                        numVendaGerado = rs.getLong(1);
                    }
                }
            }

            // 2. Salva os itens percorrendo a lista de produtos da venda
            try (PreparedStatement stmtBuscaProd = conn.prepareStatement(sqlBuscaIdProduto);
                 PreparedStatement stmtItem = conn.prepareStatement(sqlItem)) {

                for (Produto produto : vendas.getProdutos()) {

                    // Descobre o ID real do produto no banco usando o SKU
                    stmtBuscaProd.setString(1, produto.getSku());
                    long idProdutoBanco = 0;

                    try (ResultSet rsProd = stmtBuscaProd.executeQuery()) {
                        if (rsProd.next()) {
                            idProdutoBanco = rsProd.getLong("id");
                        } else {
                            throw new SQLException("Produto com SKU " + produto.getSku() + " não encontrado no banco!");
                        }
                    }

                    // Insere o item na tabela item_vendas com o preço congelado
                    stmtItem.setLong(1, numVendaGerado);
                    stmtItem.setLong(2, idProdutoBanco);
                    stmtItem.setLong(3, produto.getQuantidade());
                    stmtItem.setBigDecimal(4, produto.getPreco());

                    stmtItem.executeUpdate();
                }
            }

            // Confirma a transação (tudo certo)
            conn.commit();
            vendas.setNumVenda(numVendaGerado);
            System.out.println("Venda e itens salvos com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar a venda, realizando rollback... " + e.getMessage());
            throw new RuntimeException("Erro ao salvar a venda: " + e.getMessage());
        }
    }
    public void atualizarCheckoutId(Long numVenda, String checkoutId) {
        String sql = "UPDATE vendas SET id_checkout = ? WHERE num_venda = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, checkoutId);
            stmt.setLong(2, numVenda);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar id_checkout: " + e.getMessage());
        }
    }

    public void atualizarStatus(Long numVenda, String status) {
        String sql = "UPDATE vendas SET status_venda = ? WHERE num_venda = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setLong(2, numVenda);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status_venda: " + e.getMessage());
        }

    }
    public void atualizarStatusPorCheckout(String checkoutId, String status) {
        String sql = "UPDATE vendas SET status_venda = ? WHERE id_checkout = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, checkoutId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status_venda: " + e.getMessage());
        }
    }


}
package backEnd;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Produto {
    private BigInteger id;
    private String nome;
    private BigDecimal preco;
    private Long quantidadeEstoque;
    private String sku;

    public Produto() {}

    @Override
    public String toString() {
        return "Produto " +
                "nome= " + nome +
                ", preco= " + preco +
                ", quantidade= " + quantidadeEstoque +
                ", sku= " + sku;
    }

    public Produto(String nome, BigDecimal preco, Long quantidadeEstoque, String sku) {
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.sku = sku;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Long getQuantidade() {
        return quantidadeEstoque;
    }

    public void setQuantidade(Long quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
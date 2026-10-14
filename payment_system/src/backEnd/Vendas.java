package backEnd;

import conexao_bd.Mtd_pagamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vendas {
    private Long numVenda;
    private Cliente cliente;
    private Mtd_pagamento mtd_pagamento;
    private String forma_pagamento;

    private List<Produto> produtos = new ArrayList<Produto>();

    public List<Produto> getProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    public void addProduto(Produto produto){
        produtos.add(produto);
    }

    public Vendas(){}

    public Vendas(Long numVenda, Cliente cliente, Mtd_pagamento mtd_pagamento) {
        this.numVenda = numVenda;
        this.cliente = cliente;
        this.mtd_pagamento = mtd_pagamento;
    }

    @Override
    public String toString() {
        return "Vendas{" +
                "numVenda=" + numVenda +
                ", cliente=" + cliente +
                ", mtd_pagamento=" + mtd_pagamento +
                ", forma_pagamento='" + forma_pagamento + '\'' +
                ", produtos=" + produtos +
                '}';
    }

    public String getForma_pagamento() {
        return forma_pagamento;
    }

    public void setForma_pagamento(String forma_pagamento) {
        this.forma_pagamento = forma_pagamento;
    }

    public Long getNumVenda() {
        return numVenda;
    }

    public void setNumVenda(Long numVenda) {
        this.numVenda = numVenda;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
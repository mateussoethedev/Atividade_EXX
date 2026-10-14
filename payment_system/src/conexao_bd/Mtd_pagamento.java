package conexao_bd;

public enum Mtd_pagamento {
    PIX("Pix"),
    CARTAO("Cartao"),
    BOLETO_BANCARIO("boleto"),
    CARTEIRA_DIGITAL("Carteira Digital");

    private String descricao;

    Mtd_pagamento(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

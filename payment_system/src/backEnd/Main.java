package backEnd;

import conexao_bd.ClienteDAO;
import conexao_bd.Mtd_pagamento;
import conexao_bd.ProductDAO;
import conexao_bd.VendasDAO;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Scanner;

public class  Main{
    static void main() throws SQLException, IOException, InterruptedException {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        Cliente cliente = new Cliente();
        ClienteDAO clienteDAO = new ClienteDAO();
        Produto produto = new Produto();
        ProductDAO productDAO = new ProductDAO();
        Vendas vendas = new Vendas();
        VendasDAO vendasDAO = new VendasDAO();


        System.out.println("Primeiro Digite o seu nome");
        cliente.setNome(sc.nextLine());
        System.out.println("Digite seu Email");
        cliente.setEmail(sc.nextLine());
        System.out.println("Digite seu CPF");
        cliente.setCpf(sc.next());
        System.out.println("Digite seu Telefone");
        sc.nextLine();
        cliente.setTelefone(sc.next());
        sc.nextLine();
        System.out.println("Digite a sua senha");
        cliente.setSenha(sc.nextLine());
        System.out.println("Digite o seu cep");
        cliente.setCep(sc.next());
        System.out.println("Digite o seu logradouro");
        cliente.setLogradouro(sc.next());
        System.out.println("Digite seu numero");
        cliente.setNumero(sc.next());
        System.out.println("Digite o seu complemento");
        cliente.setComplemento(sc.next());
        System.out.println("Digite seu bairro");
        cliente.setBairro(sc.next());
        System.out.println("Digite a sua cidade");
        cliente.setCidade(sc.next());
        System.out.println("Digite o seu estado");
        cliente.setEstado(sc.next());

        System.out.println("Digite o nome do Produto");
        sc.nextLine();
        produto.setNome(sc.nextLine());
        System.out.println("Digite o preço do Produto");
        produto.setPreco(sc.nextBigDecimal());
        System.out.println("Digite a quantidade no estoque");
        produto.setQuantidade(sc.nextLong());
        System.out.println("Digite o SKU do produto");
        produto.setSku(sc.next());
        System.out.println("Escolha a forma de pagamento:");
        System.out.println("1 - PIX");
        System.out.println("2 - CARTAO");
        System.out.println("3 - BOLETO_BANCARIO");
        System.out.println("4 - CARTEIRA_DIGITAL");

        int opcaoPagamento = sc.nextInt();
        Mtd_pagamento pagamentoEscolhido = null;

        switch (opcaoPagamento) {
            case 1:
                pagamentoEscolhido = Mtd_pagamento.PIX;
                break;
            case 2:
                pagamentoEscolhido = Mtd_pagamento.CARTAO;
                break;
            case 3:
                pagamentoEscolhido = Mtd_pagamento.BOLETO_BANCARIO;
                break;
            case 4:
                pagamentoEscolhido = Mtd_pagamento.CARTEIRA_DIGITAL;
                break;
            default:
                System.out.println("Opção inválida! Definindo PIX como padrão.");
                pagamentoEscolhido = Mtd_pagamento.PIX;
        }


        vendas.setForma_pagamento(pagamentoEscolhido.name());
        productDAO.salvarProduct(produto);
        clienteDAO.salvar(cliente);
        vendas.setCliente(cliente);
        vendas.addProduto(produto);
        vendasDAO.salvarVendas(vendas);

        String apiKey = System.getenv("ASAAS_API_KEY");

        StringBuilder items = new StringBuilder();
        for (Produto p : vendas.getProdutos()) {
            if (items.length() > 0) items.append(",");
            items.append("""
            {"name": "%s", "quantity": 1, "value": %s}
            """.formatted(p.getNome(), p.getPreco().toPlainString()));
        }

        String body = """
        {
          "billingTypes": ["CREDIT_CARD", "PIX"],
          "chargeTypes": ["DETACHED"],
          "minutesToExpire": 30,
          "externalReference": "%s",
          "callback": {
            "successUrl": "https://example.com/sucesso",
            "cancelUrl": "https://example.com/cancelado",
            "expiredUrl": "https://example.com/expirado"
          },
          "items": [%s]
        }
        """.formatted(vendas.getNumVenda(), items);

        String json = "{\"nome\":\"Ana\",\"idade\":20}";
        String bodyPix = "{\"type\":\"EVP\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api-sandbox.asaas.com/v3/checkouts"))
                .header("Content-Type", "application/json")
                .header("User-Agent", "payment_system")
                .header("access_token", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());

        if (response.statusCode() != 200){
            System.out.println("Checkout creation failed");
            return;
        }

        String bodyResposta = response.body();
        int inicio = bodyResposta.indexOf("\"id\":\"") + 6;
        int fim = bodyResposta.indexOf("\"", inicio);
        String checkoutId = bodyResposta.substring(inicio, fim);
        System.out.println("Checkout ID: " + checkoutId);

        vendasDAO.atualizarCheckoutId(vendas.getNumVenda(), checkoutId);
        sc.close();
    }
}
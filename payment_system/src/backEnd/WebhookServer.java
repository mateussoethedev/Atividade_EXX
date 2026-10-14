package backEnd;

import conexao_bd.VendasDAO;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebhookServer {
    public static void main(String[] args) throws IOException {

        VendasDAO vendasDAO = new VendasDAO();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/webhook", exchange -> {
            String token = exchange.getRequestHeaders().getFirst("asaas-access-token");
            String expectedToken = System.getenv("ASAAS_WEBHOOK_TOKEN");

            if (token == null || !token.equals(expectedToken)) {
                exchange.sendResponseHeaders(401, -1);
                exchange.close();
                return;
            }

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String event = extract(body, "\"event\"\\s*:\\s*\"([^\"]+)\"");
            String checkoutId = extract(body, "\"checkout\"\\s*:\\s*\\{\\s*\"id\"\\s*:\\s*\"([^\"]+)\"");
            System.out.println(event + " / " + checkoutId);

            if ("CHECKOUT_PAID".equals(event) && checkoutId != null) {
                vendasDAO.atualizarStatusPorCheckout(checkoutId, "PAGO");
            }

            System.out.println(body);
            exchange.sendResponseHeaders(200, -1);
            exchange.close();
        });

        server.start();
        System.out.println("Webhook server running on port 8080");
    }
    private static String extract(String body, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(body);
        return matcher.find() ? matcher.group(1) : null;
    }

}
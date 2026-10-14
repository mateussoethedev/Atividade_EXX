package backEnd;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RegisterWebhook {
    static void main() throws IOException, InterruptedException {
        String body = """
{
  "email": "mateussoethe7@gmail.com",
  "interrupted": false,
  "apiVersion": 3,
  "name": "payment_system webhook",
  "url": "https://egging-grandly-shorthand.ngrok-free.dev/webhook",
  "enabled": true,
  "sendType": "SEQUENTIALLY",
  "authToken": "%s",
  "events": ["CHECKOUT_PAID"]
}
""".formatted(System.getenv("ASAAS_WEBHOOK_TOKEN"));

    String apiKey = System.getenv("ASAAS_API_KEY");

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api-sandbox.asaas.com/v3/webhooks"))
            .header("Content-Type", "application/json")
            .header("User-Agent", "payment_system")
            .header("access_token", apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());

System.out.println(response.statusCode());
System.out.println(response.body());
    }
}

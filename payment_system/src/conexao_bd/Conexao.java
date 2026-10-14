package conexao_bd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    // 1. Dados de acesso
    private static final String URL = "jdbc:postgresql://localhost:5432/payment_system";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres";

    // 2. Método que devolve a conexão
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco: " + e.getMessage());
        }
    }
}
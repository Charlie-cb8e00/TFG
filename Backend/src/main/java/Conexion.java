import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://192.168.96.222:5432/GeoBus";
        String usuario = "postgres";
        String password = "Punta.2007";

        try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {
            System.out.println("Conexión realizada correctamente");

        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }
}

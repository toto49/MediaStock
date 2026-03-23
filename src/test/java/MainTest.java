import com.eseo.mediastock.dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * The type Main test.
 */
public class MainTest {

    /**
     * Main.
     *
     * @param args the args
     */
    static void main(String[] args) {

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connexion réussie !");
            } else {
                System.out.println("La connexion est nulle !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion : " + e.getMessage());
        }
    }
}


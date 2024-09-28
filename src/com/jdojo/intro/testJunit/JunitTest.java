package com.jdojo.intro.testJunit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.jdojo.intro.DatabaseConnection;
import com.jdojo.intro.HelloFxApp;

import javafx.application.Platform;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class JunitTest {

    // Initialiser le toolkit JavaFX avant l'exécution des tests
    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {});  // Initialisation du toolkit JavaFX
    }

    // Test pour vérifier la recherche avec un matricule valide
    @Test
    void testSearchResultValidMatricule() {
        Platform.runLater(() -> {
            HelloFxApp app = new HelloFxApp();
            app.start(new Stage());  // Démarrer l'application
            app.matriculeField.setText("001");  // Saisir un matricule valide
            app.searchResult();  // Exécuter la recherche
            assertEquals("Résultat : ADMISE", app.resultLabel.getText(), 
                "Le résultat doit être 'ADMISE' pour un matricule valide.");
        });
    }

    // Test pour vérifier la recherche avec un matricule invalide
    @Test
    void testSearchResultInvalidMatricule() {
        Platform.runLater(() -> {
            HelloFxApp app = new HelloFxApp();
            app.start(new Stage());  // Démarrer l'application
            app.matriculeField.setText("00000");  // Saisir un matricule invalide
            app.searchResult();  // Exécuter la recherche
            assertEquals("Aucun résultat trouvé pour ce matricule.", app.resultLabel.getText(),
                "Le label doit indiquer qu'aucun résultat n'est trouvé.");
        });
    }

    // Test pour vérifier que le bouton de détails est caché par défaut
    @Test
    void testDetailsButtonHiddenByDefault() {
        Platform.runLater(() -> {
            HelloFxApp app = new HelloFxApp();
            app.start(new Stage());  // Démarrer l'application
            // Vérifie si le bouton est caché par défaut
            assertFalse(app.detailsButton.isVisible(), 
                "Le bouton Détails doit être caché par défaut.");
        });
    }

    // Test pour vérifier la connexion à la base de données
    @Test
    void testDatabaseConnection() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        assertNotNull(conn, "La connexion à la base de données ne doit pas être nulle.");
    }

    // Test pour vérifier que le matricule saisi est récupéré correctement
    @Test
    void testGetMatriculeInput() {
        Platform.runLater(() -> {
            HelloFxApp app = new HelloFxApp();
            app.start(new Stage());  // Démarrer l'application
            app.matriculeField.setText("001");
            assertEquals("001", app.matriculeField.getText(),
                "Le champ matricule doit retourner la valeur saisie '001'.");
        });
    }
}

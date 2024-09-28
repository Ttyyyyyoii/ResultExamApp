package com.jdojo.intro.testJunit;

import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;

import com.jdojo.intro.DatabaseConnection;
import com.jdojo.intro.HelloFxApp;

import javafx.application.Platform;
import javafx.stage.Stage;


import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;



public class JunitAssertJTest {

    // Initialiser le toolkit JavaFX avant l'exécution des tests
    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {});
    }

    // Vérification de la visibilité du bouton "Détails" après une recherche réussie
    @Test
    void testDetailsButtonVisibleAfterSearchSuccess() {
        Platform.runLater(() -> {
            HelloFxApp app = new HelloFxApp();
            app.start(new Stage());
            app.matriculeField.setText("001");  // Matricule valide
            app.searchResult();  // Exécuter la recherche

            assertThat(app.detailsButton.isVisible())
                .as("Le bouton Détails doit être visible après une recherche réussie")
                .isTrue();
        });
    }

    // Vérification du message d'erreur pour un matricule invalide
    @Test
    void testErrorMessageForInvalidMatricule() {
        Platform.runLater(() -> {
            HelloFxApp app = new HelloFxApp();
            app.start(new Stage());
            app.matriculeField.setText("999999");  // Matricule invalide
            app.searchResult();

            assertThat(app.resultLabel.getText())
                .as("Le label doit indiquer qu'aucun résultat n'est trouvé pour un matricule invalide")
                .isEqualTo("Aucun résultat trouvé pour ce matricule.");
        });
    }

    // Vérification de la connexion à la base de données
    @Test
    void testDatabaseConnectionWithAssertJ() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        assertThat(conn)
            .as("La connexion à la base de données doit être établie")
            .isNotNull();
    }

    // Vérification que la barre de progression est visible pendant la recherche
    @Test
    void testProgressBarVisibilityDuringSearch() {
        Platform.runLater(() -> {
            HelloFxApp app = new HelloFxApp();
            app.start(new Stage());
            app.matriculeField.setText("001");  // Matricule valide
            app.searchResult();

            assertThat(app.progressBarSearchUser.isVisible())
                .as("La barre de progression doit être visible pendant la recherche")
                .isTrue();
        });
    }

    // Vérification que le champ matricule est correctement renseigné
    @Test
    void testMatriculeFieldInputWithAssertJ() {
        Platform.runLater(() -> {
            HelloFxApp app = new HelloFxApp();
            app.start(new Stage());
            app.matriculeField.setText("001");

            assertThat(app.matriculeField.getText())
                .as("Le champ matricule doit contenir la valeur saisie '001'")
                .isEqualTo("001");
        });
    }
}

package com.jdojo.intro;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.*;

// Classe principale qui hérite de Application, ce qui est requis pour une application JavaFX
public class HelloFxApp extends Application {

    // Champs pour entrer le matricule, afficher le résultat, et un bouton pour afficher les détails
	public TextField matriculeField;
	public Label resultLabel;
	public Button detailsButton;
	public ProgressIndicator progressBarSearchUser;

    // Point d'entrée pour démarrer l'interface graphique
    @Override
    public void start(Stage primaryStage) {
        // Définir le titre de la fenêtre principale (Stage)
        primaryStage.setTitle("Consultation de Résultats");

        // Champ de texte pour entrer le matricule
        matriculeField = new TextField();
        matriculeField.setPromptText("Entrez votre matricule"); // Placeholder
        matriculeField.getStyleClass().add("text-field"); // Style CSS associé

        // Bouton de recherche pour lancer la recherche du résultat
        Button searchButton = new Button("Rechercher");
        // Action liée au clic du bouton de recherche
        searchButton.setOnAction(e -> {
        	
        	searchButton.setVisible(false);
        	searchButton.setManaged(false);
            
            progressBarSearchUser.setVisible(true);
            progressBarSearchUser.setManaged(true);

            // Exécuter searchResult() dans un thread séparé
            new Thread(() -> {
                // Effectuer la recherche (longue opération)
                searchResult();

                // Mettre à jour l'UI après la fin de searchResult()
                Platform.runLater(() -> {
                    progressBarSearchUser.setManaged(false);
                    progressBarSearchUser.setVisible(false);
                    
                    searchButton.setVisible(true);
                	searchButton.setManaged(true);
                });
            }).start();
        });

        searchButton.getStyleClass().add("button");

        // Label pour afficher le résultat après la recherche
        resultLabel = new Label();
        resultLabel.getStyleClass().add("label");

        // Bouton pour afficher les détails, caché par défaut
        detailsButton = new Button("Afficher les détails");
        detailsButton.setVisible(false); // Caché jusqu'à ce que le résultat soit "Succès"
        detailsButton.setOnAction(e -> showDetails()); // Action liée au clic pour afficher les détails
        detailsButton.getStyleClass().add("button");
        
        // progress-bar pour recherhcher un matricule
        progressBarSearchUser = new ProgressIndicator();
        progressBarSearchUser.setPrefWidth(57);
        progressBarSearchUser.setPrefHeight(38);
        progressBarSearchUser.setVisible(false);
        
        HBox btnSearchAndDetailUserHBox = new HBox();
        btnSearchAndDetailUserHBox.getChildren().addAll(searchButton, progressBarSearchUser, detailsButton, resultLabel);
        HBox.setMargin(searchButton, new Insets(5, 10, 5, 0)); // marges pour searchButton
        HBox.setMargin(progressBarSearchUser, new Insets(5, 0, 5, 10)); // marges pour detailsButton
        HBox.setMargin(detailsButton, new Insets(5, 0, 5, 10)); // marges pour detailsButton
        HBox.setMargin(resultLabel, new Insets(6, 0, 5, 10)); // marges pour resultLabel
        
        //mettre le le champs matricule et les bouton search, detail et resulat dans une vbox
        VBox conteneurChampMatriculeEtBtnSearchAndDetailUserHBox = new VBox();
        conteneurChampMatriculeEtBtnSearchAndDetailUserHBox.getChildren().addAll(matriculeField, btnSearchAndDetailUserHBox);
        conteneurChampMatriculeEtBtnSearchAndDetailUserHBox.getStyleClass().add("conteneurChampMatriculeEtBtnSearchAndDetailUserHBox");
        
        
        
        
        
        //ajout de nouvel box pour ajout de nouvelle donnee dans la base de donnee
        
        
       
        // Disposition des éléments dans une boîte verticale (VBox)
        TreeTableView<Object> table = new TreeTableView<Object>();
        VBox vbox = new VBox();
        vbox.getChildren().addAll(new Label("Matricule :"), conteneurChampMatriculeEtBtnSearchAndDetailUserHBox);
        vbox.setPadding(new Insets(10)); // Ajout de marges internes
        vbox.getStyleClass().add("vbox"); // Style CSS associé

        // Création d'une scène avec la VBox comme racine
        Scene scene = new Scene(vbox, 1200, 600); // 1200x600 est la taille de la fenêtre
        // Ajout du fichier CSS pour le style
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        // Ajout de la scène à la fenêtre principale et affichage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour rechercher le résultat en fonction du matricule entré
    public void searchResult() {
        String matricule = matriculeField.getText(); // Récupérer le texte entré
        System.out.println("Recherche pour le matricule : " + matricule);

        // Connexion à la base de données
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Préparation de la requête SQL pour chercher l'étudiant avec ce matricule
            String sql = "SELECT etudiants.* FROM etudiants WHERE matricule = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, matricule); // Associer le matricule à la requête
            ResultSet rs = pstmt.executeQuery(); // Exécuter la requête

            if (rs.next()) { // Si un résultat est trouvé
                // Récupération des détails de l'étudiant
                
                String resultat = rs.getString("resultat");

                // Affichage du résultat dans le label
                //resultLabel.setText("Résultat : " + resultat);
             
                resultLabel.getStyleClass().add("resultLabel");
       
                System.out.println("Résultat trouvé : " + resultat);

                // Afficher le bouton de détails, quel que soit le résultat
                detailsButton.setVisible(true);
                
                Platform.runLater(() -> {
                    resultLabel.setText("Résultat : " + resultat);
                });

            } else { // Si aucun résultat n'est trouvé
               // resultLabel.setText("Aucun résultat trouvé");
                detailsButton.setVisible(false); // Cacher le bouton de détails
                System.out.println("Aucun résultat trouvé pour ce matricule.");
                
                Platform.runLater(() -> {
                    resultLabel.setText("Aucun résultat trouvé pour ce matricule.");
                    resultLabel.getStyleClass().add("resultLabel");
                });
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Affiche l'erreur SQL dans la console
            //resultLabel.setText("Erreur lors de la recherche"); // Affiche un message d'erreur à l'utilisateur
            Platform.runLater(() -> {
                resultLabel.setText("Erreur lors de la recherche.");
            });
        }
    }

    // Méthode pour afficher les détails dans une nouvelle fenêtre (Stage)
    private void showDetails() {
        String matricule = matriculeField.getText(); // Récupérer le matricule entré

        // Connexion à la base de données pour récupérer les détails
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM etudiants WHERE matricule = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, matricule); // Associer le matricule à la requête
            ResultSet rs = pstmt.executeQuery(); // Exécuter la requête

            if (rs.next()) { // Si un résultat est trouvé
                // Récupération des informations
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String dateNaissance = rs.getString("date_naissance");
                String ecole = rs.getString("ecole");
                float moyenne = rs.getFloat("moyenne");

                // Création d'une nouvelle fenêtre pour afficher les détails
                Stage detailsStage = new Stage(); // Nouveau Stage (fenêtre)
                detailsStage.setTitle("Détails de l'étudiant");

                // Création des labels pour afficher les informations
                Label matriculeLabel = new Label("Matricule: " + matricule);
                Label nomLabel = new Label("Nom: " + nom);
                Label prenomLabel = new Label("Prénom: " + prenom);
                Label dateNaissanceLabel = new Label("Date de naissance: " + dateNaissance);
                Label ecoleLabel = new Label("École: " + ecole);
                Label moyenneLabel = new Label("Moyenne: " + moyenne + " / 20");

                // Disposition des éléments dans une VBox
                VBox vbox = new VBox(10);
                vbox.getChildren().addAll(matriculeLabel, nomLabel, prenomLabel, dateNaissanceLabel, ecoleLabel, moyenneLabel);
                vbox.setPadding(new Insets(10)); // Ajouter des marges internes
                vbox.getStyleClass().add("vbox"); // Style CSS associé

                // Création d'une scène pour la nouvelle fenêtre et ajout des éléments
                Scene scene = new Scene(vbox, 800, 400);
             // Ajout du fichier CSS pour le style
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                
                detailsStage.setScene(scene); // Ajouter la scène à la nouvelle fenêtre
                detailsStage.show(); // Afficher la nouvelle fenêtre
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Afficher les erreurs SQL dans la console
        }
    }
    

    // Méthode principale pour démarrer l'application
    public static void main(String[] args) {
        launch(args); // Lance l'application JavaFX
    }
}

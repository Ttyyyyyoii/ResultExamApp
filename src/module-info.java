module HelloFx {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive java.sql;
    requires org.junit.jupiter.api;
    requires junit;
   
    requires org.assertj.core;

    opens com.jdojo.intro to javafx.fxml;
    exports com.jdojo.intro;
}

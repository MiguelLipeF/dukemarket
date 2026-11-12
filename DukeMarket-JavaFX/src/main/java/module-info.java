module br.dev.miguel.dukemarket.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    
    requires java.sql;

    opens br.dev.miguel.dukemarket.javafx to javafx.fxml;
    opens br.dev.miguel.dukemarket.javafx.controller to javafx.fxml;
    
    exports br.dev.miguel.dukemarket.javafx;
    exports br.dev.miguel.dukemarket.javafx.controller;
    exports br.dev.miguel.dukemarket.javafx.DAO;
    exports br.dev.miguel.dukemarket.javafx.model;
    exports connection2;
    
    requires mysql.connector.java;
    
    
}

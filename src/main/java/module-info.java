module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.desktop;
    requires java.sql;
    requires java.prefs;
    requires itextpdf;
    requires org.apache.pdfbox;
    requires jbcrypt;

    opens org.example to javafx.fxml;
    opens org.example.controllers to javafx.fxml;
    opens org.example.entities to org.hibernate.orm.core;
    exports org.example;
    exports org.example.entities;
    exports org.example.utils;
    exports org.example.dao;
    exports org.example.controllers;
    exports org.example.testDao;
    opens org.example.testDao to javafx.fxml;
    exports org.example.services;
    opens org.example.services to org.hibernate.orm.core;
    exports org.example.connection;
    opens org.example.connection to org.hibernate.orm.core;

}

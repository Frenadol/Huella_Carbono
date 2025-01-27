module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.testDao;
    opens org.example.testDao to javafx.fxml;
    opens org.example.entities to org.hibernate.orm.core;

}

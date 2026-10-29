module app.uc4javafxgabrieljoao {
    requires javafx.controls;
    requires javafx.fxml;

    opens app.uc4javafxgabrieljoao to javafx.fxml;
    opens app.uc4javafxgabrieljoao.controller to javafx.fxml;
    exports app.uc4javafxgabrieljoao;
}
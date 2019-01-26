package main;

import alerts.PopUpAlerts;
import database.Database;
import javafx.application.Platform;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class GenerateReport implements Runnable {
    int idClass;

    public GenerateReport(int idClass){
        this.idClass=idClass;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Platform.runLater( () -> PopUpAlerts.popAlertInformation("Suckes", "Raport wygenerowany pomyślnie", "Generowanie raportu"));
            JasperDesign jasperDesign = JRXmlLoader.load("resources\\reports\\Report.jrxml");
            String query = "select O.Imie, O.Nazwisko, K.Skrot from Osoba O inner join Osoba_Klasa OK on O.Login=OK.Login inner join Klasa K on K.ID_Klasy=OK.ID_Klasy where K.ID_Klasy=" + idClass;
            JRDesignQuery designQuery = new JRDesignQuery();
            designQuery.setText(query);
            jasperDesign.setQuery(designQuery);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, Database.getMySQLConnection());
            JasperExportManager.exportReportToPdfFile(jasperPrint, "resources\\reports\\RaportUczniowieKlasy.pdf");
        } catch (JRException e) {
            Platform.runLater( () -> PopUpAlerts.popAlertError("Błąd", "Wystąpił błąd podczas generowania raportu", "Generowanie raportu"));
            e.printStackTrace();
        }
    }
}

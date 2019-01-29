package main.report;

import main.utilities.alerts.PopUpAlerts;
import main.database.Database;
import javafx.application.Platform;
import main.utilities.navigator.Navigator;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class GenerateReport extends Navigator implements Runnable {
    int idClass;

    public GenerateReport(int idClass){
        this.idClass=idClass;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Platform.runLater( () -> PopUpAlerts.popAlertInformation(getResourceBundle().getString("Sucseed"), getResourceBundle().getString("RaportGenerated"), getResourceBundle().getString("RaportGeneratedContent")));
            JasperDesign jasperDesign = JRXmlLoader.load("main\\resources\\reports\\Report.jrxml");
            String query = "select O.Imie, O.Nazwisko, K.Skrot from Osoba O inner join Osoba_Klasa OK on O.LOGIN=OK.LOGIN inner join Klasa K on K.ID_KLASY=OK.ID_KLASY where K.ID_KLASY=" + idClass;
            JRDesignQuery designQuery = new JRDesignQuery();
            designQuery.setText(query);
            jasperDesign.setQuery(designQuery);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, Database.getMySQLConnection());
            JasperExportManager.exportReportToPdfFile(jasperPrint, "main\\resources\\reports\\RaportUczniowieKlasy.pdf");
        } catch (JRException e) {
            Platform.runLater( () -> PopUpAlerts.popAlertError(getResourceBundle().getString("ErrorCommunicat"), getResourceBundle().getString("RaportError"), getResourceBundle().getString("RaportGeneratedContent")));
            e.printStackTrace();
        }
    }
}

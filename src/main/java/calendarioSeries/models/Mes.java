/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Modelo SERIE
 *
 * @author Cotel
 */
public class Mes {

    private final StringProperty nombreMes;
    private final IntegerProperty numMes;
    private final IntegerProperty numAno;
    private final IntegerProperty diasMes;

    private YearMonth yearMonthObject;

    /**
     * Constructor por defecto
     * Crea un objeto Mes con el mes actual
     */
    public Mes() {
        Date fecha = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        this.numAno = new SimpleIntegerProperty(cal.get(Calendar.YEAR));
        this.numMes = new SimpleIntegerProperty(cal.get(Calendar.MONTH));

        yearMonthObject = YearMonth.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        this.nombreMes = new SimpleStringProperty(yearMonthObject.getMonth().
                getDisplayName(TextStyle.FULL, new Locale("es", "ES")));
        this.diasMes = new SimpleIntegerProperty(yearMonthObject.lengthOfMonth());

    }

    /**
     * Constructor
     * Crea un objeto Mes con el año y mes especificados
     *
     * @param numAno = numero de año
     * @param numMes = numero de mes
     */
    public Mes(int numAno, int numMes) {
        this.numAno = new SimpleIntegerProperty(numAno);
        this.numMes = new SimpleIntegerProperty(numMes);

        yearMonthObject = YearMonth.of(numAno, numMes + 1);
        this.nombreMes = new SimpleStringProperty(yearMonthObject.getMonth().
                getDisplayName(TextStyle.FULL, new Locale("es", "ES")));
        this.diasMes = new SimpleIntegerProperty(yearMonthObject.lengthOfMonth());

    }

    /**
     * Getters y Setters
     */

    public String getNombreMes() {
        return this.nombreMes.get();
    }

    public int getNumMes() {
        return this.numMes.get();
    }

    public int getNumAno() {
        return this.numAno.get();
    }

    public int getDiasMes() {
        return this.diasMes.get();
    }

    public StringProperty getNombreMesProperty() {
        return this.nombreMes;
    }

    public IntegerProperty getNumMesProperty() {
        return this.numMes;
    }

    public IntegerProperty getNumAnoProperty() {
        return this.numAno;
    }

    public IntegerProperty getDiasMesProperty() {
        return this.diasMes;
    }

}

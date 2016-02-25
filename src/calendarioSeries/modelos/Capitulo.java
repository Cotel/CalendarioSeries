/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.modelos;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Cotel
 */
public class Capitulo {
    
    private StringProperty titulo;
    private StringProperty fecha;
    private BooleanProperty visto;
    
    public Capitulo(String titulo, String fecha, boolean visto) {
        this.fecha = new SimpleStringProperty(fecha);
        this.titulo = new SimpleStringProperty(titulo);
        this.visto = new SimpleBooleanProperty(visto);
        
    }
    
    public void changeVisto() {
        if(this.visto.get() == true) {
            this.visto.setValue(false);
        } else {
            this.visto.setValue(true);
        }
    }

    public StringProperty getTitulo() {
        return titulo;
    }

    public void setTitulo(StringProperty titulo) {
        this.titulo = titulo;
    }

    public StringProperty getFecha() {
        return fecha;
    }

    public void setFecha(StringProperty fecha) {
        this.fecha = fecha;
    }

    public BooleanProperty getVisto() {
        return visto;
    }

    public void setVisto(BooleanProperty visto) {
        this.visto = visto;
    }
    
    
    
}

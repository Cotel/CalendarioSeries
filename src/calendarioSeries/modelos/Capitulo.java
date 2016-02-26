/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.modelos;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Cotel
 */
public class Capitulo implements Externalizable {
    
    private StringProperty titulo;
    private StringProperty fecha;
    private BooleanProperty visto;
    
    public Capitulo() {
        this.fecha = new SimpleStringProperty();
        this.titulo = new SimpleStringProperty();
        this.visto = new SimpleBooleanProperty();
    }
    
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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getFecha().get());
        out.writeObject(getTitulo().get());
        out.writeBoolean(getVisto().get());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.fecha.set((String) in.readObject());
        this.titulo.set((String) in.readObject());
        this.visto.set(in.readBoolean());
    }
    
    
    
}

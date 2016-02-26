/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.modelos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Modelo SERIE
 *
 * @author Cotel
 */
public class Serie implements Serializable{
    
    private static final String BASE = "https://www.omdbapi.com/?";
    
    private String titulo;
    private String id;
    private String description;
    private Capitulo[][] capitulos;
//    private String[][] capitulos;
//    private boolean[][] vistos;
    private int temporadas;
    //private JSONObject json;
    private String urlImagen;
    private String year;
    
    //private StringProperty tituloProperty;
    
    /**
     * Constructor Serie
     * @param id = id de Imdb de la serie a instanciar
     */
    public Serie(String id) {
        this.id = id;
        try {
            String toJson = readUrl(BASE + "i=" + id +
                    "&type=series" + "&plot=short" + "&r=json");
            JSONObject json = new JSONObject(toJson);
            
            if(json.getString("Response").equals("True")) {
                this.titulo = json.getString("Title");
                this.description = json.getString("Plot");
                //this.tituloProperty = new SimpleStringProperty(titulo);
                this.urlImagen = json.getString("Poster");
                this.year = json.getString("Year");

                boolean next = true;
                int temporada = 1;
                int caps = 0;
                JSONObject aux;
                while(next) {
                    aux = new JSONObject(readUrl(BASE + "i=" + id +
                            "&Season=" + temporada + "&r=json"));
                    if(!aux.getString("Response").equals("False")) {
                        temporada++;
                        int actual = aux.getJSONArray("Episodes").length();
                        if(actual > caps) caps = actual;
                    } else {
                        next = false;
                        temporada--;
                    }
                }

                this.temporadas = temporada;
                if(this.temporadas != 0) {
                    this.capitulos = new Capitulo[temporadas][caps];
                    //this.vistos = new boolean[temporadas][caps];
                    for(int i=0; i<capitulos.length; i++) {
                        aux = new JSONObject(readUrl(BASE + "i=" + id +
                                "&Season=" + (i+1) + "&r=json"));
                        JSONArray listaCaps = aux.getJSONArray("Episodes");
                        for(int j=0; j<capitulos[i].length; j++) {
                            try {
                                capitulos[i][j] = new Capitulo(listaCaps.getJSONObject(j).getString("Title"),
                                                listaCaps.getJSONObject(j).getString("Released"),
                                                false);
                            } catch (JSONException e) {
                                capitulos[i][j] = new Capitulo("N/A", "N/A", false);
                            }
                        }
                    }
                } else {
                    this.capitulos = null;
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }        
    }
    
    /**
     * getCapitulosMes
     * Método que devuelve un diccionario con los dias en que se publicarán
     * capítulos de la serie en este mes y sus títulos.
     * @param ano = numero de año
     * @param mes = numero de mes
     * @return Map<Integer, String> donde el primer valor es el dia y el segundo
     * el nombre del capítulo.
     */
    public Map<Integer, String> getCapitulosMes(int ano, int mes) {
        Map<Integer, String> capitulosMes = new HashMap<Integer, String>();
        
        if(this.temporadas == 0) {
            return capitulosMes;
        }
        
        for(int i=0; i<capitulos[temporadas-1].length; i++) {
            if(capitulos[temporadas-1][i] != null) {
                String fecha = capitulos[temporadas-1][i].getFecha().get();
                if(!fecha.equals("N/A")) {
                    String[] detalle = fecha.split("-");
                    if (Integer.parseInt(detalle[0]) == ano && Integer.parseInt(detalle[1]) == mes && capitulosMes.containsKey(Integer.parseInt(detalle[2]))) {
                        String existing = capitulosMes.get(Integer.parseInt(detalle[2]));
                        capitulosMes.put(Integer.parseInt(detalle[2]), existing + "\n" + capitulos[temporadas-1][i].getTitulo().get());
                    } else if(Integer.parseInt(detalle[0]) == ano && Integer.parseInt(detalle[1]) == mes) {
                        capitulosMes.put(Integer.parseInt(detalle[2]), capitulos[temporadas-1][i].getTitulo().get());
                    }
                }
            }
        }
        
        return capitulosMes;
    }
    
    /**
     * readUrl
     * Método que lee una url y devuelve la respuesta HTTP
     * @param stringUrl = Url a leer
     * @return String con la respuesta HTTP de la url.
     */
    private String readUrl(String stringUrl) {
        BufferedReader reader = null;
        try {
               URL url = new URL(stringUrl);         
            try {
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuffer buffer = new StringBuffer();
                int read;
                char[] chars = new char[1024];
                while((read = reader.read(chars)) != -1)
                    buffer.append(chars, 0, read);
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    reader.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * setVistosHasta
     * Método que modifica el array de booleanos que representa que capítulos
     * han sido vistos por el usuario hasta la temporada y capítulo introducidos.
     * @param temporada = numero de temporada
     * @param cap  = numero de capitulo
     */
    public void setVistosHasta(int temporada, int cap) {
        for(int i=0; i<=temporada; i++) {
            for(int j=0; j<=cap; j++) {
                capitulos[i][j].getVisto().set(true);
            }
        }
    }
    
    /**
     * Getters y Setters
     */
    public int[] getLastVisto() {
        int[] res = {0, 0};
        for(int i=0; i<this.capitulos.length; i++) {
            for(int j=0; j<this.capitulos[i].length; j++) {
                if(!this.capitulos[i][j].getVisto().getValue()) {
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
            }
        }
        return res;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public String getUrlImagen() {
        return urlImagen;
    }
    
    public String getYear() {
        return this.year;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getId() {
        return this.id;
    }
    
//    public boolean[][] getVistos() {
//        return this.vistos;
//    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
        //this.tituloProperty.setValue(titulo);
    }

    public Capitulo[][] getCapitulos() {
        return capitulos;
    }
    
//    public StringProperty getTituloProperty() {
//        return tituloProperty;
//    }

    public void setCapitulos(Capitulo[][] capitulos) {
        this.capitulos = capitulos;
    }

    public int getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        this.temporadas = temporadas;
    }
    
    public boolean equals(Serie serie) {
        if(this.id.equals(serie.getId())) {
            if(this.titulo.equals(serie.getTitulo())) {
                if(this.year.equals(serie.getYear())) {
                    if(this.temporadas == serie.getTemporadas()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    

    
//    public static void main(String[] args) {
//        Serie arrow = new Serie("tt3107288");
//        arrow.setVistosHasta(2, 14);
//        for(int i=0; i<arrow.getTemporadas(); i++) {
//            for(int j=0; j<arrow.getVistos()[i].length; j++) {
//                System.out.printf("%02dx%02d " + arrow.getVistos()[i][j] + "\n", i+1, j+1);
//            }
//        }
//        
//    }
    
}

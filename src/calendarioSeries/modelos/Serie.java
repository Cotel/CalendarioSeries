/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.modelos;

import com.sun.org.apache.xerces.internal.impl.dv.xs.YearMonthDV;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Cotel
 */
public class Serie {
    
    private static final String BASE = "https://www.omdbapi.com/?";
    
    private String titulo;
    private String id;
    private String description;
    private String[][] capitulos;
    private int temporadas;
    private JSONObject json;
    private String urlImagen;
    private String year;
    
    private StringProperty tituloProperty;
    
    public Serie(String id) {
        this.id = id;
        try {
            String toJson = readUrl(BASE + "i=" + id +
                    "&type=series" + "&plot=short" + "&r=json");
            this.json = new JSONObject(toJson);
            
            if(this.json.getString("Response").equals("True")) {
                this.titulo = json.getString("Title");
                this.description = json.getString("Plot");
                this.tituloProperty = new SimpleStringProperty(titulo);
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
                    this.capitulos = new String[temporadas][caps];
                    for(int i=0; i<capitulos.length; i++) {
                        aux = new JSONObject(readUrl(BASE + "i=" + id +
                                "&Season=" + (i+1) + "&r=json"));
                        JSONArray listaCaps = aux.getJSONArray("Episodes");
                        for(int j=0; j<capitulos[i].length; j++) {
                            try {
                                capitulos[i][j] = //i+1 + "x" + (j+1) + " " +
                                        listaCaps.getJSONObject(j).getString("Title") + " " +
                                        listaCaps.getJSONObject(j).getString("Released");
                            } catch (JSONException e) {
                                capitulos[i][j] = "N/A";
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
    
    public Map<Integer, String> getCapitulosMes(int ano, int mes) {
        Map<Integer, String> capitulosMes = new HashMap<Integer, String>();
        
        if(this.temporadas == 0) {
            return capitulosMes;
        }
        
        for(int i=0; i<capitulos[temporadas-1].length; i++) {
            if(capitulos[temporadas-1][i] != null) {
                String fecha = capitulos[temporadas-1][i].substring(capitulos[temporadas-1][i].lastIndexOf(' ') + 1);
                if(!fecha.equals("N/A")) {
                    String[] detalle = fecha.split("-");
                    if (Integer.parseInt(detalle[0]) == ano && Integer.parseInt(detalle[1]) == mes && capitulosMes.containsKey(Integer.parseInt(detalle[2]))) {
                        String existing = capitulosMes.get(Integer.parseInt(detalle[2]));
                        capitulosMes.put(Integer.parseInt(detalle[2]), existing + "\n" + capitulos[temporadas-1][i].substring(0, capitulos[temporadas-1][i].lastIndexOf(' ')));
                    } else if(Integer.parseInt(detalle[0]) == ano && Integer.parseInt(detalle[1]) == mes) {
                        capitulosMes.put(Integer.parseInt(detalle[2]), capitulos[temporadas-1][i].substring(0, capitulos[temporadas-1][i].lastIndexOf(' ')));
                    }
                }
            }
        }
        
        return capitulosMes;
    }
    
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

    public void setTitulo(String titulo) {
        this.titulo = titulo;
        this.tituloProperty.setValue(titulo);
    }

    public String[][] getCapitulos() {
        return capitulos;
    }
    
    public StringProperty getTituloProperty() {
        return tituloProperty;
    }

    public void setCapitulos(String[][] capitulos) {
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
    

    
    public static void main(String[] args) {
        Serie arrow = new Serie("house of cards");
        System.out.println(arrow.getCapitulosMes(2016, 03));
        
    }
    
}

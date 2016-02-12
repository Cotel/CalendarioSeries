/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.modelos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private String[][] capitulos;
    private int temporadas;
    private JSONObject json;
    
    public Serie(String titulo) {
        try {
            String toJson = readUrl(BASE + "t=" + titulo.toLowerCase() +
                    "&type=series" + "&plot=short" + "&r=json");
            this.json = new JSONObject(toJson);
            
            this.titulo = json.getString("Title");
            
            boolean next = true;
            int temporada = 1;
            int caps = 0;
            JSONObject aux;
            while(next) {
                aux = new JSONObject(readUrl(BASE + "t=" + this.titulo +
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
            this.capitulos = new String[temporadas][caps];
            for(int i=0; i<capitulos.length; i++) {
                aux = new JSONObject(readUrl(BASE + "t=" + this.titulo +
                        "&Season=" + (i+1) + "&r=json"));
                JSONArray listaCaps = aux.getJSONArray("Episodes");
                for(int j=0; j<capitulos[i].length; j++) {
                    capitulos[i][j] = i+1 + "x" + j+1 + " " + listaCaps.getJSONObject(j).getString("Title");
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
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

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String[][] getCapitulos() {
        return capitulos;
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

    
//    public static void main(String[] args) {
//        Serie arrow = new Serie("Arrow");
//        for(int i=0; i<arrow.getTemporadas(); i++) {
//            for(int j=0; j<arrow.getCapitulos()[i].length; j++) {
//                System.out.println(arrow.getCapitulos()[i][j]);
//            }
//        }
//    }
    
}

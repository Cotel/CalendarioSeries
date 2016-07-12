/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.models;

import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static calendarioSeries.utils.URLReader.readUrl;


/**
 * Modelo SERIE
 *
 * @author Cotel
 */
@Entity
@Table(name = "Serie")
public class Serie implements Model {

    private static final String BASE = "https://www.omdbapi.com/?";

    @Id
    @Column(name = "id", unique = true)
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "seasons")
    private int seasons;

    @Column(name = "poster")
    private String poster;

    public Serie() {
    }

    public Serie(String id) {
        this.id = id;
        try {
            String toJson = readUrl(BASE + "i=" + id +
                    "&type=series" + "&plot=short" + "&r=json");
            JSONObject json = new JSONObject(toJson);

            if (json.getString("Response").equals("True")) {
                this.title = json.getString("Title");
                this.description = json.getString("Plot");
                try {
                    this.seasons = Integer.parseInt(json.getString("totalSeasons"));
                } catch (NumberFormatException e) {
                    this.seasons = 0;
                }
                this.poster = json.getString("Poster");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String toJson() {
        return readUrl(BASE + "i=" + this.id +
                "&type=series" + "&plot=short" + "&r=json");
    }
}

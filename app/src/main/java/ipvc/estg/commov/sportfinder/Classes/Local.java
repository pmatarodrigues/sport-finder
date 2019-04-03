package ipvc.estg.commov.sportfinder.Classes;

import com.google.android.gms.maps.model.LatLng;

public class Local {
    private String id;
    private String nome;
    private LatLng latLng;
    private int raio;

    public Local(String id, String nome, LatLng latLng, int raio) {
        this.id = id;
        this.nome = nome;
        this.latLng = latLng;
        this.raio = raio;
    }

    public Local() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getRaio() {
        return raio;
    }

    public void setRaio(int raio) {
        this.raio = raio;
    }

}

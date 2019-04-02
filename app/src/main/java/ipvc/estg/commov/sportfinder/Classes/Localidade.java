package ipvc.estg.commov.sportfinder.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Localidade {
    private int _ID;
    private String nome;
    private String descricao;
    private String raio;
    private String lat;
    private String lng;
    private ArrayList<Desporto> desportos = null;
    private ArrayList<String> urlFotos = null;
    private String avaliacao;

    public Localidade() {
        this.desportos = new ArrayList<>();
        this.urlFotos = new ArrayList<>();
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRaio() {
        return raio;
    }

    public void setRaio(String raio) {
        this.raio = raio;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public ArrayList<Desporto> getDesportos() {
        return desportos;
    }

    public void setDesportos(ArrayList<Desporto> desportos) {
        this.desportos = desportos;
    }
    public void addDesporto(Desporto desporto){
        if (this.desportos == null){
            this.desportos = new ArrayList<>();
        }
        this.desportos.add(desporto);
    }

    public ArrayList<String> getUrlFotos() {
        return urlFotos;
    }

    public void setUrlFotos(ArrayList<String> urlFotos) {
        this.urlFotos = urlFotos;
    }
    public void addUrl(String url){
        if(this.urlFotos == null){
            this.urlFotos = new ArrayList<>();
        }
        this.urlFotos.add(url);
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

}

package ipvc.estg.commov.sportfinder.Classes;

public class Desporto {
    private String id;
    private String nome;

    public Desporto(String nome) {
        this.nome = nome;
    }
    public Desporto(){

    }

    public Desporto(String id, String nome) {
        this.id = id;
        this.nome = nome;
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
}

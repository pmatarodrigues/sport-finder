package ipvc.estg.commov.sportfinder.Classes;

public class LocalUtilizadorEst {

    private String utilizadorID;
    private String localidadeID;
    private String dataRegisto;
    private String pontos;

    public LocalUtilizadorEst(){
    }

    public LocalUtilizadorEst(String utilizadorID, String localidadeID, String dataRegisto, String pontos) {
        this.utilizadorID = utilizadorID;
        this.localidadeID = localidadeID;
        this.dataRegisto = dataRegisto;
        this.pontos = pontos;
    }

    public String getUtilizadorID() {
        return utilizadorID;
    }

    public void setUtilizadorID(String utilizadorID) {
        this.utilizadorID = utilizadorID;
    }

    public String getLocalidadeID() {
        return localidadeID;
    }

    public void setLocalidadeID(String localidadeID) {
        this.localidadeID = localidadeID;
    }

    public String getDataRegisto() {
        return dataRegisto;
    }

    public void setDataRegisto(String dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

    public String getPontos() {
        return pontos;
    }

    public void setPontos(String pontos) {
        this.pontos = pontos;
    }
}

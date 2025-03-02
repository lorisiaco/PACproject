package bmt.spendly.models;

public class Transazione implements TransazioneIF {
    private String debitore;
    private String creditore;
    private double importo;

    public Transazione(String debitore, String creditore, double importo) {
        this.debitore = debitore;
        this.creditore = creditore;
        this.importo = importo;
    }

    @Override
    public String toString() {
        return debitore + " deve pagare " + importo + " a " + creditore;
    }

    @Override
    public String getDebitore(){
        return this.debitore;
    }

    @Override
    public String getCreditore(){
        return this.creditore;
    }

    @Override
    public double getImporto(){
        return this.importo;
    }

    @Override
    public void setDebitore(String deb){
        this.debitore=deb;
    }

    @Override
    public void setCreditore(String cred){
        this.creditore=cred;
    }

    @Override
    public void setImporto(double imp){
        this.importo=imp;
    }
}

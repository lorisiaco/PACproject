package bmt.spendly.models;

public interface TransazioneIF {
    String getDebitore();
    void setDebitore(String deb);
    String getCreditore();
    void setCreditore(String cred);
    double getImporto();
    void setImporto(double imp);
}

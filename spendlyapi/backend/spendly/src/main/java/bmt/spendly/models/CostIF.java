package bmt.spendly.models;

public interface CostIF {
    Long getCostId();
    void setCostId(Long costId);
    double getImporto();
    void setImporto(double importo);
    ExpenseType getTipologia();
    void setTipologia(ExpenseType tipologia);
    Group getGroup();
    void setGroup(Group group);
    AppUser getUser();
    void setUser(AppUser user);
    PaymentStatus getPaymentStatus();
    void setPaymentStatus(PaymentStatus paymentStatus);
}

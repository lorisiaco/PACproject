package bmt.spendly.models;

public interface AlertIF {
    Long getId();
    double getLimite();
    void setLimite(double limite);
    String getNome();
    void setNome(String nome);
    ExpenseMacroArea getMacroArea();
    void setMacroArea(ExpenseMacroArea macroArea);
    Group getGroup();
    void setGroup(Group group);
}

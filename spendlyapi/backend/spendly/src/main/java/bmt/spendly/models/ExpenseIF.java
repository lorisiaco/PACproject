package bmt.spendly.models;

import java.math.BigDecimal;

public interface ExpenseIF {
    Long getId();
    void setId(Long id);
    String getDescription();
    void setDescription(String description);
    BigDecimal getAmount();
    void setAmount(BigDecimal amount);
    Group getGroup();
    void setGroup(Group group);
    AppUser getCreatedBy();
    void setCreatedBy(AppUser createdBy);
    AppUser getBeneficiary();
    void setBeneficiary(AppUser beneficiary);
}

package bmt.spendly.models;

import java.util.List;

public class GroupResponseDTO {
    private Group group;
    private List<Cost> costs;
    private List<Alert> alerts;

    public GroupResponseDTO(Group group, List<Cost> costs, List<Alert> alerts) {
        this.group = group;
        this.costs = costs;
        this.alerts = alerts;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Cost> getCosts() {
        return costs;
    }

    public void setCosts(List<Cost> costs) {
        this.costs = costs;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }
}

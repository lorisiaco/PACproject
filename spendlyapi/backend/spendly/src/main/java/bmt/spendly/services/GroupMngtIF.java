package bmt.spendly.services;

import java.util.List;

import bmt.spendly.models.Alert;
import bmt.spendly.models.AppUser;
import bmt.spendly.models.ExpenseMacroArea;
import bmt.spendly.models.Group;

public interface GroupMngtIF {
    Group creaGruppo(String nome, String username);
    List<Group> getAllGroups();
    List<Group> getAllGroupsForUser(String username);
    Group getGroupById(Long id);
    void eliminaGruppo(Long groupId);
    Group aggiungiMembro(Long groupId, String username);
    Group rimuoviMembro(Long groupId, String username);
    List<AppUser> getMembri(Long groupId);
    AppUser getUserByUsername(String username);
    Alert creaAlert(String nome, double importo, ExpenseMacroArea macroArea, Long groupId);
    void EliminaAlert(Long alertId);
    List<Alert> getAlertsForGroup(Long groupId);
}

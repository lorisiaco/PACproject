<template>
  <div class="container mt-5">
    <h1 class="mb-4">
      <i class="fas fa-users"></i> Gestione Gruppi
    </h1>

    <!-- Statistiche -->
    <div class="row">
      <div class="col-md-4">
        <div class="dashboard-card bg-primary text-white p-3 rounded">
          <h3><i class="fas fa-users"></i> Numero Gruppi</h3>
          <p>{{ totalGroups }}</p>
        </div>
      </div>
      <div class="col-md-4">
        <div class="dashboard-card bg-success text-white p-3 rounded">
          <h3><i class="fas fa-user-friends"></i> Membri Medi</h3>
          <p>{{ averageMembers }}</p>
        </div>
      </div>
      <div class="col-md-4">
        <div class="dashboard-card bg-warning text-dark p-3 rounded">
          <h3><i class="fas fa-plus"></i> Ultimo Gruppo</h3>
          <p>{{ lastGroup }}</p>
        </div>
      </div>
    </div>

    <!-- Tabella Gruppi -->
    <h2 class="mt-4">Elenco Gruppi</h2>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>ID</th>
          <th>Nome</th>
          <th>Admin</th>
          <!-- Colonna Membri (solo il numero, cliccabile) -->
          <th>Membri</th>
          <th>Aggiungi Membro</th>
          <th>Azioni</th>
        </tr>
      </thead>
      <tbody>
        <!-- Se non ci sono gruppi -->
        <tr v-if="groups.length === 0">
          <td colspan="6" class="text-center">Nessun gruppo registrato</td>
        </tr>
        <!-- Elenco gruppi -->
        <tr v-for="group in groups" :key="group.id">
          <td>{{ group.id }}</td>
          <td>{{ group.nome }}</td>
          <td>{{ group.admin ? group.admin.username : 'N/D' }}</td>

          <!-- MOSTRA SOLO IL NUMERO DI MEMBRI E CLICCANDO CI PORTA AL DETTAGLIO -->
          <td>
            <router-link
              :to="{ name: 'GroupDetail', params: { groupId: group.id } }"
              class="text-decoration-underline"
            >
              {{ group.membri ? group.membri.length : 0 }}
            </router-link>
          </td>

          <!-- Seleziona un utente dal menu a tendina per aggiungerlo come membro -->
          <td>
            <div class="d-flex align-items-center">
              <select
                class="form-select"
                v-model="selectedMember[group.id]"
              >
                <option value="" disabled>Seleziona utente</option>
                <option
                  v-for="user in allUsers"
                  :key="user.id"
                  :value="user.username"
                >
                  {{ user.username }}
                </option>
              </select>
              <button
                class="btn btn-primary ms-2"
                @click="addMemberToGroup(group.id, selectedMember[group.id])"
              >
                Aggiungi
              </button>
            </div>
          </td>

          <!-- Azioni (Elimina Gruppo) -->
          <td>
            <button
              class="btn btn-danger btn-sm"
              @click="deleteGroup(group.id)"
            >
              Elimina
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Bottone per Aggiungere Gruppo -->
    <button class="btn btn-primary" @click="showModal = true">
      <i class="fas fa-plus"></i> Aggiungi Gruppo
    </button>

    <!-- Modale per Aggiungere Gruppo -->
    <div v-if="showModal" class="modal fade show" tabindex="-1" style="display: block;">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Aggiungi Gruppo</h5>
            <button type="button" class="btn-close" @click="showModal = false"></button>
          </div>
          <div class="modal-body">
            <input
              v-model="newGroup.nome"
              type="text"
              class="form-control mb-2"
              placeholder="Nome del gruppo"
              required
            />
          </div>
          <div class="modal-footer">
            <button class="btn btn-success" @click="addGroup">
              Salva
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

/** Lista reattiva dei gruppi */
const groups = ref([]);

/** Statistiche */
const totalGroups = ref(0);
const averageMembers = ref(0);
const lastGroup = ref("N/D");

/** Stato della modale e nuovo gruppo da creare */
const showModal = ref(false);
const newGroup = ref({ nome: "" });

/** 
 * Array di tutti gli utenti che arrivano dal DB 
 * (tramite /account/users o /api/users).
 */
const allUsers = ref([]);

/** 
 * Mappa reattiva groupId -> username 
 * (l'utente selezionato per l'aggiunta come membro) 
 */
const selectedMember = ref({});

/**
 * Al montaggio del componente, recupera i gruppi e gli utenti
 */
onMounted(() => {
  fetchGroups();
  fetchAllUsers();
});

/**
 * Carica la lista dei gruppi
 */
async function fetchGroups() {
  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");

  if (!username || !token) {
    console.error("Utente non autenticato! Username o token mancante.");
    return;
  }

  try {
    const response = await fetch(
      `http://localhost:8080/api/groups?username=${username}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );

    if (!response.ok) {
      throw new Error("Errore nel recupero dei gruppi");
    }

    const data = await response.json();
    groups.value = data;

    // Inizializza un campo selectedMember[group.id] = "" per ogni gruppo
    data.forEach((g) => {
      if (!selectedMember.value[g.id]) {
        selectedMember.value[g.id] = "";
      }
    });

    updateDashboard();
  } catch (error) {
    console.error("Errore fetchGroups:", error);
  }
}

/**
 * Carica la lista di TUTTI gli utenti dal DB 
 */
async function fetchAllUsers() {
  const token = localStorage.getItem("token");
  if (!token) {
    console.error("Token mancante. Non posso caricare gli utenti.");
    return;
  }

  try {
    const res = await fetch("http://localhost:8080/account/users", {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!res.ok) {
      throw new Error("Errore nel recupero degli utenti");
    }

    const data = await res.json();
    allUsers.value = data;
  } catch (err) {
    console.error("Errore fetchAllUsers:", err);
  }
}

/**
 * Aggiunge un nuovo gruppo
 */
async function addGroup() {
  if (!newGroup.value.nome) {
    alert("Compila il nome del gruppo!");
    return;
  }

  const token = localStorage.getItem("token");
  const username = localStorage.getItem("username");

  if (!token || !username) {
    alert("Utente non autenticato.");
    return;
  }

  try {
    const response = await fetch(
      `http://localhost:8080/api/groups?username=${username}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ nome: newGroup.value.nome }),
      }
    );

    if (response.ok) {
      // Ricarica i gruppi e resetta il form
      await fetchGroups();
      newGroup.value.nome = "";
      showModal.value = false;
    } else {
      const errorData = await response.json();
      alert(`Errore: ${errorData.message}`);
    }
  } catch (error) {
    console.error("Errore nella creazione del gruppo:", error);
  }
}

/**
 * Elimina un gruppo (solo se l'utente loggato è admin di quel gruppo)
 */
async function deleteGroup(groupId) {
  const token = localStorage.getItem("token");
  const username = localStorage.getItem("username");

  if (!token || !username) {
    alert("Utente non autenticato.");
    return;
  }

  try {
    const response = await fetch(
      `http://localhost:8080/api/groups/${groupId}?username=${username}`,
      {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (response.ok) {
      fetchGroups();
    } else {
      const errorMsg = await response.text();
      alert(`Errore nella cancellazione del gruppo: ${errorMsg}`);
    }
  } catch (error) {
    console.error("Errore nella cancellazione del gruppo:", error);
  }
}

/**
 * Aggiunge un membro al gruppo
 */
async function addMemberToGroup(groupId, memberUsername) {
  const adminUsername = localStorage.getItem("username");
  const token = localStorage.getItem("token");

  if (!adminUsername || !token) {
    alert("Utente non autenticato o token mancante.");
    return;
  }

  if (!memberUsername) {
    alert("Seleziona l'utente dal menu a tendina.");
    return;
  }

  try {
    const response = await fetch(
      `http://localhost:8080/api/groups/${groupId}/members?adminUsername=${adminUsername}&memberUsername=${memberUsername}`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText);
    }

    // Ricarichiamo i gruppi per vedere il membro aggiunto
    await fetchGroups();

    // Resettiamo la select
    selectedMember.value[groupId] = "";
  } catch (error) {
    alert(`Errore aggiunta membro: ${error}`);
    console.error("Errore durante l'aggiunta del membro:", error);
  }
}

/**
 * Aggiorna le statistiche (numero gruppi, membri medi, ultimo gruppo)
 */
function updateDashboard() {
  totalGroups.value = groups.value.length;

  if (groups.value.length > 0) {
    const totalMembers = groups.value.reduce((sum, g) => {
      return sum + (g.membri ? g.membri.length : 0);
    }, 0);
    averageMembers.value = (totalMembers / groups.value.length).toFixed(2);

    const last = groups.value[groups.value.length - 1];
    lastGroup.value = last.nome;
  } else {
    averageMembers.value = 0;
    lastGroup.value = "N/D";
  }
}
</script>

<style scoped>
.container {
  max-width: 800px;
}
/* Sovrascrive sfondo quando appare la modale "show" */
.modal {
  background: rgba(0, 0, 0, 0.5);
}
.dashboard-card {
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

</style>

<template>
  <div class="container mt-5">
    <h1 class="mb-4">
      <i class="fas fa-receipt"></i> Gestione Spese
    </h1>

    <!-- Statistiche -->
    <div class="row">
      <div class="col-md-4">
        <div class="dashboard-card bg-primary text-white p-3 rounded">
          <h3><i class="fas fa-euro-sign"></i> Totale Spese</h3>
          <p>€{{ totalSpent }}</p>
        </div>
      </div>
      <div class="col-md-4">
        <div class="dashboard-card bg-success text-white p-3 rounded">
          <h3><i class="fas fa-coins"></i> Spesa Media</h3>
          <p>€{{ averageSpent }}</p>
        </div>
      </div>
      <div class="col-md-4">
        <div class="dashboard-card bg-warning text-dark p-3 rounded">
          <h3><i class="fas fa-history"></i> Ultima Spesa</h3>
          <p>{{ lastSpent }}</p>
        </div>
      </div>
    </div>

    <!-- Tabella Spese -->
    <h2 class="mt-4">Storico Spese</h2>
    <table class="table table-striped">
      <thead>
        <tr>
          <!-- Rimosso <th>Data</th> -->
          <th>Tipologia</th>
          <th>Importo (€)</th>
          <th>Gruppo</th>
          <th>Azioni</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="costs.length === 0">
          <td colspan="4" class="text-center">Nessuna spesa registrata</td>
        </tr>
        <tr v-for="cost in costs" :key="cost.id">
          <!-- Rimosso la colonna 'Data' -->
          <td>{{ cost.tipologia.replace('_', ' ') }}</td>
          <td>€{{ cost.importo ? cost.importo.toFixed(2) : "0.00" }}</td>
          <td>{{ cost.group ? cost.group.nome : 'N/A' }}</td>
          <td>
            <button class="btn btn-danger btn-sm" @click="deleteCost(cost.id)">
              Elimina
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Bottone per Aggiungere Spesa -->
    <button class="btn btn-primary" @click="showModal = true">
      <i class="fas fa-plus"></i> Aggiungi Spesa
    </button>

    <!-- Modale per Aggiungere Spesa -->
    <div v-if="showModal" class="modal fade show" tabindex="-1" style="display: block;">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Aggiungi Spesa</h5>
            <button type="button" class="btn-close" @click="showModal = false"></button>
          </div>
          <div class="modal-body">
            <!-- Tipologia -->
            <select v-model="newCost.tipologia" class="form-control mb-2" required>
              <option value="" disabled>Seleziona la tipologia</option>
              <option v-for="type in expenseTypes" :key="type" :value="type">
                {{ type.replace('_', ' ') }}
              </option>
            </select>

            <!-- Importo -->
            <input
              v-model.number="newCost.importo"
              type="number"
              class="form-control mb-2"
              placeholder="Importo (€)"
              required
            />

            <!-- Selezione Gruppo -->
            <select v-model="newCost.groupId" class="form-control mb-2">
              <option value="">Nessun gruppo</option>
              <option v-for="group in groups" :key="group.id" :value="group.id">
                {{ group.nome }}
              </option>
            </select>
          </div>
          <div class="modal-footer">
            <button class="btn btn-success" @click="addCost">Salva</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

/** Lista delle spese caricate */
const costs = ref([]);

/** Lista dei gruppi a cui l'utente partecipa */
const groups = ref([]);

/** Variabili per le statistiche */
const totalSpent = ref(0);
const averageSpent = ref(0);
const lastSpent = ref("N/D");

/** Gestione modale */
const showModal = ref(false);

/** Nuova spesa da creare */
const newCost = ref({
  tipologia: "",
  importo: null,
  groupId: null, // Salviamo solo l'ID del gruppo
});

/** Possibili tipologie di spesa */
const expenseTypes = [
  "ABITAZIONE_AFFITTO", "ABITAZIONE_MUTUO", "ABITAZIONE_BOLLETTE", "ALIMENTARI",
  "TRASPORTI_CARBURANTE", "TRASPORTI_PUBBLICO", "TRASPORTI_MANUTENZIONE", "TRASPORTI_ASSICURAZIONE",
  "SALUTE_FARMACI", "SALUTE_VISITE", "SALUTE_ASSICURAZIONE", "ISTRUZIONE_TASSE",
  "ISTRUZIONE_MATERIALI", "ISTRUZIONE_CORSI", "ASSICURAZIONI_AUTO", "ASSICURAZIONI_CASA",
  "ASSICURAZIONI_VITA", "TASSE_PROPRIETA", "SVAGO_CINEMA", "SVAGO_TEATRO", "SVAGO_CONCERTI",
  "SVAGO_HOBBY", "VIAGGI_BIGLIETTI", "VIAGGI_HOTEL", "VIAGGI_ESCURSIONI", "RISTORANTI_PRANZI",
  "RISTORANTI_CENE", "RISTORANTI_CAFFE", "SHOPPING_ABBIGLIAMENTO", "SHOPPING_ACCESSORI",
  "SHOPPING_SCARPE", "SHOPPING_COSMETICI", "TECNOLOGIA_SMARTPHONE", "TECNOLOGIA_TABLET",
  "TECNOLOGIA_COMPUTER", "TECNOLOGIA_ABBONAMENTI",
];

/**
 * Carica la lista spese dal backend
 */
const fetchCosts = async () => {
  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");

  if (!username || !token) {
    console.error("Utente non autenticato! Username o token mancante.");
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/api/costs?username=${username}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) {
      throw new Error("Errore nel recupero delle spese");
    }

    const data = await response.json();
    costs.value = data;
    updateDashboard();
  } catch (error) {
    console.error(error);
  }
};

/**
 * Carica i gruppi a cui l'utente partecipa
 */
const fetchGroups = async () => {
  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");

  if (!username || !token) {
    console.error("Utente non autenticato! Username o token mancante.");
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/api/groups?username=${username}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) {
      throw new Error("Errore nel recupero dei gruppi");
    }

    const data = await response.json();
    groups.value = data;
  } catch (error) {
    console.error(error);
  }
};

/**
 * Aggiunge una nuova spesa
 */
const addCost = async () => {
  // Verifica campi obbligatori
  if (!newCost.value.tipologia || !newCost.value.importo) {
    alert("Compila tutti i campi obbligatori!");
    return;
  }

  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");
  if (!username || !token) {
    alert("Utente non autenticato.");
    return;
  }

  // Prepara il payload
  const payload = {
    importo: newCost.value.importo,
    tipologia: newCost.value.tipologia,
    // Se l'utente ha selezionato un gruppo, passiamo un oggetto group con { id: ... }
    group: newCost.value.groupId
      ? { id: parseInt(newCost.value.groupId) }
      : null,
  };

  try {
    const response = await fetch(`http://localhost:8080/api/costs?username=${username}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(payload),
    });

    if (response.ok) {
      // Ricarica la lista spese
      await fetchCosts();
      // Chiudi la modale e ripulisci il form
      showModal.value = false;
      newCost.value = { tipologia: "", importo: null, groupId: null };
    } else {
      const errorData = await response.json();
      alert(`Errore: ${errorData.message || "Impossibile aggiungere la spesa."}`);
    }
  } catch (error) {
    console.error("Errore nell'aggiunta della spesa:", error);
  }
};

/**
 * Elimina una spesa
 */
const deleteCost = async (id) => {
  const token = localStorage.getItem("token");
  if (!token) {
    alert("Utente non autenticato.");
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/api/costs/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.ok) {
      // Aggiorna la lista
      await fetchCosts();
    } else {
      alert("Errore nella cancellazione della spesa.");
    }
  } catch (error) {
    console.error("Errore nella cancellazione della spesa:", error);
  }
};

/**
 * Aggiorna le statistiche (totale, media, ultima spesa)
 */
const updateDashboard = () => {
  if (costs.value.length === 0) {
    totalSpent.value = 0;
    averageSpent.value = 0;
    lastSpent.value = "N/D";
    return;
  }

  const total = costs.value.reduce((sum, c) => sum + c.importo, 0);
  totalSpent.value = total.toFixed(2);
  averageSpent.value = (total / costs.value.length).toFixed(2);
  lastSpent.value = `€${costs.value[costs.value.length - 1].importo.toFixed(2)}`;
};

/**
 * Al montaggio del componente, recupera spese e gruppi
 */
onMounted(() => {
  fetchCosts();
  fetchGroups();
});
</script>

<style scoped>
.container {
  max-width: 800px;
}

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

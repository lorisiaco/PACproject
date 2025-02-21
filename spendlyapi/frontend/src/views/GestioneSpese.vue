<template>
  <div class="container mt-5">
    <h1 class="mb-4">
      <i class="fas fa-receipt"></i> Gestione Spese
    </h1>

    <!-- Statistiche -->
    <div class="row">
      <div class="col-md-4 mb-3">
        <div class="dashboard-card bg-primary text-white p-3 rounded">
          <h3 class="h5"><i class="fas fa-euro-sign"></i> Totale Spese</h3>
          <p class="fs-5 m-0">€{{ totalSpent }}</p>
        </div>
      </div>
      <div class="col-md-4 mb-3">
        <div class="dashboard-card bg-success text-white p-3 rounded">
          <h3 class="h5"><i class="fas fa-coins"></i> Spesa Media</h3>
          <p class="fs-5 m-0">€{{ averageSpent }}</p>
        </div>
      </div>
      <div class="col-md-4 mb-3">
        <div class="dashboard-card bg-warning text-dark p-3 rounded">
          <h3 class="h5"><i class="fas fa-history"></i> Ultima Spesa</h3>
          <p class="fs-5 m-0">{{ lastSpent }}</p>
        </div>
      </div>
    </div>
      <!-- Statistiche (3 card pastello) -->
      <div class="row">
        <!-- Card Totale Spese (blu pastello) -->
        <div class="col-md-4">
          <div class="dashboard-card card-totale-spese p-3 rounded text-center">
            <h3><i class="fas fa-euro-sign"></i> Totale Spese</h3>
            <p>€{{ totalSpent }}</p>
          </div>
        </div>
        <!-- Card Spesa Media (verde pastello) -->
        <div class="col-md-4">
          <div class="dashboard-card card-spesa-media p-3 rounded text-center">
            <h3><i class="fas fa-coins"></i> Spesa Media</h3>
            <p>€{{ averageSpent }}</p>
          </div>
        </div>
        <!-- Card Ultima Spesa (giallo pastello) -->
        <div class="col-md-4">
          <div class="dashboard-card card-ultima-spesa p-3 rounded text-center">
            <h3><i class="fas fa-history"></i> Ultima Spesa</h3>
            <p>{{ lastSpent }}</p>
          </div>
        </div>
      </div>

    <!-- Tabella Spese -->
    <h2 class="mt-4 mb-3">Storico Spese</h2>
    <table class="table table-bordered table-hover table-striped align-middle">
      <thead class="table-dark">
        <tr class="text-center">
          <th scope="col">Tipologia</th>
          <th scope="col">Importo (€)</th>
          <th scope="col">Gruppo</th>
          <th scope="col">Stato Pagamento</th>
          <th scope="col">Azioni</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="costs.length === 0">
          <td colspan="5" class="text-center">Nessuna spesa registrata</td>
        </tr>
        <!-- Usare cost.costId come chiave -->
        <tr v-for="cost in costs" :key="cost.costId" class="text-center">
          <td>{{ cost.tipologia.replaceAll('_', ' ') }}</td>
          <td>€{{ cost.importo ? cost.importo.toFixed(2) : '0.00' }}</td>
          <td>{{ cost.group ? cost.group.nome : 'N/A' }}</td>
          <td>
            <!-- Badge colorato in base allo stato -->
            <span 
              :class="{
                'badge': true,
                'bg-success': cost.paymentStatus === 'PAGATO',
                'bg-danger': cost.paymentStatus === 'DA_SALDARE'
              }"
            >
              {{ cost.paymentStatus === 'PAGATO' ? 'Pagato' : 'Da saldare' }}
            </span>
          </td>
          <td>
            <!-- Pulsante per SALDARE o RIPRISTINARE lo stato di pagamento -->
            <button
              class="btn btn-sm me-2"
              :class="cost.paymentStatus === 'PAGATO' ? 'btn-warning' : 'btn-success'"
              @click="togglePaymentStatus(cost)"
            >
              {{ cost.paymentStatus === 'PAGATO' ? 'Segna come Da Saldare' : 'Segna come Pagato' }}
            </button>

            <!-- Pulsante per Eliminare la spesa -->
            <button class="btn btn-danger btn-sm" @click="deleteCost(cost.costId)">
              Elimina
            </button>
          </td>
        </tr>
      </tbody>
    </table>
      <!-- Tabella Spese -->
      <h2 class="mt-4">Storico Spese</h2>
      <table class="table custom-table">
        <thead>
          <tr>
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
            <td>{{ cost.tipologia.replace('_', ' ') }}</td>
            <td>€{{ cost.importo ? cost.importo.toFixed(2) : "0.00" }}</td>
            <td>{{ cost.group ? cost.group.nome : 'Personale' }}</td>
            <td>
              <!-- Pulsante Modifica -->
              <button class="btn btn-warning btn-sm me-2" @click="openEditModal(cost)">
                Modifica
              </button>
              <!-- Pulsante Elimina -->
              <button class="btn btn-danger btn-sm" @click="deleteCost(cost.id)">
                Elimina
              </button>
            </td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <td colspan="1"><strong>Totale Spese</strong></td>
            <td>€{{ totalSpent }}</td>
            <td colspan="2"></td>
          </tr>
        </tfoot>
      </table>

      <!-- Bottone per Aggiungere Spesa -->
      <div class="mb-5">
      <button class="btn btn-primary" @click="showModal = true">
          <i class="fas fa-plus"></i> Aggiungi Spesa
        </button>
    </div>

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
            <div class="mb-3">
              <label class="form-label">Tipologia</label>
              <select v-model="newCost.tipologia" class="form-select" required>
                <option value="" disabled>Seleziona la tipologia</option>
                <option v-for="type in expenseTypes" :key="type" :value="type">
                  {{ type.replaceAll('_', ' ') }}
                </option>
              </select>
            </div>
      <!-- Modale per Aggiungere Spesa -->
      <div
        v-if="showModal"
        class="modal fade show"
        tabindex="-1"
        style="display: block; background: rgba(0, 0, 0, 0.5);"
      >
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
              <div class="mb-3">
              <label class="form-label">Importo (€)</label>
              <input
                  v-model.number="newCost.importo"
                  type="number"
                  class="form-control"
                  placeholder="Importo (€)"
                  required
                />
            </div>

              <!-- Selezione Gruppo -->
              <div class="mb-3">
              <label class="form-label">Gruppo</label>
              <select v-model="newCost.groupId" class="form-select">
                  <option value="">Nessun gruppo</option>
                  <option v-for="group in groups" :key="group.id" :value="group.id">
                    {{ group.nome }}
                  </option>
                </select>
              </div>

            <!-- Stato Pagamento (opzionale) -->
            <div class="mb-3">
              <label class="form-label">Stato Pagamento</label>
              <select v-model="newCost.paymentStatus" class="form-select">
                <option value="DA_SALDARE">Da saldare</option>
                <option value="PAGATO">Pagato</option>
              </select>
            </div>
          </div>
            <div class="modal-footer">
              <button class="btn btn-secondary" @click="showModal = false">
              Annulla
            </button>
            <button class="btn btn-success" @click="addCost">
              Salva
            </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Modale per Modificare Spesa -->
      <div
        v-if="showEditModal"
        class="modal fade show"
        tabindex="-1"
        style="display: block; background: rgba(0, 0, 0, 0.5);"
      >
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Modifica Spesa</h5>
              <button type="button" class="btn-close" @click="closeEditModal"></button>
            </div>
            <div class="modal-body">
              <!-- Tipologia -->
              <select v-model="editCost.tipologia" class="form-control mb-2" required>
                <option value="" disabled>Seleziona la tipologia</option>
                <option v-for="type in expenseTypes" :key="type" :value="type">
                  {{ type.replace('_', ' ') }}
                </option>
              </select>

              <!-- Importo -->
              <input
                v-model.number="editCost.importo"
                type="number"
                class="form-control mb-2"
                placeholder="Importo (€)"
                required
              />

              <!-- Selezione Gruppo -->
              <select v-model="editCost.groupId" class="form-control mb-2">
                <option value="">Nessun gruppo</option>
                <option v-for="group in groups" :key="group.id" :value="group.id">
                  {{ group.nome }}
                </option>
              </select>
            </div>
            <div class="modal-footer">
              <button class="btn btn-success" @click="updateCost">Salva Modifiche</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- FOOTER -->
    <footer class="footer bg-dark text-white text-center py-3 mt-auto">
      <p class="mb-0">
        &copy; 2025 Spendly. Tutti i diritti riservati.
        <router-link to="/contact" class="footer-link">Contattaci</router-link>
      </p>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

/** Lista delle spese caricate */
const costs = ref([]);
/** Lista dei gruppi */
const groups = ref([]);

/** Variabili per le statistiche */
const totalSpent = ref(0);
const averageSpent = ref(0);
const lastSpent = ref("N/D");

/** Stato modale Aggiungi Spesa di aggiunta spesa */
const showModal = ref(false);

/** Nuova spesa da creare */
const newCost = ref({
  tipologia: "",
  importo: null,
  groupId: null,
  paymentStatus: "DA_SALDARE",
});

/** Stato modale Modifica Spesa */
const showEditModal = ref(false);
const editCost = ref({
  id: null,
  tipologia: "",
  importo: null,
  groupId: null,
});

/** Tipologie di spesa disponibili (il tuo enum) */
const expenseTypes = [
  "ABITAZIONE_AFFITTO","ABITAZIONE_MUTUO","ABITAZIONE_BOLLETTE","ALIMENTARI",
  "TRASPORTI_CARBURANTE","TRASPORTI_PUBBLICO","TRASPORTI_MANUTENZIONE","TRASPORTI_ASSICURAZIONE",
  "SALUTE_FARMACI","SALUTE_VISITE","SALUTE_ASSICURAZIONE","ISTRUZIONE_TASSE",
  "ISTRUZIONE_MATERIALI","ISTRUZIONE_CORSI","ASSICURAZIONI_AUTO","ASSICURAZIONI_CASA",
  "ASSICURAZIONI_VITA","TASSE_PROPRIETA","SVAGO_CINEMA","SVAGO_TEATRO","SVAGO_CONCERTI",
  "SVAGO_HOBBY","VIAGGI_BIGLIETTI","VIAGGI_HOTEL","VIAGGI_ESCURSIONI","RISTORANTI_PRANZI",
  "RISTORANTI_CENE","RISTORANTI_CAFFE","SHOPPING_ABBIGLIAMENTO","SHOPPING_ACCESSORI",
  "SHOPPING_SCARPE","SHOPPING_COSMETICI","TECNOLOGIA_SMARTPHONE","TECNOLOGIA_TABLET",
  "TECNOLOGIA_COMPUTER","TECNOLOGIA_ABBONAMENTI",
];

/** Al montaggio del componente, recupera spese e gruppi dal backend */
onMounted(() => {
  fetchCosts();
  fetchGroups();
});

/** Funzione per recuperare l'elenco delle spese */
async function fetchCosts() {
/** Carica le spese dal backend */
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
}

/** Funzione per recuperare i gruppi */
async function fetchGroups() {
};

/** Carica i gruppi */
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
}

/** Aggiunge una nuova spesa */
async function addCost() {
/** Aggiunge una nuova spesa */
const addCost = async () => {
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
    paymentStatus: newCost.value.paymentStatus,
    group: newCost.value.groupId && newCost.value.groupId !== "undefined"
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
      // Ricarichiamo la lista delle spese
      await fetchCosts();
      // Chiudiamo la modale e resettiamo il form
      await fetchCosts();
      showModal.value = false;
      newCost.value = { tipologia: "", importo: null, groupId: null, paymentStatus: "DA_SALDARE" };
    } else {
      const errorData = await response.json();
      alert(`Errore: ${errorData.message || "Impossibile aggiungere la spesa."}`);
    }
  } catch (error) {
    console.error("Errore nell'aggiunta della spesa:", error);
  }
}

/** Elimina una spesa */
async function deleteCost(costId) {
/** Elimina una spesa */
const deleteCost = async (id) => {
  const token = localStorage.getItem("token");
  if (!token) {
    alert("Utente non autenticato.");
    return;
  }
  try {
    const response = await fetch(`http://localhost:8080/api/costs/${costId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    if (response.ok) {
      await fetchCosts();
    } else {
      alert("Errore nella cancellazione della spesa.");
    }
  } catch (error) {
    console.error("Errore nella cancellazione della spesa:", error);
  }
}

/** Quando l'utente clicca "Segna come Pagato"/"Segna come Da Saldare" */
async function togglePaymentStatus(cost) {
  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");
  if (!username || !token) {
    alert("Utente non autenticato.");
    return;
  }

  // Se la spesa è DA_SALDARE e l'utente clicca "Segna come Pagato", usiamo l'endpoint /pay
  if (cost.paymentStatus === "DA_SALDARE") {
    try {
      const response = await fetch(
        `http://localhost:8080/api/costs/${cost.costId}/pay?username=${username}`,
        {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      if (!response.ok) {
        const errorText = await response.text();
        alert("Errore nel pagamento: " + errorText);
      } else {
        // Ricarichiamo la lista
        await fetchCosts();
      }
    } catch (error) {
      console.error("Errore nel pagamento:", error);
      alert("Errore nel pagamento.");
    }
  } 
  // Se la spesa era PAGATO e l'utente vuole "Segna come Da Saldare", 
  // facciamo una PUT generica su /api/costs/{costId}
  else {
    cost.paymentStatus = "DA_SALDARE";
    try {
      const response = await fetch(`http://localhost:8080/api/costs/${cost.costId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(cost),
      });
      if (!response.ok) {
        alert("Errore nel riportare la spesa a Da Saldare.");
      } else {
        updateDashboard();
      }
    } catch (error) {
      console.error("Errore nell'aggiornamento dello stato di pagamento:", error);
    }
  }
}

/** Calcola statistiche (totale, media, ultima) */
function updateDashboard() {
};

/** Apre la modale di modifica con i dati della spesa */
const openEditModal = (cost) => {
  editCost.value.id = cost.id;
  editCost.value.tipologia = cost.tipologia;
  editCost.value.importo = cost.importo;
  editCost.value.groupId = cost.group ? cost.group.id : "";
  showEditModal.value = true;
};

/** Chiude la modale di modifica */
const closeEditModal = () => {
  showEditModal.value = false;
};

/** Salva le modifiche della spesa (PUT) */
const updateCost = async () => {
  if (!editCost.value.tipologia || !editCost.value.importo) {
    alert("Compila tutti i campi obbligatori!");
    return;
  }
  const token = localStorage.getItem("token");
  if (!token) {
    alert("Utente non autenticato.");
    return;
  }
  const payload = {
    tipologia: editCost.value.tipologia,
    importo: editCost.value.importo,
    group: editCost.value.groupId && editCost.value.groupId !== "undefined"
           ? { id: parseInt(editCost.value.groupId) }
           : null,
  };
  try {
    const response = await fetch(`http://localhost:8080/api/costs/${editCost.value.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(payload),
    });
    if (response.ok) {
      await fetchCosts();
      showEditModal.value = false;
    } else {
      const errorData = await response.json();
      alert(`Errore: ${errorData.message || "Impossibile modificare la spesa."}`);
    }
  } catch (error) {
    console.error("Errore nella modifica della spesa:", error);
  }
};

/** Aggiorna le statistiche: totale, media e ultima spesa */
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
}
};

/** Al montaggio, carica spese e gruppi */
onMounted(() => {
  fetchCosts();
  fetchGroups();
});
</script>

<style scoped>
.container {
  max-width: 900px;
  max-width: 900px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  padding: 2rem;
  margin-bottom: 1rem;
}

/* Sfondo scuro dietro la modale */
.modal {
  background: rgba(0, 0, 0, 0.5);
}

/* Card delle statistiche */
/* Card statistiche (pastello) */
.dashboard-card {
  min-height: 110px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.table > :not(caption) > * > * {
  vertical-align: middle; /* allinea il testo verticalmente */
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.dashboard-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  cursor: pointer;
}

/* Card Totale Spese (blu pastello) */
.card-totale-spese {
  background-color: #cce5ff;
  border: 1px solid #b8daff;
  color: #004085;
}
/* Card Spesa Media (verde pastello) */
.card-spesa-media {
  background-color: #d4edda;
  border: 1px solid #c3e6cb;
  color: #155724;
}
/* Card Ultima Spesa (giallo pastello) */
.card-ultima-spesa {
  background-color: #fff3cd;
  border: 1px solid #ffeeba;
  color: #856404;
}

/* Tabella custom */
table.custom-table {
  border-collapse: separate;
  border-spacing: 0 0.5rem;
  font-weight: 500;
}
table.custom-table thead {
  background: linear-gradient(to right, #007bff, #0056b3);
  color: #fff;
}
table.custom-table thead th {
  padding: 1rem;
  text-align: left;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}
table.custom-table tbody tr {
  background: linear-gradient(90deg, #E1F5FE, #B3E5FC);
  border-radius: 8px;
  margin-bottom: 0.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, background 0.3s ease;
  cursor: pointer;
}
table.custom-table tbody tr:hover {
  transform: translateX(5px);
  background: linear-gradient(90deg, #B3E5FC, #81D4FA);
}
table.custom-table td {
  padding: 1rem;
  text-align: left;
  border-top: none !important;
}
table.custom-table tfoot td {
  padding: 1rem;
}
</style>
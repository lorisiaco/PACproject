<template>
  <div class="dashboard-wrapper d-flex flex-column min-vh-100">
    <div class="container mt-5">
      <h1 class="mb-4">
        <i class="fas fa-receipt"></i> Gestione Spese
      </h1>

      <!-- Statistiche -->
      <div class="row">
        <div class="col-md-4 mb-3">
          <!-- Card Numero Gruppi (BLU) -->
          <div class="dashboard-card card-gruppi-attivi p-3 rounded">
            <h3 class="h5"><i class="fas fa-euro-sign"></i> Totale Spese</h3>
            <p class="fs-5 m-0">€{{ totalSpent }}</p>
          </div>
        </div>
        <div class="col-md-4 mb-3">
          <!-- Card Membri Medi (VERDE) -->
          <div class="dashboard-card card-ultima-spesa p-3 rounded">
            <h3 class="h5"><i class="fas fa-coins"></i> Spesa Media</h3>
            <p class="fs-5 m-0">€{{ averageSpent }}</p>
          </div>
        </div>
        <div class="col-md-4 mb-3">
          <!-- Card Ultimo Gruppo (GIALLO) -->
          <div class="dashboard-card card-totale-spese p-3 rounded">
            <h3 class="h5"><i class="fas fa-history"></i> Ultima Spesa</h3>
            <p class="fs-5 m-0">{{ lastSpent }}</p>
          </div>
        </div>
      </div>

      <!-- Tabella Spese in stile card -->
      <h2 class="mt-4 mb-3">Storico Spese</h2>
      <div class="table-card mb-4">
        <table class="table table-hover align-middle mb-0">
          <thead>
            <tr class="text-center">
              <th>Tipologia</th>
              <th>Importo (€)</th>
              <th>Gruppo</th>
              <th>Stato Pagamento</th>
              <th>Azioni</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="costs.length === 0">
              <td colspan="5" class="text-center">Nessuna spesa registrata</td>
            </tr>
            <tr
              v-for="cost in costs"
              :key="cost.costId"
              class="table-row text-center"
            >
              <td>{{ cost.tipologia.replaceAll('_', ' ') }}</td>
              <td>€{{ cost.importo ? cost.importo.toFixed(2) : '0.00' }}</td>
              <td>{{ cost.group ? cost.group.nome : 'Personale' }}</td>
              <td>
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
                <button
                  class="btn btn-sm me-2"
                  :class="cost.paymentStatus === 'PAGATO' ? 'btn-warning' : 'btn-success'"
                  @click="togglePaymentStatus(cost)"
                >
                  {{ cost.paymentStatus === 'PAGATO' ? 'Segna come Da Saldare' : 'Segna come Pagato' }}
                </button>
                <!-- Nuovo tasto Modifica -->
                <button class="btn btn-info btn-sm me-2" @click="openEditModal(cost)">
                  <i class="fas fa-edit"></i> Modifica
                </button>
                <button class="btn btn-danger btn-sm" @click="deleteCost(cost.costId)">
                  <i class="fas fa-trash"></i> Elimina
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

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
              <!-- Stato Pagamento -->
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
      <div v-if="showEditModal" class="modal fade show" tabindex="-1" style="display: block;">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Modifica Spesa</h5>
              <button type="button" class="btn-close" @click="closeEditModal"></button>
            </div>
            <div class="modal-body">
              <!-- Tipologia -->
              <div class="mb-3">
                <label class="form-label">Tipologia</label>
                <select v-model="editCost.tipologia" class="form-select" required>
                  <option value="" disabled>Seleziona la tipologia</option>
                  <option v-for="type in expenseTypes" :key="type" :value="type">
                    {{ type.replaceAll('_', ' ') }}
                  </option>
                </select>
              </div>
              <!-- Importo -->
              <div class="mb-3">
                <label class="form-label">Importo (€)</label>
                <input
                  v-model.number="editCost.importo"
                  type="number"
                  class="form-control"
                  placeholder="Importo (€)"
                  required
                />
              </div>
              <!-- Selezione Gruppo -->
              <div class="mb-3">
                <label class="form-label">Gruppo</label>
                <select v-model="editCost.groupId" class="form-select">
                  <option value="">Nessun gruppo</option>
                  <option v-for="group in groups" :key="group.id" :value="group.id">
                    {{ group.nome }}
                  </option>
                </select>
              </div>
              <!-- Stato Pagamento -->
              <div class="mb-3">
                <label class="form-label">Stato Pagamento</label>
                <select v-model="editCost.paymentStatus" class="form-select">
                  <option value="DA_SALDARE">Da saldare</option>
                  <option value="PAGATO">Pagato</option>
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-secondary" @click="closeEditModal">
                Annulla
              </button>
              <button class="btn btn-success" @click="updateCost">
                Salva Modifica
              </button>
            </div>
          </div>
        </div>
      </div>

    </div>

    <!-- FOOTER -->
    <footer class="footer bg-dark text-white text-center py-3 mt-auto">
      <p class="mb-0">&copy; 2025 Spendly. Tutti i diritti riservati.</p>
      <router-link to="/contact" class="footer-link">Contattaci</router-link>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"

const costs = ref([])
const groups = ref([])

const totalSpent = ref(0)
const averageSpent = ref(0)
const lastSpent = ref("N/D")

const showModal = ref(false)
const showEditModal = ref(false)

// Nuova spesa per aggiunta
const newCost = ref({
  tipologia: "",
  importo: null,
  groupId: null,
  paymentStatus: "DA_SALDARE",
})

// Variabile per la spesa in modifica
const editCost = ref({
  costId: null,
  tipologia: "",
  importo: null,
  groupId: null,
  paymentStatus: "DA_SALDARE",
})

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
]

onMounted(() => {
  fetchCosts()
  fetchGroups()
})

async function fetchCosts() {
  const username = localStorage.getItem("username")
  const token = localStorage.getItem("token")
  if (!username || !token) {
    console.error("Utente non autenticato! Username o token mancante.")
    return
  }
  try {
    const response = await fetch(`http://localhost:8080/api/costs?username=${username}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
    if (!response.ok) throw new Error("Errore nel recupero delle spese")
    const data = await response.json()
    costs.value = data
    updateDashboard()
  } catch (error) {
    console.error(error)
  }
}

async function fetchGroups() {
  const username = localStorage.getItem("username")
  const token = localStorage.getItem("token")
  if (!username || !token) {
    console.error("Utente non autenticato! Username o token mancante.")
    return
  }
  try {
    const response = await fetch(`http://localhost:8080/api/groups?username=${username}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
    if (!response.ok) throw new Error("Errore nel recupero dei gruppi")
    groups.value = await response.json()
  } catch (error) {
    console.error(error)
  }
}

async function addCost() {
  if (!newCost.value.tipologia || !newCost.value.importo) {
    alert("Compila tutti i campi obbligatori!")
    return
  }
  const username = localStorage.getItem("username")
  const token = localStorage.getItem("token")
  if (!username || !token) {
    alert("Utente non autenticato.")
    return
  }
  const payload = {
    importo: newCost.value.importo,
    tipologia: newCost.value.tipologia,
    paymentStatus: newCost.value.paymentStatus,
    group: newCost.value.groupId ? { id: parseInt(newCost.value.groupId) } : null,
  }
  try {
    const response = await fetch(`http://localhost:8080/api/costs?username=${username}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(payload),
    })
    if (response.ok) {
      await fetchCosts()
      showModal.value = false
      newCost.value = { tipologia: "", importo: null, groupId: null, paymentStatus: "DA_SALDARE" }
    } else {
      const errorData = await response.json()
      alert(`Errore: ${errorData.message || "Impossibile aggiungere la spesa."}`)
    }
  } catch (error) {
    console.error("Errore nell'aggiunta della spesa:", error)
  }
}

async function deleteCost(costId) {
  const token = localStorage.getItem("token")
  if (!token) {
    alert("Utente non autenticato.")
    return
  }
  try {
    const response = await fetch(`http://localhost:8080/api/costs/${costId}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    })
    if (response.ok) await fetchCosts()
    else alert("Errore nella cancellazione della spesa.")
  } catch (error) {
    console.error("Errore nella cancellazione della spesa:", error)
  }
}

async function togglePaymentStatus(cost) {
  const username = localStorage.getItem("username")
  const token = localStorage.getItem("token")
  if (!username || !token) {
    alert("Utente non autenticato.")
    return
  }
  if (cost.paymentStatus === "DA_SALDARE") {
    try {
      const response = await fetch(`http://localhost:8080/api/costs/${cost.costId}/pay?username=${username}`, {
        method: "PUT",
        headers: { Authorization: `Bearer ${token}` },
      })
      if (!response.ok) {
        const errorText = await response.text()
        alert("Errore nel pagamento: " + errorText)
      } else await fetchCosts()
    } catch (error) {
      console.error("Errore nel pagamento:", error)
      alert("Errore nel pagamento.")
    }
  } else {
    cost.paymentStatus = "DA_SALDARE"
    try {
      const response = await fetch(`http://localhost:8080/api/costs/${cost.costId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(cost),
      })
      if (!response.ok) alert("Errore nel riportare la spesa a Da Saldare.")
      else updateDashboard()
    } catch (error) {
      console.error("Errore nell'aggiornamento dello stato di pagamento:", error)
    }
  }
}

function updateDashboard() {
  if (costs.value.length === 0) {
    totalSpent.value = 0
    averageSpent.value = 0
    lastSpent.value = "N/D"
    return
  }
  const total = costs.value.reduce((sum, c) => sum + c.importo, 0)
  totalSpent.value = total.toFixed(2)
  averageSpent.value = (total / costs.value.length).toFixed(2)
  lastSpent.value = `€${costs.value[costs.value.length - 1].importo.toFixed(2)}`
}

/* MODALE PER MODIFICARE SPESA */
function openEditModal(cost) {
  // Pre-riempi il modello editCost con i dati della spesa selezionata
  editCost.value = {
    costId: cost.costId,
    tipologia: cost.tipologia,
    importo: cost.importo,
    groupId: cost.group ? cost.group.id : null,
    paymentStatus: cost.paymentStatus
  }
  showEditModal.value = true
}

function closeEditModal() {
  showEditModal.value = false
}

async function updateCost() {
  const username = localStorage.getItem("username")
  const token = localStorage.getItem("token")
  if (!username || !token) {
    alert("Utente non autenticato.")
    return
  }
  try {
    const response = await fetch(`http://localhost:8080/api/costs/${editCost.value.costId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify({
        importo: editCost.value.importo,
        tipologia: editCost.value.tipologia,
        paymentStatus: editCost.value.paymentStatus,
        group: editCost.value.groupId ? { id: parseInt(editCost.value.groupId) } : null
      })
    })
    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(errorText)
    }
    await fetchCosts()
    closeEditModal()
  } catch (error) {
    alert(`Errore nella modifica della spesa: ${error.message}`)
    console.error("updateCost:", error)
  }
}
</script>

<style scoped>
.container {
  max-width: 900px;
}

/* Sfondo scuro dietro la modale */
.modal {
  background: rgba(0, 0, 0, 0.5);
}

/* Card Dashboard generica con animazione */
.dashboard-card {
  min-height: 110px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
}
.dashboard-card:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 8px rgba(0,0,0,0.15);
}

/* Card Numero Gruppi (BLU) */
.card-gruppi-attivi {
  background-color: #cce5ff;
  border: 1px solid #b8daff;
  color: #004085;
}

/* Card Membri Medi (VERDE) */
.card-ultima-spesa {
  background-color: #d4edda;
  border: 1px solid #c3e6cb;
  color: #155724;
}

/* Card Ultimo Gruppo (GIALLO) */
.card-totale-spese {
  background-color: #fff3cd;
  border: 1px solid #ffeeba;
  color: #856404;
}

/* Contenitore stile card per la tabella */
.table-card {
  background-color: #ffffff;
  border: 1px solid #dee2e6;
  border-radius: 0.25rem;
  padding: 1rem;
}

/* Animazione righe tabella */
.table-row {
  transition: background-color 0.2s, transform 0.2s;
  cursor: pointer;
}
.table-row:hover {
  background-color: #f8f9fa;
  transform: scale(1.01);
}

/* Allinea verticalmente il testo nelle celle della tabella */
.table > :not(caption) > * > * {
  vertical-align: middle;
}

/* Footer con stile aggiornato */
.footer {
  background: #111827;
  color: white;
  text-align: center;
  padding: 1.5rem 1rem;
  margin-top: auto;
}
.footer-link {
  color: #3b82f6;
  text-decoration: none;
  font-weight: bold;
  margin-left: 0.5rem;
}
.footer-link:hover {
  text-decoration: underline;
}

/* Wrapper per far stare il footer in fondo alla pagina */
.dashboard-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
</style>

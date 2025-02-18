<template>
  <div class="group-detail-page">
    <!-- Contenitore principale -->
    <div
      class="container my-4 py-4 text-dark"
      style="
        max-width: 900px;
        background: linear-gradient(to bottom right, #f0f9ff, #cfe4fc);
        border-radius: 15px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      "
    >
      <!-- Banner in alto -->
      <div class="text-center mb-4">
        <img
          src="/images/banner.jpg"
          alt="Banner Share"
          class="img-fluid rounded shadow-sm"
          style="max-height: 300px; object-fit: cover;"
        />
        <h2 class="mt-4 fw-bold" style="font-family: 'Comic Sans MS', cursive;">
          Dettaglio Gruppo
        </h2>
      </div>

      <!-- Sezione ALERT -->
      <div class="mt-4">
        <h5><i class="fas fa-bell me-1"></i> Alert del Gruppo</h5>
        <button class="btn btn-success" @click="openAlertModal">
          <i class="fas fa-plus"></i> Aggiungi Alert
        </button>

        <!-- Elenco alert -->
        <ul v-if="alerts.length > 0" class="list-group mt-3">
          <li
            v-for="alert in alerts"
            :key="alert.id"
            class="list-group-item d-flex justify-content-between align-items-center"
          >
            <span>
              <i class="fas fa-exclamation-circle text-danger me-2"></i>
              {{ alert.nome }} - Limite: €{{ alert.limite.toFixed(2) }}
            </span>
            <button
              class="btn btn-sm btn-danger"
              @click="deleteAlert(alert.id)"
            >
              <i class="fas fa-trash"></i> Elimina
            </button>
          </li>
        </ul>
        <p v-else class="text-muted mt-3">
          <i class="fas fa-check-circle text-success"></i> Nessun alert
          presente.
        </p>

        <!-- Modale per aggiungere un nuovo Alert -->
        <div
          v-if="showAlertModal"
          class="modal fade show d-block"
          tabindex="-1"
          style="background: rgba(0, 0, 0, 0.5);"
        >
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">Aggiungi un nuovo Alert</h5>
                <button
                  type="button"
                  class="btn-close"
                  @click="closeAlertModal"
                ></button>
              </div>
              <div class="modal-body">
                <div class="mb-3">
                  <label class="form-label">Nome Alert</label>
                  <input
                    v-model="newAlert.nome"
                    type="text"
                    class="form-control"
                    placeholder="Nome alert"
                  />
                </div>
                <div class="mb-3">
                  <label class="form-label">Importo Limite (€)</label>
                  <input
                    v-model.number="newAlert.limite"
                    type="number"
                    class="form-control"
                    placeholder="Importo limite"
                  />
                </div>
                <div class="mb-3">
                  <label class="form-label">ID Gruppo</label>
                  <input
                    v-model="groupId"
                    type="text"
                    class="form-control"
                    readonly
                  />
                </div>
              </div>
              <div class="modal-footer">
                <button
                  type="button"
                  class="btn btn-secondary"
                  @click="closeAlertModal"
                >
                  Chiudi
                </button>
                <button
                  type="button"
                  class="btn btn-success"
                  @click="confirmAddAlert"
                >
                  Aggiungi Alert
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Sezione SPESE -->
      <div class="mt-4">
        <h5><i class="fas fa-wallet me-1"></i> Spese del Gruppo</h5>

        <!-- Dashboard statistica (facoltativa) -->
        <div class="row mb-3">
          <div class="col-md-4 mb-2">
            <div
              class="p-2 text-white rounded"
              style="background-color: #0d6efd;"
            >
              <h6 class="mb-1"><i class="fas fa-euro-sign"></i> Totale Spese</h6>
              <p class="mb-0">€{{ totalSpent }}</p>
            </div>
          </div>
          <div class="col-md-4 mb-2">
            <div
              class="p-2 text-white rounded"
              style="background-color: #198754;"
            >
              <h6 class="mb-1"><i class="fas fa-calculator"></i> Spesa Media</h6>
              <p class="mb-0">€{{ averageSpent }}</p>
            </div>
          </div>
          <div class="col-md-4 mb-2">
            <div
              class="p-2 text-dark rounded"
              style="background-color: #ffc107;"
            >
              <h6 class="mb-1"><i class="fas fa-history"></i> Ultima Spesa</h6>
              <p class="mb-0">{{ lastSpent }}</p>
            </div>
          </div>
        </div>

        <!-- Tabella delle spese -->
        <table v-if="costs.length > 0" class="table table-striped">
          <thead class="table-light">
            <tr>
              <th>Importo</th>
              <th>Categoria</th>
              <th>Utente</th>
              <th>Azioni</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="cost in costs" :key="cost.costId">
              <td>€ {{ cost.importo.toFixed(2) }}</td>
              <td>{{ cost.tipologia.replace('_', ' ') }}</td>
              <td>{{ cost.user.username }}</td>
              <td>
                <button
                  class="btn btn-danger btn-sm"
                  @click="deleteCost(cost.costId)"
                >
                  <i class="fas fa-trash"></i> Elimina
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <p v-else class="text-muted">
          <i class="fas fa-money-bill-wave"></i> Nessuna spesa registrata.
        </p>

        <!-- Bottone per aprire il form "Aggiungi Spesa" -->
        <button class="btn btn-primary mt-2" @click="showCostModal = true">
          <i class="fas fa-plus"></i> Aggiungi Spesa
        </button>

        <!-- Modale per Aggiungere Spesa -->
        <div
          v-if="showCostModal"
          class="modal fade show d-block"
          tabindex="-1"
          style="background: rgba(0, 0, 0, 0.5);"
        >
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">Aggiungi Spesa</h5>
                <button
                  type="button"
                  class="btn-close"
                  @click="showCostModal = false"
                ></button>
              </div>
              <div class="modal-body">
                <!-- Tipologia -->
                <select v-model="newCost.tipologia" class="form-control mb-3" required>
                  <option value="" disabled>Seleziona la tipologia</option>
                  <option
                    v-for="type in expenseTypes"
                    :key="type"
                    :value="type"
                  >
                    {{ type.replace('_', ' ') }}
                  </option>
                </select>

                <!-- Importo -->
                <input
                  v-model.number="newCost.importo"
                  type="number"
                  class="form-control mb-3"
                  placeholder="Importo (€)"
                  required
                />
              </div>
              <div class="modal-footer">
                <button
                  class="btn btn-success"
                  @click="addCost"
                >
                  Salva
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Sezione per aggiungere un membro -->
      <div class="mt-4">
        <h5><i class="fas fa-user-plus me-1"></i> Aggiungi un membro</h5>
        <div class="d-flex align-items-center">
          <select
            class="form-select"
            v-model="selectedMember"
            style="max-width: 250px;"
          >
            <option value="" disabled>Seleziona un utente</option>
            <option v-for="user in allUsers" :key="user.id" :value="user.username">
              {{ user.username }}
            </option>
          </select>
          <button
            class="btn btn-primary ms-2"
            @click="addMemberToGroup(groupId, selectedMember)"
          >
            Aggiungi
          </button>
        </div>
      </div>

      <!-- Card principale con info gruppo -->
      <div class="card shadow-lg mb-5 border-0 mt-4">
        <div
          class="card-header text-white d-flex justify-content-between align-items-center"
          style="
            background: linear-gradient(to right, #007bff, #0056b3);
            border-top-left-radius: 8px;
            border-top-right-radius: 8px;
          "
        >
          <h4 class="mb-0">
            <i class="fas fa-users me-2"></i>{{ group.nome }}
          </h4>
          <span
            class="badge bg-light text-primary fs-6 d-flex align-items-center"
            style="border-radius: 20px;"
          >
            <i class="fas fa-user-friends me-1"></i>
            {{ group.membri ? group.membri.length : 0 }} membri
          </span>
        </div>

        <div class="card-body position-relative">
          <div
            v-if="group.membri && group.membri.length > 0"
            class="table-responsive mt-2"
          >
            <h5 class="mb-3">
              <i class="fas fa-address-card me-1"></i> Membri del Gruppo
            </h5>
            <table class="table table-hover table-striped align-middle">
              <thead class="table-light">
                <tr>
                  <th class="text-center" style="width: 70%">Username</th>
                  <th class="text-center" style="width: 30%">Azione</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="member in group.membri" :key="member.id">
                  <td class="text-center">
                    <div class="d-flex align-items-center justify-content-center">
                      <img
                        src="https://cdn-icons-png.flaticon.com/512/219/219986.png"
                        alt="Avatar"
                        style="width: 30px; margin-right: 8px;"
                      />
                      {{ member.username }}
                    </div>
                  </td>
                  <td class="text-center">
                    <button
                      class="btn btn-outline-danger btn-sm"
                      @click="removeMember(member.username)"
                    >
                      <i class="fas fa-user-minus"></i> Rimuovi
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-else>
            <p class="text-muted mb-0">
              <i class="fas fa-exclamation-triangle"></i> Nessun membro presente.
            </p>
          </div>

          <!-- Admin del gruppo, se presente -->
          <div class="mt-4" v-if="group.admin">
            <strong class="me-2">Admin:</strong>
            <span class="text-muted">{{ group.admin.username }}</span>
          </div>
        </div>

        <div class="card-footer d-flex justify-content-end border-0">
          <router-link to="/groups" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Torna alla lista gruppi
          </router-link>
        </div>
      </div>
    </div>

    <!-- FOOTER -->
    <footer class="footer">
      <p>&copy; 2025 Spendly. Tutti i diritti riservati.</p>
      <router-link to="/contact" class="footer-link">Contattaci</router-link>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

/** Router e parametri */
const route = useRoute()
const router = useRouter()
const groupId = route.params.groupId

/** Variabili reattive di stato */
const group = ref({})
const costs = ref([])
const alerts = ref([])

/** Modal per ALERT */
const showAlertModal = ref(false)
const newAlert = ref({ nome: '', limite: 0 })

/** Modal per SPESA */
const showCostModal = ref(false)
/**
 * newCost con i campi necessari:
 * - tipologia
 * - importo
 * (e group se vuoi passare group:{id:..} al backend)
 */
const newCost = ref({ tipologia: '', importo: null })

/** Possibili categorie di spesa */
const expenseTypes = [
  'ABITAZIONE_AFFITTO', 'ABITAZIONE_MUTUO', 'ABITAZIONE_BOLLETTE', 'ALIMENTARI',
  'TRASPORTI_CARBURANTE', 'TRASPORTI_PUBBLICO', 'TRASPORTI_MANUTENZIONE', 'TRASPORTI_ASSICURAZIONE',
  'SALUTE_FARMACI', 'SALUTE_VISITE', 'SALUTE_ASSICURAZIONE', 'ISTRUZIONE_TASSE',
  'ISTRUZIONE_MATERIALI', 'ISTRUZIONE_CORSI', 'ASSICURAZIONI_AUTO', 'ASSICURAZIONI_CASA',
  'ASSICURAZIONI_VITA', 'TASSE_PROPRIETA', 'SVAGO_CINEMA', 'SVAGO_TEATRO', 'SVAGO_CONCERTI',
  'SVAGO_HOBBY', 'VIAGGI_BIGLIETTI', 'VIAGGI_HOTEL', 'VIAGGI_ESCURSIONI', 'RISTORANTI_PRANZI',
  'RISTORANTI_CENE', 'RISTORANTI_CAFFE', 'SHOPPING_ABBIGLIAMENTO', 'SHOPPING_ACCESSORI',
  'SHOPPING_SCARPE', 'SHOPPING_COSMETICI', 'TECNOLOGIA_SMARTPHONE', 'TECNOLOGIA_TABLET',
  'TECNOLOGIA_COMPUTER', 'TECNOLOGIA_ABBONAMENTI'
]

/** Statistiche spese */
const totalSpent = ref(0)
const averageSpent = ref(0)
const lastSpent = ref('N/D')

/** Utenti per aggiungere membri */
const allUsers = ref([])
const selectedMember = ref('')

/** Dati utente loggato */
const adminUsername = localStorage.getItem('username')
const token = localStorage.getItem('token')

/** Lifecycle: carica dati gruppo e utenti */
onMounted(() => {
  fetchGroup()
  fetchUsers()
})

/** Apertura/chiusura modale Alert */
function openAlertModal() {
  showAlertModal.value = true
}
function closeAlertModal() {
  showAlertModal.value = false
}
async function confirmAddAlert() {
  await addAlert()
  await fetchGroup()
  closeAlertModal()
}

/** Recupera dettagli gruppo (con costs e alerts) */
async function fetchGroup() {
  try {
    if (!token) {
      alert('Token mancante. Devi essere autenticato.')
      router.push({ name: 'Login' })
      return
    }
    const res = await fetch(`http://localhost:8080/api/groups/${groupId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (!res.ok) {
      throw new Error('Errore nel recupero del gruppo.')
    }
    const data = await res.json()

    // Nel tuo GroupController, showGroup() restituisce
    // un GroupResponseDTO => { group, costs, alerts }
    group.value = data.group
    costs.value = data.costs
    alerts.value = data.alerts

    updateDashboard()
  } catch (error) {
    console.error(error)
    alert('Impossibile caricare il dettaglio del gruppo.')
  }
}

/** Recupera tutti gli utenti per la select "Aggiungi membro" */
async function fetchUsers() {
  if (!token) {
    console.error('Token mancante. Non posso caricare gli utenti.')
    return
  }
  try {
    const res = await fetch('http://localhost:8080/account/users', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (!res.ok) {
      throw new Error('Errore nel recupero degli utenti')
    }
    const data = await res.json()
    allUsers.value = data
  } catch (err) {
    console.error('Errore fetchAllUsers:', err)
  }
}

/** Aggiunge un Alert (POST /api/groups/{groupId}/alerts?adminUsername=...) */
async function addAlert() {
  try {
    const url = `http://localhost:8080/api/groups/${groupId}/alerts?adminUsername=${adminUsername}`
    const res = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify(newAlert.value)
    })

    if (!res.ok) {
      const errorMsg = await res.text()
      throw new Error(errorMsg)
    }

    // Ricarica e resetta
    await fetchGroup()
    newAlert.value = { nome: '', limite: 0 }
  } catch (error) {
    alert(`Errore nella creazione dell'alert: ${error.message}`)
    console.error('addAlert:', error)
  }
}

/** Elimina un alert (DELETE /api/groups/{groupId}/alerts/{alertId}?adminUsername=...) */
async function deleteAlert(alertId) {
  try {
    const url = `http://localhost:8080/api/groups/${groupId}/alerts/${alertId}?adminUsername=${adminUsername}`
    const res = await fetch(url, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token}` }
    })

    if (!res.ok) {
      const errorMsg = await res.text()
      throw new Error(errorMsg)
    }

    await fetchGroup()
  } catch (error) {
    alert(`Errore nell'eliminazione dell'alert: ${error.message}`)
    console.error('deleteAlert:', error)
  }
}

/** Aggiunge un membro (POST /api/groups/{groupId}/members?adminUsername=...&memberUsername=...) */
async function addMemberToGroup(groupId, memberUsername) {
  if (!adminUsername || !token) {
    alert('Utente non autenticato o token mancante.')
    return
  }
  if (!memberUsername) {
    alert('Seleziona l\'utente dal menu a tendina.')
    return
  }

  try {
    const url = `http://localhost:8080/api/groups/${groupId}/members?adminUsername=${adminUsername}&memberUsername=${memberUsername}`
    const response = await fetch(url, {
      method: 'POST',
      headers: { Authorization: `Bearer ${token}` }
    })

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(errorText)
    }

    // Aggiorna
    fetchGroup()
    selectedMember.value = ''
  } catch (error) {
    alert(`Errore aggiunta membro: ${error}`)
    console.error('Errore durante l\'aggiunta del membro:', error)
  }
}

/** Rimuove un membro (DELETE /api/groups/{groupId}/members?adminUsername=...&memberUsername=...) */
async function removeMember(memberUsername) {
  try {
    if (!token || !adminUsername) {
      alert('Utente non autenticato o token mancante.')
      return
    }
    const url = `http://localhost:8080/api/groups/${groupId}/members?adminUsername=${adminUsername}&memberUsername=${memberUsername}`
    const response = await fetch(url, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token}` }
    })

    if (!response.ok) {
      const errorMsg = await response.text()
      throw new Error(errorMsg)
    }

    fetchGroup()
  } catch (error) {
    alert('Errore nella rimozione del membro: ' + error.message)
    console.error('removeMember:', error)
  }
}

/** Aggiunge una spesa (POST /api/costs?username=...) -- rotta ipotizzata in CostController */
async function addCost() {
  if (!newCost.value.tipologia || !newCost.value.importo) {
    alert('Compila tutti i campi spesa!')
    return
  }
  if (!adminUsername || !token) {
    alert('Utente non autenticato o token mancante.')
    return
  }

  // Esempio di payload con group -> { id: groupId }
  const payload = {
    importo: newCost.value.importo,
    tipologia: newCost.value.tipologia,
    group: { id: groupId }
  }

  try {
    const url = `http://localhost:8080/api/costs?username=${adminUsername}`
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify(payload)
    })

    if (!response.ok) {
      const errorData = await response.text()
      throw new Error(errorData || 'Errore nella creazione della spesa.')
    }

    // Ricarica dati gruppo e chiudi modale
    await fetchGroup()
    showCostModal.value = false
    newCost.value = { tipologia: '', importo: null }
  } catch (error) {
    alert(`Errore nell'aggiunta della spesa: ${error.message}`)
    console.error('addCost:', error)
  }
}

/** Elimina una spesa (DELETE /api/costs/{costId}) -- rotta ipotizzata in CostController */
async function deleteCost(costId) {
  if (!token || !adminUsername) {
    alert('Utente non autenticato o token mancante.')
    return
  }
  try {
    const url = `http://localhost:8080/api/costs/${costId}`
    const response = await fetch(url, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token}` }
    })

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(errorText)
    }

    // Aggiorna
    fetchGroup()
  } catch (error) {
    alert('Errore nella cancellazione della spesa: ' + error.message)
    console.error('deleteCost:', error)
  }
}

/** Aggiorna le statistiche (totalSpent, averageSpent, lastSpent) */
function updateDashboard() {
  if (costs.value.length === 0) {
    totalSpent.value = 0
    averageSpent.value = 0
    lastSpent.value = 'N/D'
    return
  }
  const total = costs.value.reduce((acc, c) => acc + c.importo, 0)
  totalSpent.value = total.toFixed(2)
  averageSpent.value = (total / costs.value.length).toFixed(2)

  const last = costs.value[costs.value.length - 1]
  lastSpent.value = `€${last.importo.toFixed(2)}`
}
</script>

<style scoped>
.group-detail-page .footer {
  text-align: center;
  margin-top: 40px;
  color: #666;
}

.footer-link {
  margin-left: 10px;
  text-decoration: underline;
}

/* Modal overlay stile bootstrap "show" */
.modal {
  background: rgba(0, 0, 0, 0.5);
}
</style>

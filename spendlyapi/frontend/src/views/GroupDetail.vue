<template>
  <div class="group-detail-page d-flex flex-column min-vh-100">
    <!-- CONTENUTO PRINCIPALE -->
    <div class="container my-4 py-4 text-dark flex-grow-1" style="max-width: 1300px;">
      <!-- Banner in alto -->
      <div class="text-center mb-4">
        <img
          src="/images/banner.jpg"
          alt="Banner Share"
          class="img-fluid rounded shadow-sm banner-img"
        />
        <h2 class="mt-4 fw-bold banner-title">
          Dettaglio Gruppo {{ group.nome }}
        </h2>
      </div>

      <!-- Sezione ALERT (modale multipla, v-for sui warning) -->
      <div
        v-for="(warning, index) in thresholdWarnings"
        :key="index"
        class="modal fade show d-block modal-warning"
      >
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header bg-danger text-white">
              <h5 class="modal-title">Attenzione!</h5>
              <button type="button" class="btn-close" @click="removeWarning(index)"></button>
            </div>
            <div class="modal-body">
              <div class="d-flex align-items-start">
                <img
                  src="/images/esclamativo.jpg"
                  alt="Attenzione!"
                  class="warning-img"
                />
                <p class="mb-0">{{ warning }}</p>
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-danger btn-sm" @click="removeWarning(index)">
                <i class="fas fa-times"></i> Chiudi
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Sezione Alert del Gruppo -->
      <div class="mb-4">
        <h5>
          <i class="fas fa-bell me-1"></i> Alert del Gruppo
        </h5>
        <button class="btn btn-success" @click="openAlertModal">
          <i class="fas fa-plus"></i> Aggiungi Alert
        </button>

        <ul v-if="alerts.length > 0" class="alert-list mt-3">
          <li
            v-for="alert in alerts"
            :key="alert.id"
            class="alert-list-item d-flex justify-content-between align-items-center"
          >
            <span>
              <i class="fas fa-exclamation-circle text-danger me-2"></i>
              {{ alert.nome }} - Limite: €{{ alert.limite.toFixed(2) }} -
              Macroarea: {{ alert.macroArea }}
            </span>
            <button class="btn btn-danger btn-sm" @click="deleteAlert(alert.id)">
              <i class="fas fa-trash"></i> Elimina
            </button>
          </li>
        </ul>
        <p v-else class="text-muted mt-3">
          <i class="fas fa-check-circle text-success"></i> Nessun alert presente.
        </p>
      </div>

      <!-- Divisione in due colonne -->
      <div class="row">
        <!-- Colonna sinistra: Spese Gruppo + Grafico Andamento -->
        <div class="col-md-6">
          <div class="mb-4">
            <h5><i class="fas fa-wallet me-1"></i> Spese del Gruppo</h5>
            <!-- Dashboard statistica -->
            <div class="row mb-3">
              <div class="col-md-4 mb-2">
                <div class="p-2 rounded stat-card" style="background-color: #f8d7da; color: #721c24;">
                  <h6 class="mb-1">
                    <i class="fas fa-euro-sign"></i> Totale Spese
                  </h6>
                  <p class="mb-0">€{{ totalSpent }}</p>
                </div>
              </div>
              <div class="col-md-4 mb-2">
                <div class="p-2 rounded stat-card" style="background-color: #d4edda; color: #155724;">
                  <h6 class="mb-1">
                    <i class="fas fa-calculator"></i> Spesa Media
                  </h6>
                  <p class="mb-0">€{{ averageSpent }}</p>
                </div>
              </div>
              <div class="col-md-4 mb-2">
                <div class="p-2 rounded stat-card" style="background-color: #fff3cd; color: #856404;">
                  <h6 class="mb-1">
                    <i class="fas fa-history"></i> Ultima Spesa
                  </h6>
                  <p class="mb-0">{{ lastSpent }}</p>
                </div>
              </div>
            </div>
            <!-- Tabella delle spese -->
            <table v-if="costs.length > 0" class="table custom-table">
              <thead>
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
                    <button class="btn btn-danger btn-sm" @click="deleteCost(cost.costId)">
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
          </div>
          <!-- Grafico Andamento Spese -->
          <div class="mb-4">
            <h5 class="mb-3"><i class="fas fa-chart-area me-1"></i> Andamento Spese</h5>
            <div class="chart-container bigger-chart">
              <canvas id="trendChart"></canvas>
            </div>
          </div>
        </div>

        <!-- Colonna destra: Macroaree -->
        <div class="col-md-6">
          <!-- Elenco testuale per Macroaree -->
          <div class="mb-4">
            <h5><i class="fas fa-chart-bar me-1"></i> Spesa per Macroaree</h5>
            <ul class="macro-list">
              <li
                v-for="(sum, macro) in macroAreaSums"
                :key="macro"
                class="macro-list-item d-flex justify-content-between align-items-center"
              >
                {{ macro }}:
                <span>€{{ sum.toFixed(2) }}</span>
              </li>
            </ul>
          </div>
          <!-- Grafici: Istogramma e Grafico a Torta -->
          <div class="mb-4">
            <h5 class="mb-3"><i class="fas fa-chart-line me-1"></i> Rappresentazione per Macroaree</h5>
            <div class="row">
              <div class="col-md-6">
                <div class="chart-container bigger-chart">
                  <canvas id="barChart"></canvas>
                </div>
              </div>
              <div class="col-md-6">
                <div class="chart-container bigger-chart">
                  <canvas id="pieChart"></canvas>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Sezione sotto le due colonne: Aggiungi membro e Tabella Gruppo -->
      <div class="mt-4">
        <h5>
          <i class="fas fa-user-plus me-1"></i> Aggiungi un membro
        </h5>
        <div class="d-flex align-items-center">
          <select class="form-select" v-model="selectedMember" style="max-width: 250px;">
            <option value="" disabled>Seleziona un utente</option>
            <option v-for="user in allUsers" :key="user.id" :value="user.username">
              {{ user.username }}
            </option>
          </select>
          <button class="btn btn-primary ms-2" @click="addMemberToGroup(groupId, selectedMember)">
            Aggiungi
          </button>
        </div>
      </div>

      <!-- Card Gruppo: Informazioni e membri -->
      <div class="card shadow-lg mb-5 border-0 mt-4">
        <div class="card-header text-white d-flex justify-content-between align-items-center header-gradient">
          <h4 class="mb-0">
            <i class="fas fa-users me-2"></i>{{ group.nome }}
          </h4>
          <span class="badge bg-light text-primary fs-6 d-flex align-items-center" style="border-radius: 20px;">
            <i class="fas fa-user-friends me-1"></i>
            {{ group.membri ? group.membri.length : 0 }} membri
          </span>
        </div>
        <div class="card-body position-relative">
          <div v-if="group.membri && group.membri.length > 0" class="table-responsive mt-2">
            <h5 class="mb-3">
              <i class="fas fa-address-card me-1"></i> Membri del Gruppo
            </h5>
            <table class="table custom-table align-middle">
              <thead>
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
                        class="avatar-img"
                      />
                      {{ member.username }}
                    </div>
                  </td>
                  <td class="text-center">
                    <button class="btn btn-danger btn-sm" @click="removeMember(member.username)">
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

      <!-- Sezione Transazioni-->
      <div>
        <h5>Transazioni</h5>
        <!-- Pulsante per aggiornare i dati -->
        <button @click="ottimizzaDebiti" class="btn btn-primary">
          Calcolo Debiti
        </button>
        <!-- Lista delle transazioni -->
        <div v-if="transactions.length">
          <div v-for="(trans, index) in transactions" :key="index" class="card mb-2 p-2">
            <p><strong>{{ trans.debitore }}</strong> → <strong>{{ trans.creditore }}</strong></p>
            <p>💰 {{ trans.importo.toFixed(2) }} €</p>
          </div>
        </div>
        <p v-else>Nessuna transazione da mostrare.</p>
      </div>
    </div>

    <!-- FOOTER -->
    <footer class="footer">
      <p>&copy; 2025 Spendly. Tutti i diritti riservati.</p>
      <router-link to="/contact" class="footer-link">Contattaci</router-link>
    </footer>

    <!-- MODALE per Aggiungere Alert -->
    <div
      v-if="showAlertModal"
      class="modal fade show d-block"
      tabindex="-1"
      style="background: rgba(0, 0, 0, 0.5);"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Aggiungi un nuovo Alert</h5>
            <button type="button" class="btn-close" @click="closeAlertModal"></button>
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
              <label class="form-label">Macroarea</label>
              <select v-model="newAlert.macroArea" class="form-select">
                <option value="" disabled>Seleziona la macroarea</option>
                <option
                  v-for="(types, macro) in macroAreaMapping"
                  :key="macro"
                  :value="macro"
                >
                  {{ macro }}
                </option>
              </select>
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
            <button type="button" class="btn btn-secondary" @click="closeAlertModal">
              Chiudi
            </button>
            <button type="button" class="btn btn-success" @click="confirmAddAlert">
              Aggiungi Alert
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- MODALE per Aggiungere Spesa -->
    <div
      v-if="showCostModal"
      class="modal fade show d-block"
      tabindex="-1"
      style="background: rgba(0, 0, 0, 0.5);"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Aggiungi Spesa</h5>
            <button type="button" class="btn-close" @click="showCostModal = false"></button>
          </div>
          <div class="modal-body">
            <select
              v-model="newCost.tipologia"
              class="form-control mb-3"
              required
            >
              <option value="" disabled>Seleziona la tipologia</option>
              <option v-for="type in expenseTypes" :key="type" :value="type">
                {{ type.replace('_', ' ') }}
              </option>
            </select>
            <input
              v-model.number="newCost.importo"
              type="number"
              class="form-control mb-3"
              placeholder="Importo (€)"
              required
            />
          </div>
          <div class="modal-footer">
            <button class="btn btn-success" @click="addCost">
              Salva
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Chart from 'chart.js/auto'

/** Router e parametri */
const route = useRoute()
const router = useRouter()
const groupId = route.params.groupId

/** Variabili reattive di stato */
const group = ref({})
const costs = ref([])
const alerts = ref([])
const transactions = ref([])

/** Modal per ALERT */
const showAlertModal = ref(false)
const newAlert = ref({ nome: '', limite: 0, macroArea: '' })

/** Modal per SPESA */
const showCostModal = ref(false)
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

/** Variabili per alert soglia */
const thresholdWarnings = ref([])

/** Mapping delle macroaree */
const macroAreaMapping = {
  ABITAZIONE: ['ABITAZIONE_AFFITTO', 'ABITAZIONE_MUTUO', 'ABITAZIONE_BOLLETTE'],
  TRASPORTI: ['TRASPORTI_CARBURANTE', 'TRASPORTI_PUBBLICO', 'TRASPORTI_MANUTENZIONE', 'TRASPORTI_ASSICURAZIONE'],
  SALUTE: ['SALUTE_FARMACI', 'SALUTE_VISITE', 'SALUTE_ASSICURAZIONE'],
  ISTRUZIONE: ['ISTRUZIONE_TASSE', 'ISTRUZIONE_MATERIALI', 'ISTRUZIONE_CORSI'],
  ASSICURAZIONI: ['ASSICURAZIONI_AUTO', 'ASSICURAZIONI_CASA', 'ASSICURAZIONI_VITA'],
  TASSE: ['TASSE_PROPRIETA'],
  SVAGO_E_CULTURA: ['SVAGO_CINEMA', 'SVAGO_TEATRO', 'SVAGO_CONCERTI', 'SVAGO_HOBBY'],
  VIAGGI: ['VIAGGI_BIGLIETTI', 'VIAGGI_HOTEL', 'VIAGGI_ESCURSIONI'],
  RISTORAZIONE_E_ALIMENTARI: ['ALIMENTARI', 'RISTORANTI_PRANZI', 'RISTORANTI_CENE', 'RISTORANTI_CAFFE'],
  SHOPPING: ['SHOPPING_ABBIGLIAMENTO', 'SHOPPING_ACCESSORI', 'SHOPPING_SCARPE', 'SHOPPING_COSMETICI'],
  TECNOLOGIA: ['TECNOLOGIA_SMARTPHONE', 'TECNOLOGIA_TABLET', 'TECNOLOGIA_COMPUTER', 'TECNOLOGIA_ABBONAMENTI']
}

/** Computed: Somma costi per Macroaree */
const macroAreaSums = computed(() => {
  const sums = {}
  for (const macro in macroAreaMapping) {
    sums[macro] = 0
  }
  costs.value.forEach(cost => {
    for (const macro in macroAreaMapping) {
      if (macroAreaMapping[macro].includes(cost.tipologia)) {
        sums[macro] += cost.importo
      }
    }
  })
  return sums
})

/** Computed: Etichette e dati per grafici macroaree */
const macroLabels = computed(() => Object.keys(macroAreaSums.value))
const macroValues = computed(() => Object.values(macroAreaSums.value))

/** Computed: Etichette e dati per il grafico trend spese */
const expenseTrendLabels = computed(() => costs.value.map((_, i) => `Spesa ${i + 1}`))
const expenseTrendData = computed(() => {
  let sum = 0
  return costs.value.map(c => {
    sum += c.importo
    return sum
  })
})

/** Variabili per le istanze dei grafici */
let barChartInstance = null
let pieChartInstance = null
let trendChartInstance = null

/** Nuovo set di colori per il grafico a torta (tonalità pastello) */
const pieChartColors = [
  'rgba(135, 206, 250, 0.8)',
  'rgba(144, 238, 144, 0.8)',
  'rgba(255, 182, 193, 0.8)',
  'rgba(255, 218, 185, 0.8)',
  'rgba(221, 160, 221, 0.8)',
  'rgba(176, 196, 222, 0.8)',
  'rgba(240, 230, 140, 0.8)',
  'rgba(152, 251, 152, 0.8)',
  'rgba(255, 160, 122, 0.8)',
  'rgba(175, 238, 238, 0.8)'
]

/** Array di colori per gli altri grafici */
const chartColors = [
  'rgba(255, 99, 132, 0.6)',
  'rgba(54, 162, 235, 0.6)',
  'rgba(255, 206, 86, 0.6)',
  'rgba(75, 192, 192, 0.6)',
  'rgba(153, 102, 255, 0.6)',
  'rgba(255, 159, 64, 0.6)',
  'rgba(199, 199, 199, 0.6)',
  'rgba(83, 102, 255, 0.6)',
  'rgba(255, 102, 255, 0.6)',
  'rgba(102, 255, 102, 0.6)'
]

/** Al mount del componente */
onMounted(() => {
  fetchGroup()
  fetchUsers()
  renderBarChart()
  renderPieChart()
  renderTrendChart()
})

/** Watch per aggiornare i grafici macroaree quando cambiano i dati */
watch(macroAreaSums, () => {
  updateCharts()
}, { deep: true })

/** Watch per aggiornare il grafico trend quando cambiano le spese */
watch(costs, () => {
  updateTrendChart()
  checkAlertThresholds() // Aggiorna i warning
}, { deep: true })

/** Genera i warning per ogni alert che soddisfa il criterio */
function checkAlertThresholds() {
  thresholdWarnings.value = []
  if (!alerts.value || alerts.value.length === 0) return
  alerts.value.forEach(a => {
    const macroTotal = macroAreaSums.value[a.macroArea] || 0
    if (macroTotal >= 0.8 * a.limite) {
      thresholdWarnings.value.push(
        `Attenzione! Nella macroarea ${a.macroArea} hai speso €${macroTotal.toFixed(2)} su un limite di €${a.limite.toFixed(2)} (Alert: ${a.nome}).`
      )
    }
  })
}

/** Rimuove un warning */
function removeWarning(index) {
  thresholdWarnings.value.splice(index, 1)
}

/** Apertura/Chiusura modale Alert */
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

/** Carica i dettagli del gruppo */
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
    if (!res.ok) throw new Error('Errore nel recupero del gruppo.')
    const data = await res.json()
    group.value = data.group
    costs.value = data.costs
    alerts.value = data.alerts
    updateDashboard()
    checkAlertThresholds()
  } catch (error) {
    console.error(error)
    alert('Impossibile caricare il dettaglio del gruppo.')
  }
}

/** Carica gli utenti per la select */
async function fetchUsers() {
  if (!token) {
    console.error('Token mancante. Non posso caricare gli utenti.')
    return
  }
  try {
    const res = await fetch('http://localhost:8080/account/users', {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (!res.ok) throw new Error('Errore nel recupero degli utenti')
    const data = await res.json()
    allUsers.value = data
  } catch (err) {
    console.error('Errore fetchAllUsers:', err)
  }
}

/** Crea un nuovo alert */
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
    newAlert.value = { nome: '', limite: 0, macroArea: '' }
  } catch (error) {
    alert(`Errore nella creazione dell'alert: ${error.message}`)
    console.error('addAlert:', error)
  }
}

/** Elimina un alert */
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
    fetchGroup()
  } catch (error) {
    alert(`Errore nell'eliminazione dell'alert: ${error.message}`)
    console.error('deleteAlert:', error)
  }
}

/** Aggiunge un membro al gruppo */
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
    fetchGroup()
    selectedMember.value = ''
  } catch (error) {
    alert(`Errore aggiunta membro: ${error}`)
    console.error('Errore durante l aggiunta del membro:', error)
  }
}

/** Rimuove un membro */
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

/** Aggiunge una spesa al gruppo */
async function addCost() {
  if (!newCost.value.tipologia || !newCost.value.importo) {
    alert('Compila tutti i campi spesa!')
    return
  }
  if (!adminUsername || !token) {
    alert('Utente non autenticato o token mancante.')
    return
  }
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
    fetchGroup()
    showCostModal.value = false
    newCost.value = { tipologia: '', importo: null }
  } catch (error) {
    alert(`Errore nell'aggiunta della spesa: ${error.message}`)
    console.error('addCost:', error)
  }
}

/** Elimina una spesa */
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
    fetchGroup()
  } catch (error) {
    alert('Errore nella cancellazione della spesa: ' + error.message)
    console.error('deleteCost:', error)
  }
}

/** Aggiorna le statistiche e controlla le soglie */
function updateDashboard() {
  if (costs.value.length === 0) {
    totalSpent.value = 0
    averageSpent.value = 0
    lastSpent.value = 'N/D'
  } else {
    const total = costs.value.reduce((acc, c) => acc + c.importo, 0)
    totalSpent.value = total.toFixed(2)
    averageSpent.value = (total / costs.value.length).toFixed(2)
    const last = costs.value[costs.value.length - 1]
    lastSpent.value = `€${last.importo.toFixed(2)}`
  }
  checkAlertThresholds()
}

/* --- Grafici con Chart.js --- */
function renderBarChart() {
  const ctx = document.getElementById('barChart').getContext('2d')
  barChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: macroLabels.value,
      datasets: [{
        label: 'Spesa per Macroarea',
        data: macroValues.value,
        backgroundColor: chartColors.slice(0, macroLabels.value.length)
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } }
    }
  })
}

function renderPieChart() {
  const ctx = document.getElementById('pieChart').getContext('2d')
  pieChartInstance = new Chart(ctx, {
    type: 'pie',
    data: {
      labels: macroLabels.value,
      datasets: [{
        label: 'Spesa per Macroarea',
        data: macroValues.value,
        backgroundColor: pieChartColors.slice(0, macroLabels.value.length)
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false
    }
  })
}

function renderTrendChart() {
  const ctx = document.getElementById('trendChart').getContext('2d')
  trendChartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: expenseTrendLabels.value,
      datasets: [{
        label: 'Andamento Spese',
        data: expenseTrendData.value,
        borderColor: 'rgba(75, 192, 192, 1)',
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        fill: true
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true } }
    }
  })
}

function updateCharts() {
  if (barChartInstance) {
    barChartInstance.data.labels = macroLabels.value
    barChartInstance.data.datasets[0].data = macroValues.value
    barChartInstance.data.datasets[0].backgroundColor = chartColors.slice(0, macroLabels.value.length)
    barChartInstance.update()
  }
  if (pieChartInstance) {
    pieChartInstance.data.labels = macroLabels.value
    pieChartInstance.data.datasets[0].data = macroValues.value
    pieChartInstance.data.datasets[0].backgroundColor = pieChartColors.slice(0, macroLabels.value.length)
    pieChartInstance.update()
  }
}

function updateTrendChart() {
  if (trendChartInstance) {
    trendChartInstance.data.labels = expenseTrendLabels.value
    trendChartInstance.data.datasets[0].data = expenseTrendData.value
    trendChartInstance.update()
  }
}

async function ottimizzaDebiti() {
  try {
    const response = await fetch(`http://localhost:8080/api/groups/${groupId}/OttimizzaDebiti`)
    if (!response.ok) throw new Error("Errore nel recupero delle transazioni")
    transactions.value = await response.json()
  } catch (error) {
    console.error("Errore nel caricamento:", error)
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap');

html, body {
  height: 100%;
  margin: 0;
  padding: 0;
  font-family: 'Poppins', sans-serif;
}

/* Layout flessibile per far stare il footer in fondo */
.group-detail-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

body {
  background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%);
}

.container {
  background: linear-gradient(to bottom right, #f0f9ff, #cfe4fc);
  border-radius: 15px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 2rem;
}

/* Banner */
.banner-img {
  max-height: 300px;
  object-fit: cover;
}
.banner-title {
  font-weight: 600;
  letter-spacing: 1px;
}

/* Statistiche */
.stat-card {
  transition: transform 0.3s ease;
}
.stat-card:hover {
  transform: scale(1.02);
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
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
table.custom-table tbody tr:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
table.custom-table td {
  padding: 1rem;
  text-align: left;
}

/* Macroaree */
ul.macro-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.macro-list-item {
  background: linear-gradient(90deg, #e8f5e9, #c8e6c9);
  border-radius: 8px;
  margin-bottom: 0.5rem;
  padding: 0.75rem 1rem;
  transition: transform 0.2s ease, background 0.2s ease;
  cursor: pointer;
  font-weight: 600;
}
.macro-list-item:hover {
  transform: translateX(5px);
  background: linear-gradient(90deg, #c8e6c9, #a5d6a7);
}

/* Alert list */
ul.alert-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.alert-list-item {
  background: linear-gradient(90deg, #ffe1e1, #ffc9c9);
  border-radius: 8px;
  margin-bottom: 0.5rem;
  padding: 0.75rem 1rem;
  transition: transform 0.2s ease, background 0.2s ease;
  cursor: pointer;
  font-weight: 500;
}
.alert-list-item:hover {
  transform: translateX(5px);
  background: linear-gradient(90deg, #ffc9c9, #ffb3b3);
}

/* Effetto hover generico */
.table-hover tbody tr:hover,
.list-group-item:hover,
.list-item-hover {
  transform: scale(1.02);
  transition: transform 0.2s ease-in-out;
  background-color: #f8f9fa;
  z-index: 1;
  position: relative;
  cursor: pointer;
}

/* Avatar */
.avatar-img {
  width: 30px;
  margin-right: 8px;
}

/* Header card */
.header-gradient {
  background: linear-gradient(to right, #007bff, #0056b3);
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

/* Footer card */
.card-footer {
  background-color: #f8f9fa;
  border-top: none;
  padding: 0.75rem 1rem;
}

/* Container per i grafici con dimensioni maggiori */
.chart-container.bigger-chart {
  min-height: 400px;
  margin-bottom: 1.5rem;
}

/* FOOTER */
.footer {
  background: #1e293b;
  color: white;
  text-align: center;
  padding: 1.5rem 1rem;
  margin-top: auto;
}
.footer-link {
  color: #38bdf8;
  text-decoration: none;
  font-weight: bold;
  margin-left: 0.5rem;
}
.footer-link:hover {
  text-decoration: underline;
}

/* Modal overlay */
.modal {
  background: rgba(0, 0, 0, 0.5);
}
.modal-warning .warning-img {
  width: 160px;
  margin-right: 40px;
}

/* Bottoni personalizzati */
.btn-custom-detail {
  background-color: #1e90ff;
  color: #fff;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}
.btn-custom-detail:hover {
  background-color: #1c86ee;
}
.btn-custom-delete {
  background-color: #dc3545;
  color: #fff;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}
.btn-custom-delete:hover {
  background-color: #c82333;
}
.btn-secondary {
  background-color: #6c757d;
  border: none;
  color: #fff;
}
.btn-secondary:hover {
  background-color: #5a6268;
}

/* Wrapper per far stare il footer in fondo */
.dashboard-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
</style>

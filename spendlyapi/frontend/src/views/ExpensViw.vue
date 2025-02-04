<template>
    <div class="container mt-5">
      <h1 class="mb-4"><i class="fas fa-receipt"></i> Gestione Spese</h1>
  
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
            <th>Data</th>
            <th>Descrizione</th>
            <th>Importo (€)</th>
            <th>Modalità</th>
            <th>Beneficiario</th>
            <th>Azioni</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="expenses.length === 0">
            <td colspan="6" class="text-center">Nessuna spesa registrata</td>
          </tr>
          <tr v-for="expense in expenses" :key="expense.id">
            <td>{{ expense.date || 'N/D' }}</td>
            <td>{{ expense.description }}</td>
            <td>€{{ expense.amount.toFixed(2) }}</td>
            <!-- Se la spesa ha un beneficiary la consideriamo individuale -->
            <td>{{ expense.beneficiary ? 'Individuale' : 'Di Gruppo' }}</td>
            <td>{{ expense.beneficiary ? expense.beneficiary.username : '-' }}</td>
            <td>
              <button class="btn btn-danger btn-sm" @click="deleteExpense(expense.id)">Elimina</button>
            </td>
          </tr>
        </tbody>
      </table>
  
      <!-- Bottone per Aggiungere Spesa -->
      <button class="btn btn-primary" @click="openModal">
        <i class="fas fa-plus"></i> Aggiungi Spesa
      </button>
  
      <!-- Modale per Aggiungere Spesa -->
      <div v-if="showModal" class="modal fade show" tabindex="-1" style="display: block;">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Aggiungi Spesa</h5>
              <button type="button" class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <!-- Campo Descrizione -->
              <input
                v-model="newExpense.description"
                type="text"
                class="form-control mb-2"
                placeholder="Descrizione"
                required
              />
              <!-- Campo Importo -->
              <input
                v-model.number="newExpense.amount"
                type="number"
                class="form-control mb-2"
                placeholder="Importo (€)"
                required
              />
              <!-- Selezione Tipo di Spesa -->
              <div class="mb-2">
                <label for="expenseType" class="form-label">Tipo di Spesa:</label>
                <select id="expenseType" v-model="newExpense.expenseType" class="form-control" required>
                  <option value="" disabled>Seleziona il tipo</option>
                  <option v-for="type in expenseTypes" :key="type" :value="type">
                    {{ type }}
                  </option>
                </select>
              </div>
              <!-- Selezione Modalità Spesa -->
              <div class="mb-2">
                <label class="form-label">Modalità Spesa:</label>
                <div>
                  <input type="radio" id="modeGroup" value="group" v-model="newExpense.mode" />
                  <label for="modeGroup">Spesa di Gruppo</label>
                </div>
                <div>
                  <input type="radio" id="modeIndividual" value="individual" v-model="newExpense.mode" />
                  <label for="modeIndividual">Spesa Individuale</label>
                </div>
              </div>
              <!-- Se la modalità è individuale, mostra il campo per il Beneficiario -->
              <div class="mb-2" v-if="newExpense.mode === 'individual'">
                <label for="beneficiary" class="form-label">Beneficiario:</label>
                <select id="beneficiary" v-model="newExpense.beneficiary" class="form-control" required>
                  <option value="" disabled>Seleziona beneficiario</option>
                  <option v-for="member in groupMembers" :key="member.id" :value="member.id">
                    {{ member.username }}
                  </option>
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-success" @click="addExpense">Salva</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  
  const expenses = ref([]);
  const totalSpent = ref(0);
  const averageSpent = ref(0);
  const lastSpent = ref("N/D");
  const showModal = ref(false);
  
  // Nuovo oggetto spesa con i campi necessari
  const newExpense = ref({
    description: "",
    amount: null,
    expenseType: "",   // Campo per il tipo di spesa
    mode: "group",     // Default: spesa di gruppo
    beneficiary: null  // Se la spesa è individuale, conterrà l'ID del beneficiario
  });
  
  // Array per contenere i membri del gruppo (per spese individuali)
  const groupMembers = ref([]);
  
  // Array con i possibili tipi di spesa
  const expenseTypes = ref([
    'ABITAZIONE_AFFITTO',
    'ABITAZIONE_MUTUO',
    'ABITAZIONE_BOLLETTE',
    'ALIMENTARI',
    'TRASPORTI_CARBURANTE',
    'TRASPORTI_PUBBLICO',
    'TRASPORTI_MANUTENZIONE',
    'TRASPORTI_ASSICURAZIONE',
    'SALUTE_FARMACI',
    'SALUTE_VISITE',
    'SALUTE_ASSICURAZIONE',
    'ISTRUZIONE_TASSE',
    'ISTRUZIONE_MATERIALI',
    'ISTRUZIONE_CORSI',
    'ASSICURAZIONI_AUTO',
    'ASSICURAZIONI_CASA',
    'ASSICURAZIONI_VITA',
    'TASSE_PROPRIETA',
    'SVAGO_CINEMA',
    'SVAGO_TEATRO',
    'SVAGO_CONCERTI',
    'SVAGO_HOBBY',
    'VIAGGI_BIGLIETTI',
    'VIAGGI_HOTEL',
    'VIAGGI_ESCURSIONI',
    'RISTORANTI_PRANZI',
    'RISTORANTI_CENE',
    'RISTORANTI_CAFFE',
    'SHOPPING_ABBIGLIAMENTO',
    'SHOPPING_ACCESSORI',
    'SHOPPING_SCARPE',
    'SHOPPING_COSMETICI',
    'TECNOLOGIA_SMARTPHONE',
    'TECNOLOGIA_TABLET',
    'TECNOLOGIA_COMPUTER',
    'TECNOLOGIA_ABBONAMENTI'
  ]);
  
  const fetchExpenses = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/costs", {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem('token')}`
        }
      });
      const data = await response.json();
      expenses.value = data;
      updateDashboard();
    } catch (error) {
      console.error("Errore nel recupero delle spese:", error);
    }
  };
  
  const fetchGroupMembers = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/groups/members", {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem('token')}`
        }
      });
      const data = await response.json();
      groupMembers.value = data;
    } catch (error) {
      console.error("Errore nel recupero dei membri del gruppo:", error);
    }
  };
  
  const addExpense = async () => {
    if (!newExpense.value.description || !newExpense.value.amount || !newExpense.value.expenseType) {
      alert("Compila tutti i campi obbligatori!");
      return;
    }
  
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');
    if (!token || !username) {
      alert("Utente non autenticato.");
      return;
    }
  
    // Costruiamo il payload includendo tutti i campi richiesti
    const payload = {
      description: newExpense.value.description,
      amount: newExpense.value.amount,
      expenseType: newExpense.value.expenseType,
      mode: newExpense.value.mode,
      beneficiary: newExpense.value.mode === 'individual'
        ? { id: newExpense.value.beneficiary }
        : null,
      // Si assume che l'ID del gruppo e dell'utente siano salvati in localStorage
      group: { id: parseInt(localStorage.getItem('groupId')) },
      createdBy: { id: parseInt(localStorage.getItem('userId')) }
    };
  
    try {
      const response = await fetch(`http://localhost:8080/api/costs?username=${username}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      });
  
      if (response.ok) {
        await fetchExpenses();
        // Resettiamo il form e chiudiamo il modale
        newExpense.value = { description: "", amount: null, expenseType: "", mode: "group", beneficiary: null };
        closeModal();
      } else {
        const errorData = await response.json();
        alert(`Errore: ${errorData.message}`);
      }
    } catch (error) {
      console.error("Errore nell'aggiunta della spesa:", error);
    }
  };
  
  const deleteExpense = async (id) => {
    try {
      const token = localStorage.getItem('token');
      await fetch(`http://localhost:8080/api/costs/${id}`, {
        method: "DELETE",
        headers: {
          "Authorization": `Bearer ${token}`
        }
      });
      fetchExpenses();
    } catch (error) {
      console.error("Errore nella cancellazione della spesa:", error);
    }
  };
  
  const updateDashboard = () => {
    const total = expenses.value.reduce((sum, e) => sum + e.amount, 0);
    totalSpent.value = total.toFixed(2);
    averageSpent.value = expenses.value.length > 0 ? (total / expenses.value.length).toFixed(2) : 0;
    lastSpent.value =
      expenses.value.length > 0
        ? `€${expenses.value[expenses.value.length - 1].amount.toFixed(2)}`
        : "N/D";
  };
  
  const openModal = () => {
    showModal.value = true;
  };
  
  const closeModal = () => {
    showModal.value = false;
  };
  
  onMounted(() => {
    fetchExpenses();
    fetchGroupMembers();
  });
  </script>
  
  <style scoped>
  .container {
    max-width: 800px;
  }
  
  .modal {
    background: rgba(0, 0, 0, 0.5);
  }
  </style>
  
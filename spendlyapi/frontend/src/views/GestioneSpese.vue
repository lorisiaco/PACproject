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
          <th>Azioni</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="expenses.length === 0">
          <td colspan="4" class="text-center">Nessuna spesa registrata</td>
        </tr>
        <tr v-for="expense in expenses" :key="expense.costId">
          <td>{{ expense.date || 'N/D' }}</td>
          <td>{{ expense.tipologia }}</td>
          <td>€{{ expense.importo.toFixed(2) }}</td>
          <td>
            <button class="btn btn-danger btn-sm" @click="deleteExpense(expense.costId)">
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
            <input v-model="newExpense.tipologia" type="text" class="form-control mb-2" placeholder="Descrizione" required />
            <input v-model.number="newExpense.importo" type="number" class="form-control mb-2" placeholder="Importo (€)" required />
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
import { ref, onMounted } from "vue";

// Stati reattivi
const expenses = ref([]);
const totalSpent = ref(0);
const averageSpent = ref(0);
const lastSpent = ref("N/D");
const showModal = ref(false);
const newExpense = ref({ tipologia: "", importo: null });

// **Recupero le spese solo dell'utente loggato**
const fetchExpenses = async () => {
  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");

  if (!username || !token) {
    console.error("Utente non autenticato!");
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/api/costs?username=${username}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Errore nel recupero delle spese");

    const data = await response.json();
    console.log("Fetched expenses:", data); // Debug log
    expenses.value = data;
    updateDashboard();
  } catch (error) {
    console.error(error);
  }
};

// **Aggiunta nuova spesa**
const addExpense = async () => {
  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");

  if (!newExpense.value.tipologia || !newExpense.value.importo) {
    alert("Compila tutti i campi!");
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/api/costs?username=${username}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        importo: newExpense.value.importo,
        tipologia: newExpense.value.tipologia,
      }),
    });

    if (response.ok) {
      fetchExpenses();
      newExpense.value = { tipologia: "", importo: null };
      showModal.value = false;
    } else {
      const errorData = await response.json();
      alert(`Errore: ${errorData.message}`);
    }
  } catch (error) {
    console.error("Errore nell'aggiunta della spesa:", error);
  }
};

// **Eliminazione spesa**
const deleteExpense = async (id) => {
  const token = localStorage.getItem("token");

  try {
    const response = await fetch(`http://localhost:8080/api/costs/${id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    });

    if (response.ok) {
      expenses.value = expenses.value.filter((expense) => expense.costId !== id);
      updateDashboard();
    } else {
      console.error("Errore nella cancellazione della spesa");
    }
  } catch (error) {
    console.error("Errore nella cancellazione della spesa:", error);
  }
};

// **Aggiornamento delle statistiche**
const updateDashboard = () => {
  let total = expenses.value.reduce((sum, e) => sum + e.importo, 0);
  totalSpent.value = total.toFixed(2);
  averageSpent.value = expenses.value.length > 0 ? (total / expenses.value.length).toFixed(2) : 0;
  lastSpent.value = expenses.value.length > 0 ? `€${expenses.value[expenses.value.length - 1].importo.toFixed(2)}` : "N/D";
};

// **Avvio il fetch delle spese al montaggio della pagina**
onMounted(fetchExpenses);
</script>

<style scoped>
.container {
  max-width: 800px;
}
.modal {
  background: rgba(0, 0, 0, 0.5);
}
</style>

<template>
  <div class="dashboard-wrapper d-flex flex-column min-vh-100">
    <div class="container mt-5 d-flex flex-column align-items-center">
      <!-- Titolo -->
      <h1 class="mb-4 text-center">
        <i class="fas fa-wallet text-primary"></i> Gestione Budget
      </h1>

      <!-- Sezione Budget -->
      <div class="budget-card shadow rounded-4 p-4 mb-4 w-100 text-center">
        <h4 class="mb-3">Il tuo Budget</h4>
        <p v-if="budget === null" class="display-6 text-muted">
          <em>Nessun budget registrato</em>
        </p>
        <p v-else class="display-3 fw-bold text-success">
          € {{ budget.amount.toFixed(2) }}
        </p>
        <button class="btn btn-success btn-lg mt-3" @click="showModal = true">
          <i class="fas fa-plus-circle me-2"></i> Ricarica Budget
        </button>
      </div>

      <!-- Pulsanti azione -->
      <div class="d-flex flex-column align-items-center w-100">
        <router-link to="/gestione-spese" class="btn btn-outline-primary btn-lg w-75 mb-3">
          <i class="fas fa-money-check-alt me-2"></i> Vai a Gestione Spese
        </router-link>
        <button class="btn btn-warning btn-lg w-75" @click="salvadanaioFittizio">
          <i class="fas fa-piggy-bank me-2"></i> Salvadanaio (In arrivo)
        </button>
      </div>

      <!-- Modale Ricarica Budget -->
      <div v-if="showModal" class="modal fade show" tabindex="-1" style="display: block;">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Ricarica il tuo Budget</h5>
              <button type="button" class="btn-close" @click="showModal = false"></button>
            </div>
            <div class="modal-body">
              <div class="mb-3">
                <label class="form-label">Importo da aggiungere</label>
                <input
                  type="number"
                  v-model.number="addAmount"
                  class="form-control"
                  placeholder="Inserisci importo (€)"
                  min="0.01"
                  step="0.01"
                />
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-secondary" @click="showModal = false">Annulla</button>
              <button class="btn btn-primary" @click="addFunds">
                <i class="fas fa-check"></i> Conferma Ricarica
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
import { ref, onMounted } from "vue";

const budget = ref(null);
const addAmount = ref(0);
const showModal = ref(false);

/** Recupera il budget dal backend */
async function fetchBudget() {
  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");

  if (!username || !token) {
    console.error("Utente non autenticato!");
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/api/budget?username=${username}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) throw new Error("Errore nel recupero del budget");
    const data = await response.json();
    budget.value = data ?? null;
  } catch (error) {
    console.error(error);
  }
}

/** Aggiunge fondi al budget */
async function addFunds() {
  if (addAmount.value <= 0) {
    alert("Inserisci un importo positivo");
    return;
  }

  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");

  try {
    const response = await fetch(
      `http://localhost:8080/api/budget/add?username=${username}&amount=${addAmount.value}`,
      {
        method: "PUT",
        headers: { Authorization: `Bearer ${token}` },
      }
    );

    if (!response.ok) throw new Error("Errore nell'aggiunta fondi");
    const updatedBudget = await response.json();
    budget.value = updatedBudget;
    alert(`✅ Fondi aggiunti con successo! Nuovo budget: €${updatedBudget.amount.toFixed(2)}`);
    addAmount.value = 0;
    showModal.value = false;
  } catch (error) {
    console.error(error);
    alert("❌ Impossibile aggiungere fondi.");
  }
}

/** Pulsante fittizio per salvadanaio */
function salvadanaioFittizio() {
  alert("💡 La funzione Salvadanaio sarà disponibile in futuro. Stay tuned!");
}

onMounted(() => {
  fetchBudget();
});
</script>

<style scoped>
/* Wrapper per far stare il footer in fondo alla pagina */
.dashboard-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.container {
  max-width: 700px;
}

.budget-card {
  background-color: #f8f9fa;
  border: 2px solid #e0e0e0;
  border-radius: 15px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}

.budget-card:hover {
  transform: translateY(-5px) scale(1.02);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
}

.display-3 {
  font-size: 3rem;
}

.btn {
  border-radius: 12px;
  font-size: 1.2rem;
}

.btn-warning {
  color: #fff;
  background-color: #ffc107;
}

.btn-warning:hover {
  background-color: #e0a800;
}

/* Modale */
.modal {
  background: rgba(0, 0, 0, 0.5);
}

.modal-content {
  border-radius: 16px;
}

.modal-header .btn-close {
  font-size: 1.2rem;
}

/* Footer */
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
</style>

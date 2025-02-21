<template>
    <div class="container mt-5">
      <!-- Titolo e pulsante "Vai a Gestione Spese" -->
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="mb-0">
          <i class="fas fa-wallet"></i> Gestione Budget
        </h1>
        <router-link
          to="/gestione-spese"
          class="btn btn-outline-primary d-flex align-items-center"
        >
          <i class="fas fa-money-check me-2"></i>
          Vai a Gestione Spese
        </router-link>
      </div>
  
      <div class="row">
        <!-- Colonna sinistra: Mostra il budget dell'utente -->
        <div class="col-md-6 mb-4">
          <div class="card shadow-sm">
            <div class="card-body">
              <h4 class="card-title mb-3">Il tuo Budget</h4>
              <p v-if="budget === null">
                <em>Nessun budget registrato</em>
              </p>
              <p v-else class="display-6">
                € {{ budget.amount.toFixed(2) }}
              </p>
            </div>
          </div>
        </div>
  
        <!-- Colonna destra: ricarica con Carta o Google Pay -->
        <div class="col-md-6 mb-4">
          <div class="card shadow-sm">
            <div class="card-body">
              <h4 class="card-title mb-3">Ricarica il tuo Budget</h4>
              <p class="text-secondary">Scegli un metodo di pagamento e ricarica i tuoi fondi:</p>
              <div class="d-grid gap-2 mb-3">
                <button
                  class="btn btn-warning btn-lg"
                  @click="selectPaymentMethod('card')"
                >
                  <i class="fas fa-credit-card me-2"></i> Ricarica con Carta
                </button>
                <button
                  class="btn btn-success btn-lg"
                  @click="selectPaymentMethod('google')"
                >
                  <i class="fab fa-google-pay me-2"></i> Ricarica con Google Pay
                </button>
              </div>
              
              <!-- Se l'utente ha scelto un metodo di pagamento, mostriamo il form in-page -->
              <div v-if="paymentMethod" class="bg-light p-3 rounded">
                <p class="mb-3 fw-bold">
                  Stai ricaricando tramite 
                  <span v-if="paymentMethod === 'card'">Carta</span>
                  <span v-else-if="paymentMethod === 'google'">Google Pay</span>
                </p>
                
                <!-- Importo -->
                <div class="mb-3">
                  <label class="form-label">Importo da ricaricare</label>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    class="form-control"
                    v-model.number="addAmount"
                  />
                </div>
  
                <!-- Dati di pagamento "finti" -->
                <div v-if="paymentMethod === 'card'">
                  <label class="form-label">Numero Carta</label>
                  <input 
                    type="text" 
                    class="form-control mb-2"
                    placeholder="xxxx-xxxx-xxxx-xxxx"
                  />
                  <div class="row">
                    <div class="col">
                      <label class="form-label">Scadenza</label>
                      <input 
                        type="text" 
                        class="form-control" 
                        placeholder="MM/YY" 
                      />
                    </div>
                    <div class="col">
                      <label class="form-label">CVV</label>
                      <input
                        type="text"
                        class="form-control"
                        placeholder="123"
                      />
                    </div>
                  </div>
                </div>
                
                <div v-else-if="paymentMethod === 'google'">
                  <label class="form-label">Account Google</label>
                  <input
                    type="email"
                    class="form-control"
                    placeholder="nome@gmail.com"
                  />
                </div>
  
                <!-- Pulsanti per completare o annullare la ricarica -->
                <div class="mt-3 d-flex gap-2">
                  <button
                    class="btn btn-secondary"
                    @click="cancelPayment"
                  >
                    Annulla
                  </button>
                  <button
                    class="btn btn-primary"
                    @click="addFunds"
                  >
                    Conferma Ricarica
                  </button>
                </div>
              </div>
              <!-- Fine del form se l'utente ha scelto un metodo -->
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from "vue";
  
  const budget = ref(null);          // budget dell'utente
  const addAmount = ref(0);          // importo da ricaricare
  const paymentMethod = ref(null);   // 'card' o 'google' (null se non scelto)
  
  onMounted(() => {
    fetchBudget();
  });
  
  /**
   * Recupera il budget dal backend
   */
  async function fetchBudget() {
    const username = localStorage.getItem("username");
    const token = localStorage.getItem("token");
  
    if (!username || !token) {
      console.error("Utente non autenticato!");
      return;
    }
  
    try {
      const response = await fetch(
        `http://localhost:8080/api/budget?username=${username}`, 
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (!response.ok) {
        throw new Error("Errore nel recupero del budget");
      }
      const data = await response.json();
      // Se il backend ritorna "Nessun budget trovato..." data potrebbe essere una stringa
      if (typeof data === "object" && data !== null) {
        budget.value = data;
      } else {
        budget.value = null;
      }
    } catch (error) {
      console.error(error);
    }
  }
  
  /**
   * L'utente seleziona un metodo di pagamento
   */
  function selectPaymentMethod(method) {
    paymentMethod.value = method; // 'card' o 'google'
    addAmount.value = 0;          // reset importo
  }
  
  /**
   * L'utente annulla la ricarica
   */
  function cancelPayment() {
    paymentMethod.value = null;
    addAmount.value = 0;
  }
  
  /**
   * Aggiunge fondi al budget (PUT /api/budget/add)
   */
  async function addFunds() {
    if (addAmount.value <= 0) {
      alert("Inserisci un importo positivo");
      return;
    }
  
    const username = localStorage.getItem("username");
    const token = localStorage.getItem("token");
    if (!username || !token) {
      alert("Utente non autenticato.");
      return;
    }
  
    try {
      const response = await fetch(
        `http://localhost:8080/api/budget/add?username=${username}&amount=${addAmount.value}`,
        {
          method: "PUT",
          headers: { Authorization: `Bearer ${token}` }
        }
      );
  
      if (!response.ok) {
        throw new Error("Errore nell'aggiunta fondi");
      }
      const updatedBudget = await response.json();
      budget.value = updatedBudget;
      addAmount.value = 0;
  
      // Reset della "schermata pagamento"
      paymentMethod.value = null;
  
      alert(
        `Fondi aggiunti con successo! Il tuo nuovo budget è € ${updatedBudget.amount.toFixed(2)}`
      );
    } catch (error) {
      console.error(error);
      alert("Impossibile aggiungere fondi.");
    }
  }
  </script>
  
  <style scoped>
  .container {
    max-width: 900px;
  }
  </style>
  
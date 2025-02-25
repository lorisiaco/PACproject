<template>
  <div class="dashboard-wrapper d-flex flex-column min-vh-100">
    <div class="container mt-5 d-flex flex-column align-items-center">
      <!-- Titolo -->
      <h1 class="mb-4 text-center page-title">
        <i class="fas fa-wallet text-primary"></i> Gestione Budget
      </h1>

      <!-- Sezione Budget -->
      <div class="budget-card shadow-sm rounded-4 p-4 mb-4 w-100 text-center">
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

      <!-- Sezione Risparmi -->
      <div class="savings-card shadow-sm rounded-4 p-4 mb-4 w-100 text-center">
        <h4 class="mb-3">I tuoi Risparmi</h4>
        <div v-if="savings.length === 0">
          <p class="text-muted"><em>Nessun risparmio creato</em></p>
        </div>

        <!-- Griglia di "card" quadrate -->
        <div v-else class="savings-container d-flex flex-wrap justify-content-center">
          <div
            v-for="saving in savings"
            :key="saving.id"
            class="saving-card position-relative overflow-hidden"
            :style="{
                      backgroundImage: `url('${saving.imageUrl}')`,
                    }"
          >
            <!-- Overlay sfumato -->
            <div class="saving-overlay d-flex flex-column justify-content-end align-items-start text-white p-3">
              <h5 class="mb-1">{{ saving.name }}</h5>
              <p class="mb-2 fw-bold">€ {{ saving.amount.toFixed(2) }}</p>
              <div class="d-flex">
                <button class="btn btn-sm btn-success me-2" @click="openAddModal(saving)">
                  Aggiungi
                </button>
                <button class="btn btn-sm btn-warning me-2" @click="openRemoveModal(saving)">
                  Togli
                </button>
                <button class="btn btn-sm btn-danger" @click="deleteSaving(saving)">
                  Cancella
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Pulsante per aggiungere un nuovo risparmio -->
        <button class="btn btn-primary mt-4" @click="showSavingModal = true">
          <i class="fas fa-plus-circle me-2"></i> Aggiungi Risparmio
        </button>
      </div>

      <!-- Pulsanti azione -->
      <div class="d-flex flex-column align-items-center w-100">
        <router-link to="/gestione-spese" class="btn btn-outline-primary btn-lg w-75 mb-3">
          <i class="fas fa-money-check-alt me-2"></i> Vai a Gestione Spese
        </router-link>
        <!-- Rimosso il pulsante Salvadanaio -->
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

      <!-- Modale Aggiungi Risparmio (creation) -->
      <div v-if="showSavingModal" class="modal fade show" tabindex="-1" style="display: block;">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Crea un Risparmio</h5>
              <button type="button" class="btn-close" @click="showSavingModal = false"></button>
            </div>
            <div class="modal-body">
              <div class="mb-3">
                <label class="form-label">Nome Risparmio</label>
                <input type="text" v-model="newSavingName" class="form-control" placeholder="Esempio: Vacanza in Egitto" />
              </div>
              <div class="mb-3">
                <label class="form-label">Importo da accantonare</label>
                <input
                  type="number"
                  v-model.number="newSavingAmount"
                  class="form-control"
                  placeholder="Inserisci importo (€)"
                  min="0.01"
                  step="0.01"
                />
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-secondary" @click="showSavingModal = false">Annulla</button>
              <button class="btn btn-primary" @click="createSaving">
                <i class="fas fa-check"></i> Conferma
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Modale per Aggiungere fondi a un Risparmio -->
      <div v-if="showAddFundsModal" class="modal fade show" tabindex="-1" style="display: block;">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Aggiungi fondi al Risparmio: {{ selectedSaving.name }}</h5>
              <button type="button" class="btn-close" @click="showAddFundsModal = false"></button>
            </div>
            <div class="modal-body">
              <div class="mb-3">
                <label class="form-label">Importo da aggiungere</label>
                <input
                  type="number"
                  v-model.number="savingAddAmount"
                  class="form-control"
                  placeholder="Inserisci importo (€)"
                  min="0.01"
                  step="0.01"
                />
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-secondary" @click="showAddFundsModal = false">Annulla</button>
              <button class="btn btn-primary" @click="addFundsToSaving">
                <i class="fas fa-check"></i> Conferma
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Modale per Togliere fondi da un Risparmio -->
      <div v-if="showRemoveFundsModal" class="modal fade show" tabindex="-1" style="display: block;">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Togli fondi dal Risparmio: {{ selectedSaving.name }}</h5>
              <button type="button" class="btn-close" @click="showRemoveFundsModal = false"></button>
            </div>
            <div class="modal-body">
              <div class="mb-3">
                <label class="form-label">Importo da togliere</label>
                <input
                  type="number"
                  v-model.number="savingRemoveAmount"
                  class="form-control"
                  placeholder="Inserisci importo (€)"
                  min="0.01"
                  step="0.01"
                />
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-secondary" @click="showRemoveFundsModal = false">Annulla</button>
              <button class="btn btn-primary" @click="removeFundsFromSaving">
                <i class="fas fa-check"></i> Conferma
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

const savings = ref([]);
const newSavingName = ref("");
const newSavingAmount = ref(0);
const showSavingModal = ref(false);

// Variabili per modali di aggiunta/rimozione fondi
const selectedSaving = ref(null);
const savingAddAmount = ref(0);
const savingRemoveAmount = ref(0);
const showAddFundsModal = ref(false);
const showRemoveFundsModal = ref(false);

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

/** Funzione per cercare un'immagine su Unsplash correlata al nome del risparmio */
async function fetchImageForSaving(savingName) {
  try {
    const accessKey = 'aAjtZBBgYvVCnwy67pE7oPKPdTMoWuE50He7HQ0n1HY';
    // Esempio di chiamata: prendiamo 1 sola foto
    const response = await fetch(
      `https://api.unsplash.com/search/photos?query=${encodeURIComponent(savingName)}&per_page=1&client_id=${accessKey}`
    );
    const data = await response.json();

    // Se troviamo almeno 1 risultato, restituiamo l'URL "regular"
    if (data.results && data.results.length > 0) {
      return data.results[0].urls.regular;
    } else {
      // Se non ci sono foto, usiamo un placeholder
      return 'https://via.placeholder.com/350x220?text=Nessuna+foto+trovata';
    }
  } catch (error) {
    console.error('Errore fetch immagine:', error);
    // Se qualcosa va storto, usiamo un placeholder
    return 'https://via.placeholder.com/350x220?text=Errore+caricamento';
  }
}


/** Recupera i risparmi dal backend */
async function fetchSavings() {
  const username = localStorage.getItem("username");
  if (!username) return;

  try {
    const response = await fetch(`http://localhost:8080/api/savings?username=${username}`);
    if (!response.ok) throw new Error("Errore nel recupero dei risparmi");
    
    // Array base di risparmi
    const rawSavings = await response.json();

    // Per ogni risparmio, cerchiamo un'immagine su Unsplash in base al nome
    const updatedSavings = await Promise.all(
      rawSavings.map(async (saving) => {
        const imageUrl = await fetchImageForSaving(saving.name);
        // Ritorna l'oggetto risparmio con l'URL immagine incluso
        return { ...saving, imageUrl };
      })
    );

    // Infine, aggiorniamo la reattività di "savings"
    savings.value = updatedSavings;
  } catch (error) {
    console.error(error);
  }
}


/** Crea un nuovo risparmio */
async function createSaving() {
  if (!newSavingName.value || newSavingAmount.value <= 0) {
    alert("Inserisci un nome e un importo positivo.");
    return;
  }
  const username = localStorage.getItem("username");
  try {
    const response = await fetch(
      `http://localhost:8080/api/savings/create?username=${username}&name=${encodeURIComponent(
        newSavingName.value
      )}&amount=${newSavingAmount.value}`,
      {
        method: "POST",
      }
    );
    if (!response.ok) throw new Error("Errore nella creazione del risparmio");
    await fetchSavings();
    await fetchBudget();
    newSavingName.value = "";
    newSavingAmount.value = 0;
    showSavingModal.value = false;
    alert("✅ Risparmio creato con successo!");
  } catch (error) {
    console.error(error);
    alert("❌ Errore nella creazione del risparmio.");
  }
}

/** Apre il modal per aggiungere fondi al risparmio selezionato */
function openAddModal(saving) {
  selectedSaving.value = saving;
  savingAddAmount.value = 0;
  showAddFundsModal.value = true;
}

/** Aggiunge fondi al risparmio selezionato */
async function addFundsToSaving() {
  if (savingAddAmount.value <= 0) {
    alert("Inserisci un importo positivo!");
    return;
  }
  const username = localStorage.getItem("username");
  try {
    const response = await fetch(
      `http://localhost:8080/api/savings/addFunds?username=${username}&savingId=${selectedSaving.value.id}&amount=${savingAddAmount.value}`,
      {
        method: "PUT",
      }
    );
    if (!response.ok) throw new Error("Errore nell'aggiunta fondi al risparmio");
    await fetchSavings();
    await fetchBudget();
    showAddFundsModal.value = false;
    alert("✅ Fondi aggiunti al risparmio!");
  } catch (error) {
    console.error(error);
    alert("❌ Errore nell'aggiornamento del risparmio.");
  }
}

/** Apre il modal per togliere fondi dal risparmio selezionato */
function openRemoveModal(saving) {
  selectedSaving.value = saving;
  savingRemoveAmount.value = 0;
  showRemoveFundsModal.value = true;
}

/** Rimuove fondi dal risparmio selezionato */
async function removeFundsFromSaving() {
  if (savingRemoveAmount.value <= 0) {
    alert("Inserisci un importo positivo!");
    return;
  }
  const username = localStorage.getItem("username");
  try {
    const response = await fetch(
      `http://localhost:8080/api/savings/removeFunds?username=${username}&savingId=${selectedSaving.value.id}&amount=${savingRemoveAmount.value}`,
      {
        method: "PUT",
      }
    );
    if (!response.ok) throw new Error("Errore nella rimozione dei fondi dal risparmio");
    await fetchSavings();
    await fetchBudget();
    showRemoveFundsModal.value = false;
    alert("✅ Fondi rimossi dal risparmio!");
  } catch (error) {
    console.error(error);
    alert("❌ Errore nell'aggiornamento del risparmio.");
  }
}

/** Elimina un risparmio e riporta i fondi al Budget */
async function deleteSaving(saving) {
  if (
    !confirm(
      `Confermi la cancellazione del risparmio "${saving.name}"? I fondi saranno restituiti al tuo budget.`
    )
  )
    return;

  const username = localStorage.getItem("username");
  try {
    const response = await fetch(
      `http://localhost:8080/api/savings/delete?username=${username}&savingId=${saving.id}`,
      {
        method: "DELETE",
      }
    );
    if (!response.ok) throw new Error("Errore nella cancellazione del risparmio");
    await fetchSavings();
    await fetchBudget();
    alert("✅ Risparmio eliminato!");
  } catch (error) {
    console.error(error);
    alert("❌ Errore nella cancellazione del risparmio.");
  }
}

onMounted(() => {
  fetchBudget();
  fetchSavings();
});
</script>

<style scoped>
/* Wrapper per far stare il footer in fondo alla pagina */
.dashboard-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: #f9fafb; /* Sfondo chiaro generale */
}

/* Titolo Pagina */
.page-title {
  font-weight: 600;
  font-size: 2rem;
  color: #333;
}

/* Card Budget */
.budget-card {
  background-color: #ffffff;
  border: 1px solid #eee;
  border-radius: 15px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.budget-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.06);
}

/* Card Risparmi (contenitore) */
.savings-card {
  background-color: #ffffff;
  border: 1px solid #eee;
  border-radius: 15px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.savings-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.06);
}

/* Griglia card risparmi */
.savings-container {
  gap: 1rem;
  margin-top: 1rem;
}

/* Singola card risparmio */
.saving-card {
  width: 350px;
  height: 220px;
  background-size: cover;
  background-position: center;
  border-radius: 15px;
  margin: 0.5rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.saving-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
}

/* Overlay sfumato per testo su immagine */
.saving-overlay {
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.0) 20%, rgba(0, 0, 0, 0.7) 100%);
  border-radius: 15px;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
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

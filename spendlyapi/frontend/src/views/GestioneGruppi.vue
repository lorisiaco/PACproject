<template>
  <div>
    <!-- CONTENITORE PRINCIPALE -->
    <div class="container mt-5 p-4">
      <!-- STATISTICHE (in ordine: 1. Numero Gruppi (blu), 2. Membri Medi (verde), 3. Ultimo Gruppo (giallo)) -->
      <div class="row mb-4">
        <!-- 1) Numero Gruppi (BLU) -->
        <div class="col-md-4">
          <div class="card result-card card-gruppi-attivi shadow-sm text-center p-3">
            <div class="card-body">
              <h3><i class="fas fa-users"></i> Numero Gruppi</h3>
              <p class="display-6">{{ totalGroups }}</p>
            </div>
          </div>
        </div>

        <!-- 2) Membri Medi (VERDE) -->
        <div class="col-md-4">
          <div class="card result-card card-ultima-spesa shadow-sm text-center p-3">
            <div class="card-body">
              <h3><i class="fas fa-user-friends"></i> Membri Medi</h3>
              <p class="display-6">{{ averageMembers }}</p>
            </div>
          </div>
        </div>

        <!-- 3) Ultimo Gruppo (GIALLO) -->
        <div class="col-md-4">
          <div class="card result-card card-totale-spese shadow-sm text-center p-3">
            <div class="card-body">
              <h3><i class="fas fa-plus"></i> Ultimo Gruppo</h3>
              <p class="display-6">{{ lastGroup }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- ELENCO DEI GRUPPI (slider orizzontale) -->
      <h2 class="text-center mb-3">Elenco Gruppi</h2>
      <div v-if="groups.length === 0" class="text-center">
        <p>Nessun gruppo registrato</p>
      </div>
      <div v-else class="groups-slider">
        <!-- Contenitore scorrevole -->
        <div class="groups-track" ref="sliderRef">
          <!-- Singola card gruppo -->
          <div v-for="group in groups" :key="group.id" class="group-card">
            <div class="card card-hover h-100">
              <img
                class="card-img-top"
                :src="`https://picsum.photos/300/200?random=${group.id}`"
                alt="Immagine casuale"
              />
              <div class="card-header">
                <h5 class="card-title mb-0">{{ group.nome }}</h5>
              </div>
              <div class="card-body">
                <p class="mb-1"><strong>ID:</strong> {{ group.id }}</p>
                <p class="mb-1">
                  <strong>Admin:</strong> {{ group.admin ? group.admin.username : 'N/D' }}
                </p>
                <p class="mb-0">
                  <strong>Membri:</strong> {{ group.membri ? group.membri.length : 0 }}
                </p>
              </div>
              <div class="card-footer d-flex">
                <div class="mx-auto">
                  <button class="btn btn-custom-detail me-2" @click="goToGroupDetail(group.id)">
                    Dettagli
                  </button>
                  <button class="btn btn-custom-delete" @click="deleteGroup(group.id)">
                    Elimina
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- Frecce di navigazione (testo < e >) -->
        <button class="arrow left" @click="scrollLeft">&lt;</button>
        <button class="arrow right" @click="scrollRight">&gt;</button>
      </div>

      <!-- Bottone per Aggiungere Gruppo -->
      <button class="btn btn-primary" @click="showModal = true">
        <i class="fas fa-plus"></i> Aggiungi Gruppo
      </button>

      <!-- Modale per Aggiungere Gruppo -->
      <div v-if="showModal" class="modal fade show" tabindex="-1" style="display: block;">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Aggiungi Gruppo</h5>
              <button type="button" class="btn-close" @click="showModal = false"></button>
            </div>
            <div class="modal-body">
              <input
                v-model="newGroup.nome"
                type="text"
                class="form-control mb-2"
                placeholder="Nome del gruppo"
                required
              />
            </div>
            <div class="modal-footer">
              <button class="btn btn-success" @click="addGroup">Salva</button>
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
import { useRouter } from "vue-router";

const router = useRouter();

/** Lista reattiva dei gruppi */
const groups = ref([]);

/** Statistiche */
const totalGroups = ref(0);
const averageMembers = ref(0);
const lastGroup = ref("N/D");

/** Stato della modale e nuovo gruppo da creare */
const showModal = ref(false);
const newGroup = ref({ nome: "" });

/** Riferimento allo slider orizzontale */
const sliderRef = ref(null);

/** Scroll a sinistra di 300px */
function scrollLeft() {
  if (sliderRef.value) {
    sliderRef.value.scrollBy({ left: -300, behavior: "smooth" });
  }
}

/** Scroll a destra di 300px */
function scrollRight() {
  if (sliderRef.value) {
    sliderRef.value.scrollBy({ left: 300, behavior: "smooth" });
  }
}

/** Al montaggio del componente, recupera i gruppi */
onMounted(() => {
  fetchGroups();
  fetchAllUsers();
});

/** Carica la lista dei gruppi */
async function fetchGroups() {
  const username = localStorage.getItem("username");
  const token = localStorage.getItem("token");

  if (!username || !token) {
    console.error("Utente non autenticato! Username o token mancante.");
    return;
  }

  try {
    const response = await fetch(
      `http://localhost:8080/api/groups?username=${username}`,
      { headers: { Authorization: `Bearer ${token}` } }
    );

    if (!response.ok) {
      throw new Error("Errore nel recupero dei gruppi");
    }

    const data = await response.json();
    groups.value = data;
    updateDashboard();
  } catch (error) {
    console.error("Errore fetchGroups:", error);
  }
}

/** Carica la lista di TUTTI gli utenti (se necessario) */
async function fetchAllUsers() {
  const token = localStorage.getItem("token");
  if (!token) {
    console.error("Token mancante. Non posso caricare gli utenti.");
    return;
  }

  try {
    const res = await fetch("http://localhost:8080/account/users", {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!res.ok) {
      throw new Error("Errore nel recupero degli utenti");
    }
    await res.json();
  } catch (err) {
    console.error("Errore fetchAllUsers:", err);
  }
}

/** Aggiunge un nuovo gruppo */
async function addGroup() {
  if (!newGroup.value.nome) {
    alert("Compila il nome del gruppo!");
    return;
  }

  const token = localStorage.getItem("token");
  const username = localStorage.getItem("username");

  if (!token || !username) {
    alert("Utente non autenticato.");
    return;
  }

  try {
    const response = await fetch(
      `http://localhost:8080/api/groups?username=${username}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ nome: newGroup.value.nome }),
      }
    );

    if (response.ok) {
      await fetchGroups();
      newGroup.value.nome = "";
      showModal.value = false;
    } else {
      const errorData = await response.json();
      alert(`Errore: ${errorData.message}`);
    }
  } catch (error) {
    console.error("Errore nella creazione del gruppo:", error);
  }
}

/** Elimina un gruppo */
async function deleteGroup(groupId) {
  const token = localStorage.getItem("token");
  const username = localStorage.getItem("username");

  if (!token || !username) {
    alert("Utente non autenticato.");
    return;
  }

  try {
    const response = await fetch(
      `http://localhost:8080/api/groups/${groupId}?username=${username}`,
      {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      }
    );

    if (response.ok) {
      fetchGroups();
    } else {
      const errorMsg = await response.text();
      alert(`Errore nella cancellazione del gruppo: ${errorMsg}`);
    }
  } catch (error) {
    console.error("Errore nella cancellazione del gruppo:", error);
  }
}

/** Naviga alla pagina dei dettagli del gruppo */
function goToGroupDetail(groupId) {
  router.push({ name: "GroupDetail", params: { groupId } });
}

/** Aggiorna le statistiche */
function updateDashboard() {
  totalGroups.value = groups.value.length;

  if (groups.value.length > 0) {
    const totalMembers = groups.value.reduce(
      (sum, g) => sum + (g.membri ? g.membri.length : 0),
      0
    );
    averageMembers.value = (totalMembers / groups.value.length).toFixed(2);

    const last = groups.value[groups.value.length - 1];
    lastGroup.value = last.nome;
  } else {
    averageMembers.value = 0;
    lastGroup.value = "N/D";
  }
}
</script>

<style scoped>
html,
body {
  height: 100%;
  margin: 0;
  padding: 0;
}
body {
  background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%);
  font-family: Arial, sans-serif;
}
.container {
  max-width: 900px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  padding: 2rem;
}
.modal {
  background: rgba(0, 0, 0, 0.5);
}
.card {
  border: none;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.4s ease-out, box-shadow 0.4s ease-out;
}
.card-hover:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.25);
}
.card-img-top {
  border-radius: 10px 10px 0 0;
  object-fit: cover;
  height: 200px;
  width: 100%;
}
.card-header {
  background-color: #f8f9fa;
  border-bottom: none;
}
.card-footer {
  background-color: #f8f9fa;
  border-top: none;
  padding: 0.75rem 1rem;
}
.card-footer .mx-auto {
  display: flex;
  justify-content: center;
}
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
.groups-slider {
  position: relative;
  overflow: hidden;
  margin-bottom: 2rem;
}
.groups-track {
  display: flex;
  gap: 1.5rem;
  overflow-x: auto;
  scroll-behavior: smooth;
  padding-bottom: 1rem;
  -ms-overflow-style: none;
  scrollbar-width: none;
}
.groups-track::-webkit-scrollbar {
  display: none;
}
.group-card {
  flex: 0 0 auto;
  width: 300px;
}
.arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: transparent;
  border: none;
  color: #333;
  font-size: 2rem;
  cursor: pointer;
  opacity: 0;
  transition: opacity 0.3s, color 0.3s;
  z-index: 10;
}
.arrow:hover {
  color: #555;
}
.groups-slider:hover .arrow {
  opacity: 1;
}
.arrow.left {
  left: 10px;
}
.arrow.right {
  right: 10px;
}
/* Footer */
.footer {
  background: #111827;
  color: white;
  text-align: center;
  padding: 1rem;
  margin-top: auto;
}
.footer-link {
  color: #1e90ff;
  text-decoration: none;
  margin-left: 0.5rem;
}
.footer-link:hover {
  text-decoration: underline;
}
</style>

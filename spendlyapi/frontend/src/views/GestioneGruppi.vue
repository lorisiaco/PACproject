<template>
  <div class="container mt-5 p-4">
    <!-- Statistiche -->
    <div class="row mb-4">
      <div class="col-md-4">
        <div class="dashboard-card bg-primary text-white p-3 rounded">
          <h3><i class="fas fa-users"></i> Numero Gruppi</h3>
          <p>{{ totalGroups }}</p>
        </div>
      </div>
      <div class="col-md-4">
        <div class="dashboard-card bg-success text-white p-3 rounded">
          <h3><i class="fas fa-user-friends"></i> Membri Medi</h3>
          <p>{{ averageMembers }}</p>
        </div>
      </div>
      <div class="col-md-4">
        <div class="dashboard-card bg-warning text-dark p-3 rounded">
          <h3><i class="fas fa-plus"></i> Ultimo Gruppo</h3>
          <p>{{ lastGroup }}</p>
        </div>
      </div>
    </div>

    <!-- Titolo centrato per l'elenco dei gruppi -->
    <h2 class="text-center mb-3">Elenco Gruppi</h2>
    <div class="row">
      <!-- Messaggio se non ci sono gruppi -->
      <div v-if="groups.length === 0" class="col-12">
        <p class="text-center">Nessun gruppo registrato</p>
      </div>

      <!-- Card per ogni gruppo -->
      <div v-for="group in groups" :key="group.id" class="col-md-4 mb-4">
        <div class="card card-hover h-100">
          <!-- Immagine casuale con Picsum -->
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
            <!-- Bottoni centrati -->
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

    <!-- Bottone per Aggiungere Gruppo con blu scuro -->
    <button class="btn btn-custom-add" @click="showModal = true">
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
            <button class="btn btn-success" @click="addGroup">
              Salva
            </button>
          </div>
        </div>
      </div>
    </div>
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
      {
        headers: { Authorization: `Bearer ${token}` },
      }
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

/** Carica la lista di TUTTI gli utenti dal DB (se necessario) */
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
        headers: {
          Authorization: `Bearer ${token}`,
        },
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
    const totalMembers = groups.value.reduce((sum, g) => {
      return sum + (g.membri ? g.membri.length : 0);
    }, 0);
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
/* Impostazioni base per html e body */
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}

/* Sfondo con gradiente intenso */
body {
  background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%);
  font-family: 'Arial', sans-serif;
}

/* Contenitore principale con fondo bianco e box-shadow */
.container {
  max-width: 900px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  padding: 2rem;
}

/* Modale semi-trasparente */
.modal {
  background: rgba(0, 0, 0, 0.5);
}

/* Card statistiche */
.dashboard-card {
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-radius: 8px;
}

/* Stili per le card dei gruppi */
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

/* Centra i bottoni nel footer della card */
.card-footer .mx-auto {
  display: flex;
  justify-content: center;
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

/* Bottone Aggiungi Gruppo in blu scuro personalizzato */
.btn-custom-add {
  background-color: #003366;
  color: #fff;
  border: none;
  padding: 0.65rem 1.5rem;
  border-radius: 5px;
  font-size: 1rem;
  transition: background-color 0.3s ease;
  margin-top: 1.5rem;
}

.btn-custom-add:hover {
  background-color: #002244;
}
</style>

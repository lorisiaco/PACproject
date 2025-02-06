<template>
    <div class="container mt-5">
      <h1 class="mb-4"><i class="fas fa-users"></i> Gestione Gruppi</h1>
  
      <!-- Statistiche -->
      <div class="row">
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
  
      <!-- Tabella Gruppi -->
      <h2 class="mt-4">Elenco Gruppi</h2>
      <table class="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Admin</th>
            <th>Membri</th>
            <th>Azioni</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="groups.length === 0">
            <td colspan="5" class="text-center">Nessun gruppo registrato</td>
          </tr>
          <tr v-for="group in groups" :key="group.id">
            <td>{{ group.id }}</td>
            <td>{{ group.nome }}</td>
            <td>{{ group.admin ? group.admin.username : 'N/D' }}</td>
            <td>
              <!-- Mostra il numero di membri oppure 0 se non c'è una lista definita -->
              {{ group.membri ? group.membri.length : 0 }}
            </td>
            <td>
              <button class="btn btn-danger btn-sm" @click="deleteGroup(group.id)">
                Elimina
              </button>
            </td>
          </tr>
        </tbody>
      </table>
  
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
  </template>
  
  <script setup>
  import { ref, onMounted } from "vue";
  
  // Reactive references
  const groups = ref([]);
  const totalGroups = ref(0);
  const averageMembers = ref(0);
  const lastGroup = ref("N/D");
  const showModal = ref(false);
  
  const newGroup = ref({
    nome: ""
  });
  
  // 1. Funzione per recuperare i gruppi dal backend
  const fetchGroups = async () => {
    const username = localStorage.getItem("username");
    const token = localStorage.getItem("token");
  
    if (!username || !token) {
      console.error("Utente non autenticato! Username o token mancante.");
      return;
    }
  
    try {
      const response = await fetch(`http://localhost:8080/api/groups?username=${username}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
  
      if (!response.ok) {
        throw new Error("Errore nel recupero dei gruppi");
      }
  
      const data = await response.json();
      groups.value = data;
      updateDashboard();
    } catch (error) {
      console.error(error);
    }
  };
  
  // 2. Funzione per aggiungere un nuovo gruppo
  const addGroup = async () => {
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
      const response = await fetch(`http://localhost:8080/api/groups?username=${username}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({ nome: newGroup.value.nome })
      });
  
      if (response.ok) {
        // Ricarica i gruppi e resetta il form
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
  };
  
  // 3. Funzione per eliminare un gruppo
  const deleteGroup = async (groupId) => {
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("username");
  
    if (!token || !username) {
      alert("Utente non autenticato.");
      return;
    }
  
    try {
      // La logica presuppone che solo l'admin possa eliminare il proprio gruppo
      const response = await fetch(`http://localhost:8080/api/groups/${groupId}?username=${username}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
  
      if (response.ok) {
        fetchGroups();
      } else {
        const errorMsg = await response.text();
        alert(`Errore nella cancellazione del gruppo: ${errorMsg}`);
      }
    } catch (error) {
      console.error("Errore nella cancellazione del gruppo:", error);
    }
  };
  
  // 4. Funzione per aggiornare le statistiche
  const updateDashboard = () => {
    // Numero totale di gruppi
    totalGroups.value = groups.value.length;
  
    // Numero medio di membri per gruppo
    if (groups.value.length > 0) {
      const totalMembers = groups.value.reduce((sum, g) => {
        return sum + (g.membri ? g.membri.length : 0);
      }, 0);
      averageMembers.value = (totalMembers / groups.value.length).toFixed(2);
    } else {
      averageMembers.value = 0;
    }
  
    // Ultimo gruppo creato (se esistono gruppi, prendiamo l'ultimo dall'array)
    if (groups.value.length > 0) {
      const last = groups.value[groups.value.length - 1];
      lastGroup.value = last.nome;
    } else {
      lastGroup.value = "N/D";
    }
  };
  
  // 5. Al mount iniziale, carica i gruppi
  onMounted(fetchGroups);
  </script>
  
  <style scoped>
  .container {
    max-width: 800px;
  }
  /* Sovrascrive sfondo quando appare la modale come nel caso "show" */
  .modal {
    background: rgba(0, 0, 0, 0.5);
  }
  .dashboard-card {
    min-height: 120px;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
  </style>
  
<template>
  <div class="container mt-4">
    <h2>Dettaglio Gruppo: {{ group.nome }}</h2>

    <!-- Elenco membri -->
    <div v-if="group.membri && group.membri.length > 0">
      <h4 class="mt-3">Membri</h4>
      <ul>
        <li
          v-for="member in group.membri"
          :key="member.id"
          class="d-flex align-items-center"
        >
          <span>{{ member.username }}</span>
          <!-- Bottone rimozione membro -->
          <button class="btn btn-danger btn-sm ms-3" @click="removeMember(member.username)">
            Rimuovi
          </button>
        </li>
      </ul>
    </div>
    <div v-else>
      <p>Nessun membro presente in questo gruppo.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const groupId = route.params.groupId // Questo è :groupId

// Stato del gruppo
const group = ref({})

// Al caricamento del componente, facciamo la GET /api/groups/:groupId
onMounted(() => {
  fetchGroup()
})

async function fetchGroup() {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      alert('Token mancante. Devi essere autenticato.')
      return
    }
    const res = await fetch(`http://localhost:8080/api/groups/${groupId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (!res.ok) {
      throw new Error('Errore nel recupero del gruppo.')
    }
    const data = await res.json()
    group.value = data
  } catch (error) {
    console.error(error)
    alert('Impossibile caricare il dettaglio del gruppo.')
  }
}

/**
 * Rimuovi un membro dal gruppo tramite:
 * DELETE /api/groups/{groupId}/members?adminUsername=...&memberUsername=...
 */
async function removeMember(memberUsername) {
  try {
    const token = localStorage.getItem('token')
    const adminUsername = localStorage.getItem('username')

    if (!token || !adminUsername) {
      alert('Utente non autenticato o token mancante.')
      return
    }
    const url = `http://localhost:8080/api/groups/${groupId}/members?adminUsername=${adminUsername}&memberUsername=${memberUsername}`

    const response = await fetch(url, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    if (!response.ok) {
      const errorMsg = await response.text()
      throw new Error(errorMsg)
    }

    // Ricarica i dati del gruppo per vedere l'effetto
    fetchGroup()
  } catch (error) {
    alert('Errore nella rimozione del membro: ' + error.message)
    console.error('removeMember:', error)
  }
}
</script>

<style scoped>
.container {
  max-width: 800px;
}
</style>

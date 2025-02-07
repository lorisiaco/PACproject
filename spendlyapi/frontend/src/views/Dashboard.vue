<template>
  <div class="dashboard-page">
    <!-- Header -->
    <header class="dashboard-header">
      <h1>La Tua Dashboard Finanziaria</h1>
    </header>

    <!-- Statistiche principali -->
    <div class="stats-container">
      <!-- Totale Spese -->
      <div class="stat-card red">
        <h2>Totale Spese</h2>
        <p>€{{ totalExpenses }}</p>
        <span>Aggiornato a Oggi</span>
      </div>

      <!-- Gruppi Attivi -->
      <div class="stat-card blue">
        <h2>Gruppi Attivi</h2>
        <p>{{ activeGroups }}</p>
        <span>Totale gruppi</span>
      </div>

      <!-- Ultima Spesa -->
      <div class="stat-card green">
        <h2>Ultima Spesa</h2>
        <p>€{{ lastExpense }}</p>
        <span>Data ultima registrazione</span>
      </div>
    </div>

    <!-- Grafici -->
    <div class="charts-container">
      <div class="chart">
        <h3>Andamento delle Spese (Linea)</h3>
        <canvas id="lineChart"></canvas>
      </div>
      <div class="chart">
        <h3>Spese per Categoria (Istogramma)</h3>
        <canvas id="barChart"></canvas>
      </div>
      <div class="chart">
        <h3>Distribuzione Spese (Torta)</h3>
        <canvas id="pieChart"></canvas>
      </div>
    </div>

    <!-- Azioni rapide (Esempio) -->
    <div class="quick-actions">
      <div class="action-card">
        <h4>📊 Spese Personali</h4>
        <p>Gestisci le tue spese individuali.</p>
      </div>
      <div class="action-card">
        <h4>⚠️ Alert di Spesa</h4>
        <p>Ricevi notifiche quando superi i tuoi limiti.</p>
      </div>
      <div class="action-card">
        <h4>💰 Budget</h4>
        <p>Monitora e pianifica il tuo budget mensile.</p>
      </div>
    </div>
  </div>
</template>

<script>
import Chart from "chart.js/auto";

export default {
  name: "Dashboard",
  data() {
    return {
      totalExpenses: 0,    // Somma di tutte le spese
      activeGroups: 0,     // Numero di gruppi attivi
      lastExpense: 0,      // Importo dell'ultima spesa
      token: null,
      username: null,
    };
  },
  methods: {
    /**
     * Recupera la lista dei gruppi e imposta activeGroups
     */
    async fetchActiveGroups() {
      this.token = localStorage.getItem("token");
      this.username = localStorage.getItem("username");

      if (!this.token || !this.username) {
        console.error("Utente non autenticato!");
        return;
      }

      try {
        const res = await fetch(
          `http://localhost:8080/api/groups?username=${this.username}`,
          {
            headers: { Authorization: `Bearer ${this.token}` }
          }
        );
        if (!res.ok) {
          throw new Error("Errore nel recupero dei gruppi");
        }
        const data = await res.json();
        this.activeGroups = data.length;
      } catch (error) {
        console.error("Errore fetchActiveGroups:", error);
      }
    },

    /**
     * Recupera la lista delle spese e imposta totalExpenses e lastExpense
     */
    async fetchTotalExpenses() {
      this.token = localStorage.getItem("token");
      this.username = localStorage.getItem("username");

      if (!this.token || !this.username) {
        console.error("Utente non autenticato!");
        return;
      }

      try {
        const res = await fetch(
          `http://localhost:8080/api/costs?username=${this.username}`,
          {
            headers: { Authorization: `Bearer ${this.token}` }
          }
        );
        if (!res.ok) {
          throw new Error("Errore nel recupero delle spese");
        }
        const costs = await res.json();

        // Calcola la somma
        let sum = 0;
        costs.forEach((c) => {
          sum += c.importo || 0; // c.importo deve essere numerico
        });
        this.totalExpenses = sum.toFixed(2);

        // Imposta ultima spesa (in base all'ultima presente nell'array)
        if (costs.length > 0) {
          // supponendo che l'ultima spesa sia l'ultimo elemento
          const last = costs[costs.length - 1];
          this.lastExpense = last.importo.toFixed(2);
        } else {
          this.lastExpense = 0;
        }
      } catch (error) {
        console.error("Errore fetchTotalExpenses:", error);
      }
    },

    /**
     * Renderizza i grafici con Chart.js
     */
    renderCharts() {
      const lineChartCtx = document.getElementById("lineChart").getContext("2d");
      new Chart(lineChartCtx, {
        type: "line",
        data: {
          labels: ["Gen", "Feb", "Mar", "Apr", "Mag", "Giu", "Lug", "Ago", "Set", "Ott", "Nov", "Dic"],
          datasets: [
            {
              label: "Spese (€)",
              data: [200, 300, 250, 400, 450, 500, 600, 700, 650, 800, 850, 900],
              borderColor: "#4f46e5",
              fill: true,
              backgroundColor: "rgba(79, 70, 229, 0.1)",
            },
          ],
        },
      });

      const barChartCtx = document.getElementById("barChart").getContext("2d");
      new Chart(barChartCtx, {
        type: "bar",
        data: {
          labels: ["Affitto", "Spesa Alimentare", "Trasporti", "Svago", "Bollette"],
          datasets: [
            {
              label: "Spese per Categoria (€)",
              data: [300, 250, 200, 150, 400],
              backgroundColor: ["#f87171", "#60a5fa", "#34d399", "#fbbf24", "#a78bfa"],
            },
          ],
        },
      });

      const pieChartCtx = document.getElementById("pieChart").getContext("2d");
      new Chart(pieChartCtx, {
        type: "pie",
        data: {
          labels: ["Affitto", "Spesa Alimentare", "Trasporti", "Svago", "Bollette"],
          datasets: [
            {
              data: [300, 250, 200, 150, 400],
              backgroundColor: ["#f87171", "#60a5fa", "#34d399", "#fbbf24", "#a78bfa"],
            },
          ],
        },
      });
    },
  },
  /**
   * Quando il componente è montato, richiamiamo i metodi per recuperare i dati dal backend
   * e renderizzare i grafici
   */
  mounted() {
    this.fetchActiveGroups();
    this.fetchTotalExpenses();
    this.renderCharts();
  },
};
</script>

<style scoped>
.dashboard-page {
  padding: 2rem;
  font-family: Arial, sans-serif;
  color: #1e293b;
}

.dashboard-header h1 {
  text-align: center;
  color: #1e3a8a;
}

.stats-container {
  display: flex;
  justify-content: space-around;
  margin-bottom: 2rem;
}

.stat-card {
  flex: 1;
  text-align: center;
  color: white;
  padding: 1.5rem;
  margin: 0 0.5rem;
  border-radius: 12px;
  background-color: white;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.stat-card.red {
  background-color: #ef4444;
}

.stat-card.blue {
  background-color: #3b82f6;
}

.stat-card.green {
  background-color: #10b981;
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
  margin-top: 2rem;
}

.chart {
  background: white;
  padding: 1rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.quick-actions {
  display: flex;
  justify-content: space-around;
  margin-top: 2rem;
}

.action-card {
  flex: 1;
  text-align: center;
  background: white;
  padding: 1rem;
  margin: 0 0.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}


</style>

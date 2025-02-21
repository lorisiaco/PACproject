<template>
  <div class="dashboard-wrapper d-flex flex-column min-vh-100">
    <!-- HERO -->
    <section class="dashboard-hero text-center text-white">
      <div class="hero-content container">
        <h1 class="hero-title display-3 fw-bold">La Tua Dashboard</h1>
        <p class="hero-subtitle lead">
          Controlla le tue spese, gestisci i gruppi e migliora le tue finanze con stile.
        </p>
      </div>
    </section>

    <!-- CONTENUTO PRINCIPALE -->
    <div class="container my-5 flex-grow-1">
      <!-- SEZIONE: RISULTATI E STATISTICHE PRINCIPALI -->
      <section class="results-section mb-5">
        <h2 class="section-title text-center mb-4">I Tuoi Risultati</h2>
        <div class="row g-4">
          <!-- Card Totale Spese (rosso) -->
          <div class="col-md-4">
            <div class="card result-card card-totale-spese shadow-sm text-center">
              <div class="card-body">
                <div class="result-icon mb-3">
                  <i class="fas fa-wallet fa-2x"></i>
                </div>
                <h3 class="fw-bold">Totale Spese</h3>
                <p class="display-6">€{{ totalExpenses }}</p>
                <small class="text-muted">Aggiornato a Oggi</small>
              </div>
            </div>
          </div>

          <!-- Card Gruppi Attivi (blu) -->
          <div class="col-md-4">
            <div class="card result-card card-gruppi-attivi shadow-sm text-center">
              <div class="card-body">
                <div class="result-icon mb-3">
                  <i class="fas fa-user-friends fa-2x"></i>
                </div>
                <h3 class="fw-bold">Gruppi Attivi</h3>
                <p class="display-6">{{ activeGroups }}</p>
                <small class="text-muted">Totale gruppi</small>
              </div>
            </div>
          </div>

          <!-- Card Ultima Spesa (verde) -->
          <div class="col-md-4">
            <div class="card result-card card-ultima-spesa shadow-sm text-center">
              <div class="card-body">
                <div class="result-icon mb-3">
                  <i class="fas fa-check-circle fa-2x"></i>
                </div>
                <h3 class="fw-bold">Ultima Spesa</h3>
                <p class="display-6">€{{ lastExpense }}</p>
                <small class="text-muted">Data ultima registrazione</small>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- SEZIONE: AZIONI VELOCI (ICONE GRANDI) -->
      <section class="quick-links mb-5">
        <h2 class="section-title text-center mb-4">Azioni Veloci</h2>
        <div class="row g-4 justify-content-center">
          <div class="col-md-3" v-for="(action, index) in quickLinks" :key="index">
            <div class="card quick-link-card text-center p-3 shadow-sm">
              <div class="link-icon mb-2">
                <i :class="action.icon + ' fa-2x'"></i>
              </div>
              <h5 class="fw-bold">{{ action.title }}</h5>
              <p class="text-muted">{{ action.desc }}</p>
              <!-- Usiamo router-link per portare l’utente alla pagina effettiva -->
              <router-link
                class="btn btn-primary mt-2"
                :to="action.route"
              >
                Vai
              </router-link>
            </div>
          </div>
        </div>
      </section>

      <!-- SEZIONE: SUGGERIMENTI DI RISPARMIO (RULLO TRASPORTATORE) -->
      <section class="savings-tips mb-5">
        <h2 class="section-title text-center mb-4">Suggerimenti di Risparmio</h2>
        <!-- Contenitore slider -->
        <div class="tips-slider">
          <div class="tips-track">
            <!-- Prima serie di card -->
            <div
              class="tip-card"
              v-for="(tip, index) in savingsTips"
              :key="index"
              :class="{'colored-card': index < 3}"
            >
              <div class="tip-icon">
                <i :class="tip.icon + ' fa-2x'"></i>
              </div>
              <h4 class="tip-title">{{ tip.title }}</h4>
              <p class="tip-text">{{ tip.text }}</p>
            </div>
            <!-- Duplichiamo le card per uno scorrimento continuo -->
            <div
              class="tip-card"
              v-for="(tip, index) in savingsTips"
              :key="'dup-' + index"
              :class="{'colored-card': index < 3}"
            >
              <div class="tip-icon">
                <i :class="tip.icon + ' fa-2x'"></i>
              </div>
              <h4 class="tip-title">{{ tip.title }}</h4>
              <p class="tip-text">{{ tip.text }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- SEZIONE: CITAZIONE MOTIVAZIONALE -->
      <section class="motivational-quote text-center mb-5">
        <blockquote class="blockquote">
          <p class="mb-3">
            "La felicità non è nel possedere, ma nel saper spendere con saggezza."
          </p>
          <footer class="blockquote-footer">
            <cite>Qualche Filosofo delle Finanze</cite>
          </footer>
        </blockquote>
      </section>
    </div>

    <!-- FOOTER -->
    <footer class="footer bg-dark text-white text-center py-3 mt-auto">
      <p class="mb-0">&copy; 2025 Spendly. Tutti i diritti riservati.</p>
      <router-link to="/contact" class="footer-link">Contattaci</router-link>
    </footer>
  </div>
</template>

<script>
export default {
  name: "SpectacularDashboard",
  data() {
    return {
      totalExpenses: 0,
      activeGroups: 0,
      lastExpense: 0,
      // Suggerimenti di risparmio
      savingsTips: [
        {
          icon: "fas fa-piggy-bank",
          title: "Crea un Fondo Emergenze",
          text: "Metti da parte almeno 3 mesi di spese per affrontare imprevisti."
        },
        {
          icon: "fas fa-shopping-cart",
          title: "Compra Intelligente",
          text: "Fai una lista prima di fare la spesa per evitare acquisti impulsivi."
        },
        {
          icon: "fas fa-lightbulb",
          title: "Riduci i Consumi",
          text: "Spegni luci e dispositivi inutilizzati per abbattere i costi in bolletta."
        },
        {
          icon: "fas fa-calendar-alt",
          title: "Pianifica il Budget",
          text: "Stabilisci un budget mensile e attieniti ad esso."
        },
        {
          icon: "fas fa-ban",
          title: "Evita Debiti",
          text: "Riduci al minimo l'uso delle carte di credito e dei prestiti."
        },
        {
          icon: "fas fa-cut",
          title: "Risparmia sull'Abbonamento",
          text: "Valuta se tutti i tuoi abbonamenti sono necessari."
        },
        {
          icon: "fas fa-tag",
          title: "Usa i Coupon",
          text: "Cerca sconti e coupon per i tuoi acquisti quotidiani."
        },
        {
          icon: "fas fa-plug",
          title: "Risparmia Energia",
          text: "Scegli elettrodomestici a basso consumo energetico."
        },
        {
          icon: "fas fa-undo",
          title: "Compra di Seconda Mano",
          text: "Valuta l'acquisto di prodotti usati in buone condizioni."
        },
        {
          icon: "fas fa-utensils",
          title: "Cucina a Casa",
          text: "Risparmia cucinando piatti fatti in casa anziché ordinare fuori."
        },
        {
          icon: "fas fa-chart-line",
          title: "Risparmia per Investire",
          text: "Destina una parte delle spese a investimenti futuri."
        },
        {
          icon: "fas fa-receipt",
          title: "Monitora le Spese",
          text: "Usa app o strumenti per tenere traccia delle tue spese quotidiane."
        },
        {
          icon: "fas fa-plane",
          title: "Risparmia in Viaggi",
          text: "Cerca offerte e pianifica i tuoi viaggi con anticipo."
        },
        {
          icon: "fas fa-apple-alt",
          title: "Riduci lo Spreco Alimentare",
          text: "Compra solo ciò di cui hai veramente bisogno per evitare sprechi."
        },
        {
          icon: "fas fa-cogs",
          title: "Automatizza il Risparmio",
          text: "Imposta bonifici automatici verso un conto di risparmio."
        }
      ],
      // Sezione Azioni Veloci: 4 voci
      quickLinks: [
        {
          icon: "fas fa-wallet",
          title: "Spese Personali",
          desc: "Visualizza e modifica le tue spese",
          // rotta definita nel router come { path: '/gestione-spese', name: 'GestioneSpese' }
          route: { name: "GestioneSpese" }
        },
        {
          icon: "fas fa-user-friends",
          title: "Gruppi",
          desc: "Crea o gestisci i tuoi gruppi",
          // rotta definita come { path: '/groups', name: 'Groups' }
          route: { name: "Groups" }
        },
        {
          icon: "fas fa-chart-pie",
          title: "Budget",
          desc: "Gestisci il tuo budget",
          // rotta definita come { path: '/budget', name: 'Budget' } (assicurati di averla!)
          route: { name: "Profile" }
        },
        {
          icon: "fas fa-user",
          title: "Profilo",
          desc: "Visualizza e modifica il tuo profilo",
          // rotta definita come { path: '/profile', name: 'Profile' }
          route: { name: "Profile" }
        }
      ],
      token: null,
      username: null
    };
  },
  methods: {
    async fetchStats() {
      this.token = localStorage.getItem("token");
      this.username = localStorage.getItem("username");
      if (!this.token || !this.username) return;

      try {
        // Fetch gruppi
        const resGroups = await fetch(
          `http://localhost:8080/api/groups?username=${this.username}`,
          { headers: { Authorization: `Bearer ${this.token}` } }
        );
        if (resGroups.ok) {
          const groupsData = await resGroups.json();
          this.activeGroups = groupsData.length;
        }
        // Fetch spese
        const resCosts = await fetch(
          `http://localhost:8080/api/costs?username=${this.username}`,
          { headers: { Authorization: `Bearer ${this.token}` } }
        );
        if (resCosts.ok) {
          const costsData = await resCosts.json();
          let sum = 0;
          costsData.forEach((c) => (sum += c.importo || 0));
          this.totalExpenses = sum.toFixed(2);
          if (costsData.length > 0) {
            this.lastExpense = costsData[costsData.length - 1].importo.toFixed(2);
          }
        }
      } catch (err) {
        console.error("Errore fetchStats:", err);
      }
    }
  },
  mounted() {
    this.fetchStats();
  }
};
</script>

<style scoped>
/* Layout base per far sì che il footer stia in fondo */
.dashboard-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* HERO: sfondo a gradiente e testo al centro */
.dashboard-hero {
  background: linear-gradient(135deg, #1e3a8a, #3b82f6);
  padding: 3rem 1rem;
  position: relative;
}
.hero-content {
  max-width: 900px;
  margin: 0 auto;
}
.hero-title {
  color: #fff;
}
.hero-subtitle {
  color: #e2e8f0;
}

/* Sezione Risultati Principali */
.results-section .section-title {
  font-weight: 700;
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
}
.result-card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.result-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}
.result-icon {
  font-size: 2rem;
}

/* Card Totale Spese = ROSSO */
.card-totale-spese {
  background-color: #f8d7da; /* rosso chiaro */
  border: 1px solid #f5c6cb;
  color: #721c24;
}

/* Card Gruppi Attivi = BLU */
.card-gruppi-attivi {
  background-color: #cce5ff; /* blu chiaro */
  border: 1px solid #b8daff;
  color: #004085;
}

/* Card Ultima Spesa = VERDE */
.card-ultima-spesa {
  background-color: #d4edda; /* verde chiaro */
  border: 1px solid #c3e6cb;
  color: #155724;
}

/* Sezione Suggerimenti di Risparmio (RULLO TRASPORTATORE) */
.savings-tips {
  position: relative;
  overflow: hidden;
  width: 100%;
  /* Aggiungiamo un padding per “allargare” un po' in altezza */
  padding: 2rem 0;
}
.tips-slider {
  width: 100%;
  overflow: hidden;
  position: relative;
}
.tips-track {
  display: flex;
  white-space: nowrap;
  animation: marquee 40s linear infinite;
}
.tip-card {
  width: 300px;
  flex: 0 0 auto;
  margin-right: 1.5rem;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
  text-align: center;
  transition: transform 0.3s ease;
  white-space: normal;
  word-wrap: break-word;
}
.tip-card:hover {
  transform: translateY(-5px);
}
.tip-icon {
  margin-bottom: 1rem;
}
.tip-title {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
}
.tip-text {
  color: #6b7280;
  font-size: 0.95rem;
}

/* Keyframe per lo scorrimento orizzontale */
@keyframes marquee {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-50%);
  }
}

/* Sezione Azioni Veloci */
.quick-links .section-title {
  font-weight: 700;
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
}
.quick-link-card {
  border: none;
  transition: transform 0.3s ease;
}
.quick-link-card:hover {
  transform: translateY(-5px);
}
.link-icon {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}

/* Sezione Citazione Motivazionale */
.motivational-quote blockquote {
  font-size: 1.3rem;
  font-style: italic;
  color: #374151;
}
.motivational-quote .blockquote-footer {
  color: #6b7280;
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

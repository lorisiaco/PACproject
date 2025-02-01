<template>
  <div class="home">
    <!-- Hero Section -->
    <header class="hero">
      <div class="hero-content">
        <h1 class="hero-title">Il tuo Controllo Finanziario, Semplice e Potente</h1>
        <p class="hero-subtitle">
          Gestisci le tue spese, traccia i tuoi movimenti e ottimizza il tuo budget con Spendly.
        </p>
        <div class="hero-buttons">
          <router-link to="/register" class="btn btn-primary">Inizia Ora</router-link>
          <router-link to="/login" class="btn btn-secondary">Accedi</router-link>
        </div>
      </div>
    </header>

    <!-- Dashboard Preview -->
    <section class="dashboard-preview">
      <h2 class="section-title">Uno sguardo alla tua Dashboard</h2>
      <div class="features-grid">
        <div class="feature-card">
          <h3>📊 Resoconto Mensile</h3>
          <p>Visualizza il tuo saldo e i tuoi trend finanziari.</p>
          <div class="feature-image">
            <img src="/images/isto.jpg" alt="Resoconto Mensile" />
          </div>
        </div>
        <div class="feature-card">
          <h3>📅 Pianificazione Spese</h3>
          <p>Organizza i tuoi pagamenti e controlla le tue uscite.</p>
          <div class="feature-image">
            <img src="/images/maialino.jpg" alt="Pianificazione Spese" />
          </div>
        </div>
        <div class="feature-card">
          <h3>💳 Gestione Gruppi</h3>
          <p>Dividi spese con amici o colleghi in pochi click.</p>
          <div class="feature-image">
            <img src="/images/mani.jpg" alt="Gestione Gruppi" />
          </div>
        </div>
      </div>
    </section>

    <!-- Call to Action -->
    <section class="call-to-action">
      <h2>Inizia a migliorare la tua gestione finanziaria oggi!</h2>
      <div class="cta-buttons">
        <router-link to="/register" class="btn btn-primary">Registrati Gratis</router-link>
        <router-link to="/login" class="btn btn-secondary">Accedi</router-link>
      </div>
    </section>

    <!-- Footer -->
    <footer class="footer">
      <p>&copy; 2025 Spendly. Tutti i diritti riservati.</p>
      <router-link to="/contact" class="footer-link">Contattaci</router-link>
    </footer>
  </div>
</template>

<script>
export default {
  name: "Home",
};
</script>

<style scoped>
/* Stile globale della pagina */
.home {
  font-family: Arial, sans-serif;
}

/* Hero Section */
.hero {
  background: #2563eb;
  color: white;
  text-align: center;
  padding: 4rem 1rem;
}

.hero-title {
  font-size: 2.8rem;
  font-weight: bold;
}

.hero-subtitle {
  font-size: 1.2rem;
  margin: 1rem 0;
}

.hero-buttons .btn {
  margin: 0.5rem;
  padding: 0.8rem 1.5rem;
  font-size: 1rem;
  border-radius: 8px;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

.hero-buttons .btn-primary {
  background: white;
  color: #2563eb;
  font-weight: bold;
  border: none;
}

.hero-buttons .btn-primary:hover {
  background: #e2e8f0;
}

.hero-buttons .btn-secondary {
  background: #1e40af;
  color: white;
  font-weight: bold;
  border: none;
}

.hero-buttons .btn-secondary:hover {
  background: #1d4ed8;
}

/* Dashboard Preview */
.dashboard-preview {
  background: #f9fafb;
  padding: 3rem 1rem;
  text-align: center;
}

.section-title {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 2rem;
}

/* Layout delle card con Grid */
.features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.feature-card {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.feature-card h3 {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
}

.feature-card p {
  font-size: 1rem;
  color: #6b7280;
  margin-bottom: 1.5rem;
}

.feature-image img {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

/* Call to Action */
.call-to-action {
  background: #2563eb;
  color: white;
  text-align: center;
  padding: 3rem 1rem;
}

.call-to-action h2 {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 1rem;
}

.call-to-action .cta-buttons .btn {
  margin: 0.5rem;
  padding: 0.8rem 1.5rem;
  font-size: 1.2rem;
  border-radius: 8px;
  text-decoration: none;
}

.call-to-action .btn-primary {
  background: white;
  color: #2563eb;
  font-weight: bold;
  border: none;
}

.call-to-action .btn-primary:hover {
  background: #e2e8f0;
}

.call-to-action .btn-secondary {
  background: #1e40af;
  color: white;
  font-weight: bold;
  border: none;
}

.call-to-action .btn-secondary:hover {
  background: #1d4ed8;
}

/* Footer */
.footer {
  background: #1e293b;
  color: white;
  text-align: center;
  padding: 1.5rem 1rem;
}

.footer-link {
  color: #38bdf8;
  text-decoration: none;
  font-weight: bold;
}

.footer-link:hover {
  text-decoration: underline;
}
</style>

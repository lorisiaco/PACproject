<template>
    <div class="contact-page">
      <!-- Intestazione -->
      <header class="contact-header">
        <h1>Contattaci</h1>
        <p>Siamo qui per aiutarti: ecco come entrare in contatto con il nostro Team!</p>
      </header>
  
      <!-- Sezione Team -->
      <section class="team-section">
        <h2>Il Nostro Team</h2>
        <p>Per qualsiasi esigenza, scrivici o chiamaci ai recapiti indicati.</p>
        <div class="team-cards">
          <div class="team-card" v-for="member in team" :key="member.email">
            <h3>{{ member.name }}</h3>
            <p class="role">{{ member.role }}</p>
            <p><strong>Email:</strong> <a :href="`mailto:${member.email}`">{{ member.email }}</a></p>
            <p><strong>Tel:</strong> {{ member.phone }}</p>
          </div>
        </div>
      </section>
  
      <!-- Form di Contatto -->
      <section class="contact-form-section">
        <h2>Scrivici un Messaggio</h2>
        <form @submit.prevent="sendMessage">
          <div class="form-group">
            <label for="name">Nome</label>
            <input id="name" v-model="form.name" type="text" placeholder="Inserisci il tuo nome" required />
          </div>
          <div class="form-group">
            <label for="email">Email</label>
            <input id="email" v-model="form.email" type="email" placeholder="Inserisci la tua email" required />
          </div>
          <div class="form-group">
            <label for="message">Messaggio</label>
            <textarea id="message" v-model="form.message" placeholder="Scrivi il tuo messaggio" rows="5" required></textarea>
          </div>
          <button type="submit" class="btn-submit">Invia</button>
        </form>
      </section>
  
      <!-- Footer -->
      <footer class="contact-footer">
        <p>&copy; 2025 Spendly. Tutti i diritti riservati.</p>
        <p><router-link to="/contact">Contattaci</router-link></p>
      </footer>
    </div>
  </template>
  
  <script>
  export default {
    name: "Contact",
    data() {
      return {
        team: [
          {
            name: "Loris Iacoban",
            role: "Founder & CEO",
            email: "loris.iacoban@spendly.com",
            phone: "+39 0111 123456",
          },
          {
            name: "Mario Rossi",
            role: "Finance Manager",
            email: "mario.rossi@spendly.com",
            phone: "+39 0222 654321",
          },
          {
            name: "Giulia Bianchi",
            role: "Customer Success",
            email: "giulia.bianchi@spendly.com",
            phone: "+39 0333 987654",
          },
        ],
        form: {
          name: "",
          email: "",
          message: "",
        },
      };
    },
    methods: {
      sendMessage() {
        console.log("Messaggio inviato:", this.form);
        alert("Messaggio inviato con successo!");
        this.form.name = "";
        this.form.email = "";
        this.form.message = "";
      },
    },
  };
  </script>
  
  <style scoped>
  /* Stile Generale */
  .contact-page {
    font-family: Arial, sans-serif;
    line-height: 1.6;
    color: #1e293b;
  }
  
  /* Intestazione */
  .contact-header {
    background-color: #1e3a8a;
    color: white;
    text-align: center;
    padding: 2rem 1rem;
  }
  
  .contact-header h1 {
    font-size: 2.5rem;
    margin-bottom: 0.5rem;
  }
  
  .contact-header p {
    font-size: 1.2rem;
  }
  
  /* Sezione Team */
  .team-section {
    text-align: center;
    padding: 2rem 1rem;
  }
  
  .team-section h2 {
    font-size: 2rem;
    margin-bottom: 0.5rem;
  }
  
  .team-section p {
    margin-bottom: 2rem;
  }
  
  .team-cards {
    display: flex;
    justify-content: center;
    gap: 1.5rem;
    flex-wrap: wrap;
  }
  
  .team-card {
    background: white;
    padding: 1.5rem;
    border-radius: 12px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    max-width: 300px;
    text-align: left;
    flex: 1;
    min-width: 260px;
  }
  
  .team-card h3 {
    margin-bottom: 0.5rem;
    color: #1e3a8a;
  }
  
  .team-card .role {
    color: #64748b;
    font-size: 0.9rem;
    margin-bottom: 1rem;
  }
  
  /* Form di Contatto */
  .contact-form-section {
    padding: 2rem 1rem;
    background: #f9fafb;
    text-align: center;
  }
  
  .contact-form-section h2 {
    font-size: 2rem;
    margin-bottom: 1rem;
  }
  
  form {
    max-width: 600px;
    margin: 0 auto;
  }
  
  .form-group {
    margin-bottom: 1rem;
  }
  
  label {
    display: block;
    font-weight: bold;
    margin-bottom: 0.5rem;
  }
  
  input,
  textarea {
    width: 100%;
    padding: 0.8rem;
    border: 1px solid #d1d5db;
    border-radius: 8px;
    font-size: 1rem;
  }
  
  textarea {
    resize: vertical;
  }
  
  .btn-submit {
    background-color: #2563eb;
    color: white;
    border: none;
    padding: 0.8rem 1.5rem;
    font-size: 1rem;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  .btn-submit:hover {
    background-color: #1e40af;
  }
  
  /* Footer */
  .contact-footer {
    text-align: center;
    padding: 1rem;
    background-color: #1e3a8a;
    color: white;
  }
  
  .contact-footer a {
    color: #93c5fd;
    text-decoration: none;
  }
  
  .contact-footer a:hover {
    text-decoration: underline;
  }
  </style>
  
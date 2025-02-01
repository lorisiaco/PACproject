<template>
    <div>
      <header class="bg-blue-600 text-white py-4">
        <div class="container mx-auto text-center">
          <h1 class="text-3xl font-bold">Contattaci</h1>
          <p>Siamo qui per aiutarti: ecco come entrare in contatto con il nostro Team!</p>
        </div>
      </header>
  
      <main class="container mx-auto my-8">
        <section class="team-section grid grid-cols-1 md:grid-cols-3 gap-6">
          <div
            v-for="member in team"
            :key="member.name"
            class="team-member bg-white shadow-md rounded-lg p-4 text-center"
          >
            <h3 class="text-lg font-bold">{{ member.name }}</h3>
            <p class="text-sm text-gray-600">{{ member.role }}</p>
            <p>Email: <a :href="'mailto:' + member.email" class="text-blue-500">{{ member.email }}</a></p>
            <p>Tel: {{ member.phone }}</p>
          </div>
        </section>
  
        <section class="contact-form my-12 bg-gray-100 p-6 rounded-lg shadow-md">
          <h2 class="text-2xl font-bold text-center mb-4">Scrivici un Messaggio</h2>
          <form @submit.prevent="submitForm" class="grid gap-4">
            <input v-model="formData.name" type="text" placeholder="Nome" class="p-3 border rounded" required />
            <input v-model="formData.email" type="email" placeholder="Email" class="p-3 border rounded" required />
            <textarea v-model="formData.message" placeholder="Messaggio" class="p-3 border rounded" required></textarea>
            <button type="submit" class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600">Invia</button>
          </form>
        </section>
      </main>
  
      <footer class="bg-black text-white py-4 text-center">
        <p>© 2025 Spendly. Tutti i diritti riservati. <a href="#" class="text-blue-400">Contattaci</a></p>
      </footer>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        team: [
          { name: "Loris Iacoban", role: "Founder & CEO", email: "loris.iacoban@spendly.com", phone: "+39 0111 123456" },
          { name: "Mario Rossi", role: "Finance Manager", email: "mario.rossi@spendly.com", phone: "+39 0222 654321" },
          { name: "Giulia Bianchi", role: "Customer Success", email: "giulia.bianchi@spendly.com", phone: "+39 0333 987654" },
        ],
        formData: {
          name: "",
          email: "",
          message: "",
        },
      };
    },
    methods: {
      submitForm() {
        alert(`Messaggio inviato da: ${this.formData.name}`);
        // Logica per inviare il messaggio al server
      },
    },
  };
  </script>
  
  <style>
  .container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 1rem;
  }
  </style>
  
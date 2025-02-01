<template>
  <div class="home bg-gray-50 text-gray-900">
    <!-- Navbar -->
    <nav class="bg-gray-900 text-white shadow-md py-4">
      <div class="max-w-7xl mx-auto px-4 flex justify-between items-center">
        <!-- Logo -->
        <router-link to="/" class="inline-block">
          <img src="/images/logo.png" alt="Spendly Logo" class="h-12 w-auto object-contain" />
        </router-link>
        <div class="hidden md:flex space-x-6">
          <router-link to="/login" class="hover:text-blue-300">Accedi</router-link>
          <router-link to="/register" class="hover:text-blue-300">Registrati</router-link>
          <router-link to="/contact" class="hover:text-blue-300">Contatti</router-link>
        </div>
      </div>
    </nav>

    <!-- Hero Section -->
    <header class="bg-blue-600 text-white text-center py-20">
      <div class="max-w-4xl mx-auto">
        <h1 class="text-5xl font-bold">Il tuo Controllo Finanziario, Semplice e Potente</h1>
        <p class="mt-4 text-lg">Gestisci le tue spese, traccia i tuoi movimenti e ottimizza il tuo budget con Spendly.</p>
        <!-- Pulsanti -->
        <div class="mt-6 flex justify-center space-x-4">
          <router-link
            to="/register"
            class="bg-white text-blue-600 font-bold py-3 px-6 rounded-lg shadow-lg hover:bg-gray-100"
          >
            Inizia Ora
          </router-link>
          <router-link
            to="/login"
            class="bg-blue-800 text-white font-bold py-3 px-6 rounded-lg shadow-lg hover:bg-blue-700"
          >
            Accedi
          </router-link>
        </div>
      </div>
    </header>

    <!-- Anteprima Dashboard -->
    <section class="py-16 bg-gray-100">
      <div class="max-w-7xl mx-auto px-6">
        <h2 class="text-3xl font-bold text-center text-gray-800">Uno sguardo alla tua Dashboard</h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8 mt-10">
          <div class="p-6 bg-white rounded-lg shadow-md text-center">
            <h3 class="text-xl font-bold">📊 Resoconto Mensile</h3>
            <p class="mt-2 text-gray-600">Visualizza il tuo saldo e i tuoi trend finanziari.</p>
            <img src="/images/isto.jpg" alt="Grafico" class="mt-4 mx-auto h-40 object-contain" />
          </div>
          <div class="p-6 bg-white rounded-lg shadow-md text-center">
            <h3 class="text-xl font-bold">📅 Pianificazione Spese</h3>
            <p class="mt-2 text-gray-600">Organizza i tuoi pagamenti e controlla le tue uscite.</p>
            <img src="/images/maialino.jpg" alt="Calendario" class="mt-4 mx-auto h-40 object-contain" />
          </div>
          <div class="p-6 bg-white rounded-lg shadow-md text-center">
            <h3 class="text-xl font-bold">💳 Gestione Gruppi</h3>
            <p class="mt-2 text-gray-600">Dividi spese con amici o colleghi in pochi click.</p>
            <img src="/images/mani.jpg" alt="Spese Gruppo" class="mt-4 mx-auto h-40 object-contain" />
          </div>
        </div>
      </div>
    </section>

    <!-- Call to Action -->
    <section class="bg-blue-600 text-white text-center py-12">
      <h2 class="text-3xl font-bold">Inizia a migliorare la tua gestione finanziaria oggi!</h2>
      <div class="mt-6 flex justify-center space-x-4">
        <router-link
          to="/register"
          class="bg-white text-blue-600 font-bold py-3 px-6 rounded-lg shadow-lg hover:bg-gray-100"
        >
          Registrati Gratis
        </router-link>
        <router-link
          to="/login"
          class="bg-blue-800 text-white font-bold py-3 px-6 rounded-lg shadow-lg hover:bg-blue-700"
        >
          Accedi
        </router-link>
      </div>
    </section>

    <!-- Footer -->
    <footer class="bg-gray-900 text-white text-center py-6">
      <p>&copy; 2025 Spendly. Tutti i diritti riservati.</p>
      <router-link to="/contact" class="text-blue-400 hover:underline">Contattaci</router-link>
    </footer>
  </div>
</template>

<script>
export default {
  name: "Home",
};
</script>

<style scoped>
/* Se hai bisogno di aggiungere stili personalizzati */
</style>

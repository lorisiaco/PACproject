<!-- src/views/Register.vue -->
<template>
  <div class="bg-gray-50 min-h-screen">
    <!-- Navbar (o altro contenuto) -->
    <nav class="bg-gray-900 text-white shadow-md py-4">
      <div class="max-w-7xl mx-auto px-4 flex justify-between items-center">
        <a href="/" class="inline-block">
          <img src="/images/logo.png" alt="Spendly Logo" class="h-12 w-auto object-contain" />
        </a>
        <div class="hidden md:flex space-x-6">
          <a href="/login" class="hover:text-blue-300">Accedi</a>
          <a href="/register" class="hover:text-blue-300">Registrati</a>
          <a href="/contact" class="hover:text-blue-300">Contatti</a>
        </div>
      </div>
    </nav>

    <!-- Contenuto principale (form di registrazione, ecc.) -->
    <main class="max-w-3xl mx-auto mt-10 mb-16 px-4">
      <!-- Il tuo form o altro contenuto -->
    </main>

    <!-- Footer -->
    <Footer/>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import Footer from '@/components/Footer.vue'  // Assicurati che il percorso sia corretto

export default {
  name: 'Register',
  components: {
    Footer,
  },
  setup() {
    // Il tuo codice per gestire il form, validazioni, ecc.
    const auth = useAuthStore()
    const form = ref({
      firstName: '',
      lastName: '',
      username: '',
      email: '',
      phone: '',
      address: '',
      password: '',
      confirmPassword: '',
      profileImage: null,
    })

    const errors = ref({})

    const isValidEmail = (email) => {
      const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      return regex.test(email)
    }

    const handleFileUpload = (event) => {
      form.value.profileImage = event.target.files[0]
    }

    const handleRegister = async () => {
      errors.value = {}

      if (form.value.password !== form.value.confirmPassword) {
        errors.value.confirmPassword = 'Le password non coincidono'
      }

      if (!isValidEmail(form.value.email)) {
        errors.value.email = 'Formato email non valido'
      }

      if (Object.keys(errors.value).length > 0) {
        return
      }

      try {
        const formData = new FormData()
        Object.entries(form.value).forEach(([key, value]) => {
          if (key === 'profileImage' && value) {
            formData.append(key, value)
          } else {
            formData.append(key, value)
          }
        })

        await auth.register(formData)
      } catch (err) {
        if (err.response && err.response.data) {
          errors.value = err.response.data
        } else {
          errors.value.general = 'Errore durante la registrazione'
        }
      }
    }

    return { form, errors, handleRegister, handleFileUpload }
  },
}
</script>

<style scoped>
/* Aggiungi eventuali stili personalizzati per questa pagina */
</style>

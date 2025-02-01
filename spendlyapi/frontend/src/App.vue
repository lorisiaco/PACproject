<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import Navbar from './components/Navbar.vue';

// Usiamo la route corrente per determinare se la navbar deve essere visibile
const route = useRoute();
const showNavbar = computed(() => {
  // Aggiungi le route dove la navbar NON deve essere visibile
  const noNavbarRoutes = ['/login',,'/register','/profile'];
  return !noNavbarRoutes.includes(route.path);
});
</script>

<template>
  <div id="app">
    <!-- Navbar visibile solo se `showNavbar` è true -->
    <Navbar v-if="showNavbar" />
    <router-view />
  </div>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>

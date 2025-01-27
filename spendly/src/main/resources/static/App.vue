<template>
  <div class="container">
    <h1>Spendly Dashboard</h1>
    <c-button color="primary">Aggiungi Spesa</c-button>
  </div>
</template>

<script>
import { CButton } from "@coreui/vue";

export default {
  components: {
    CButton,
  },
};
</script>

<style>
.container {
  padding: 20px;
}
</style>
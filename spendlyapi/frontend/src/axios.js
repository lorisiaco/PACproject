// src/axios.js
import axios from 'axios'

const instance = axios.create({
  baseURL: 'http://localhost:8080', // Cambia l'URL se necessario
})

// Aggiungi l'header Authorization se il token esiste
const token = localStorage.getItem('token')
if (token) {
  instance.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

export default instance

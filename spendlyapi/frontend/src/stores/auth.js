// src/stores/auth.js
import { defineStore } from 'pinia'
import axios from '../axios'
import router from '../router'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user')) || {},
    role: localStorage.getItem('role') || '',
  }),
  actions: {
    async login(username, password) {
      try {
        const response = await axios.post('http://localhost:8080/account/login', { username, password })
        this.token = response.data.token
        this.user = response.data.user
        this.role = response.data.user.role

        localStorage.setItem('token', this.token)
        localStorage.setItem('user', JSON.stringify(this.user))
        localStorage.setItem('role', this.role)

        axios.defaults.headers.common['Authorization'] = `Bearer ${this.token}`

        router.push({ name: 'Home' })
      } catch (error) {
        console.error('Login error:', error)
        throw error
      }
    },
    async register(userData) {
      try {
        const response = await axios.post('http://localhost:8080/account/register', userData)
        // Dopo la registrazione, puoi automaticamente loggare l'utente o redirigerlo al login
        router.push({ name: 'Login' })
      } catch (error) {
        console.error('Registration error:', error)
        throw error
      }
    },
    logout() {
      this.token = ''
      this.user = {}
      this.role = ''
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      localStorage.removeItem('role')
      delete axios.defaults.headers.common['Authorization']
      router.push({ name: 'Home' })
    },
    async fetchProfile() {
      try {
        const response = await axios.get('http://localhost:8080/account/profile')
        this.user = response.data.user
        this.role = response.data.user.role
        localStorage.setItem('user', JSON.stringify(this.user))
        localStorage.setItem('role', this.role)
      } catch (error) {
        console.error('Fetch profile error:', error)
      }
    }
  }
})

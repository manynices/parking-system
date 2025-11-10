import { createApp } from 'vue'
import { createPinia } from 'pinia'

import Login from './view/Login.vue'
import router from './router'
import './styles/main.css'

const login = createApp(Login)

login.use(createPinia())
login.use(router)

login.mount('#login')



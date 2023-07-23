import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import { createStore } from 'vuex'
//import Vue from 'vue'
import router from './router'
import user from './stores/modules/custom'
import VueAxios from 'vue-axios'
import axios from 'axios'
import 'element-plus/dist/index.css'
import.meta.env.BASE_URL
import stores from './stores'
import Particles from 'particles.vue3'
import cors from 'cors'
import Vuecookie from 'vue-cookies'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
//import {Message} from 'element-plus'
axios.defaults.baseURL = '/api'
let baseConfig = {
  baseUrl: ''
}
export let config = {
  development: {
    ...baseConfig,
    baseUrl: ''
  }
}
// const store= createStore({
//     modules :{
//         user
//     }
// })
const store = createStore(stores)
const app = createApp(App)
//import { useRouter } from "vue-router";
//const store=createStore(user)
//app.use(store)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.use(createPinia())
app.use(router)
app.use(VueAxios, axios)
app.use(store)
app.use(cors)
app.use(Particles)
app.use(Vuecookie)
app.mount('#app')

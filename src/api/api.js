import axios from 'axios'
import { config } from '/src/main'
var instance = axios.create({
  baseURL: config.development.baseUrl,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/form-data',
    Accept: 'application/json',
    'Access-Control-Allow-Origin': '*'
  }
})
axios.defaults.withCredentials = true

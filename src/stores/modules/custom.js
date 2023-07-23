//import {createStore} from 'vuex'
//import {parse} from "qs";

//namespaced:true,
const state = {
  user: 0,
  sell: 2,
  admin: 3
}
const mutations = {
  saveUser(state, user) {
    //更改用户状态信息
    state.user = user
  },
  deleteUser(state) {
    state.user = null
  },
  saveSell(state, sell) {
    state.sell = sell
  },
  deleteSell(state) {
    state.sell = null
  },
  saveAdmin(state, admin) {
    state.admin = admin
  },
  deleteAdmin(state) {
    state.admin = null
  }
}
const actions = {
  setUser({ commit }, code) {
    commit('saveUser', parseInt(code))
  },
  setSeller({ commit }, code) {
    commit('saveSell', code)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

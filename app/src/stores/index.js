import { createStore } from 'vuex'

import user from './modules/custom'
// 创建一个新的Vuex存储

const store = createStore({
  modules: {
    user
  }
})
// 导出Vuex存储
export default store

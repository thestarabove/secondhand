<template>
  <Menu />
  <div class="container">
    <el-row>
      <el-col :span="6">
        <el-aside>
          <el-avatar
            v-if="this.$cookies.get('img') == null"
            class="mr-3"
            :size="128"
            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
          />
          <el-avatar v-else class="mr-3" :size="128" :src="img" />
          <el-menu
            :default-active="activeMenu"
            class="el-menu-vertical-demo"
            @select="handleMenuSelect"
            :router="true"
          >
            <el-menu-item index="/Info/userInfo">
              <i class="el-icon-menu"></i>
              <span>用户信息</span>
            </el-menu-item>
            <el-menu-item index="/Info/myCollection">
              <i class="el-icon-menu"></i>
              <span>收藏商品</span>
            </el-menu-item>
            <el-menu-item-group title="我的订单">
              <el-menu-item index="/Info/myOrder">待确认</el-menu-item>
              <el-menu-item index="/Info/myOrdered">已完成</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="我的评价">
              <el-menu-item index="/Info/myRating">待评价</el-menu-item>
              <el-menu-item index="/Info/myRated">已评价</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group v-if="this.$cookies.get('role')==2" title="我的发布">
              <el-menu-item index="/Info/mySells">正在售卖</el-menu-item>
              <el-menu-item index="/Info/mySelled">已卖出</el-menu-item>
            </el-menu-item-group>
          </el-menu>
        </el-aside>
      </el-col>
      <el-col :span="18">
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Menu from '../components/Menu.vue'

export default {
  name: 'Info',
  components: {
    Menu
  },
  data() {
    return {
      activeMenu: '',
      img: 'http://' + this.$cookies.get('img')
    }
  },
  watch: {
    $route(to) {
      this.activeMenu = to.path
    }
  },
  methods: {
    handleMenuSelect(index) {
      this.$router.push(index)
    }
  }
}
</script>

<style>
.container {
  height: 100vh;
  padding: 20px;
}
</style>

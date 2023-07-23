<template>
  <AdminMenu/>
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
            <el-menu-item index="/Admin/adminInfo">
              <i class="el-icon-menu"></i>
              <span>用户信息</span>
            </el-menu-item>
            <el-menu-item index="/Admin/applicant">
              <i class="el-icon-menu"></i>
              <span>申请商家</span>
            </el-menu-item>
            <el-menu-item-group title="用户管理">
              <el-menu-item index="/Admin/allUser">普通用户</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="用户评价">
              <el-menu-item index="/Admin/RatingApp">待审核</el-menu-item>
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
import AdminMenu from "../components/AdminMenu.vue";
export default {
  name: 'Admin',
  components:{
    AdminMenu
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

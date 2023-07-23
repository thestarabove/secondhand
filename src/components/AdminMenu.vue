<template>
  <el-affix :offset="0">
    <el-menu
        class=""
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        :ellipsis="false"
        style="width: 100%;"
    >
      <el-menu-item index="0"
      ><img style="max-width: 50px" src="../assets/logo.jpg"
      /></el-menu-item>
      <div class="mt-4" style="width: 60%"></div>
      <el-menu-item index="1">欢迎您！管理员 {{ this.$cookies.get('account') }}</el-menu-item>
      <div style="width: 70%;"></div>
      <el-menu-item index="7" @click="Out">退出</el-menu-item>
    </el-menu>
  </el-affix>
</template>

<script lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
//import { Search } from '@element-plus/icons-vue'
const activeIndex = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
export default {
  name: 'AdminMenu',
  methods: {
    Out() {
      this.$router.push('/Login')
      this.$cookies.clear
      axios
          .get('user/loginOut')
          .then((res) => {
            console.log(res)
          })
          .catch((error) => {
            ElMessage({
              message: '退出失败',
              type: 'warning'
            })
          })

    },
  },
  data() {
    return {
      goodName: ''
    }
  }
}
</script>

<style>
.flex-grow {
  width: 700px;
}
.mt-4 {
  width: 300px;
}
.el-input {
  margin-right: 300px;
  width: 30px;
}
.w-50 {
  margin-top: 10px;
  margin-left: 300px;
  width: 300px;
}
.but {
  margin-bottom: 10px;
  margin-left: 650px;
}
</style>

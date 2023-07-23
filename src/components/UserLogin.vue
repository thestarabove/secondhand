<template>
  <body style="display: block">
    <div id="app" data-v-app style="margin: 0 0 0 0">
      <div class="login">
        <Particles id="tsparticles" class="login__particles" />
        <div class="loginPart">
          <h2>用户登录</h2>
          <el-form
            ref="ruleFormRef"
            :model="user"
            status-icon
            :rules="rules"
            label-width="100px"
            class="demo-ruleForm"
            style="transform: translate(-30px)"
          >
            <el-form-item label="账号：" prop="account">
              <el-input v-model="user.account" clearable placeholder="请输入账号" maxlength="20" />
            </el-form-item>
            <el-form-item label="密码：" prop="password">
              <el-input
                v-model="user.password"
                type="password"
                placeholder="请输入密码"
                maxlength="20"
                show-password
                clearable
              />
            </el-form-item>
            <el-form-item label="验证码：" prop="verifyCode">
              <el-input
                prop="code"
                style="width: 150px"
                v-model="user.verifyCode"
                placeholder="请输入验证码"
                maxlength="4"
                clearable
              />
              <img class="verifyCodeImg" :src="imageSrc" v-if="showImage" alt="Image" />
              <button @click="restImg">刷新图片</button>
            </el-form-item>
            <el-button class="btn" type="primary" @click.native.prevent="login('ruleFormRef')"
              >登录</el-button
            >
            <div style="text-align: right; transform: translate(0, 30px)">
              <el-link type="warning" @click="Register">没有账号？去注册</el-link>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </body>
</template>
<script lang="ts">
import { reactive, ref, onMounted } from 'vue'
import router from '../router/index'
//import { login } from '@/api/Login'
//import { tokenStore,accountStore } from '@/store/modules/user'
//import { loginReq } from '@/api/types/loginReq'
import type { FormInstance } from 'element-plus'

import { ElMessage } from 'element-plus'
import { mapActions, mapMutations, mapState } from 'vuex'
import axios from 'axios'
import cors from 'cors'

//校验

//校验

export default {
  name: 'UserLogin',


  beforeCreate: function () {
    document.getElementsByTagName('body')[0].className = 'body-bg'

  },
  data() {
    var validatePassword = (rule: any, value: any, callback: any) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    var validateAccount = (rule: any, value: any, callback: any) => {
      if (value === '') {
        callback(new Error('请输入账号'))
      } else {
        callback()
      }
    }
    var validateVerification = (rule: any, value: any, callback: any) => {
      if (value === '') {
        callback(new Error('请输入验证码'))
      } else {
        console.log(value)
        callback()
      }
    }
    return {
      imageSrc: 'http://127.0.0.1:8081/secondhand/getKaptchaImage',
      showImage: true,
      user: {
        account: '',
        password: '',
        verifyCode: ''
      },
      rules: reactive({
        password: [{ validator: validatePassword, trigger: 'blur' }],
        account: [{ validator: validateAccount, trigger: 'blur' }],
        verifyCode: [{ validator: validateVerification, trigger: 'blur' }]
      }),
    }
  },
  computed: {
    ...mapState({
      users: (state) => state.user.user,
      sell: (state) => state.user.sell,
      admin: (state) => state.user.admin
    })
  },
  methods: {
    ...mapMutations('user', ['saveUser']),
    ...mapActions('user', ['setUser']),

    login(ruleFormRef) {
      this.$refs[ruleFormRef].validate((valid) => {
        if (valid) {
          axios.defaults.withCredentials = true
          axios
            .post('user/doLogin', this.user)
            .then((res) => {
              if (res.data.code === 200) {
                //setToken(res.data.token);
                // localStorage.setItem("USERNAME", res.data.username);
                //console.log(res.data.data.role)
                // console.log(this.user)
                if (this.users === res.data.data.role) {
                  ElMessage({
                    message: '用户登录成功啦',
                    type: 'success',
                    showClose: true
                  })
                  this.$router.push({
                    name: 'index',
                    path: '/index'
                  })
                  this.$cookies.set('account', res.data.data.account)
                  this.$cookies.set('role', res.data.data.role)
                  this.$cookies.set('userId', res.data.data.userId)
                  this.$cookies.set('userName', res.data.data.userName)
                  this.$cookies.set('img', res.data.data.img)
                } else if (this.sell === res.data.data.role) {
                  //this.saveAdmin(res.data.data);
                  //sessionStorage.setItem("admin",JSON.stringify(this.admin));
                  ElMessage({
                    message: '商家登录成功！',
                    type: 'success'
                  })
                  this.$router.push({
                    name: 'index',
                    path: '/index',
                    param: res.data.data
                  })
                  this.$cookies.set('account', res.data.data.account)
                  this.$cookies.set('role', res.data.data.role)
                  this.$cookies.set('userId', res.data.data.userId)
                  this.$cookies.set('userName', res.data.data.userName)
                  this.$cookies.set('img', res.data.data.img)
                }
              else if (this.admin===res.data.data.role) {
                 // sessionStorage.setItem('admin', JSON.stringify(this.admin))
                  ElMessage({
                    message: '管理员登录成功！',
                    type: 'success'
                  })
                  this.$router.push({
                    name:'Admin',
                    path: '/Admin',
                    param: res.data.data
                  })
                  this.$cookies.set('account', res.data.data.account)
                  this.$cookies.set('role', res.data.data.role)
                  this.$cookies.set('userId', res.data.data.userId)
                  this.$cookies.set('userName', res.data.data.userName)
                  this.$cookies.set('img', res.data.data.img)
                }
              } else {
                ElMessage({
                  message: '账户名或密码错误',
                  type: 'error',
                  showClose: true
                })
              }
            })
            .catch((err) => {
              ElMessage({
                message: '验证码错误',
                type: 'error',
                showClose: true
              })
            })
        } else {
          return false
        }
      })
    },
    Register() {
      this.$router.push('/register')
    },
    restImg() {
      this.showImage = false
      this.$nextTick(() => {
        this.showImage = true
      })
    }
  }
}
</script>
<style scoped>
#app {
  margin: 0;
}

.login {
  height: 100%;
  width: 100%;
  overflow: hidden;
  margin: 0 0 0 0;
}
.login__particles {
  height: 100%;
  width: 100%;
  background-size: cover;
  background-repeat: no-repeat;
  background-image: url('../assets/login/login2.jpg');
  background-position: left center;
  opacity: 0.9;
  position: fixed;
  pointer-events: none;
}
.loginPart {
  position: absolute;
  /*定位方式绝对定位absolute*/
  top: 50%;
  left: 80%;
  /*顶和高同时设置50%实现的是同时水平垂直居中效果*/
  transform: translate(-50%, -50%);
  /*实现块元素百分比下居中*/
  width: 450px;
  padding: 50px;
  background: rgba(255, 204, 255, 0.3);
  /*背景颜色为黑色，透明度为0.8*/
  box-sizing: border-box;
  /*box-sizing设置盒子模型的解析模式为怪异盒模型，
  将border和padding划归到width范围内*/
  box-shadow: 0px 15px 25px rgba(0, 0, 0, 0.5);
  /*边框阴影  水平阴影0 垂直阴影15px 模糊25px 颜色黑色透明度0.5*/
  border-radius: 15px;
  /*边框圆角，四个角均为15px*/
}
h2 {
  margin: 0 0 30px;
  padding: 0;
  color: #fff;
  text-align: center;
  /*文字居中*/
}
.btn {
  transform: translate(170px);
  width: 80px;
  height: 40px;
  font-size: 15px;
}
</style>

<template>
  <div class="loginbody">
    <div class="logindata">
      <div class="logintext">
        <h2>Welcome</h2>
      </div>
      <div class="formdata">
        <el-form ref="form" :model="form" :rules="rules">
          <el-form-item prop="account">
            <el-input v-model="form.account" clearable placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              clearable
              placeholder="请输入密码"
              show-password
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="tool">
        <div>
          <el-checkbox v-model="checked" @change="remenber">记住密码</el-checkbox>
        </div>
        <div>
          <span class="shou" @click="forgetpas">忘记密码？</span>
        </div>
      </div>
      <div class="butt">
        <el-button type="primary" @click.native.prevent="login('form')">登录</el-button>
        <el-button class="shou" @click="register">注册</el-button>
      </div>
    </div>
  </div>
</template>

<script>
//import { login } from "@/api/login";
//import { setToken } from "@/request/auth";
import { mapActions, mapMutations, mapState } from 'vuex'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { useStore } from 'vuex'
import store from '../stores'
import user from '../stores/modules/custom.js'
import { useRouter } from 'vue-router'
const router = useRouter()
export default {
  name: 'Login',
  data() {
    return {
      form: {
        password: '',
        account: ''
      },
      checked: false,
      rules: {
        account: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { max: 10, message: '不能大于10个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { max: 10, message: '不能大于10个字符', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapState({
      user: (state) => state.user.user,
      sell: (state) => state.user.sell,
      admin: (state) => state.user.admin
    })
  },

  methods: {
    ...mapMutations('user', ['saveUser']),
    ...mapActions('user', ['setUser']),
    saveUserData(store, data) {
      this.$store.commit('saveUser', data)
    },
    login(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          axios
            .post('user/Login', this.form)
            .then((res) => {
              if (res.data.code === 200) {
                //setToken(res.data.token);
                // localStorage.setItem("USERNAME", res.data.username);
                if (this.user == res.data.data) {
                  ElMessage({
                    message: '登录成功啦',
                    type: 'success',
                    showClose: true
                  })
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
                message: '账户名或密码错误',
                type: 'error',
                showClose: true
              })
            })
        } else {
          return false
        }
      })
    },
    remenber(data) {
      this.checked = data
      if (this.checked) {
        localStorage.setItem('news', JSON.stringify(this.form))
      } else {
        localStorage.removeItem('news')
      }
    },
    forgetpas() {
      this.$message({
        type: 'info',
        message: '功能尚未开发额😥',
        showClose: true
      })
    },
    register() {}
  }
}
</script>

<style scoped>
.loginbody {
  position: relative;
  width: 100%;
  height: 100%;
  min-width: 1000px;
  background-image: url('../assets/login/login.jpg');
  background-size: cover;
  background-position: left center;
  overflow: auto;
  background-repeat: no-repeat;
  margin: 0 auto 0 auto;
  font-size: 14px;
  padding-top: 150px;
}

.logintext {
  margin-bottom: 20px;
  line-height: 50px;
  text-align: center;
  font-size: 30px;
  font-weight: bolder;
  color: white;
  text-shadow: 2px 2px 4px #000000;
}

.logindata {
  width: 400px;
  height: 300px;
  transform: translate(-50%);
  margin-left: 50%;
}

.tool {
  display: flex;
  justify-content: space-between;
  color: #606266;
}

.butt {
  margin-top: 10px;
  text-align: center;
}

.shou {
  cursor: pointer;
  color: #606266;
}

/*ui*/
/* /deep/ .el-form-item__label {
  font-weight: bolder;
  font-size: 15px;
  text-align: left;
}

/deep/ .el-button {
  width: 100%;
  margin-bottom: 10px;

} */
</style>

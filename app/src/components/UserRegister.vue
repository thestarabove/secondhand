<template>
  <div class="bg">
    <div class="register">
      <el-form
        :model="form"
        status-icon
        :rules="rules"
        ref="form"
        label-width="100px"
        class="demo-form"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input type="text" v-model="form.userName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="账号" prop="account">
          <el-input type="text" v-model="form.account" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="form.checkPass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="userPhone">
          <el-input type="tel" v-model="form.userPhone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input type="text" v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="imageValid">
          <el-input type="text" v-model="form.imageValid"></el-input>
          <canvas ref="canvas" id="canvas" width="120" height="40" @click="updateCanvas()"></canvas>
        </el-form-item>
        <el-form-item>
          <el-button type="button" @click="register('form')">提交</el-button>
          <el-button @click="resetForm('form')">重置</el-button>
          <el-link type="warning" @click="log">有账号？去登录</el-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
export default {
  name: 'UserRegister',
  beforeCreate: function () {
    document.getElementsByTagName('body')[0].className = 'body-bg'
  },
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.form.checkPass !== '') {
          this.$refs.form.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    var validatePass3 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('验证码不能为空'))
      } else if (value !== this.num) {
        this.updateCanvas()
        callback(new Error('验证码输入有误'))
      } else {
        callback()
      }
    }
    return {
      form: {
        userName: '',
        account: '',
        password: '',
        checkPass: '',
        userPhone: '',
        address: '',
        imageValid: '' //用户输入的验证码
      },
      num: '', //图片中的随机验证码
      rules: {
        name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ validator: validatePass, trigger: 'blur' }],
        checkPass: [{ validator: validatePass2, trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { min: 11, max: 11, message: '电话号码格式不正确', trigger: 'blur' }
        ],
        imageValid: [{ validator: validatePass3, trigger: 'blur' }]
      }
    }
  },

  methods: {
    //重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },

    //提交注册
    register(formName) {
      this.$refs[formName].validate((valid) => {
        //表单验证
        if (!valid) {
          return
        } else {
          axios
            .post('/user/register', this.form)
            .then((response) => {
              //console.log(response.data)
              if (response.data.status === 200) {
                ElMessage({
                  message: '注册成功！',
                  type: 'success'
                })
                this.resetForm('form')
                this.$router.push('/login')
              } else {
                this.resetForm('form')
                ElMessage({
                  message: '该用户名已被注册！',
                  type: 'warning'
                })
              }
            })
            .catch((error) => {
              alert(error.message)
            })
        }
      })
    },

    // 随机颜色函数
    getColor() {
      var r = Math.floor(Math.random() * 256)
      var g = Math.floor(Math.random() * 256)
      var b = Math.floor(Math.random() * 256)
      return 'rgb(' + r + ',' + g + ',' + b + ')'
    },
    draw() {
      var context = this.$refs.canvas.getContext('2d') //舞台，getContext() 方法可返回一个对象，该对象提供了用于在画布上绘图的方法和属性。
      context.strokeRect(0, 0, 120, 40) //绘制矩形（无填充）
      var aCode = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f']
      // 绘制字母
      var arr = [] //定义一个数组用来接收产生的随机数
      for (var i = 0; i < 4; i++) {
        let x = 20 + i * 20 //每个字母之间间隔20
        let y = 20 + 10 * Math.random() //y轴方向位置为20-30随机
        var index = Math.floor(Math.random() * aCode.length) //随机索引值
        var txt = aCode[index]
        context.font = 'bold 20px 微软雅黑' //设置或返回文本内容的当前字体属性
        context.fillStyle = this.getColor //设置或返回用于填充绘画的颜色、渐变或模式，随机
        context.translate(x, y) //重新映射画布上的 (0,0) 位置，字母不可以旋转移动，所以移动容器
        var deg = (90 * Math.random() * Math.PI) / 180 //0-90度随机旋转
        context.rotate(deg) //  旋转当前绘图
        context.fillText(txt, 0, 0) //在画布上绘制“被填充的”文本
        context.rotate(-deg) //将画布旋转回初始状态
        context.translate(-x, -y) //将画布移动回初始状态
        arr[i] = txt //接收产生的随机数
      }
      this.num = arr[0] + arr[1] + arr[2] + arr[3] //将产生的验证码放入num
      // 绘制干扰线条
      for (let i = 0; i < 8; i++) {
        context.beginPath() //起始一条路径，或重置当前路径
        context.moveTo(Math.random() * 120, Math.random() * 40) //把路径移动到画布中的随机点，不创建线条
        context.lineTo(Math.random() * 120, Math.random() * 40) //添加一个新点，然后在画布中创建从该点到最后指定点的线条
        context.strokeStyle = this.getColor() //随机线条颜色
        context.stroke() //  绘制已定义的路径
      }
      // 绘制干扰点，和上述步骤一样，此处用长度为1的线代替点
      for (let i = 0; i < 20; i++) {
        context.beginPath()
        let x = Math.random() * 120
        let y = Math.random() * 40
        context.moveTo(x, y)
        context.lineTo(x + 1, y + 1)
        context.strokeStyle = this.getColor()
        context.stroke()
      }
    },
    //更新验证码
    updateCanvas() {
      var context = this.$refs.canvas.getContext('2d')
      context.clearRect(0, 0, 120, 40) //在给定的矩形内清除指定的像素
      this.draw()
      console.log(this.num)
    },
    log() {
      this.$router.push('/Login')
    }
  },
  mounted() {
    this.draw()
  }
}
</script>

<style>
.body-bg {
  height: 100%;
  width: 100%;
  background-image: url('../assets/login/login2.jpg');
  background-size: cover;
  background-repeat: no-repeat;
  background-image: url('../assets/login/login2.jpg');
  background-position: left center;
}

.register {
  margin: 100px 700px;
  padding: 2em 0;
  width: 500px;
}

.imageVaild {
  position: relative;
}

canvas {
  position: absolute;
  top: 0;
  right: 0;
}
.toMerchantRegister {
  position: relative;
}
.toMerchantRegister a {
  position: absolute;
  top: -50px;
  right: 0;
}
a {
  text-decoration: none;
}
</style>

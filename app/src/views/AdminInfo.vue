<template>
  <div>
    <el-main>
      <div class="content">
        <div aria-label="A complete example of page header" style="width: 700px">
          <el-page-header>
            <template #breadcrumb>
              <el-breadcrumb separator="/">
                <el-breadcrumb-item> 个人信息界面 </el-breadcrumb-item>
                <el-breadcrumb-item><a>route 1</a></el-breadcrumb-item>
              </el-breadcrumb>
            </template>
            <template #content>
              <div class="flex items-center">
                <el-avatar
                    v-if="data.img == null"
                    class="mr-3"
                    :size="64"
                    src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                />
                <el-avatar v-else class="mr-3" :size="64" :src="image" />

                <span class="text-large font-600 mr-3">{{ data.userName }}</span>
                <span class="text-sm mr-2" style="color: var(--el-text-color-regular)">
                  Sub title
                </span>
                <el-tag>Default</el-tag>
              </div>
            </template>
            <template #extra>
              <div class="flex items-center">
                <el-button type="primary" class="ml-2" @click="dialogFormVisible = true"
                >Edit</el-button
                >
                <el-dialog
                    title="提示"
                    v-model="dialogFormVisible"
                    width="30%"
                >
                  <el-form :model="form">
                    <el-form-item label="昵称：">
                      <el-input v-model="form.userName" autocomplete="off"  style="width: 350px" />
                    </el-form-item>
                    <el-form-item label="密码：">
                      <el-input v-model="form.password" autocomplete="off"  style="width: 350px" />
                    </el-form-item>
                    <el-form-item label="手机号：">
                      <el-input v-model="form.userPhone" autocomplete="off" style="width: 350px" />
                    </el-form-item>
                    <el-form-item label="地址：">
                      <el-input v-model="form.address" autocomplete="off" style="width: 450px" />
                    </el-form-item>
                    <el-form-item label="头像：" style="width: 350px">
                      <el-upload
                          class="avatar-uploader"
                          :http-request="update"
                          :show-file-list="false"
                          :on-success="handleAvatarSuccess"
                          :before-upload="beforeAvatarUpload"
                      >
                        <img v-if="form.img" :src="form.img" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                      </el-upload>
                    </el-form-item>
                  </el-form>
                  <template #footer>
                    <span class="dialog-footer">
                      <el-button @click="dialogFormVisible = false">取 消</el-button>
                      <el-button type="primary" @click="update">确 定</el-button>
                    </span>
                  </template>
                </el-dialog>
              </div>
            </template>

            <el-descriptions :column="3" size="large" class="mt-4">
              <el-descriptions-item label="账号名:">{{ data.userName }}</el-descriptions-item>
              <el-descriptions-item label="手机号：">{{ data.userPhone }}</el-descriptions-item>

              <el-descriptions-item label="状态">
                <el-tag size="small">{{ roles }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="Address">{{ data.address }} </el-descriptions-item>
            </el-descriptions>
            <p class="mt-4 text-sm">
            </p>
          </el-page-header>
        </div>
      </div>
    </el-main>
  </div>
</template>
<script>
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { onMounted } from 'vue'
import { reactive, ref } from 'vue'
import qs from 'qs'
//import type { UploadProps } from 'element-plus'

const imgUrl = ref('')

export default {
  name: 'adminInfo',
  mounted() {
    axios
        .get('user/userInfo', this.$cookies.get('userId'))
        .then((res) => {
          //this.$cookies.set("img",res.data.data.img)
          // console.log(this.$cookies.get('img'))
          //  console.log(res.data.data.img)
          if (this.$cookies.get('img')!=res.data.data.img){
            this.$cookies.remove('img')
            this.$cookies.set('img',res.data.data.img)
          }
          this.image="http://"+res.data.data.img
         // console.log(this.image)
          this.data = res.data.data
          if (res.data.data.role == 0) {
            this.roles = '普通用户'
          } else if (res.data.data.role == 1) {
            this.roles = '正在申请'
          } else if (res.data.data.role == 2) {
            this.roles = '商家'
          } else if (res.data.data.role == 3) {
            this.roles = '管理员'
          }
        })
        .catch((err) => {
          ElMessage({
            message: '错误',
            type: 'error'
          })
        })
  },
  setup() {
    let dialogFormVisible = ref(false)
    const form = reactive({
      userName: '',
      password: '',
      userPhone: '',
      address: '',
      img: '',
      file: ''
    })

    return {
      dialogFormVisible, // 添加用户弹框
      form
    }
  },
  data() {
    return {
      data: {},
      roles: '',
      image: 'http://' ,
      uploadUrl: 'http://localhost:8081/secondhand/user/userUpdate'
    }
  },
  methods: {
    beforeAvatarUpload(file) {
      console.log('beforeUpload', file)
      this.form.file = file
    },
    update() {
      const formData = new FormData()
      formData.append('user', JSON.stringify(this.form))
      formData.append('file', this.form.file)
      // console.log(this.form.file+"-------------file")
      //console.log(JSON.stringify(formData.get('file'))+'-------------formFile')
      //console.log(formData.get('file'),formData.get('user'))
      //console.log(this.form)
      axios
          .post('user/userUpdate', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          .then((res) => {
           // console.log(res.data)
            if (this.$cookies.get('img')!=res.data.data.img){
              this.$cookies.remove('img')
              this.$cookies.set('img', res.data.data.img)
              this.image='http://'+res.data.data.img
            }
            console.log(this.$cookies.get('img'))
          })
      this.dialogFormVisible = false
      window.location.reload()
      //this.$router.push('/Info/userInfo')
    },

    handleAvatarSuccess(res, file) {
      console.log('handleSuccess', res, file)
      this.form.img.value = URL.createObjectURL(file.raw)
    }
  }
}
</script>

<style scoped>
.personal {
  display: flex;
  height: 955px;
}

.sidebar {
  width: 200px;
  padding: 20px;
  background-color: #f2f2f2;
}

.avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  margin-bottom: 20px;
}

.menu {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.menu li {
  margin-bottom: 10px;
}

.content {
  flex: 1;
  padding: 20px;
  margin: 30px;
  height: 500px;
  width: 799px;
  /*background-size: cover;*/
  /*background-repeat: no-repeat;*/
  /*background-image: url('../assets/login/login2.jpg');*/
  /*background-position: left center;*/
}
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
.mt-4{
  width: 600px;
}
</style>

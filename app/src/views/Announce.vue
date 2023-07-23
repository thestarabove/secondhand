<template>
  <Menu />
  <div class="common-layout">
    <el-container>
      <el-header style="height: 150px"></el-header>
      <el-container>
        <el-aside width="35%"></el-aside>
        <el-container>
          <el-main>
            <div>
              <div style="margin: 20px" />
              <el-form
                :label-position="labelPosition"
                label-width="200px"
                :model="formLabelAlign"
                style="max-width: 460px"
              >
                <el-form-item label="商品名">
                  <el-input v-model="formLabelAlign.cartTitle" />
                </el-form-item>
                <el-form-item label="价格">
                  <el-input v-model="formLabelAlign.price" />
                </el-form-item>
                <el-form-item label="商品介绍">
                  <el-input v-model="formLabelAlign.cartDescription" type="textarea" />
                </el-form-item>
                <el-form-item label="商品库存">
                  <el-input v-model="formLabelAlign.goodNum"  />
                </el-form-item>
                <el-form-item label="图片上传">
                  <el-upload
                    v-model:file-list="fileList"
                    class="upload-demo"
                    action="http://localhost:8081/secondhand/user/seller/uploadImage"
                    :on-success="handleSuccess"
                    :on-preview="handlePreview"
                    :on-remove="handleRemove"
                    list-type="picture"
                    name="image"
                    multiple
                  >
                    <el-empty v-if="fileList == null" :image-size="50" description="请上传图片" />
                    <el-button type="primary" v-else>上传</el-button>
                  </el-upload>
                </el-form-item>
              </el-form>
              <el-form-item>
                <el-button type="primary" @click="upload">发布</el-button>
                <el-button @click="reset">重置</el-button>
              </el-form-item>
            </div>
          </el-main>
          <el-footer></el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts">
import { reactive, ref } from 'vue'
import Menu from '../components/Menu.vue'
import type { UploadProps, UploadUserFile } from 'element-plus'
import axios from 'axios'
import { ElMessage } from 'element-plus'
export default {
  components: {
    Menu
  },
  setup() {
    const labelPosition = ref('top')
    const formLabelAlign = reactive({
      cartTitle: '',
      price: '',
      cartDescription: '',
      goodNum:''
    })
    const fileList = ref<UploadUserFile[]>([])

    const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
      this.fileList = uploadFiles
   //   console.log(uploadFile, uploadFiles)
    }

    const handlePreview: UploadProps['onPreview'] = (file) => {
      //console.log(file)
    }
    return {
      labelPosition,
      formLabelAlign,
      fileList,
      handleRemove,
      handlePreview
    }
  },
  data() {
    return {
      imagesUrl: []
    }
  },
  methods: {
    handleSuccess(response, file, fileList) {
      file.response = response
      const images = fileList.map((file) => file.response.data)
      this.imagesUrl = images
     // console.log(images)
    },
    upload() {
      const formData = new FormData()
      formData.append('goods', JSON.stringify(this.formLabelAlign))
      for (let i = 0; i < this.fileList.length + 1; i++) {
        if (this.imagesUrl[i] !== undefined) {
          formData.append('files', this.imagesUrl[i])
        }
      }
    //  console.log(formData.get('files'))
      axios
        .post('user/seller/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then((res) => {
          console.log(res.data)
          ElMessage({
            message: '发布成功！',
            type: 'success'
          })
          window.location.reload()
        })
        .catch((err) => {
          ElMessage({
            message: '上传失败',
            type: 'warning'
          })
        })
    },
    reset() {
      this.formLabelAlign.cartTitle = ''
      this.formLabelAlign.price = ''
      this.formLabelAlign.cartDescription = ''
      this.formLabelAlign.goodNum=''
    }
  }
}
</script>

<style scoped>
.el-input {
  width: 600px;
  margin-right: 30px;
}
.el-empty {
  height: 50px;
}
</style>

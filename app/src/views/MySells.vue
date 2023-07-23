<template>
  <el-main>
    <el-row>
      <el-col
          v-for="(item, index) in sellGoods"
          :key="index"
          :span="8"
          :offset="sellGoods.length > 0 ? 2 : 0"
          style="width: 20%;"
      >
        <el-card :body-style="{ padding: '0px' }">
          <div class="card-content">
            <div class="image-container">
              <img
                  v-if="item.cartImg == null"
                  src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
                  class="image"
              />
              <img class="image" v-if="item.cartImg != null" :src="sellGoodsImage[index]" />
            </div>
            <div class="details" style="padding: 14px">
              <div class="name">

                商品名称：{{ item.cartTitle }}
              </div>
              <div class="price">价格：{{item.price}}</div>

              <div class="status">状态:{{item.status}}</div>

              <div class="bottom">
                <el-button text class="button" @click="deleteGoods(item.secondHandId)">下架</el-button>
                <el-button text class="button" @click="openDig(item)">修改</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div class="page">
      <el-pagination
          background
          layout="prev, pager, next"
          :total="changePage.total"
          :page-size="4"
          @current-change="handleCurrentChange"
          v-model:current-page="changePage.currentPage"
      />
    </div>
  </el-main>
  <el-dialog v-model="dialogFormVisible" title="Shipping address">
    <el-form :model="form">
      <el-form-item label="商品名称" :label-width="formLabelWidth" >
        <el-input v-model="form.name" autocomplete="off"  style="width: 200px;"/>
      </el-form-item>
      <el-form-item label="商品单价" :label-width="formLabelWidth" >
        <el-input v-model="form.price" autocomplete="off" style="width: 200px;"/>
      </el-form-item>
      <el-form-item label="商品库存" :label-width="formLabelWidth" >
        <el-input v-model="form.goodsNum" autocomplete="off" style="width: 200px;" />
      </el-form-item>
      <el-form-item label="商品简介" :label-width="formLabelWidth" >
        <el-input v-model="form.cartDescription" type="textarea" />
      </el-form-item>
      <el-form-item label="图片上传" :label-width="formLabelWidth">
        <el-upload
            v-model:file-list="fileList"
            class="upload-demo"
            action="http://localhost:8081/secondhand/user/seller/uploadImage"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-success="handleSuccess"
            list-type="picture"
            name="image"
            multiple
        >
          <el-empty v-if="fileList == null" :image-size="50" description="请上传图片" />
          <el-button type="primary" v-else>上传</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="updateGood">
          Confirm
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import { reactive, ref } from 'vue';
import type { UploadProps, UploadUserFile } from 'element-plus';
import {ElMessage} from "element-plus";
import axios from "axios";
export default {
  name: "MySells",
  mounted() {
  this.findGoodsSizeByUser()
  this.findGoodsBySeller()
  },
  setup() {
    const dialogFormVisible = ref(false)
    const formLabelWidth = '240px'
    const likeId=ref()
    const fileList = ref<UploadUserFile[]>([
    ])
    const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
    this.fileList = uploadFiles
     // console.log(fileList)
    }
    const handlePreview: UploadProps['onPreview'] = (file) => {
   // fileList=file
    }
    const form = reactive({
      goodId:'',
      name: '',
      cartDescription:'',
      price:'',
      goodsNum:'',
      images:[]
    })
    const openDig = (item) => {
      form.goodId=item.secondHandId
        console.log(form.goodId)

      dialogFormVisible.value=true
    }
    const deleteGoods=(item)=>{
      console.log(item)
      const secondHandId=item
      axios.get(`user/seller/deleteGood/${secondHandId}`,).then(()=>{
        dialogFormVisible.value=false
        window.location.reload()
        ElMessage({
          message: '下架成功',
          type: 'success'
        })
      }).catch(err=>{
        ElMessage({
          message: '下架失败',
          type: 'warning'
        })
      })
    }

    return {
      deleteGoods,
      likeId,
      openDig,
      dialogFormVisible,
      formLabelWidth,
      form,
      fileList,
      handlePreview,
      handleRemove,
    }
  },
  methods:{
    handleSuccess(response, file, fileList) {
      file.response = response
      const images = fileList.map((file) => file.response.data)
      this.imagesUrl = images
     // console.log(this.imagesUrl)
    },
    findGoodsSizeByUser() {
      axios.get('user/seller/findGoods',).then(res=>{
        // console.log(res)
        this.changePage.total=res.data.data
      //  console.log(this.changePage)

      })
    },
    handleCurrentChange(value) {
      this.changePage.currentPage = value
      // console.log(this.changePage.currentPage)
      // console.log(this.goodData.length)
      this.findGoodsBySeller()
    },
    clearGoodImage() {
      this.sellGoodsImage = []
    },
    findGoodsBySeller(){
        this.clearGoodImage()
        axios.get('user/seller/findGoodsPage',{
          params: {
            state: this.changePage.currentPage
          }
        }).then(res=>{
          this.sellGoods=res.data.data
         // console.log(this.sellGoods)
          for (let i = 0; i < this.sellGoods.length ; i++) {
            const cartImg = this.sellGoods[i]?.cartImg
            //console.log(cartImg)
            if (cartImg && cartImg.length > 0) {
              this.sellGoodsImage.push('http://' + cartImg[0])
            }else {
              this.sellGoodsImage.push("")
            }
          // console.log(this.sellGoodsImage)
          }
         //console.log(this.imagesUrl)
        }).catch(err=>{

        })
    },
    updateGood(){
      const formData =new FormData();
      formData.set("goods",JSON.stringify(this.form))
      //console.log(this.imagesUrl[0])
      for (let i = 0; i <this.fileList.length + 1; i++) {
        if (this.imagesUrl[i] !== undefined) {
          formData.append('files', this.imagesUrl[i])
        }
      }
      axios.post('user/seller/updateGood',formData).then(res=>{
       // console.log(res)
        if (res.data.status==200){
          ElMessage({
            message: '修改成功',
            type: 'success'
          })
          //window.location.reload()
        }
        this.dialogFormVisible=false
      }).catch(err=>{

      })
    }
  },
  data(){
    return{
      sellGoods:[],
      sellGoodsImage:[],
      changePage: {
        currentPage: 1,
        total: 10
      },
      imagesUrl:[]
    }
  }
}
</script>

<style scoped>
.card-content {
  display: grid;
  grid-template-columns: 60% 1fr;
  grid-gap: 10px;

}

.image-container {
  grid-column: 1;
  align-self: center;
}

.image {
  width: 80%;
}

.details {
  grid-column: 2;
  display: flex;
  flex-direction: column;
  justify-content: space-between;

}

.name {
  font-weight: bold;
}

.status-price {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 10px;
}

.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.el-col-8{
  padding-bottom: 10px;
}
.page {
  margin-top: 30px;
  margin-bottom: 20px;
  margin-left: 50%;
}
</style>
<template>
  <el-main>
    <el-row v-if="this.collection!=null&&this.collection!=undefined&&this.collection.length>0">
      <el-col
          v-for="(item, index) in collection"
          :key="index"
          :span="8"
          :offset="collection.length > 0 ? 2 : 0"
          style="width: 20%;"
      >
        <el-card :body-style="{ padding: '0px' }">
          <div class="card-content">
            <div class="image-container">
              <img
                  v-if="item.goods.cartImg == null"
                  src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
                  class="image"
              />
              <img class="image" v-if="item.goods.cartImg != null" :src="collectionImage[index]" />
            </div>
            <div class="details" style="padding: 14px">
              <div class="name">

               商品名称：{{ item.goods.cartTitle }}
              </div>
              <div class="price">价格：{{item.goods.price}}</div>

                <div class="status">状态:{{item.goods.status}}</div>

              <div class="bottom">

                <el-button type="warning" icon="StarFilled" circle @click="openDig(item)" />

                <el-button text class="button" @click="findGood(item.goods.cartTitle)">详情</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div v-else>
      <el-empty description="暂无收藏商品" />
    </div>
  </el-main>
  <el-dialog
      v-model="dialogVisible"
      title="是否取消收藏"
      width="30%"
  >
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">返回</el-button>
        <el-button type="primary" @click="deleteCollection">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script>
import {onMounted, reactive} from 'vue'
//import { useRouter } from "vue-router"; //引入useRouter
import Info from '../components/Info.vue'
import axios from "axios";
import { ElMessageBox } from 'element-plus'
import {ref} from "vue";
export default {

     mounted() {
       axios.get('user/userLike').then(res=>{
        // console.log(res)
         this.collection=res.data.data
        // console.log(this.collection)

         for (let i = 0; i < this.collection.length ; i++) {
           const cartImg = this.collection[i]?.goods.cartImg
           //console.log(cartImg)
           if (cartImg && cartImg.length > 0) {
             this.collectionImage.push('http://' + cartImg[0])
           }else {
             this.collectionImage.push("")
           }
            //console.log(this.collectionImage)
         }
       })
       },
  setup() {
       const dialogVisible=ref()
    const likeId=ref()
    const openDig = (item) => {
      likeId.value=item.likeId
    //  console.log(likeId)
     dialogVisible.value=true
    }
    const deleteCollection=()=>{
        axios.get('user/updateLike',{
          params:{
            likeId:likeId.value
          }
        }).then(()=>{
          dialogVisible.value=false
          window.location.reload()
        })
    }
    return {
      deleteCollection,
      dialogVisible,
      likeId,
      openDig,
    }
  },
  methods:{
  findGood(goodName) {
    const queryGoodName = goodName
    this.$router.push({ path: '/GoodInfo', query: { goodName: queryGoodName } })
  },
  },
  data(){
       return{
         collection:[],
         collectionImage:[]
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
</style>
<template>
  <el-scrollbar height="500px">
    <div v-if="Rating!=null&&Rating!=undefined&&Rating.length>0">
    <div  v-for="(item,index) in Rating" :key="index" class="scrollbar-demo-item">
      <div class="orderInfo">
        <div class="orderInfo-first">
          <div class="order1-title"><el-icon><GoodsFilled /></el-icon> 评价信息</div>
          <div class="order2-title"> 商品名称</div>
          <div class="order3-title"> 商品图片</div>
          <div class="order4-title"> 创建时间</div>
          <div class="order5-title"> 评价状态</div>
          <div class="order6-title"></div>
        </div>
        <div class="orderInfo-content">
          <div class="order1"> </div>
          <div class="order2"> {{item.order.goods.cartTitle}}</div>
          <div class="orderInfo-second">
            <img class="imgOrder" :src="RatingImage[index]" >
          </div>
          <div class="order3"> {{RatingTime[index]  }}</div>
          <div class="order4"> {{  item.state }}</div>
          <div class="order5">
            <div >
            </div>
          </div>
          <div class="order6">
            <el-button type="primary" icon="Search" @click="openPaymentDialog(item)"  >
              评价
            </el-button>

          </div>
        </div>
      </div>
    </div>
    </div>
    <div v-else>
      <el-empty description="暂无订单评价" />
    </div>
  </el-scrollbar>

  <el-dialog v-model="dialogRatingFormVisible" title="Shopping address">
    <el-form :model="form">
      <el-form-item label="订单号" :label-width="formLabelWidth">
        <el-text>{{form.orderId}}</el-text>
      </el-form-item>
      <el-form-item label="商品名称" :label-width="formLabelWidth">
        <el-text>{{form.name}}</el-text>
      </el-form-item>
      <el-form-item label="评价分数" :label-width="formLabelWidth">
        <div class="demo-rate-block">
          <el-rate v-model="form.rate" />
        </div>
      </el-form-item>
      <el-form-item label="评价详情" :label-width="formLabelWidth">
        <el-input v-model="form.formDetail" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogRatingFormVisible = false">取消</el-button>
        <el-button type="primary" @click="completeRating()"  >
          评价
        </el-button>
      </span>
    </template>
  </el-dialog>

</template>

<script>
import axios from "axios";
import { reactive, ref } from 'vue'
import {ElMessage} from "element-plus";
export default {
  name: "MyRating",
  mounted() {
    axios.get('cart/findRating').then(res=>{
      this.Rating=res.data.data
     // console.log(this.Rating)
      for (let i = 0; i < this.Rating.length ; i++) {
        const cartImg = this.Rating[i]?.order.goods.cartImg
        //console.log(cartImg)
        if (cartImg && cartImg.length > 0) {
          this.RatingImage.push('http://' + cartImg[0])
        }
        // console.log(this.orderImage)
      }

      for (let i = 0; i < this.Rating.length ; i++) {
        const orderTime = this.Rating[i]?.createTime
        const  date=new Date(orderTime)
        const year = date.getFullYear(); // 年份
        const month = date.getMonth() + 1; // 月份，注意月份从 0 开始，所以需要加 1
        const day = date.getDate(); // 日期
        const hours = date.getHours(); // 小时
        const minutes = date.getMinutes(); // 分钟
        const seconds = date.getSeconds(); // 秒
// 将年、月、日、小时、分钟、秒拼接为字符串，可以根据需要自定义分隔符和格式
        const formattedDateTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

        if (orderTime!=null) {
          //console.log(formattedDateTime)
          this.RatingTime.push(""+formattedDateTime)
        }
        //console.log(this.orderTime)
      }
    })
  },
  data(){
    return{
      Rating:[],
      RatingImage:[],
      RatingTime:[]
    }
  },
  setup(){
    const dialogRatingFormVisible = ref(false)
    //const value1 = ref(null)
    const formLabelWidth = '140px'
    const form = reactive({
      orderId:'',
      evaluateId:'',
      name: '',
      rate:null,
      formDetail:'',
    })
    const openPaymentDialog=(item)=>{
      form.orderId = item.order.orderId,
          form.name=item.order.goods.cartTitle
        form.evaluateId=item.evaluateId
      dialogRatingFormVisible.value = true;
    }
   const completeRating = (item) => {
      const formData=new FormData()
     formData.append('rating',JSON.stringify(form))
        axios.post('cart/completeRating',formData).then(res=>{
          ElMessage({
            message: '评价成功',
            type: 'success'
          })
        })
     dialogRatingFormVisible.value = false;
   }
    return{
      dialogRatingFormVisible,
      formLabelWidth,
      form,
      openPaymentDialog,
      completeRating
    }
  }
}



</script>

<style scoped>
.scrollbar-demo-item {
  align-items: center;
  height: 180px;
  margin: 10px;
  border-radius: 4px;
  background: #b1b3b8;
  color: #e9e9eb;
}
.imgOrder{
  height: 110px;
  width: 120px;
}
.orderInfo{
  display: block;
  float: left;
  height: 100%;
  width: 100%;
}
.orderInfo-first{
  display: flex;
  float: left;
  margin-top: 5px;
  margin-left: 15px;
  height: 30px;
  width: 100%;
}
.orderInfo-second{
  float: left;
  height: 80px;
}
.orderInfo-content{
  display: flex;
  align-items: center;
  height: 110px;
  margin-left: 15px;
  width: 100%;
}

.orderInfo-second {
  margin-right: 10px;
  margin-left: 10px;
  width: 10%;
}
.order1{
  width: 10%;
}
.order1-title{
  width: 10%;
}
.order2{
  width: 10%;

}
.order2-title{
  width: 13%;
}
.order3{
  width: 18%;
  text-align: center;
}
.order3-title{
  width: 15%;
}
.order4-title{
  width: 15%;
}
.order4{
  width: 13%;
  text-align: center;
}
.order5-title{
  width: 18%;
}
.order5{
  width: 18%;
  text-align: center;
}
.order6-title{
  width: 12%;
}
.order6{
  width: 18%;
  text-align: center;
}
</style>
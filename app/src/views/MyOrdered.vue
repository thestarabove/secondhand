<template>
  <el-scrollbar height="500px">
    <div v-if="order!=null && order!=undefined &&order.length>0 " v-for="(item,index) in order" :key="index" class="scrollbar-demo-item">
      <div class="orderInfo">
        <div class="orderInfo-first">
          <div class="order1-title"><el-icon><GoodsFilled /></el-icon> 订单信息</div>
          <div class="order2-title"> 商品名称</div>
          <div class="order3-title"> 商品图片</div>
          <div class="order4-title"> 创建时间</div>
          <div class="order5-title"> 订单状态</div>
        </div>
        <div class="orderInfo-content">
          <div class="order1"> </div>
          <div class="order2"> {{ item.goods.cartTitle }}</div>
          <div class="orderInfo-second">
            <img class="imgOrder" :src="orderImage[index]">
          </div>
          <div class="order3"> {{ orderTime[index] }}</div>
          <div class="order4"> {{ item.state }}</div>
          <div class="order5">
          </div>
          <div class="order6">
            <el-button type="primary" icon="Search"  @click="openPaymentDialog(item)">
              详情
            </el-button>

          </div>
        </div>
      </div>

    </div>
    <div v-else>
      <el-empty description="暂无购买商品" />
    </div>
  </el-scrollbar>
  <el-dialog v-model="dialogFormVisible" title="Shopping address">
    <el-form :model="form">
      <el-form-item label="订单号" :label-width="formLabelWidth">
        <el-text>{{form.orderId}}</el-text>
      </el-form-item>
      <el-form-item label="商品名称" :label-width="formLabelWidth">
        <el-text>{{form.name}}</el-text>
      </el-form-item>
      <el-form-item label="数量" :label-width="formLabelWidth">
        <el-text>{{form.num}}</el-text>
      </el-form-item>
      <el-form-item label="总价" :label-width="formLabelWidth">
        <el-text>{{form.total}}</el-text>
      </el-form-item>
      <el-form-item label="购买用户" :label-width="formLabelWidth">
        <el-text>{{form.buyerName}}</el-text>
      </el-form-item>
      <el-form-item label="手机号" :label-width="formLabelWidth">
        <el-text>{{form.phone}}</el-text>
      </el-form-item>
      <el-form-item label="地址" :label-width="formLabelWidth">
        <el-text>{{form.address}}</el-text>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary"  @click="dialogFormVisible = false">
         确认
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import router from "../router";
import axios from "axios";
import { reactive, ref } from 'vue'
//import {Search}from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
export default {
  name: "MyOrdered",
  mounted() {
    // console.log(this.$cookies.get("unCompleteOrder"))
    axios.get('/cart/findOrdered').then(res=>{
      // console.log(res.data.data[0])
      this.order=res.data.data
     // console.log(this.order)
      for (let i = 0; i < this.order.length ; i++) {
        const cartImg = this.order[i]?.goods.cartImg
        //console.log(cartImg)
        if (cartImg && cartImg.length > 0) {
          this.orderImage.push('http://' + cartImg[0])
        }
        // console.log(this.orderImage)
      }
      for (let i = 0; i < this.order.length ; i++) {
        const orderTime = this.order[i]?.createTime
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
          this.orderTime.push(""+formattedDateTime)
        }
        //console.log(this.orderTime)
      }
      if (res != undefined && res.data.data.length > 0) {
        //console.log(res.data.data[0]);
      } else {
        ElMessage({
          message: '没有订单',
          type: 'warning'
        });
      }
    }).catch(err=>{
      ElMessage({
        message: '没有订单',
        type: 'warning'
      })
    })

  },

  data(){
    return{
      order:[
      ],
      orderImage:[],
      orderTime:[],
      countdown: [], // 倒计时时长，单位：秒
      timers: [] // 定时器
    }
  },
  methods:{
  },
  setup(){
    const dialogTableVisible = ref(false)
    const dialogFormVisible = ref(false)
    const formLabelWidth = '140px'
    const form = reactive({
      orderId:'',
      name: '',
      num:'',
      total:'',
      buyerName:'',
      phone:'',
      address:'',
    })
    const openPaymentDialog=(item)=>{
      form.orderId = item.orderId;
      form.name = item.goods.cartTitle;
      form.num = item.num;
      form.total = item.total;
      form.buyerName = item.userVo.userName;
      form.phone = item.userVo.userPhone;
      form.address = item.userVo.address;
      dialogFormVisible.value = true;
    }

    return{
      dialogTableVisible,
      dialogFormVisible,
      formLabelWidth,
      form,
      openPaymentDialog,
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
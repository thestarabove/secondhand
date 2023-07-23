<template>
  <el-scrollbar height="500px">
    <div v-if="RatingApp!=null&&RatingApp!=undefined&&RatingApp.length>0" v-for="(item,index) in RatingApp" :key="index" class="scrollbar-demo-item">
      <div class="orderInfo">
        <div class="orderInfo-first">
          <div class="order1-title"><el-icon><GoodsFilled /></el-icon> 评价信息</div>
          <div class="order2-title"> 订单号</div>
          <div class="order3-title"> 创建时间</div>
          <div class="order4-title"> 用户评分</div>
          <div class="order5-title"> 评价内容</div>
        </div>
        <div class="orderInfo-content">
          <div class="order1"> </div>
          <div class="order2"> {{ item.orderId }}</div>
          <div class="orderInfo-second">
            {{this.RatingAppTime[index]}}
          </div>
          <div class="order3"> {{ item.starRating}}</div>
          <div class="order4" >{{item.content}}</div>
          <div class="order6">
            <el-button type="primary"  @click="openDig(item)"   >
              审核
            </el-button>

          </div>
        </div>
      </div>
    </div>
    <div v-else >
      <el-empty description="暂无需审核的评价单" />
    </div>
  </el-scrollbar>

  <el-dialog
      v-model="dialogVisible"
      width="30%"
  >
    <span>确定该评论通过吗</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="failRating">不通过</el-button>
        <el-button type="primary" @click="updateRating">
          通过
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
  name: "RatingApp",
  mounted() {
    // console.log(this.$cookies.get("unCompleteOrder"))
    axios.get('/admin/findRatingApp').then(res=>{
      // console.log(res.data.data[0])
      this.RatingApp=res.data.data
     // console.log(this.RatingApp)
      for (let i = 0; i < this.RatingApp.length ; i++) {
        const orderTime = this.RatingApp[i]?.createTime
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
          this.RatingAppTime.push(""+formattedDateTime)
        }
        console.log(this.RatingAppTime)
      }
      if (res != undefined && res.data.data.length > 0) {
        //console.log(res.data.data[0]);
      } else {
        ElMessage({
          message: '没有订单评价',
          type: 'warning'
        });
      }
    }).catch(err=>{
      ElMessage({
        message: '获取评价失败',
        type: 'warning'
      })
    })

  },

  data(){
    return{
      RatingApp:[
      ],
      RatingAppTime:[

      ]
    }
  },
  methods:{
    allow(){

    }

  },
  setup(){
    const dialogVisible = ref(false)
    const evaluateId=ref()
    const openDig = (item) => {
      evaluateId.value=item.evaluateId
      dialogVisible.value=true
      // console.log(userId)
    }
    const updateRating=(item)=>{
      axios.get('admin/updateRatingApp',{
        params:{
          RatingId:evaluateId.value
        }
      }).then(res=>{
        ElMessage({
          message: '通过成功',
          type: 'success'
        })
        dialogVisible.value=false
        window.location.reload()
      }).catch(err=>{
        ElMessage({
          message: '通过失败',
          type: 'warning'
        })
      })
    }
    const failRating=(item)=>{
      axios.get('admin/failRatingApp',{
        params:{
          RatingId:evaluateId.value
        }
      }).then(res=>{
        ElMessage({
          message: '审核失败',
          type: 'success'
        })
        dialogVisible.value=false
        window.location.reload()
      }).catch(err=>{
        ElMessage({
          message: '审核错误',
          type: 'warning'
        })
      })
    }
    return{
      dialogVisible,
      openDig,
      updateRating,
      failRating
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
  margin-top: 60px;
  height: 80px;
  text-align: center;
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
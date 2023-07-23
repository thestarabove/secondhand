<template>
  <el-scrollbar height="500px">
    <div v-for="(item,index) in userToSell" :key="index" class="scrollbar-demo-item">
      <div class="orderInfo">
        <div class="orderInfo-first">
          <div class="order1-title"><el-icon><GoodsFilled /></el-icon> 用户信息</div>
          <div class="order2-title"> 用户昵称</div>
          <div class="order3-title"> 用户账号</div>
          <div class="order4-title"> 用户手机号</div>
          <div class="order5-title"> 用户地址</div>
          <div class="order6-title"> 用户状态</div>
        </div>
        <div class="orderInfo-content">
          <div class="order1"> </div>
          <div class="order2"> {{ item.userName }}</div>
          <div class="orderInfo-second">
            {{item.account}}
          </div>
          <div class="order3"> {{ item.userPhone}}</div>
          <div class="order4" >{{item.address}}</div>
          <div class="order5" v-if="item.role==1">
            申请中
          </div>
          <div class="order5" v-else-if="item.role==0" >
         普通用户
        </div>
          <div class="order5" v-else-if="item.role==2" >
            商家
          </div>
          <div class="order5" v-else-if="item.role==3" >
            管理员
          </div>
          <div class="order6">

            <el-button type="primary"  @click="openDig(item)"   >
              注销
            </el-button>

          </div>
        </div>
      </div>
    </div>
  </el-scrollbar>

  <el-dialog
      v-model="dialogVisible"
      width="30%"
  >
    <span>确定删除该用户吗</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="updateSeller">
          删除
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
  name: "AllUser",
  mounted() {
    // console.log(this.$cookies.get("unCompleteOrder"))
    axios.get('/admin/getAllUser').then(res=>{
      // console.log(res.data.data[0])
      this.userToSell=res.data.data
    //  console.log(this.userToSell)
      if (res != undefined && res.data.data.length > 0) {
        //console.log(res.data.data[0]);
      } else {
        ElMessage({
          message: '没有用户',
          type: 'warning'
        });
      }
    }).catch(err=>{
      ElMessage({
        message: '获取用户失败',
        type: 'warning'
      })
    })

  },

  data(){
    return{
      userToSell:[
      ],
    }
  },
  methods:{
  },
  setup(){
    const dialogVisible = ref(false)
    const userId=ref()
    const openDig = (item) => {
      userId.value=item.userId
      dialogVisible.value=true
      // console.log(userId)
    }
    const updateSeller=(item)=>{
      axios.get('admin/delete',{
        params:{
          userId:userId.value
        }
      }).then(res=>{
        ElMessage({
          message: '删除成功',
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
    return{
      dialogVisible,
      openDig,
      updateSeller,
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
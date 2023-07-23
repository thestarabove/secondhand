<template>
  <div>
    <Menu/>
  </div>
  <div class="common-layout">
    <el-container>
      <el-header>Header</el-header>
      <el-container>
        <el-aside width="200px">Aside</el-aside>
        <el-container>
          <el-main><div class="shoppingTrolley">
            <div class="content">
              <div class="major">
                <table>
                  <thead>
                  <tr>
                    <td>
                      <el-checkbox v-model="checkAll" @click="goCheckAll" label="全选" size="large" />
                      <!--                <input type="checkbox" v-model="checkAll" @click="goCheckAll()"> 全选-->
                    </td>
                    <td>商品</td>
                    <td>单价</td>
                    <td>数量</td>
                    <td>金额</td>
                    <td>操作</td>
                  </tr>
                  </thead>
                  <tbody ref="tbody">
                  <tr v-for="(item,index) in cartList" :key="index">
                    <td>
                      <el-checkbox @change="goCheck(item)" v-model="item.checked" size="large" />
                      <!--                <input type="checkbox" @click="goCheck(item)" v-model="item.checked"> 选中-->
                    </td>
                    <td>
                      <div class="imgbox">
                        <img :src="item.imgUrl" alt="图片走丢了">
                      </div>
                      <p>{{ item.goodName }}</p>

                    </td>
                    <td>{{ item.price }}</td>
                    <td><el-input-number v-model="item.num" :min="1" :max="10" @change="handleChange(item.num,item.shoppingCar.secondHandId)" /></td>
                    <td style="color: orangered;font-size: 20px">${{item.price * item.num}}</td>
                    <td><el-button type="danger" icon="Delete" circle @click="deleteCart(item.shoppingCar.secondHandId)" /></td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <div class="settlement" ref="drag">
                <div>
                  <el-checkbox v-model="checkAll" @click="goCheckAll" label="全选" size="large" />
                </div>
                <div>

                </div>
                <div>已选商品{{ totalnum }}</div>
                <div>
                  合计 <span style="color: orangered;font-size: 20px">${{totalprice}}</span>
                </div>
                <div>
                  <el-button type="danger" v-if="totalprice==0"   >请选商品</el-button>
                  <el-button type="primary" v-else  @click="open" >下单</el-button>
                </div>
              </div>
            </div>
          </div></el-main>
          <el-footer>Footer</el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>


</template>

<script>
import Menu from '../components/Menu.vue'
import {reactive, toRefs, watch, computed, ref, onMounted} from "vue";
import axios from "axios";
import { markRaw } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {CircleCheckFilled} from '@element-plus/icons-vue'
import router from "../router";
export default {
  name: "cart",
  components:{
    Menu
  },
  setup(){
    let data = reactive({
      //是否全选
      checkAll:false,
      //总合计价格
      totalprice:0,
      //总数量
      totalnum:0,
      handleChange(value,goodId){
      //  console.log(value)
       // console.log(goodId)
        if (value>1){
          axios.get('cart/amount',{
            params:{
                 Num:value,
              GoodId:goodId
            }
          }).then(res=>{
          // console.log(res)
          })
        }

      }
    })
    //购物车数据 此处数据应来自服务器接口
    let cartList = reactive([
    ])
    let selectGoods=reactive([])
    function goCheck(item){
      //数值是点击之前的值 所以要取反
      //定义一个长度
    //  console.log('ggggggggg')
       const index=selectGoods.indexOf(item.shoppingCar.secondHandId)
      if (item.checked){
        selectGoods.push(item.shoppingCar.secondHandId)
      }else {
               if (index !==-1){
                 selectGoods.splice(index,1)
               }
      }

      console.log(selectGoods)
      let length = 1
      cartList.forEach((i) => {
        if(i.checked){
          length ++
         // console.log(length)

        }
      })
      //console.log(item)
      if (length === cartList.length+1){
        data.checkAll = true
      }else {
        data.checkAll = false
      }

    }

    function goCheckAll(){
      //数值是点击之前的值 所以要取反
      if(!data.checkAll){
        cartList.forEach((item) => {
          item.checked = true
        })
      }else {
        cartList.forEach((item) => {
          item.checked = false
        })
      }

    }

    //计算属性
    data.totalprice = computed(() => {
      let totalprice = 0

      cartList.forEach((item) => {
        if(item.checked){
          totalprice += parseInt(item.price) * item.num
        }
      })
      return totalprice
    })

    data.totalnum = computed(() => {
      let totalnum = 0

      cartList.forEach((item) => {
        if(item.checked){
          totalnum ++
        }
      })
      return totalnum
    })

    const drag = ref(null)

    const tbody = ref(null)

    let numValue = ref(0)
    const imagesUrl=ref()
    onMounted(() => {
      numValue = tbody.value.offsetHeight + tbody.value.offsetTop
       getCart()
      //获取滚动条的高度
      // window.addEventListener('scroll',handleScroll,true)
    })
   function getCart(){
     axios.get('cart/findCart').then(res=>{
       for (let i=0;i<res.data.data.length;i++){
         cartList.push(res.data.data[i])
       }
       // cartList.push(res.data.data[0])
       // cartList.push(res.data.data[1])
       //
       // console.log(res.data.data)
       // console.log('xxxxxxxxxxx')
       // console.log(cartList)
     }).catch(err=>{
       ElMessage({
         message:"获取购物车数据失败",
         type:"warning"
       })
     })
   }

   function deleteCart(id){
      axios.get('cart/deleteCart',{
        params:{
          secondHandId:id
        }
      }).then(res=>{
        ElMessage({
          message:"删除成功",
          type:"success"
        })
      window.location.reload()
      }).catch(err=>{
        ElMessage({
          message:"获取购物车数据失败",
          type:"warning"
        })
      })

   }

   function sendOrder(){
   }

    const open = () => {
      ElMessageBox.confirm(
          '即将下单，请确认',
          '提示',
          {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
            icon:markRaw(CircleCheckFilled)
          }
      )
          .then(() => {
            axios.get('cart/sendOrder',{
              params:{
               secondHandId:selectGoods.join(',')

              }
            }).then(res=>{
              const orderId=res.data.data
            //  console.log(orderId)
              $cookies.set("unCompleteOrder",orderId)
              router.push({name:'MyOrder'})
              ElMessage({
                type: 'success',
                message: '下单成功',
              })

            })
          })
          .catch(() => {
            ElMessage({
              type: 'info',
              message: '取消下单',
            })
          })
    }
    /*
    function handleScroll(){
      let top = Math.floor(document.body.scrollTop || document.documentElement.scrollTop || window.pageYOffset)
      if(top < (numValue - 132)){
        drag.value.style = 'position: fixed;\n' +
              '    bottom: 0;\n' +
              '    z-index: 10;\n' +
              '    width: 1250px;'
        }else {
        drag.value.style = ' '
      }
    }*/

    return {
      ...toRefs(data),
      cartList,
      goCheckAll,
      goCheck,
      drag,
      tbody,
      getCart,
      deleteCart,
      open,
      selectGoods
    }

  },
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}
.shoppingTrolley {

}
.shoppingTrolley .content {
  width: 1250px;
  margin: 0 auto;
  min-height: 700px;
  box-sizing: border-box;
}
.shoppingTrolley .content .major {
  border: 1px solid skyblue;
  border-radius: 10px 10px 0 0;
}
table {
  width: 100%;
}
table thead {

}
table thead tr {
  box-shadow: #8ef1d5 5px 5px 0px;
  border-bottom: 1px solid #cbc5c5;
  background: #ebebeb;
  border-radius: 10px;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  text-align: center;
}
table thead tr td{
  border-right: 1px solid rosybrown;
  flex: 1;
}
table thead tr td:nth-of-type(2) {
  flex: 3;
}
tbody .imgbox {
  width: 80px;
  overflow: hidden;
}
tbody .imgbox img {
  height: 80px;
  width: 100%;
}
table tbody tr{
  padding: 20px 10px;
  border-bottom: 1px solid rosybrown;
  height: 100px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  text-align: center;
}
table tbody tr td{
  flex: 1;
}
table tbody tr td:nth-of-type(2) {
  flex: 3;
  display: flex;
  align-items: center;
  justify-content: space-evenly;
}
.settlement {
  display: flex;
  height: 80px;
  align-items: center;
  border-radius: 0 0 10px 10px;
  border: 1px solid rebeccapurple;
  justify-content: space-around;
  background: #f5f8fa;
}
.settlement div:last-of-type button{
  border: 0;
  width: 70px;
  height: 40px;
  border-radius: 10px;
  font-weight: bolder;
  cursor: pointer;
}
</style>
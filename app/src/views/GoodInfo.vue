<template>
  <Menu />
  <div class="common-layout">
    <el-container>
      <el-header>Header</el-header>
      <el-container>
        <el-aside width="500px" style="height: 500px">
          <div class="aside">
            <el-carousel height="400px">
              <el-carousel-item v-for="(o, item) in goodImage.length" :key="o">
                <img class="images" :src="goodImage[item]" alt="" />
              </el-carousel-item>
            </el-carousel>
          </div>
          Aside</el-aside
        >
        <el-container>
          <el-main>
            <div>
              <el-descriptions title="商品简介：" :column="4" direction="vertical" border>
                <el-descriptions-item label="商品名称">{{
                  this.goodsName[0]?.cartTitle
                }}</el-descriptions-item>
                <el-descriptions-item label="发布时间">{{
                  this.createTime[0]
                }}</el-descriptions-item>
                <el-descriptions-item label="更新时间" :span="2">{{
                  this.updateTime[0]
                }}</el-descriptions-item>

                <el-descriptions-item label="Remarks">
                  <el-tag size="small">{{ this.goodsName[0]?.status }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="价格" :span="2">{{
                  this.goodsName[0]?.price
                }}</el-descriptions-item>
                <el-descriptions-item label="收藏" :span="2"
                  >
                  <div v-if="this.goodLiked===false">
                  <el-icon><Star /></el-icon>
                  </div>
                  <div v-else>
                    <el-icon><StarFilled /></el-icon>
                  </div>
                </el-descriptions-item>

                <el-descriptions-item label="商品描述">
                  {{ this.goodsName[0]?.cartDescription }}
                </el-descriptions-item>
              </el-descriptions>
            </div>
            <div class="purchase">
              <el-button type="danger" round>立即购买</el-button>
              <el-button type="warning" round @click="addCart">加入购物车</el-button>
            </div>
            Main
          </el-main>
          <el-footer>Footer</el-footer>
        </el-container>
        <el-aside width="400px">
          <div></div>
          Aside</el-aside
        >
      </el-container>
    </el-container>
  </div>

  <el-backtop :right="100" :bottom="100" />
</template>
<script>
import Menu from '../components/Menu.vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
export default {
  name: 'GoodInfo',
  components: {
    Menu
  },
  mounted()  {
    this.getGood()
        .then(() => {
          this.findGoodLikeByUser();
        })
        .catch((error) => {
          console.error(error);
        });
  },
  data() {
    return {
      goodImage: [],
      goodsName: {
        cartTitle: ''
      },
      goodLiked:false,
      createTime:[],
      updateTime:[]
    }
  },
  methods: {
    clearGoodImage() {
      this.goodImage = []
    },
    getGood() {
      this.clearGoodImage();
      return new Promise((resolve, reject) => {
        axios
            .get('good/searchGood', {
              params: {
                goodName: this.$route.query.goodName,
              },
            })
            .then((res) => {
             // console.log(res.data);
              this.goodsName = res.data.data;
              const cartImg = this.goodsName[0]?.cartImg;
              const secondHandId = this.goodsName[0]?.secondHandId;
              for (let i = 0; i < cartImg.length; i++) {
                if (cartImg && cartImg.length > 0) {
                  this.goodImage.push('http://' + cartImg[i]);
                }
              }
             // console.log(this.goodsName[0].secondHandId);

                const orderTime = this.goodsName[0]?.createTime
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
                  this.createTime.push(""+formattedDateTime)
                }
                //console.log(this.orderTime)

            this.findUpdateTime(this.goodsName[0]?.updateTime)
              resolve();
            })
            .catch((error) => {
              //console.error(error);
              reject(error);
            });
      });
    },
    findUpdateTime(time){
      const orderTime = time
      //console.log(orderTime)
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
        this.updateTime.push(""+formattedDateTime)
      }
     // console.log(this.updateTime)
    },
    addCart() {
      axios
        .get('cart/addCart', {
          params: {
            secondHandId: this.goodsName[0].secondHandId
          }
        })
        .then((res) => {
          ElMessage({
            Message: '加入成功！',
            type: 'success'
          })
        })
        .catch((err) => {
          ElMessage({
            Message: '加入失败！',
            type: 'warning'
          })
        })
    },
    findGoodLikeByUser() {
      if (this.goodsName && this.goodsName.length > 0) {
        axios
            .get('user/GoodLikeByUser', {
              params: {
               goodLike: this.goodsName[0].secondHandId,
              },
            })
            .then((res) => {
              //console.log(res);
              this.goodLiked=res.data.data
              //console.log(this.goodLiked)
            })
            .catch((error) => {
              console.error(error);
            });
      }
    }
  }
}
</script>

<style scoped>
.common-layout {
  --el-header-height: 160px;
}
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
  background-size: cover;
  background-repeat: no-repeat;
  background-position: left center;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
  background-size: cover;
  background-repeat: no-repeat;
  background-position: left center;
}
.aside {
  margin-left: 60px;
  margin-right: 60px;
  margin-top: 60px;
}
.el-descriptions {
}
.images {
  background-size: cover;
  background-repeat: no-repeat;
  background-position: left center;
}
.purchase {
  margin-left: 700px;
  margin-top: 10px;
}
</style>

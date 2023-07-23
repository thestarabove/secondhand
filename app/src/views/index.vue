<template>
  <Menu />
  <el-carousel :interval="4000" type="card" height="400px">
    <el-carousel-item v-for="item in imageUrl" :key="item">
      <img class="el-carousel__item" :src="item.url" alt="" />
    </el-carousel-item>
  </el-carousel>

  <div>
    <div class="notice-box" @mouseenter="mouseEnter" @mouseleave="mouseLeave">
      <transition name="notice-slide">
        <p class="notice-item" :key="noticeList.id">
          {{ noticeList.text }}
        </p>
      </transition>
    </div>
  </div>
  <div class="menu">
    <h1>商品</h1>
  </div>

  <div class="row">
    <el-row>
      <el-col
        v-for="(o, index) in goodData.length"
        :key="o"
        :span="8"
        :offset="goodData.length > 0 ? 2 : 0"
      >
        <el-card :body-style="{ padding: '0px' }" style="background-color: #f5f7fa">
          <div class="card-content" >
            <div class="image-container" >
            <img
              v-if="goodData[index]?.cartImg == null"
              src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
              class="image"
            />
            <img class="image" v-if="goodData[index]?.cartImg != null" :src="goodImage[index]" />
          </div>
          <div style="padding: 14px">
            <span>{{ goodData[index]?.cartTitle }}</span>
            <div class="price" >
            <h2>¥:{{ goodData[index]?.price }}</h2>
            </div>
            <div class="bottom">
              <el-button  type="primary" class="button" @click="purchase(goodData[index]?.cartTitle)"
                >购买</el-button
              >
            </div>
          </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
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
</template>

<script>
import Menu from '../components/Menu.vue'
import axios from 'axios'
import { reactive } from 'vue'
const imageUrl = [
  { url: 'http://localhost:8081/images/b2017a3b1d82.jpeg' },
  { url: 'http://localhost:8081/images/a3fa2a0b9ad1.jpeg' },
  {
    url: 'http://localhost:8081/images/30ede70fea0a.jpeg'
  }
]
export default {
  name: 'index',
  components: {
    Menu
  },
  data() {
    return {
      imageUrl,
      goodImage: [],
      noticeArr: [{ title: '【二手商城公告】\n'
           }, { title:   '\n' +
            '尊敬的用户，\n' +
            '\n' +
            '我们很高兴地宣布，我们的二手商城正式上线！这是一个专为您提供买卖二手商品的平台，旨在为用户们提供一个便捷、安全、可靠的交易环境。' }, { title: '我们诚挚邀请您加入我们的二手商城，并开始享受买卖二手商品的乐趣！如果您有任何疑问或建议，请随时联系我们的客服团队。\n' +
            '\n' +
            '祝您愉快的二手交易体验！' }],
      number: 0,
      goodId: 0,
      changePage: {
        currentPage: 1,
        total: 10
      },
      goodData: [{ cartImg: '' }]
    }
  },
  computed: {
    noticeList() {
      return {
        id: this.number,
        text: this.noticeArr[this.number].title
      }
      timer: null
    }
  },
  mounted() {
    this.scrollMove(), this.getGoodsSize(), this.getGood(), this.goodData
  },

  methods: {
    scrollMove() {
      this.timer = setTimeout(() => {
        if (this.number == this.noticeArr.length - 1) {
          this.number = 0
        } else {
          this.number += 1
        }
        this.scrollMove()
      }, 5000)
    },
    mouseEnter() {
      clearInterval(this.timer)
    },
    mouseLeave() {
      this.scrollMove()
    },
    getGoodsSize() {
      axios.get('good/findAllGood').then((res) => {
        //console.log(res.data.data)
        this.changePage.total = res.data.data
      })
    },
    clearGoodImage() {
      this.goodImage = []
    },
    getGood() {
      this.clearGoodImage()
      axios
        .get('good/findGood', {
          params: {
            state: this.changePage.currentPage
          }
        })
        .then((res) => {
          this.goodData = res.data.data
          //localStorage.setItem('goodData',JSON.stringify(this.goodData))
          //console.log(storedGoodData[0])
         // console.log(this.goodData[2])
          for (let i = 0; i < 5; i++) {
            const cartImg = this.goodData[i]?.cartImg
            if (cartImg && cartImg.length > 0) {
              this.goodImage.push('http://' + cartImg[0])
            }
          }
          //console.log(this.goodData[2].cartImg[0])
        })
    },
    purchase(goodName) {
      const queryGoodName = goodName
      this.$router.push({ path: '/GoodInfo', query: { goodName: queryGoodName } })
    },
    handleCurrentChange(value) {
      this.changePage.currentPage = value
      // console.log(this.changePage.currentPage)
      // console.log(this.goodData.length)
      this.getGood()
    }
  }
}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 400px;
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
.notice-box {
  width: 1700px;
  height: 80px;
  margin: 0 auto;
  overflow: hidden;
  position: relative;
  text-align: center;
  font-size: 19px;
  border: 2px solid black;
  background-color: #f5f7fa;
}
.notice-item {
  width: 100%;
  height: 30px;
  line-height: 30px;
  box-sizing: border-box;
  position: absolute;
  top: 0;
}
.notice-slide-enter-active,
.notice-slide-leave-active {
  transition: all 0.8s linear;
}
.notice-slide-enter {
  top: 30px;
}
.notice-slide-leave-to {
  top: -30px;
}
.bottom {
  position: absolute;
  bottom: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}


.el-col-8 {
  max-width: 19%;
  margin-top: 10px;

}
.el-col-offset-2{
  margin-left: 5%;
}
.image {
  width: 150px;
  display: block;
  position: center;
}
.row {
  width: 85%;
}
.menu {
  margin-top: 30px;
  margin-bottom: 20px;
}
.page {
  margin-top: 30px;
  margin-bottom: 20px;
  margin-left: 50%;
}
.card-content {
  display: grid;
  grid-template-columns: 70% 1fr;
}
.image-container {
  grid-column: 1;
  align-self: center;
}
.price {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 10px;
}

</style>

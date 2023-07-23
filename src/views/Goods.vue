<template>
  <Menu />
  <div class="common-layout">
    <el-container>
      <el-header></el-header>
      <el-container>
        <el-container>
          <el-main>
            <div>
              <div class="row">
                <el-row>
                  <el-col
                    v-for="(o, index) in goodData.length"
                    :key="o"
                    :span="8"
                    :offset="goodData.length > 0 ? 2 : 2"
                  >
                    <el-card :body-style="{ padding: '0px' }" style="background-color: #f5f7fa">
                      <div>
                        <img
                          v-if="goodData[index]?.cartImg == null"
                          src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
                          class="image"
                        />
                        <img
                          class="image"
                          v-if="goodData[index]?.cartImg != null"
                          :src="goodImage[index]"
                        />
                      </div>
                      <div style="padding: 14px">
                        <span>{{ goodData[index]?.cartTitle }} </span>
                        <div class="bottom">
                          <h2>¥: {{ goodData[index]?.price }}</h2>
                          <h3></h3>
                          <el-button
                            text
                            class="button"
                            @click="purchase(goodData[index]?.cartTitle)"
                            >购买</el-button
                          >
                        </div>
                      </div>
                    </el-card>
                  </el-col>
                </el-row>
              </div>
            </div>

          </el-main>
          <el-footer></el-footer>
        </el-container>
        <el-aside width="400px">
          <div></div>
          </el-aside
        >
      </el-container>
    </el-container>
  </div>

  <el-backtop :right="100" :bottom="100" />
</template>

<script>
import axios from 'axios'
import Menu from '../components/Menu.vue'
export default {
  name: 'Goods',
  components: {
    Menu
  },
  data() {
    return {
      goodImage: [],
      changePage: {
        currentPage: 1,
        total: 10
      },
      goodData: [{ cartImg: '' }]
    }
  },
  mounted() {
    this.getGood()
  },
  methods: {
    clearGoodImage() {
      this.goodImage = []
    },
    getGood() {
      axios
        .get('good/searchGood', {
          params: {
            goodName: this.$route.query.goodName
          }
        })
        .then((res) => {
         // console.log(res.data)
          this.goodData = res.data.data
          for (let i = 0; i < this.goodData.length + 1; i++) {
            const cartImg = this.goodData[i]?.cartImg
            if (cartImg && cartImg.length > 0) {
              this.goodImage.push('http://' + cartImg[0])
            }
          }
        })
    },
    purchase(goodName) {
      const queryGoodName = goodName
      this.$router.push({ path: '/GoodInfo', query: { goodName: queryGoodName } })
    }
  }
}
</script>

<style scoped>
.common-layout {
}
.el-header {
  --el-header-height: 100px;
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
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
.aside {
  margin-left: 60px;
  margin-right: 60px;
  margin-top: 60px;
}
.el-col-8 {
  max-width: 220px;
  background-color: #e9e9eb;
  margin-top: 15px;
}
.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.button {
  padding: 0;
  min-height: auto;
}
.image {
  width: 150px;
  display: block;
  position: center;
}
</style>

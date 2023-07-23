<template>
  <el-affix :offset="0">
    <el-menu
      class=""
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      :ellipsis="false"
      style="width: 100%;"
    >
      <el-menu-item index="0"
        ><img style="max-width: 50px" src="../assets/logo.jpg"
      /></el-menu-item>
      <div class="flex-grow">
        <el-autocomplete
            v-model="goodName"
            :fetch-suggestions="fetchSuggestions"
            placeholder="搜索商品"
            @select="selectSearchHistory"
            class="autocomplete"
        >
          <template #append>
            <el-button @click="searchGood" slot="prepend">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
          <template #default="{ item }">
            <div class="flex-container">
              <div class="flex-item">
                {{ item }}
              </div>
              <div class="flex-item">
                <el-button size="small" type="danger" icon="Delete" circle @click.stop="deleteSearch(item)" />
              </div>
            </div>
          </template>
        </el-autocomplete>
      </div>
      <div class="mt-4"></div>
      <el-menu-item index="1">欢迎您！ {{ this.$cookies.get('account') }}</el-menu-item>
      <el-sub-menu index="2">
        <template #title>我的账号</template>
        <el-menu-item index="2-1" @click="collection">我的收藏</el-menu-item>
        <el-menu-item index="2-2" @click="Info">我的信息</el-menu-item>
        <el-sub-menu v-if="this.$cookies.get('role') == 2" index="2-3">
          <template #title>我发布的</template>
          <el-menu-item index="2-3-1" @click="sells">正在售卖</el-menu-item>
          <el-menu-item index="2-3-2" @click="selled">已卖出的</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="2-4">
          <template #title>我的订单</template>
          <el-menu-item index="2-4-1" @click="ordering">未确认的订单</el-menu-item>
          <el-menu-item index="2-4-2" @click="rating">未评价的订单</el-menu-item>
          <el-menu-item index="2-4-3" @click="ordered">已完成的订单</el-menu-item>
        </el-sub-menu>
      </el-sub-menu>
      <el-menu-item index="3" @click="Cart">购物车</el-menu-item>
      <el-menu-item index=""></el-menu-item>
      <el-menu-item index=""></el-menu-item>
      <el-menu-item v-if="this.$cookies.get('role') == 2" index="" @click="purchaseGood"
        >发布商品</el-menu-item
      >

      <el-menu-item index="4" @click="index">首页</el-menu-item>
      <el-menu-item index="5">客服</el-menu-item>
      <el-menu-item v-if="this.$cookies.get('role') == 0" @click="appSeller" index="6"
        >申请成为卖家</el-menu-item
      >
      <el-menu-item index="7" @click="Out">退出</el-menu-item>
    </el-menu>
  </el-affix>
</template>

<script lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import {g, q} from "vitest/dist/types-ad1c3f45";
//import { Search } from '@element-plus/icons-vue'
const activeIndex = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
export default {
  name: 'Menu',
  methods: {
    Out() {
      this.$router.push('/Login')
      this.$cookies.clear
      axios
        .get('user/loginOut')
        .then((res) => {
          console.log(res)
        })
        .catch((error) => {
          ElMessage({
            message: '退出失败',
            type: 'warning'
          })
        })
    },
    Info() {
      this.$router.push('/Info')
    },
    index() {
      this.$router.push('/index')
    },
    purchaseGood() {
      this.$router.push('/Announce')
    },
    Cart() {
      this.$router.push('/Cart')
    },
    collection(){
      this.$router.push('/Info/MyCollection')
    },
    sells(){
      this.$router.push('/Info/MySells')
    },
    selled(){
      this.$router.push('/Info/Myselled')
    },
    ordering(){
      this.$router.push('/Info/MyOrder')
    },
    ordered(){
      this.$router.push('/Info/MyOrdered')
    },
    appSeller(){
      axios.get('user/applicant').then(res=>{
        ElMessage({
          message: '正在申请',
          type: 'success'
        })
      })
    },
    rating(){
      this.$router.push('/Info/MyRating')
    },
    // 获取搜索建议的方法，根据输入的关键词过滤搜索记录
    fetchSuggestions(queryString, callback) {
      axios.get('good/searchHistory').then(res => {
        this.searchRecords = res.data.data;

        const searchResults = this.searchRecords.filter(record =>
            record.toLowerCase().includes(queryString.toLowerCase())
        );

        callback(searchResults);
      }).catch(error => {
        console.error(error);
        // 处理请求错误的逻辑
        callback([]); // 若请求错误，可将空数组传递给回调函数
      });
    },
    // 用户选择搜索记录后的回调方法
    selectSearchHistory(value) {
      this.goodName = value;
      this.searchGood();
    },
    // 执行搜索商品的方法
  searchGood() {
    const queryGoodName = this.goodName
    this.$router.push({ path: '/Goods', query: { goodName: queryGoodName } })
    this.goodName = '';
    this.searchRecords = [];
  },
    deleteSearch(item){
      axios.get('good/deleteHistory',{
        params:{
          goodName:item
        }
      }).then(res=>{
        console.log(res)
      })
    }
  },
  data() {
    return {
      goodName: '',
      searchRecords:[],
    }
  }
}
</script>

<style>
.flex-grow {
  width: 700px;
}
.mt-4 {
  width: 300px;
}

.w-50 {
  margin-top: 10px;
  margin-left: 300px;
  width: 300px;
}
.but {
  margin-bottom: 10px;
  margin-left: 650px;
}
.autocomplete{
  width: 300px;
  margin-left: 300px;
}
.flex-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.flex-item {
  flex: 1;
}
</style>

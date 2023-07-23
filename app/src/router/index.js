import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/index',
      name: 'index',
      component: () => import('../views/index.vue')
    },
    {
      path: '/',
      name: 'home',
      component: () => import('../components/UserLogin.vue')
    },
    {
      path: '/Admin',
      name: 'Admin',
      //redirect:'/Admin/userInfo',
      component: () => import('../views/Admin.vue'),
      children:[
        {
          path: 'AdminInfo',
          name: 'AdminInfo',
          // route level code-splitting
          // this generates a separate chunk (About.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import('../views/AdminInfo.vue')
        },
        {
          path: 'Applicant',
          name: 'Applicant',
          // route level code-splitting
          // this generates a separate chunk (About.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import('../views/Applicant.vue')
        },
        {
          path: 'RatingApp',
          name: 'RatingApp',
          // route level code-splitting
          // this generates a separate chunk (About.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import('../views/RatingApp.vue')
        },
        {
          path: 'AllUser',
          name: 'AllUser',
          // route level code-splitting
          // this generates a separate chunk (About.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import('../views/AllUser.vue')
        },

      ]
    },
    {
      path: '/login',
      name: 'login',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../components/UserLogin.vue')
    },
    {
      path: '/register',
      name: 'register',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../components/UserRegister.vue')
    },
    {
      path: '/Info',
      name: 'Info',
      redirect: '/Info/userInfo',
      component: () => import('../components/Info.vue'),
      children: [
        {
          path: 'UserInfo',
          name: 'UserInfo',
          // route level code-splitting
          // this generates a separate chunk (About.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import('../views/UserInfo.vue')
        },
        {
          path: 'MyCollection',
          name: 'MyCollection',
          component: () => import('../views/MyCollection.vue')
        },
        {
          path: 'MyOrder',
          name: 'MyOrder',
          component: () => import('../views/MyOrder.vue')
        }
        ,
        {
          path: 'MyRating',
          name: 'MyRating',
          component: () => import('../views/MyRating.vue')
        }
        ,
        {
          path: 'MySells',
          name: 'MySells',
          component: () => import('../views/MySells.vue')
        },
        {
          path: 'MyOrdered',
          name: 'MyOrdered',
          component: () => import('../views/MyOrdered.vue')
        },
        {
          path: 'MyRated',
          name: 'MyRated',
          component: () => import('../views/MyRated.vue')
        },
        {
          path: 'Myselled',
          name: 'Myselled',
          component: () => import('../views/Myselled.vue')
        }
      ]
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
    },
    {
      path: '/GoodInfo',
      name: 'GoodInfo',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/GoodInfo.vue')
    },
    {
      path: '/Goods',
      name: 'Goods',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/Goods.vue')
    },
    {
      path: '/Announce',
      name: 'Announce',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/Announce.vue')
    },

    {
      path: '/Cart',
      name: 'Cart',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/Cart.vue')
    }
  ]
})

export default router

import {createRouter, createWebHistory} from 'vue-router'
import store from "@/store";

const routes = [
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/the-login.vue'),
    },
    {
        path: '/',
        name: 'main',
        component: () => import('../views/the-main.vue'),
        meta: {
            authRequire: true
        },
    },
    {
        path: '/401',
        name: '401',
        component: () => import('../views/no-auth.vue'),
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

router.beforeEach((to, from, next) => {
    if (to.matched.some(item => item.meta.authRequire)) { // 鉴权
        const _user = store.state.user;
        if (_user) { // 查询本地存储信息是否已经登陆
            next();
        } else {
            next({
                path: '/login',
                redirect: from // 登陆成功后回到当前页面，这里传值给login页面，to.fullPath为当前点击的页面
            });
        }
    } else {
        next();
    }
})

export default router

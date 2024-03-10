import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/the-login.vue')
    },
    {
        path: '/',
        name: 'main',
        component: () => import('../views/the-main.vue')
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router

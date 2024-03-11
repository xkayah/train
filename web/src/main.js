import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from "ant-design-vue";
import 'ant-design-vue/dist/antd.css'
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";

const app = createApp(App);
app.use(Antd).use(store).use(router).mount('#app')

const icons = Icons;
for (const i in icons) {
    app.component(i, icons[i])
}

/**
 * axios 配置
 */

const TOKEN_PREFIX = "Bearer ";

axios.interceptors.request.use(config => {
    console.log("==>INPUT req:", config);
    const _token = store.state.user.token;
    if (_token) {
        config.headers.Authorization = TOKEN_PREFIX + _token;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

axios.interceptors.response.use(resp => {
    console.log("<==OUT resp:", resp.data);
    return resp.data;
}, error => {
    console.log("resp err:", error);
    if (error.response.status === 401) {
        console.log("status", error.response.status);
        store.commit("setUser", {});
        router.push('/login?rtUrl=' + (encodeURIComponent(router.currentRoute.value.fullPath) || ""));
    }
    return Promise.reject(error);

});

axios.defaults.baseURL = process.env.VUE_APP_SERVER

console.log("env:", process.env.NODE_ENV)
console.log("serve:", process.env.VUE_APP_SERVER)
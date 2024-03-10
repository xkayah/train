import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from "ant-design-vue";
import 'ant-design-vue/dist/antd.css'
import * as Icons from '@ant-design/icons-vue';
import Axios from "axios";

const app = createApp(App);
app.use(Antd).use(store).use(router).mount('#app')

const icons = Icons;
for (const i in icons) {
    app.component(i, icons[i])
}

/**
 * axios 配置
 */
Axios.interceptors.request.use(config => {
    console.log("input params:", config);
    return config;
}, error => {
    return Promise.reject(error);
});

Axios.interceptors.response.use(resp => {
    console.log("out res:", resp);
    return resp;
}, error => {
    console.log("resp err:", error);
    return Promise.reject(error);
});

Axios.defaults.baseURL = process.env.VUE_APP_SERVER

console.log("env:", process.env.NODE_ENV)
console.log("serve:", process.env.VUE_APP_SERVER)
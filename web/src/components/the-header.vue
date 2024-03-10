<template>
    <a-layout-header class="header">
        <div class="header-logo">
            <div>
                <router-link to="/">
                    <h2>logo</h2>
                </router-link>
            </div>
        </div>
        <div class="header-user-info">
            <div>
                {{ user.uname }}
                <router-link to="/login">退出登录</router-link>
            </div>
        </div>
    </a-layout-header>
</template>

<script>
import {defineComponent} from 'vue';
import store from "@/store";

export default defineComponent({
    name: "the-header",
    setup() {
        let user = store.state.user;
        return {
            user,
        };
    },
});
</script>

<style scoped>
.header {
    background: #fff;
    padding: 0;
}

.header-logo {
    float: left;
    padding-left: 1.5%;
}

.header-user-info {
    float: right;
    padding-right: 1.5%;
}
</style>

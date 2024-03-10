<template>
    <a-layout style="min-height: 100vh" id="components-layout-demo-side">
        <the-sider/>
        <a-layout>
            <the-header/>
            <a-layout-content style="margin: 0 16px">
                <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
                    Bill is a cat.
                </div>
                <h2>所有用户总数：{{ count }}</h2>
            </a-layout-content>
            <the-footer/>
        </a-layout>
    </a-layout>
</template>
<script>
import {defineComponent, ref} from 'vue';
import TheSider from "@/components/the-sider.vue";
import TheHeader from "@/components/the-header.vue";
import TheFooter from "@/components/the-footer.vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
    components: {
        TheFooter,
        TheHeader,
        TheSider,
    },
    setup() {
        const count = ref(0);
        axios.get("/ucenter/user/count")
            .then(resp => {
                console.log("count:", resp)
                if (resp.code === 200) {
                    count.value = resp.data;
                } else {
                    notification.error({description: resp.msg})
                }
            })
        return {
            collapsed: ref(false),
            selectedKeys: ref(['1']),
            count,
        };
    },
});
</script>
<style>
#components-layout-demo-side .logo {
    height: 32px;
    margin: 16px;
    background: rgba(255, 255, 255, 0.3);
}

.site-layout .site-layout-background {
    background: #fff;
}

[data-theme='dark'] .site-layout .site-layout-background {
    background: #141414;
}
</style>
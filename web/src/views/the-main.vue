<template>
    <a-layout style="min-height: 100vh" id="components-layout-demo-side">
        <the-sider></the-sider>
        <a-layout>
            <a-layout-header style="background: #fff; padding: 0"/>
            <a-layout-content style="margin: 0 16px">
                <a-breadcrumb style="margin: 16px 0">
                    <a-breadcrumb-item>User</a-breadcrumb-item>
                    <a-breadcrumb-item>Bill</a-breadcrumb-item>
                </a-breadcrumb>
                <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
                    Bill is a cat.
                </div>
            </a-layout-content>
            <a-layout-footer style="text-align: center">
                Ant Design ©2018 Created by Ant UED
            </a-layout-footer>
        </a-layout>
    </a-layout>
</template>
<script>
import {defineComponent, ref} from 'vue';
import TheSider from "@/components/the-sider.vue";

export default defineComponent({
    components: {
        TheSider,
    },
    data() {
        return {
            collapsed: ref(false),
            selectedKeys: ref(['1']),
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
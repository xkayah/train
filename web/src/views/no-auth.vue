<template>
    <a-popover title="Title">
        <template #content>
            <p>401</p>
            <p>Not Found</p>
        </template>
        <a-button type="primary">Hover me</a-button>
    </a-popover>
</template>

<script>
import {defineComponent} from 'vue';
export default defineComponent({
    name: "no-auth",
    components: {
    },
    setup() {
        return {};
    },
});
</script>

<style scoped>

</style>

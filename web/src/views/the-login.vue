<template>
    <a-row class="login">
        <a-divider class="">
            <car-outlined two-tone-color="#eb2f96" width="50px" />
            <br/>
            <h1 style="text-align: center">
                Sign in to Mnus12306
            </h1>
        </a-divider>
        <a-col :span="8" :offset="8" class="login-main">
            <a-form
                    :model="loginForm"
                    name="basic"
                    autocomplete="off"
            >
                <h3>Username or mobile</h3>
                <a-form-item
                        name="mobile"
                        :rules="[{ required: true, message: '请输入手机号!' }]"
                >
                    <a-input v-model:value="loginForm.mobile" >
                        <template #suffix>
                            <a-tooltip title="Extra information">
                                <info-circle-outlined style="color: rgba(0, 0, 0, 0.45)"/>
                            </a-tooltip>
                        </template>
                    </a-input>
                </a-form-item>

                <h3>Code</h3>
                <a-form-item
                        name="code"
                        :rules="[{ required: true, message: '请输入验证码!' }]"
                >
                    <a-input v-model="loginForm.code">
                        <template #addonAfter>
                            <a click="sendCode">send code</a>
                        </template>
                    </a-input>

                </a-form-item>

                <a-form-item :wrapper-col="{ offset: 8, span: 8 }">
                    <a-button type="primary" html-type="submit">Sign in</a-button>
                </a-form-item>
            </a-form>
        </a-col>
    </a-row>
</template>

<script>
import {defineComponent, reactive} from 'vue';

export default defineComponent({
    name: "the-login",
    setup() {
        const loginForm = reactive({
            mobile: '',
            code: '',
        });
        const onFinish = values => {
            console.log('Success:', values);
        };
        const onFinishFailed = errorInfo => {
            console.log('Failed:', errorInfo);
        };
        const sendCode = values => {
            console.log('Success:', values);
        };
        return {
            loginForm,
            onFinish,
            onFinishFailed,
            sendCode,
        };
    },
});
</script>
<style>
.login-main {
    margin-top: 10px;
    padding: 30px 30px 20px;
    border: 2px solid rgb(216, 222, 228);
    border-radius: 10px;
    background-color: rgb(246, 248, 250);
}

.login-main h3 {
    text-align: left;
}

</style>
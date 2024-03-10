<template>
    <a-row class="login">
        <a-divider class="logo-body">
            <car-outlined two-tone-color="#eb2f96" width="50px"/>
            <br/>
            <h1 style="text-align: center">
                Sign in to Mnus12306
            </h1>
        </a-divider>
        <a-col :span="8" :offset="8" class="login-form-body">
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
                    <a-input v-model:value="loginForm.mobile">
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
                    <a-input v-model:value="loginForm.code">
                        <template #addonAfter>
                            <a @click="sendCode">send code</a>
                        </template>
                    </a-input>

                </a-form-item>

                <a-form-item :wrapper-col="{ offset: 8, span: 8 }">
                    <a-button type="primary" html-type="submit" @click="signIn()">Sign in</a-button>
                </a-form-item>
            </a-form>
        </a-col>
    </a-row>
</template>

<script>
import {defineComponent, reactive} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import {useRouter} from "vue-router";

export default defineComponent({
    name: "the-login",
    setup() {
        const router = useRouter();

        const loginForm = reactive({
            mobile: '',
            code: '',
        });
        const sendCode = () => {
            axios.post("http://localhost:10100/ucenter/user/send-code", {
                mobile: loginForm.mobile
            }).then(resp => {
                if (resp.code === 200) {
                    notification.success({description: 'Send code success!'});
                } else {
                    notification.error({description: resp.msg});
                }
            })
        };
        const signIn = () => {
            axios.post("http://localhost:10100/ucenter/user/sign-in", {
                mobile: loginForm.mobile,
                code: loginForm.code
            }).then(resp => {
                console.log(resp)
                if (resp.code === 200) {
                    notification.success({description: 'Login success!'});
                    router.push("/");
                } else {
                    notification.error({description: resp.msg});
                }
            })
        };
        return {
            router,
            loginForm,
            sendCode,
            signIn,
        };
    },
});
</script>
<style>
.login-form-body {
    margin-top: 10px;
    padding: 30px 30px 20px;
    border: 2px solid rgb(216, 222, 228);
    border-radius: 10px;
    background-color: rgb(246, 248, 250);
}

.login-form-body h3 {
    text-align: left;
}

.logo-body {

}

</style>
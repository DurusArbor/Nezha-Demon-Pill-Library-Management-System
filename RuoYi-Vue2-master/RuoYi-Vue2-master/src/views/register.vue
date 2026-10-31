<template>
  <div class="register">
    <video
      ref="registerVideo"
      class="register-video"
      autoplay
      muted
      loop
      playsinline
      webkit-playsinline
      preload="auto"
    >
      <source src="/videos/login-bg.mp4" type="video/mp4" />
    </video>
    <div class="register-mask"></div>
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
      <h3 class="title">{{title}}</h3>
      <el-form-item prop="username">
        <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password" :rules="registerPwdValidator">
        <el-input
          v-model="registerForm.password"
          type="password"
          auto-complete="new-password"
          placeholder="密码"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
          v-model="registerForm.confirmPassword"
          type="password"
          auto-complete="new-password"
          placeholder="确认密码"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="realName">
        <el-input v-model="registerForm.realName" type="text" auto-complete="off" placeholder="真实姓名（用于读者档案）">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="phone">
        <el-input v-model="registerForm.phone" type="text" auto-complete="off" placeholder="手机号">
          <svg-icon slot="prefix" icon-class="phone" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="email">
        <el-input
          v-model="registerForm.email"
          type="email"
          auto-complete="off"
          placeholder="邮箱"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="registerForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="register-code">
          <img :src="codeUrl" @click="getCode" class="register-code-img"/>
        </div>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleRegister"
        >
          <span v-if="!loading">注 册</span>
          <span v-else>注 册 中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-register-footer">
      <span>{{ footerContent }}</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg, register } from "@/api/login"
import passwordRule from "@/utils/passwordRule"
import defaultSettings from '@/settings'

export default {
  mixins: [passwordRule],
  data() {
    return {
      title: process.env.VUE_APP_TITLE,
      footerContent: defaultSettings.footerContent,
      codeUrl: "",
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        realName: "",
        phone: "",
        email: "",
        code: "",
        uuid: ""
      },
      loading: false,
      captchaEnabled: true
    }
  },
  computed: {
    registerRules() {
      return {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
          { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        realName: [
          { required: true, trigger: "blur", message: "请输入真实姓名" }
        ],
        phone: [
          { required: true, trigger: "blur", message: "请输入手机号" },
          { pattern: /^1[3-9]\d{9}$/, message: "手机号格式不正确", trigger: "blur" }
        ],
        email: [
          { required: true, trigger: "blur", message: "请输入邮箱" },
          { type: "email", message: "邮箱格式不正确", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "请再次输入您的密码", trigger: "blur" },
          {
            validator: (rule, value, callback) => {
              if (this.registerForm.password !== value) {
                callback(new Error("两次输入的密码不一致"))
              } else {
                callback()
              }
            }, trigger: "blur"
          }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      }
    }
  },
  created() {
    this.getCode()
  },
  mounted() {
    // 清除浏览器自动填充/记忆的账号（Chrome 常忽略 autocomplete=off，需挂载后强制清空两次）
    this.clearAutofill()
    // 尝试带声音自动播放；浏览器拦截时先静音播放，用户首次点击/按键后自动开声音
    const video = this.$refs.registerVideo
    if (!video) return
    video.muted = false
    const tryPlay = video.play()
    if (tryPlay && tryPlay.catch) {
      tryPlay.catch(() => {
        video.muted = true
        const p2 = video.play()
        if (p2 && p2.catch) { p2.catch(() => {}) }
        const unmute = () => {
          video.muted = false
          const p3 = video.play()
          if (p3 && p3.catch) { p3.catch(() => {}) }
          document.removeEventListener('click', unmute)
          document.removeEventListener('keydown', unmute)
        }
        document.addEventListener('click', unmute)
        document.addEventListener('keydown', unmute)
      })
    }
  },
  methods: {
    clearAutofill() {
      const reset = () => {
        const uuid = this.registerForm.uuid
        this.registerForm = {
          username: "", password: "", confirmPassword: "",
          realName: "", phone: "", email: "", code: "", uuid: uuid
        }
      }
      reset()
      setTimeout(reset, 300)
    },
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.registerForm.uuid = res.uuid
        }
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          register(this.registerForm).then(() => {
            const username = this.registerForm.username
            this.$alert("<font color='red'>恭喜你，您的账号 " + username + " 注册成功！</font>", '系统提示', {
              dangerouslyUseHTMLString: true,
              type: 'success'
            }).then(() => {
              this.$router.push("/login")
            }).catch(() => {})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.register {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  overflow: hidden;
  background: #2a0808 url("../assets/images/login-banner.jpg") center center / cover no-repeat;
}
.register-video {
  position: absolute;
  top: 0; left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 0;
  pointer-events: none;
}
.register-mask {
  position: absolute;
  top: 0; left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  background: linear-gradient(90deg, rgba(30,4,3,.62) 0%, rgba(80,12,6,.35) 48%, rgba(160,40,10,.42) 100%);
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #ff3b2f;
  font-weight: 700;
  letter-spacing: 1px;
  text-shadow: 0 1px 6px rgba(255,240,220,.9), 0 0 2px rgba(255,255,255,.9);
}

.register-form {
  position: relative;
  border-radius: 8px;
  background: transparent;
  width: 400px;
  padding: 25px 25px 5px 25px;
  z-index: 2;
  box-shadow: none;
  .el-button--primary {
    background: linear-gradient(135deg, #e0311f 0%, #ff5a1f 100%);
    border-color: #d3302b;
  }
  ::v-deep .el-input {
    height: 38px;
    .el-input__inner {
      height: 38px;
      background: transparent;
      border-color: rgba(255,255,255,.55);
      color: #fff;
      caret-color: #fff;
    }
    .el-input__inner::placeholder { color: rgba(255,255,255,.75); }
    .el-input__inner:focus { border-color: #ffb74d; }
    .input-icon { color: #fff; }
    .el-input__inner:-webkit-autofill {
      -webkit-text-fill-color: #fff;
      -webkit-box-shadow: 0 0 0 1000px transparent inset;
      transition: background-color 9999s ease-in-out 0s;
    }
  }
  ::v-deep .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
  ::v-deep .el-checkbox__label { color: #ffe4b3; text-shadow: 0 1px 2px rgba(0,0,0,.6); }
  .link-type { color: #ffe4b3; text-shadow: 0 1px 2px rgba(0,0,0,.6); }
}
.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.register-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-register-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
  z-index: 2;
  text-shadow: 0 1px 3px rgba(0,0,0,.5);
}
.register-code-img {
  height: 38px;
}
</style>

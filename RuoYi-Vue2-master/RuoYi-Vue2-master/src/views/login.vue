<template>
  <div class="login">
    <!-- 视频背景（mp4 存放在 public/videos，随项目永久保存） -->
    <video
      ref="loginVideo"
      class="login-video"
      autoplay
      muted
      loop
      playsinline
      webkit-playsinline
      preload="auto"
    >
      <source src="/videos/login-bg.mp4" type="video/mp4" />
    </video>
    <div class="login-mask"></div>
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">{{title}}</h3>
      <p class="subtitle">图书馆藏 · 借阅管理 · 读者服务</p>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          auto-complete="off"
          placeholder="账号"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>{{ footerContent }}</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from '@/utils/jsencrypt'
import defaultSettings from '@/settings'

export default {
  name: "Login",
  data() {
    return {
      title: process.env.VUE_APP_TITLE,
      footerContent: defaultSettings.footerContent,
      codeUrl: "",
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关：图书馆系统开放读者自助注册（后端 sys.account.registerUser 同时校验）
      register: true,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCode()
    this.getCookie()
  },
  mounted() {
    // 尝试带声音自动播放；浏览器拦截时先静音播放，用户首次点击/按键后自动开声音
    const video = this.$refs.loginVideo
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
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    getCookie() {
      const username = Cookies.get("username")
      const password = Cookies.get("password")
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 })
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove("username")
            Cookies.remove("password")
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            const mottos = [
              "今日命由我，先定个小目标！",
              "我命由我不由天，今天也要好好读书！",
              "若前方无路，我便踏出一条路。",
              "是魔是仙，我自己说了算！",
              "我命由我不由天，先从借一本书开始。",
              "永远不要放弃，哪怕前路坎坷。",
              "生而孤独，从不认输，今天也要加油！",
              "你是谁，只有你自己说了才算。"
            ]
            const motto = mottos[Math.floor(Math.random() * mottos.length)]
            this.$notify({ title: "哪吒魔丸 · 今日修行", message: motto, type: "warning", duration: 5000 })
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{})
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
.login {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  overflow: hidden;
  background: #2a0808 url("../assets/images/login-banner.jpg") center center / cover no-repeat;
}
.login-video {
  position: absolute;
  top: 0; left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 0;
  pointer-events: none;
}
.login-mask {
  position: absolute;
  top: 0; left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  background: linear-gradient(90deg, rgba(30,4,3,.62) 0%, rgba(80,12,6,.35) 48%, rgba(160,40,10,.42) 100%);
}
.title {
  margin: 0px auto 10px auto;
  text-align: center;
  color: #ff3b2f;
  font-weight: 700;
  letter-spacing: 1px;
  text-shadow: 0 1px 6px rgba(255,240,220,.9), 0 0 2px rgba(255,255,255,.9);
}
.subtitle {
  text-align: center;
  color: #ffe9d6;
  font-size: 12px;
  margin: 0 0 25px 0;
  letter-spacing: 1px;
  text-shadow: 0 1px 3px rgba(0,0,0,.7);
}

.login-form {
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
    letter-spacing: 4px;
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
  ::v-deep .el-checkbox__inner {
    background-color: transparent;
    border-color: rgba(255,255,255,.75);
  }
  ::v-deep .el-checkbox__input.is-focus .el-checkbox__inner,
  ::v-deep .el-checkbox__inner:hover { border-color: #ffb74d; }
  ::v-deep .el-checkbox__input.is-checked .el-checkbox__inner {
    background-color: transparent;
    border-color: #ffe4b3;
  }
  ::v-deep .el-checkbox__input.is-checked .el-checkbox__inner::after {
    border-color: #ffe4b3;
  }
  ::v-deep .el-checkbox__label { color: #ffe4b3; text-shadow: 0 1px 2px rgba(0,0,0,.6); }
  .link-type { color: #ffe4b3; text-shadow: 0 1px 2px rgba(0,0,0,.6); }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
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
.login-code-img {
  height: 38px;
}
</style>

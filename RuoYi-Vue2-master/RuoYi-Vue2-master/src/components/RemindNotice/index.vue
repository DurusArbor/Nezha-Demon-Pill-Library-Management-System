<template>
  <div></div>
</template>

<script>
import { myReminds, readRemind } from '@/api/system/remind'

/**
 * 读者端全局催缴轮询：
 * 定时拉取未读催缴，逐条弹窗提醒，读者点击「我知道了」后标记已读。
 */
export default {
  name: 'RemindNotice',
  data() {
    return {
      timer: null,
      showing: false,
      // 本次会话已处理过的催缴ID，避免重复弹窗
      handled: []
    }
  },
  computed: {
    isReader() {
      const roles = this.$store.getters.roles || []
      return roles.indexOf('reader') !== -1
    }
  },
  mounted() {
    if (!this.isReader) return
    // 进入页面先查一次，之后每 30 秒轮询
    this.poll()
    this.timer = setInterval(this.poll, 30000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
      this.timer = null
    }
  },
  methods: {
    poll() {
      if (this.showing) return
      myReminds('0').then(res => {
        const list = res.data || []
        const pending = list.filter(item => this.handled.indexOf(item.remindId) === -1)
        if (pending.length > 0) {
          this.showQueue(pending)
        }
      }).catch(() => {})
    },
    showQueue(list) {
      this.showing = true
      const queue = list.slice()
      const next = () => {
        if (queue.length === 0) {
          this.showing = false
          return
        }
        const item = queue.shift()
        this.handled.push(item.remindId)
        this.$alert(item.remindContent || '您有一条催缴通知，请尽快处理。', '逾期催缴提醒', {
          confirmButtonText: '我知道了',
          type: 'warning',
          callback: () => {
            readRemind(item.remindId).then(() => {
              next()
            }).catch(() => next())
          }
        })
      }
      next()
    }
  }
}
</script>

<template>
  <div class="app-container">
    <el-card shadow="never" class="reader-hero" style="margin-bottom: 15px;">
      <div style="display:flex;align-items:center;justify-content:space-between;flex-wrap:wrap;gap:10px;">
        <div>
          <div style="font-size:18px;font-weight:700;color:#fff;">哪吒魔丸 · 读者中心</div>
          <div style="color:rgba(255,255,255,.9);font-size:13px;margin-top:4px;">我命由我不由天 · 馆藏查询、自助借还、续借与预约。</div>
        </div>
        <div style="color:rgba(255,255,255,.92);font-size:13px;">{{ today }}</div>
      </div>
    </el-card>

    <el-card shadow="hover" class="cult-card" style="margin-bottom:15px;">
      <div class="cult-inner">
        <div style="display:flex;flex-direction:column;align-items:center;gap:6px;">
          <div class="cult-avatar" :class="cult.nature">
            <i :class="natureIcon"></i>
          </div>
          <div class="cult-titles">
            <el-tag v-for="t in cult.titles" :key="t" size="mini" class="title-tag">{{ t }}</el-tag>
            <span v-if="!cult.titles || cult.titles.length === 0" class="no-title">暂无称号</span>
          </div>
        </div>
        <div class="cult-main">
          <div class="cult-top">
            <span class="cult-level">{{ cult.levelName }}</span>
            <el-tag :type="natureTagType" effect="dark" size="small">{{ natureLabel }}</el-tag>
            <span class="cult-merit"><i class="el-icon-sunny"></i> 功德 {{ cult.meritPoints }}</span>
            <span class="cult-demon"><i class="el-icon-lightning"></i> 魔性 {{ cult.demonValue }}</span>
          </div>
          <el-progress :percentage="Number(cult.percent)||0" :stroke-width="12" :color="progressColor" :format="progressFormat" class="cult-progress"></el-progress>
          <div class="cult-next">{{ cultTip }}</div>
        </div>
        <div class="cult-actions">
          <el-button :type="cult.signedToday ? 'info' : 'warning'" :disabled="cult.signedToday" icon="el-icon-edit-outline" @click="doSign" :loading="signLoading">
            {{ cult.signedToday ? '今日已签到' : '每日签到' }}
          </el-button>
          <el-tooltip :content="purifyTip" placement="top">
            <span style="display:inline-flex;width:100%;">
              <el-button type="danger" plain icon="el-icon-magic-stick" :disabled="!canPurify" @click="doPurify" style="width:100%;">
                一念成仙
              </el-button>
            </span>
          </el-tooltip>
          <el-tooltip content="20 功德/次 · 随机 +10~200 功德 · 每日最多 3 次 · 魔丸不可抽" placement="top">
            <span style="display:inline-flex;width:100%;">
              <el-button type="warning" plain icon="el-icon-box" @click="doLottery" style="width:100%;">
                仙丹盲盒（剩 {{ cult.boxLeftToday || 0 }}）
              </el-button>
            </span>
          </el-tooltip>
          <el-tooltip content="50 功德兑换 1 张 · 缴罚金时自动抵消一次" placement="top">
            <span style="display:inline-flex;width:100%;">
              <el-button type="success" plain icon="el-icon-tickets" @click="doBuyToken" style="width:100%;">
                兑换免罚令（持 {{ cult.forgiveTokens || 0 }}）
              </el-button>
            </span>
          </el-tooltip>
          <el-button type="danger" plain icon="el-icon-trophy" @click="goRank" style="width:100%;">封神榜</el-button>
        </div>
      </div>
    </el-card>

    <el-row :gutter="16">
      <el-col :xs="12" :sm="12" :md="6" v-for="(card, i) in cards" :key="i">
        <el-card shadow="hover" class="stat-card" @click.native="go(card.path)">
          <div class="stat-icon" :style="{ background: card.color }">
            <i :class="card.icon"></i>
          </div>
          <div class="stat-body">
            <div class="stat-num">{{ card.value }}<span class="stat-unit">{{ card.unit }}</span></div>
            <div class="stat-label">{{ card.label }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:15px;">
      <el-col :xs="24" :md="12">
        <el-card shadow="never">
          <div slot="header"><span><i class="el-icon-reading"></i> 快捷入口</span></div>
          <div class="quick-links">
            <el-button type="primary" icon="el-icon-search" @click="go('/readerSelf/books')">在线书城</el-button>
            <el-button type="success" icon="el-icon-list" @click="go('/readerSelf/myBorrows')">我的借阅</el-button>
            <el-button type="warning" icon="el-icon-time" @click="go('/readerSelf/myReserves')">我的预约</el-button>
            <el-button type="danger" icon="el-icon-money" @click="go('/readerSelf/myFines')">我的罚金</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="never">
          <div slot="header"><span><i class="el-icon-bell"></i> 借阅提示</span></div>
          <ul class="tips">
            <li>每册借期 30 天，最多同时借阅 5 本，可续借 1 次（延长 15 天）。</li>
            <li>馆藏为 0 时可在线预约，图书归还后系统自动到书提醒，保留 3 天。</li>
            <li>逾期图书按 0.5 元/天计罚金，归还后可在「我的罚金」中缴纳。</li>
          </ul>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-top:15px;">
      <div slot="header" class="notice-header">
        <span><i class="el-icon-document"></i> 图书馆公告</span>
        <el-button type="text" @click="openAll">查看更多<i class="el-icon-arrow-right el-icon--right"></i></el-button>
      </div>
      <div v-loading="noticeLoading">
        <div class="notice-item" v-for="n in noticeList" :key="n.noticeId" @click="viewNotice(n)">
          <el-tag size="mini" :type="n.noticeType === '1' ? 'warning' : 'danger'" class="notice-tag">
            {{ n.noticeType === '1' ? '通知' : '公告' }}
          </el-tag>
          <span class="notice-title">{{ n.noticeTitle }}</span>
          <span class="notice-time">{{ parseTime(n.createTime, '{y}-{m}-{d}') }}</span>
        </div>
        <div v-if="!noticeLoading && noticeList.length === 0" class="notice-empty">暂无公告</div>
      </div>
    </el-card>

    <el-dialog title="全部图书馆公告" :visible.sync="allOpen" width="720px" append-to-body>
      <div v-loading="allLoading">
        <div class="notice-item" v-for="n in allNoticeList" :key="n.noticeId" @click="viewNotice(n)">
          <el-tag size="mini" :type="n.noticeType === '1' ? 'warning' : 'danger'" class="notice-tag">
            {{ n.noticeType === '1' ? '通知' : '公告' }}
          </el-tag>
          <span class="notice-title">{{ n.noticeTitle }}</span>
          <span class="notice-time">{{ parseTime(n.createTime, '{y}-{m}-{d}') }}</span>
        </div>
        <div v-if="!allLoading && allNoticeList.length === 0" class="notice-empty">暂无公告</div>
      </div>
    </el-dialog>

    <el-dialog :title="currentNotice.noticeTitle" :visible.sync="detailOpen" width="600px" append-to-body>
      <div class="notice-detail-meta">
        <el-tag size="mini" :type="currentNotice.noticeType === '1' ? 'warning' : 'danger'">
          {{ currentNotice.noticeType === '1' ? '通知' : '公告' }}
        </el-tag>
        <span style="margin-left:10px;color:#909399;font-size:12px;">{{ parseTime(currentNotice.createTime) }}</span>
      </div>
      <div class="notice-detail-content" v-html="currentNotice.noticeContent" style="margin-top:12px;line-height:1.8;color:#303133;"></div>
    </el-dialog>

    <!-- 罚金催缴提醒：登录进入读者中心，若有未缴罚金则中间弹窗 -->
    <el-dialog title="罚金催缴提醒" :visible.sync="fineDialog" width="600px" append-to-body :close-on-click-modal="false">
      <div>
        <el-alert
          title="您有未缴罚金（天劫未解）！图书逾期按 0.5 香火钱/天计罚，请尽快归还图书并缴纳罚金，以消天劫、净化魔性，否则将影响借阅与修行。"
          type="error"
          :closable="false"
          show-icon
        />
        <el-table :data="unpaidFines" size="small" border stripe style="margin-top:12px;">
          <el-table-column label="图书" prop="bookName" min-width="170" show-overflow-tooltip />
          <el-table-column label="触犯天条（天）" prop="overdueDays" width="110" align="center" />
          <el-table-column label="天劫（香火钱）" width="120" align="center">
            <template slot-scope="scope">¥{{ scope.row.fineAmount }}</template>
          </el-table-column>
        </el-table>
        <div style="text-align:right;margin-top:10px;font-size:14px;color:#b5393a;font-weight:600;">
          待缴合计：¥{{ unpaidTotal.toFixed(2) }}
        </div>
      </div>
      <span slot="footer">
        <el-button @click="fineDialog = false">知道了</el-button>
        <el-button type="danger" icon="el-icon-money" @click="goFine">去缴纳</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { readerBooks, myBorrows, myReserves, myFines, readerNotices, getCultivation, readerSign, readerFortune, readerPurify, readerLottery, readerBuyToken } from "@/api/system/reader"

export default {
  name: "ReaderSelfIndex",
  data() {
    return {
      today: "",
      signLoading: false,
      cult: {
        levelName: "凡人", nature: "half", meritPoints: 0, demonValue: 0,
        percent: 0, curProgress: 0, spanProgress: 1, nextLevelName: "道童",
        signedToday: false, overdueCount: 0, titles: [],
        forgiveTokens: 0, boxLeftToday: 3
      },
      booksTotal: 0,
      noticeList: [],
      noticeLoading: false,
      allOpen: false,
      allLoading: false,
      allNoticeList: [],
      detailOpen: false,
      currentNotice: {},
      fineDialog: false,
      unpaidFines: [],
      unpaidTotal: 0,
      cards: [
        { label: "馆藏图书", value: 0, unit: "册", icon: "el-icon-reading", color: "linear-gradient(135deg,#d4321f,#ff7029)", path: "/readerSelf/books" },
        { label: "借阅中（含逾期）", value: 0, unit: "本", icon: "el-icon-edit-outline", color: "linear-gradient(135deg,#e8771f,#ffb020)", path: "/readerSelf/myBorrows" },
        { label: "预约中", value: 0, unit: "条", icon: "el-icon-time", color: "linear-gradient(135deg,#9c2118,#d4452f)", path: "/readerSelf/myReserves" },
        { label: "未缴罚金", value: 0, unit: "元", icon: "el-icon-warning", color: "linear-gradient(135deg,#5f1414,#a0261c)", path: "/readerSelf/myFines" }
      ]
    }
  },
  created() {
    const d = new Date()
    const week = ['日', '一', '二', '三', '四', '五', '六'][d.getDay()]
    this.today = `${d.getFullYear()} 年 ${d.getMonth() + 1} 月 ${d.getDate()} 日 星期${week}`
    this.load()
    this.loadNotices()
    this.loadCultivation()
    this.tryFortune()
  },
  computed: {
    canPurify() {
      return Number(this.cult.meritPoints) >= 10 && Number(this.cult.demonValue) > 0
    },
    canLottery() {
      return this.cult.nature !== "demon" && Number(this.cult.boxLeftToday) > 0 && Number(this.cult.meritPoints) >= 20
    },
    lotteryTip() {
      if (this.cult.nature === "demon") {
        return "魔丸不可开仙丹盲盒，先还清逾期、缴清罚金或净化魔性"
      }
      if (Number(this.cult.boxLeftToday) <= 0) {
        return "今日已抽满 3 次，明日再来"
      }
      if (Number(this.cult.meritPoints) < 20) {
        return "功德不足 20，无法开仙丹盲盒"
      }
      return "20 功德开一次，随机 +10~200 功德（今日剩 " + this.cult.boxLeftToday + " 次）"
    },
    purifyTip() {
      if (Number(this.cult.demonValue) <= 0) {
        return "当前魔性为 0，无需净化"
      }
      if (Number(this.cult.meritPoints) < 10) {
        return "功德不足 10，无法净化"
      }
      return "每 10 功德净化 1 点魔性（可洗 " + Math.min(this.cult.demonValue, Math.floor(this.cult.meritPoints / 10)) + " 点）"
    },
    natureIcon() {
      return { demon: "el-icon-discover", spirit: "el-icon-magic-stick", half: "el-icon-help" }[this.cult.nature]
    },
    natureLabel() {
      return { demon: "魔丸", spirit: "灵珠", half: "半魔半仙" }[this.cult.nature]
    },
    natureTagType() {
      return { demon: "danger", spirit: "success", half: "warning" }[this.cult.nature]
    },
    progressColor() {
      return { demon: "#c0392b", spirit: "#27ae60", half: "#e6a23c" }[this.cult.nature]
    },
    progressFormat(p) {
      return p + "%"
    },
    cultTip() {
      if (this.cult.percent >= 100) {
        return "修行已圆满，功德无量"
      }
      const need = Number(this.cult.spanProgress) - Number(this.cult.curProgress)
      if (this.cult.nature === "demon") {
        return "魔性缠身，速归还逾期图书、积累功德以净化（距「" + this.cult.nextLevelName + "」还差 " + need + " 功德）"
      }
      return "距晋升「" + this.cult.nextLevelName + "」还差 " + need + " 功德"
    }
  },
  methods: {
    go(path) { this.$router.push(path) },
    goRank() { this.$router.push("/readerSelf/rank") },
    loadCultivation() {
      getCultivation().then(res => { this.cult = res.data || this.cult })
    },
    doSign() {
      this.signLoading = true
      readerSign().then(res => {
        this.$message.success(res.msg || "签到成功")
        this.signLoading = false
        this.loadCultivation()
      }).catch(() => { this.signLoading = false })
    },
    // 每日奇遇/天劫：登录进入读者中心时触发，每天最多一次
    tryFortune() {
      readerFortune().then(res => {
        const d = res.data || {}
        if (d.triggered) {
          this.$message({ message: d.message || "", type: d.good ? "success" : "error", duration: 4000 })
          this.loadCultivation()
        }
      }).catch(() => {})
    },
    // 开仙丹盲盒：20 功德/次，随机 +10~200 功德，每日最多 3 次（魔丸后端拦截）
    doLottery() {
      if (this.cult.nature === "demon") {
        this.$modal.msgWarning("魔丸不可开仙丹盲盒，先还清逾期、缴清罚金或净化魔性")
        return
      }
      if (Number(this.cult.boxLeftToday) <= 0) {
        this.$modal.msgWarning("今日已抽满 3 次，明日再来")
        return
      }
      if (Number(this.cult.meritPoints) < 20) {
        this.$modal.msgWarning("功德不足 20，无法开仙丹盲盒（今日剩 " + this.cult.boxLeftToday + " 次，当前功德 " + this.cult.meritPoints + "，每抽一次消耗 20 功德）")
        return
      }
      readerLottery().then(res => {
        const d = res.data || {}
        this.$message.success("仙丹到手，功德 +" + d.gained + "！")
        this.loadCultivation()
      }).catch(() => {})
    },
    // 兑换免罚令：50 功德/张，缴罚金时自动抵消一次
    doBuyToken() {
      if (Number(this.cult.meritPoints) < 50) {
        this.$modal.msgWarning("功德不足 50，无法兑换免罚令（当前功德 " + this.cult.meritPoints + "，每张需 50 功德）")
        return
      }
      readerBuyToken().then(res => {
        const d = res.data || {}
        this.$message.success("免罚令到手（已持有 " + d.tokens + " 张），缴罚金自动抵消！")
        this.loadCultivation()
      }).catch(() => {})
    },
    // 魔性净化仪式：每 10 功德净化 1 点魔性（最多洗到魔性清零）
    doPurify() {
      if (Number(this.cult.demonValue) <= 0) {
        this.$modal.msgWarning("当前魔性为 0，无需净化")
        return
      }
      const clean = Math.min(this.cult.demonValue, Math.floor(this.cult.meritPoints / 10))
      if (clean <= 0) {
        this.$modal.msgWarning("功德不足 10，无法净化")
        return
      }
      this.$modal.confirm("每 10 功德净化 1 点魔性，本次将消耗 " + (clean * 10) + " 功德净化 " + clean + " 点魔性，是否继续？").then(() => {
        readerPurify().then(res => {
          this.$message.success(res.msg || "魔性消退，一念成仙！")
          this.loadCultivation()
        }).catch(() => {})
      }).catch(() => {})
    },
    loadNotices() {
      this.noticeLoading = true
      readerNotices({ pageNum: 1, pageSize: 5 }).then(res => {
        this.noticeList = res.rows || []
        this.noticeLoading = false
      }).catch(() => { this.noticeLoading = false })
    },
    openAll() {
      this.allOpen = true
      this.allLoading = true
      readerNotices({ pageNum: 1, pageSize: 100 }).then(res => {
        this.allNoticeList = res.rows || []
        this.allLoading = false
      }).catch(() => { this.allLoading = false })
    },
    viewNotice(n) {
      this.currentNotice = n
      this.detailOpen = true
    },
    load() {
      readerBooks({ pageNum: 1, pageSize: 1 }).then(res => { this.cards[0].value = res.total })
      myBorrows().then(res => {
        const list = res.data || []
        const active = list.filter(r => r.borrowStatus === '0' || r.borrowStatus === '2').length
        this.cards[1].value = active
      })
      myReserves().then(res => {
        const list = res.data || []
        this.cards[2].value = list.filter(r => r.reserveStatus === '0' || r.reserveStatus === '1').length
        // 预约书已到馆：登录进入页面后弹窗提醒
        const arrived = list.filter(r => r.reserveStatus === '1')
        if (arrived.length > 0) {
          const names = arrived.map(r => r.bookName || '未知图书').join('、')
          this.$notify({
            title: '预约到书提醒',
            message: '您预约的《' + names + '》已到馆，请尽快到馆借出，逾期预约将自动失效。',
            type: 'warning',
            duration: 0,
            position: 'top-right'
          })
        }
      })
      myFines().then(res => {
        const list = res.data || []
        const unpaid = list.filter(r => r.fineStatus === '0')
        const sum = unpaid.reduce((s, r) => s + Number(r.fineAmount || 0), 0)
        this.cards[3].value = sum.toFixed(1)
        // 罚金催缴：登录进入读者中心，若有未缴罚金则中间弹窗提醒
        if (unpaid.length > 0) {
          this.unpaidFines = unpaid
          this.unpaidTotal = sum
          this.fineDialog = true
        }
      })
    },
    goFine() {
      this.fineDialog = false
      this.$router.push("/readerSelf/myFines")
    }
  }
}
</script>

<style scoped>
.reader-hero {
  background: linear-gradient(100deg, #8c1610 0%, #d4321f 60%, #ff6a1f 100%) !important;
  border: none;
}
.reader-hero >>> .el-card__body { padding: 20px 24px; }
.cult-card { border: 1px solid #f0d9c8; }
.cult-card >>> .el-card__body { padding: 18px 20px; }
.cult-inner { display: flex; align-items: center; gap: 18px; flex-wrap: wrap; }
.cult-avatar {
  width: 60px; height: 60px; border-radius: 50%; flex: none;
  display: flex; align-items: center; justify-content: center;
  font-size: 30px; color: #fff;
}
.cult-avatar.demon { background: radial-gradient(circle at 30% 30%, #ff6a3d, #b31e12); box-shadow: 0 0 16px rgba(200,40,20,.5); }
.cult-avatar.spirit { background: radial-gradient(circle at 30% 30%, #7fe0c0, #1e9e78); box-shadow: 0 0 16px rgba(30,160,120,.4); }
.cult-avatar.half { background: radial-gradient(circle at 30% 30%, #ffcf6b, #e08a1e); box-shadow: 0 0 16px rgba(224,138,30,.4); }
.cult-titles { display: flex; flex-wrap: wrap; justify-content: center; gap: 4px; max-width: 90px; }
.title-tag { border-color: #d4321f; color: #b31e12; background: #fff7f5; }
.no-title { font-size: 11px; color: #c0c4cc; }
.cult-main { flex: 1; min-width: 260px; }
.cult-top { display: flex; align-items: center; gap: 10px; flex-wrap: wrap; margin-bottom: 10px; }
.cult-level { font-size: 18px; font-weight: 700; color: #b31e12; }
.cult-merit { color: #e6a23c; font-size: 13px; font-weight: 600; }
.cult-demon { color: #c0392b; font-size: 13px; font-weight: 600; }
.cult-progress { margin-bottom: 6px; }
.cult-next { color: #909399; font-size: 12px; }
.cult-actions { flex: none; display: flex; flex-direction: column; gap: 8px; }
.cult-actions .el-button { margin-left: 0; }
.cult-actions .el-button + .el-button { margin-left: 0; }
.stat-card { cursor: pointer; margin-bottom: 16px; }
.stat-card >>> .el-card__body { display: flex; align-items: center; padding: 16px; }
.stat-icon {
  width: 48px; height: 48px; border-radius: 8px; flex: none;
  display: flex; align-items: center; justify-content: center;
  color: #fff; font-size: 22px; margin-right: 12px;
  box-shadow: 0 4px 10px rgba(180,40,20,.25);
}
.stat-num { font-size: 22px; font-weight: 700; color: #1f2d3d; }
.stat-unit { font-size: 12px; font-weight: 400; color: #909399; margin-left: 2px; }
.stat-label { color: #909399; font-size: 12px; margin-top: 4px; }
.quick-links .el-button { margin: 0 12px 12px 0; }
.tips { margin: 0; padding-left: 18px; color: #606266; font-size: 13px; line-height: 2; }
.notice-header { display: flex; align-items: center; justify-content: space-between; }
.notice-item {
  display: flex; align-items: center; padding: 9px 4px;
  border-bottom: 1px dashed #ebeef5; cursor: pointer;
}
.notice-item:hover .notice-title { color: #d4321f; }
.notice-item:last-child { border-bottom: none; }
.notice-tag { flex: none; }
.notice-title {
  flex: 1; margin: 0 12px; color: #303133; font-size: 14px;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}
.notice-time { flex: none; color: #909399; font-size: 12px; }
.notice-empty { text-align: center; color: #909399; padding: 20px 0; font-size: 13px; }
</style>

<template>
  <div class="app-container home">
    <!-- 哪吒主题欢迎横幅 -->
    <div class="hero-card">
      <div class="hero-inner">
        <div class="hero-text">
          <h2>哪吒魔丸图书管理系统</h2>
          <p class="hero-sub">我命由我不由天 · 馆藏流通 · 借阅管理 · 读者服务 一体化平台</p>
        </div>
        <div class="hero-date">
          <i class="el-icon-date"></i>
          <span>{{ today }}</span>
        </div>
      </div>
    </div>

    <!-- 核心指标卡片（真实统计） -->
    <el-row :gutter="20" class="stat-row">
      <el-col :xs="12" :sm="12" :md="6" v-for="item in statCards" :key="item.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-inner">
            <div class="stat-icon" :style="{ background: item.color }">
              <i :class="item.icon"></i>
            </div>
            <div class="stat-meta">
              <div class="stat-value">{{ item.value }}<span class="stat-unit">{{ item.unit }}</span></div>
              <div class="stat-label">{{ item.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 公告（来自 sys_notice 真实数据） -->
      <el-col :xs="24" :md="14">
        <el-card shadow="never">
          <div slot="header" class="clearfix notice-header">
            <span><i class="el-icon-bell"></i> 图书馆公告</span>
            <el-button type="text" @click="openAllNotices">查看更多<i class="el-icon-arrow-right el-icon--right"></i></el-button>
          </div>
          <ul class="notice-list" v-loading="noticeLoading">
            <li v-if="notices.length === 0 && !noticeLoading" style="text-align:center;color:#909399;padding:20px 0">暂无公告</li>
            <li v-for="(n, i) in notices" :key="i" class="notice-li" @click="viewNotice(n)">
              <el-tag size="mini" :type="n.noticeType === '1' ? 'warning' : 'primary'">{{ n.noticeType === '1' ? '通知' : '公告' }}</el-tag>
              <span class="notice-title">{{ n.noticeTitle }}</span>
              <span class="notice-date">{{ (n.createTime || '').slice(0, 10) }}</span>
            </li>
          </ul>
        </el-card>
      </el-col>

      <!-- 快捷操作 -->
      <el-col :xs="24" :md="10">
        <el-card shadow="never">
          <div slot="header" class="clearfix">
            <span><i class="el-icon-s-order"></i> 快捷操作</span>
          </div>
          <div class="quick-row">
            <el-button type="primary" plain icon="el-icon-plus" @click="go('/book/list')">图书列表</el-button>
            <el-button type="success" plain icon="el-icon-user" @click="go('/reader/list')">读者管理</el-button>
            <el-button type="warning" plain icon="el-icon-edit" @click="go('/borrow/list')">借阅记录</el-button>
            <el-button type="danger" plain icon="el-icon-warning" @click="go('/borrow/overdue')">逾期处理</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据看板 ①：借阅趋势 + 热门图书 -->
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :xs="24" :md="14">
        <el-card shadow="never">
          <div slot="header"><i class="el-icon-data-line"></i> 借阅趋势（近 30 天）</div>
          <div ref="trendChart" style="height:280px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="10">
        <el-card shadow="never">
          <div slot="header"><i class="el-icon-trophy"></i> 热门图书 Top 5</div>
          <div ref="hotChart" style="height:280px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据看板 ②：逾期率 + 魔丸灵珠分布 -->
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :xs="24" :md="12">
        <el-card shadow="never">
          <div slot="header"><i class="el-icon-warning"></i> 逾期率</div>
          <div ref="overdueChart" style="height:280px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="never">
          <div slot="header"><i class="el-icon-discover"></i> 魔丸灵珠分布</div>
          <div ref="natureChart" style="height:280px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查看更多：全部公告 -->
    <el-dialog title="全部图书馆公告" :visible.sync="allNoticeOpen" width="760px" append-to-body>
      <ul class="notice-list" v-loading="allNoticeLoading">
        <li v-if="allNotices.length === 0 && !allNoticeLoading" style="text-align:center;color:#909399;padding:20px 0">暂无公告</li>
        <li v-for="(n, i) in allNotices" :key="i" class="notice-li" @click="viewNotice(n)">
          <el-tag size="mini" :type="n.noticeType === '1' ? 'warning' : 'primary'">{{ n.noticeType === '1' ? '通知' : '公告' }}</el-tag>
          <span class="notice-title">{{ n.noticeTitle }}</span>
          <span class="notice-date">{{ (n.createTime || '').slice(0, 10) }}</span>
        </li>
      </ul>
    </el-dialog>

    <!-- 公告详情 -->
    <el-dialog :title="currentNotice.noticeTitle" :visible.sync="noticeDetailOpen" width="640px" append-to-body>
      <div class="notice-detail-meta">
        <el-tag size="mini" :type="currentNotice.noticeType === '1' ? 'warning' : 'danger'">
          {{ currentNotice.noticeType === '1' ? '通知' : '公告' }}
        </el-tag>
        <span style="margin-left:10px;color:#909399;font-size:12px;">{{ (currentNotice.createTime || '').slice(0, 19) }}</span>
      </div>
      <div class="notice-detail-content" v-html="currentNotice.noticeContent"
           style="margin-top:12px;line-height:1.8;color:#303133;"></div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser } from "@/api/system/Libuser"
import { listBorrow } from "@/api/system/borrow"
import { listNotice, getNotice } from "@/api/system/notice"
import { dashboardStats } from "@/api/system/stats"
import * as echarts from "echarts"

export default {
  name: 'Index',
  data() {
    return {
      today: '',
      noticeLoading: false,
      notices: [],
      allNoticeOpen: false,
      allNoticeLoading: false,
      allNotices: [],
      noticeDetailOpen: false,
      currentNotice: {},
      trendData: [],
      hotData: [],
      overdueData: {},
      natureData: [],
      chartObjs: {},
      statCards: [
        { label: '馆藏册数', value: 0, unit: '册', icon: 'el-icon-reading', color: 'linear-gradient(135deg,#d4321f,#ff7029)' },
        { label: '当前在借', value: 0, unit: '册', icon: 'el-icon-edit-outline', color: 'linear-gradient(135deg,#e8771f,#ffb020)' },
        { label: '注册读者', value: 0, unit: '人', icon: 'el-icon-user-solid', color: 'linear-gradient(135deg,#9c2118,#d4452f)' },
        { label: '逾期未还', value: 0, unit: '笔', icon: 'el-icon-warning', color: 'linear-gradient(135deg,#5f1414,#a0261c)' }
      ]
    }
  },
  created() {
    // 读者角色进入读者专属首页，不加载管理端统计
    const roles = this.$store.getters.roles || []
    if (roles.indexOf('reader') !== -1 && roles.indexOf('admin') === -1) {
      this.$router.replace('/readerSelf/index')
      return
    }
    const d = new Date()
    const week = ['日', '一', '二', '三', '四', '五', '六'][d.getDay()]
    this.today = `${d.getFullYear()} 年 ${d.getMonth() + 1} 月 ${d.getDate()} 日 星期${week}`
    this.loadStats()
    this.loadNotices()
    this.loadDashboard()
    window.addEventListener("resize", this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.resizeCharts)
    Object.values(this.chartObjs).forEach(c => { if (c) c.dispose() })
    this.chartObjs = {}
  },
  methods: {
    resizeCharts() {
      Object.values(this.chartObjs).forEach(c => { if (c) c.resize() })
    },
    // 数据看板：一次拉取 趋势/热门/逾期率/馆藏/魔丸灵珠分布
    loadDashboard() {
      dashboardStats().then(res => {
        const d = res.data || {}
        this.trendData = d.trend || []
        this.hotData = d.hotBooks || []
        this.overdueData = d.overdueRate || {}
        this.natureData = d.natureDist || []
        const st = d.stock || {}
        this.statCards[0].value = st.stock_total || 0
        this.$nextTick(() => {
          this.renderTrend()
          this.renderHot()
          this.renderOverdue()
          this.renderNature()
        })
      }).catch(() => {})
    },
    renderTrend() {
      const el = this.$refs.trendChart
      if (!el) return
      const dates = this.trendData.map(x => (x.d || '').slice(5))
      const counts = this.trendData.map(x => x.cnt)
      this.chartObjs.trend = echarts.init(el)
      this.chartObjs.trend.setOption({
        tooltip: { trigger: 'axis', triggerOn: 'mousemove|click', renderMode: 'richText', confine: true },
        grid: { left: 40, right: 20, top: 30, bottom: 30 },
        xAxis: { type: 'category', data: dates, axisLabel: { fontSize: 10 } },
        yAxis: { type: 'value', minInterval: 1 },
        series: [{
          data: counts, type: 'line', smooth: true,
          areaStyle: { color: 'rgba(212,50,31,0.18)' },
          lineStyle: { color: '#d4321f', width: 2 }, itemStyle: { color: '#e8551f' }
        }]
      })
    },
    renderHot() {
      const el = this.$refs.hotChart
      if (!el) return
      const names = this.hotData.map(x => x.bookName).reverse()
      const cnts = this.hotData.map(x => x.borrowCnt).reverse()
      this.chartObjs.hot = echarts.init(el)
      this.chartObjs.hot.setOption({
        tooltip: { trigger: 'axis', triggerOn: 'mousemove|click', renderMode: 'richText', confine: true },
        grid: { left: 10, right: 40, top: 20, bottom: 20, containLabel: true },
        xAxis: { type: 'value', minInterval: 1 },
        yAxis: { type: 'category', data: names, axisLabel: { fontSize: 11 } },
        series: [{
          data: cnts, type: 'bar', barWidth: 16,
          itemStyle: { color: '#e8631c', borderRadius: [0, 4, 4, 0] },
          label: { show: true, position: 'right' }
        }]
      })
    },
    renderOverdue() {
      const el = this.$refs.overdueChart
      if (!el) return
      const active = Number(this.overdueData.active_total) || 0
      const overdue = Number(this.overdueData.overdue) || 0
      const rate = active > 0 ? Math.round(overdue / active * 1000) / 10 : 0
      this.chartObjs.overdue = echarts.init(el)
      this.chartObjs.overdue.setOption({
        tooltip: { trigger: 'item', triggerOn: 'mousemove|click', renderMode: 'richText', confine: true },
        title: {
          text: rate + '%',
          subtext: '逾期率 · 逾期 ' + overdue + ' / 在借 ' + active,
          left: 'center', top: 'center',
          textStyle: { fontSize: 26, color: '#d4321f', fontWeight: 700 },
          subtextStyle: { fontSize: 11, color: '#909399' }
        },
        series: [{
          type: 'pie', radius: ['58%', '78%'], center: ['50%', '52%'],
          label: { show: false },
          data: [
            { value: overdue, name: '逾期' },
            { value: Math.max(active - overdue, 0), name: '未逾期' }
          ],
          itemStyle: { borderColor: '#fff', borderWidth: 2 },
          color: ['#d4321f', '#409eff']
        }]
      })
    },
    renderNature() {
      const el = this.$refs.natureChart
      if (!el) return
      const map = { demon: '魔丸', spirit: '灵珠', half: '半魔丸' }
      const colors = { demon: '#d4321f', spirit: '#1e9e78', half: '#e6a23c' }
      const data = this.natureData.map(x => ({
        value: x.cnt, name: map[x.nature] || x.nature, itemStyle: { color: colors[x.nature] || '#909399' }
      }))
      this.chartObjs.nature = echarts.init(el)
      this.chartObjs.nature.setOption({
        tooltip: { trigger: 'item', triggerOn: 'mousemove|click', renderMode: 'richText', confine: true, formatter: '{b}：{c} 人 ({d}%)' },
        legend: { bottom: 0, itemWidth: 12, itemHeight: 12, textStyle: { fontSize: 12 } },
        series: [{
          type: 'pie', radius: ['42%', '68%'], center: ['50%', '46%'],
          itemStyle: { borderColor: '#fff', borderWidth: 2 },
          label: { formatter: '{b} {d}%', fontSize: 12 },
          data: data
        }]
      })
    },
    loadStats() {
      // 馆藏册数由 loadDashboard 的 stock 接口填充
      // 当前在借 = 借阅中(0) + 逾期未还(2)，均为未归还
      listBorrow({ pageNum: 1, pageSize: 1, borrowStatus: '0' }).then(res => {
        const borrowing = res.total
        listBorrow({ pageNum: 1, pageSize: 1, borrowStatus: '2' }).then(res2 => {
          this.statCards[1].value = borrowing + res2.total
        })
      })
      listUser({ pageNum: 1, pageSize: 1 }).then(res => { this.statCards[2].value = res.total })
      listBorrow({ pageNum: 1, pageSize: 1, borrowStatus: '2' }).then(res => { this.statCards[3].value = res.total })
    },
    loadNotices() {
      this.noticeLoading = true
      listNotice({ pageNum: 1, pageSize: 5 }).then(res => {
        this.notices = res.rows || []
        this.noticeLoading = false
      }).catch(() => { this.noticeLoading = false })
    },
    openAllNotices() {
      this.allNoticeOpen = true
      this.allNoticeLoading = true
      listNotice({ pageNum: 1, pageSize: 200 }).then(res => {
        this.allNotices = res.rows || []
        this.allNoticeLoading = false
      }).catch(() => { this.allNoticeLoading = false })
    },
    viewNotice(n) {
      this.allNoticeOpen = false
      getNotice(n.noticeId).then(res => {
        this.currentNotice = res.data || n
        this.noticeDetailOpen = true
      })
    },
    go(path) {
      this.$router.push(path).catch(() => {})
    }
  }
}
</script>

<style scoped lang="scss">
.home {
  .hero-card {
    border-radius: 10px;
    color: #fff;
    background:
      linear-gradient(100deg, rgba(60,8,8,.86) 0%, rgba(150,26,18,.55) 52%, rgba(255,110,30,.42) 100%),
      url("../assets/images/home-banner.jpg") center center / cover no-repeat;
    box-shadow: 0 8px 24px rgba(150,26,18,.25);
  }
  .hero-inner {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 34px 34px;
    flex-wrap: wrap;
  }
  .hero-text h2 {
    margin: 0 0 10px;
    font-size: 28px;
    font-weight: 700;
    letter-spacing: 2px;
    text-shadow: 0 2px 10px rgba(0,0,0,.45);
  }
  .hero-sub { margin: 0; opacity: .92; font-size: 13px; letter-spacing: 1px; }
  .hero-date {
    opacity: .95; font-size: 13px;
    background: rgba(0,0,0,.25);
    padding: 6px 14px;
    border-radius: 20px;
  }
  .stat-row { margin: 20px 0; }
  .stat-card {
    .el-card__body { padding: 18px; }
    .stat-inner { display: flex; align-items: center; }
    .stat-icon {
      width: 48px; height: 48px; border-radius: 8px;
      display: flex; align-items: center; justify-content: center;
      color: #fff; font-size: 24px; margin-right: 14px;
      box-shadow: 0 4px 10px rgba(180,40,20,.25);
    }
    .stat-value { font-size: 22px; font-weight: 700; color: #303133; line-height: 1.2; }
    .stat-unit { font-size: 12px; color: #909399; margin-left: 4px; font-weight: 400; }
    .stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
  }
  .notice-list, .rank-list { list-style: none; padding: 0; margin: 0; }
  .notice-header { display: flex; align-items: center;
    .el-button { margin-left: auto; }
  }
  .notice-list li {
    display: flex; align-items: center; padding: 10px 0;
    border-bottom: 1px dashed #f5e0de;
    .notice-title { flex: 1; margin: 0 12px; font-size: 13px; color: #303133; }
    .notice-date { color: #909399; font-size: 12px; }
    &.notice-li { cursor: pointer; }
    &.notice-li:hover { background: #fdf6f5; }
    &.notice-li:hover .notice-title { color: #d4321f; }
  }
  .rank-list li {
    display: flex; align-items: center; padding: 10px 0;
    .rank-no {
      width: 22px; height: 22px; line-height: 22px; text-align: center;
      border-radius: 4px; background: #f7e8e6; color: #b36a5f;
      font-size: 12px; margin-right: 12px;
      &.top { background: #d3302b; color: #fff; }
    }
    .rank-book { flex: 1; font-size: 13px; color: #303133; }
    .rank-author { color: #909399; font-size: 12px; margin-left: 6px; }
  }
  .quick-row { button { margin: 0 12px 10px 0; } }
}
</style>

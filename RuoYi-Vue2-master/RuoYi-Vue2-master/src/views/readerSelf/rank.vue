<template>
  <div class="app-container">
    <el-card shadow="never" class="rank-hero" style="margin-bottom:15px;">
      <div style="display:flex;align-items:center;justify-content:space-between;flex-wrap:wrap;gap:10px;">
        <div>
          <div style="font-size:18px;font-weight:700;color:#fff;"><i class="el-icon-trophy"></i> 封神榜 · 功德排行</div>
          <div style="color:rgba(255,255,255,.9);font-size:13px;margin-top:4px;">多读书、按时还，功德圆满者位列仙班；逾期生魔性者沦为魔丸。</div>
        </div>
        <el-button type="warning" plain icon="el-icon-back" @click="$router.push('/readerSelf/index')">返回读者中心</el-button>
      </div>
    </el-card>

    <el-card shadow="never" v-loading="loading">
      <div class="rank-row" v-for="(r, i) in pagedList" :key="r.userId" :class="{ top: globalIndex(i) < 3, me: r.userId === myUserId }">
        <div class="rank-no" :class="medalClass(globalIndex(i))">{{ globalIndex(i) + 1 }}</div>
        <div class="rank-avatar" :class="natureOf(r)">
          <i :class="natureIconOf(r)"></i>
        </div>
        <div class="rank-name">
          {{ r.userName }}
          <el-tag size="mini" v-if="r.userId === myUserId" type="danger" effect="dark" style="margin-left:6px;">我</el-tag>
        </div>
        <el-tag size="small" effect="plain" class="rank-level">{{ levelName(r.level) }}</el-tag>
        <div class="rank-merit">
          <span class="merit-num">{{ r.meritPoints }}</span>
          <span class="merit-unit">功德</span>
        </div>
      </div>
      <div v-if="!loading && rankList.length === 0" style="text-align:center;color:#909399;padding:30px 0;">暂无排行数据</div>
      <el-pagination
        v-if="rankList.length > pageSize"
        style="text-align:center;margin-top:15px;"
        background
        layout="prev, pager, next, total"
        :total="rankList.length"
        :page-size="pageSize"
        :current-page="pageNum"
        @current-change="handlePageChange" />
    </el-card>
  </div>
</template>

<script>
import { getRank, getCultivation } from "@/api/system/reader"

export default {
  name: "ReaderSelfRank",
  data() {
    return {
      loading: true,
      rankList: [],
      myUserId: null,
      pageNum: 1,
      pageSize: 10
    }
  },
  computed: {
    pagedList() {
      const start = (this.pageNum - 1) * this.pageSize
      return this.rankList.slice(start, start + this.pageSize)
    }
  },
  created() {
    getCultivation().then(res => { this.myUserId = res.data ? res.data.userId : null }).catch(() => {})
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      getRank().then(res => {
        this.rankList = res.data || []
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    handlePageChange(page) {
      this.pageNum = page
    },
    globalIndex(i) {
      return (this.pageNum - 1) * this.pageSize + i
    },
    levelName(level) {
      return ["凡人", "道童", "天兵", "仙将", "三坛海会大神", "魔童战神"][level] || "凡人"
    },
    natureOf(r) {
      if ((r.overdueCount || 0) > 0 || (r.unpaidFineCount || 0) > 0 || (r.demonValue || 0) >= 30) return "demon"
      if ((r.demonValue || 0) === 0) return "spirit"
      return "half"
    },
    natureIconOf(r) {
      return { demon: "el-icon-discover", spirit: "el-icon-magic-stick", half: "el-icon-help" }[this.natureOf(r)]
    },
    medalClass(i) {
      return { 0: "gold", 1: "silver", 2: "bronze" }[i] || ""
    }
  }
}
</script>

<style scoped>
.rank-hero {
  background: linear-gradient(100deg, #8c1610 0%, #d4321f 60%, #ff6a1f 100%) !important;
  border: none;
}
.rank-hero >>> .el-card__body { padding: 18px 20px; }
.rank-row {
  display: flex; align-items: center; gap: 14px; padding: 12px 8px;
  border-bottom: 1px dashed #ebeef5;
}
.rank-row:last-child { border-bottom: none; }
.rank-row.top { background: linear-gradient(90deg, #fff7ec, #fff); border-radius: 8px; }
.rank-row.me { box-shadow: inset 3px 0 0 #d4321f; background: #fff8f6; border-radius: 6px; }
.rank-no {
  width: 30px; height: 30px; border-radius: 50%; flex: none;
  display: flex; align-items: center; justify-content: center;
  background: #f0f0f0; color: #909399; font-weight: 700; font-size: 14px;
}
.rank-no.gold { background: linear-gradient(135deg,#ffd76a,#f0a020); color:#fff; }
.rank-no.silver { background: linear-gradient(135deg,#d8dde6,#aab2c0); color:#fff; }
.rank-no.bronze { background: linear-gradient(135deg,#e8a87c,#c07a4a); color:#fff; }
.rank-avatar {
  width: 40px; height: 40px; border-radius: 50%; flex: none;
  display: flex; align-items: center; justify-content: center;
  color: #fff; font-size: 20px;
}
.rank-avatar.demon { background: radial-gradient(circle at 30% 30%, #ff6a3d, #b31e12); }
.rank-avatar.spirit { background: radial-gradient(circle at 30% 30%, #7fe0c0, #1e9e78); }
.rank-avatar.half { background: radial-gradient(circle at 30% 30%, #ffcf6b, #e08a1e); }
.rank-name { flex: 1; font-size: 15px; font-weight: 600; color: #303133; }
.rank-level { flex: none; }
.rank-merit { flex: none; text-align: right; min-width: 90px; }
.merit-num { font-size: 18px; font-weight: 700; color: #e6a23c; }
.merit-unit { font-size: 12px; color: #909399; margin-left: 3px; }
</style>

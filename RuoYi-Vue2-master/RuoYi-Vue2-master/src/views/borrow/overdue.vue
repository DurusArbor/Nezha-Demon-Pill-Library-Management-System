<template>
  <div class="app-container">
    <el-alert title="以下借阅记录已超过应还日期，请尽快通知读者归还并处理滞纳金。" type="warning" :closable="false" style="margin-bottom: 15px" />

    <el-row :gutter="20" class="stat-row">
      <el-col :span="8">
        <el-card shadow="never">
          <div class="stat-item">
            <div class="stat-num" style="color:#b5393a">{{ total }}</div>
            <div class="stat-label">逾期笔数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never">
          <div class="stat-item">
            <div class="stat-num" style="color:#c98a1a">{{ maxOverdueDays }} 天</div>
            <div class="stat-label">最长逾期天数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never">
          <div class="stat-item">
            <div class="stat-num" style="color:#1f4e79">¥ {{ totalFine.toFixed(2) }}</div>
            <div class="stat-label">累计未缴罚金</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <div slot="header">
        <span><i class="el-icon-warning"></i> 逾期明细</span>
      </div>
      <el-table v-loading="loading" :data="overdueList" border stripe>
        <el-table-column label="流水号" prop="borrowId" width="90" align="center" />
        <el-table-column label="图书" min-width="180" show-overflow-tooltip>
          <template slot-scope="scope">{{ bookNameMap[scope.row.bookId] || ('图书#' + scope.row.bookId) }}</template>
        </el-table-column>
        <el-table-column label="读者" width="120">
          <template slot-scope="scope">{{ userNameMap[scope.row.userId] || ('读者#' + scope.row.userId) }}</template>
        </el-table-column>
        <el-table-column label="借出时间" prop="borrowTime" width="110" align="center" />
        <el-table-column label="应还日期" prop="deadlineTime" width="110" align="center" />
        <el-table-column label="逾期天数" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="small" type="danger">{{ overdueDays(scope.row) }} 天</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="应缴罚金" width="110" align="center">
          <template slot-scope="scope">
            <span class="fine-text">¥ {{ fineOf(scope.row).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="warning" icon="el-icon-bell" @click="handleUrge(scope.row)">催缴</el-button>
            <el-button size="mini" type="success" icon="el-icon-check" @click="handleReturn(scope.row)">办理归还</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { listBorrow, doReturn } from "@/api/system/borrow"
import { listBook } from "@/api/system/book"
import { listUser } from "@/api/system/Libuser"
import { urgeRemind } from "@/api/system/remind"

export default {
  name: 'BorrowOverdue',
  data() {
    return {
      loading: true,
      total: 0,
      overdueList: [],
      bookNameMap: {},
      userNameMap: {},
      fineRate: 0.5
    }
  },
  computed: {
    maxOverdueDays() {
      return this.overdueList.length ? Math.max(...this.overdueList.map(r => this.overdueDays(r))) : 0
    },
    totalFine() {
      return this.overdueList.reduce((s, r) => s + this.fineOf(r), 0)
    }
  },
  created() { this.getList() },
  methods: {
    overdueDays(row) {
      if (!row.deadlineTime) return 0
      const due = new Date(row.deadlineTime).getTime()
      const now = Date.now()
      return Math.max(0, Math.floor((now - due) / (1000 * 60 * 60 * 24)))
    },
    // 优先使用系统已生成的未缴罚金金额；尚无罚金时按 0.5 元/天估算
    fineOf(row) {
      if (row.fineAmount != null && Number(row.fineAmount) > 0) {
        return Number(row.fineAmount)
      }
      return this.overdueDays(row) * this.fineRate
    },
    getList() {
      this.loading = true
      Promise.all([
        listBorrow({ pageNum: 1, pageSize: 1000, borrowStatus: '2' }),
        listBook({ pageNum: 1, pageSize: 1000 }),
        listUser({ pageNum: 1, pageSize: 1000 })
      ]).then(([borrowRes, bookRes, userRes]) => {
        this.overdueList = borrowRes.rows || []
        this.total = borrowRes.total
        const bm = {}
        ;(bookRes.rows || []).forEach(b => { bm[b.bookId] = b.bookName })
        this.bookNameMap = bm
        const um = {}
        ;(userRes.rows || []).forEach(u => { um[u.userId] = u.userName })
        this.userNameMap = um
        this.loading = false
      })
    },
    handleUrge(row) {
      const bookName = this.bookNameMap[row.bookId] || ('图书#' + row.bookId)
      this.$modal.confirm('确认向读者「' + (this.userNameMap[row.userId] || ('读者#' + row.userId)) +
        '」发送《' + bookName + '》的催缴通知？发送后读者端会弹出催缴提醒。').then(() => {
        return urgeRemind(row.borrowId)
      }).then(() => {
        this.$modal.msgSuccess('催缴通知已发送')
      }).catch(() => {})
    },
    handleReturn(row) {
      this.$modal.confirm('确认已收到《' + (this.bookNameMap[row.bookId] || ('图书#' + row.bookId)) + '》的归还？' +
        '归还后该笔逾期记录将从「逾期未还」移除，应缴罚金 ¥' + this.fineOf(row).toFixed(2) +
        ' 自动生成（未缴则保留），请到「罚金管理」确认缴纳。').then(() => {
        return doReturn(row.borrowId)
      }).then(() => {
        this.$modal.msgSuccess('归还成功，图书已回库')
        this.getList()
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.stat-row { margin-bottom: 15px; }
.stat-item { text-align: center; padding: 10px 0; }
.stat-num { font-size: 26px; font-weight: 700; }
.stat-label { color: #909399; font-size: 12px; margin-top: 6px; }
.fine-text { color: #b5393a; font-weight: 600; }
</style>

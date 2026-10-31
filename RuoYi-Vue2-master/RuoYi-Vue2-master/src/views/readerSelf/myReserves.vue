<template>
  <div class="app-container">
    <el-alert
      title="预约到书后请在保留期内（系统默认 3 天）办理借阅，超期未借将自动失效。"
      type="info"
      :closable="false"
      style="margin-bottom: 15px"
    />
    <el-table v-loading="loading" :data="reserveList" border stripe>
      <el-table-column label="图书" prop="bookName" min-width="180" show-overflow-tooltip />
      <el-table-column label="作者" prop="bookAuthor" min-width="120" show-overflow-tooltip />
      <el-table-column label="预约时间" prop="reserveTime" width="120" align="center" />
      <el-table-column label="到书保留期至" width="130" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.reserveStatus === '0'" style="color:#909399">到书后起算</span>
          <span v-else>{{ scope.row.expireTime || "—" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag size="small" :type="statusTag(scope.row.reserveStatus)">{{ statusLabel(scope.row.reserveStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.reserveStatus === '1'" size="mini" type="primary" @click="handleBorrow(scope.row)">去借书</el-button>
          <el-button v-if="scope.row.reserveStatus === '0' || scope.row.reserveStatus === '1'" size="mini" type="danger" @click="handleCancel(scope.row)">取消预约</el-button>
          <span v-if="scope.row.reserveStatus === '2' || scope.row.reserveStatus === '3' || scope.row.reserveStatus === '4'" style="color:#c0c4cc">—</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { myReserves, readerBorrow, readerCancelReserve } from "@/api/system/reader"

export default {
  name: "ReaderSelfMyReserves",
  data() {
    return { loading: true, reserveList: [] }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      myReserves().then(res => {
        this.reserveList = res.data || []
        this.loading = false
      })
    },
    handleBorrow(row) {
      this.$modal.confirm("确认借阅已到书的《" + row.bookName + "》？").then(() => readerBorrow(row.bookId)).then(() => {
        this.$modal.msgSuccess("借阅成功，请按时归还")
        this.getList()
      }).catch(() => {})
    },
    handleCancel(row) {
      this.$modal.confirm("确认取消《" + row.bookName + "》的预约？").then(() => readerCancelReserve(row.reserveId)).then(() => {
        this.$modal.msgSuccess("已取消预约")
        this.getList()
      }).catch(() => {})
    },
    statusTag(s) { return { "0": "primary", "1": "success", "2": "info", "3": "info", "4": "danger" }[s] || "" },
    statusLabel(s) { return { "0": "预约中", "1": "已到书待借", "2": "已借出", "3": "已取消", "4": "已过期" }[s] || s }
  }
}
</script>

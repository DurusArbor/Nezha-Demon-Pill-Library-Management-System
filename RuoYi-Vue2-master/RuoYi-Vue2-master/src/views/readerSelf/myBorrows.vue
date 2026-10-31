<template>
  <div class="app-container">
    <el-alert
      title="自助还书与续借：未缴罚金的借阅会在这里提示；续借次数与天数由系统规则（sys_config）控制。"
      type="info"
      :closable="false"
      style="margin-bottom: 15px"
    />
    <el-table v-loading="loading" :data="borrowList" border stripe>
      <el-table-column label="图书" prop="bookName" min-width="180" show-overflow-tooltip />
      <el-table-column label="借出时间" prop="borrowTime" width="120" align="center" />
      <el-table-column label="应还日期" prop="deadlineTime" width="120" align="center">
        <template slot-scope="scope">
          <span :class="{ 'overdue-text': scope.row.borrowStatus === '2' }">{{ scope.row.deadlineTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="归还时间" prop="returnTime" width="120" align="center">
        <template slot-scope="scope">{{ scope.row.returnTime || "—" }}</template>
      </el-table-column>
      <el-table-column label="续借" width="100" align="center">
        <template slot-scope="scope">{{ scope.row.renewCount }} 次</template>
      </el-table-column>
      <el-table-column label="未缴罚金" width="110" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.fineAmount > 0" size="small" type="danger">¥{{ scope.row.fineAmount }}</el-tag>
          <span v-else>—</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90" align="center">
        <template slot-scope="scope">
          <el-tag size="small" :type="statusTag(scope.row.borrowStatus)">{{ statusLabel(scope.row.borrowStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="170" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.borrowStatus === '0'" size="mini" type="primary" @click="handleReturn(scope.row)">还书</el-button>
          <el-button v-if="scope.row.borrowStatus === '0'" size="mini" type="warning" @click="handleRenew(scope.row)">续借</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { myBorrows, readerReturn, readerRenew } from "@/api/system/reader"

export default {
  name: "ReaderSelfMyBorrows",
  data() {
    return { loading: true, borrowList: [] }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      myBorrows().then(res => {
        this.borrowList = res.data || []
        this.loading = false
      })
    },
    handleReturn(row) {
      this.$modal.confirm("确认归还《" + row.bookName + "》？").then(() => readerReturn(row.borrowId)).then(() => {
        this.$modal.msgSuccess("归还成功")
        this.getList()
      }).catch(() => {})
    },
    handleRenew(row) {
      this.$modal.confirm("确认续借《" + row.bookName + "》？").then(() => readerRenew(row.borrowId)).then(() => {
        this.$modal.msgSuccess("续借成功")
        this.getList()
      }).catch(() => {})
    },
    statusTag(s) { return { "0": "primary", "1": "success", "2": "danger" }[s] || "" },
    statusLabel(s) { return { "0": "借阅中", "1": "已归还", "2": "逾期" }[s] || s }
  }
}
</script>

<style scoped>
.overdue-text { color: #b5393a; font-weight: 600; }
</style>

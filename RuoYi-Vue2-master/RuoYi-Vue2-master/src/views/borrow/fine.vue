<template>
  <div class="app-container">
    <el-alert type="info" :closable="false" show-icon style="margin-bottom: 15px;">
      <template slot="title">
        缴纳罚金仅结清欠费金额，<b>不代表图书已归还</b>；首页「逾期未还」笔数以实际还书为准，未归还的图书请到「逾期处理」办理还书。
      </template>
    </el-alert>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="图书名" prop="bookName">
        <el-input v-model="queryParams.bookName" placeholder="请输入图书名" clearable style="width: 200px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="读者姓名" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入读者姓名" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="fineStatus">
        <el-select v-model="queryParams.fineStatus" placeholder="缴纳状态" clearable style="width: 160px">
          <el-option label="未缴" value="0" />
          <el-option label="已缴" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="fineList" border stripe>
      <el-table-column label="罚金号" prop="fineId" width="90" align="center" />
      <el-table-column label="图书" prop="bookName" min-width="170" show-overflow-tooltip />
      <el-table-column label="读者" prop="userName" width="120" show-overflow-tooltip />
      <el-table-column label="逾期天数" prop="overdueDays" width="90" align="center" />
      <el-table-column label="罚金金额" width="100" align="center">
        <template slot-scope="scope">
          <span :class="{ 'fine-text': scope.row.fineStatus === '0' }">¥{{ scope.row.fineAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="归还状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag size="small" :type="scope.row.borrowStatus === '1' ? 'success' : 'danger'">
            {{ scope.row.borrowStatus === '1' ? '已归还' : '未归还' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="生成时间" prop="createTime" width="130" align="center" />
      <el-table-column label="缴纳时间" width="130" align="center">
        <template slot-scope="scope">{{ scope.row.payTime || "—" }}</template>
      </el-table-column>
      <el-table-column label="状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag size="small" :type="scope.row.fineStatus === '0' ? 'danger' : 'success'">
            {{ scope.row.fineStatus === '0' ? '未缴' : '已缴' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.borrowStatus !== '1'" size="mini" type="warning" @click="goReturn">去归还</el-button>
          <el-button v-if="scope.row.fineStatus === '0'" size="mini" type="primary" @click="handlePay(scope.row)">确认缴纳</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listFine, payFine } from "@/api/system/fine"

export default {
  name: "FineManage",
  data() {
    return {
      loading: true,
      total: 0,
      fineList: [],
      queryParams: { pageNum: 1, pageSize: 10, bookName: "", userName: "", fineStatus: "" }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listFine(this.queryParams).then(res => {
        this.fineList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    goReturn() { this.$router.push("/borrow/overdue") },
    handlePay(row) {
      this.$modal.confirm("确认读者「" + row.userName + "」已缴纳《" + row.bookName + "》罚金 ¥" + row.fineAmount + "？").then(() => payFine(row.fineId)).then(() => {
        this.$modal.msgSuccess("已确认缴纳")
        this.getList()
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.fine-text { color: #b5393a; font-weight: 600; }
</style>

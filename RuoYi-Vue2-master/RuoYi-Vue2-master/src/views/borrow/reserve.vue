<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="书名" prop="bookName">
        <el-input v-model="queryParams.bookName" placeholder="请输入书名" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="读者姓名" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入读者姓名" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="reserveStatus">
        <el-select v-model="queryParams.reserveStatus" placeholder="预约状态" clearable style="width: 160px">
          <el-option label="预约中" value="0" />
          <el-option label="已到书待借" value="1" />
          <el-option label="已借出" value="2" />
          <el-option label="已取消" value="3" />
          <el-option label="已过期" value="4" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="reserveList" border stripe>
      <el-table-column label="预约号" prop="reserveId" width="90" align="center" />
      <el-table-column label="图书" prop="bookName" min-width="180" show-overflow-tooltip />
      <el-table-column label="读者" prop="userName" width="130" show-overflow-tooltip />
      <el-table-column label="预约时间" prop="reserveTime" width="130" align="center" />
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
      <el-table-column label="操作" width="110" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.reserveStatus === '0' || scope.row.reserveStatus === '1'" size="mini" type="danger" @click="handleCancel(scope.row)">取消预约</el-button>
          <span v-else style="color:#c0c4cc">—</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listReserve, cancelReserve } from "@/api/system/reserve"

export default {
  name: "ReserveManage",
  data() {
    return {
      loading: true,
      total: 0,
      reserveList: [],
      queryParams: { pageNum: 1, pageSize: 10, bookName: "", userName: "", reserveStatus: "" }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listReserve(this.queryParams).then(res => {
        this.reserveList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleCancel(row) {
      this.$modal.confirm("确认取消读者「" + row.userName + "」对《" + row.bookName + "》的预约？").then(() => cancelReserve(row.reserveId)).then(() => {
        this.$modal.msgSuccess("已取消预约")
        this.getList()
      }).catch(() => {})
    },
    statusTag(s) { return { "0": "primary", "1": "success", "2": "info", "3": "info", "4": "danger" }[s] || "" },
    statusLabel(s) { return { "0": "预约中", "1": "已到书待借", "2": "已借出", "3": "已取消", "4": "已过期" }[s] || s }
  }
}
</script>

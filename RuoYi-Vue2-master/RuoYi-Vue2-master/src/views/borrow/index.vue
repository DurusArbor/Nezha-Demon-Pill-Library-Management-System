<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="书名" prop="bookName">
        <el-input v-model="queryParams.bookName" placeholder="请输入书名" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="读者姓名" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入读者姓名" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="borrowStatus">
        <el-select v-model="queryParams.borrowStatus" placeholder="借阅状态" clearable>
          <el-option label="借阅中" value="0" />
          <el-option label="已归还" value="1" />
          <el-option label="逾期" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">办理借书</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="borrowList" @selection-change="handleSelectionChange" border stripe>
      <el-table-column label="流水号" prop="borrowId" width="90" align="center" />
      <el-table-column label="图书" min-width="180" show-overflow-tooltip>
        <template slot-scope="scope">{{ bookNameMap[scope.row.bookId] || ('图书#' + scope.row.bookId) }}</template>
      </el-table-column>
      <el-table-column label="读者" width="120">
        <template slot-scope="scope">{{ userNameMap[scope.row.userId] || ('读者#' + scope.row.userId) }}</template>
      </el-table-column>
      <el-table-column label="借出时间" prop="borrowTime" width="110" align="center" />
      <el-table-column label="应还日期" prop="deadlineTime" width="110" align="center">
        <template slot-scope="scope">
          <span :class="{ 'overdue-text': scope.row.borrowStatus === '2' }">{{ scope.row.deadlineTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="归还时间" prop="returnTime" width="110" align="center">
        <template slot-scope="scope">{{ scope.row.returnTime || '—' }}</template>
      </el-table-column>
      <el-table-column label="状态" prop="borrowStatus" width="90" align="center">
        <template slot-scope="scope">
          <el-tag size="small" :type="statusTag(scope.row.borrowStatus)">
            {{ statusLabel(scope.row.borrowStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-check" v-if="scope.row.borrowStatus !== '1'" @click="handleReturn(scope.row)">办理归还</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 办理借书 -->
    <el-dialog title="办理借书" :visible.sync="dialog.visible" width="500px" append-to-body>
      <el-alert title="选择读者和图书后提交，系统自动计算应还日期（30天）并扣减库存" type="info" :closable="false" style="margin-bottom:15px" />
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="读者" prop="userId">
          <el-select v-model="form.userId" placeholder="搜索读者姓名" filterable style="width:100%" @change="onUserChange">
            <el-option v-for="u in userOptions" :key="u.userId" :label="u.userName + (u.userStatus==='1' ? '（已冻结）' : '')" :value="u.userId" :disabled="u.userStatus==='1'" />
          </el-select>
        </el-form-item>
        <el-form-item label="图书" prop="bookId">
          <el-select v-model="form.bookId" placeholder="搜索图书名" filterable style="width:100%">
            <el-option v-for="b in bookOptions" :key="b.bookId"
              :label="b.bookName + '（可借 ' + ((b.bookCount||0)-(b.borrowCount||0)) + ' 本）'"
              :value="b.bookId"
              :disabled="(b.borrowCount||0) >= (b.bookCount||0)" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确认借书</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBorrow, delBorrow, doBorrow, doReturn } from "@/api/system/borrow"
import { listBook } from "@/api/system/book"
import { listUser } from "@/api/system/Libuser"

export default {
  name: 'BorrowList',
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      total: 0,
      borrowList: [],
      bookOptions: [],
      userOptions: [],
      bookNameMap: {},
      userNameMap: {},
      queryParams: { pageNum: 1, pageSize: 10, bookName: '', userName: '', borrowStatus: '' },
      dialog: { visible: false },
      form: { userId: null, bookId: null },
      rules: {
        userId: [{ required: true, message: '请选择读者', trigger: 'change' }],
        bookId: [{ required: true, message: '请选择图书', trigger: 'change' }]
      }
    }
  },
  created() { this.loadOptions(); this.getList() },
  methods: {
    loadOptions() {
      listBook({ pageNum: 1, pageSize: 1000 }).then(res => {
        this.bookOptions = res.rows || []
        const m = {}; this.bookOptions.forEach(b => { m[b.bookId] = b.bookName }); this.bookNameMap = m
      })
      listUser({ pageNum: 1, pageSize: 1000 }).then(res => {
        this.userOptions = res.rows || []
        const m = {}; this.userOptions.forEach(u => { m[u.userId] = u.userName }); this.userNameMap = m
      })
    },
    getList() {
      this.loading = true
      listBorrow(this.queryParams).then(response => {
        this.borrowList = response.rows; this.total = response.total; this.loading = false
      })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm('queryForm'); this.handleQuery() },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.borrowId); this.multiple = !selection.length
    },
    handleAdd() {
      this.form = { userId: null, bookId: null }
      this.dialog.visible = true
    },
    onUserChange() {},
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          doBorrow(this.form.userId, this.form.bookId).then(() => {
            this.$modal.msgSuccess('借书成功，应还日期为 30 天后')
            this.dialog.visible = false
            this.loadOptions()
            this.getList()
          }).catch(() => {})
        }
      })
    },
    handleReturn(row) {
      this.$modal.confirm('确认归还这本图书？').then(() => doReturn(row.borrowId)).then(() => {
        this.$modal.msgSuccess('归还成功，库存已回补')
        this.loadOptions(); this.getList()
      }).catch(() => {})
    },
    handleDelete(row) {
      const borrowIds = row.borrowId || this.ids
      this.$modal.confirm('是否确认删除选中的借阅记录？（注意：删除不会自动回补库存，谨慎操作）').then(() => delBorrow(borrowIds)).then(() => {
        this.getList(); this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    statusTag(s) { return { '0': 'primary', '1': 'success', '2': 'danger' }[s] || '' },
    statusLabel(s) { return { '0': '借阅中', '1': '已归还', '2': '逾期' }[s] || s }
  }
}
</script>

<style scoped>
.overdue-text { color: #b5393a; font-weight: 600; }
</style>

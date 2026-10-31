<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="书名" prop="bookName">
        <el-input v-model="queryParams.bookName" placeholder="请输入书名" clearable style="width: 220px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="图书分类" clearable style="width: 160px">
          <el-option v-for="c in categoryOptions" :key="c.categoryId" :label="c.categoryName" :value="c.categoryId" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="bookList" border stripe>
      <el-table-column label="书号" prop="bookId" width="80" align="center" />
      <el-table-column label="书名" prop="bookName" min-width="200" show-overflow-tooltip />
      <el-table-column label="作者" prop="bookAuthor" min-width="120" show-overflow-tooltip />
      <el-table-column label="出版社" prop="bookPublish" min-width="160" show-overflow-tooltip />
      <el-table-column label="馆藏" width="90" align="center">
        <template slot-scope="scope">{{ scope.row.bookCount || 0 }} 本</template>
      </el-table-column>
      <el-table-column label="可借" width="90" align="center">
        <template slot-scope="scope">
          <el-tag v-if="available(scope.row) > 0" size="small" type="success">{{ available(scope.row) }}</el-tag>
          <el-tag v-else size="small" type="info">0</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" :disabled="available(scope.row) <= 0" @click="handleBorrow(scope.row)">借书</el-button>
          <el-button size="mini" type="warning" :disabled="available(scope.row) > 0" @click="handleReserve(scope.row)">预约</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { readerBooks, readerBorrow, readerReserve, readerCategories } from "@/api/system/reader"

export default {
  name: "ReaderSelfBooks",
  data() {
    return {
      loading: true,
      total: 0,
      bookList: [],
      categoryOptions: [],
      queryParams: { pageNum: 1, pageSize: 10, bookName: "", categoryId: "" }
    }
  },
  created() {
    readerCategories().then(res => {
      this.categoryOptions = res.data || []
    })
    this.getList()
  },
  methods: {
    available(row) {
      return (row.bookCount || 0) - (row.borrowCount || 0)
    },
    getList() {
      this.loading = true
      readerBooks(this.queryParams).then(res => {
        this.bookList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleBorrow(row) {
      this.$modal.confirm("确认借阅《" + row.bookName + "》？").then(() => readerBorrow(row.bookId)).then(() => {
        this.$modal.msgSuccess("借阅成功，请按时归还")
        this.getList()
      }).catch(() => {})
    },
    handleReserve(row) {
      this.$modal.confirm("该书已全部借出，确认预约《" + row.bookName + "》？归还后系统会提醒您取书。").then(() => readerReserve(row.bookId)).then(() => {
        this.$modal.msgSuccess("预约成功，归还后我们会通知您")
        this.getList()
      }).catch(() => {})
    }
  }
}
</script>

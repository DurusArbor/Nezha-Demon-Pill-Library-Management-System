<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="书名" prop="bookName">
        <el-input v-model="queryParams.bookName" placeholder="请输入书名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="作者" prop="bookAuthor">
        <el-input v-model="queryParams.bookAuthor" placeholder="请输入作者" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="全部分类" clearable style="width:140px">
          <el-option v-for="c in categoryList" :key="c.categoryId" :label="c.categoryName" :value="c.categoryId" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="bookStatus">
        <el-select v-model="queryParams.bookStatus" placeholder="图书状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="下架" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增图书</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button></el-col>
    </el-row>

    <!-- 表格 -->
    <el-table v-loading="loading" :data="bookList" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="编号" prop="bookId" width="70" align="center" />
      <el-table-column label="书名" prop="bookName" min-width="180" show-overflow-tooltip />
      <el-table-column label="作者" prop="bookAuthor" width="110" />
      <el-table-column label="分类" prop="categoryName" width="100" align="center">
        <template slot-scope="scope">
          <el-tag size="small" type="info">{{ scope.row.categoryName || '未分类' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="ISBN" prop="bookIsbn" width="130" />
      <el-table-column label="馆藏/可借" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.bookCount }} / {{ (scope.row.bookCount||0) - (scope.row.borrowCount||0) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="bookStatus" width="80" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.bookStatus === '0' ? 'success' : 'danger'" size="small">
            {{ scope.row.bookStatus === '0' ? '正常' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialog.title" :visible.sync="dialog.visible" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="书名" prop="bookName"><el-input v-model="form.bookName" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="作者" prop="bookAuthor"><el-input v-model="form.bookAuthor" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="ISBN" prop="bookIsbn"><el-input v-model="form.bookIsbn" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="选择分类" style="width:100%">
                <el-option v-for="c in categoryList" :key="c.categoryId" :label="c.categoryName" :value="c.categoryId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出版社" prop="bookPublisher"><el-input v-model="form.bookPublisher" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出版日期" prop="publishDate">
              <el-date-picker v-model="form.publishDate" type="date" value-format="yyyy-MM-dd" style="width:100%" placeholder="选择出版日期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="馆藏总数" prop="bookCount">
              <el-input-number v-model="form.bookCount" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="bookStatus">
              <el-radio-group v-model="form.bookStatus">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">下架</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBook, getBook, delBook, addBook, updateBook } from "@/api/system/book"
import { categoryOptions } from "@/api/system/category"

export default {
  name: 'BookList',
  data() {
    return {
      loading: true,
      showSearch: true,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      bookList: [],
      categoryList: [],
      queryParams: {
        pageNum: 1, pageSize: 10,
        bookName: '', bookAuthor: '', categoryId: null, bookStatus: ''
      },
      dialog: { visible: false, title: '' },
      form: this.reset(),
      rules: { bookName: [{ required: true, message: '书名为必填项', trigger: 'blur' }] }
    }
  },
  created() { this.getCategory(); this.getList() },
  methods: {
    reset() {
      return {
        bookId: null, bookName: '', bookAuthor: '', bookIsbn: '', bookPublisher: '',
        publishDate: null, bookCount: 1, borrowCount: 0, bookStatus: '0', categoryId: null, remark: ''
      }
    },
    getCategory() {
      categoryOptions().then(r => { this.categoryList = r.rows || [] })
    },
    getList() {
      this.loading = true
      listBook(this.queryParams).then(response => {
        this.bookList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm('queryForm'); this.handleQuery() },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.bookId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() { this.form = this.reset(); this.dialog.title = '新增图书'; this.dialog.visible = true },
    handleUpdate(row) {
      const id = row.bookId || this.ids[0]
      getBook(id).then(response => {
        this.form = response.data
        this.dialog.title = '编辑图书'
        this.dialog.visible = true
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const req = this.form.bookId != null ? updateBook(this.form) : addBook(this.form)
          req.then(() => {
            this.$modal.msgSuccess(this.form.bookId != null ? '修改成功' : '新增成功')
            this.dialog.visible = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const bookIds = row.bookId || this.ids
      this.$modal.confirm('是否确认删除选中的图书？').then(() => delBook(bookIds)).then(() => {
        this.getList(); this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>

</script>

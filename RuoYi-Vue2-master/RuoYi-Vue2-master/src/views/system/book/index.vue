<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="图书名称" prop="bookName">
        <el-input
          v-model="queryParams.bookName"
          placeholder="请输入图书名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作者" prop="bookAuthor">
        <el-input
          v-model="queryParams.bookAuthor"
          placeholder="请输入作者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="ISBN编号" prop="bookIsbn">
        <el-input
          v-model="queryParams.bookIsbn"
          placeholder="请输入ISBN编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出版社" prop="bookPublisher">
        <el-input
          v-model="queryParams.bookPublisher"
          placeholder="请输入出版社"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出版日期" prop="publishDate">
        <el-date-picker clearable
          v-model="queryParams.publishDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择出版日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="馆藏总数量" prop="bookCount">
        <el-input
          v-model="queryParams.bookCount"
          placeholder="请输入馆藏总数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已借出数量" prop="borrowCount">
        <el-input
          v-model="queryParams.borrowCount"
          placeholder="请输入已借出数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:book:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:book:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:book:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:book:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bookList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图书ID" align="center" prop="bookId" />
      <el-table-column label="图书名称" align="center" prop="bookName" />
      <el-table-column label="作者" align="center" prop="bookAuthor" />
      <el-table-column label="ISBN编号" align="center" prop="bookIsbn" />
      <el-table-column label="出版社" align="center" prop="bookPublisher" />
      <el-table-column label="出版日期" align="center" prop="publishDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.publishDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="馆藏总数量" align="center" prop="bookCount" />
      <el-table-column label="已借出数量" align="center" prop="borrowCount" />
      <el-table-column label="状态：0正常，1下架" align="center" prop="bookStatus" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:book:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:book:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改图书信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="图书名称" prop="bookName">
              <el-input v-model="form.bookName" placeholder="请输入图书名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="作者" prop="bookAuthor">
              <el-input v-model="form.bookAuthor" placeholder="请输入作者" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="ISBN编号" prop="bookIsbn">
              <el-input v-model="form.bookIsbn" placeholder="请输入ISBN编号" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="出版社" prop="bookPublisher">
              <el-input v-model="form.bookPublisher" placeholder="请输入出版社" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="出版日期" prop="publishDate">
              <el-date-picker clearable
                v-model="form.publishDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择出版日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="馆藏总数量" prop="bookCount">
              <el-input v-model="form.bookCount" placeholder="请输入馆藏总数量" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="已借出数量" prop="borrowCount">
              <el-input v-model="form.borrowCount" placeholder="请输入已借出数量" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBook, getBook, delBook, addBook, updateBook } from "@/api/system/book"

export default {
  name: "Book",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 图书信息表格数据
      bookList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bookName: null,
        bookAuthor: null,
        bookIsbn: null,
        bookPublisher: null,
        publishDate: null,
        bookCount: null,
        borrowCount: null,
        bookStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        bookName: [
          { required: true, message: "图书名称不能为空", trigger: "blur" }
        ],
        bookCount: [
          { required: true, message: "馆藏总数量不能为空", trigger: "blur" }
        ],
        borrowCount: [
          { required: true, message: "已借出数量不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询图书信息列表 */
    getList() {
      this.loading = true
      listBook(this.queryParams).then(response => {
        this.bookList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        bookId: null,
        bookName: null,
        bookAuthor: null,
        bookIsbn: null,
        bookPublisher: null,
        publishDate: null,
        bookCount: null,
        borrowCount: null,
        bookStatus: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.bookId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加图书信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const bookId = row.bookId || this.ids
      getBook(bookId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改图书信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.bookId != null) {
            updateBook(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addBook(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const bookIds = row.bookId || this.ids
      this.$modal.confirm('是否确认删除图书信息编号为"' + bookIds + '"的数据项？').then(function() {
        return delBook(bookIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/book/export', {
        ...this.queryParams
      }, `book_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

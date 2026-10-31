<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="读者ID(关联lib_user)" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入读者ID(关联lib_user)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图书ID(关联lib_book)" prop="bookId">
        <el-input
          v-model="queryParams.bookId"
          placeholder="请输入图书ID(关联lib_book)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="借出时间" prop="borrowTime">
        <el-date-picker clearable
          v-model="queryParams.borrowTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择借出时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="归还时间，NULL代表未归还" prop="returnTime">
        <el-date-picker clearable
          v-model="queryParams.returnTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择归还时间，NULL代表未归还">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="应还截止日期" prop="deadlineTime">
        <el-date-picker clearable
          v-model="queryParams.deadlineTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择应还截止日期">
        </el-date-picker>
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
          v-hasPermi="['system:borrow:add']"
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
          v-hasPermi="['system:borrow:edit']"
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
          v-hasPermi="['system:borrow:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:borrow:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="borrowList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="借阅记录ID" align="center" prop="borrowId" />
      <el-table-column label="读者ID(关联lib_user)" align="center" prop="userId" />
      <el-table-column label="图书ID(关联lib_book)" align="center" prop="bookId" />
      <el-table-column label="借出时间" align="center" prop="borrowTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.borrowTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="归还时间，NULL代表未归还" align="center" prop="returnTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.returnTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应还截止日期" align="center" prop="deadlineTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deadlineTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态 0借阅中 1已归还 2逾期" align="center" prop="borrowStatus" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:borrow:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:borrow:remove']"
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

    <!-- 添加或修改图书借阅记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="读者ID(关联lib_user)" prop="userId">
              <el-input v-model="form.userId" placeholder="请输入读者ID(关联lib_user)" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="图书ID(关联lib_book)" prop="bookId">
              <el-input v-model="form.bookId" placeholder="请输入图书ID(关联lib_book)" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="借出时间" prop="borrowTime">
              <el-date-picker clearable
                v-model="form.borrowTime"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择借出时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="归还时间，NULL代表未归还" prop="returnTime">
              <el-date-picker clearable
                v-model="form.returnTime"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择归还时间，NULL代表未归还">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="应还截止日期" prop="deadlineTime">
              <el-date-picker clearable
                v-model="form.deadlineTime"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择应还截止日期">
              </el-date-picker>
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
import { listBorrow, getBorrow, delBorrow, addBorrow, updateBorrow } from "@/api/system/borrow"

export default {
  name: "Borrow",
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
      // 图书借阅记录表格数据
      borrowList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        bookId: null,
        borrowTime: null,
        returnTime: null,
        deadlineTime: null,
        borrowStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "读者ID(关联lib_user)不能为空", trigger: "blur" }
        ],
        bookId: [
          { required: true, message: "图书ID(关联lib_book)不能为空", trigger: "blur" }
        ],
        borrowTime: [
          { required: true, message: "借出时间不能为空", trigger: "blur" }
        ],
        deadlineTime: [
          { required: true, message: "应还截止日期不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询图书借阅记录列表 */
    getList() {
      this.loading = true
      listBorrow(this.queryParams).then(response => {
        this.borrowList = response.rows
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
        borrowId: null,
        userId: null,
        bookId: null,
        borrowTime: null,
        returnTime: null,
        deadlineTime: null,
        borrowStatus: null,
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
      this.ids = selection.map(item => item.borrowId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加图书借阅记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const borrowId = row.borrowId || this.ids
      getBorrow(borrowId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改图书借阅记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.borrowId != null) {
            updateBorrow(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addBorrow(this.form).then(response => {
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
      const borrowIds = row.borrowId || this.ids
      this.$modal.confirm('是否确认删除图书借阅记录编号为"' + borrowIds + '"的数据项？').then(function() {
        return delBorrow(borrowIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/borrow/export', {
        ...this.queryParams
      }, `borrow_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="姓名" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="读者姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="电话" prop="userPhone">
        <el-input v-model="queryParams.userPhone" placeholder="联系电话" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="userStatus">
        <el-select v-model="queryParams.userStatus" placeholder="状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="禁用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增读者</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="姓名" prop="userName" width="120" />
      <el-table-column label="联系电话" prop="userPhone" width="140" />
      <el-table-column label="邮箱" prop="userEmail" min-width="180" show-overflow-tooltip />
      <el-table-column label="状态" prop="userStatus" width="90" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.userStatus === '0' ? 'success' : 'danger'" size="small">
            {{ scope.row.userStatus === '0' ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="境界" width="130" align="center">
        <template slot-scope="scope">
          <el-tag size="small" effect="plain" type="warning">{{ levelName(scope.row.cultivationLevel) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="信用" width="100" align="center">
        <template slot-scope="scope">
          <el-tag size="small" :type="natureTag(scope.row).type" effect="dark">{{ natureTag(scope.row).label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="功德/魔性" width="110" align="center">
        <template slot-scope="scope">
          <span style="color:#e6a23c;">{{ scope.row.meritPoints }}</span>
          <span style="color:#c0c4cc;">/</span>
          <span style="color:#c0392b;">{{ scope.row.demonValue }}</span>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" prop="createTime" width="160" align="center" />
      <el-table-column label="备注" prop="remark" show-overflow-tooltip />
      <el-table-column label="操作" width="220" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-user" @click="openProfile(scope.row)">画像</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="dialog.title" :visible.sync="dialog.visible" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="姓名" prop="userName"><el-input v-model="form.userName" /></el-form-item>
        <el-form-item label="联系电话" prop="userPhone"><el-input v-model="form.userPhone" /></el-form-item>
        <el-form-item label="邮箱" prop="userEmail"><el-input v-model="form.userEmail" /></el-form-item>
        <el-form-item label="状态" prop="userStatus">
          <el-radio-group v-model="form.userStatus">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 读者画像抽屉 -->
    <el-drawer :visible.sync="profileDrawer.visible" :size="420" :title="'读者画像 - ' + (profileDrawer.userName || '')" direction="rtl">
      <div v-loading="profileDrawer.loading" style="padding: 0 20px">
        <h4 style="margin:10px 0"><i class="el-icon-collection-tag"></i> 借阅分类偏好</h4>
        <div v-if="profileDrawer.dist.length === 0" style="color:#909399;padding:10px 0">暂无借阅记录</div>
        <div v-for="d in profileDrawer.dist" :key="d.categoryId" style="margin-bottom:12px">
          <div style="display:flex;justify-content:space-between;font-size:13px;margin-bottom:4px">
            <span>{{ d.categoryName }}</span><span>{{ d.cnt }} 本</span>
          </div>
          <el-progress :percentage="Math.round(d.cnt / profileDrawer.total * 100)" :stroke-width="10" :color="'#d3302b'" />
        </div>

        <h4 style="margin:20px 0 10px"><i class="el-icon-star-off"></i> 为你推荐（基于最常借的分类）</h4>
        <div v-if="profileDrawer.recs.length === 0" style="color:#909399">暂无推荐</div>
        <el-card v-for="r in profileDrawer.recs" :key="r.bookId" shadow="never" style="margin-bottom:10px">
          <div style="font-size:14px;font-weight:600">{{ r.bookName }}</div>
          <div style="font-size:12px;color:#909399;margin-top:4px">{{ r.bookAuthor }} · 被借 {{ r.borrowCnt }} 次</div>
        </el-card>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { listUser, getUser, addUser, updateUser, delUser } from "@/api/system/Libuser"
import { readerProfile } from "@/api/system/stats"

export default {
  name: 'ReaderList',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      userList: [],
      queryParams: { pageNum: 1, pageSize: 10, userName: '', userPhone: '', userStatus: '' },
      dialog: { visible: false, title: '' },
      form: this.reset(),
      profileDrawer: { visible: false, loading: false, userName: '', dist: [], recs: [], total: 1 },
      rules: {
        userName: [{ required: true, message: '读者姓名为必填项', trigger: 'blur' }],
        userPhone: [
          { required: true, message: '联系电话为必填项', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$|^0\d{2,3}-?\d{7,8}$/, message: '请输入正确的手机号码或座机号码', trigger: 'blur' }
        ]
      }
    }
  },
  created() { this.getList() },
  methods: {
    reset() {
      return { userId: null, userName: '', userPhone: '', userEmail: '', userStatus: '0', remark: '' }
    },
    levelName(level) {
      return ["凡人", "道童", "天兵", "仙将", "三坛海会大神", "魔童战神"][level] || "凡人"
    },
    natureTag(row) {
      const overdue = row.overdueCount || 0
      const unpaid = row.unpaidFineCount || 0
      const demon = row.demonValue || 0
      if (overdue > 0 || unpaid > 0 || demon >= 30) { return { label: "魔丸", type: "danger" } }
      if (demon === 0) { return { label: "灵珠", type: "success" } }
      return { label: "半魔半仙", type: "warning" }
    },
    getList() {
      this.loading = true
      listUser(this.queryParams).then(response => {
        this.userList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.form = this.reset()
      this.dialog.title = '新增读者'
      this.dialog.visible = true
    },
    handleUpdate(row) {
      const id = row.userId || this.ids[0]
      getUser(id).then(response => {
        this.form = response.data
        this.dialog.title = '编辑读者'
        this.dialog.visible = true
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.userId != null) {
            updateUser(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.dialog.visible = false
              this.getList()
            })
          } else {
            addUser(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.dialog.visible = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const userIds = row.userId || this.ids
      this.$modal.confirm('是否确认删除读者编号为"' + userIds + '"的数据项？').then(() => {
        return delUser(userIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    openProfile(row) {
      this.profileDrawer = { visible: true, loading: true, userName: row.userName, dist: [], recs: [], total: 1 }
      readerProfile(row.userId).then(res => {
        const data = res.data || {}
        const dist = data.categoryDist || []
        this.profileDrawer.dist = dist
        this.profileDrawer.recs = data.recommendations || []
        this.profileDrawer.total = dist.reduce((s, x) => s + (x.cnt || 0), 1)
        this.profileDrawer.loading = false
      }).catch(() => { this.profileDrawer.loading = false })
    }
  }
}
</script>

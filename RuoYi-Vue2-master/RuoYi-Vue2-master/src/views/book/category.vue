<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增分类</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange" border stripe>
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="ID" prop="categoryId" width="80" align="center" />
      <el-table-column label="分类名称" prop="categoryName" min-width="160" />
      <el-table-column label="描述" prop="description" min-width="240" show-overflow-tooltip />
      <el-table-column label="排序" prop="sortNum" width="80" align="center" />
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialog.title" :visible.sync="dialog.visible" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="分类名称" prop="categoryName"><el-input v-model="form.categoryName" /></el-form-item>
        <el-form-item label="描述" prop="description"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item label="排序" prop="sortNum"><el-input-number v-model="form.sortNum" :min="0" /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCategory, addCategory, updateCategory, delCategory } from "@/api/system/category"

export default {
  name: 'CategoryList',
  data() {
    return {
      loading: true, list: [], ids: [], multiple: true,
      dialog: { visible: false, title: '' },
      form: { categoryId: null, categoryName: '', description: '', sortNum: 0 },
      rules: { categoryName: [{ required: true, message: '分类名必填', trigger: 'blur' }] }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listCategory({ pageNum: 1, pageSize: 100 }).then(res => {
        this.list = res.rows || []; this.loading = false
      })
    },
    handleSelectionChange(s) { this.ids = s.map(x => x.categoryId); this.multiple = !s.length },
    handleAdd() { this.form = { categoryId: null, categoryName: '', description: '', sortNum: 0 }; this.dialog.title = '新增分类'; this.dialog.visible = true },
    handleUpdate(row) { this.form = { ...row }; this.dialog.title = '编辑分类'; this.dialog.visible = true },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const req = this.form.categoryId ? updateCategory(this.form) : addCategory(this.form)
        req.then(() => { this.$modal.msgSuccess('保存成功'); this.dialog.visible = false; this.getList() })
      })
    },
    handleDelete(row) {
      const ids = row.categoryId || this.ids
      this.$modal.confirm('确认删除选中分类？').then(() => delCategory(ids)).then(() => {
        this.getList(); this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>

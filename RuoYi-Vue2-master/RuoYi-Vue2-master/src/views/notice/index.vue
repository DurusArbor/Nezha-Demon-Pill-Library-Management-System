<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd">新增公告</el-button>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <div slot="header" class="notice-header">
        <span><i class="el-icon-document"></i> 全部图书馆公告（共 {{ noticeList.length }} 条）</span>
      </div>
      <div v-loading="noticeLoading">
        <div class="notice-item" v-for="n in noticeList" :key="n.noticeId">
          <el-tag size="mini" :type="n.noticeType === '1' ? 'warning' : 'danger'" class="notice-tag"
                  @click.stop="viewNotice(n)">
            {{ n.noticeType === '1' ? '通知' : '公告' }}
          </el-tag>
          <span class="notice-title" :class="{ 'title-off': n.status === '1' }" @click="viewNotice(n)">{{ n.noticeTitle }}</span>
          <el-tag v-if="n.status === '1'" size="mini" type="info" class="notice-tag">已关闭</el-tag>
          <span class="notice-ops">
            <el-button type="text" size="mini" icon="el-icon-edit" @click.stop="handleUpdate(n)"></el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" class="op-del" @click.stop="handleDelete(n)"></el-button>
          </span>
          <span class="notice-time">{{ parseTime(n.createTime, '{y}-{m}-{d}') }}</span>
        </div>
        <div v-if="!noticeLoading && noticeList.length === 0" class="notice-empty">暂无公告</div>
      </div>
    </el-card>

    <!-- 详情 -->
    <el-dialog :title="currentNotice.noticeTitle" :visible.sync="detailOpen" width="640px" append-to-body>
      <div class="notice-detail-meta">
        <el-tag size="mini" :type="currentNotice.noticeType === '1' ? 'warning' : 'danger'">
          {{ currentNotice.noticeType === '1' ? '通知' : '公告' }}
        </el-tag>
        <span style="margin-left:10px;color:#909399;font-size:12px;">{{ parseTime(currentNotice.createTime) }}</span>
      </div>
      <div class="notice-detail-content" v-html="currentNotice.noticeContent"
           style="margin-top:12px;line-height:1.8;color:#303133;"></div>
    </el-dialog>

    <!-- 新增/修改 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body @closed="reset">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="公告标题" prop="noticeTitle">
          <el-input v-model="form.noticeTitle" placeholder="请输入公告标题" maxlength="50" />
        </el-form-item>
        <el-form-item label="类型" prop="noticeType">
          <el-radio-group v-model="form.noticeType">
            <el-radio label="1">通知</el-radio>
            <el-radio label="2">公告</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常（读者可见）</el-radio>
            <el-radio label="1">关闭（读者不可见）</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内容" prop="noticeContent">
          <editor v-model="form.noticeContent" :min-height="200" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listNotice, getNotice, addNotice, updateNotice, delNotice } from '@/api/system/notice'

export default {
  name: 'NoticeManage',
  data() {
    return {
      noticeLoading: false,
      noticeList: [],
      detailOpen: false,
      currentNotice: {},
      open: false,
      title: '',
      form: {},
      rules: {
        noticeTitle: [{ required: true, message: '公告标题不能为空', trigger: 'blur' }],
        noticeType: [{ required: true, message: '请选择类型', trigger: 'change' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }],
        noticeContent: [{ required: true, message: '公告内容不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.noticeLoading = true
      listNotice({ pageNum: 1, pageSize: 200 }).then(res => {
        this.noticeList = res.rows || []
        this.noticeLoading = false
      }).catch(() => { this.noticeLoading = false })
    },
    viewNotice(n) {
      getNotice(n.noticeId).then(res => {
        this.currentNotice = res.data || n
        this.detailOpen = true
      })
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增公告'
    },
    handleUpdate(n) {
      getNotice(n.noticeId).then(res => {
        this.form = {
          noticeId: res.data.noticeId,
          noticeTitle: res.data.noticeTitle,
          noticeType: res.data.noticeType,
          status: res.data.status,
          noticeContent: res.data.noticeContent
        }
        this.open = true
        this.title = '修改公告'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const action = this.form.noticeId != null ? updateNotice(this.form) : addNotice(this.form)
        action.then(() => {
          this.$modal.msgSuccess(this.form.noticeId != null ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(n) {
      this.$modal.confirm('确认删除公告「' + n.noticeTitle + '」？删除后读者端也将不再显示。').then(() => {
        return delNotice(n.noticeId)
      }).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.getList()
      }).catch(() => {})
    },
    reset() {
      this.form = {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: '1',
        status: '0',
        noticeContent: undefined
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    }
  }
}
</script>

<style scoped>
.notice-header { display: flex; align-items: center; justify-content: space-between; }
.notice-item {
  display: flex; align-items: center; padding: 10px 4px;
  border-bottom: 1px dashed #ebeef5;
}
.notice-item:hover .notice-ops { opacity: 1; }
.notice-item:last-child { border-bottom: none; }
.notice-tag { flex: none; cursor: pointer; }
.notice-title {
  flex: 1; margin: 0 12px; color: #303133; font-size: 14px; cursor: pointer;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}
.notice-title:hover { color: #d4321f; }
.title-off { color: #b0b3b8; }
.notice-ops { flex: none; opacity: 0; transition: opacity .2s; }
.op-del { color: #f56c6c; }
.notice-time { flex: none; color: #909399; font-size: 12px; margin-left: 8px; }
.notice-empty { text-align: center; color: #909399; padding: 20px 0; font-size: 13px; }
.notice-detail-content >>> p { margin: 0 0 8px; }
</style>

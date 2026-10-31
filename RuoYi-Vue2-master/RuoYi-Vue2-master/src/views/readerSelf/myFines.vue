<template>
  <div class="app-container">
    <el-alert
      title="图书逾期即触犯天条、降下天劫：按 0.5 香火钱/天计罚。请速归还并缴纳香火钱，以消天劫、净化魔性。"
      type="warning"
      :closable="false"
      style="margin-bottom: 15px"
    />
    <el-table v-loading="loading" :data="fineList" border stripe>
      <el-table-column label="图书" prop="bookName" min-width="170" show-overflow-tooltip />
      <el-table-column label="触犯天条（天）" prop="overdueDays" width="120" align="center" />
      <el-table-column label="天劫（香火钱）" width="130" align="center">
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
      <el-table-column label="天劫状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag size="small" :type="scope.row.fineStatus === '0' ? 'danger' : 'success'">
            {{ scope.row.fineStatus === '0' ? '天劫未解' : '天劫已消' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="110" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.fineStatus === '0'" size="mini" type="primary" icon="el-icon-money" @click="openPay(scope.row)">
            去缴纳
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 仿支付收银台 -->
    <el-dialog title="罚金缴纳 · 收银台" :visible.sync="payDialogVisible" width="380px" :close-on-click-modal="false" append-to-body>
      <div class="pay-body">
        <div class="pay-amount">
          <div class="pay-amount-label">{{ payRow.bookName }}</div>
          <div class="pay-amount-value"><span class="pay-rmb">¥</span>{{ payRow.fineAmount }}</div>
        </div>
        <div v-if="forgiveTokens > 0" class="pay-token-tip">
          <i class="el-icon-tickets"></i> 持有免罚令 {{ forgiveTokens }} 张，本次缴纳将自动抵消，无需支付
        </div>
        <div class="pay-methods">
          <div class="pay-method" :class="{ active: payMethod === 'wechat' }" @click="switchMethod('wechat')">
            <i class="wechat-icon"></i>微信支付
          </div>
          <div class="pay-method" :class="{ active: payMethod === 'alipay' }" @click="switchMethod('alipay')">
            <i class="alipay-icon"></i>支付宝
          </div>
        </div>
        <div class="pay-qr">
          <svg class="qr-svg" :class="payMethod" viewBox="0 0 21 21" :style="{ width: '150px', height: '150px' }">
            <rect v-for="(cell, i) in qrCells" :key="i" :x="cell.c" :y="cell.r" width="1" height="1"
              :fill="cell.on ? '#000' : '#fff'"></rect>
          </svg>
          <div class="qr-tip">请使用{{ payMethod === 'wechat' ? '微信' : '支付宝' }}扫码支付（演示环境）</div>
        </div>
      </div>
      <div slot="footer">
        <el-button size="small" @click="payDialogVisible = false">取消</el-button>
        <el-button size="small" type="primary" icon="el-icon-check" :loading="paying" @click="handlePaid">我已支付</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { myFines, readerPayFine, getCultivation } from "@/api/system/reader"

export default {
  name: "ReaderSelfMyFines",
  data() {
    return {
      loading: true,
      fineList: [],
      payDialogVisible: false,
      payRow: {},
      payMethod: "wechat",
      paying: false,
      qrCells: [],
      forgiveTokens: 0
    }
  },
  created() { this.getList(); this.loadTokens() },
  methods: {
    // 读取持有免罚令数量（缴罚金时自动抵消）
    loadTokens() {
      getCultivation().then(res => {
        const d = res.data || {}
        this.forgiveTokens = Number(d.forgiveTokens) || 0
      }).catch(() => {})
    },
    getList() {
      this.loading = true
      myFines().then(res => {
        this.fineList = res.data || []
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    // 打开收银台（生成仿二维码）
    openPay(row) {
      this.payRow = row
      this.payMethod = "wechat"
      this.paying = false
      this.genQr()
      this.payDialogVisible = true
    },
    switchMethod(m) {
      this.payMethod = m
      this.genQr()
    },
    // 生成 21x21 仿二维码：三个定位角 + 随机模块（演示用，扫码无效）
    genQr() {
      const n = 21
      const cells = []
      for (let r = 0; r < n; r++) {
        for (let c = 0; c < n; c++) {
          cells.push({ r: r, c: c, on: this.qrBit(r, c, n) })
        }
      }
      this.qrCells = cells
    },
    qrBit(r, c, n) {
      const finder = (rr, cc) => {
        const edge = rr === 0 || rr === 6 || cc === 0 || cc === 6
        const core = rr >= 2 && rr <= 4 && cc >= 2 && cc <= 4
        return edge || core
      }
      if (r < 7 && c < 7) return finder(r, c)
      if (r < 7 && c >= n - 7) return finder(r, c - (n - 7))
      if (r >= n - 7 && c < 7) return finder(r - (n - 7), c)
      return Math.random() < 0.45
    },
    // 模拟支付成功：调用现有接口标记已缴
    handlePaid() {
      this.paying = true
      readerPayFine(this.payRow.fineId).then(res => {
        this.paying = false
        this.payDialogVisible = false
        this.$modal.msgSuccess(res.msg || "支付成功，天劫已消！")
        this.getList()
      }).catch(() => { this.paying = false })
    }
  }
}
</script>

<style scoped>
.fine-text { color: #b5393a; font-weight: 600; }
.pay-body { padding: 0 6px; }
.pay-amount { text-align: center; padding: 6px 0 10px; border-bottom: 1px dashed #dcdfe6; }
.pay-amount-label { font-size: 13px; color: #606266; margin-bottom: 6px; }
.pay-amount-value { font-size: 30px; font-weight: 700; color: #303133; }
.pay-rmb { font-size: 20px; margin-right: 2px; }
.pay-token-tip { margin: 10px 0 0; padding: 8px 10px; background: #fdf6ec; border: 1px solid #f5dab1;
  border-radius: 6px; color: #b5821a; font-size: 12px; text-align: center; }
.pay-methods { display: flex; gap: 10px; margin: 14px 0; justify-content: center; }
.pay-method { flex: 1; max-width: 140px; display: flex; align-items: center; justify-content: center; gap: 6px;
  padding: 8px 0; border: 1px solid #dcdfe6; border-radius: 6px; cursor: pointer;
  font-size: 13px; color: #606266; user-select: none; }
.pay-method.active { border-color: #409eff; color: #409eff; background: #ecf5ff; }
.wechat-icon { width: 18px; height: 18px; border-radius: 4px; background: #07c160; }
.alipay-icon { width: 18px; height: 18px; border-radius: 4px; background: #1677ff; }
.pay-qr { display: flex; flex-direction: column; align-items: center; gap: 8px; }
.qr-svg { border: 1px solid #e4e7ed; border-radius: 6px; background: #fff; padding: 4px; box-sizing: border-box; }
.qr-tip { font-size: 12px; color: #909399; }
</style>

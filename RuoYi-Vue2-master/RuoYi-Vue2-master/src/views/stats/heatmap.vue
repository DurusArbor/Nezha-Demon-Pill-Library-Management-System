<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header"><i class="el-icon-data-analysis"></i> 借阅热力图（按周几 × 时段）</div>
      <p style="color:#909399;font-size:13px;margin-top:0">颜色越深表示该时段借书量越大，可用于安排馆员值班与排课。</p>
      <div ref="heatmapChart" style="height:380px"></div>
    </el-card>
  </div>
</template>

<script>
import { borrowHeatmap } from "@/api/system/stats"
import * as echarts from "echarts"

export default {
  name: 'BorrowHeatmap',
  created() { this.load() },
  methods: {
    load() {
      borrowHeatmap().then(res => {
        const rows = res.data || []
        // echarts heatmap 需要 [x, y, value]
        const data = rows.map(r => [r.hour, r.weekday, r.cnt])
        const max = rows.reduce((m, r) => Math.max(m, r.cnt), 0)
        this.$nextTick(() => {
          const c = echarts.init(this.$refs.heatmapChart)
          c.setOption({
            tooltip: {
              formatter: p => {
                const days = ['周日','周一','周二','周三','周四','周五','周六']
                return `${days[p.value[1]]} ${p.value[0]}:00<br/>借书 ${p.value[2]} 次`
              }
            },
            grid: { left: 60, right: 30, top: 30, bottom: 60 },
            xAxis: { type: 'category', name: '小时', data: Array.from({length:24}, (_,i)=>i+':00') },
            yAxis: { type: 'category', data: ['周日','周一','周二','周三','周四','周五','周六'] },
            visualMap: {
              min: 0, max: max || 1, calculable: true, orient: 'horizontal',
              left: 'center', bottom: 0,
              inRange: { color: ['#e8f0f8', '#90b4d6', '#3d6fa0', '#1f4e79'] }
            },
            series: [{
              type: 'heatmap', data: data,
              label: { show: true, fontSize: 10 },
              emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.3)' } }
            }]
          })
        })
      })
    }
  }
}
</script>

import request from '@/utils/request'

// 首页数据看板（借阅趋势/热门图书/逾期率/魔丸灵珠分布）
export function dashboardStats() {
  return request({ url: '/system/stats/dashboard', method: 'get' })
}

// 近30天借阅趋势
export function borrowTrend() {
  return request({ url: '/system/stats/borrowTrend', method: 'get' })
}

// 热门图书 Top5
export function hotBooks() {
  return request({ url: '/system/stats/hotBooks', method: 'get' })
}

// 借阅热力图
export function borrowHeatmap() {
  return request({ url: '/system/stats/heatmap', method: 'get' })
}

// 读者画像
export function readerProfile(userId) {
  return request({ url: '/system/stats/readerProfile/' + userId, method: 'get' })
}
